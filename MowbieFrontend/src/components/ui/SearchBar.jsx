import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

const SearchBar = () => {
  return (
    <div className="join hidden lg:flex items-center bg-base-100 rounded-lg pl-2">
      <FontAwesomeIcon icon={faMagnifyingGlass} size="lg" />
      <input
        type="text"
        placeholder="Tìm kiếm"
        className="input input-bordered input-sm join-item w-60 lg:w-120 ml-2 bg-transparent outline-none"
      />
    </div>
  );
};

export default SearchBar;
