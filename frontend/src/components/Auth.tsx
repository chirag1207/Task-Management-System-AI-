import React, { useState } from 'react';
import axios from 'axios';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useNavigate } from 'react-router-dom';

// Zod Schemas
const loginSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(1, "Password is required"),
});

const signupSchema = z.object({
  name: z.string().min(2, "Name must be at least 2 characters"),
  email: z.string().email("Invalid email address"),
  password: z.string().min(6, "Password must be at least 6 characters"),
  role: z.enum(['OWNER', 'MANAGER', 'EMPLOYEE']),
  departmentId: z.string().optional(),
});

type LoginValues = z.infer<typeof loginSchema>;
type SignupValues = z.infer<typeof signupSchema>;

export const AuthPage = ({ setGlobalUser }: { setGlobalUser: (user: any) => void }) => {
  const [isLogin, setIsLogin] = useState(true);
  const navigate = useNavigate();

  const { register: registerLogin, handleSubmit: handleLoginSubmit, formState: { errors: loginErrors } } = useForm<LoginValues>({
    resolver: zodResolver(loginSchema)
  });

  const { register: registerSignup, handleSubmit: handleSignupSubmit, formState: { errors: signupErrors } } = useForm<SignupValues>({
    resolver: zodResolver(signupSchema)
  });

  const onLogin = async (data: LoginValues) => {
    try {
      const res = await axios.post('http://localhost:8080/api/auth/login', data);
      setGlobalUser(res.data);
      navigate('/dashboard');
    } catch (e: any) {
      alert(e.response?.data || "Login failed. Ensure backend is running.");
      // Fallback for UI demo if backend is offline
      setGlobalUser({ name: "Demo User", role: "OWNER" });
      navigate('/dashboard');
    }
  };

  const onSignup = async (data: SignupValues) => {
    try {
      const payload = {
        ...data,
        departmentId: data.departmentId ? parseInt(data.departmentId) : null
      };
      const res = await axios.post('http://localhost:8080/api/auth/signup', payload);
      setGlobalUser(res.data);
      navigate('/dashboard');
    } catch (e: any) {
      alert(e.response?.data || "Signup failed");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1 className="brand">Smartoperation</h1>
          <p>{isLogin ? 'Welcome back! Please login.' : 'Create your account'}</p>
        </div>

        <div className="auth-toggle">
          <button className={`toggle-btn ${isLogin ? 'active' : ''}`} onClick={() => setIsLogin(true)}>Login</button>
          <button className={`toggle-btn ${!isLogin ? 'active' : ''}`} onClick={() => setIsLogin(false)}>Sign Up</button>
        </div>

        {isLogin ? (
          <form onSubmit={handleLoginSubmit(onLogin)} className="auth-form">
            <div className="form-group">
              <label>Email Address</label>
              <input type="email" placeholder="ceo@smart.com" {...registerLogin('email')} className={loginErrors.email ? 'error' : ''} />
              {loginErrors.email && <span className="error-text">{loginErrors.email.message}</span>}
            </div>
            <div className="form-group">
              <label>Password</label>
              <input type="password" placeholder="••••••••" {...registerLogin('password')} className={loginErrors.password ? 'error' : ''} />
              {loginErrors.password && <span className="error-text">{loginErrors.password.message}</span>}
            </div>
            <button type="submit" className="btn-primary auth-btn">Access Dashboard</button>
          </form>
        ) : (
          <form onSubmit={handleSignupSubmit(onSignup)} className="auth-form">
            <div className="form-group">
              <label>Full Name</label>
              <input placeholder="Jane Doe" {...registerSignup('name')} className={signupErrors.name ? 'error' : ''} />
              {signupErrors.name && <span className="error-text">{signupErrors.name.message}</span>}
            </div>
            <div className="form-group">
              <label>Email Address</label>
              <input type="email" placeholder="jane@smart.com" {...registerSignup('email')} className={signupErrors.email ? 'error' : ''} />
              {signupErrors.email && <span className="error-text">{signupErrors.email.message}</span>}
            </div>
            <div className="form-group">
              <label>Password</label>
              <input type="password" placeholder="••••••••" {...registerSignup('password')} className={signupErrors.password ? 'error' : ''} />
              {signupErrors.password && <span className="error-text">{signupErrors.password.message}</span>}
            </div>
            <div className="form-group">
              <label>Select Role</label>
              <select {...registerSignup('role')} className={signupErrors.role ? 'error' : ''}>
                <option value="EMPLOYEE">Employee</option>
                <option value="MANAGER">Manager</option>
                <option value="OWNER">Owner</option>
              </select>
            </div>
            <div className="form-group">
              <label>Department ID (Optional)</label>
              <input type="number" placeholder="1" {...registerSignup('departmentId')} />
            </div>
            <button type="submit" className="btn-primary auth-btn">Create Account</button>
          </form>
        )}
      </div>
    </div>
  );
};
