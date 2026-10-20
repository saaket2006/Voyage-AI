import React, { useState, useEffect } from 'react';
import { Destination } from '../../types/destination';
import {
  X,
  Star,
  Clock,
  Calendar,
  CloudSun,
  Bed,
  Utensils,
  Landmark,
  Banknote,
  Check,
  Bookmark,
  ExternalLink,
} from 'lucide-react';
import { Badge } from '../common/Badge';
import { Button } from '../common/Button';

export interface DestinationDetailModalProps {
  destination: Destination | null;
  onClose: () => void;
  isSaved?: boolean;
  onToggleSave?: (destinationId: string) => void;
}

type TabType = 'overview' | 'stays' | 'dining' | 'attractions' | 'budget';

export const DestinationDetailModal: React.FC<DestinationDetailModalProps> = ({
  destination,
  onClose,
  isSaved = false,
  onToggleSave,
}) => {
  const [activeTab, setActiveTab] = useState<TabType>('overview');

  // Handle ESC key to dismiss modal per accessibility standards
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };
    if (destination) {
      document.body.style.overflow = 'hidden';
      window.addEventListener('keydown', handleKeyDown);
    }
    return () => {
      document.body.style.overflow = 'unset';
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [destination, onClose]);

  if (!destination) return null;

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-6 bg-slate-950/60 backdrop-blur-sm transition-opacity duration-200"
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
      onClick={onClose}
    >
      {/* Modal Container with Emil Kowalski entrance physics (scale 0.96 -> 1, 200ms ease-out) */}
      <div
        className="relative bg-white rounded-3xl border border-slate-200 shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-hidden flex flex-col transform transition-transform duration-200 ease-[cubic-bezier(0.23,1,0.32,1)]"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header Hero Banner */}
        <div className="relative h-64 sm:h-72 w-full bg-slate-900 shrink-0">
          <img
            src={destination.heroImageUrl || destination.imageUrl}
            alt={destination.name}
            className="w-full h-full object-cover object-center"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-slate-950/80 via-slate-950/40 to-transparent" />

          {/* Close Button */}
          <button
            onClick={onClose}
            aria-label="Close modal"
            className="absolute top-4 right-4 w-9 h-9 rounded-full bg-black/40 hover:bg-black/60 text-white flex items-center justify-center backdrop-blur-md border border-white/20 transition-transform active:scale-90"
          >
            <X className="w-5 h-5" />
          </button>

          {/* Title and Key Badges */}
          <div className="absolute bottom-5 left-6 right-6 text-white">
            <div className="flex flex-wrap items-center gap-2 mb-2">
              <Badge variant="terracotta" size="sm" className="bg-terracotta-600/90 text-white border-transparent">
                {destination.region}
              </Badge>
              <span className="text-xs text-slate-200 font-medium">
                {destination.state}, India
              </span>
            </div>
            <h2 id="modal-title" className="text-3xl sm:text-4xl font-extrabold font-display tracking-tight text-white mb-1">
              {destination.name}
            </h2>
            <p className="text-xs sm:text-sm text-slate-200 font-medium line-clamp-1">
              {destination.tagline}
            </p>
          </div>
        </div>

        {/* Navigation Tabs Bar */}
        <div className="flex items-center gap-1 px-6 border-b border-slate-200 bg-slate-50/70 overflow-x-auto shrink-0 scrollbar-none">
          {[
            { id: 'overview', label: 'Overview', icon: <Landmark className="w-3.5 h-3.5" /> },
            { id: 'stays', label: `Stays (${destination.stays.length})`, icon: <Bed className="w-3.5 h-3.5" /> },
            { id: 'dining', label: `Dining (${destination.dining.length})`, icon: <Utensils className="w-3.5 h-3.5" /> },
            { id: 'attractions', label: `Attractions (${destination.attractions.length})`, icon: <Star className="w-3.5 h-3.5" /> },
            { id: 'budget', label: 'Budget Details', icon: <Banknote className="w-3.5 h-3.5" /> },
          ].map((tab) => {
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as TabType)}
                className={`flex items-center gap-1.5 py-3.5 px-3 border-b-2 text-xs font-semibold whitespace-nowrap transition-colors duration-150 active:scale-95 ${
                  isActive
                    ? 'border-brand-900 text-brand-900 bg-white/60'
                    : 'border-transparent text-slate-600 hover:text-slate-900'
                }`}
              >
                {tab.icon}
                <span>{tab.label}</span>
              </button>
            );
          })}
        </div>

        {/* Tab Content Panels (Scrollable) */}
        <div className="p-6 overflow-y-auto flex-1 space-y-6">
          {/* TAB 1: OVERVIEW */}
          {activeTab === 'overview' && (
            <div className="space-y-6">
              {/* Quick Info Grid */}
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
                <div className="p-3.5 rounded-2xl bg-slate-50 border border-slate-200/70">
                  <span className="text-[10px] font-bold uppercase tracking-wider text-slate-600 block mb-1">
                    Rating
                  </span>
                  <div className="flex items-center gap-1.5 text-sm font-bold text-slate-900">
                    <Star className="w-4 h-4 fill-amber-400 text-amber-400" />
                    <span>{destination.averageRating.toFixed(1)} / 5</span>
                  </div>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-50 border border-slate-200/70">
                  <span className="text-[10px] font-bold uppercase tracking-wider text-slate-600 block mb-1">
                    Ideal Trip Length
                  </span>
                  <div className="flex items-center gap-1.5 text-sm font-bold text-slate-900">
                    <Clock className="w-4 h-4 text-brand-700" />
                    <span>{destination.recommendedDurationDays} Days</span>
                  </div>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-50 border border-slate-200/70">
                  <span className="text-[10px] font-bold uppercase tracking-wider text-slate-600 block mb-1">
                    Best Season
                  </span>
                  <div className="flex items-center gap-1.5 text-xs font-bold text-slate-900">
                    <Calendar className="w-4 h-4 text-emerald-600 shrink-0" />
                    <span className="truncate">{destination.bestTimeToVisit}</span>
                  </div>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-50 border border-slate-200/70">
                  <span className="text-[10px] font-bold uppercase tracking-wider text-slate-600 block mb-1">
                    Current Climate
                  </span>
                  <div className="flex items-center gap-1.5 text-xs font-bold text-slate-900">
                    <CloudSun className="w-4 h-4 text-amber-500 shrink-0" />
                    <span>{destination.weather.tempC}°C ({destination.weather.condition})</span>
                  </div>
                </div>
              </div>

              {/* Narrative Description */}
              <div>
                <h3 className="text-sm font-bold uppercase tracking-wider text-slate-600 mb-2">
                  Destination Story
                </h3>
                <p className="text-sm text-slate-700 leading-relaxed font-sans">
                  {destination.description}
                </p>
              </div>

              {/* Curated Highlights */}
              <div>
                <h3 className="text-sm font-bold uppercase tracking-wider text-slate-600 mb-2.5">
                  Signature Experiences
                </h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                  {destination.highlights.map((highlight, idx) => (
                    <div
                      key={idx}
                      className="flex items-center gap-2 p-2.5 rounded-xl bg-brand-50/50 border border-brand-100/60 text-xs text-brand-950 font-medium"
                    >
                      <Check className="w-3.5 h-3.5 text-brand-700 shrink-0" />
                      <span>{highlight}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* TAB 2: CURATED STAYS */}
          {activeTab === 'stays' && (
            <div className="space-y-4">
              <p className="text-xs text-slate-600">
                Hand-vetted boutique havelis, eco-resorts, and heritage stays emphasizing architectural integrity.
              </p>
              <div className="grid grid-cols-1 gap-4">
                {destination.stays.map((stay) => (
                  <div
                    key={stay.id}
                    className="flex flex-col sm:flex-row gap-4 p-4 rounded-2xl border border-slate-200 bg-white shadow-xs hover:border-slate-300 transition-colors"
                  >
                    <img
                      src={stay.imageUrl}
                      alt={stay.name}
                      className="w-full sm:w-40 h-32 object-cover rounded-xl shrink-0"
                    />
                    <div className="flex-1 flex flex-col justify-between">
                      <div>
                        <div className="flex items-center justify-between mb-1">
                          <span className="text-[10px] font-bold uppercase tracking-wider text-brand-800 bg-brand-50 px-2 py-0.5 rounded-md border border-brand-200/50">
                            {stay.type}
                          </span>
                          <span className="flex items-center gap-1 text-xs font-bold text-slate-900">
                            <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
                            {stay.rating}
                          </span>
                        </div>
                        <h4 className="text-base font-bold text-slate-900 font-display">
                          {stay.name}
                        </h4>
                        <div className="flex flex-wrap gap-1.5 mt-2">
                          {stay.highlights.map((h, i) => (
                            <span key={i} className="text-[11px] text-slate-600 bg-slate-100 px-2 py-0.5 rounded-md">
                              {h}
                            </span>
                          ))}
                        </div>
                      </div>
                      <div className="mt-3 pt-2 border-t border-slate-100 flex items-center justify-between">
                        <span className="text-xs text-slate-600">From ₹{stay.pricePerNight.toLocaleString()} / night</span>
                        <span className="text-xs font-semibold text-brand-900 hover:underline cursor-pointer flex items-center gap-1">
                          View details <ExternalLink className="w-3 h-3" />
                        </span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 3: DINING & CUISINE */}
          {activeTab === 'dining' && (
            <div className="space-y-4">
              <p className="text-xs text-slate-600">
                Signature local dining, heritage sweets, and authentic regional culinary specialties.
              </p>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                {destination.dining.map((dine) => (
                  <div
                    key={dine.id}
                    className="p-4 rounded-2xl border border-slate-200 bg-white shadow-xs flex flex-col justify-between"
                  >
                    <div>
                      <div className="flex items-center justify-between mb-1.5">
                        <span className="text-[10px] font-bold text-terracotta-800 bg-terracotta-50 px-2 py-0.5 rounded-md border border-terracotta-200/50">
                          {dine.cuisine}
                        </span>
                        <span className="text-xs font-bold text-slate-700">{dine.priceRange}</span>
                      </div>
                      <h4 className="text-sm font-bold text-slate-900 font-display mb-1">
                        {dine.name}
                      </h4>
                      <p className="text-xs text-slate-600 mt-2">
                        <span className="font-semibold text-slate-800">Must Try: </span>
                        {dine.mustTry}
                      </p>
                    </div>
                    <div className="mt-3 pt-2 border-t border-slate-100 flex items-center justify-between text-xs text-slate-600">
                      <span className="flex items-center gap-1">
                        <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
                        <span className="font-semibold text-slate-900">{dine.rating}</span> rating
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 4: ATTRACTIONS */}
          {activeTab === 'attractions' && (
            <div className="space-y-4">
              <p className="text-xs text-slate-600">
                Curated monuments, architectural masterpieces, and scenic trails.
              </p>
              <div className="space-y-3">
                {destination.attractions.map((attr) => (
                  <div
                    key={attr.id}
                    className="p-4 rounded-2xl border border-slate-200 bg-white shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-3"
                  >
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <span className="text-[10px] font-bold text-slate-700 bg-slate-100 px-2 py-0.5 rounded-md">
                          {attr.category}
                        </span>
                        <span className="text-xs text-slate-600 flex items-center gap-1">
                          <Clock className="w-3 h-3" /> ~{attr.estimatedHours} hrs
                        </span>
                      </div>
                      <h4 className="text-sm font-bold text-slate-900 font-display">
                        {attr.name}
                      </h4>
                      <p className="text-xs text-slate-600 max-w-lg leading-relaxed">
                        {attr.description}
                      </p>
                    </div>
                    <div className="text-right shrink-0">
                      <span className="text-[10px] uppercase tracking-wider text-slate-600 block">
                        Entry Fee
                      </span>
                      <span className="text-sm font-bold text-slate-900">
                        {attr.entryFee === 0 ? 'Free Entry' : `₹${attr.entryFee}`}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 5: HONEST BUDGET INTELLIGENCE */}
          {activeTab === 'budget' && (
            <div className="space-y-6">
              <div className="p-4 rounded-2xl bg-amber-50/60 border border-amber-200/80">
                <h4 className="text-xs font-bold uppercase tracking-wider text-amber-900 mb-1">
                  Honest Data Provenance Principle
                </h4>
                <p className="text-xs text-amber-800 leading-relaxed font-sans">
                  Unlike conventional aggregators that omit hidden expenses, Voyage-AI provides realistic daily estimates covering curated stays, dining, transport, and admissions for 2 travelers.
                </p>
              </div>

              {/* Tiers Comparison Table */}
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
                <div className="p-4 rounded-2xl border border-slate-200 bg-white">
                  <span className="text-xs font-bold text-emerald-800 bg-emerald-50 px-2.5 py-0.5 rounded-full border border-emerald-200/50">
                    Budget Explorer
                  </span>
                  <p className="text-xl font-extrabold text-slate-900 mt-3 font-sans">
                    ₹{destination.budgetBreakdown.tiers.budget.toLocaleString()}
                    <span className="text-xs font-normal text-slate-600"> /day</span>
                  </p>
                  <p className="text-[11px] text-slate-600 mt-2">
                    Clean homestays, local eateries, public transit & auto-rickshaws.
                  </p>
                </div>

                <div className="p-4 rounded-2xl border-2 border-brand-800 bg-brand-50/30">
                  <span className="text-xs font-bold text-brand-900 bg-brand-100 px-2.5 py-0.5 rounded-full border border-brand-300">
                    Comfort Curated (Recommended)
                  </span>
                  <p className="text-xl font-extrabold text-brand-950 mt-3 font-sans">
                    ₹{destination.budgetBreakdown.tiers.comfort.toLocaleString()}
                    <span className="text-xs font-normal text-slate-600"> /day</span>
                  </p>
                  <p className="text-[11px] text-slate-600 mt-2">
                    Heritage havelis, signature dining, dedicated cab transfers & guided entries.
                  </p>
                </div>

                <div className="p-4 rounded-2xl border border-slate-200 bg-white">
                  <span className="text-xs font-bold text-amber-800 bg-amber-50 px-2.5 py-0.5 rounded-full border border-amber-200/50">
                    Luxury Heritage
                  </span>
                  <p className="text-xl font-extrabold text-slate-900 mt-3 font-sans">
                    ₹{destination.budgetBreakdown.tiers.luxury.toLocaleString()}
                    <span className="text-xs font-normal text-slate-600"> /day</span>
                  </p>
                  <p className="text-[11px] text-slate-600 mt-2">
                    5-star palace hotels, private chauffeur, fine royal cuisine & exclusive private tours.
                  </p>
                </div>
              </div>

              {/* Total trip estimate */}
              <div className="p-4 rounded-2xl bg-slate-100 border border-slate-200 flex items-center justify-between">
                <div>
                  <span className="text-xs text-slate-600">Estimated Total for 2 Travelers ({destination.recommendedDurationDays} Days):</span>
                  <p className="text-lg font-bold text-slate-900 font-sans">
                    ₹{destination.budgetBreakdown.estimatedTotalForTwo.toLocaleString()} (Comfort Baseline)
                  </p>
                </div>
                <Badge variant="outline" size="sm">
                  INR Estimates
                </Badge>
              </div>
            </div>
          )}
        </div>

        {/* Modal Footer with Tactile Action Bar */}
        <div className="p-4 px-6 border-t border-slate-200 bg-white flex items-center justify-between gap-3 shrink-0">
          <div className="text-xs text-slate-600">
            <span>Avg Daily: </span>
            <span className="font-bold text-slate-900 font-sans">₹{destination.budgetBreakdown.averageDaily.toLocaleString()}</span>
          </div>

          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="md"
              icon={<Bookmark className={`w-4 h-4 ${isSaved ? 'fill-terracotta-600 text-terracotta-600' : ''}`} />}
              onClick={() => onToggleSave && onToggleSave(destination.id)}
            >
              {isSaved ? 'Saved to Wishlist' : 'Save Destination'}
            </Button>

            <Button
              variant="primary"
              size="md"
              onClick={onClose}
            >
              Done Exploring
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};
