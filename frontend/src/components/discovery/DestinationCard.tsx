import React from 'react';
import { Destination } from '../../types/destination';
import { Star, Clock, CloudSun, ArrowRight, Bed, Utensils, MapPin, Bookmark } from 'lucide-react';
import { Badge } from '../common/Badge';
import { ImageWithFallback } from '../common/ImageWithFallback';

export interface DestinationCardProps {
  destination: Destination;
  onSelect: (destination: Destination) => void;
  isSaved?: boolean;
  onToggleSave?: (destinationId: string) => void;
}

export const DestinationCard: React.FC<DestinationCardProps> = ({ destination, onSelect, isSaved, onToggleSave }) => {
  const budgetColorMap = {
    Budget: 'text-emerald-700 bg-emerald-50 border-emerald-200/60',
    Comfort: 'text-brand-900 bg-brand-50 border-brand-200/60',
    Luxury: 'text-amber-800 bg-amber-50 border-amber-200/60',
  };

  return (
    <article className="group bg-white rounded-3xl border border-slate-200/80 overflow-hidden shadow-xs hover:shadow-md hover:border-slate-300 transition-[box-shadow,border-color,transform] duration-200 ease-[cubic-bezier(0.23,1,0.32,1)] flex flex-col justify-between">
      {/* Visual Header with Photography & Reusable Fallback */}
      <div className="relative h-60 w-full overflow-hidden bg-slate-900">
        <ImageWithFallback
          src={destination.imageUrl}
          alt={`${destination.name}, ${destination.state}`}
          fallbackTitle={destination.name}
          fallbackSubtitle={`${destination.state}, India`}
          loading="lazy"
          className="w-full h-full object-cover object-center group-hover:scale-105 transition-transform duration-500 ease-out"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-slate-950/75 via-slate-950/20 to-transparent pointer-events-none" />

        {/* Region & Weather Badges + Bookmark */}
        <div className="absolute top-3.5 left-3.5 right-3.5 flex items-center justify-between gap-2 z-10 pointer-events-auto">
          <Badge variant="outline" size="sm" className="bg-white/95 text-slate-800 font-semibold shadow-xs">
            {destination.region}
          </Badge>
          <div className="flex items-center gap-1.5">
            <div
              className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-slate-900/85 backdrop-blur-md text-white text-[11px] font-medium border border-white/10 shadow-xs"
              title="Seasonal median temperature estimate (Illustrative)"
            >
              <CloudSun className="w-3.5 h-3.5 text-amber-300" />
              <span>{destination.weather.tempC}°C</span>
              <span className="text-[9px] text-slate-400 font-normal">est.</span>
            </div>
            {onToggleSave && (
              <button
                type="button"
                onClick={(e) => {
                  e.stopPropagation();
                  onToggleSave(destination.id);
                }}
                className={`p-1.5 rounded-full backdrop-blur-md border shadow-xs transition-[transform,background-color] active:scale-90 focus:outline-none ${
                  isSaved
                    ? 'bg-terracotta-600 text-white border-terracotta-500 hover:bg-terracotta-700'
                    : 'bg-slate-900/85 text-white/90 border-white/10 hover:bg-slate-800'
                }`}
                aria-label={isSaved ? `Remove ${destination.name} from wishlist` : `Save ${destination.name} to wishlist`}
                title={isSaved ? 'Saved to Wishlist' : 'Save to Wishlist'}
              >
                <Bookmark className={`w-3.5 h-3.5 ${isSaved ? 'fill-current' : ''}`} />
              </button>
            )}
          </div>
        </div>

        {/* Photo Attribution Credit when available */}
        {destination.provenance?.imageAttributionText && (
          <div
            className="absolute bottom-1 right-2 z-10 px-1.5 py-0.5 rounded bg-black/60 backdrop-blur-xs text-[9px] text-slate-300 truncate max-w-[75%]"
            title={`Photo: ${destination.provenance.imageAttributionText} (${destination.provenance.imageLicenseType || 'All Rights Reserved'})`}
          >
            📷 {destination.provenance.imageAttributionText}
          </div>
        )}

        {/* Bottom image overlay: Title & State */}
        <div className="absolute bottom-3.5 left-3.5 right-3.5 text-white pointer-events-none">
          <div className="flex items-center gap-1.5 text-xs text-slate-200 font-medium mb-0.5">
            <MapPin className="w-3.5 h-3.5 text-terracotta-400 shrink-0" />
            <span>{destination.state}, India</span>
          </div>
          <h2 className="text-2xl font-bold font-display tracking-tight text-white leading-tight">
            {destination.name}
          </h2>
        </div>
      </div>

      {/* Body Content */}
      <div className="p-5 flex-1 flex flex-col justify-between space-y-4">
        <div>
          {/* Metadata Row: Rating & Duration */}
          <div className="flex items-center justify-between text-xs text-slate-600 mb-2.5">
            <div
              className="flex items-center gap-1 font-semibold text-slate-900"
              title="Curated community benchmark rating (Illustrative)"
            >
              <Star className="w-4 h-4 fill-amber-400 text-amber-400" />
              <span>{destination.averageRating.toFixed(1)}</span>
              <span className="text-slate-600 font-normal">({destination.reviewCount.toLocaleString()})</span>
              <span className="text-[10px] font-medium text-slate-600 bg-slate-100 px-1.5 py-0.2 rounded border border-slate-200">
                {destination.provenance?.curationStatus === 'COMMUNITY_INGESTED' ? 'Community' : 'Curated'}
              </span>
            </div>
            <div className="flex items-center gap-1 text-slate-600">
              <Clock className="w-3.5 h-3.5" />
              <span>{destination.recommendedDurationDays > 0 ? `${destination.recommendedDurationDays} Days Recommended` : 'Flexible Duration'}</span>
            </div>
          </div>

          {/* Editorial Tagline */}
          <p className="text-xs font-semibold text-brand-900 mb-1.5 line-clamp-1">
            {destination.tagline}
          </p>

          {/* Description */}
          <p className="text-xs text-slate-600 leading-relaxed line-clamp-2 mb-3.5">
            {destination.description}
          </p>

          {/* Quick counts for Stays & Dining */}
          <div className="flex items-center gap-3 text-[11px] text-slate-600 font-medium py-2 px-3 rounded-xl bg-slate-50 border border-slate-100">
            <span className="flex items-center gap-1">
              <Bed className="w-3.5 h-3.5 text-brand-700" />
              <span>{destination.stays.length} Handpicked {destination.stays.length === 1 ? 'Stay' : 'Stays'}</span>
            </span>
            <span className="text-slate-300">•</span>
            <span className="flex items-center gap-1">
              <Utensils className="w-3.5 h-3.5 text-terracotta-600" />
              <span>{destination.dining.length} Culinary Spots</span>
            </span>
          </div>
        </div>

        {/* Footer: Honest Budget Indicator & CTA Button */}
        <div className="pt-3 border-t border-slate-100 flex items-center justify-between gap-3">
          <div>
            <div className="flex items-center gap-1.5">
              <span className="text-[10px] font-bold uppercase tracking-wider text-slate-600">
                Illustrative Est.
              </span>
              <span className={`text-[10px] font-bold px-1.5 py-0.2 rounded border ${budgetColorMap[destination.budgetTier]}`}>
                {destination.budgetTier}
              </span>
            </div>
            <p className="text-sm font-extrabold text-slate-900 font-sans">
              ₹{destination.budgetBreakdown.averageDaily.toLocaleString()}{' '}
              <span className="text-[11px] font-normal text-slate-600">/day</span>
            </p>
          </div>

          <button
            onClick={() => onSelect(destination)}
            className="inline-flex items-center gap-1.5 px-3.5 py-2 text-xs font-semibold rounded-xl bg-slate-900 text-white hover:bg-brand-900 transition-[transform,background-color] duration-150 active:scale-95 shadow-xs focus:outline-none focus-visible:ring-2 focus-visible:ring-brand-600 focus-visible:ring-offset-2"
            aria-label={`Explore details for ${destination.name}`}
          >
            <span>Explore Plan</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>
    </article>
  );
};
