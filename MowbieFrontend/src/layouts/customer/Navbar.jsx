import React from "react";
import ThemeSwitcher from "../../components/common/ThemeSwitcher";
import SearchBar from "../../components/customer/SearchBar";
import Logo from "../../components/common/Logo";
import Avatar from "../../components/common/Avatar";
import Notification from "../../components/common/Notification";
import DropdownCategory from "../../components/customer/DropdownCategory";
import DropdownCart from "../../components/customer/DropdownCart";

const Navbar = () => {
  return (
    <>
      <div className="navbar fixed top-0 left-0 w-full bg-base-100 px-4 lg:px-50 flex justify-between items-center shadow-md z-50">
        <Logo />
        <DropdownCategory />
        <SearchBar />
        <DropdownCart />
        <Notification />
        <Avatar />
        <ThemeSwitcher />
      </div>
    </>
  );
};

export default Navbar;
