import React, { useEffect, useState } from 'react';
import LazyLoad from 'react-lazyload';
import { Link } from 'react-router-dom';
import LoadingSpinner from '../../../LoadingSpinner';
import { Collapse, Dropdown, Ripple, Carousel, Input, initTWE } from "tw-elements";
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { API_BASE_URL } from '../../../configAPI';
const ProductList = ({ onProductClick }) => {
    const [products, setProducts] = useState([]);
    const [likedProducts, setLikedProducts] = useState([]);
    const [priceRanges, setPriceRanges] = useState({});
    const [visibleProductsCount, setVisibleProductsCount] = useState(12);
    const [loading, setLoading] = useState(true);
    const [loadingMore, setLoadingMore] = useState(false);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const token = localStorage.getItem("jwtToken");
                let productRes;

                if (token) {
                    // Nếu có token, giải mã token để lấy id_user
                    const decodedToken = jwtDecode(token);
                    const id_user = decodedToken.id_user;

                    // Lấy sản phẩm đã thích của người dùng
                    const likedProductsRes = await axios.get(`${API_BASE_URL}/user/api/like/favorites/${id_user}`, {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Access-Control-Allow-Origin": "*" 
                        },
                    });
                    setLikedProducts(likedProductsRes.data);

                    // Lấy tất cả sản phẩm
                    productRes = await axios.get(`${API_BASE_URL}/user/api/products1`,{
                        headers: {
                            "Access-Control-Allow-Origin": "*"
                        }
                    });
                    const allProducts = productRes ? productRes.data : [];

                    // Ghép các sản phẩm đã thích lên đầu, còn lại là các sản phẩm khác
                    const sortedProducts = [
                        ...likedProductsRes.data,
                        ...allProducts.filter(product => !likedProductsRes.data.some(liked => liked.id === product.id)),
                    ];

                    setProducts(sortedProducts);
                } else {
                    // Nếu không có token, chỉ lấy tất cả sản phẩm
                    productRes = await axios.get(`${API_BASE_URL}/user/api/products1`,{
                        headers: {
                            "Access-Control-Allow-Origin": "*"
                        }
                    });
                    setProducts(productRes.data);
                }
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };

        fetchProducts();
    }, []);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get(`${API_BASE_URL}/user/api/products1`,{
                    headers: {
                        "Access-Control-Allow-Origin": "*"
                    }
                });
                setProducts(response.data);
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };
        fetchProducts();
    }, []);

    useEffect(() => {
        products.forEach(product => {
            axios.get(`${API_BASE_URL}/user/api/variants/price-range/${product.id}`,{
                headers: {
                    "Access-Control-Allow-Origin": "*"
                }
            })
                .then(response => {
                    setPriceRanges(prevState => ({
                        ...prevState,
                        [product.id]: response.data
                    }));
                })
                .catch(error => {
                    console.error('Error fetching price range:', error);
                });
        });
    }, [products]);

    const handleShowMore = () => {
        setTimeout(() => {
            setVisibleProductsCount(prevCount => prevCount + 8);
            setLoadingMore(false);
        }, 100);
    };

    return (
        <div className="mt-3">
            <h2 className="grid grid-cols-2 md:grid-cols-4 gap-x-6 gap-y-8 mx-auto max-w-7xl px-2 py-4 sm:px-2 sm:py-4 text-xl font-bold text-start mb-4">Sản phẩm mới</h2>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-x-6 gap-y-8 mx-auto max-w-7xl px-2 py-4 sm:px-2 sm:py-4">
                {products.slice(0, visibleProductsCount).map((item, index) => (
                    <div className="relative group" key={index}>
                        <LazyLoad height={200} offset={100} once>
                            {likedProducts.some(liked => liked.id === item.id) && (
                                <div className="absolute top-2 left-0 bg-black text-white px-2 py-1 text-sm ">

                                    Đề xuất cho bạn
                                </div>
                            )}
                            <div className="aspect-w-1 aspect-h-1 w-full overflow-hidden rounded-sm bg-gray-200 group-hover:opacity-75">
                                <img
                                    onClick={() => onProductClick(item)}
                                    src={item.image_prod}
                                    alt={item.name_prod}
                                    className="object-cover object-center w-full h-full cursor-pointer transition-all duration-500"
                                    style={{ opacity: 0 }}
                                    onLoad={(e) => e.target.style.opacity = 1} // Hiệu ứng fade-in
                                />
                            </div>
                        </LazyLoad>
                        <div className="mt-4 text-left">
                            <h3 className="text-left text-sm text-gray-700">
                                <button
                                    onClick={() => onProductClick(item)}
                                    className="hover:text-red-600 transition-colors duration-200"
                                >
                                    {item.name_prod}
                                </button>
                            </h3>
                            <p className="text-sm text-red-600 font-bold">
                                {priceRanges[item.id]
                                    ? (priceRanges[item.id].minPrice === priceRanges[item.id].maxPrice
                                        ? `${priceRanges[item.id].minPrice.toLocaleString()} đ`
                                        : `${priceRanges[item.id].minPrice.toLocaleString()} đ - ${priceRanges[item.id].maxPrice.toLocaleString()} đ`
                                    )
                                    : 'Đang tải...'}
                            </p>
                        </div>
                    </div>
                ))}
            </div>

            {/* Nếu sản phẩm đã thích, hiển thị dòng "Có thể bạn quan tâm" */}
            {/* Phần ảnh sản phẩm */}
            {/* bo góc rounded-md */}
            {/* Hiển thị khoảng giá */}
            {/* Phần thông tin sản phẩm */}
            {/* <div className="grid grid-cols-2 md:grid-cols-4 gap-x-6 gap-y-8 mx-auto max-w-7xl px-2 py-4 sm:px-2 sm:py-4">
                    {products.slice(0, visibleProductsCount).map((item, index) => (
                        <div className="relative group" key={index}>
                           
                            {likedProducts.some(liked => liked.id === item.id) && (
                                <div className="absolute top-2 left-0 bg-black text-white px-2 py-1 text-sm "> 
                                
                                    Đề xuất cho bạn
                                </div>
                            )}

                           
                            <div className="aspect-w-1 aspect-h-1 w-full overflow-hidden rounded-md bg-gray-200 group-hover:opacity-75">
                                <img
                                    onClick={() => onProductClick(item)}
                                    src={item.image_prod}
                                    alt={item.name_prod}
                                    className="object-cover object-center w-full h-full group-hover:opacity-75 cursor-pointer"
                                />
                            </div>
                            
                            <div className="mt-4 text-left">
                                <h3 className="text-left text-gray-700">
                                    <button onClick={() => onProductClick(item)}>
                                        {item.name_prod}
                                    </button>
                                </h3>

                                
                                <p className="text-lg text-red-600 font-bold">
                                    {priceRanges[item.id]
                                        ? (priceRanges[item.id].minPrice === priceRanges[item.id].maxPrice
                                            ? `${priceRanges[item.id].minPrice.toLocaleString()} đ`
                                            : `${priceRanges[item.id].minPrice.toLocaleString()} đ - ${priceRanges[item.id].maxPrice.toLocaleString()} đ`
                                        )
                                        : 'Đang tải...'}
                                </p>
                            </div>
                        </div>
                    ))}
                </div> */}

            {visibleProductsCount < products.length && (
                <div className="flex justify-center items-center mt-6 mb-4">
                    {loadingMore ? (
                        <LoadingSpinner />
                    ) : (
                        <button
                            onClick={handleShowMore}
                            className="text-red-600 border border-red-600 px-4 py-2 rounded-lg hover:bg-red-100 transition-all"
                        >
                            <Link to="/search">Xem tất cả</Link>
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default ProductList;



