<template>
  <div class="terminal-wrapper">
    <div class="grid-background"></div>
    <div class="scan-line"></div>

    <div class="ai-container">
      <div class="terminal-header">
        <div class="brand">
          <span class="york-logo">YORK U</span>
          <span class="divider">|</span>
          <span class="sub-brand">ACADEMIC AI TERMINAL</span>
        </div>
        <div class="status-indicators">
          <span class="status-item">NET: <span class="active">ONLINE</span></span>
          <span class="status-item">CPU: <span class="active">NOMINAL</span></span>
          <span class="status-item time">{{ currentTime }}</span>
        </div>
      </div>

      <div class="chat-window" ref="chatWindow">
        <div class="message system-msg">
          <div class="avatar">SYS</div>
          <div class="content">
            <div class="typewriter">
              > SYSTEM INITIALIZED...<br>
              > YORK UNIVERSITY DATABASE CONNECTED.<br>
              > WAITING FOR INPUT_
            </div>
          </div>
        </div>

        <div v-for="(msg, index) in chatHistory" :key="index" :class="['message', msg.role === 'user' ? 'user-msg' : 'ai-msg']">
          <div class="avatar">{{ msg.role === 'user' ? 'USR' : 'AI' }}</div>
          <div class="content">
            <div v-html="msg.content"></div>
          </div>
        </div>

        <div v-if="loading" class="message ai-msg loading-msg">
          <div class="avatar">AI</div>
          <div class="content blink">PROCESSING DATA STREAM...</div>
        </div>
      </div>

      <div class="input-area">
        <span class="prompt">></span>
        <input
          v-model="question"
          @keyup.enter="sendMessage"
          type="text"
          placeholder="Enter SQL command or natural language query..."
          :disabled="loading"
          autocomplete="off"
        >
        <button @click="sendMessage" :disabled="loading" class="cyber-btn">
          {{ loading ? 'EXECUTING' : 'TRANSMIT' }}
          <span class="glitch-effect"></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue';

const question = ref('');
const chatHistory = ref([]);
const loading = ref(false);
const chatWindow = ref(null);
const currentTime = ref('');

// 时间更新逻辑
let timer;
const updateTime = () => {
  const now = new Date();
  currentTime.value = now.toLocaleTimeString('en-US', { hour12: false });
};

onMounted(() => {
  updateTime();
  timer = setInterval(updateTime, 1000);
});

onUnmounted(() => {
  clearInterval(timer);
});

const sendMessage = async () => {
  if (!question.value.trim()) return;

  const userQ = question.value;
  chatHistory.value.push({ role: 'user', content: userQ });
  question.value = '';
  scrollToBottom();

  loading.value = true;
  try {
    // 替换为你的真实后端地址
    const res = await fetch(`http://localhost:8080/ai/search?question=${encodeURIComponent(userQ)}`, {
      // method: 'GET', // 或 POST，取决于你后端
      // headers: { 'Content-Type': 'application/json' }
    });
    const result = await res.json();

    let htmlContent = '';
    if (result.code === 1) {
      const { type, aiMessage, data } = result.data;

      // AI 的文字回复
      htmlContent += `<div class="ai-text-response">> ${aiMessage}</div>`;

      // 渲染表格
      if (type === 'SELECT' && Array.isArray(data) && data.length > 0) {
        htmlContent += renderTable(data);
        htmlContent += `<div class="meta-info">[RECORDS FOUND: ${data.length}]</div>`;
      } else if (type === 'DML') {
        htmlContent += `<div class="success-log">✅ OPERATION SUCCESSFUL: ${data}</div>`;
      }
    } else {
      htmlContent = `<span class="error-log">[ERROR] ${result.msg}</span>`;
    }

    chatHistory.value.push({ role: 'ai', content: htmlContent });

  } catch (error) {
    chatHistory.value.push({ role: 'ai', content: `<span class="error-log">[SYSTEM FAILURE] ${error.message}</span>` });
  } finally {
    loading.value = false;
    scrollToBottom();
  }
};

const scrollToBottom = async () => {
  await nextTick();
  if (chatWindow.value) {
    chatWindow.value.scrollTop = chatWindow.value.scrollHeight;
  }
};

const renderTable = (list) => {
  if (!list.length) return '';
  const cols = Object.keys(list[0]);
  let html = '<div class="table-wrapper"><table class="ai-table"><thead><tr>';
  cols.forEach(c => html += `<th>${c.toUpperCase()}</th>`);
  html += '</tr></thead><tbody>';
  list.forEach(row => {
    html += '<tr>';
    cols.forEach(c => html += `<td>${row[c]}</td>`);
    html += '</tr>';
  });
  html += '</tbody></table></div>';
  return html;
};
</script>

<style scoped>
/* 定义 York U 科技风变量 */
:root {
  --york-red: #E31837;         /* York 官方红 */
  --york-red-dim: rgba(227, 24, 55, 0.2);
  --york-red-glow: rgba(227, 24, 55, 0.6);
  --bg-dark: #050505;          /* 极黑背景 */
  --bg-panel: #0a0a0a;         /* 面板背景 */
  --text-main: #e0e0e0;
  --text-dim: #888;
  --border-color: #333;
  --font-tech: 'JetBrains Mono', 'Fira Code', 'Consolas', monospace;
}

/* 1. 整体容器 & 背景特效 */
.terminal-wrapper {
  position: relative;
  height: calc(100vh - 84px); /* 适配你的 Layout */
  background-color: #000;
  font-family: 'JetBrains Mono', 'Consolas', monospace; /* 强制等宽字体 */
  color: #e0e0e0;
  overflow: hidden;
  border: 1px solid #333;
  box-shadow: 0 0 20px rgba(0,0,0,0.8);
}

/* 背景网格线 */
.grid-background {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background-image:
    linear-gradient(rgba(50, 50, 50, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(50, 50, 50, 0.1) 1px, transparent 1px);
  background-size: 20px 20px;
  pointer-events: none;
  z-index: 0;
}

/* CRT 扫描线特效 */
.scan-line {
  position: absolute;
  top: 0; left: 0; right: 0; height: 100%;
  background: linear-gradient(to bottom, rgba(255,255,255,0), rgba(255,255,255,0) 50%, rgba(0,0,0,0.1) 50%, rgba(0,0,0,0.1));
  background-size: 100% 4px;
  pointer-events: none;
  z-index: 10;
  opacity: 0.3;
}

.ai-container {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  height: 100%;
  background: rgba(10, 10, 10, 0.85); /* 半透明背景 */
  backdrop-filter: blur(2px);
}

/* 2. 顶部 Header */
.terminal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 40px;
  background: #111;
  border-bottom: 2px solid var(--york-red);
  font-size: 12px;
  letter-spacing: 1px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.york-logo {
  color: var(--york-red); /* York Red */
  font-weight: 900;
  font-size: 14px;
  text-shadow: 0 0 5px var(--york-red-glow);
}

.sub-brand { color: #888; }
.divider { color: #444; }

.status-indicators {
  display: flex;
  gap: 15px;
  color: #666;
}

.status-item .active {
  color: #4ade80; /* Green for online status */
}

/* 3. 聊天窗口 */
.chat-window {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 滚动条美化 */
.chat-window::-webkit-scrollbar { width: 6px; }
.chat-window::-webkit-scrollbar-track { background: #000; }
.chat-window::-webkit-scrollbar-thumb { background: #333; border-radius: 3px; }
.chat-window::-webkit-scrollbar-thumb:hover { background: var(--york-red); }

/* 消息气泡通用 */
.message {
  display: flex;
  gap: 12px;
  max-width: 90%;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.avatar {
  width: 36px;
  height: 36px;
  background: #222;
  border: 1px solid #444;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: bold;
  flex-shrink: 0;
}

.content {
  padding: 12px 16px;
  border-radius: 0 10px 10px 10px;
  font-size: 14px;
  line-height: 1.6;
  position: relative;
}

/* 用户消息 */
.user-msg {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.user-msg .avatar {
  border-color: var(--york-red);
  color: var(--york-red);
  box-shadow: 0 0 5px var(--york-red-dim);
}

.user-msg .content {
  background: rgba(227, 24, 55, 0.15); /* 极淡的 York Red 背景 */
  border: 1px solid var(--york-red-glow);
  color: #fff;
  border-radius: 10px 0 10px 10px;
}

/* AI 消息 */
.ai-msg { align-self: flex-start; }

.ai-msg .avatar {
  border-color: #fff;
  color: #fff;
}

.system-msg .avatar { border-color: #4ade80; color: #4ade80; }

.ai-msg .content, .system-msg .content {
  background: #151515;
  border: 1px solid #333;
  border-left: 3px solid #fff; /* AI 左侧白条 */
  color: #ccc;
}

.system-msg .content {
  border-left-color: #4ade80;
  color: #4ade80;
  font-family: 'Consolas', monospace;
}

/* 4. 输入区域 */
.input-area {
  padding: 15px 20px;
  background: #050505;
  border-top: 1px solid #333;
  display: flex;
  align-items: center;
  gap: 10px;
}

.prompt {
  color: var(--york-red);
  font-weight: bold;
  font-size: 18px;
  animation: blink 1s infinite;
}

input {
  flex: 1;
  background: transparent;
  border: none;
  color: #fff;
  font-family: inherit;
  font-size: 16px;
  outline: none;
  padding: 10px;
  border-bottom: 1px solid #333;
  transition: border-color 0.3s;
}

input:focus {
  border-bottom-color: var(--york-red);
}

.cyber-btn {
  background: var(--york-red); /* York Red */
  color: white;
  border: none;
  padding: 10px 25px;
  font-family: inherit;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  clip-path: polygon(10px 0, 100% 0, 100% calc(100% - 10px), calc(100% - 10px) 100%, 0 100%, 0 10px);
  transition: all 0.2s;
  position: relative;
}

.cyber-btn:hover:not(:disabled) {
  background: #ff1f43;
  box-shadow: 0 0 15px var(--york-red-glow);
  text-shadow: 0 0 5px white;
}

.cyber-btn:disabled {
  background: #333;
  color: #666;
  cursor: not-allowed;
}

/* 5. 深度选择器：表格与内容样式 */
:deep(.ai-text-response) {
  margin-bottom: 10px;
  color: #fff;
  font-weight: bold;
}

:deep(.meta-info) {
  margin-top: 5px;
  font-size: 10px;
  color: #666;
  text-align: right;
}

:deep(.error-log) { color: #ff3333; }
:deep(.success-log) { color: #4ade80; }

/* 表格样式 - 科技感 */
:deep(.table-wrapper) {
  overflow-x: auto;
  border: 1px solid #333;
  border-radius: 4px;
}

:deep(.ai-table) {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
  white-space: nowrap;
}

:deep(.ai-table th) {
  background: #1a1a1a;
  color: var(--york-red); /* York Red Header Text */
  border-bottom: 1px solid #444;
  padding: 10px 15px;
  text-align: left;
  font-weight: bold;
}

:deep(.ai-table td) {
  padding: 8px 15px;
  border-bottom: 1px solid #222;
  color: #ccc;
}

:deep(.ai-table tr:hover td) {
  background: rgba(227, 24, 55, 0.05);
  color: #fff;
}

/* 动画 */
.blink { animation: blink 1s infinite; }
@keyframes blink { 50% { opacity: 0; } }

</style>