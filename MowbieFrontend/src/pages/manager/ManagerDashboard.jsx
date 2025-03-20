import SideBar from "../../layouts/manager/SideBar";
import NavbarManager from "../../layouts/manager/NavbarManager";
import { Outlet } from "react-router-dom";

const ManagerDashboard = () => {
  return (
    <div className="fixed flex min-h-full min-w-full bg-base-200">
      <SideBar />

      <div className="w-full h-full">
        <NavbarManager />

        <Outlet />
      </div>
    </div>
  );
};

export default ManagerDashboard;
