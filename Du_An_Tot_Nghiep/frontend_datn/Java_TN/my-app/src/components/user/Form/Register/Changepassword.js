import React, { Component } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { jwtDecode } from "jwt-decode";
import { toast, Toaster } from "sonner";
import axios from "axios";
import { withRouter } from "react-router-dom";
import { API_BASE_URL } from "../../../../configAPI";

class Changepassword extends Component {
    state = {
      isPasswordVisible: false,
      isConfirmPasswordVisible: false,
      isPasswordOldVisible: false,
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    };
  
    togglePasswordVisibility = () => {
      this.setState((prevState) => ({
        isPasswordVisible: !prevState.isPasswordVisible,
      }));
    };
  
    togglePasswordOldVisibility = () => {
      this.setState((prevState) => ({
        isPasswordOldVisible: !prevState.isPasswordOldVisible,
      }));
    };
  
    toggleConfirmPasswordVisibility = () => {
      this.setState((prevState) => ({
        isConfirmPasswordVisible: !prevState.isConfirmPasswordVisible,
      }));
    };
  
    handleChange = (e) => {
      this.setState({ [e.target.name]: e.target.value });
    };
  
    handleSubmit = async (e) => {
      e.preventDefault();
      const { oldPassword, newPassword, confirmPassword } = this.state;
      const token = localStorage.getItem("jwtToken");
  
      if (!oldPassword || !newPassword || !confirmPassword) {
        toast("Vui lòng điền đầy đủ thông tin!", {
          type: "warning",
          position: "top-right",
          duration: 3000,
          closeButton: true,
          richColors: true,
        });
        return;
      }
  
      if (newPassword !== confirmPassword) {
        toast("Mật khẩu mới và xác nhận mật khẩu không khớp!", {
          type: "warning",
          position: "top-right",
          duration: 3000,
          closeButton: true,
          richColors: true,
        });
        return;
      }
  
      if (!token) {
        toast("Token không hợp lệ hoặc đã hết hạn!", {
          type: "warning",
          position: "top-right",
          duration: 3000,
          closeButton: true,
          richColors: true,
        });
        return;
      }
  
      try {
        const decodedToken = jwtDecode(token);
        const id_user = decodedToken.id_user;
  
        const response = await axios.post(
          `${API_BASE_URL}/user/api/info/change-password`,
          { id_user, oldPassword, newPassword },
          {
            headers: { Authorization: `Bearer ${token}` },
            "Access-Control-Allow-Origin": "*" 
          }
        );
  
        if (response.status === 200) {
          toast(response.data, {
            type: "success",
            position: "top-right",
            duration: 3000,
            closeButton: true,
            richColors: true,
          });
  
          // Remove the JWT and name_prod from storage and redirect
          localStorage.removeItem("jwtToken");
          sessionStorage.removeItem("name_prod");
  
          // Redirect to login page
          this.props.history.push("/login");
        }
      } catch (error) {
        if (error.response) {
          toast(error.response.data, {
            type: "error",
            position: "top-right",
            duration: 3000,
            closeButton: true,
            richColors: true,
          });
        } else {
          toast("Có lỗi xảy ra khi thay đổi mật khẩu!", {
            type: "error",
            position: "top-right",
            duration: 3000,
            closeButton: true,
            richColors: true,
          });
        }
      }
    };
  
    render() {
      const {
        isPasswordVisible,
        isConfirmPasswordVisible,
        isPasswordOldVisible,
        oldPassword,
        newPassword,
        confirmPassword,
      } = this.state;
  
      return (
        <div>
          <div>
            <Toaster position="top-right" reverseOrder={false} />
          </div>
          <section className="bg-gray-50 dark:bg-gray-900">
            <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
              <div className="w-full p-6 bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md dark:bg-gray-800 dark:border-gray-700 sm:p-8">
                <h2 className="mb-1 text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                  Đổi mật khẩu
                </h2>
                <form
                  className="mt-4 space-y-4 lg:mt-5 md:space-y-5"
                  onSubmit={this.handleSubmit}
                >
                  {/* Mật khẩu cũ */}
                  <div>
                    <label
                      htmlFor="old-password"
                      className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >
                      Mật khẩu cũ
                    </label>
                    <div className="relative">
                      <input
                        type={isPasswordOldVisible ? "text" : "password"}
                        name="oldPassword"
                        id="old-password"
                        value={oldPassword}
                        onChange={this.handleChange}
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                      <button
                        type="button"
                        onClick={this.togglePasswordOldVisibility}
                        className="absolute inset-y-0 right-0 flex items-center pr-3"
                      >
                        {isPasswordOldVisible ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </div>
  
                  {/* Mật khẩu mới */}
                  <div>
                    <label
                      htmlFor="new-password"
                      className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >
                      Mật khẩu mới
                    </label>
                    <div className="relative">
                      <input
                        type={isPasswordVisible ? "text" : "password"}
                        name="newPassword"
                        id="new-password"
                        value={newPassword}
                        onChange={this.handleChange}
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                      <button
                        type="button"
                        onClick={this.togglePasswordVisibility}
                        className="absolute inset-y-0 right-0 flex items-center pr-3"
                      >
                        {isPasswordVisible ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </div>
  
                  {/* Xác nhận mật khẩu */}
                  <div>
                    <label
                      htmlFor="confirm-password"
                      className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                    >
                      Xác nhận mật khẩu
                    </label>
                    <div className="relative">
                      <input
                        type={isConfirmPasswordVisible ? "text" : "password"}
                        name="confirmPassword"
                        id="confirm-password"
                        value={confirmPassword}
                        onChange={this.handleChange}
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                      <button
                        type="button"
                        onClick={this.toggleConfirmPasswordVisibility}
                        className="absolute inset-y-0 right-0 flex items-center pr-3"
                      >
                        {isConfirmPasswordVisible ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </div>
  
                  <button
                    type="submit"
                    className="w-full text-white bg-primary-600 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
                  >
                    Đặt lại mật khẩu
                  </button>
                </form>
              </div>
            </div>
          </section>
        </div>
      );
    }
  }
  
  export default withRouter(Changepassword);
