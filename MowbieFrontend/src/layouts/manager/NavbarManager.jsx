import React from "react";
import Avatar from "../../components/common/Avatar";
import ThemeSwitcher from "../../components/common/ThemeSwitcher";
import Notification from "../../components/common/Notification";

const NavbarManager = () => {
  return (
    <div className="navbar navbar-end w-full px-20 h-10 bg-base-100 shadow-md">
      <Notification />
      <Avatar />
      <ThemeSwitcher />
    </div>
  );
};

export default NavbarManager;
