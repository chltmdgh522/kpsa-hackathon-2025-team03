import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';

interface AnalysisData {
  summary: string;
  strengths: string;
  cautions: string;
  training: string;
  guide: string;
}

interface AnalysisListPageProps {
  onBack: () => void;
}

const TITLES = [
  '발달 특성 요약',
  '강점',
  '주의가 필요한 영역',
  '훈련제안',
  '행동 가이드',
];

const AnalysisListPage: React.FC<AnalysisListPageProps> = ({ onBack }) => {
  const [data, setData] = useState<AnalysisData | null>(null);
  const [openIdx, setOpenIdx] = useState(0); // 첫번째만 기본 오픈
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 실제 백엔드 연동 예시
    // fetch('/api/analysis')
    //   .then(res => res.json())
    //   .then(setData)
    //   .finally(() => setLoading(false));

    // 임시 데이터
    setTimeout(() => {
      setData({
        summary: '최승호 환자의 퀴즈 결과는 감정 인식에 대한 상대적 강점을 보이는 반면, 타인에 대한 관심, 맥락 이해, 공감 능력에서 어려움을 겪고 있는 것으로 나타났습니다. 특히 타인에 대한 관심 부분에서 음수 점수와 높은 오답 횟수가 관찰되어, 타인과의 상호작용이나 사회적 관계 형성에 있어 상당한 도전을 경험할 가능성이 있어 보입니다.',
        strengths: '감정 인식 영역에서 상대적으로 높은 점수와 낮은 오답 횟수를 기록한 것은 강점으로 볼 수 있습니다. 이는 최승호 환자가 자신의 감정을 인식하고 이해하는 데 있어 일정 수준의 능력을 보이고 있음을 의미합니다. 이러한 강점은 자기조절 능력과 긍정적인 자기인식을 발달시키는 데 중요한 기초가 될 수 있습니다.',
        cautions: '타인 관심 점수가 음수이고 오답 횟수가 매우 높은 것은 타인에 대한 이해와 관심이 상당히 부족함을 나타냅니다. 이로 인해 사회적 상호작용이나 관계 형성에 어려움을 겪을 수 있으며, 이는 사회성 발달에 있어 중요한 주의가 필요한 영역입니다. 또한, 맥락 이해와 공감 능력도 낮은 점수를 기록하여, 상황에 맞는 적절한 반응 형성이나 타인의 감정을 이해하는 데 어려움이 있음을 시사합니다.',
        training: '- 타인 관심: 사회적 이야기나 역할 놀이를 통해 다양한 사회적 상황을 경험하게 하여 타인과의 관계에 대한 이해와 관심을 증진시킬 수 있습니다.\n- 맥락 이해: 맥락에 맞는 사진이나 동영상을 보면서 그 상황에서 기대되는 행동이나 반응을 논의하는 활동을 포함시킬 수 있습니다.\n- 공감 능력: 감정 카드 게임이나 감정 일기 작성을 통해 다른 사람의 감정을 인식하고 그에 대해 공감하는 연습을 할 수 있습니다.',
        guide: '- 일상 속에서 타인의 관점을 이해하려는 기회를 자주 마련해 주세요.\n- 각종 사회적 상황에서 예상되는 반응이나 행동을 미리 설명해 주세요.\n- 아동이 타인의 감정이나 상황에 대해 관심을 보일 때, 이를 긍정적으로 강화해 주세요.\n- 아동과의 대화에서 감정과 관련된 어휘를 자주 사용하고, 다양한 감정을 표현하는 방법을 모델링 해주세요.\n- 일상적인 상호작용에서 사회적 기술과 관련된 작은 목표를 설정하고, 이를 달성했을 때 아동을 격려하는 것도 중요합니다.'
      });
      setLoading(false);
    }, 400);
  }, []);

  const contents = data ? [
    data.summary,
    data.strengths,
    data.cautions,
    data.training,
    data.guide,
  ] : [];

  return (
    <AnimatePresence>
      <motion.div
        initial={{ x: 60, opacity: 0 }}
        animate={{ x: 0, opacity: 1 }}
        exit={{ x: 60, opacity: 0 }}
        transition={{ duration: 0.4, ease: "easeOut" }}
        style={{ 
          width: '100%', 
          minHeight: '130vh', 
          background: 'linear-gradient(135deg, #e9f8f8 0%, #f0f9ff 50%, #e6f3ff 100%)', 
          borderRadius: 10, 
          position: 'relative', 
          display: 'flex', 
          flexDirection: 'column', 
          alignItems: 'center', 
          paddingBottom: 40,
          overflowY: 'auto',
          maxHeight: '100vh',
          scrollbarWidth: 'thin',
          scrollbarColor: '#5B93C6 #e9f8f8'
        }}
      >
        <style>{`
          ::-webkit-scrollbar {
            width: 6px;
          }
          ::-webkit-scrollbar-track {
            background: #e9f8f8;
            border-radius: 3px;
          }
          ::-webkit-scrollbar-thumb {
            background: #5B93C6;
            border-radius: 3px;
          }
          ::-webkit-scrollbar-thumb:hover {
            background: #4a7bb8;
          }
        `}</style>
        {/* 뒤로가기 버튼 */}
        <motion.div 
          style={{
            display: 'flex',
            alignItems: 'center',
            padding: '30px 0 0 0',
            position: 'relative',
            width: '100%',
            height: '40px',
            maxWidth: 400
          }}
          whileHover={{ scale: 1.05 }}
          transition={{ duration: 0.2 }}
        >
          <button 
            onClick={onBack} 
            style={{ 
              background: 'none', 
              border: 'none', 
              padding: 0, 
              cursor: 'pointer',
              position: 'absolute',
              left: '10px',
              top: 10
            }}
          >
            <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="20" cy="20" r="20" fill="#E6F3FF"/>
              <path d="M23 14L17 20L23 26" stroke="#1e3a8a" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </button>
        </motion.div>
        {/* 제목 - 그라데이션 배경 */}
        <motion.div 
          style={{
            width: '100%',
            background: 'linear-gradient(135deg, #5B93C6 0%, #4a7bb8 50%, #3d6ba8 100%)',
            color: '#fff',
            fontWeight: 900,
            fontSize: 26,
            textAlign: 'center',
            padding: '18px 0 14px 0',
            margin: '24px 0 32px 0',
            letterSpacing: '-1px',
            borderRadius: 0,
            boxShadow: '0 4px 20px rgba(91, 147, 198, 0.3)',
            textShadow: '0 2px 4px rgba(0,0,0,0.1)'
          }}
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.6, delay: 0.1 }}
        >
          <span style={{ color: '#fff', fontWeight: 900 }}>AI기반 맞춤형 분석 리스트</span>
        </motion.div>
        {/* 아코디언 리스트 */}
        <div style={{ 
          width: '100%', 
          maxWidth: '100%', 
          margin: '0 auto', 
          padding: '0 12px',
          maxHeight: '100%',
          overflowY: 'auto',
          scrollbarWidth: 'none', /* Firefox */
          msOverflowStyle: 'none', /* IE and Edge */
          minHeight: 0
        }}>
          <style>
            {`
              div::-webkit-scrollbar {
                display: none;
              }
            `}
          </style>
          {TITLES.map((title, idx) => (
            <motion.div 
              key={title} 
              style={{ marginBottom: 18 }}
              initial={{ x: -20, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              transition={{ duration: 0.5, delay: idx * 0.1 }}
            >
              <motion.div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  fontWeight: 900,
                  fontSize: 22,
                  marginBottom: 0,
                  color: '#222',
                  cursor: 'pointer',
                  userSelect: 'none',
                  padding: '12px 16px',
                  borderRadius: 8,
                  background: 'rgba(255, 255, 255, 0.8)',
                  backdropFilter: 'blur(10px)',
                  boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                  transition: 'all 0.3s ease'
                }}
                onClick={() => setOpenIdx(openIdx === idx ? -1 : idx)}
                whileHover={{ 
                  scale: 1.02,
                  boxShadow: '0 4px 20px rgba(0,0,0,0.12)',
                  background: 'rgba(255, 255, 255, 0.95)'
                }}
                whileTap={{ scale: 0.98 }}
              >
                <span style={{ 
                  background: '#FFE89A', 
                  borderRadius: 6, 
                  padding: '4px 12px', 
                  marginRight: 12,
                  fontSize: 20
                }}>{title}</span>
                <motion.span
                  animate={{ rotate: openIdx === idx ? 180 : 0 }}
                  transition={{ duration: 0.3, ease: "easeInOut" }}
                  style={{ 
                    marginLeft: 'auto', 
                    marginRight: 6, 
                    fontSize: 28, 
                    color: '#5B93C6', 
                    display: 'flex', 
                    alignItems: 'center',
                    filter: 'drop-shadow(0 1px 2px rgba(0,0,0,0.1))'
                  }}
                >
                  {openIdx === idx ? '⌃' : '⌄'}
                </motion.span>
              </motion.div>
              <motion.div
                initial={false}
                animate={{ 
                  height: openIdx === idx ? 'auto' : 0, 
                  opacity: openIdx === idx ? 1 : 0,
                  scale: openIdx === idx ? 1 : 0.95
                }}
                transition={{ 
                  duration: 0.4, 
                  ease: "easeInOut",
                  opacity: { duration: 0.3 },
                  scale: { duration: 0.2 }
                }}
                style={{
                  overflow: 'visible',
                  background: 'rgba(255, 255, 255, 0.95)',
                  borderRadius: 12,
                  boxShadow: openIdx === idx ? '0 8px 32px rgba(91, 147, 198, 0.15)' : 'none',
                  marginTop: 8,
                  marginBottom: openIdx === idx ? 8 : 0,
                  padding: openIdx === idx ? 20 : 0,
                  border: openIdx === idx ? '1px solid rgba(91, 147, 198, 0.2)' : 'none',
                  minHeight: openIdx === idx ? 'auto' : 0,
                  maxHeight: openIdx === idx ? 'none' : 0,
                  backdropFilter: 'blur(10px)',
                  transformOrigin: 'top'
                }}
              >
                {loading ? (
                  <motion.div 
                    style={{ textAlign: 'center', color: '#5B93C6', fontWeight: 700, fontSize: 18, padding: 24 }}
                    animate={{ opacity: [0.5, 1, 0.5] }}
                    transition={{ duration: 1.5, repeat: Infinity }}
                  >
                    로딩 중...
                  </motion.div>
                ) : (
                  <motion.div 
                    style={{ whiteSpace: 'pre-line', color: '#222', fontSize: 17, lineHeight: 1.7 }}
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.3, delay: 0.1 }}
                  >
                    {contents[idx]}
                  </motion.div>
                )}
              </motion.div>
              <motion.div 
                style={{ 
                  height: 3, 
                  background: 'linear-gradient(90deg, #5B93C6 0%, #4a7bb8 100%)', 
                  borderRadius: 2, 
                  marginTop: 6 
                }}
                initial={{ scaleX: 0 }}
                animate={{ scaleX: 1 }}
                transition={{ duration: 0.5, delay: idx * 0.1 + 0.2 }}
              />
            </motion.div>
          ))}
        </div>
      </motion.div>
    </AnimatePresence>
  );
};

export default AnalysisListPage; 