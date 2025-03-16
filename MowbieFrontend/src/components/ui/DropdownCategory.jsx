import React from "react";

const DropdownCategory = () => {
  const categories = JSON.parse(localStorage.getItem("categories")) || [];
  return (
    <div className="hidden lg:flex items-center bg-base-100">
      <div className="dropdown">
        <div tabIndex={0} role="button">
          {/* Page content here */}
          <label className="btn btn-xs btn-dash text-xs">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              width="15"
              height="15"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <line x1="4" y1="6" x2="18" y2="6" />
              <line x1="4" y1="12" x2="14" y2="12" />
              <line x1="4" y1="18" x2="15" y2="18" />
            </svg>
            Danh mục sản phẩm
          </label>
        </div>
        <ul
          tabIndex={0}
          className="dropdown-content menu bg-base-100 rounded-box z-1 w-30 p-2 mt-5 shadow-sm"
        >
          {categories.map((element) => (
            <li key={element.categoryId}>
              <a href="#">{element.categoryName}</a>
            </li>
          ))}
          <li>
            <a>Khác </a>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default DropdownCategory;
