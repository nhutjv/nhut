import React, { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import { toast, Toaster } from 'sonner';
// import 'react-toastify/dist/ReactToastify.css';
import { FaTrashAlt, FaEdit, FaPlus, FaSearch, FaArrowLeft, FaArrowRight } from 'react-icons/fa';
import Swal from 'sweetalert2';
import { API_BASE_URL } from '../../../configAPI';

const ListColor = () => {
    const [colors, setColors] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchQuery, setSearchQuery] = useState('');
    const itemsPerPage = 10;
    const history = useHistory();

    useEffect(() => {
        fetchColors();
    }, [currentPage]);

    const fetchColors = () => {
        const token = localStorage.getItem('jwtToken');

        axios.get(`${API_BASE_URL}/admin/api/colors`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                  "Access-Control-Allow-Origin": "*"
            },
        })
            .then((response) => {
                setColors(response.data);
            })
            .catch((error) => {
                console.error('Error fetching colors:', error);
            });
    };

    const deleteColor = (id) => {
        const token = localStorage.getItem('jwtToken');

        // Use SweetAlert2 for the confirmation dialog
        Swal.fire({
            title: 'Bạn có chắc chắn?',
            text: 'Màu sắc này sẽ bị xóa vĩnh viễn!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                axios.delete(`${API_BASE_URL}/admin/api/colors/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                          "Access-Control-Allow-Origin": "*"
                    },
                })
                    .then((response) => {
                        toast('Màu sắc đã được xóa thành công!', {
                            type: 'success',
                            position: 'top-right',
                            duration: 3000,
                            closeButton: true,
                            richColors: true
                        })
                        // toast.success(');
                        fetchColors();
                    })
                    .catch((error) => {
                        console.error('Error deleting color:', error);
                        toast('Có lỗi xảy ra khi xóa màu sắc.', {
                            type: 'error',
                            position: 'top-right',
                            duration: 3000,
                            closeButton: true,
                            richColors: true
                        })
                        // toast.error();
                    });
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                toast('Thao tác xóa đã bị hủy.', {
                    type: 'info',
                    position: 'top-right',
                    duration: 3000,
                    closeButton: true,
                    richColors: true
                })
                // toast.info('Thao tác xóa đã bị hủy.');
            }
        });
    };

    const filteredColors = colors.filter(
        (color) =>
            color.color_name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const totalPages = Math.ceil(filteredColors.length / itemsPerPage);
    const currentColors = filteredColors.slice(
        (currentPage - 1) * itemsPerPage,
        currentPage * itemsPerPage
    );

    const handlePrevPage = () => setCurrentPage((prevPage) => Math.max(prevPage - 1, 1));
    const handleNextPage = () => setCurrentPage((prevPage) => Math.min(prevPage + 1, totalPages));
    const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
        setCurrentPage(1);
    };

    return (
        <div className="p-6 bg-gradient-to-br from-gray-100 to-blue-50 min-h-screen">
            <Toaster position="top-right" reverseOrder={false} />
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-bold text-gray-700">Danh sách Màu sắc</h2>
                <Link
                    to="/admin/create-color"
                    className="flex items-center bg-gradient-to-r from-blue-500 to-blue-600 text-white px-5 py-2 rounded-full shadow-md hover:shadow-lg transform hover:scale-105 transition"
                >
                    <FaPlus className="mr-2" />
                    Thêm Màu sắc mới
                </Link>
            </div>

            <div className="relative mb-6">
                <FaSearch className="absolute left-3 top-3 text-gray-400" />
                <input
                    type="text"
                    className="pl-10 border border-gray-300 px-4 py-2 w-full rounded-lg focus:ring-2 focus:ring-blue-400 focus:border-none"
                    placeholder="Tìm kiếm theo tên màu sắc"
                    value={searchQuery}
                    onChange={handleSearchChange}
                />
            </div>

            <div className="overflow-x-auto bg-white rounded-xl shadow-lg">
                <table className="min-w-full border-collapse">
                    <thead>
                        <tr className="table-header text-white text-left">
                            <th className="px-4 py-3 text-center font-semibold">STT</th>
                            <th className="px-6 py-3 font-semibold">Màu sắc</th>
                            <th className="px-6 py-3 font-semibold">Mã màu</th>
                            <th className="px-6 py-3 text-center font-semibold">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentColors.map((color, index) => (
                            <tr
                                key={color.id}
                                className="hover:bg-gray-100 transition border-b"
                            >
                                <td className="px-4 py-4 text-center font-medium text-gray-700">
                                    {(currentPage - 1) * itemsPerPage + index + 1}
                                </td>
                                <td className="px-6 py-4">
                                    <div
                                        className="w-16 h-16 rounded-full cursor-pointer"
                                        onClick={() => history.push(`/admin/update-color/${color.id}`)}
                                        style={{
                                            backgroundColor: color.color_name,
                                        }}
                                    ></div>
                                </td>
                                <td className="px-6 py-4">{color.color_name}</td>
                                <td className="px-6 py-4 text-center">
                                    <button
                                        onClick={() => deleteColor(color.id)}
                                        className="text-red-500 hover:text-red-700"
                                    >
                                        <FaTrashAlt />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* Pagination */}
            <div className="flex justify-between items-center mt-8 space-x-3 w-full">
                <div className="flex-grow text-left mt-8">
                    Hiển thị {currentColors.length} trong tổng số {filteredColors.length} màu sắc
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
        </div>
    );
};

export default ListColor;