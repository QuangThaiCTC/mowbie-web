import SideBar from "../../components/layout/manager/SideBar";
import NavbarManager from "../../components/layout/manager/NavbarManager";
import { Outlet } from "react-router-dom";
import UserList from "../../components/layout/manager/UserList";

const ManagerDashboard = () => {
  return (
    <div className="fixed flex min-h-screen bg-base-200">
      {/* Sidebar cố định bên trái */}
      <SideBar />

      {/* Phần bên phải chứa Navbar + Nội dung */}
      <div className="flex min-w-screen">
        {/* Navbar cố định phía trên */}
        <NavbarManager />

        {/* Nội dung động sẽ hiển thị ở đây */}
        <div className="rounded-lg p-5 overflow-auto flex-1">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;
