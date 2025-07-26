import React from "react";

const LoginPage: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-b from-blue-100 to-blue-300">
      <h1 className="text-3xl font-bold mb-4">마음 탐험대 로그인</h1>
      <input className="border rounded px-4 py-2 mb-2" placeholder="닉네임을 입력하세요" />
      <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">시작하기</button>
    </div>
  );
};

export default LoginPage; 