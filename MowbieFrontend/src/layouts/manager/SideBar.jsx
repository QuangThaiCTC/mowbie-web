import React from "react";
import Logo from "../../components/common/Logo";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUsers,
  faChartBar,
  faCubes,
  faTags,
} from "@fortawesome/free-solid-svg-icons";
import { NavLink } from "react-router-dom";

const SideBar = () => {
  return (
    <div className="drawer drawer-open min-h-full w-60 bg-base-100 shadow-lg py-5">
      <ul className="menu">
        <div className="flex w-full justify-center items-center">
          <Logo />
        </div>
        <li></li>
        <li className="mt-5">
          <NavLink to="/dashboard/report">
            <FontAwesomeIcon icon={faChartBar} />
            Thống kê
          </NavLink>
        </li>
        <li className="mt-5">
          <NavLink to="/dashboard/users">
            <FontAwesomeIcon icon={faUsers} />
            Danh sách người dùng
          </NavLink>
        </li>
        <li className="mt-5">
          <NavLink to="/dashboard/categories">
            <FontAwesomeIcon icon={faTags} />
            Danh sách danh mục
          </NavLink>
        </li>
        <li className="mt-5">
          <NavLink to="/dashboard/products">
            <FontAwesomeIcon icon={faCubes} />
            Danh sách sản phẩm
          </NavLink>
        </li>
      </ul>
    </div>
  );
};

export default SideBar;
