import React from 'react';
import { Compass, Sparkles, MapPin, ShieldCheck, Banknote } from 'lucide-react';
import { Badge } from '../common/Badge';

export interface HeroSectionProps {
  onFeaturedClick: () => void;
}

export const HeroSection: React.FC<HeroSectionProps> = ({ onFeaturedClick }) => {
  return (
    <section className="relative overflow-hidden rounded-3xl bg-slate-900 text-white mb-10 shadow-lg border border-slate-800">
      {/* Background Photography with Dark Gradient Overlay for Contrast */}
      <div className="absolute inset-0 z-0">
        <img
          src="https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=1600&q=85"
          alt="Rajasthan Royal Palace"
          className="w-full h-full object-cover object-center opacity-40 scale-105 transition-transform duration-700 ease-out"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-900/80 to-transparent" />
        <div className="absolute inset-0 bg-gradient-to-r from-slate-950/90 via-slate-900/60 to-transparent" />
      </div>

      <div className="relative z-10 max-w-4xl px-6 py-12 sm:px-10 sm:py-16 lg:py-20 flex flex-col items-start justify-center">
        {/* Curated Editorial Tag */}
        <div className="flex flex-wrap items-center gap-2 mb-4">
          <Badge variant="terracotta" size="md" icon={<Sparkles className="w-3.5 h-3.5 text-terracotta-400" />}>
            Curated Subcontinent Portfolio
          </Badge>
          <span className="text-xs text-slate-300 font-medium flex items-center gap-1">
            <MapPin className="w-3.5 h-3.5 text-terracotta-400" /> 6 Handpicked Indian Regions
          </span>
        </div>

        {/* Hero Title */}
        <h1 className="text-3xl sm:text-5xl lg:text-6xl font-extrabold font-display tracking-tight text-white leading-tight mb-4 max-w-2xl">
          Craft Your Journey Across the Subcontinent
        </h1>

        {/* Narrative Subtitle */}
        <p className="text-base sm:text-lg text-slate-200 max-w-xl leading-relaxed mb-8">
          Explore hand-curated destinations, architectural havelis, and signature culinary trails—anchored by transparent, realistic daily budget intelligence.
        </p>

        {/* Editorial Trust Pillars */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 w-full max-w-2xl pt-6 border-t border-white/10 text-xs">
          <div className="flex items-center gap-2.5 text-slate-300">
            <div className="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center shrink-0">
              <ShieldCheck className="w-4 h-4 text-emerald-400" />
            </div>
            <div>
              <p className="font-semibold text-white">Curated Stays</p>
              <p className="text-[11px] text-slate-400">Vetted heritage & boutique havelis</p>
            </div>
          </div>

          <div className="flex items-center gap-2.5 text-slate-300">
            <div className="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center shrink-0">
              <Banknote className="w-4 h-4 text-amber-300" />
            </div>
            <div>
              <p className="font-semibold text-white">Transparent Budgets</p>
              <p className="text-[11px] text-slate-400">Accurate daily tier estimates</p>
            </div>
          </div>

          <div className="flex items-center gap-2.5 text-slate-300">
            <div className="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center shrink-0">
              <Compass className="w-4 h-4 text-sky-400" />
            </div>
            <div>
              <p className="font-semibold text-white">Trip Continuity</p>
              <p className="text-[11px] text-slate-400">Seamless transition into planning</p>
            </div>
          </div>
        </div>

        {/* Action Button */}
        <div className="mt-8 flex items-center gap-3">
          <button
            onClick={onFeaturedClick}
            className="inline-flex items-center gap-2 px-5 py-2.5 text-sm font-semibold rounded-xl bg-terracotta-600 hover:bg-terracotta-500 text-white transition-[transform,background-color] duration-150 active:scale-95 shadow-md border border-terracotta-500/50"
          >
            <span>Explore Featured: Jaipur, Rajasthan</span>
            <span className="text-white/80">→</span>
          </button>
        </div>
      </div>
    </section>
  );
};
