import React from "react";

type BasicButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  children: React.ReactNode;
};

const BasicButton: React.FC<BasicButtonProps> = ({ children, ...props }) => (
  <button
    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition font-semibold"
    {...props}
  >
    {children}
  </button>
);

export default BasicButton; 