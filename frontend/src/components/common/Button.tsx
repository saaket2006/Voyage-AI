import React from 'react';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'accent' | 'ghost' | 'outline';
  size?: 'sm' | 'md' | 'lg';
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  isLoading?: boolean;
}

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  icon,
  iconPosition = 'left',
  isLoading = false,
  className = '',
  disabled,
  ...props
}) => {
  const baseStyles =
    'relative inline-flex items-center justify-center font-medium font-sans rounded-xl focus:outline-none focus-visible:ring-2 focus-visible:ring-brand-600 focus-visible:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed select-none transition-[transform,background-color,border-color,color,box-shadow] duration-150 ease-[cubic-bezier(0.23,1,0.32,1)] active:scale-[0.97]';

  const variantStyles = {
    primary:
      'bg-brand-900 text-white hover:bg-brand-800 shadow-sm border border-brand-950/20 active:bg-brand-950',
    accent:
      'bg-terracotta-600 text-white hover:bg-terracotta-500 shadow-sm border border-terracotta-700/20 active:bg-terracotta-700',
    secondary:
      'bg-slate-100 text-slate-800 hover:bg-slate-200/80 border border-slate-200/80 active:bg-slate-300/70',
    outline:
      'bg-white text-slate-700 hover:bg-slate-50 border border-slate-300 shadow-xs hover:border-slate-400 active:bg-slate-100',
    ghost:
      'bg-transparent text-slate-600 hover:text-slate-900 hover:bg-slate-100/70 active:bg-slate-200/60',
  };

  const sizeStyles = {
    sm: 'text-xs px-3 py-1.5 gap-1.5',
    md: 'text-sm px-4 py-2 gap-2',
    lg: 'text-base px-5 py-2.5 gap-2.5',
  };

  return (
    <button
      className={`${baseStyles} ${variantStyles[variant]} ${sizeStyles[size]} ${className}`}
      disabled={disabled || isLoading}
      {...props}
    >
      {isLoading ? (
        <span className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin" />
      ) : (
        <>
          {icon && iconPosition === 'left' && <span className="shrink-0">{icon}</span>}
          <span>{children}</span>
          {icon && iconPosition === 'right' && <span className="shrink-0">{icon}</span>}
        </>
      )}
    </button>
  );
};
