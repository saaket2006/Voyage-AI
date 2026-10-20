import React from 'react';

export const SkeletonCard: React.FC = () => {
  return (
    <div
      className="bg-white rounded-2xl border border-slate-200/80 overflow-hidden shadow-xs flex flex-col animate-pulse"
      aria-hidden="true"
    >
      <div className="h-56 bg-slate-200 w-full" />
      <div className="p-5 flex-1 flex flex-col justify-between space-y-4">
        <div className="space-y-2.5">
          <div className="flex items-center justify-between">
            <div className="h-4 bg-slate-200 rounded-md w-24" />
            <div className="h-4 bg-slate-200 rounded-md w-16" />
          </div>
          <div className="h-6 bg-slate-200 rounded-md w-3/4" />
          <div className="h-4 bg-slate-200 rounded-md w-full" />
          <div className="h-4 bg-slate-200 rounded-md w-5/6" />
        </div>
        <div className="pt-4 border-t border-slate-100 flex items-center justify-between">
          <div className="h-5 bg-slate-200 rounded-md w-28" />
          <div className="h-8 bg-slate-200 rounded-xl w-24" />
        </div>
      </div>
    </div>
  );
};
