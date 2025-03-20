import { useState } from "react";

const ProductCarousel = ({ productImages }) => {
  const [currentSlide, setCurrentSlide] = useState(0);

  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % productImages.length);
  };

  const prevSlide = () => {
    setCurrentSlide((prev) =>
      prev === 0 ? productImages.length - 1 : prev - 1
    );
  };

  return (
    <div className="relative w-full">
      <div className="carousel w-full">
        {productImages.map((img, index) => (
          <div
            key={index}
            className={`carousel-item w-full ${
              index === currentSlide ? "block" : "hidden"
            }`}
          >
            <img
              src={`http://192.168.10.1:8081/api/${img}`}
              className="w-full object-cover rounded-lg"
              alt={`Product ${index + 1}`}
            />
          </div>
        ))}
      </div>

      {/* Nút điều hướng */}
      <div className="absolute left-5 right-5 top-1/2 flex -translate-y-1/2 transform justify-between">
        <button onClick={prevSlide} className="btn btn-circle">
          ❮
        </button>
        <button onClick={nextSlide} className="btn btn-circle">
          ❯
        </button>
      </div>
    </div>
  );
};

export default ProductCarousel;
