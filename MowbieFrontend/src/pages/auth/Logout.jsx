import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Logout = () => {
  const API_URL = import.meta.env.VITE_API_BASE_URL;

  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const handleAuth = async () => {
      try {
        const response = await axios.post(`${API_URL}/auth/logout`, null, {
          withCredentials: true,
        });

        if (response.status === 200) {
          localStorage.clear();
          navigate('/home');
        }
      } catch (error) {
        alert(
          error.response?.data?.message || 'Có lỗi xảy ra, vui lòng đăng nhập.'
        );
        localStorage.clear();
        navigate('/auth');
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

export default Logout;
