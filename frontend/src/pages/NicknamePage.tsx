import React from "react";
import nicknameBg from "../assets/닉네임배경.png";

interface NicknamePageProps {
  onComplete: (nickname: string) => void;
}

const NicknamePage: React.FC<NicknamePageProps> = ({ onComplete }) => {
  const [nickname, setNickname] = React.useState("");
  return (
    <div
      className="flex flex-col items-center justify-center w-full h-full bg-cover bg-center"
      style={{ backgroundImage: `url(${nicknameBg})` }}
    >
      <div className="mt-8 mb-4 text-2xl text-gray-700">닉네임</div>
      <input
        className="rounded-2xl border border-gray-300 px-6 py-4 text-2xl text-center font-bold bg-white shadow focus:outline-none mb-4"
        placeholder="닉네임을 입력하세요"
        value={nickname}
        onChange={e => setNickname(e.target.value)}
        autoFocus
      />
      <button
        className="bg-blue-500 text-white px-8 py-3 rounded-xl text-lg font-bold shadow hover:bg-blue-600 transition"
        onClick={() => onComplete(nickname)}
        disabled={!nickname.trim()}
      >
        완료
      </button>
    </div>
  );
};

export default NicknamePage; 