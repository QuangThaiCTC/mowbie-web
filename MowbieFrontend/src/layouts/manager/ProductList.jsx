import React, { useEffect, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faSortUp,
  faSortDown,
} from "@fortawesome/free-solid-svg-icons";
import NotFoundItem from "../../components/common/NotFoundItem";
import ProductItem from "../../components/manager/ProductItem";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: "asc" });
  const productsPerPage = 10;

  useEffect(() => {
    fetch("/products.json")
      .then((response) => response.json())
      .then((data) => {
        setProducts(data.products); // Dữ liệu của bạn nằm trong `products`
        setFilteredProducts(data.products); // Cập nhật danh sách hiển thị ban đầu
        setLoading(false);
      })
      .catch((error) => {
        console.error("Lỗi khi tải dữ liệu:", error);
        setLoading(false);
      });
  }, []);

  console.log(products);
  // Xử lý tìm kiếm
  const handleSearch = (e) => {
    const keyword = e.target.value.toLowerCase();
    setSearchTerm(keyword);

    if (keyword === "") {
      setFilteredProducts(products);
    } else {
      const filtered = products.filter((product) =>
        product.productName.toLowerCase().includes(keyword)
      );
      setFilteredProducts(filtered);
    }

    setCurrentPage(1); // Reset về trang đầu tiên khi tìm kiếm
  };

  // Xử lý sắp xếp
  const handleSort = (key) => {
    let direction = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }

    const sortedProducts = [...filteredProducts].sort((a, b) => {
      if (a[key] < b[key]) return direction === "asc" ? -1 : 1;
      if (a[key] > b[key]) return direction === "asc" ? 1 : -1;
      return 0;
    });

    setSortConfig({ key, direction });
    setFilteredProducts(sortedProducts);
  };

  // Tính toán số trang
  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = filteredProducts.slice(
    indexOfFirstProduct,
    indexOfLastProduct
  );

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
                <th>Hình sản phẩm</th>

                <th
                  className="cursor-pointer"
                  onClick={() => handleSort("productName")}
                >
                  Tên sản phẩm{" "}
                  {sortConfig.key === "productName" &&
                    (sortConfig.direction === "asc" ? (
                      <FontAwesomeIcon icon={faSortUp} />
                    ) : (
                      <FontAwesomeIcon icon={faSortDown} />
                    ))}
                </th>

                <th
                  className="cursor-pointer"
                  onClick={() => handleSort("productPrice")}
                >
                  Giá sản phẩm{" "}
                  {sortConfig.key === "productPrice" &&
                    (sortConfig.direction === "asc" ? (
                      <FontAwesomeIcon icon={faSortUp} />
                    ) : (
                      <FontAwesomeIcon icon={faSortDown} />
                    ))}
                </th>

                <th></th>
              </tr>
            </thead>
            <tbody>
              {console.log(currentProducts.length)}
              {currentProducts.length > 0 ? (
                currentProducts.map((product) => (
                  <ProductItem key={product.productId} product={product} />
                ))
              ) : (
                <tr>
                  <td colSpan={5}>
                    <NotFoundItem message={"Không tìm thấy sản phẩm"} />
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

export default ProductList;
