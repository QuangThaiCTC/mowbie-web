import React from "react";
import Logo from "../../ui/Logo";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUsers } from "@fortawesome/free-solid-svg-icons";
import { NavLink } from "react-router-dom";

const SideBar = () => {
  return (
    <div className="drawer drawer-open min-h-full w-60 bg-base-100 shadow-lg py-5">
      <ul className="menu">
        <div className="flex w-full justify-center items-center">
          <Logo />
        </div>
        <li></li>
        <li>
          <NavLink to="/dashboard/users">
            <FontAwesomeIcon icon={faUsers} />
            Danh sách người dùng
          </NavLink>
        </li>
      </ul>
    </div>
  );
};

export default SideBar;
