import { useState, useEffect } from "react";
import ProductDetailItem from "./ProductDetailItem";

const ProductItem = ({ product }) => {
  const defaultImage =
    "http://192.168.10.1:8081/api/uploads/products/no-pictures.png";
  const [imagePath, setImagePath] = useState(
    product.productImage
      ? "http://192.168.10.1:8081/api/" + product.productImage
      : defaultImage
  );
  const [productDetail, setProductDetail] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const onClickProductDetail = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await fetch("/productDetail.json");
      if (!response.ok) throw new Error("Lỗi khi tải dữ liệu!");

      const data = await response.json();
      setProductDetail(data.productDetail);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (productDetail) {
      document.getElementById("productDetail").showModal();
    }
  }, [productDetail]);

  return (
    <tr className="w-full">
      <td>
        <div className="avatar">
          <div className="mask mask-squircle h-12 w-12">
            <img src={imagePath} alt="Product Image" />
          </div>
        </div>
      </td>
      <td>{product.productName}</td>
      <td>{product.productPrice.toLocaleString()} VNĐ</td>
      <td className="text-right pr-10">
        <button
          className="btn btn-outline btn-sm btn-success w-20"
          onClick={onClickProductDetail}
          disabled={loading}
        >
          {loading ? "Đang tải..." : "Chi tiết"}
        </button>
      </td>
      {productDetail && (
        <ProductDetailItem productDetail={productDetail} error={error} />
      )}
    </tr>
  );
};

export default ProductItem;
