import React from 'react';
import { Compass, RotateCcw } from 'lucide-react';
import { Button } from './Button';

export interface EmptyStateProps {
  title?: string;
  description?: string;
  onReset?: () => void;
  actionText?: string;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  title = 'No destinations found',
  description = 'Try adjusting your region, travel style, or budget filters to explore other destinations across India.',
  onReset,
  actionText = 'Reset all filters',
}) => {
  return (
    <div className="py-16 px-6 text-center bg-white rounded-3xl border border-dashed border-slate-200 max-w-lg mx-auto my-8">
      <div className="w-14 h-14 rounded-2xl bg-brand-50 text-brand-800 flex items-center justify-center mx-auto mb-4 border border-brand-100/80 shadow-xs">
        <Compass className="w-7 h-7 stroke-[1.75]" />
      </div>
      <h3 className="text-xl font-bold font-display text-slate-900 mb-2 tracking-tight">
        {title}
      </h3>
      <p className="text-sm text-slate-600 mb-6 leading-relaxed max-w-md mx-auto">
        {description}
      </p>
      {onReset && (
        <Button
          variant="secondary"
          size="md"
          icon={<RotateCcw className="w-4 h-4" />}
          onClick={onReset}
        >
          {actionText}
        </Button>
      )}
    </div>
  );
};
