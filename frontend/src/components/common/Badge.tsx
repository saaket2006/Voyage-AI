import React from 'react';

export interface BadgeProps {
  children: React.ReactNode;
  variant?: 'brand' | 'terracotta' | 'neutral' | 'success' | 'outline';
  size?: 'sm' | 'md';
  className?: string;
  icon?: React.ReactNode;
}

export const Badge: React.FC<BadgeProps> = ({
  children,
  variant = 'neutral',
  size = 'md',
  className = '',
  icon,
}) => {
  const variantStyles = {
    brand: 'bg-brand-50 text-brand-900 border-brand-200/60',
    terracotta: 'bg-terracotta-50 text-terracotta-900 border-terracotta-200/60',
    neutral: 'bg-slate-100 text-slate-700 border-slate-200/70',
    success: 'bg-emerald-50 text-emerald-800 border-emerald-200/60',
    outline: 'bg-white/80 text-slate-700 border-slate-300 backdrop-blur-sm',
  };

  const sizeStyles = {
    sm: 'text-[11px] px-2 py-0.5 gap-1 tracking-tight',
    md: 'text-xs px-2.5 py-1 gap-1.5 font-medium',
  };

  return (
    <span
      className={`inline-flex items-center rounded-full border transition-colors ${variantStyles[variant]} ${sizeStyles[size]} ${className}`}
    >
      {icon && <span className="shrink-0">{icon}</span>}
      <span>{children}</span>
    </span>
  );
};
