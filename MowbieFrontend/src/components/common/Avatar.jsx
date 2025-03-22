import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Profile from './Profile';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import { faArrowRightToBracket } from '@fortawesome/free-solid-svg-icons';

const Avatar = () => {
  const API_URL = import.meta.env.VITE_API_BASE_URL;

  const user = useState(localStorage.getItem('user') || 'Khách');
  const [avatar, setAvatar] = useState(localStorage.getItem('avatar'));
  const username =
    user[0].length < 10
      ? user[0]
      : JSON.parse(user[0]).username.split(' ')[
          JSON.parse(user[0]).username.split(' ').length - 1
        ];
  return (
    <>
      <details className="dropdown dropdown-end">
        <summary className="btn btn-ghost btn-circle avatar">
          <div className="w-8 h-8 rounded-full bg-base-300 flex items-center justify-center">
            <img
              src={avatar || setAvatar(`${API_URL}/uploads/users/default.png`)}
              alt="Avatar"
            />
          </div>
        </summary>
        <ul className="menu menu-sm dropdown-content bg-base-100 rounded-box z-10 mt-5 w-40 p-2 shadow">
          {username !== 'Khách' ? (
            <>
              <li>
                <Profile />
              </li>
              <li>
                <Link to="/logout">
                  <FontAwesomeIcon icon={faArrowRightFromBracket} />
                  Đăng xuất
                </Link>
              </li>
            </>
          ) : (
            <>
              <li>
                <Link to="/login">
                  <FontAwesomeIcon icon={faArrowRightToBracket} />
                  Đăng nhập
                </Link>
              </li>
            </>
          )}
        </ul>
      </details>
      <span className="text-sm font-bold hidden md:inline">{username}</span>
    </>
  );
};

export default Avatar;
