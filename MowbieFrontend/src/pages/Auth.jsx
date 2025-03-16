import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Auth = () => {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Hàm lấy avatar từ server (Base64) và lưu vào localStorage
  const fetchAvatar = async (avatarPath) => {
    if (!avatarPath) return; // Nếu không có avatarPath, không gọi API

    try {
      const response = await axios.get(
        "http://192.168.10.1:8081/api/users/avatar",
        {
          params: { avatarPath },
        }
      );

      if (response.data.image) {
        localStorage.setItem("avatar", response.data.image);
      }
    } catch (error) {
      console.error("Lỗi lấy avatar:", error);
    }
  };

  useEffect(() => {
    const handleAuth = async () => {
      try {
        const response = await axios.post(
          "http://192.168.10.1:8081/api/auth/refresh-token",
          null,
          { withCredentials: true }
        );

        if (response.status === 200) {
          localStorage.setItem("access_token", response.data.data.access_token);
          localStorage.setItem("user", JSON.stringify(response.data.data.user));
          await fetchAvatar(response.data.data.user.avatarPath);
          navigate(
            response.data.data.user.userRole === "manager"
              ? "/dashboard"
              : "/home"
          );
        }

        if (response.status === 401) {
          alert(response.data.message);
          localStorage.clear();
          navigate("/login");
        }
      } catch (error) {
        console.log(error);
        localStorage.clear();
        navigate("/home");
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
