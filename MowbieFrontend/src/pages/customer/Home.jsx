import React, { useEffect } from "react";
import Navbar from "../../layouts/customer/Navbar";
import axios from "axios";
import Banner from "../../components/customer/Banner";

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
    <div>
      <Navbar />
      <Banner />
    </div>
  );
};

export default Home;
