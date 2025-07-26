import React from "react";
import { motion } from "framer-motion";
import questPaper from "../assets/퀘스트수락용지.png";
import acceptBtn from "../assets/수락버튼.png";

interface QuestAcceptPageProps {
  onAccept?: () => void;
}

const QuestAcceptPage: React.FC<QuestAcceptPageProps> = ({ onAccept }) => {
  return (
    <div className="relative w-full h-full flex flex-col items-center justify-center" style={{ background: '#2B2027' }}>
      <motion.img
        src={questPaper}
        alt="퀘스트 수락 용지"
        className="w-[420px] h-auto max-w-[130%] mx-auto mt-12"
        initial={{ scale: 0.7, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ duration: 0.7, ease: "easeOut" }}
      />
      <button
        className="absolute left-1/2 bottom-12 -translate-x-1/2 outline-none focus:outline-none border-none bg-transparent p-0 m-0 shadow-none"
        onClick={onAccept}
      >
        <img src={acceptBtn} alt="수락 버튼" className="w-[280px] h-auto" />
      </button>
    </div>
  );
};

export default QuestAcceptPage; 