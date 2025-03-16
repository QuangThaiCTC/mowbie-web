import React from "react";
import ThemeSwitcher from "../../ui/ThemeSwitcher";
import SearchBar from "../../ui/SearchBar";
import Logo from "../../ui/Logo";
import Avatar from "../../ui/Avatar";
import Notification from "../../ui/Notification";
import DropdownCategory from "../../ui/DropdownCategory";
import DropdownCart from "../../ui/DropdownCart";

const Navbar = () => {
  return (
    <>
      <div className="navbar absolute bg-base-100 px-4 lg:px-50 w-full flex justify-between items-center shadow-md">
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
