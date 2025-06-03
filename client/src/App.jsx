import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import Menubar from "./components/Menubar/Menubar";
import Dashboard from "./pages/Dashboard/Dashboard.jsx";
import Explore from "./pages/Explore/Explore.jsx";
import Items from "./pages/Items/Items.jsx";
import Categories from "./pages/Categories/Categories.jsx";
import Users from "./pages/Users/Users.jsx";
import { Toaster } from "react-hot-toast";
import Login from "./pages/Login/Login.jsx";
import Orders from "./components/Orders/Orders.jsx";
import { useContext } from "react";
import { AppContext } from "./context/AppContext.jsx";
import NotFound from "./pages/NotFound/NotFound.jsx";

const App = () => {
    const location = useLocation();
    const { auth } = useContext(AppContext);

    const LoginRoute = ({ element }) => {
        if (auth.token) {
            return <Navigate to='/dashboard' replace />;
        }

        return element;
    }

    const ProtectedRoute = ({ element, allowedRoles }) => {
        if (!auth.token) {
            return <Navigate to='/login' replace />;
        }

        if (allowedRoles && !allowedRoles.includes(auth.role)) {
            return <Navigate to='/dashboard' replace />;
        }

        return element;
    }

    return (
        <div>
            {location.pathname !== '/login' && <Menubar />}
            <Toaster />
            <Routes>
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/explore" element={<Explore />} />

                {/* Admin Only Routes */}
                <Route path="/items" element={<ProtectedRoute element={<Items />} allowedRoles={['ROLE_ADMIN']} />} />
                <Route path="/categories" element={<ProtectedRoute element={<Categories />} allowedRoles={['ROLE_ADMIN']} />} />
                <Route path="/users" element={<ProtectedRoute element={<Users />} allowedRoles={['ROLE_ADMIN']} />} />

                <Route path="/login" element={<LoginRoute element={<Login />} />} />
                <Route path="/orders" element={<Orders />} />
                <Route path="/" element={<Dashboard />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </div>
    );
}

export default App;