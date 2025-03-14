import axios from 'axios';
import { API_BASE_URL } from '../../../../configAPI';

// Tạo một instance của axios
const axiosInstance = axios.create({
    baseURL: `${API_BASE_URL}/api/v1`,
});

// Xử lý lỗi toàn cục
axiosInstance.interceptors.response.use(
    response => response, // Trả về response nếu thành công
    error => {
        // Không in lỗi ra console ở đây
        return Promise.reject(error); // Chỉ trả về lỗi mà không log
    }
);

export default axiosInstance;
