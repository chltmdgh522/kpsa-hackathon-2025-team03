import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Settings, Home } from "lucide-react";
import backgroundImg from "../assets/LastPage/배경.png";
import cardBackgroundImg from "../assets/마지막카드.png";
import SettingsModal from "../components/SettingsModal";
import RecordPage from "./RecordPage";
import { api } from "../utils/apiClient";
import { QuizEndingResponse } from "../types/api";

interface GameResult {
  playTime: string; // "00:00:00" 형식
  correctCount: number;
  wrongCount: number;
  totalQuestions: number;
  score: number; // 0-100 점수
}

interface AfterPlayPageProps {
  onComplete?: () => void;
  onGoHome?: () => void;
  gameResult?: GameResult; // 백엔드에서 받아올 게임 결과 데이터
  sessionId?: string; // 게임 세션 ID (API에서 데이터를 가져올 때 사용)
  quizRecordId?: number; // 퀴즈 기록 ID (API에서 데이터를 가져올 때 사용)
}

const AfterPlayPage: React.FC<AfterPlayPageProps> = ({ 
  onComplete, 
  onGoHome, 
  gameResult,
  sessionId,
  quizRecordId
}) => {
  const [showSettings, setShowSettings] = useState(false);
  const [showRecordPage, setShowRecordPage] = useState(false);
  const [showConfetti, setShowConfetti] = useState(true);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [apiGameResult, setApiGameResult] = useState<GameResult>({
    playTime: "00:00:00",
    correctCount: 0,
    wrongCount: 0,
    totalQuestions: 0,
    score: 0
  });

  console.log('AfterPlayPage 렌더링, onGoHome:', onGoHome, 'quizRecordId:', quizRecordId);

  // API에서 퀴즈 엔딩 데이터 가져오기
  useEffect(() => {
    const fetchQuizEnding = async () => {
      if (!quizRecordId) {
        console.log('quizRecordId가 없어서 기본 데이터 사용');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        
        console.log('퀴즈 엔딩 API 호출:', `/api/record/ending/${quizRecordId}`);
        const response: QuizEndingResponse = await api.get(`/api/record/ending/${quizRecordId}`);
        
        console.log('퀴즈 엔딩 API 응답:', response);
        console.log('개별 퀴즈 점수:', {
          quiz1: response.quiz1,
          quiz2: response.quiz2,
          quiz3: response.quiz3,
          quiz4: response.quiz4,
          quiz5: response.quiz5
        });
        
        // PT6M20S 형식을 00:00:00 형식으로 변환
        const formatTime = (duration: string): string => {
          console.log('시간 형식 변환 입력:', duration);
          
          // ISO 8601 Duration 형식 파싱 (PT6M20S, PT1H30M45S 등)
          const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/);
          if (!match) {
            console.log('시간 형식 파싱 실패, 기본값 반환');
            return "00:00:00";
          }
          
          const hours = parseInt(match[1] || "0");
          const minutes = parseInt(match[2] || "0");
          const seconds = parseInt(match[3] || "0");
          
          const formattedTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
          console.log('변환된 시간:', formattedTime);
          
          return formattedTime;
        };
        
        // 각 퀴즈의 정답/오답 계산
        const quizResults = [response.quiz1, response.quiz2, response.quiz3, response.quiz4, response.quiz5];
        console.log('퀴즈 결과 배열:', quizResults);
        
        const totalQuestions = 5; // 총 문제 수를 5문제로 하드코딩
        // 점수가 0보다 크면 정답으로 간주 (API에서 정답 시 양수, 오답 시 0 반환)
        const correctCount = quizResults.filter(score => score > 0).length;
        const wrongCount = totalQuestions - correctCount;
        const averageScore = quizResults.reduce((sum, score) => sum + score, 0) / totalQuestions;
        
        console.log('정답/오답 계산 과정:', {
          quizResults,
          totalQuestions,
          correctScores: quizResults.filter(score => score > 0),
          wrongScores: quizResults.filter(score => score === 0),
          correctCount,
          wrongCount,
          averageScore
        });
        
        const result: GameResult = {
          playTime: formatTime(response.allTime),
          correctCount,
          wrongCount,
          totalQuestions,
          score: Math.round(averageScore * 10) // 0-100 점수로 변환
        };
        
        console.log('변환된 게임 결과:', result);
        setApiGameResult(result);
        
      } catch (err) {
        console.error('퀴즈 엔딩 API 호출 실패:', err);
        setError('게임 결과를 불러오는데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchQuizEnding();
  }, [quizRecordId]);

  // 최종 게임 결과 결정 (API 데이터 우선, props 데이터는 fallback)
  const finalGameResult = apiGameResult.totalQuestions > 0 ? apiGameResult : (gameResult || {
    playTime: "00:00:00",
    correctCount: 3,
    wrongCount: 2,
    totalQuestions: 5,
    score: 60
  });

  // 결과 보러가기 클릭 핸들러
  const handleViewResults = () => {
    setShowRecordPage(true);
  };

  // 홈으로 이동 핸들러
  const handleGoHome = () => {
    console.log('홈 버튼 클릭됨');
    console.log('onGoHome prop:', onGoHome);
    if (onGoHome && typeof onGoHome === 'function') {
      console.log('onGoHome 함수 실행');
      onGoHome();
    } else {
      console.log('onGoHome prop이 없거나 함수가 아님');
    }
  };

  // 설정 모달이 열려있거나 기록 페이지가 열려있으면 해당 컴포넌트 렌더링
  if (showSettings) {
    console.log('설정 모달 렌더링');
    return <SettingsModal isOpen={showSettings} onClose={() => setShowSettings(false)} />;
  }

  if (showRecordPage) {
    return <RecordPage onBack={() => setShowRecordPage(false)} />;
  }

  // 로딩 중일 때
  if (loading) {
    return (
      <div
        className="relative w-full h-full bg-cover bg-center overflow-hidden flex items-center justify-center"
        style={{ backgroundImage: `url(${backgroundImg})` }}
      >
        <div className="text-white text-2xl font-bold">결과를 불러오는 중...</div>
      </div>
    );
  }

  // 에러가 있을 때
  if (error) {
    return (
      <div
        className="relative w-full h-full bg-cover bg-center overflow-hidden flex items-center justify-center"
        style={{ backgroundImage: `url(${backgroundImg})` }}
      >
        <div className="text-white text-xl text-center">
          <div className="mb-4">{error}</div>
          <button 
            onClick={() => window.location.reload()} 
            className="bg-white/30 hover:bg-white/50 text-white px-4 py-2 rounded-lg"
          >
            다시 시도
          </button>
        </div>
      </div>
    );
  }

  return (
    <>
      <style>
        {`
          @keyframes confetti-fall {
            0% {
              transform: translateY(-100vh) rotate(0deg);
              opacity: 1;
            }
            100% {
              transform: translateY(100vh) rotate(360deg);
              opacity: 0;
            }
          }
          
          @keyframes star-glow {
            0%, 100% { transform: scale(1); filter: brightness(1); }
            50% { transform: scale(1.1); filter: brightness(1.3); }
          }
          
          @keyframes float {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
          }
        `}
      </style>
      
      <div
        className="relative w-full h-full bg-cover bg-center overflow-hidden"
        style={{ backgroundImage: `url(${backgroundImg})` }}
      >
        {/* 폭죽 효과 */}
        {showConfetti && (
          <div className="absolute inset-0 pointer-events-none">
            {[...Array(20)].map((_, i) => (
              <motion.div
                key={i}
                className="absolute w-2 h-2 rounded-sm"
                style={{
                  backgroundColor: ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', '#ff9ff3'][i % 6],
                  left: `${Math.random() * 100}%`,
                  top: `${Math.random() * 100}%`,
                }}
                animate={{
                  y: [0, window.innerHeight],
                  x: [0, (Math.random() - 0.5) * 200],
                  rotate: [0, 360],
                  opacity: [1, 0],
                }}
                transition={{
                  duration: 3 + Math.random() * 2,
                  delay: Math.random() * 2,
                  repeat: Infinity,
                  ease: "linear"
                }}
              />
            ))}
          </div>
        )}

        {/* 설정 버튼 - 우상단 */}
        <motion.button
          onClick={() => {
            console.log('설정 버튼 클릭됨');
            setShowSettings(true);
          }}
          className="absolute top-6 right-6 p-3 rounded-full bg-white/30 hover:bg-white/50 transition-colors z-50 cursor-pointer"
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          style={{ pointerEvents: 'auto' }}
        >
          <Settings className="w-6 h-6 text-white" />
        </motion.button>

        {/* 홈 버튼 - 좌상단 */}
        <motion.button
          onClick={handleGoHome}
          className="absolute top-6 left-6 p-3 rounded-full bg-white/30 hover:bg-white/50 transition-colors z-50 cursor-pointer"
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          style={{ pointerEvents: 'auto' }}
        >
          <Home className="w-6 h-6 text-white" />
        </motion.button>

        {/* 메인 결과 카드 - 마지막카드.png 전체 표시 */}
        <div className="absolute inset-0 flex items-center justify-center px-4 z-20">
          <motion.div 
            className="relative max-w-sm w-full flex flex-col items-center justify-center"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
          >
            {/* PNG 이미지 전체 표시 */}
            <img 
              src={cardBackgroundImg} 
              alt="결과 카드" 
              className="w-full h-auto rounded-3xl shadow-2xl"
            />
            
            {/* 오버레이 컨테이너 */}
            <div className="absolute inset-0 flex flex-col items-center justify-center px-8 py-8">
              {/* 플레이 시간 */}
              <motion.div 
                className="flex flex-col items-center justify-center mb-10 mt-24"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.3 }}
              >
                <div className="text-center mb-2">
                  <span className="text-[26px] font-bold text-black">플레이 시간</span>
                </div>
                <div className="bg-[#FFF2B8] rounded-xl p-3 border-4 border-white shadow-sm flex items-center justify-center min-w-[180px]">
                  <span className="text-[26px] font-bold text-black">
                    {finalGameResult.playTime}
                  </span>
                </div>
              </motion.div>
              
              {/* 정답/오답 비율 */}
              <motion.div 
                className="flex flex-col items-center justify-center mb-6"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.5 }}
              >
                {/* 파이차트 */}
                <div className="relative w-40 h-50 mx-auto mb-0">
                  <svg className="w-full h-full transform -rotate-90" viewBox="0 0 100 100">
                    {/* 배경 원 */}
                    <circle
                      cx="50"
                      cy="50"
                      r="40"
                      fill="none"
                      stroke="#E5E7EB"
                      strokeWidth="8"
                    />
                    {/* 정답 부분 */}
                    <motion.circle
                      cx="50"
                      cy="50"
                      r="40"
                      fill="none"
                      stroke="#4ADE80"
                      strokeWidth="8"
                      strokeLinecap="round"
                      initial={{ strokeDasharray: "0 251.2" }}
                      animate={{ strokeDasharray: `${(finalGameResult.correctCount / finalGameResult.totalQuestions) * 251.2} 251.2` }}
                      transition={{ duration: 1.5, delay: 0.8, ease: "easeOut" }}
                    />
                    {/* 오답 부분 */}
                    <motion.circle
                      cx="50"
                      cy="50"
                      r="40"
                      fill="none"
                      stroke="#F87171"
                      strokeWidth="8"
                      strokeLinecap="round"
                      initial={{ strokeDasharray: "0 251.2" }}
                      animate={{ strokeDasharray: `${(finalGameResult.wrongCount / finalGameResult.totalQuestions) * 251.2} 251.2` }}
                      transition={{ duration: 1.5, delay: 1.2, ease: "easeOut" }}
                    />
                  </svg>
                  {/* 중앙 텍스트 */}
                  <motion.div 
                    className="absolute inset-0 flex items-center justify-center"
                    initial={{ opacity: 0, scale: 0.5 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ duration: 0.5, delay: 1.5 }}
                  >
                    <div className="text-center">
                      <div className="text-2xl font-semibold text-black">{finalGameResult.totalQuestions}</div>
                      <div className="text-sm text-black font-medium">총 문제</div>
                    </div>
                  </motion.div>
                </div>
                
                {/* 간단한 비율 표시 */}
                <div className="flex justify-center items-center mb-4 gap-8">
                  <div className="flex flex-col items-center justify-center">
                    <div className="text-[26px] font-bold text-green-600">{finalGameResult.correctCount}</div>
                    <div className="text-[20px] font-bold text-black">정답</div>
                  </div>
                  <div className="text-[26px] font-bold text-black">/</div>
                  <div className="flex flex-col items-center justify-center">
                    <div className="text-[26px] font-bold text-red-600">{finalGameResult.wrongCount}</div>
                    <div className="text-[20px] font-bold text-black">오답</div>
                  </div>
                </div>
              </motion.div>
              
              {/* 결과 보러가기 버튼 */}
              <motion.div 
                className="flex items-center justify-center w-full mt-auto"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.7 }}
              >
                <motion.button
                  onClick={handleViewResults}
                  className="w-full bg-[#FFF2B8] text-black font-bold py-4 px-6 rounded-xl border-4 border-white shadow-sm flex items-center justify-center"
                  whileHover={{ 
                    scale: 1.05,
                    y: -2
                  }}
                  whileTap={{ 
                    scale: 0.95,
                    y: 0
                  }}
                  animate={{
                    y: [0, -5, 0]
                  }}
                  transition={{
                    y: {
                      duration: 2,
                      repeat: Infinity,
                      ease: "easeInOut"
                    }
                  }}
                >
                  <span className="text-[26px] font-bold">결과 보러 가기</span>
                </motion.button>
              </motion.div>
            </div>
          </motion.div>
        </div>

        {/* 하단 장식 요소들 */}
        <motion.div
          className="absolute bottom-8 left-1/2 transform -translate-x-1/2 flex space-x-6"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 1 }}
        >
          {[...Array(5)].map((_, i) => (
            <motion.div
              key={i}
              className="relative"
              animate={{
                y: [0, -15, 0],
                rotate: [0, 360],
              }}
              transition={{
                duration: 3,
                repeat: Infinity,
                delay: i * 0.4,
                ease: "easeInOut"
              }}
            >
              <div 
                className={`w-4 h-4 rounded-full ${
                  i % 3 === 0 ? 'bg-yellow-400' : 
                  i % 3 === 1 ? 'bg-pink-400' : 'bg-blue-400'
                }`}
                style={{
                  boxShadow: `0 0 10px ${
                    i % 3 === 0 ? '#fbbf24' : 
                    i % 3 === 1 ? '#f472b6' : '#60a5fa'
                  }`
                }}
              />
            </motion.div>
          ))}
        </motion.div>
      </div>
    </>
  );
};

export default AfterPlayPage; 