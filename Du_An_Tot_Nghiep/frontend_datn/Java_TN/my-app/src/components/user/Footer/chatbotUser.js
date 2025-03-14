import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ReactMarkdown from 'react-markdown';
import { AiOutlineMessage } from 'react-icons/ai';
import { jwtDecode } from 'jwt-decode';
import { Toaster, toast } from 'sonner';
import { API_BASE_URL } from '../../../configAPI';

const STATISTICS_API_URL = `${API_BASE_URL}/user/api/products1/all`;
// const GEMINI_API_URL = 'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-002:generateContent';
const GEMINI_API_URL = 'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent';
const API_KEY = process.env.REACT_APP_GEMINI_API_KEY;

const Chatbot = () => {
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([]);
  const [typingMessage, setTypingMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [statistics, setStatistics] = useState(null);
  const [isOpen, setIsOpen] = useState(false);
  const [dimensions, setDimensions] = useState({ width: 320, height: 450 });
  const [startPos, setStartPos] = useState({ x: 0, y: 0 });

  useEffect(() => {
    const fetchStatistics = async () => {
      const cachedData = getStatisticsFromCache();
      if (cachedData) {
        setStatistics(cachedData);
        return;
      }

      try {
        const response = await axios.get(STATISTICS_API_URL);
        setStatistics(response.data);
        cacheStatistics(response.data);
      } catch (error) {
        console.error('Error fetching statistics:', error);
      }
    };

    fetchStatistics();
  }, []);

  const getStatisticsFromCache = () => {
    const cachedData = localStorage.getItem('statistics');
    if (cachedData) {
      const parsedData = JSON.parse(cachedData);
      const expirationTime = parsedData.expirationTime || 0;
      if (Date.now() - expirationTime < 3600000) {
        return parsedData.data;
      }
    }
    return null;
  };

  const cacheStatistics = (data) => {
    const expirationTime = Date.now();
    const cachedData = { data, expirationTime };
    localStorage.setItem('statistics', JSON.stringify(cachedData));
  };

  const handleInputChange = (e) => setInput(e.target.value);

  const typeOutMessage = async (text) => {
    setTypingMessage('');
    for (let i = 0; i < text.length; i++) {
      setTypingMessage((prev) => prev + text[i]);
      await new Promise((resolve) => setTimeout(resolve, 5)); //dl tx
    }
    setTypingMessage('');
  };
  const handleSendMessage = async () => {
    if (!input.trim() || !statistics || loading) return;

    const userMessage = input.trim();
    setMessages([...messages, { type: 'user', text: userMessage }]);
    setInput('');
    setLoading(true);

    const token = localStorage.getItem('jwtToken');
    if (!token) {
      console.error("Token không tồn tại trong localStorage");
      return;
    }

    let decoded;
    try {
      decoded = jwtDecode(token);  // Giải mã token JWT
    } catch (error) {
      console.error("Lỗi khi giải mã token:", error);
      return;
    }

    const userId = decoded.id_user; // Lấy userId từ decoded token
    const ten = decoded.hoten || 'bạn';  // Lấy tên người dùng hoặc 'bạn' nếu không có tên

    // Gửi yêu cầu đến API để lấy đơn hàng của người dùng
    try {
  



    

      const context = `### Dữ liệu của trang web tôi:\n
     - **Thông tin khách hàng**: ${ten}
      - ** dưới đây là dữ liệu mà tôi đưa cho bạn để bạn đặt mình vào người bán hàng tư vấn cho khách hàng, hãy trả lời tinh tế ngắn gọn nhưng đủ ý và cà khịa khách hàng**
     -**các dữ liệu phía dưới nếu trả về phải trả về dạng dễ đọc, giới thiệu đây là trang web thời trang với các mặt hàng quần áo thời trang**
      -**tên trang web là MAOU hãy xưng hô là MAOU, tên khách hàng là: ${ten} nếu không có thì gọi là bạn **
     -**khi trả lời tôi cần trả lời chính xác và sát với câu hỏi nhất nếu không cần giải thích thì đừng giải thích chỉ khi hỏi mới giải thích*
  - **đây là dữ liệu Sản phẩm và biến thể chi tiết như sau: productId là mã sản phẩm, productName là tên sản phẩm, productDescription là mô tả sản phẩm thường thì trả lời không cần trả lời cái này chỉ hiện ra nếu được yêu cầu, productQuantity là số lượng tồn kho , categoryName là loại sản phẩm, brandName là thương hiệu sản phẩm, variantId là mã biến thể sản phẩm, variantQuantity là số lượng biến thể dựa theo mã biến thể,variantPrice là giá của biến thể(nó có thể tính bằng k ví dụ 1000đ là 1k), colorName là màu của biến thể nếu trả lời hãy chuyển đổi mã màu đó sang dạng chữ , sizeName là kích thước biến thể nếu có trả lời hãy chuyển đổi sang kiểu dd-MM-yyyy, tóm lại khi trả lời dữ liệu đừng trả về dạng json mà dạng text dễ đọc và nhớ xuống hàng mỗi cái nếu cần, những câu hỏi liên quan đến sản phẩm của trang web xin vui lòng dựa vào dữ liệu để trả lời nếu người dùng hỏi giá cả hoặc số lượng của sản phẩm đó vv, và nếu đang hỏi tới một sản phẩm hãy gắn và hiện link như sau để người dùng nhấp vào Vui lòng truy cập vào [MAOU](https://maou.id.vn/product/x) để xem thông tin chi tiết,x là id sản phẩm **: ${JSON.stringify(statistics.products)}
- **voucher và chi tiết voucher  thì id là mã voucher, code là mã code dùng để áp dụng voucher , discountValue là phần trăm giảm giá của voucher đó , expirationDate là hạn sử dụng của voucher, typeVoucherName là loại voucher  nếu nó là ORDER có nghĩa là nó sẽ giảm giá cho tổng tiền của sản phẩm còn nếu là SHIPPING  thì nó sẽ giảm giá phí vận chuyển**: ${JSON.stringify(statistics.vouchers)}
 - ** flash sale và chi tiết thì id là mã flashsale, name_FS là tên chương trình giảm giá, variants.variantId  và  có nghĩa là mã biến thể đang được giảm giá(bạn có thể dựa vào mã này để lấy ra chi tiết biến thể nếu người dùng cần có thể tìm trong ${JSON.stringify(statistics.products)}) và discountPercent phần trăm được giảm giá**: ${JSON.stringify(statistics.flashSales)}
  - ** thông tin liên hệ của chủ shop là GMAIL= nnhut2705@gmai.com SĐT & ZALO= 0779602365 (nếu khách hàng không hiểu cần tư vấn hãy đưa thông tin này đừng hỏi cái gì cũng đưa), địa chỉ shop trên gg map = Vui lòng truy cập vào [MAOU](https://maps.app.goo.gl/ex85aUtCd8LZA3Gp7) hoặc 10 Đường số 3, khu dân cư Metro, Ninh Kiều, Cần Thơ 902070, Vietnam **
  - **nếu người dùng hỏi cách đặt hàng thì hãy hướng dẫn như sau: bước 1: tìm sản phẩm bằng công cụ tìm kiếm có tích hợp tìm bằng giọng nói,bước 2: thêm sản phẩm vào giỏ hàng, bước 3: ấn đặt hàng, có thể ấn đặt ngay trong chi tiết sản phẩm mà không cho vào giỏ hàng, kiểm tra đơn hàng bằng cách ấn vào biểu tượng avatar chọn tài khoản chọn xem đơn hàng** 
         - **gợi ý size cho người dùng BẢNG SIZE CHUNG CHO NỮ (Đơn vị: cm, kg)
Size XS:
Chiều cao: 147-153
Cân nặng: 38-43 kg
Vòng ngực: 74-80
Vòng mông: 82-88
Size S:
Chiều cao: 150-155
Cân nặng: 41-46 kg
Vòng ngực: 79-82
Vòng mông: 88-90
Size M:
Chiều cao: 155-163
Cân nặng: 47-52 kg
Vòng ngực: 82-87
Vòng mông: 90-94
Size L:
Chiều cao: 160-165
Cân nặng: 53-58 kg
Vòng ngực: 88-94
Vòng mông: 94-98
Size XL:
Chiều cao: 162-166
Cân nặng: 59-64 kg
Vòng ngực: 94-99
Vòng mông: 98-102
BẢNG SIZE CHUNG CHO NAM (Đơn vị: cm, kg)
Size S:
Chiều cao: 162-168
Cân nặng: 57-62 kg
Vòng ngực: 84-88
Vòng mông: 85-89
Size M:
Chiều cao: 169-173
Cân nặng: 63-67 kg
Vòng ngực: 88-94
Vòng mông: 90-94
Size L:
Chiều cao: 171-175
Cân nặng: 68-72 kg
Vòng ngực: 94-98
Vòng mông: 95-99
Size XL:
Chiều cao: 173-177
Cân nặng: 73-77 kg
Vòng ngực: 98-104
Vòng mông: 100-104
Size XXL:
Chiều cao: 175-179
Cân nặng: 78-82 kg
Vòng ngực: 104-107
Vòng mông: 104-108
BẢNG SIZE CHUNG CHO TRẺ EM (Đơn vị: cm, kg)
Size 98 (2-2Y):
Chiều cao: 95-101
Cân nặng: 13-15 kg
Size 104 (3-4Y):
Chiều cao: 101-107
Cân nặng: 15-18 kg
Size 110 (4-5Y):
Chiều cao: 107-113
Cân nặng: 18-22 kg
Size 116 (6Y):
Chiều cao: 113-119
Cân nặng: 22-25 kg
Size 122 (7Y):
Chiều cao: 119-125
Cân nặng: 25-28 kg
Size 128 (8Y):
Chiều cao: 125-131
Cân nặng: 28-32 kg
Size 134 (9Y):
Chiều cao: 131-137
Cân nặng: 32-36 kg
Size 140 (10-11Y):
Chiều cao: 137-145
Cân nặng: 36-39 kg
Size 152 (11-12Y):
Chiều cao: 145-157
Cân nặng: 39-46 kg
Size 164 (13-14Y):
Chiều cao: 157-169
Cân nặng: 46-55 kg**:
        ### Câu hỏi từ người dùng:
        ${userMessage}`;

      const geminiResponse = await axios.post(
        `${GEMINI_API_URL}?key=${API_KEY}`,
        {
          contents: [{ parts: [{ text: context }] }],
        },
        { headers: { 'Content-Type': 'application/json' } }
      );

      const botReply = geminiResponse.data.candidates?.[0]?.content?.parts?.[0]?.text || 'Không có phản hồi hợp lệ từ chatbot';
      await typeOutMessage(botReply);

      setMessages((prevMessages) => [
        ...prevMessages,
        { type: 'bot', text: botReply },
      ]);

    } catch (error) {
      console.error('Error fetching orders or generating response:', error);
      const errorMessage = 'Xin lỗi, hiện tại đang đầy vui lòng thử lại sau.';
      await typeOutMessage(errorMessage);
      setMessages((prevMessages) => [
        ...prevMessages,
        { type: 'bot', text: errorMessage },
      ]);
    } finally {
      setLoading(false);
    }
  };



  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSendMessage();
    }
  };

  const toggleChatbot = () => {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      toast('Bạn cần đăng nhập để sử dụng chatbot');
      return;
    }

    if (!isOpen) {
      setMessages((prevMessages) => [
        ...prevMessages,
        { type: 'bot', text: 'Chào mừng bạn đến với MAOU! Mình có thể giúp gì cho bạn hôm nay?' },
      ]);
    } else {
      setMessages((prevMessages) =>
        prevMessages.filter((message) => message.text !== 'Chào mừng bạn đến với MAOU! Mình có thể giúp gì cho bạn hôm nay?')
      );
    }
    setIsOpen(!isOpen);
  };

  const startResize = (e, direction) => {
    e.preventDefault();
    setStartPos({ x: e.clientX, y: e.clientY });

    const onMouseMove = (event) => handleResize(event, direction);
    const onMouseUp = () => stopResize(onMouseMove, onMouseUp);

    document.addEventListener('mousemove', onMouseMove);
    document.addEventListener('mouseup', onMouseUp);
  };

  const handleResize = (e, direction) => {
    let newWidth = dimensions.width;
    let newHeight = dimensions.height;

    if (direction.includes('right')) {
      newWidth = Math.min(600, Math.max(320, dimensions.width + (e.clientX - startPos.x)));
    } else if (direction.includes('left')) {
      newWidth = Math.min(600, Math.max(320, dimensions.width - (e.clientX - startPos.x)));
    }

    if (direction.includes('bottom')) {
      newHeight = Math.min(600, Math.max(350, dimensions.height + (e.clientY - startPos.y)));
    } else if (direction.includes('top')) {
      newHeight = Math.min(600, Math.max(350, dimensions.height - (e.clientY - startPos.y)));
    }

    setDimensions({ width: newWidth, height: newHeight });
  };

  const stopResize = (onMouseMove, onMouseUp) => {
    document.removeEventListener('mousemove', onMouseMove);
    document.removeEventListener('mouseup', onMouseUp);
  };

  //s ck
  useEffect(() => {
    const savedMessages = JSON.parse(localStorage.getItem('messages'));
    if (savedMessages) {
      setMessages(savedMessages);
    }
  }, []);

  useEffect(() => {
    if (messages.length > 0) {
      localStorage.setItem('messages', JSON.stringify(messages));
    }
  }, [messages]);



  return (
    <div>
      <button
        onClick={toggleChatbot}
        className="fixed bottom-5 right-5 bg-gradient-to-r from-blue-500 to-blue-700 hover:from-blue-600 hover:to-blue-800 text-white p-3 rounded-full shadow-lg transform transition-all duration-300 hover:scale-110 z-50 flex items-center justify-center"
      >
        {isOpen ? <AiOutlineMessage size={24} /> : <AiOutlineMessage size={24} />}
      </button>

      {isOpen && (
        <div
          className="fixed bottom-20 right-5 bg-white shadow-xl rounded-lg flex flex-col z-50"
          style={{ width: dimensions.width, height: dimensions.height }}
        >
          {/* Vùng kéo dãn ở các cạnh */}
          <div
            onMouseDown={(e) => startResize(e, 'top')}
            className="absolute top-0 left-0 w-full h-1 cursor-ns-resize"
          />
          <div
            onMouseDown={(e) => startResize(e, 'bottom')}
            className="absolute bottom-0 left-0 w-full h-1 cursor-ns-resize"
          />
          <div
            onMouseDown={(e) => startResize(e, 'left')}
            className="absolute top-0 left-0 h-full w-1 cursor-ew-resize"
          />
          <div
            onMouseDown={(e) => startResize(e, 'right')}
            className="absolute top-0 right-0 h-full w-1 cursor-ew-resize"
          />

          {/* Vùng kéo dãn ở các góc */}
          <div
            onMouseDown={(e) => startResize(e, 'top-left')}
            className="absolute top-0 left-0 w-3 h-3 cursor-nwse-resize"
          />
          <div
            onMouseDown={(e) => startResize(e, 'top-right')}
            className="absolute top-0 right-0 w-3 h-3 cursor-nesw-resize"
          />
          <div
            onMouseDown={(e) => startResize(e, 'bottom-left')}
            className="absolute bottom-0 left-0 w-3 h-3 cursor-nesw-resize"
          />
          <div
            onMouseDown={(e) => startResize(e, 'bottom-right')}
            className="absolute bottom-0 right-0 w-3 h-3 cursor-nwse-resize"
          />

          <div className="flex-1 p-4 overflow-y-auto">
            {messages.map((msg, index) => (
              <div className={`p-2 mb-2 rounded-lg ${msg.type === 'user' ? 'bg-blue-100 self-end text-right' : 'bg-gray-100 self-start text-left'} shadow`}>
                {msg.type === 'bot' ? (
                  <ReactMarkdown
                    components={{
                      a: ({ ...props }) => (
                        <a {...props} className="text-blue-500 underline hover:text-blue-700" target="_blank" rel="noopener noreferrer">
                          {props.children}
                        </a>
                      ),
                    }}
                  >
                    {msg.text}
                  </ReactMarkdown>
                ) : (
                  msg.text
                )}
              </div>

            ))}
            {typingMessage && (
              <div className="p-2 mb-2 rounded-lg bg-gray-100 self-start shadow">
                <ReactMarkdown>{typingMessage}</ReactMarkdown>
              </div>
            )}
          </div>
          <div className="flex border-t p-2">
            <input
              type="text"
              value={input}
              onChange={handleInputChange}
              onKeyPress={handleKeyPress}
              placeholder="Nhập tin nhắn của bạn..."
              className="flex-1 border-none outline-none p-2 rounded-l-lg"
              disabled={loading}
            />
            <button
              onClick={handleSendMessage}
              className="bg-gradient-to-r from-blue-400 to-blue-400 text-white px-4 py-2 rounded-r-lg hover:from-blue-500 hover:to-blue-600 transition-all duration-300 flex items-center justify-center"
              disabled={loading}
            >
              {loading ? (
                <svg
                  className="animate-spin h-7 w-7 mr-2"
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M4 12a8 8 0 018-8v16a8 8 0 01-8-8z"
                  />
                </svg>
              ) : (
                'Gửi'
              )}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Chatbot;
