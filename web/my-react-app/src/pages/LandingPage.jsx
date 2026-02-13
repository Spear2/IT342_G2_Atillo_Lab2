import React from "react";
import { Link } from "react-router-dom";
import { getToken } from "../security/auth";
import "../css/landing.css";

const LandingPage = () => {
  return (
    <div className="landing-page">
      <header className="landing-content">
        <h1 className="landing-title">Welcome to Our App</h1>
        <p className="landing-subtitle">
          This is a simple introduction to our platform. Join us today to get started.
        </p>
        
        <div className="landing-actions">
          {getToken() ? (
            <Link to="/dashboard" className="landing-button landing-button--login">
              Go to Dashboard
            </Link>
          ) : (
            <>
              <Link to="/login" className="landing-button landing-button--login">
                Login
              </Link>
              <Link
                to="/register"
                className="landing-button landing-button--register"
              >
                Register
              </Link>
            </>
          )}
        </div>
      </header>
    </div>
  );
};

export default LandingPage;