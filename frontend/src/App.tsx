import React, { useMemo } from 'react';
import KakaoLoginPage from './pages/KakaoLoginPage';

function getBoxStyle() {
  const vw = window.innerWidth;
  const vh = window.innerHeight;
  const targetRatio = 375 / 812;
  const currentRatio = vw / vh;
  let width, height;
  if (currentRatio > targetRatio) {
    // 화면이 더 넓음: 높이를 기준으로 맞춤
    height = vh;
    width = vh * targetRatio;
  } else {
    // 화면이 더 높음: 너비를 기준으로 맞춤
    width = vw;
    height = vw / targetRatio;
  }
  return {
    width: `${width}px`,
    height: `${height}px`,
    background: '#fff',
    boxShadow: '0 4px 32px 0 rgba(0,0,0,0.08)',
    overflow: 'hidden',
    display: 'flex',
    flexDirection: 'column' as const,
  };
}

const App: React.FC = () => {
  const [_, setRerender] = React.useState(0);
  React.useEffect(() => {
    const onResize = () => setRerender(v => v + 1);
    window.addEventListener('resize', onResize);
    return () => window.removeEventListener('resize', onResize);
  }, []);
  const boxStyle = useMemo(getBoxStyle, [window.innerWidth, window.innerHeight]);

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-[#ECEEEF]">
      <div style={boxStyle}>
        <KakaoLoginPage />
      </div>
    </div>
  );
};

export default App;
