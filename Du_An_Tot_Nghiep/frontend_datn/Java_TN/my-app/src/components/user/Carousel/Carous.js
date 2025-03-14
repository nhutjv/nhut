import React, { Component } from "react";
import axios from "axios";
import { API_BASE_URL } from "../../../configAPI";

export default class Carous extends Component {
  state = {
    slides: [], // Danh sách các slide
    currentIndex: 0, // Slide hiện tại
  };

  componentDidMount() {
    this.fetchSlides();
    this.autoSlide();
  }

  fetchSlides = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/auth/all-slide`, { headers: {
        "Access-Control-Allow-Origin": "*"
    }});
      this.setState({ slides: response.data });
    } catch (error) {
      console.error("Lỗi khi tải danh sách slide:", error);
    }
  };

  autoSlide = () => {
    this.interval = setInterval(() => {
      this.setState((prevState) => ({
        currentIndex: (prevState.currentIndex + 1) % prevState.slides.length,
      }));
    }, 3000); // Tự động chuyển slide sau 3 giây
  };

  componentWillUnmount() {
    clearInterval(this.interval); // Dừng auto-slide khi component bị unmount
  }

  render() {
    const { slides, currentIndex } = this.state;

    return (
      <div
        id="carouselExampleCaptions"
        className="relative mx-auto max-w-7xl px-4 sm:px-2"
      >
        {/* Carousel Items */}
        <div className="relative w-full overflow-hidden">
          {slides.length > 0 ? (
            slides.map((slide, index) => (
              <div
                key={index}
                className={`${
                  index === currentIndex ? "block" : "hidden"
                } w-full transition-transform duration-500 ease-in-out`}
              >
                <img
                  src={slide.image_SlideShow}
                  alt={slide.title || `Slide ${index + 1}`}
                  className="block w-full object-cover"
                />
              </div>
            ))
          ) : (
            <p>Loading...</p>
          )}
        </div>

        {/* Indicators */}
        <div className="absolute bottom-0 left-0 right-0 z-[2] mx-auto mb-4 flex list-none justify-center p-0">
          {slides.map((_, index) => (
            <button
              key={index}
              type="button"
              className={`mx-2 h-3 w-3 rounded-full ${
                currentIndex === index ? "" : "bg-gray-300"
              }`}
              onClick={() => this.setState({ currentIndex: index })}
            />
          ))}
        </div>

        {/* Controls */}
        <button
          className="absolute bottom-0 left-0 top-0 z-[1] flex w-[15%] items-center justify-center text-white"
          onClick={() =>
            this.setState((prevState) => ({
              currentIndex:
                (prevState.currentIndex - 1 + slides.length) % slides.length,
            }))
          }
        >
          
        </button>
        <button
          className="absolute bottom-0 right-0 top-0 z-[1] flex w-[15%] items-center justify-center text-white"
          onClick={() =>
            this.setState((prevState) => ({
              currentIndex: (prevState.currentIndex + 1) % slides.length,
            }))
          }
        >
          
        </button>
      </div>
    );
  }
}

