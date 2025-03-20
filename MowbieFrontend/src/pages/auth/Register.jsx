import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import ErrorAlert from "../../components/common/ErrorAlert";

const Register = () => {
  const [formData, setFormData] = useState({
    email: "",
    username: "",
    phone_number: "",
    password: "",
    confirm_password: "",
  });
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async () => {
    if (Object.values(formData).some((value) => value.trim() === "")) {
      setErrorMessage("Không được để trống!");
      setTimeout(() => setErrorMessage(""), 3000);
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(
        "http://192.168.10.1:8081/api/auth/register",
        null,
        { params: formData, withCredentials: true }
      );

      if (response.status !== 200) {
        setErrorMessage(response?.data?.message);
      }

      navigate("/login");
    } catch (error) {
      setErrorMessage(error.response?.data?.message);
    } finally {
      setLoading(false);
      setTimeout(() => setErrorMessage(""), 3000);
    }
  };

  return (
    <>
      <ErrorAlert message={errorMessage} type="error" />

      <div className="w-screen h-screen flex flex-col items-center justify-center bg-base-200">
        <div className="flex m-5">
          <label className="text-5xl font-bold">Mow</label>
          <img src="/src/assets/logo.png" alt="Logo" className="w-10 lg:w-15" />
        </div>

        <div className="bg-base-300 p-6 rounded-lg shadow-lg w-96">
          <fieldset className="border border-base-100 p-4 rounded mb-4">
            <legend className="fieldset-legend text-base text-10xl font-bold">
              Đăng ký
            </legend>
            <label className="text-base text-sm font-medium">Email:</label>
            <input
              name="email"
              type="email"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              placeholder="vidu@mail.com"
              value={formData.email}
              onChange={handleChange}
            />

            <label className="text-base text-sm font-medium">Họ và tên:</label>
            <input
              name="username"
              type="text"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              placeholder="Nguyễn Văn A"
              value={formData.username}
              onChange={handleChange}
            />

            <label className="text-base text-sm font-medium">
              Số điện thoại:
            </label>
            <input
              name="phone_number"
              type="phone"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              placeholder="Nhập số điện thoại..."
              value={formData.phone_number}
              onChange={handleChange}
            />

            <label className="text-base text-sm font-medium">Mật khẩu:</label>
            <input
              name="password"
              type="password"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              placeholder="Vidu123"
              value={formData.password}
              onChange={handleChange}
            />

            <label className="text-base text-sm font-medium">
              Xác nhận mật khẩu:
            </label>
            <input
              name="confirm_password"
              type="password"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              placeholder="Vidu123"
              value={formData.confirm_password}
              onChange={handleChange}
            />
          </fieldset>

          <div className="flex flex-col items-center justify-center">
            <button
              onClick={handleRegister}
              className={`btn btn-primary w-full ${loading && "btn-disabled"}`}
            >
              {loading ? "Đang đăng ký..." : "Đăng ký"}
            </button>
          </div>

          <p className="text-center text-sm text-base-500 mt-3">
            Bạn đã có tài khoản?{" "}
            <Link to="/login" className="text-base text-sm underline">
              Đăng nhập ngay
            </Link>
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

export default Register;
