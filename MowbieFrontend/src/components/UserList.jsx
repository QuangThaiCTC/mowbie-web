import React from "react";
import ErrorAlert from "./ErrorAlert";

const UserList = ({ data }) => {
  return (
    <>
      <ul className="list bg-gray-100 rounded-box shadow-md text-black">
        <li className="p-4 pb-2 text-xl opacity-60 tracking-wide">
          Danh sách khách hàng
        </li>

        {Array.isArray(data) ? (
          data.map(({ id, username, email }) => (
            <li key={id} className="p-4 border-b border-gray-200">
              <div className="flex items-center justify-between">
                <div>
                  <h1 className="font-bold">{username}</h1>
                  <p className="text-sm">{email}</p>
                </div>
                <button className="btn btn-sm btn-primary">Xem chi tiết</button>
              </div>
            </li>
          ))
        ) : (
          <ErrorAlert message="Không có dữ liệu!" type="error" />
        )}
      </ul>
    </>
  );
};

export default UserList;
