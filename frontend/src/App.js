import React, { useEffect, useState } from "react";
import "./App.css";
import MyLogo from "./assets/my-logo.svg"; 

const API_BASE = "http://localhost:8080/api/tasks";

function App() {
  const [tasks, setTasks] = useState([]);
  const [text, setText] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // 初回レンダリング時に一覧取得
  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const res = await fetch(API_BASE);
        if (!res.ok) {
          throw new Error("サーバーエラー: " + res.status);
        }
        const data = await res.json();
        setTasks(data);
      } catch (e) {
        console.error(e);
        setError("タスク一覧の取得に失敗しました");
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const addTask = async () => {
    const trimmed = text.trim();
    if (!trimmed) return;

    try {
      const res = await fetch(API_BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: trimmed }),
      });
      if (!res.ok) {
        throw new Error("追加に失敗しました");
      }
      const created = await res.json();
      setTasks((prev) => [...prev, created]);
      setText("");
    } catch (e) {
      console.error(e);
      setError("タスク追加に失敗しました");
    }
  };

  const toggleTask = async (task) => {
    try {
      const updated = { ...task, done: !task.done };
      const res = await fetch(`${API_BASE}/${task.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updated),
      });
      if (!res.ok) {
        throw new Error("更新に失敗しました");
      }
      const data = await res.json();
      setTasks((prev) =>
        prev.map((t) => (t.id === data.id ? data : t))
      );
    } catch (e) {
      console.error(e);
      setError("タスク更新に失敗しました");
    }
  };

  const deleteTask = async (taskId) => {
    try {
      const res = await fetch(`${API_BASE}/${taskId}`, {
        method: "DELETE",
      });
      if (!res.ok && res.status !== 204) {
        throw new Error("削除に失敗しました");
      }
      setTasks((prev) => prev.filter((t) => t.id !== taskId));
    } catch (e) {
      console.error(e);
      setError("タスク削除に失敗しました");
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      addTask();
    }
  };

  const doneCount = tasks.filter((t) => t.done).length;

  return (
    <div className="app">
      <header className="header">
        <img src={MyLogo} alt="My Logo" className="logo" />
        <h1>チェックリスト</h1>
      </header>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {loading && <p>読み込み中...</p>}

      <div className="input-row">
        <input
          type="text"
          placeholder="タスクを入力して Enter または追加ボタン"
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={handleKeyDown}
        />
        <button onClick={addTask}>追加</button>
      </div>

      <p className="summary">
        完了: {doneCount} / {tasks.length}
      </p>

      <ul className="task-list">
        {tasks.map((task) => (
          <li key={task.id} className={task.done ? "task done" : "task"}>
            <label>
              <input
                type="checkbox"
                checked={task.done}
                onChange={() => toggleTask(task)}
              />
              <span>{task.title}</span>
            </label>
            <button className="delete" onClick={() => deleteTask(task.id)}>
              削除
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
