import React from 'react';
import { Region, TravelStyle, BudgetTier } from '../../types/destination';
import { SlidersHorizontal, RotateCcw } from 'lucide-react';

export interface FilterBarProps {
  selectedRegion: Region;
  onSelectRegion: (region: Region) => void;
  selectedStyle: TravelStyle;
  onSelectStyle: (style: TravelStyle) => void;
  selectedBudget: BudgetTier | 'All';
  onSelectBudget: (tier: BudgetTier | 'All') => void;
  onResetFilters: () => void;
  hasActiveFilters: boolean;
  totalResultsCount: number;
}

const REGIONS: Region[] = [
  'All',
  'North India',
  'South India',
  'Western Ghats',
  'Himalayas',
  'Coastal & Islands',
];

const STYLES: TravelStyle[] = [
  'All',
  'Heritage & Culture',
  'Nature & Wildlife',
  'Wellness & Spiritual',
  'Adventure & Trekking',
  'Coastal & Relaxation',
];

const BUDGETS: Array<BudgetTier | 'All'> = ['All', 'Budget', 'Comfort', 'Luxury'];

export const FilterBar: React.FC<FilterBarProps> = ({
  selectedRegion,
  onSelectRegion,
  selectedStyle,
  onSelectStyle,
  selectedBudget,
  onSelectBudget,
  onResetFilters,
  hasActiveFilters,
  totalResultsCount,
}) => {
  return (
    <div className="bg-white rounded-2xl p-4 sm:p-5 border border-slate-200/80 shadow-xs mb-8 space-y-4">
      {/* Top row: Header, filter icon & clear button */}
      <div className="flex items-center justify-between gap-2 border-b border-slate-100 pb-3">
        <div className="flex items-center gap-2 text-slate-900 font-semibold text-sm">
          <SlidersHorizontal className="w-4 h-4 text-brand-900" />
          <span>Curated Discovery Filters</span>
          <span className="text-xs font-normal text-slate-600 ml-1">
            ({totalResultsCount} {totalResultsCount === 1 ? 'destination' : 'destinations'})
          </span>
        </div>

        {hasActiveFilters && (
          <button
            onClick={onResetFilters}
            className="text-xs font-medium text-terracotta-700 hover:text-terracotta-900 flex items-center gap-1 transition-colors active:scale-95"
          >
            <RotateCcw className="w-3.5 h-3.5" />
            <span>Reset filters</span>
          </button>
        )}
      </div>

      {/* Region Selector Pills */}
      <div>
        <span className="block text-[11px] font-bold uppercase tracking-wider text-slate-600 mb-2">
          Region
        </span>
        <div className="flex flex-wrap gap-1.5" role="group" aria-label="Filter by region">
          {REGIONS.map((region) => {
            const isSelected = selectedRegion === region;
            return (
              <button
                key={region}
                onClick={() => onSelectRegion(region)}
                aria-pressed={isSelected}
                className={`text-xs px-3 py-1.5 rounded-xl font-medium transition-[background-color,color,transform,box-shadow] duration-150 ease-[cubic-bezier(0.23,1,0.32,1)] active:scale-95 ${
                  isSelected
                    ? 'bg-brand-900 text-white shadow-xs font-semibold'
                    : 'bg-slate-100/80 text-slate-600 hover:bg-slate-200/70 hover:text-slate-900'
                }`}
              >
                {region}
              </button>
            );
          })}
        </div>
      </div>

      {/* Travel Style & Budget Row */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
        {/* Style Selector */}
        <div>
          <span className="block text-[11px] font-bold uppercase tracking-wider text-slate-600 mb-2">
            Experience Style
          </span>
          <div className="flex flex-wrap gap-1.5" role="group" aria-label="Filter by travel style">
            {STYLES.map((style) => {
              const isSelected = selectedStyle === style;
              return (
                <button
                  key={style}
                  onClick={() => onSelectStyle(style)}
                  aria-pressed={isSelected}
                  className={`text-xs px-2.5 py-1 rounded-lg font-medium transition-[background-color,color,transform] duration-150 active:scale-95 ${
                    isSelected
                      ? 'bg-terracotta-600 text-white font-semibold'
                      : 'bg-slate-100/70 text-slate-600 hover:bg-slate-200/60 hover:text-slate-900'
                  }`}
                >
                  {style}
                </button>
              );
            })}
          </div>
        </div>

        {/* Budget Tier Selector */}
        <div>
          <span className="block text-[11px] font-bold uppercase tracking-wider text-slate-600 mb-2">
            Budget Tier
          </span>
          <div className="flex flex-wrap gap-1.5" role="group" aria-label="Filter by budget tier">
            {BUDGETS.map((tier) => {
              const isSelected = selectedBudget === tier;
              return (
                <button
                  key={tier}
                  onClick={() => onSelectBudget(tier)}
                  aria-pressed={isSelected}
                  className={`text-xs px-3 py-1 rounded-lg font-medium transition-[background-color,color,transform] duration-150 active:scale-95 ${
                    isSelected
                      ? 'bg-slate-900 text-white font-semibold'
                      : 'bg-slate-100/70 text-slate-600 hover:bg-slate-200/60 hover:text-slate-900'
                  }`}
                >
                  {tier === 'All' ? 'All Tiers' : tier}
                </button>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};
