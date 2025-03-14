import React, { useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom"; // Import useHistory for navigation
import { toast, Toaster } from "sonner";
import { ClipLoader } from "react-spinners"; // Import spinner from react-spinners
import { API_BASE_URL } from "../../../../configAPI";

export default function EmailRegis() {
  const [email, setEmail] = useState(""); 
  const [isLoading, setIsLoading] = useState(false); // State for loading
  const history = useHistory(); 

  // Function to handle OTP request
  const handleSendOtp = async () => {
    setIsLoading(true); // Start loader
    try {
      const response = await axios.post(`${API_BASE_URL}/api/auth/register`, { email }, { headers: {
        "Access-Control-Allow-Origin": "*"
    }});

      if (response.status === 200) {
        toast("OTP đã được gửi tới email của bạn.", {
          type: 'success',
          position: 'top-right',
          duration: 3000,
          closeButton: true,
          richColors: true
        });
        localStorage.setItem("emailRG", email);
        history.push("/otpregis"); // Navigate to OTP verify page
      }
    } catch (error) {
      toast(error.response?.data || "Có lỗi xảy ra, vui lòng thử lại!", {
        type: 'error',
        position: 'top-right',
        duration: 3000,
        closeButton: true,
        richColors: true
      });
    } finally {
      setIsLoading(false); // Stop loader
    }
  };

  return (
    <>
    <div>
      <Toaster position="top-right" reverseOrder={false} />
    </div>
      <div className="px-8 mb-4 text-center">
        <h3 className="pt-4 mb-2 text-3xl">Đăng Ký Tài Khoản Mới</h3>
      </div>
      <form className="px-8 pt-6 pb-8 mb-4 bg-white rounded" onSubmit={(e) => e.preventDefault()}>
        <div className="mb-4">
          <label
            className="block mb-2 text-sm font-bold text-gray-700"
            htmlFor="email"
          >
            Email
          </label>
          <input
            className="w-full px-3 py-2 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
            id="email"
            type="email"
            placeholder="Vui lòng nhập email..."
            value={email}
            onChange={(e) => setEmail(e.target.value)} // Update state on input change
            required
          />
        </div>
        <div className="mb-6 text-center">
          <button
            className={`w-full px-4 py-2 font-bold text-white bg-red-500 rounded-full hover:bg-red-700 focus:outline-none focus:shadow-outline flex items-center justify-center ${isLoading ? "cursor-not-allowed opacity-50" : ""}`}
            type="button"
            onClick={handleSendOtp} // Call handleSendOtp when button is clicked
            disabled={isLoading} // Disable button when loading
          >
            {isLoading ? (
              <ClipLoader color="#ffffff" size={20} /> // Spinner here
            ) : (
              "Gửi OTP"
            )}
          </button>
        </div>

        <hr className="mb-6 border-t" />
        <div className="text-center">
          <a
            className="inline-block text-sm text-blue-500 align-baseline hover:text-blue-800"
            href="/login"
          >
            Đã có tài khoản!
          </a>
        </div>
        <div className="text-center">
          <a
            className="inline-block text-sm text-blue-500 align-baseline hover:text-blue-800"
            href="/forgot"
          >
            Quên mật khẩu!
          </a>
        </div>
      </form>
    </>
  );
}

