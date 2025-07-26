import React from "react";
import nicknameBg from "../assets/닉네임배경.png";

interface WelcomePageProps {
  nickname: string;
  onNext?: () => void;
}

const WelcomePage: React.FC<WelcomePageProps> = ({ nickname, onNext }) => {
  return (
    <div
      className="relative w-full h-full flex flex-col items-center justify-end bg-cover bg-center"
      style={{ backgroundImage: `url(${nicknameBg})` }}
    >
      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 w-[90%] max-w-xs bg-blue-50 rounded-2xl border border-blue-200 shadow-lg px-6 py-6 flex flex-col items-center">
        <div className="text-lg font-bold text-gray-800 text-center mb-2">
          “이곳은 감정이 자유롭게 흐르는 아름다운 나라”
        </div>
        <button className="self-end mt-2 bg-white rounded-full w-10 h-10 flex items-center justify-center shadow border border-gray-300" onClick={onNext}>
          <span className="text-2xl text-brown-700">▶</span>
        </button>
      </div>
    </div>
  );
};

export default WelcomePage; 