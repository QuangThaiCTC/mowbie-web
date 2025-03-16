import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "../pages/Login";
import Register from "../pages/Register";
import Home from "../pages/customer/Home";
import ManagerDashboard from "../pages/manager/ManagerDashboard";
import Auth from "../pages/Auth";
import Logout from "../pages/Logout";
import TestComponent from "../pages/TestComponent";
import UserList from "../components/layout/manager/UserList";

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
            <Route path="users" element={<UserList />} />
          </Route>
          <Route path="/auth" element={<Auth />} />

          <Route path="/test" element={<TestComponent />} />
        </Routes>
      </Router>
    </>
  );
};

export default AppRoutes;
