import React, { useState, useEffect } from 'react';
import { Compass, ImageOff } from 'lucide-react';

export interface ImageWithFallbackProps extends React.ImgHTMLAttributes<HTMLImageElement> {
  fallbackTitle?: string;
  fallbackSubtitle?: string;
}

export const ImageWithFallback: React.FC<ImageWithFallbackProps> = ({
  src,
  alt,
  className = '',
  fallbackTitle,
  fallbackSubtitle,
  referrerPolicy = 'no-referrer-when-downgrade',
  ...props
}) => {
  const [hasError, setHasError] = useState<boolean>(false);

  useEffect(() => {
    setHasError(false);
  }, [src]);

  if (hasError || !src) {
    return (
      <div
        data-testid="image-fallback"
        className={`relative w-full h-full min-h-[140px] bg-gradient-to-br from-slate-800 via-slate-900 to-slate-950 flex flex-col items-center justify-center p-4 text-center select-none ${className}`}
        role="img"
        aria-label={alt || fallbackTitle || 'Destination image preview'}
      >
        <div className="w-10 h-10 rounded-xl bg-white/10 backdrop-blur-sm flex items-center justify-center text-slate-300 mb-2 border border-white/10">
          <Compass className="w-5 h-5 text-terracotta-400 stroke-[1.8]" />
        </div>
        {fallbackTitle && (
          <p className="text-sm font-bold font-display text-white tracking-tight line-clamp-1">
            {fallbackTitle}
          </p>
        )}
        {fallbackSubtitle ? (
          <p className="text-[11px] text-slate-400 font-medium line-clamp-1 mt-0.5">
            {fallbackSubtitle}
          </p>
        ) : (
          <span className="inline-flex items-center gap-1 text-[10px] text-slate-400 mt-1">
            <ImageOff className="w-3 h-3 text-slate-500" />
            <span>Curated Travel Preview</span>
          </span>
        )}
      </div>
    );
  }

  return (
    <img
      src={src}
      alt={alt}
      referrerPolicy={referrerPolicy}
      onError={() => setHasError(true)}
      className={className}
      {...props}
    />
  );
};
