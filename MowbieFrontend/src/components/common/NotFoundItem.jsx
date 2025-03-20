import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBan } from "@fortawesome/free-solid-svg-icons";

const NotFoundItem = ({ message }) => {
  return (
    <div className="flex flex-col items-center justify-center h-full bg-base-200 text-gray-800 rounded-lg p-6">
      <FontAwesomeIcon icon={faBan} size="4x" className="text-red-500 mb-4" />
      <h1 className="text-xl font-bold mb-2">{message}</h1>
      <p className="text-sm text-gray-600">
        Không có kết quả phù hợp với tìm kiếm của bạn.
      </p>
    </div>
  );
};

export default NotFoundItem;
