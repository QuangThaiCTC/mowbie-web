import React from "react";
import UserList from "./UserList";

const DashboardContent = ({ selectedMenu, data }) => {
  return (
    <>
      <div className="flex-1 p-6">
        <h1 className="text-3xl font-bold">Chào mừng đến với Dashboard</h1>
        <p className="mt-4 text-lg">Dữ liệu của bạn sẽ hiển thị ở đây...</p>

        {selectedMenu === "users" && <UserList data={data} />}
        {selectedMenu === "products" && <p>Hiển thị danh sách sản phẩm...</p>}
        {selectedMenu === "orders" && <p>Hiển thị danh sách đơn đặt hàng...</p>}
        {selectedMenu === "payments" && <p>Hiển thị danh sách thanh toán...</p>}
        {selectedMenu === "report" && <p>Hiển thị thống kê...</p>}
      </div>
    </>
  );
};

export default DashboardContent;
