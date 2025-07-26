import React, { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import darkBg from "../assets/암흑배경.png";
import crownShine from "../assets/빛나는왕관.png";

interface CrownShinePageProps {
  onComplete?: () => void;
}

const CrownShinePage: React.FC<CrownShinePageProps> = ({ onComplete }) => {
  const controls = useAnimation();

  useEffect(() => {
    controls.start({
      rotate: 360,
      filter: [
        "brightness(1)",
        "brightness(1.5)",
        "brightness(1)",
        "brightness(1.7)",
        "brightness(1)"
      ],
      transition: { duration: 2, ease: "easeInOut" },
    }).then(() => {
      if (onComplete) onComplete();
    });
  }, [controls, onComplete]);

  return (
    <div
      className="relative w-full h-full flex flex-col items-center justify-center"
      style={{ backgroundImage: `url(${darkBg})`, backgroundSize: "cover", backgroundPosition: "center" }}
    >
      <motion.img
        src={crownShine}
        alt="빛나는 왕관"
        className="w-[220px] h-auto max-w-[90%] select-none pointer-events-none"
        initial={{ rotate: 0, filter: "brightness(1)" }}
        animate={controls}
      />
    </div>
  );
};

export default CrownShinePage; 