import React, { useEffect, useState } from "react";

const Logo = () => {
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "light");
  const [logo, setLogo] = useState(
    theme === "light" ? "/src/assets/logo.png" : "/src/assets/logo-light.png"
  );

  useEffect(() => {
    const updateLogo = () => {
      const newTheme = localStorage.getItem("theme") || "light";
      setTheme(newTheme);
    };

    window.addEventListener("storage", updateLogo);
    return () => {
      window.removeEventListener("storage", updateLogo);
    };
  }, []);

  useEffect(() => {
    setLogo(
      theme === "light" ? "/src/assets/logo.png" : "/src/assets/logo-light.png"
    );
  }, [theme]);

  return (
    <div className="join">
      <a
        href="#"
        className="text-2xl lg:text-md font-bold text-base-content pl-2 join-item"
      >
        Mow
      </a>
      <img src={logo} alt="Logo" className="w-8 lg:w-10 join-item" />
    </div>
  );
};

export default Logo;
