// Navbar.tsx
import { useState } from "react";
import LoginModal from "./LoginModal";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import type { RootState } from "../redux/store";
import { logout } from "../redux/authSlice";

const Navbar = () => {
  const [showLogin, setShowLogin] = useState(false);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const auth = useSelector((state: RootState) => state.auth.isLoggedNow);

  const handleLogout = async () => {

    dispatch(logout());
  }
  return (

    <>
      <nav className="p-4 shadow flex justify-between items-center">

        <div className="text-xl font-bold">MyApp</div>
        <div className="flex gap-3">
          <button onClick={() => navigate('/register')}
            className="cursor-pointer px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition-shadow">Registrati</button>
          {
            auth == false ? <button
              onClick={() => setShowLogin(true)}
              className="cursor-pointer px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition-shadow">
              Login
            </button> :
              <button
                onClick={() => { handleLogout() }}
                className="cursor-pointer px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-shadow">
                Logout
              </button>
          }
        </div>


      </nav>

      {showLogin && (
        <LoginModal onClose={() => setShowLogin(false)} />
      )}
    </>
  );
};

export default Navbar;