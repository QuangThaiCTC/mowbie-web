import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-regular-svg-icons";

const Notification = () => {
  return (
    <div>
      <div className="dropdown">
        <div tabIndex={0} className="btn btn-ghost mb-1">
          <FontAwesomeIcon icon={faBell} size="lg" />
        </div>

        <ul
          tabIndex={0}
          className="dropdown-content z-[1] menu shadow-lg bg-base-100 rounded-box w-80 gap-2 mt-5"
        >
          <li>
            <a className="flex items-start">
              <img
                alt="Profile"
                src="/src/assets/avatar.png"
                className="w-8 rounded-full"
              />

              <div className="flex flex-col gap-2">
                <span className="text-wrap">
                  <span className="font-bold">User name</span>
                  replied on the
                  <span className="text-primary">Upload Image</span>
                  artical.
                </span>

                <span className="text-xs text-accent">2m ago</span>
              </div>
            </a>
          </li>
          <li>
            <a className="flex items-start">
              <img
                alt="Profile"
                src="/src/assets/avatar.png"
                className="w-8 rounded-full"
              />

              <div className="flex flex-col gap-2">
                <span className="text-wrap">
                  <span className="font-bold">User name</span>
                  start following you.
                </span>

                <span className="text-xs text-accent">45m ago</span>
              </div>
            </a>
          </li>
          <li>
            <a className="flex items-start">
              <img
                alt="Profile"
                src="/src/assets/avatar.png"
                className="w-8 rounded-full"
              />

              <div className="flex flex-col gap-2">
                <span className="text-wrap">
                  <span className="font-bold">User name</span>
                  like your reply on
                  <span className="text-primary">Test with TDD</span>
                  artical.
                </span>

                <span className="text-xs text-accent">3h ago</span>
              </div>
            </a>
          </li>

          <a className="btn btn-primary">Đánh dấu tất cả đã đọc</a>
        </ul>
      </div>
    </div>
  );
};

export default Notification;
