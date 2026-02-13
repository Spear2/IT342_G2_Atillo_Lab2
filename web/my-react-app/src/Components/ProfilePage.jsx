import React from "react";

const ProfilePage = ({ user, isLoading, error }) => {
  return (
    <div>
      <div className="dashboard-header">
        <div>
          <h2 className="dashboard-title">Profile</h2>
          <p className="dashboard-subtitle">Manage your account details</p>
        </div>
      </div>

      {isLoading && <p>Loading profile...</p>}
      {error && <div className="dashboard-error">{error}</div>}

      {user && (
        <div className="profile-card">
          <div className="profile-row">
            <span className="dashboard-label">Email</span>
            <p className="dashboard-value">{user.email}</p>
          </div>
          <div className="profile-row">
            <span className="dashboard-label">Phone</span>
            <p className="dashboard-value">
              {user.phoneNumber || "Not provided"}
            </p>
          </div>
          <div className="profile-row">
            <span className="dashboard-label">Address</span>
            <p className="dashboard-value">{user.address || "Not provided"}</p>
          </div>
          <div className="profile-row">
            <span className="dashboard-label">Status</span>
            <p className="dashboard-value">
              {user.active ? "Active" : "Inactive"}
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
