import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../css/auth.css";

const API_BASE =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const RegisterPage = () => {
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [form, setForm] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    // address: "",
    // phoneNumber: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {{
    e.preventDefault();
    setIsSubmitting(true);
    if(!form.email || !form.password || !form.confirmPassword){
      alert("Please fill the field");
      return;
    }
    try{
      const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (!response.ok) {
        const err = await response.json();
        alert(err.message || "Customer registration failed!");
        return;
      }
      const data = await response.json();
      alert("Customer registration successful!", data);
      navigate("/dashboard");
    }
    catch(err){
      setError(err.message || "Unable to register right now.");
    }
    finally{
      setIsSubmitting(false);
    }
  }}
  

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h2 className="auth-title">Create Account</h2>
        <p className="auth-subtitle">Start by filling in your details.</p>

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

        <label className="auth-label" htmlFor="password">
          Confirm password
        </label>
        <input
          id="confirmPassword"
          name="confirmPassword"
          type="password"
          value={form.confirmPassword}
          onChange={handleChange}
          className="auth-input"
          required
        />

        {/* <label className="auth-label" htmlFor="address">
          Address
        </label>
        <input
          id="address"
          name="address"
          type="text"
          value={form.address}
          onChange={handleChange}
          className="auth-input"
          required
        />

        <label className="auth-label" htmlFor="phoneNumber">
          Phone Number
        </label>
        <input
          id="phoneNumber"
          name="phoneNumber"
          type="text"
          value={form.phoneNumber}
          onChange={handleChange}
          className="auth-input"
          required
        /> */}

        {error && <div className="auth-error">{error}</div>}

        <button
          className="auth-button auth-button--register"
          type="submit"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Creating..." : "Register"}
        </button>

        <p className="auth-footer">
          Already registered? <Link to="/login">Login here</Link>
        </p>
      </form>
    </div>
  );
};

export default RegisterPage;
