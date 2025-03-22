import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faMagnifyingGlass,
  faSortUp,
  faSortDown,
  faPlus,
} from '@fortawesome/free-solid-svg-icons';
import NotFoundItem from '../../components/common/NotFoundItem';
import CategoryItem from '../../components/manager/CategoryItem';
import { createPortal } from 'react-dom';

const CategoryList = () => {
  const API_URL = import.meta.env.VITE_API_BASE_URL;

  const [categories, setCategories] = useState([]);
  const [filteredCategories, setFilteredCategories] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'asc' });
  const [isAdding, setIsAdding] = useState(false);
  const [newCategoryName, setNewCategoryName] = useState('');
  const categoriesPerPage = 10;

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get(`${API_URL}/categories`);
        if (response.status === 200) {
          setCategories(response.data.data.categories);
          setFilteredCategories(response.data.data.categories);
        }
      } catch (error) {
        console.error('Lỗi khi tải danh mục:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchCategories();
  }, []);

  // Thêm danh mục mới
  const handleAddCategory = async () => {
    if (!newCategoryName.trim()) return;

    try {
      const accessToken = localStorage.getItem('access_token');

      const response = await axios.post(`${API_URL}/categories`, null, {
        params: { categoryName: newCategoryName },
        headers: {
          Authorization: `Bearer ${accessToken}`, // Truyền token vào headers
        },
      });

      if (response.status === 200) {
        // Fetch lại danh sách mới thay vì chỉ thêm 1 item
        const fetchResponse = await axios.get(`${API_URL}/categories`, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        if (fetchResponse.status === 200) {
          setCategories(fetchResponse.data.data.categories);
          setFilteredCategories(fetchResponse.data.data.categories);
        }

        setIsAdding(false);
        setNewCategoryName('');
      }
    } catch (error) {
      console.error('Lỗi khi thêm danh mục:', error);
      setIsAdding(false);
    }
  };

  //Cập nhật khi sửa hoặc xoá
  const handleUpdateCategory = (categoryId, newName) => {
    setCategories((prev) =>
      prev.map((cate) =>
        cate.categoryId === categoryId
          ? { ...cate, categoryName: newName }
          : cate
      )
    );
    setFilteredCategories((prev) =>
      prev.map((cate) =>
        cate.categoryId === categoryId
          ? { ...cate, categoryName: newName }
          : cate
      )
    );
  };

  const handleDeleteCategory = (categoryId) => {
    setCategories((prev) =>
      prev.filter((cate) => cate.categoryId !== categoryId)
    );
    setFilteredCategories((prev) =>
      prev.filter((cate) => cate.categoryId !== categoryId)
    );
  };

  // Xử lý tìm kiếm
  const handleSearch = (e) => {
    const keyword = e.target.value.toLowerCase();
    setSearchTerm(keyword);
    setFilteredCategories(
      keyword === ''
        ? categories
        : categories.filter((cate) =>
            cate.categoryName.toLowerCase().includes(keyword)
          )
    );
    setCurrentPage(1);
  };

  // Xử lý sắp xếp
  const handleSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });

    setFilteredCategories((prev) =>
      [...prev].sort((a, b) =>
        a[key] < b[key]
          ? direction === 'asc'
            ? -1
            : 1
          : a[key] > b[key]
          ? direction === 'asc'
            ? 1
            : -1
          : 0
      )
    );
  };

  // Phân trang
  const totalPages = Math.ceil(filteredCategories.length / categoriesPerPage);
  const currentCategories = filteredCategories.slice(
    (currentPage - 1) * categoriesPerPage,
    currentPage * categoriesPerPage
  );

  if (loading)
    return (
      <span className="loading loading-spinner loading-5xl text-accent"></span>
    );

  return (
    <div className="rounded-lg bg-base m-5 p-5 items-center">
      <div className="flex justify-between">
        <label className="input input-sm">
          <FontAwesomeIcon icon={faMagnifyingGlass} />
          <input
            type="search"
            placeholder="Tìm kiếm"
            value={searchTerm}
            onChange={handleSearch}
          />
        </label>
        <button
          className="btn btn-primary btn-sm"
          onClick={() => setIsAdding(true)}
        >
          <FontAwesomeIcon icon={faPlus} className="mr-1" />
          Thêm danh mục
        </button>
      </div>
      <br />
      <div className="w-full max-h-[calc(100vh-300px)] overflow-y-auto mt-5">
        <div className="overflow-x-auto rounded-lg bg-base-100">
          <table className="table">
            <thead>
              <tr>
                <th
                  className="cursor-pointer"
                  onClick={() => handleSort('categoryName')}
                >
                  Tên danh mục{' '}
                  {sortConfig.key === 'categoryName' && (
                    <FontAwesomeIcon
                      icon={
                        sortConfig.direction === 'asc' ? faSortUp : faSortDown
                      }
                    />
                  )}
                </th>
                <th className="text-right"></th>
              </tr>
            </thead>
            <tbody>
              {currentCategories.length > 0 ? (
                currentCategories.map((cate) => (
                  <CategoryItem
                    key={cate.categoryId}
                    cate={cate}
                    onCategoryUpdate={handleUpdateCategory}
                    onCategoryDelete={handleDeleteCategory}
                  />
                ))
              ) : (
                <tr>
                  <td colSpan={3}>
                    <NotFoundItem message={'Không tìm thấy danh mục'} />
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

      {/* Modal thêm danh mục */}
      {isAdding &&
        createPortal(
          <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-base-200 p-6 rounded-lg shadow-lg w-80 text-center">
              <h2 className="text-lg font-bold">Thêm danh mục</h2>
              <input
                type="text"
                className="input input-sm input-bordered w-full mt-2"
                value={newCategoryName}
                onChange={(e) => setNewCategoryName(e.target.value)}
                placeholder="Nhập tên danh mục"
              />
              <div className="mt-4 flex justify-center space-x-3">
                <button
                  className="btn btn-sm btn-primary"
                  onClick={handleAddCategory}
                >
                  Thêm
                </button>
                <button
                  className="btn btn-sm btn-secondary"
                  onClick={() => setIsAdding(false)}
                >
                  Hủy
                </button>
              </div>
            </div>
          </div>,
          document.body
        )}
    </div>
  );
};

export default CategoryList;
