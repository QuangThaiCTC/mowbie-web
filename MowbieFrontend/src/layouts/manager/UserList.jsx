import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faMagnifyingGlass,
  faSortUp,
  faSortDown,
} from '@fortawesome/free-solid-svg-icons';
import UserItem from '../../components/manager/UserItem';
import NotFoundItem from '../../components/common/NotFoundItem';

const UserList = () => {
  const API_URL = import.meta.env.VITE_API_BASE_URL;

  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'asc' });
  const usersPerPage = 10;

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const accessToken = localStorage.getItem('access_token'); // Lấy token từ localStorage
        const response = await axios.get(`${API_URL}/users`, {
          headers: {
            Authorization: `Bearer ${accessToken}`, // Thêm token vào header
          },
        });

        if (response.status === 200) {
          const fetchedUsers = response.data.data.users;
          localStorage.setItem('users', JSON.stringify(fetchedUsers));
          setUsers(fetchedUsers);
          setFilteredUsers(fetchedUsers);
        }
      } catch (error) {
        console.error('Lỗi khi tải danh sách người dùng:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  // Xử lý tìm kiếm
  const handleSearch = (e) => {
    const keyword = e.target.value.toLowerCase();
    setSearchTerm(keyword);

    if (keyword === '') {
      setFilteredUsers(users);
    } else {
      const filtered = users.filter(
        (user) =>
          user.username.toLowerCase().includes(keyword) ||
          user.email.toLowerCase().includes(keyword) ||
          user.userRole.toLowerCase().includes(keyword)
      );
      setFilteredUsers(filtered);
    }

    setCurrentPage(1); // Reset về trang đầu tiên khi tìm kiếm
  };

  // Xử lý sắp xếp
  const handleSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }

    const sortedUsers = [...filteredUsers].sort((a, b) => {
      if (a[key] < b[key]) return direction === 'asc' ? -1 : 1;
      if (a[key] > b[key]) return direction === 'asc' ? 1 : -1;
      return 0;
    });

    setSortConfig({ key, direction });
    setFilteredUsers(sortedUsers);
  };

  // Tính toán số trang
  const totalPages = Math.ceil(filteredUsers.length / usersPerPage);
  const indexOfLastUser = currentPage * usersPerPage;
  const indexOfFirstUser = indexOfLastUser - usersPerPage;
  const currentUsers = filteredUsers.slice(indexOfFirstUser, indexOfLastUser);

  if (loading) {
    <span className="loading loading-spinner loading-5xl text-accent"></span>;
  }

  return (
    <div className="rounded-lg bg-base m-5 p-5 items-center">
      <label className="input input-sm">
        <FontAwesomeIcon icon={faMagnifyingGlass} />
        <input
          type="search"
          placeholder="Tìm kiếm"
          value={searchTerm}
          onChange={handleSearch}
        />
      </label>
      <br />

      <div className="w-full max-h-[calc(100vh-300px)] overflow-y-auto mt-5">
        <div className="overflow-x-auto rounded-lg bg-base-100">
          <table className="table">
            <thead>
              <tr>
                {/* Họ và tên */}
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort('username')}
                >
                  Họ và tên{' '}
                  {sortConfig.key === 'username' &&
                    (sortConfig.direction === 'asc' ? (
                      <FontAwesomeIcon icon={faSortUp} />
                    ) : (
                      <FontAwesomeIcon icon={faSortDown} />
                    ))}
                </th>

                {/* Email */}
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort('email')}
                >
                  Email{' '}
                  {sortConfig.key === 'email' &&
                    (sortConfig.direction === 'asc' ? (
                      <FontAwesomeIcon icon={faSortUp} />
                    ) : (
                      <FontAwesomeIcon icon={faSortDown} />
                    ))}
                </th>

                {/* Số điện thoại */}
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort('phoneNumber')}
                >
                  Số điện thoại{' '}
                  {sortConfig.key === 'phoneNumber' &&
                    (sortConfig.direction === 'asc' ? (
                      <FontAwesomeIcon icon={faSortUp} />
                    ) : (
                      <FontAwesomeIcon icon={faSortDown} />
                    ))}
                </th>

                {/* Vai trò */}
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort('userRole')}
                >
                  Vai trò{' '}
                  {sortConfig.key === 'userRole' &&
                    (sortConfig.direction === 'asc' ? (
                      <FontAwesomeIcon icon={faSortUp} />
                    ) : (
                      <FontAwesomeIcon icon={faSortDown} />
                    ))}
                </th>

                {/* Cột cuối (không có sắp xếp) */}
                <th></th>
              </tr>
            </thead>
            <tbody>
              {currentUsers.length > 0 ? (
                currentUsers.map((user) => (
                  <UserItem key={user.userId} user={user} />
                ))
              ) : (
                <tr>
                  <td colSpan={5}>
                    <NotFoundItem message={'Không tìm thấy người dùng'} />
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <br />
      <div className="join">
        <button
          className="join-item btn btn-sm"
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
          disabled={currentPage === 1}
        >
          «
        </button>
        <button className="join-item btn btn-sm">
          Trang {currentPage} / {totalPages}
        </button>
        <button
          className="join-item btn btn-sm"
          onClick={() =>
            setCurrentPage((prev) => Math.min(prev + 1, totalPages))
          }
          disabled={currentPage === totalPages}
        >
          »
        </button>
      </div>
    </div>
  );
};

export default UserList;
