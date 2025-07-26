import React from "react";
import { motion } from "framer-motion";
import darkBg from "../assets/암흑배경.png";
import crown from "../assets/박살난왕관.png";

interface ShatteredCrownPageProps {
  onNext?: () => void;
}

const ShatteredCrownPage: React.FC<ShatteredCrownPageProps> = ({ onNext }) => {
  return (
    <div
      className="relative w-full h-full flex flex-col items-center justify-end bg-cover bg-center"
      style={{ backgroundImage: `url(${darkBg})` }}
    >
      <div className="absolute left-1/2 top-1/3 -translate-x-1/2 -translate-y-1/2 flex flex-col items-center">
        <motion.img
          src={crown}
          alt="박살난 왕관"
          className="w-[520px] h-auto max-w-[100%] select-none pointer-events-none"
          initial={{ rotate: 0 }}
          animate={{ rotate: [0, -10, 10, -8, 8, -5, 5, 0] }}
          transition={{ duration: 0.8, repeat: 1 }}
        />
      </div>
      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 w-[90%] max-w-xs bg-blue-50 rounded-2xl border border-blue-200 shadow-lg px-6 py-6 flex flex-col items-center">
        <div className="text-lg font-bold text-gray-800 text-center mb-2">
          “어느날, 감정을 지키던 고대의 수정이 부서지며 혼란에 빠지게 된다”
        </div>
        <button className="self-end mt-2 bg-white rounded-full w-10 h-10 flex items-center justify-center shadow border border-gray-300" onClick={onNext}>
          <span className="text-2xl text-brown-700">▶</span>
        </button>
      </div>
    </div>
  );
};

export default ShatteredCrownPage; 