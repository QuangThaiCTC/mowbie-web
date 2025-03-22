import React, { useState } from 'react';
import { createPortal } from 'react-dom';
import axios from 'axios';
import ErrorAlert from '../common/ErrorAlert';

const CategoryItem = ({ cate, onCategoryUpdate, onCategoryDelete }) => {
  const API_URL = import.meta.env.VITE_API_BASE_URL;

  const [isEditing, setIsEditing] = useState(false);
  const [categoryName, setCategoryName] = useState(cate.categoryName);
  const [isDeleting, setIsDeleting] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertType, setAlertType] = useState('success');

  // Cập nhật danh mục
  const handleUpdate = async () => {
    try {
      const accessToken = localStorage.getItem('access_token');

      const response = await axios.put(
        `${API_URL}/categories/${cate.categoryId}`,
        null,
        {
          params: {
            categoryName: categoryName,
          },
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (response.status === 200) {
        onCategoryUpdate(cate.categoryId, categoryName);
        setAlertMessage('Cập nhật danh mục thành công!');
        setAlertType('success');
      } else {
        setAlertMessage('Cập nhật danh mục thất bại!');
        setAlertType('error');
      }
    } catch (error) {
      console.error('Lỗi cập nhật danh mục:', error);
      setAlertMessage('Không thể cập nhật danh mục!');
      setAlertType('error');
    } finally {
      setIsEditing(false);
      setTimeout(() => setAlertMessage(''), 2000);
    }
  };

  // Xóa danh mục
  const handleDelete = async () => {
    try {
      const accessToken = localStorage.getItem('access_token');

      const response = await axios.delete(
        `${API_URL}/categories/${cate.categoryId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (response.status === 200) {
        onCategoryDelete(cate.categoryId);
        setAlertMessage('Xóa danh mục thành công!');
        setAlertType('success');
      } else {
        setAlertMessage(response.data.message || 'Xóa thất bại!');
        setAlertType('error');
      }
    } catch (error) {
      console.error('Lỗi xóa danh mục:', error);
      setAlertMessage('Không thể xóa danh mục!');
      setAlertType('error');
    } finally {
      setIsDeleting(false);
      setTimeout(() => setAlertMessage(''), 2000);
    }
  };

  return (
    <>
      {alertMessage &&
        createPortal(
          <ErrorAlert message={alertMessage} type={alertType} />,
          document.body
        )}
      <tr className="w-full">
        <td>
          {isEditing ? (
            <input
              type="text"
              className="input input-sm input-bordered w-full"
              value={categoryName}
              onChange={(e) => setCategoryName(e.target.value)}
            />
          ) : (
            <div className="font-bold">{cate.categoryName}</div>
          )}
        </td>
        <td className="text-right pr-10">
          {isEditing ? (
            <button
              className="btn btn-outline btn-sm btn-primary mb-2 w-20"
              onClick={handleUpdate}
            >
              Lưu
            </button>
          ) : (
            <button
              className="btn btn-outline btn-sm btn-success mb-2 w-20"
              onClick={() => setIsEditing(true)}
            >
              Sửa
            </button>
          )}
          <br />
          <button
            className="btn btn-outline btn-sm btn-error w-20"
            onClick={() => setIsDeleting(true)}
          >
            Xóa
          </button>
        </td>
      </tr>

      {/* Portal render modal ra ngoài */}
      {isDeleting &&
        createPortal(
          <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-base-200 rounded-lg shadow-lg p-6 w-80 text-center">
              <h2 className="text-lg font-bold text-base-800">Xác nhận xóa</h2>
              <p className="text-base-600 mt-2">
                Bạn có chắc chắn muốn xóa danh mục{' '}
                <strong>{cate.categoryName}</strong> không?
              </p>
              <div className="mt-4 flex justify-center space-x-3">
                <button className="btn btn-sm btn-error" onClick={handleDelete}>
                  Xóa
                </button>
                <button
                  className="btn btn-sm btn-secondary"
                  onClick={() => setIsDeleting(false)}
                >
                  Hủy
                </button>
              </div>
            </div>
          </div>,
          document.body
        )}
    </>
  );
};

export default CategoryItem;
