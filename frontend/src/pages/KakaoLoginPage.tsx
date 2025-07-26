import React, { useState } from "react";
import FadeOutCircle from "../components/FadeOutCircle";
import NicknamePage from "./NicknamePage";
import WelcomePage from "./WelcomePage";
import ShatteredCrownPage from "./ShatteredCrownPage";
import QuestAcceptPage from "./QuestAcceptPage";
import CrownShinePage from "./CrownShinePage";
import loginImg from "../assets/로그인.png";

const KakaoLoginPage: React.FC = () => {
  const [step, setStep] = useState<'login' | 'fade' | 'nickname' | 'welcome' | 'shattered' | 'questAccept' | 'crownShine'>('login');
  const [nickname, setNickname] = useState("");

  if (step === 'fade') {
    return (
      <FadeOutCircle image={loginImg} duration={1200} onComplete={() => setStep('nickname')} />
    );
  }
  if (step === 'nickname') {
    return <NicknamePage onComplete={(name) => { setNickname(name); setStep('welcome'); }} />;
  }
  if (step === 'welcome') {
    return <WelcomePage nickname={nickname} onNext={() => setStep('shattered')} />;
  }
  if (step === 'shattered') {
    return <ShatteredCrownPage onNext={() => setStep('questAccept')} />;
  }
  if (step === 'questAccept') {
    return <QuestAcceptPage onAccept={() => setStep('crownShine')} />;
  }
  if (step === 'crownShine') {
    return <CrownShinePage />;
  }
  // 로그인 버튼 화면
  return (
    <div className="relative w-full h-full flex flex-col items-center justify-center" style={{
      backgroundImage: `url(${loginImg})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }}>
      <button
        className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 px-8 py-4 bg-gray-200 text-2xl font-bold rounded-md shadow"
        onClick={() => setStep('fade')}
      >
        카카오 로그인
      </button>
    </div>
  );
};

export default React.memo(KakaoLoginPage); 