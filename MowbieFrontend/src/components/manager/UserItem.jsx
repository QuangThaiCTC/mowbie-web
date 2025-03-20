import { useState, useEffect } from "react";
import axios from "axios";

const UserItem = ({ user }) => {
  const defaultAvatar =
    "http://192.168.10.1:8081/api/uploads/users/default.png";
  const [avatar, setAvatar] = useState(null);
  const [isActive, setIsActive] = useState(user.isActive);

  useEffect(() => {
    if (user.avatarPath) {
      setAvatar("http://192.168.10.1:8081/api/" + user.avatarPath);
    } else {
      setAvatar(defaultAvatar);
    }
  }, [user.avatarPath]);

  const handleToggleStatus = async () => {
    const newStatus = !isActive;
    setIsActive(newStatus);

    try {
      const response = await axios.put(
        "http://192.168.10.1:8081/api/users/status",
        null,
        {
          params: {
            userId: user.userId,
            status: newStatus,
          },
        }
      );

      if (response.data.status !== 200) {
        alert(response.data.message || "Có lỗi xảy ra!");
        setIsActive(!newStatus);
      }
    } catch (error) {
      console.error("Lỗi cập nhật trạng thái:", error);
      alert("Không thể cập nhật trạng thái!");
      setIsActive(!newStatus);
    }
  };

  return (
    <tr className="w-full">
      <td>
        <div className="flex items-center gap-3">
          <div className="avatar indicator">
            {isActive ? (
              <div className="indicator-item inline-grid *:[grid-area:1/1]">
                <div className="status status-accent animate-ping"></div>
                <div className="status status-accent"></div>
              </div>
            ) : (
              <div className="indicator-item inline-grid *:[grid-area:1/1]">
                <div className="status status-error animate-ping"></div>
                <div className="status status-error"></div>
              </div>
            )}
            <div className="mask mask-squircle h-12 w-12">
              <img src={avatar} alt="Avatar" />
            </div>
          </div>
          <div>
            <div className="font-bold">{user.username}</div>
          </div>
        </div>
      </td>
      <td>{user.email}</td>
      <td>{user.phoneNumber}</td>
      <td>{user.userRole}</td>
      <th>
        <button
          className={`btn btn-outline btn-xs ${
            isActive ? "btn-error" : "btn-success"
          }`}
          onClick={handleToggleStatus}
        >
          {isActive ? "Khoá tài khoản" : "Mở khoá tài khoản"}
        </button>
      </th>
    </tr>
  );
};

export default UserItem;
