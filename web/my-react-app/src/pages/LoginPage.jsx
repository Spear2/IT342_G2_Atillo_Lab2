import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { setToken } from "../security/auth";
import "../css/auth.css";


const LoginPage = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const API_BASE =
    import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";
  
  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!form.email || !form.password) {
      setError("Please fill in both email and password.");
      return;
    }

    setIsSubmitting(true);
    try {
      const response = await fetch(`${API_BASE}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: form.email.trim(),
          password: form.password,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        if (!data?.token) {
          throw new Error("Missing token from server response.");
        }
        setToken(data.token);
        navigate("/dashboard", { replace: true });
      } else {
        const message = await response.text();
        setError(message || "Invalid credentials.");
      }
    } catch (err) {
      setError(err.message || "Unable to login right now.");
    } finally {
      setIsSubmitting(false);
    }
  };
  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h2 className="auth-title">Login</h2>
        <p className="auth-subtitle">Access your account.</p>

        <label className="auth-label" htmlFor="email">
          Email
        </label>
        <input
          id="email"
          name="email"
          type="email"
          value={form.email}
          onChange={handleChange}
          className="auth-input"
          required
        />

        <label className="auth-label" htmlFor="password">
          Password
        </label>
        <input
          id="password"
          name="password"
          type="password"
          value={form.password}
          onChange={handleChange}
          className="auth-input"
          required
        />

        {error && <div className="auth-error">{error}</div>}

        <button
          className="auth-button auth-button--login"
          type="submit"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Signing in..." : "Login"}
        </button>

        <p className="auth-footer">
          No account yet? <Link to="/register">Register here</Link>
        </p>
      </form>
    </div>
  );
};

export default LoginPage;
