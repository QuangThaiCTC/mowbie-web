import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";

const DropdownCart = () => {
  const user = useState(localStorage.getItem("user") || "Khách");
  const username =
    user[0].length < 10
      ? user[0]
      : JSON.parse(user[0]).username.split(" ")[
          JSON.parse(user[0]).username.split(" ").length - 1
        ];
  return (
    <div>
      {!username === "Khách" ? (
        <Link to="/">
          <button className="btn btn-ghost">
            <div className="indicator">
              <FontAwesomeIcon icon={faCartShopping} size="lg" />
              <span className="badge badge-xs badge-dash indicator-item">
                0
              </span>
            </div>
          </button>
        </Link>
      ) : (
        <Link to="/login">
          <button className="btn btn-ghost">
            <div className="indicator">
              <FontAwesomeIcon icon={faCartShopping} size="lg" />
              <span className="badge badge-xs badge-soft badge-primary indicator-item">
                0
              </span>
            </div>
          </button>
        </Link>
      )}
    </div>
  );
};

export default DropdownCart;
