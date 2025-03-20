import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";
import Home from "../pages/customer/Home";
import ManagerDashboard from "../pages/manager/ManagerDashboard";
import Auth from "../pages/auth/Auth";
import Logout from "../pages/auth/Logout";
import TestComponent from "../pages/test/TestComponent";
import UserList from "../layouts/manager/UserList";
import Dashboard from "../layouts/manager/Dashboard";
import CategoryList from "../layouts/manager/CategoryList";
import ProductList from "../layouts/manager/ProductList";

const AppRoutes = () => {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/auth" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/home" element={<Home />} />
          <Route path="/dashboard/*" element={<ManagerDashboard />}>
            <Route path="report" element={<Dashboard />} />
            <Route path="users" element={<UserList />} />
            <Route path="categories" element={<CategoryList />} />
            <Route path="products" element={<ProductList />} />
          </Route>
          <Route path="/auth" element={<Auth />} />

          <Route path="/test" element={<TestComponent />} />
        </Routes>
      </Router>
    </>
  );
};

export default AppRoutes;
