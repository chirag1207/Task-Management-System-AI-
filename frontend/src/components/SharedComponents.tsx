import React, { useState } from 'react';
import { formatDistanceToNow, isPast } from 'date-fns';

export const KPICard = ({ title, value, icon, trend, danger = false }: { title: string, value: string | number, icon: string, trend?: string, danger?: boolean }) => (
  <div className={`kpi-card ${danger ? 'danger-card' : ''}`}>
    <div className="kpi-header">
      <span className="kpi-title">{title}</span>
      <span className="kpi-icon">{icon}</span>
    </div>
    <div className="kpi-value">{value}</div>
    {trend && <div className="kpi-trend" style={{ color: danger ? 'var(--danger)' : 'var(--accent)' }}>{trend}</div>}
  </div>
);

export const TaskTable = ({ tasks, title, onTaskClick }: { tasks: any[], title: string, onTaskClick?: (task: any) => void }) => (
  <div className="card task-list">
    <h2>{title}</h2>
    <div className="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Status</th>
            <th>Priority</th>
            <th>Due Date</th>
            <th>Assignee</th>
            <th>Department</th>
            <th>Risk</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map(t => {
            const isOverdue = t.dueDate && isPast(new Date(t.dueDate)) && t.status !== 'COMPLETED';
            return (
              <tr key={t.id} onClick={() => onTaskClick && onTaskClick(t)} style={{ cursor: onTaskClick ? 'pointer' : 'default' }}>
                <td>#{t.id}</td>
                <td className="task-title-cell">{t.title}</td>
                <td><span className={`status-badge ${t.status.toLowerCase()}`}>{t.status}</span></td>
                <td><span className={`priority-badge ${t.priority?.toLowerCase() || 'medium'}`}>{t.priority || 'MEDIUM'}</span></td>
                <td style={{ color: isOverdue ? 'var(--danger)' : 'var(--text-muted)' }}>
                  {t.dueDate ? formatDistanceToNow(new Date(t.dueDate), { addSuffix: true }) : 'No Due Date'}
                </td>
                <td>{t.assignee?.name || 'Unassigned'}</td>
                <td>{t.department?.name || 'N/A'}</td>
                <td>
                  {t.aiAnalysis ? <span className="ai-risk-badge" title={t.aiAnalysis}>🤖 AI</span> : (isOverdue ? '🔴 High Risk' : '🟢 Low Risk')}
                </td>
              </tr>
            );
          })}
          {tasks.length === 0 && <tr><td colSpan={8} className="text-center">No tasks available.</td></tr>}
        </tbody>
      </table>
    </div>
  </div>
);

// New Component: Task Detail Modal
export const TaskDetailModal = ({ task, onClose, onAddComment }: { task: any, onClose: () => void, onAddComment: (content: string) => void }) => {
  const [commentText, setCommentText] = useState('');

  if (!task) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Task #{task.id}: {task.title}</h2>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>
        <div className="modal-body">
          <div className="task-meta-grid">
            <div><strong>Status:</strong> {task.status}</div>
            <div><strong>Priority:</strong> {task.priority}</div>
            <div><strong>Assignee:</strong> {task.assignee?.name || 'Unassigned'}</div>
            <div><strong>Due Date:</strong> {task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'None'}</div>
          </div>
          
          <div className="task-description">
            <h3>Description</h3>
            <p>{task.description || 'No description provided.'}</p>
          </div>

          <div className="ai-analysis-panel">
            <h3>🤖 AI Risk Analysis</h3>
            <p>{task.aiAnalysis || 'Waiting for next cron cycle...'}</p>
          </div>

          <div className="comments-section">
            <h3>Activity & Comments</h3>
            <div className="comment-list">
              {task.comments && task.comments.length > 0 ? (
                task.comments.map((c: any) => (
                  <div key={c.id} className="comment-item">
                    <div className="comment-author">{c.author?.name} <span>{formatDistanceToNow(new Date(c.createdAt))} ago</span></div>
                    <div className="comment-text">{c.content}</div>
                  </div>
                ))
              ) : <p className="text-muted">No comments yet.</p>}
            </div>
            <div className="add-comment">
              <input 
                value={commentText} 
                onChange={e => setCommentText(e.target.value)} 
                placeholder="Add an update..." 
                onKeyDown={e => {
                  if(e.key === 'Enter' && commentText.trim()) {
                    onAddComment(commentText);
                    setCommentText('');
                  }
                }}
              />
              <button className="btn-primary" onClick={() => {
                if(commentText.trim()) {
                  onAddComment(commentText);
                  setCommentText('');
                }
              }}>Post</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
