import React, { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FaTrashAlt, FaEdit, FaPlus, FaSearch, FaArrowLeft, FaArrowRight } from 'react-icons/fa';
import Swal from 'sweetalert2';
import { API_BASE_URL } from '../../../configAPI';

const CategoryList = () => {
    const [categories, setCategories] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchQuery, setSearchQuery] = useState('');
    const itemsPerPage = 10;
    const history = useHistory();

    useEffect(() => {
        fetchCategories();
    }, [currentPage]);

    const fetchCategories = () => {
        const token = localStorage.getItem('jwtToken');

        axios.get(`${API_BASE_URL}/admin/api/categories`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                  "Access-Control-Allow-Origin": "*"
            },
        })
            .then((response) => {
                setCategories(response.data);
            })
            .catch((error) => {
                console.error('Error fetching categories:', error);
                toast.error('Có lỗi xảy ra khi lấy danh sách danh mục!');
            });
    };

    const deleteCategory = (id) => {
        const token = localStorage.getItem('jwtToken');
    
        // Use SweetAlert2 for the confirmation dialog
        Swal.fire({
            title: 'Bạn có chắc chắn?',
            text: 'Danh mục này sẽ bị xóa vĩnh viễn!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                axios.delete(`${API_BASE_URL}/admin/api/categories/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                          "Access-Control-Allow-Origin": "*"
                    },
                })
                .then(() => {
                    toast.success('Danh mục đã được xóa thành công!');
                    fetchCategories();
                })
                .catch((error) => {
                    console.error('Error deleting category:', error);
                    toast.error('Có lỗi xảy ra khi xóa danh mục.');
                });
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                toast.info('Thao tác xóa đã bị hủy.');
            }
        });
    };

    const filteredCategories = categories.filter(
        (category) =>
            category.name_cate.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const totalPages = Math.ceil(filteredCategories.length / itemsPerPage);
    const currentCategories = filteredCategories.slice(
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
            <ToastContainer />
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-bold text-gray-700">Danh sách Danh mục</h2>
                <Link
                    to="/admin/create-category"
                    className="flex items-center bg-gradient-to-r from-blue-500 to-blue-600 text-white px-5 py-2 rounded-full shadow-md hover:shadow-lg transform hover:scale-105 transition"
                >
                    <FaPlus className="mr-2" />
                    Tạo Danh mục mới
                </Link>
            </div>

            <div className="relative mb-6">
                <FaSearch className="absolute left-3 top-3 text-gray-400" />
                <input
                    type="text"
                    className="pl-10 border border-gray-300 px-4 py-2 w-full rounded-lg focus:ring-2 focus:ring-blue-400 focus:border-none"
                    placeholder="Tìm kiếm theo tên danh mục"
                    value={searchQuery}
                    onChange={handleSearchChange}
                />
            </div>

            <div className="overflow-x-auto bg-white rounded-xl shadow-lg">
                <table className="min-w-full border-collapse">
                    <thead>
                        <tr className="table-header text-white text-left">
                            <th className="px-4 py-3 text-center font-semibold">STT</th>
                            <th className="px-6 py-3 font-semibold">Hình ảnh</th>
                            <th className="px-6 py-3 font-semibold">Tên Danh mục</th>
                            <th className="px-6 py-3 text-center font-semibold">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentCategories.map((category, index) => (
                            <tr
                                key={category.id}
                                className="hover:bg-gray-100 transition border-b"
                            >
                                <td className="px-4 py-4 text-center font-medium text-gray-700">
                                    {(currentPage - 1) * itemsPerPage + index + 1}
                                </td>
                                <td className="px-6 py-4">
                                    {category.cate_image ? (
                                        <img
                                            src={category.cate_image}
                                            alt={category.name_cate}
                                            className="h-16 w-16 object-cover rounded-lg border"
                                        />
                                    ) : (
                                        <span className="text-gray-500">Chưa có hình</span>
                                    )}
                                </td>
                                <td className="px-6 py-4 text-blue-600 font-medium">
                                    <button
                                        onClick={() => history.push(`/admin/update-category/${category.id}`)}
                                        className="hover:underline"
                                    >
                                        {category.name_cate}
                                    </button>
                                </td>
                                <td className="px-6 py-4 text-center">
                                    <button
                                        onClick={() => deleteCategory(category.id)}
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
                    Hiển thị {currentCategories.length} trong tổng số {filteredCategories.length} danh mục
                </div>
                <div className="flex text-left">
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

export default CategoryList;
