import React, { useState } from "react";
import { createPortal } from "react-dom";
import ProductCarousel from "../common/ProductCarousel";

const ProductDetailItem = ({ productDetail }) => {
  const [isEditing, setIsEditing] = useState(false);

  if (!productDetail) return null;

  const closeModal = () => {
    setIsEditing(false); // Reset trạng thái khi đóng modal
    document.getElementById("productDetail").close();
  };

  return createPortal(
    <dialog
      id="productDetail"
      className="modal modal-bottom sm:modal-middle"
      tabIndex="-1"
    >
      <div className="modal-box absolute flex flex-col max-h-[calc(100vh-100px)] min-w-[calc(60vw)] p-6 bg-base-200">
        <div className="flex flex-wrap p-6">
          <div className="w-1/3 pr-6 flex flex-col">
            {/* Hình sản phẩm */}
            <ProductCarousel productImages={productDetail.productImages} />

            {/* Mô tả */}
            {isEditing ? (
              <textarea
                className="mt-4 w-full border p-2 text-base-600"
                defaultValue={productDetail.productDescription}
              />
            ) : (
              <p className="mt-4 text-base-600">
                {productDetail.productDescription}
              </p>
            )}
          </div>

          {/* Cột bên phải */}
          <div className="w-2/3 pl-6 flex flex-col">
            {/* Tên + Giá */}
            <div className="mb-4">
              {isEditing ? (
                <>
                  <input
                    className="w-full border p-2 text-xl font-bold text-base-600 mb-2"
                    defaultValue={productDetail.productName}
                  />
                  <input
                    className="w-full border p-2 text-lg font-semibold text-base-600"
                    defaultValue={productDetail.productPrice}
                  />
                </>
              ) : (
                <>
                  <h2 className="text-xl font-bold">
                    {productDetail.productName}
                  </h2>
                  <p className="text-lg text-red-500 font-semibold">
                    {productDetail.productPrice.toLocaleString()} VNĐ
                  </p>
                </>
              )}
            </div>

            {/* Thông số kỹ thuật */}
            <h2 className="font-bold mb-2">Thông số kỹ thuật</h2>
            <div className="overflow-y-auto max-h-[calc(100vh-350px)] rounded-lg border border-gray-200 p-4 bg-base-100 shadow-lg">
              {productDetail.productSpecs.map((spec, index) => (
                <div key={index} className="mb-2">
                  <p className="font-semibold">{spec.specTitle}:</p>
                  {isEditing ? (
                    <input
                      className="w-full border p-1 text-sm text-base-600"
                      defaultValue={spec.specContent}
                    />
                  ) : (
                    <p className="text-sm text-base-600 whitespace-pre-line">
                      {spec.specContent}
                    </p>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Số lượng tồn kho + Nút đóng + Nút chỉnh sửa */}
        <div className="mt-6 flex justify-between items-center">
          <p className="text-sm text-base-500">
            Số lượng trong kho:{" "}
            <span className="font-bold">{productDetail.stockQuantity}</span>
          </p>
          <div className="flex gap-4">
            {isEditing ? (
              <button
                className="btn btn-outline btn-success"
                onClick={() => setIsEditing(false)}
              >
                Lưu
              </button>
            ) : (
              <button
                className="btn btn-outline btn-primary"
                onClick={() => setIsEditing(true)}
              >
                Chỉnh sửa
              </button>
            )}
            <button className="btn btn-outline btn-error" onClick={closeModal}>
              Đóng
            </button>
          </div>
        </div>
      </div>
    </dialog>,
    document.body
  );
};

export default ProductDetailItem;
