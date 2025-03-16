import React from "react";
import Avatar from "../../ui/Avatar";
import ThemeSwitcher from "../../ui/ThemeSwitcher";
import Notification from "../../ui/Notification";

const NavbarManager = () => {
  return (
    <div className="navbar navbar-end min-w-full px-20 pr-80 h-10 bg-base-100 shadow-md">
      <Notification />
      <Avatar />
      <ThemeSwitcher />
    </div>
  );
};

export default NavbarManager;
