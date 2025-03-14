

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaSearch, FaArrowLeft, FaArrowRight, FaEye, FaExclamationTriangle, FaCheck, FaTimes } from 'react-icons/fa';
import 'react-toastify/dist/ReactToastify.css';
import { toast, Toaster } from 'sonner';
import { API_BASE_URL } from '../../../configAPI';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [searchQuery, setSearchQuery] = useState('');
  const [loadingButtonId, setLoadingButtonId] = useState(null);
  const [warningShown, setWarningShown] = useState(new Set());
  const itemsPerPage = 10;
  const [loadingWarningId, setLoadingWarningId] = useState(null); // Trạng thái riêng cho nút cảnh báo
  const [cancelledOrders, setCancelledOrders] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [isOrderModalOpen, setIsOrderModalOpen] = useState(false);
  const [selectedMonth, setSelectedMonth] = useState(""); // Lưu trữ tháng được chọn
  // const [openOrderDetails, setOpenOrderDetails] = React.useState(null); // Lưu trạng thái đơn hàng đang mở
  const [currentOrderPage, setCurrentOrderPage] = useState(1);
  const itemsPerOrderPage = 3;

  const [openOrderDetails, setOpenOrderDetails] = React.useState([]); // Lưu trạng thái các đơn hàng đang mở





  const handleMonthChange = (event) => {
    setSelectedMonth(event.target.value);
  };


  useEffect(() => {
    fetchUsers();
  }, []);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${hours}:${minutes}:${seconds} ngày ${day}/${month}/${year}`;
  };

  useEffect(() => {
    try {
      const filtered = users.filter(user =>
        user.username.toLowerCase().includes(searchQuery.toLowerCase()) ||
        user.fullName.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setFilteredUsers(filtered);
      setCurrentPage(1);
    } catch (error) {
      console.log(error);
    }

  }, [searchQuery, users]);

  const fetchUsers = () => {
    setLoading(true);
    const token = localStorage.getItem('jwtToken');

    axios
      .get(`${API_BASE_URL}/admin/api/users`, {
        headers: { Authorization: `Bearer ${token}`, "Access-Control-Allow-Origin": "*"  },
      })
      .then(response => {

        const allUsers = response.data;

        return axios.get(`${API_BASE_URL}/admin/api/users/users/cancelled-orders`, {
          headers: { Authorization: `Bearer ${token}`, "Access-Control-Allow-Origin": "*"  },
        })
          .then(cancelledOrdersResponse => {
            console.log(cancelledOrdersResponse.data);
            const cancelledOrders = cancelledOrdersResponse.data;

            const usersWithCancelledOrders = allUsers.map(user => {
              const cancelledOrderData = cancelledOrders.find(cancelled => cancelled.userId === user.id);
              const cancelledCount = cancelledOrderData ? cancelledOrderData.cancelledOrdersCount : 0;
              const totalOrdersCount = cancelledOrderData ? cancelledOrderData.totalOrdersCount : 0;
              const cancelledTotalCash = cancelledOrderData ? cancelledOrderData.cancelledOrdersTotalCash : 0;
              const percentCancelled = cancelledOrderData ? (cancelledOrderData.cancelledOrdersCount * 100) / cancelledOrderData.totalOrdersCount : 0; //
              return {
                ...user,
                totalOrdersCount: totalOrdersCount,
                totalCancelledOrders: cancelledCount,
                cancelledTotalCash: cancelledTotalCash,
                percentCancelled: percentCancelled,
                warning: cancelledCount === 4,
              };
            });

            const filteredUsers = usersWithCancelledOrders.filter(user => user.role?.id === 2);
            setUsers(filteredUsers);
            setFilteredUsers(filteredUsers);
          });
      })
      .catch(() => setErrorMessage('Có lỗi xảy ra khi tải danh sách người dùng!'))
      .finally(() => setLoading(false));
  };

  const fetchCancelledOrders = async (userId) => {
    const token = localStorage.getItem('jwtToken');

    await axios
      .get(`${API_BASE_URL}/admin/api/users/users/${userId}/cancelled-orders`, {
        headers: { Authorization: `Bearer ${token}`, "Access-Control-Allow-Origin": "*"  },
      })
      .then(response => {
        console.log(response.data);
        setCancelledOrders(response.data);
        setSelectedUser(userId);
        setIsOrderModalOpen(true);
      })
      .catch(() => {
        toast('Không thể lấy thông tin đơn hàng đã hủy!', {
          type: 'error',
          position: 'top-right',
          duration: 3000,
          closeButton: true,
          richColors: true
        });
      });
  };

  // const handleToggleDetails = (orderId) => {
  //   setOpenOrderDetails(openOrderDetails === orderId ? null : orderId);
  // };
  const handleToggleDetails = (orderId) => {
    setOpenOrderDetails((prevOpenOrders) =>
      prevOpenOrders.includes(orderId)
        ? prevOpenOrders.filter((id) => id !== orderId) // Nếu đã mở thì xóa
        : [...prevOpenOrders, orderId] // Nếu chưa mở thì thêm
    );
  };



  const handleStatusChange = (userId, currentStatus) => {
    setLoadingButtonId(userId);
    const token = localStorage.getItem('jwtToken');

    axios
      .put(
        `${API_BASE_URL}/admin/api/users/status/${userId}`,
        { status_user: !currentStatus },
        { headers: { Authorization: `Bearer ${token}`, "Access-Control-Allow-Origin": "*"  } }
      )
      .then(response => {
        const updatedUser = response.data;
        setUsers(users.map(user =>
          user.id === userId ? { ...user, status_user: updatedUser.status_user } : user
        ));

        toast(updatedUser.status_user
          ? `Đã kích hoạt tài khoản của người dùng ${updatedUser.username}.`
          : `Đã vô hiệu hóa tài khoản của người dùng ${updatedUser.username}.`, {
          type: 'success',
          position: 'top-right',
          duration: 3000,
          closeButton: true,
          richColors: true
        });
      })

      .catch(error => {
        toast("Có lỗi xảy ra khi cập nhật trạng thái!", {
          type: 'error',
          position: 'top-right',
          duration: 3000,
          closeButton: true,
          richColors: true
        });
      })
      .finally(() => setLoadingButtonId(null));
  };


  const handleWarningUserCancellOrder = (userId) => {
    const token = localStorage.getItem("jwtToken");
    setLoadingWarningId(userId); // Start loading for the warning button

    axios.post(`${API_BASE_URL}/admin/api/users/warning`, { idUser: userId }, {
      headers: { Authorization: `Bearer ${token}`, "Access-Control-Allow-Origin": "*"  },
    })
      .then(response => {
        // Assuming the response contains the warning message and user/shop info

        setWarningShown(prev => new Set(prev.add(userId)));

        toast("Đã gửi cảnh báo đến người dùng!", {
          type: 'success',
          position: 'top-right',
          duration: 3000,
          closeButton: true,
          richColors: true
        });
      })
      .catch(() => {
        toast("Có lỗi xảy ra khi gửi cảnh báo đến người dùng!", {
          type: 'error',
          position: 'top-right',
          duration: 3000,
          closeButton: true,
          richColors: true
        });
      })
      .finally(() => {
        setLoadingWarningId(null); // End loading for the warning button
      });
  };



  const handleViewCancelDetails = (id) => {
    fetchCancelledOrders(id);
  };

  const filteredOrders = selectedMonth
    ? cancelledOrders.filter((dataDetail) => {
      const orderDate = new Date(dataDetail.updateddDate);
      return orderDate.getMonth() + 1 === parseInt(selectedMonth, 10);
    })
    : cancelledOrders;

  const totalCancelledAmount = filteredOrders.reduce(
    (total, detail) => total + detail.totalCash,
    0
  );


  const totalPages = Math.ceil(filteredUsers.length / itemsPerPage);
  const currentUsers = filteredUsers.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);


  const totalOrderPages = Math.ceil(filteredOrders.length / itemsPerOrderPage);
  const paginatedOrders = filteredOrders.slice(
    (currentOrderPage - 1) * itemsPerOrderPage,
    currentOrderPage * itemsPerOrderPage
  )


  const handlePrevPage = () => setCurrentPage(prevPage => Math.max(prevPage - 1, 1));
  const handleNextPage = () => setCurrentPage(prevPage => Math.min(prevPage + 1, totalPages));
  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className="p-6 bg-gradient-to-br from-gray-100 to-blue-50 min-h-screen">
      <div>
        <Toaster position="top-right" reverseOrder={false} />
      </div>
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-3xl font-bold text-gray-700">Quản Lý Khách Hàng</h2>
      </div>

      <div className="relative mb-6">
        <FaSearch className="absolute left-3 top-3 text-gray-400" />
        <input
          type="text"
          className="pl-10 border border-gray-300 px-4 py-2 w-full rounded-lg focus:ring-2 focus:ring-blue-400 focus:border-none"
          placeholder="Tìm kiếm người dùng..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      <div className="overflow-x-auto bg-white rounded-xl shadow-lg">
        <table className="table-auto min-w-full border-collapse">
          <thead>
            <tr className="table-header">
              <th className="px-4 py-3 text-center font-semibold">STT</th>
              <th className="px-6 py-3 font-semibold">Ảnh</th>
              <th className="px-6 py-3 font-semibold">Tên tài khoản</th>
              <th className="px-6 py-3 font-semibold">Họ và tên</th>
              {/* <th className="px-6 py-3 font-semibold">Trạng thái</th> */}
              <th className="px-6 py-3 text-center font-semibold">Tổng đơn hủy/đặt</th>
              <th className="px-6 py-3 font-semibold">Tỉ lệ hủy đơn</th>
              <th className="px-6 py-3 font-semibold">Tổng tiền đã hủy</th>
              <th className="px-6 py-3 text-center font-semibold"></th>
              <th className="px-6 py-3 text-center font-semibold">Hành động</th>
            </tr>
          </thead>
          <tbody>
            {currentUsers.map((user, index) => (
              <tr key={user.id} className="hover:bg-gray-100 transition border-b">
                <td className="px-4 py-4 text-center font-medium">{(currentPage - 1) * itemsPerPage + index + 1}</td>
                <td className="px-6 py-4">
                  <img
                    src={user.image_user || 'default-image-url.jpg'}
                    alt={user.username}
                    className="h-16 w-16 object-cover rounded-full border"
                  />
                </td>
                <td className="px-6 py-4 truncate max-w-[130px]">{user.username}</td>
                <td className="px-6 py-4 truncate max-w-[250px]">
                  <div className="flex items-center">
                    <span className="truncate max-w-[120px]"> {/* Thêm truncate và max-width */}
                      {user.fullName}
                    </span>
                    {!user.status_user ? (
                      <span className="ml-2">
                        <FaTimes />
                      </span>
                    ) : (
                      <span className="ml-2">
                        <FaCheck />
                      </span>
                    )}
                  </div>
                </td>


                {/* <td className="px-6 py-4">{user.status_user ? 'Hoạt động' : 'Không hoạt động'}</td> */}
                <td className="px-6 py-4 text-center">{user.totalCancelledOrders || 0}/{user.totalOrdersCount || 0}</td>
                <td className="px-6 py-4 text-center">
                  {user.percentCancelled.toFixed(0)}%
                </td>
                <td className="px-6 py-4 text-center">{user.cancelledTotalCash.toLocaleString() || 0}đ</td>
                <td className="px-6 py-4 text-center">
                  <button
                    onClick={() => handleViewCancelDetails(user.id)}
                    className="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded-full flex items-center space-x-2"
                  >
                    <FaEye />

                  </button>
                </td>
                <td className="px-6 py-4 text-center">
                  <div className="flex space-x-4 justify-center"> {/* Dùng flex để căn chỉnh ngang */}
                    <button
                      onClick={() => handleWarningUserCancellOrder(user.id)}
                      className="px-4 py-2 bg-yellow-500 hover:bg-yellow-600 text-white rounded-full flex items-center space-x-2"
                      disabled={loadingWarningId === user.id}
                    >
                      {loadingWarningId === user.id ? (
                        <div className="spinner-border animate-spin border-2 border-white border-t-transparent w-4 h-4 rounded-full"></div>
                      ) : (
                        <FaExclamationTriangle />
                      )}
                    </button>

                    <button
                      onClick={() => handleStatusChange(user.id, user.status_user)}
                      className={`px-4 py-2 rounded-full flex items-center space-x-2 ${user.status_user ? 'bg-red-500 hover:bg-red-600' : 'bg-green-500 hover:bg-green-600'}`}
                      disabled={loadingButtonId === user.id}
                    >
                      {loadingButtonId === user.id ? (
                        <div className="spinner-border animate-spin border-2 border-white border-t-transparent w-4 h-4 rounded-full"></div>
                      ) : (
                        <>
                          {user.status_user ? <FaTimes /> : <FaCheck />}
                        </>
                      )}
                    </button>
                  </div>
                </td>

              </tr>
            ))}
          </tbody>
        </table>
      </div>
     

      <div className="flex justify-between items-center mt-5 space-x-3 w-full">
        <div className="flex-grow text-left mt-8">
          Hiển thị {currentUsers.length} trong tổng số {filteredUsers.length} khách hàng
        </div>

        <div className="flex mt-0">
          <button
            onClick={handlePrevPage}
            disabled={currentPage === 1}
            className={`flex items-center pagination-container pagination-button 
            ${currentPage === 1
                ? 'cursor-not-allowed disabled'
                : 'bg-gradient-to-r from-blue-500 to-blue-600 text-white hover:scale-105 transform transition'
              }`}
          >
            <FaArrowLeft />
          </button>

          {[...Array(totalPages).keys()].map((num) => (
            <button
              key={num + 1}
              onClick={() => handlePageChange(num + 1)}
              className={`pagination-container pagination-button ${currentPage === num + 1
                ? 'active'
                : 'bg-gray-200 hover:bg-blue-500 hover:text-white'
                }`}
            >
              {num + 1}
            </button>
          ))}

          <button
            onClick={handleNextPage}
            disabled={currentPage === totalPages}
            className={`flex items-center pagination-container pagination-button  ${currentPage === totalPages
              ? 'disabled cursor-not-allowed'
              : 'bg-gradient-to-r from-blue-500 to-blue-600 text-white hover:scale-105 transform transition'
              }`}
          >
            <FaArrowRight className="text-center" />
          </button>
        </div>
      </div>

      <div className="flex justify-between items-center mt-8 space-x-3 w-full">
        
      </div>


      {isOrderModalOpen && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center">

          <div className="bg-white rounded-lg p-6 shadow-lg w-3/4 max-h-[80vh] overflow-y-auto">
            {/* <button
              onClick={() => setIsOrderModalOpen(false)}
              className="mt-4 px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-full"
            >
              Đóng
            </button>
            <h3 className="text-xl font-bold mb-4">Chi tiết đơn hàng đã hủy</h3> */}
            <div className="flex items-center justify-between">
              <h3 className="text-xl font-bold">Chi tiết đơn hàng đã hủy</h3>
              <button
                onClick={() => setIsOrderModalOpen(false)}
                className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-full"
              >
                X
              </button>
            </div>

            {/* Select để chọn tháng */}
            <div className="mb-4">
              <label htmlFor="monthSelect" className="block font-medium mb-2">
                Lọc theo tháng:
              </label>
              <select
                id="monthSelect"
                value={selectedMonth}
                onChange={handleMonthChange}
                className="border rounded-lg px-4 py-2 w-full"
              >
                <option value="">Tất cả các tháng</option>
                {[...Array(12).keys()].map((month) => (
                  <option key={month + 1} value={month + 1}>
                    Tháng {month + 1}
                  </option>
                ))}
              </select>
            </div>

            {paginatedOrders.length > 0 ? (
              <>
                <ul className="list-disc ml-5">
                  {paginatedOrders.map((detail, index) => (
                    <div key={index} className="mb-4 border-b pb-2">
                      <li>
                        <p>Đơn hàng ID: {detail.orderId}</p>
                        <p>Thời gian hủy: {formatDate(detail.updateddDate)}</p>
                        <p>Lí do hủy: {detail.note}</p>
                        <p>Tổng tiền đơn hàng: {detail.totalCash.toLocaleString()} VNĐ (Đã bao gồm phí vận chuyển {detail.deliveryFee.toLocaleString()})</p>


                        <button
                          onClick={() => handleToggleDetails(detail.orderId)}
                          className="mt-2 px-3 py-1 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                        >
                          {openOrderDetails.includes(detail.orderId) ? "Ẩn chi tiết" : "Xem chi tiết"}
                        </button>


                      </li>


                      {openOrderDetails.includes(detail.orderId) && (
                        <div className="mt-2 max-h-60 overflow-y-auto">
                          {detail.orderDetails.map((orderdetail, idx) => (
                            <ul key={idx} className="list-disc ml-5">
                              <li>
                                <p>Tên sản phẩm: {orderdetail.nameProduct}</p>
                                <p>
                                  Màu sắc: <span style={{ backgroundColor: orderdetail.nameColor }}>&nbsp;&nbsp;&nbsp;&nbsp;</span> ({orderdetail.nameColor})
                                </p>
                                <p>Kích cỡ: {orderdetail.nameSize}</p>
                                <p>Số lượng: {orderdetail.quantity}</p>
                                <p>Giá: {orderdetail.price.toLocaleString()} VNĐ</p>
                                <p>Giảm giá FS: {orderdetail.discountFS.toLocaleString()} VNĐ</p>
                              </li>
                            </ul>
                          ))}
                        </div>
                      )}

                    </div>
                  ))}
                </ul>

                <p className="mt-4 font-bold">
                  Tổng tiền đơn hàng hủy: {totalCancelledAmount.toLocaleString()} VNĐ
                </p>

                {/* Phân trang */}
                <div className="flex justify-center mt-4">
                  <button
                    onClick={() => setCurrentOrderPage((prev) => Math.max(prev - 1, 1))}
                    disabled={currentOrderPage === 1}
                    className="px-4 py-2 mx-1 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50"
                  >

                    Trang trước
                  </button>
                  {Array.from({ length: totalOrderPages }, (_, index) => (
                    <button
                      key={index}
                      onClick={() => setCurrentOrderPage(index + 1)}
                      className={`px-4 py-2 mx-1 ${currentOrderPage === index + 1 ? "bg-blue-600 text-white" : "bg-gray-300"
                        } rounded hover:bg-blue-500`}
                    >
                      {index + 1}
                    </button>
                  ))}
                  <button
                    onClick={() =>
                      setCurrentOrderPage((prev) => Math.min(prev + 1, totalOrderPages))
                    }
                    disabled={currentOrderPage === totalOrderPages}
                    className="px-4 py-2 mx-1 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50"
                  >
                    Trang sau

                  </button>
                </div>
              </>
            ) : (
              <p>Không có đơn hàng đã hủy.</p>
            )}



          </div>
        </div>
      )}

    </div>
  );

};

export default UserManagement;
