import React, { useState, useEffect } from "react";
import DialogBox from "../components/DialogBox";
import Q3SuccessPage from "./Q3SuccessPage";
import { fetchQuizData, checkQuizAnswer, retryQuiz, QuizData as ApiQuizData } from "../utils/gameApi";

// 이미지 import
import bg5 from "../assets/5_배경.png";

// 문제 이미지
import questionImage from "../assets/Quiz_3/Quiz3-1.png";

// 물체 이미지들
import object1 from "../assets/Quiz_3/Quiz3-1-1.png"; // 가위
import object2 from "../assets/Quiz_3/Quiz3-1-2.png"; // 옷핀
import object3 from "../assets/Quiz_3/Quiz3-1-3.png"; // 바늘과 실 (정답)
import object4 from "../assets/Quiz_3/Quiz3-1-4.png"; // 페인트
import object5 from "../assets/Quiz_3/Quiz3-1-5.png"; // 털실

// 결과 이미지들
import wrongImage from "../assets/Quiz_3/Quiz3-2.png";
import successImage from "../assets/Quiz_3/Quiz3-3.png";

interface Quest3Props {
  onAnswer?: (answer: string) => void;
  onComplete?: (score: number) => void;
  onNext?: () => void;
  // 백엔드 연동을 위한 props
  isLoading?: boolean;
  quizStatusInfo?: {
    quizId: number | null;
    quizRecordId: number | null;
    imageId: number | null;
    imageUrl: string | null;
    audioUrl: string | null;
    quizNumber: number | null;
  } | null;
}

const Quest3: React.FC<Quest3Props> = ({ 
  onAnswer, 
  onComplete, 
  onNext,
  isLoading = false,
  quizStatusInfo
}) => {
  const [selectedObject, setSelectedObject] = useState<string | null>(null);
  const [showResult, setShowResult] = useState(false);
  const [isCorrect, setIsCorrect] = useState(false);
  const [showSuccessPage, setShowSuccessPage] = useState(false);
  const [currentQuiz, setCurrentQuiz] = useState<ApiQuizData | null>(null);
  const [answeredQuizImageUrl, setAnsweredQuizImageUrl] = useState<string | null>(null);
  const [answeredCorrectAnswer, setAnsweredCorrectAnswer] = useState<string | null>(null);
  const [loadedQuizRecordId, setLoadedQuizRecordId] = useState<number | null>(null); // 중복 호출 방지용

  // imageId에 따라 정답을 판단하는 함수 (Quest 3)
  const getCorrectAnswerFromImageId = (imageId: number): string => {
    console.log('Quest3: 정답 판단 - imageId:', imageId);
    
    // Quest 3는 "바늘과 실"이 정답
    // imageId에 따라 정답이 결정됨 (백엔드에서 관리)
    switch (imageId) {
      case 1:
        console.log('Quest3: 정답 판단 결과 - 바늘과 실 (imageId: 1)');
        return "바늘과 실";
      case 2:
        console.log('Quest3: 정답 판단 결과 - 바늘과 실 (imageId: 2)');
        return "바늘과 실";
      case 3:
        console.log('Quest3: 정답 판단 결과 - 바늘과 실 (imageId: 3)');
        return "바늘과 실";
      case 4:
        console.log('Quest3: 정답 판단 결과 - 바늘과 실 (imageId: 4)');
        return "바늘과 실";
      case 5:
        console.log('Quest3: 정답 판단 결과 - 바늘과 실 (imageId: 5)');
        return "바늘과 실";
    }
    
    console.log('Quest3: 알 수 없는 imageId:', imageId);
    return "바늘과 실"; // 기본값
  };

  // 컴포넌트 마운트 시 퀴즈 데이터 로드
  useEffect(() => {
    // quizStatusInfo가 없거나 quizRecordId가 없으면 기본 데이터로 시작
    if (!quizStatusInfo || !quizStatusInfo.quizRecordId) {
      console.log('Quest3: quizStatusInfo 또는 quizRecordId가 없음, 기본 데이터로 시작');
      setCurrentQuiz({
        quizId: 3,
        quizRecordId: 0,
        imageId: 1,
        imageUrl: questionImage,
        audioUrl: ""
      });
      return;
    }

    // 이미 해당 quizRecordId로 데이터가 로드된 경우 중복 호출 방지
    if (loadedQuizRecordId === quizStatusInfo.quizRecordId) {
      console.log('Quest3: 이미 이 quizRecordId로 데이터가 로드됨, 중복 호출 방지');
      return;
    }

    const loadQuizData = async () => {
      console.log('Quest3: 퀴즈 데이터 로딩 시작...');
      
      try {
        // 퀴즈 3번 데이터 로드
        const quizRecordId = quizStatusInfo.quizRecordId;
        const quizData = await fetchQuizData(3, quizRecordId);
        
        if (quizData) {
          console.log('Quest3: 퀴즈 데이터 로드 성공', quizData);
          // API 데이터를 그대로 사용 (Quest 1, 2와 동일)
          setCurrentQuiz(quizData);
          setLoadedQuizRecordId(quizStatusInfo.quizRecordId); // 이 quizRecordId로는 다시 호출 안 함
          
          // 오디오 재생 (audioUrl이 있으면)
          if (quizData.audioUrl) {
            console.log('Quest3: 오디오 재생 시작:', quizData.audioUrl);
            const audio = new Audio(quizData.audioUrl);
            audio.play().catch(error => {
              console.error('Quest3: 오디오 재생 실패:', error);
            });
          }
        } else {
          console.log('Quest3: API 데이터 없음, 기본 데이터 사용');
          setCurrentQuiz({
            quizId: 3,
            quizRecordId: 0,
            imageId: 1,
            imageUrl: questionImage,
            audioUrl: ""
          });
          setLoadedQuizRecordId(quizStatusInfo.quizRecordId); // 이 quizRecordId로는 다시 호출 안 함
        }
      } catch (error) {
        console.error('Quest3: 퀴즈 데이터 로드 중 오류:', error);
        setCurrentQuiz({
          quizId: 3,
          quizRecordId: 0,
          imageId: 1,
          imageUrl: questionImage,
          audioUrl: ""
        });
        setLoadedQuizRecordId(quizStatusInfo.quizRecordId); // 이 quizRecordId로는 다시 호출 안 함
      }
    };

    loadQuizData();
  }, [quizStatusInfo]);

  // 물체 선택 처리
  const handleObjectSelect = async (objectId: string) => {
    if (!currentQuiz) return;
    
    console.log('Quest3: 답변 선택:', objectId);
    console.log('Quest3: 현재 퀴즈 이미지 URL:', currentQuiz.imageUrl);
    console.log('Quest3: 현재 퀴즈 imageId:', currentQuiz.imageId);
    
    setSelectedObject(objectId);
    
    // 답변한 퀴즈 이미지 URL과 정답 저장
    setAnsweredQuizImageUrl(currentQuiz.imageUrl);
    const correctAnswer = getCorrectAnswerFromImageId(currentQuiz.imageId);
    setAnsweredCorrectAnswer(correctAnswer);
    console.log('Quest3: 답변 시 정답 저장 - imageId:', currentQuiz.imageId, '정답:', correctAnswer);
    
    try {
      // API로 정답 확인
      const result = await checkQuizAnswer({
        quizId: 3,
        imageId: currentQuiz.imageId,
        answer1: objectId, // 선택된 물체 이름
        answer2: "", // 퀴즈 3번은 answer2 없음
        time: "PT1M30S" // 기본값, 실제로는 측정된 시간 사용
      });
      
      console.log('Quest3: API 정답 확인 요청 데이터:', {
        quizId: 3,
        imageId: currentQuiz.imageId,
        answer1: objectId,
        answer2: "",
        time: "PT1M30S"
      });
      
      if (result) {
        console.log('Quest3: 정답 확인 결과:', result);
        setIsCorrect(result.answer);
        setShowResult(true);
        
        // 정답 확인 응답의 오디오 재생 (힌트나 해설)
        if (result.audioUrl) {
          console.log('Quest3: 정답 확인 오디오 재생:', result.audioUrl);
          const audio = new Audio(result.audioUrl);
          audio.play().catch(error => {
            console.error('Quest3: 정답 확인 오디오 재생 실패:', error);
          });
        }
        
        // 백엔드로 답변 전송
        onAnswer?.(objectId);
      } else {
        console.error('Quest3: 정답 확인 실패');
        // 로컬 로직으로 폴백
        const correctAnswer = getCorrectAnswerFromImageId(currentQuiz.imageId);
        const correct = objectId === correctAnswer;
        console.log('Quest3: 로컬 정답 확인:', {
          선택한답: objectId,
          정답: correctAnswer,
          결과: correct
        });
        setIsCorrect(correct);
        setShowResult(true);
        onAnswer?.(objectId);
      }
    } catch (error) {
      console.error('Quest3: 정답 확인 중 오류:', error);
      // 로컬 로직으로 폴백
      const correctAnswer = getCorrectAnswerFromImageId(currentQuiz.imageId);
      const correct = objectId === correctAnswer;
      console.log('Quest3: 오류 시 로컬 정답 확인:', {
        선택한답: objectId,
        정답: correctAnswer,
        결과: correct
      });
      setIsCorrect(correct);
      setShowResult(true);
      onAnswer?.(objectId);
    }
  };

  // 다시 시도
  const handleRetry = async () => {
    if (!currentQuiz) return;
    
    console.log('Quest3: 다시하기 시작...');
    console.log('Quest3: 현재 quizId:', currentQuiz.quizId);
    console.log('Quest3: 현재 imageId:', currentQuiz.imageId);
    
    try {
      const retryData = await retryQuiz(currentQuiz.quizId, currentQuiz.imageId);
      
      if (retryData) {
        console.log('Quest3: 다시하기 데이터 로드 성공:', retryData);
        setCurrentQuiz(retryData);
        setSelectedObject(null);
        setShowResult(false);
        setIsCorrect(false);
        setAnsweredQuizImageUrl(null);
        setAnsweredCorrectAnswer(null);
      } else {
        console.error('Quest3: 다시하기 데이터 로드 실패');
      }
    } catch (error) {
      console.error('Quest3: 다시하기 중 오류:', error);
    }
  };

  // 다음 퀴즈로 이동
  const handleNextQuest = () => {
    console.log('Quest3: 다음 퀴즈로 이동');
    onNext?.();
  };

  // 성공 페이지 완료
  const handleSuccessComplete = () => {
    console.log('Quest3: 성공 페이지 완료');
    onComplete?.(100);
  };

  // 성공 페이지
  if (showSuccessPage) {
    return <Q3SuccessPage onComplete={handleSuccessComplete} />;
  }

  // 로딩 상태
  if (isLoading) {
    return (
      <div
        className="relative w-full h-full flex flex-col items-center justify-center bg-cover bg-center"
        style={{ backgroundImage: `url(${bg5})` }}
      >
        <div className="text-2xl font-['Noto_Sans_KR']">로딩 중...</div>
      </div>
    );
  }

  // 결과 화면 (정답)
  if (showResult && isCorrect) {
    return (
      <div
        className="relative w-full h-full flex flex-col items-center justify-between bg-cover bg-center py-8"
        style={{ backgroundImage: `url(${bg5})` }}
      >
        {/* 상단 정답 메시지 */}
        <div className="flex-shrink-0">
          <DialogBox text="정답입니다" fontSize="text-[26px]" />
        </div>

        {/* 중앙 정답 이미지 */}
        <div className="flex-1 flex items-center justify-center min-h-0">
          <img
            src={successImage}
            alt="정답 이미지"
            style={{ width: '290px', height: 'auto' }}
            draggable={false}
          />
        </div>

        {/* 하단 다음퀘스트 DialogBox */}
        <div className="flex-shrink-0">
          <div onClick={handleNextQuest} style={{ cursor: 'pointer' }}>
            <DialogBox text="다음퀘스트" fontSize="text-[26px]" />
          </div>
        </div>
      </div>
    );
  }

  // 결과 화면 (오답)
  if (showResult && !isCorrect) {
    return (
      <div
        className="relative w-full h-full flex flex-col items-center justify-between bg-cover bg-center py-8"
        style={{ backgroundImage: `url(${bg5})` }}
      >
        {/* 상단 다시해볼까 메시지 */}
        <div className="flex-shrink-0">
          <DialogBox text="다시해볼까?" fontSize="text-[26px]" />
        </div>

        {/* 중앙 틀린 문제의 캐릭터 이미지 */}
        <div className="flex-1 flex items-center justify-center min-h-0">
          <img
            src={answeredQuizImageUrl || currentQuiz?.imageUrl || questionImage}
            alt="틀린 문제 캐릭터"
            style={{ width: '290px', height: 'auto' }}
            draggable={false}
          />
        </div>

        {/* 하단 다시하기 DialogBox */}
        <div className="flex-shrink-0">
          <div onClick={handleRetry} style={{ cursor: 'pointer' }}>
            <DialogBox text="다시하기" fontSize="text-[26px]" />
          </div>
        </div>
      </div>
    );
  }

  // 메인 퀴즈 화면
  return (
    <div
      className="relative w-full h-full flex flex-col items-center justify-between bg-cover bg-center py-8"
      style={{ backgroundImage: `url(${bg5})` }}
    >
      {/* 상단 문제 문장 */}
      <div className="flex-shrink-0">
        <DialogBox text="어떤 물체가 정답일까?" fontSize="text-[26px]" />
      </div>

      {/* 중앙 문제 이미지 */}
      <div className="flex-1 flex items-center justify-center min-h-0">
        <img
          src={currentQuiz?.imageUrl || questionImage}
          alt="문제 이미지"
          style={{ width: '290px', height: 'auto' }}
          draggable={false}
        />
      </div>

      {/* 하단 물체들 (스크롤 가능) */}
      <div className="flex-shrink-0 w-full relative" style={{ height: '200px' }}>
        <div 
          className="absolute overflow-x-auto"
          style={{ 
            left: '42px', 
            top: '0px',
            width: 'calc(100% - 84px)',
            height: '100%'
          }}
        >
          <div className="flex space-x-4" style={{ minWidth: 'max-content', padding: '10px 0' }}>
            {[
              { id: "가위", image: object1, name: "가위" },
              { id: "옷핀", image: object2, name: "옷핀" },
              { id: "바늘과 실", image: object3, name: "바늘과 실" },
              { id: "페인트", image: object4, name: "페인트" },
              { id: "털실", image: object5, name: "털실" }
            ].map((object) => (
              <div
                key={object.id}
                onClick={() => handleObjectSelect(object.id)}
                style={{ 
                  cursor: 'pointer',
                  transition: 'transform 0.2s ease',
                  transform: selectedObject === object.id ? 'scale(1.05)' : 'scale(1)',
                  width: '130px',
                  height: '130px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  flexShrink: 0
                }}
                onMouseEnter={(e) => {
                  if (selectedObject !== object.id) {
                    e.currentTarget.style.transform = 'scale(1.05)';
                  }
                }}
                onMouseLeave={(e) => {
                  if (selectedObject !== object.id) {
                    e.currentTarget.style.transform = 'scale(1)';
                  }
                }}
              >
                <img
                  src={object.image}
                  alt={object.name}
                  style={{ 
                    width: '100%', 
                    height: '100%', 
                    objectFit: 'contain',
                    filter: selectedObject === object.id ? 'brightness(1.2)' : 'brightness(1)'
                  }}
                  draggable={false}
                />
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Quest3; 