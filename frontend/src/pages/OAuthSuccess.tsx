import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OAuthSuccess: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get("accessToken");
    
    console.log('OAuthSuccess: accessToken 확인:', accessToken ? '있음' : '없음');

    if (accessToken) {
      // 토큰 저장
      localStorage.setItem("accessToken", accessToken);
      console.log("✅ 로그인 성공. 토큰 저장 완료");
      
      // 로그인 성공 후 홈으로 리다이렉트
      navigate("/");
    } else {
      console.error("❌ accessToken 없음");
      // 실패 시 로그인 페이지로 이동
      navigate("/");
    }
  }, [navigate]);

  return (
    <div
      style={{
        width: "100%",
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        color: "white",
        fontSize: "18px",
        fontFamily: "Noto Sans KR, sans-serif"
      }}
    >
      <div style={{ textAlign: "center" }}>
        <div
          style={{
            width: "60px",
            height: "60px",
            border: "4px solid rgba(255,255,255,0.3)",
            borderTop: "4px solid white",
            borderRadius: "50%",
            animation: "spin 1s linear infinite",
            margin: "0 auto 20px"
          }}
        />
        <p>로그인 처리 중입니다...</p>
      </div>
      
      <style>
        {`
          @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
          }
        `}
      </style>
    </div>
  );
};

export default OAuthSuccess; 