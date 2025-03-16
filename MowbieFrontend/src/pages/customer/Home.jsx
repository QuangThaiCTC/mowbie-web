import React, { useEffect } from "react";
import { Helmet } from "react-helmet";
import Navbar from "../../components/layout/customer/Navbar";
import axios from "axios";

const Home = () => {
  useEffect(() => {
    const handleAuth = async () => {
      try {
        const response = await axios.get(
          "http://192.168.10.1:8081/api/categories",
          null,
          { withCredentials: true }
        );

        if (response.status === 200) {
          const categories = response.data.data.categories;
          localStorage.setItem("categories", JSON.stringify(categories));
        }

        if (response.status === 401) {
          console.log(response.data.message);
        }
      } catch (error) {
        console.log(error);
      }
    };

    handleAuth();
  }, []);

  return (
    <>
      <Helmet>
        <link rel="icon" type="image/png" href="logo-white.png" />
        <title>Trang chủ - Mowbie</title>
      </Helmet>
      <Navbar />
      <div className="flex h-screen lg:px-50 lg:py-5 lg:pt-20 bg-base-200"></div>
    </>
  );
};

export default Home;
