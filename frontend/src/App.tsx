import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { KPICard, TaskTable, TaskDetailModal } from './components/SharedComponents';
import { AuthPage } from './components/Auth';
import './index.css';

// Type Definitions
type Role = 'OWNER' | 'MANAGER' | 'EMPLOYEE';

const TaskSchema = z.object({
  title: z.string().min(3, 'Title is too short'),
  priority: z.enum(['LOW', 'MEDIUM', 'HIGH']).default('MEDIUM'),
});
type TaskFormValues = z.infer<typeof TaskSchema>;

const DashboardWrapper = ({ user, setGlobalUser }: { user: any, setGlobalUser: any }) => {
  const [activeRole, setActiveRole] = useState<Role>(user?.role || 'OWNER');
  const [tasks, setTasks] = useState<any[]>([]);
  const [selectedTask, setSelectedTask] = useState<any>(null);
  const [toasts, setToasts] = useState<string[]>([]);
  const navigate = useNavigate();
  
  const { register, handleSubmit, reset, formState: { errors } } = useForm<TaskFormValues>({ resolver: zodResolver(TaskSchema) });

  useEffect(() => {
    if (!user) { navigate('/'); return; }
    setActiveRole(user.role);
    fetchTasks();

    // WebSocket Setup
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-alerts'),
      onConnect: () => {
        client.subscribe('/topic/alerts', (msg) => {
          const body = JSON.parse(msg.body);
          setToasts(prev => [...prev, body.message]);
          setTimeout(() => setToasts(prev => prev.slice(1)), 5000);
          fetchTasks(); // Refresh data on alert
        });
      },
    });
    client.activate();
    return () => { client.deactivate(); };
  }, [user, navigate]);

  const fetchTasks = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/tasks');
      setTasks(res.data);
    } catch (e) {
      console.log('Using dummy data since backend might be offline');
      if (tasks.length === 0) {
        setTasks([{ id: 1, title: 'Mock Task', status: 'OPEN', priority: 'HIGH', dueDate: new Date().toISOString() }]);
      }
    }
  };

  const onSubmit = async (data: TaskFormValues) => {
    try {
      await axios.post('http://localhost:8080/api/tasks', { ...data, status: 'OPEN' });
      fetchTasks(); reset();
    } catch (e) { console.error(e); }
  };

  const handleTaskClick = async (task: any) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/tasks/${task.id}/comments`);
      setSelectedTask({ ...task, comments: res.data });
    } catch (e) {
      setSelectedTask({ ...task, comments: [] });
    }
  };

  const handleAddComment = async (content: string) => {
    if(!selectedTask) return;
    try {
      const res = await axios.post(`http://localhost:8080/api/tasks/${selectedTask.id}/comments`, { authorId: user.id, content });
      setSelectedTask({ ...selectedTask, comments: [res.data, ...selectedTask.comments] });
    } catch(e) { console.error("Could not post comment", e); }
  };

  const handleLogout = () => { setGlobalUser(null); navigate('/'); };

  const renderOwnerDashboard = () => (
    <>
      <div className="header"><h1>Global Overview</h1></div>
      <div className="kpi-grid">
        <KPICard title="Total Tasks" value={tasks.length} icon="📋" trend="+12% from last week" />
        <KPICard title="Departments" value={4} icon="🏢" />
        <KPICard title="Critical Risks" danger={true} value={tasks.filter(t => t.aiAnalysis?.includes('CRITICAL') || (t.dueDate && new Date(t.dueDate) < new Date() && t.status !== 'COMPLETED')).length} icon="⚠️" trend="Action Required" />
        <KPICard title="Completion Rate" value={`${Math.round((tasks.filter(t => t.status === 'COMPLETED').length / (tasks.length || 1)) * 100)}%`} icon="📈" />
      </div>
      <div className="dashboard-grid">
        <TaskTable tasks={tasks} title="All Company Tasks" onTaskClick={handleTaskClick} />
        <div className="card">
          <h2>Create Global Task</h2>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="form-group"><input placeholder="Enter title..." {...register('title')} /></div>
            <div className="form-group"><select {...register('priority')}><option value="LOW">Low</option><option value="MEDIUM">Medium</option><option value="HIGH">High</option></select></div>
            <button type="submit" className="btn-primary">Assign Task</button>
          </form>
        </div>
      </div>
    </>
  );

  return (
    <div className="dashboard-container">
      <aside className="sidebar">
        <div className="brand">Smartoperation</div>
        <div className="user-profile-widget">
           <div className="avatar">{user?.name?.charAt(0) || 'U'}</div>
           <div className="user-info"><strong>{user?.name || 'User'}</strong><span>{user?.role || 'Role'}</span></div>
        </div>
        <div className="role-selector">
          <label style={{color: 'var(--text-muted)', fontSize: '0.75rem'}}>SIMULATE ROLE</label>
          <button className={`role-btn ${activeRole === 'OWNER' ? 'active' : ''}`} onClick={() => setActiveRole('OWNER')}>👑 Owner</button>
          <button className={`role-btn ${activeRole === 'MANAGER' ? 'active' : ''}`} onClick={() => setActiveRole('MANAGER')}>👔 Manager</button>
          <button className={`role-btn ${activeRole === 'EMPLOYEE' ? 'active' : ''}`} onClick={() => setActiveRole('EMPLOYEE')}>💻 Employee</button>
        </div>
        <button className="logout-btn" onClick={handleLogout}>Log Out</button>
      </aside>
      
      <main className="main-content">
        {activeRole === 'OWNER' && renderOwnerDashboard()}
        {activeRole === 'MANAGER' && renderOwnerDashboard() /* Mocking for simplicity */}
        {activeRole === 'EMPLOYEE' && renderOwnerDashboard() /* Mocking for simplicity */}
      </main>

      <TaskDetailModal task={selectedTask} onClose={() => setSelectedTask(null)} onAddComment={handleAddComment} />
      
      <div className="toast-container">
        {toasts.map((t, i) => <div key={i} className="toast">{t}</div>)}
      </div>
    </div>
  );
};

function App() {
  const [globalUser, setGlobalUser] = useState<any>(null);
  return (
    <Router>
      <Routes>
        <Route path="/" element={<AuthPage setGlobalUser={setGlobalUser} />} />
        <Route path="/dashboard" element={<DashboardWrapper user={globalUser} setGlobalUser={setGlobalUser} />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;
