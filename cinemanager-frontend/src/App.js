import "bootstrap/dist/css/bootstrap.min.css";
import Login from "./components/login-component";
import { Routes, Route, Link } from "react-router-dom";
import { useState } from "react";
import { getCurrentUser } from "./services/authentication-service";
import Register from "./components/register-component";

function App() {

  const user = getCurrentUser();
  const [currentUser, setCurrentUser] = useState();
  let showEmployeeOption = false;
  let showManagerOption = false;
  let showAdminOption = false;

  if (user) {
    setCurrentUser(user);
    showEmployeeOption = user.roles.includes("EMPLOYEE");
    showManagerOption = user.roles.includes("MANAGER");
    showAdminOption = user.roles.includes("ADMINISTRATOR");
  }

  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <Link to={"/"} className="navbar-brand">
          CineManager
        </Link>
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={"/home"} className="nav-link">
              Home
            </Link>
          </li>

          {(showEmployeeOption || showAdminOption || showManagerOption) && (
            <li className="nav-item">
              <Link to={"/employee"} className="nav-link">
                Moderator Board
              </Link>
            </li>
          )}


          {(showManagerOption || showAdminOption) && (
            <li className="nav-item">
              <Link to={"/manager"} className="nav-link">
                Moderator Board
              </Link>
            </li>
          )}

          {showAdminOption && (
            <li className="nav-item">
              <Link to={"/admin"} className="nav-link">
                Admin Board
              </Link>
            </li>
          )}

          {currentUser && (
            <li className="nav-item">
              <Link to={"/user"} className="nav-link">
                User
              </Link>
            </li>
          )}
        </div>

        {currentUser ? (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.username}
              </Link>
            </li>
            <li className="nav-item">
              <a href="/login" className="nav-link" onClick={this.logOut}>
                LogOut
              </a>
            </li>
          </div>
        ) : (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
                Login
              </Link>
            </li>

            <li className="nav-item">
              <Link to={"/register"} className="nav-link">
                Sign Up
              </Link>
            </li>
          </div>
        )}
      </nav>
      <div className="container mt-3">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
