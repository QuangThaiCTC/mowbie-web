import React, { useRef, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import ErrorAlert from "./ErrorAlert";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { faCircleUser } from "@fortawesome/free-regular-svg-icons";

const Profile = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  const [avatar, setAvatar] = useState(localStorage.getItem("avatar"));
  const navigate = useNavigate();
  const fileInputRef = useRef(null);
  const [formData, setFormData] = useState({
    userId: user.userId,
    username: user.username,
    phoneNumber: user.phoneNumber,
    newPassword: "",
    avatar: null,
  });

  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user) navigate("/login");
  }, [user, navigate]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleIconClick = () => fileInputRef.current.click();

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setAvatar(URL.createObjectURL(file));
      setFormData({ ...formData, avatar: file });
    }
  };

  const handleUpdate = async () => {
    setLoading(true);
    try {
      const uploadData = new FormData();
      uploadData.append("userId", formData.userId);
      uploadData.append("username", formData.username);
      uploadData.append("phoneNumber", formData.phoneNumber);
      uploadData.append("newPassword", formData.newPassword);
      if (formData.avatar) uploadData.append("avatar", formData.avatar);

      const response = await axios.put(
        "http://192.168.10.1:8081/api/users/update",
        uploadData,
        {
          withCredentials: true,
          headers: { "Content-Type": "multipart/form-data" },
        }
      );

      if (response.status === 200) {
        const newUser = response.data.data.user;
        localStorage.setItem("user", JSON.stringify(newUser));
        navigate("/auth");
      } else {
        setErrorMessage(response?.data?.message || "Có lỗi xảy ra!");
      }
    } catch (error) {
      setErrorMessage(error.response?.data?.message || "Có lỗi xảy ra!");
    } finally {
      setLoading(false);
      setTimeout(() => setErrorMessage(""), 3000);
    }
  };

  const handleCloseModal = () => {
    setAvatar(localStorage.getItem("avatar"));
    setFormData({
      userId: user.userId,
      username: user.username,
      phoneNumber: user.phoneNumber,
      newPassword: "",
      avatar: null,
    });
    document.getElementById("profile").close();
  };

  return (
    <>
      <button onClick={() => document.getElementById("profile").showModal()}>
        <FontAwesomeIcon icon={faCircleUser} />
        Thông tin cá nhân
      </button>
      <dialog
        id="profile"
        className="modal modal-bottom sm:modal-middle"
        tabIndex="-1"
      >
        <div className="modal-box absolute justify-center items-center">
          <ErrorAlert message={errorMessage} type="error" />
          <fieldset className="border border-base-300 p-4 rounded mb-4">
            <div className="flex w-full items-center justify-center">
              <div className="indicator" onClick={handleIconClick}>
                <div className="avatar">
                  <div className="ring-primary ring-offset-base-100 w-24 rounded-full ring ring-offset-2">
                    <img
                      src={avatar || setAvatar("/src/assets/avatar.png")}
                      alt="Avatar"
                    />
                    <span className="badge badge-xs badge-dash indicator-item">
                      <input
                        type="file"
                        ref={fileInputRef}
                        onChange={handleFileChange}
                        accept="image/*"
                        className="hidden"
                      />
                      <FontAwesomeIcon icon={faPenToSquare} size="xl" />
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <legend className="fieldset-legend text-base text-10xl font-bold">
              Thông tin cá nhân
            </legend>
            <label className="text-base text-sm font-medium">Email:</label>
            <input
              name="email"
              type="email"
              disabled
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              value={user.email}
            />

            <label className="text-base text-sm font-medium">Họ và tên:</label>
            <input
              name="username"
              type="text"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              value={formData.username}
              placeholder="Nguyễn Văn A"
              onChange={handleChange}
            />

            <label className="text-base text-sm font-medium">
              Số điện thoại:
            </label>
            <input
              name="phoneNumber"
              type="text"
              className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
              required
              value={formData.phoneNumber}
              onChange={handleChange}
            />

            <div
              className="tooltip tooltip-top tooltip-warning w-full"
              data-tip="Chỉ nhập khi muốn đổi mật khẩu!"
            >
              <label className="text-base text-sm font-medium">
                Mật khẩu mới:
              </label>
              <input
                name="newPassword"
                type="password"
                className="input input-bordered w-full mt-2 bg-base-100 text-base-500"
                placeholder="Vidu123"
                value={formData.newPassword}
                required
                onChange={handleChange}
              />
            </div>
          </fieldset>

          <div className="flex flex-col items-center justify-center">
            <button
              onClick={handleUpdate}
              className={`btn btn-primary w-full mb-3 ${
                loading && "btn-disabled"
              }`}
            >
              {loading ? "Đang cập nhật..." : "Cập nhật"}
            </button>
            <button onClick={handleCloseModal} className="btn btn-soft w-full">
              Đóng
            </button>
          </div>
        </div>
      </dialog>
    </>
  );
};

export default Profile;
