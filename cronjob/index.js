require('dotenv').config();
const cron = require('node-cron');
const mysql = require('mysql2/promise');
const { z } = require('zod');

// Zod schema for validating DB results
const TaskRowSchema = z.object({
  id: z.number(),
  title: z.string(),
  status: z.string(),
  priority: z.string(),
  due_date: z.date().nullable()
});

const pool = mysql.createPool({
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'smartuser',
  password: process.env.DB_PASSWORD || 'smartpassword',
  database: process.env.DB_NAME || 'smartoperation',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

async function runAnalysis() {
  console.log('[CRON] Starting AI Risk Analysis cycle...');
  try {
    const [rows] = await pool.query('SELECT id, title, status, priority, due_date FROM task WHERE status != "COMPLETED"');
    
    // Validate with Zod
    const tasks = z.array(TaskRowSchema).parse(rows);
    console.log(`[CRON] Analyzing ${tasks.length} active tasks.`);

    for (const task of tasks) {
      let riskScore = '';
      
      if (!task.due_date) {
        riskScore = 'No Due Date Assigned. Medium Risk of stagnation.';
      } else {
        const now = new Date();
        const diffDays = Math.ceil((task.due_date - now) / (1000 * 60 * 60 * 24));
        
        if (diffDays < 0) {
          riskScore = `CRITICAL RISK: Task is overdue by ${Math.abs(diffDays)} days! Priority: ${task.priority}.`;
        } else if (diffDays <= 2 && task.priority === 'HIGH') {
          riskScore = `HIGH RISK: High priority task due in ${diffDays} days. Needs immediate attention.`;
        } else if (diffDays <= 5) {
          riskScore = `MEDIUM RISK: Due in ${diffDays} days. Status is ${task.status}.`;
        } else {
          riskScore = `LOW RISK: On track. Due in ${diffDays} days.`;
        }
      }
      
      const aiAnalysis = `[${new Date().toISOString()}] AI Model: ${riskScore}`;
      
      await pool.query('UPDATE task SET ai_analysis = ? WHERE id = ?', [aiAnalysis, task.id]);
    }
    
    console.log('[CRON] Analysis complete. Task risk scores updated.');

  } catch (err) {
    console.error('[CRON] Error running analysis:', err);
  }
}

// Run every minute
cron.schedule('* * * * *', () => {
  runAnalysis();
});

console.log('[CRON] Node AI Risk Analyzer service started.');
runAnalysis(); // Run once immediately
