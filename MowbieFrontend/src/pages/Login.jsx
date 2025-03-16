import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { Helmet } from "react-helmet";
import ErrorAlert from "../components/ui/ErrorAlert";

const Login = () => {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

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

  const handleLogin = async () => {
    if (!formData.email.trim() || !formData.password.trim()) {
      setErrorMessage("Không được để trống!");
      setTimeout(() => setErrorMessage(""), 3000);
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(
        "http://192.168.10.1:8081/api/auth/login",
        null,
        {
          params: formData,
          withCredentials: true,
        }
      );

      if (response.status === 200) {
        const { access_token, user } = response.data.data;

        // Lưu thông tin vào localStorage
        localStorage.setItem("access_token", access_token);
        localStorage.setItem("user", JSON.stringify(user));

        // Gọi API lấy avatar nếu có
        await fetchAvatar(user.avatarPath);

        // Điều hướng theo vai trò user
        navigate(user.userRole === "manager" ? "/dashboard" : "/home");
      }

      if (
        response.status === 400 ||
        response.status === 401 ||
        response.status === 403
      ) {
        setErrorMessage(response.data.message);
        localStorage.clear();
      }
    } catch (error) {
      setErrorMessage(error.response?.data?.message || "Lỗi hệ thống!");
    } finally {
      setLoading(false);
      setTimeout(() => setErrorMessage(""), 3000);
    }
  };

  return (
    <>
      <Helmet>
        <link rel="icon" type="image/png" href="logo-white.png" />
        <title>Đăng nhập - Mowbie</title>
      </Helmet>
      <ErrorAlert message={errorMessage} type="error" />
      <div className="w-screen h-screen flex flex-col items-center justify-center bg-base-200">
        <div className="flex m-5">
          <label className="text-5xl font-bold">Mow</label>
          <img src="/src/assets/logo.png" alt="Logo" className="w-10 lg:w-15" />
        </div>

        <div className="bg-base-300 p-6 rounded-lg shadow-lg w-96">
          <fieldset className="border border-base-100 p-4 rounded mb-4">
            <legend className="fieldset-legend text-base text-10xl font-bold">
              Đăng nhập
            </legend>
            <label className="text-base text-sm font-medium">Email:</label>
            <input
              name="email"
              type="email"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              placeholder="vidu@mail.com"
              value={formData.email}
              onChange={handleChange}
            />
            <label className="text-base text-sm font-medium">Mật khẩu:</label>
            <input
              name="password"
              type="password"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              placeholder="Vidu123"
              value={formData.password}
              onChange={handleChange}
            />
          </fieldset>

          <div className="flex flex-col items-center justify-center">
            <button
              onClick={handleLogin}
              className={`btn btn-primary w-full ${loading && "btn-disabled"}`}
            >
              {loading ? "Đang đăng nhập..." : "Đăng nhập"}
            </button>
          </div>

          <p className="text-center text-sm text-base-500 mt-3">
            Bạn chưa có tài khoản?{" "}
            <Link to="/register" className="text-base text-sm underline">
              Đăng ký ngay
            </Link>{" "}
            <br />
            <Link to="/home" className="text-base text-sm underline">
              Tiếp tục với tư cách khách
            </Link>
          </p>
        </div>
      </div>
    </>
  );
};

export default Login;
