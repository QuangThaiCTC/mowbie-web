import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Auth = () => {
  const API_URL = import.meta.env.VITE_API_BASE_URL;
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const getAvatarUrl = (avatarPath) => {
    if (!avatarPath) {
      return `${API_URL}/users/default.png`;
    }
    return `${API_URL}/${avatarPath}`;
  };

  useEffect(() => {
    const handleAuth = async () => {
      try {
        const response = await axios.post(
          `${API_URL}/auth/refresh-token`,
          null,
          { withCredentials: true }
        );

        if (response.status === 200) {
          localStorage.setItem('access_token', response.data.data.access_token);
          localStorage.setItem('user', JSON.stringify(response.data.data.user));
          const avatarUrl = getAvatarUrl(response.data.data.user.avatarPath);
          localStorage.setItem('avatar', avatarUrl);
          navigate(
            response.data.data.user.userRole === 'manager'
              ? '/dashboard'
              : '/home'
          );
        }

        if (response.status === 401) {
          alert(response.data.message);
          localStorage.clear();
          navigate('/login');
        }
      } catch (error) {
        console.log(error);
        localStorage.clear();
        navigate('/home');
      } finally {
        setLoading(false);
      }
    };

    handleAuth();
  }, [navigate]);

  if (loading) {
    return (
      <div className="w-screen h-screen flex items-center justify-center bg-gray-100">
        <span className="loading loading-spinner loading-5xl text-accent"></span>
      </div>
    );
  }
};

export default Auth;
