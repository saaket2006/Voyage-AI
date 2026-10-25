import React, { useState, useEffect, useMemo } from 'react';
import { Navbar } from './components/layout/Navbar';
import { HeroSection } from './components/discovery/HeroSection';
import { FilterBar } from './components/discovery/FilterBar';
import { DestinationCard } from './components/discovery/DestinationCard';
import { DestinationDetailModal } from './components/discovery/DestinationDetailModal';
import { SkeletonCard } from './components/common/SkeletonCard';
import { EmptyState } from './components/common/EmptyState';
import { MOCK_DESTINATIONS } from './data/mockDestinations';
import { Destination, Region, TravelStyle, BudgetTier } from './types/destination';
import { Compass, Sparkles, MapPin, Calendar, Heart, Layers, Info } from 'lucide-react';
import { Button } from './components/common/Button';
import { ImageWithFallback } from './components/common/ImageWithFallback';
import { AiTravelAssistant } from './components/ai/AiTravelAssistant';

export const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>('destinations');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [selectedRegion, setSelectedRegion] = useState<Region>('All');
  const [selectedStyle, setSelectedStyle] = useState<TravelStyle>('All');
  const [selectedBudget, setSelectedBudget] = useState<BudgetTier | 'All'>('All');
  const [selectedDestination, setSelectedDestination] = useState<Destination | null>(null);
  const [savedDestinationIds, setSavedDestinationIds] = useState<Set<string>>(new Set());
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [backendConnected, setBackendConnected] = useState<boolean>(false);
  const [apiDestinations, setApiDestinations] = useState<Destination[] | null>(null);
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [pageSize] = useState<number>(12);
  const [totalCount, setTotalCount] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [fetchError, setFetchError] = useState<string | null>(null);

  const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api/v1';

  // Verify backend connectivity on load
  useEffect(() => {
    let isMounted = true;
    const checkHealth = async () => {
      try {
        const response = await fetch(`${apiBaseUrl}/health`);
        if (response.ok && isMounted) {
          setBackendConnected(true);
        }
      } catch {
        if (isMounted) setBackendConnected(false);
      }
    };
    checkHealth();
    return () => {
      isMounted = false;
    };
  }, [apiBaseUrl]);

  // Reset page to 0 whenever search or filter options change
  useEffect(() => {
    setCurrentPage(0);
  }, [searchQuery, selectedRegion, selectedStyle, selectedBudget]);

  // Fetch persistent destinations from backend when connected with pagination and server-side filtering
  useEffect(() => {
    if (!backendConnected) {
      setApiDestinations(null);
      return;
    }

    let isMounted = true;
    const fetchDestinations = async () => {
      setIsLoading(true);
      setFetchError(null);
      try {
        const params = new URLSearchParams();
        params.append('page', currentPage.toString());
        params.append('size', pageSize.toString());
        if (searchQuery.trim()) {
          params.append('search', searchQuery.trim());
        }
        if (selectedRegion !== 'All') {
          params.append('region', selectedRegion);
        }
        if (selectedStyle !== 'All') {
          params.append('travelStyle', selectedStyle);
        }
        if (selectedBudget !== 'All') {
          params.append('budgetTier', selectedBudget);
        }

        const res = await fetch(`${apiBaseUrl}/destinations?${params.toString()}`);
        if (res.ok && isMounted) {
          const totalCountHeader = res.headers.get('X-Total-Count');
          const totalPagesHeader = res.headers.get('X-Total-Pages');
          if (totalCountHeader) {
            setTotalCount(parseInt(totalCountHeader, 10));
          }
          if (totalPagesHeader) {
            setTotalPages(Math.max(1, parseInt(totalPagesHeader, 10)));
          }

          const json = await res.json();
          const items = json.data || [];
          const mapped: Destination[] = items.map((item: any) => {
            const mock = MOCK_DESTINATIONS.find((m) => m.id === item.id || m.name.toLowerCase() === (item.name || '').toLowerCase());
            return {
              id: item.id,
              name: item.name,
              state: item.state || (mock ? mock.state : ''),
              country: item.country || 'India',
              region: (item.region || (mock ? mock.region : 'North India')) as Region,
              tagline: item.tagline || (mock ? mock.tagline : `Explore ${item.name}`),
              description: item.description || (mock ? mock.description : ''),
              imageUrl: item.imageUrl || (mock ? mock.imageUrl : item.heroImageUrl) || 'https://images.unsplash.com/photo-1599661046289-e31897846e41',
              heroImageUrl: item.heroImageUrl || (mock ? mock.heroImageUrl : item.imageUrl) || 'https://images.unsplash.com/photo-1599661046289-e31897846e41',
              travelStyles: (item.travelStyles && item.travelStyles.length > 0)
                ? item.travelStyles
                : (mock ? mock.travelStyles : ['Heritage & Culture']),
              budgetTier: (item.budgetTier as BudgetTier) || (mock ? mock.budgetTier : 'Comfort'),
              recommendedDurationDays: item.recommendedDurationDays != null ? item.recommendedDurationDays : (mock ? mock.recommendedDurationDays : 3),
              bestTimeToVisit: item.bestTimeToVisit || (mock ? mock.bestTimeToVisit : 'October to March'),
              averageRating: mock ? mock.averageRating : 4.8,
              reviewCount: mock ? mock.reviewCount : 120,
              budgetBreakdown: mock ? mock.budgetBreakdown : {
                currency: 'INR',
                averageDaily: 4500,
                estimatedTotalForTwo: 27000,
                durationDays: item.recommendedDurationDays || 3,
                tiers: { budget: 2500, comfort: 4500, luxury: 12000 }
              },
              stays: mock ? mock.stays : [],
              dining: mock ? mock.dining : [],
              attractions: mock ? mock.attractions : [],
              highlights: (mock && mock.highlights.length > 0) ? mock.highlights : [item.state, item.region].filter(Boolean),
              weather: mock ? mock.weather : { tempC: 25, condition: 'Sunny' },
              provenance: item.provenance ? {
                provider: item.provenance.provider,
                sourceUrl: item.provenance.sourceUrl,
                license: item.provenance.license,
                retrievedAt: item.provenance.retrievedAt,
                imageAttributionText: item.provenance.imageAttributionText,
                imageLicenseType: item.provenance.imageLicenseType,
                imageLicenseUrl: item.provenance.imageLicenseUrl,
                imageSourceUrl: item.provenance.imageSourceUrl,
                curationStatus: item.provenance.curationStatus,
              } : undefined
            };
          });
          setApiDestinations(mapped);
          if (!totalCountHeader) {
            setTotalCount(mapped.length);
            setTotalPages(1);
          }
        } else if (isMounted) {
          setFetchError(`Backend returned ${res.status}`);
        }
      } catch (err: any) {
        console.warn('Failed to fetch destinations from backend:', err);
        if (isMounted) {
          setFetchError(err.message || 'Network error fetching destinations');
        }
      } finally {
        if (isMounted) setIsLoading(false);
      }
    };

    fetchDestinations();
    return () => {
      isMounted = false;
    };
  }, [backendConnected, apiBaseUrl, currentPage, pageSize, searchQuery, selectedRegion, selectedStyle, selectedBudget]);

  // Mock destinations client-side filtering (fallback when backend is not connected)
  const mockFiltered = useMemo(() => {
    if (backendConnected && apiDestinations !== null) {
      return [];
    }
    return MOCK_DESTINATIONS.filter((item) => {
      if (selectedRegion !== 'All' && item.region !== selectedRegion) return false;
      if (selectedStyle !== 'All' && !item.travelStyles.includes(selectedStyle)) return false;
      if (selectedBudget !== 'All' && item.budgetTier !== selectedBudget) return false;
      if (searchQuery.trim()) {
        const query = searchQuery.toLowerCase();
        const matchesName = item.name.toLowerCase().includes(query);
        const matchesState = item.state.toLowerCase().includes(query);
        const matchesTagline = (item.tagline || '').toLowerCase().includes(query);
        const matchesHighlights = (item.highlights || []).some((h) => h.toLowerCase().includes(query));
        if (!matchesName && !matchesState && !matchesTagline && !matchesHighlights) {
          return false;
        }
      }
      return true;
    });
  }, [backendConnected, apiDestinations, selectedRegion, selectedStyle, selectedBudget, searchQuery]);

  // Active destinations to render on the current page
  const activeDestinations = useMemo(() => {
    if (backendConnected && apiDestinations !== null) {
      return apiDestinations;
    }
    const start = currentPage * pageSize;
    return mockFiltered.slice(start, start + pageSize);
  }, [backendConnected, apiDestinations, mockFiltered, currentPage, pageSize]);

  // Total matching count across the entire catalog
  const activeTotalCount = useMemo(() => {
    if (backendConnected && apiDestinations !== null) {
      return totalCount;
    }
    return mockFiltered.length;
  }, [backendConnected, apiDestinations, totalCount, mockFiltered.length]);

  // Total available pages
  const activeTotalPages = useMemo(() => {
    if (backendConnected && apiDestinations !== null) {
      return totalPages;
    }
    return Math.max(1, Math.ceil(mockFiltered.length / pageSize));
  }, [backendConnected, apiDestinations, totalPages, mockFiltered.length, pageSize]);

  const hasActiveFilters =
    selectedRegion !== 'All' || selectedStyle !== 'All' || selectedBudget !== 'All' || searchQuery.trim() !== '';

  const handleResetFilters = () => {
    setSelectedRegion('All');
    setSelectedStyle('All');
    setSelectedBudget('All');
    setSearchQuery('');
    setCurrentPage(0);
  };

  const handleToggleSave = (id: string) => {
    setSavedDestinationIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col font-sans selection:bg-brand-900 selection:text-white">
      {/* Editorial Navigation */}
      <Navbar
        activeTab={activeTab}
        onTabChange={setActiveTab}
        searchQuery={searchQuery}
        onSearchChange={setSearchQuery}
        backendConnected={backendConnected}
      />

      {/* Main Container */}
      <main className="flex-1 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 w-full">
        {/* DESTINATIONS TAB (CORE DISCOVERY PROTOTYPE) */}
        {activeTab === 'destinations' && (
          <div>
            {/* Editorial Showcase Hero */}
            <HeroSection
              onFeaturedClick={() => {
                const jaipur = MOCK_DESTINATIONS.find((d) => d.id === 'jaipur-rajasthan');
                if (jaipur) setSelectedDestination(jaipur);
              }}
            />

            {/* Filter Bar */}
            <FilterBar
              selectedRegion={selectedRegion}
              onSelectRegion={(reg) => setSelectedRegion(reg)}
              selectedStyle={selectedStyle}
              onSelectStyle={(style) => setSelectedStyle(style)}
              selectedBudget={selectedBudget}
              onSelectBudget={(budget) => setSelectedBudget(budget)}
              onResetFilters={handleResetFilters}
              hasActiveFilters={hasActiveFilters}
              totalResultsCount={activeTotalCount}
            />

            {/* Destination Grid / Loading / Error / Empty State */}
            {isLoading ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {[1, 2, 3, 4, 5, 6].map((i) => (
                  <SkeletonCard key={i} />
                ))}
              </div>
            ) : fetchError && activeDestinations.length === 0 ? (
              <div className="p-10 text-center bg-white rounded-3xl border border-red-200/80 shadow-xs">
                <p className="text-base font-semibold text-slate-800">Unable to load destinations</p>
                <p className="text-xs text-slate-500 mt-1 max-w-md mx-auto">{fetchError}</p>
                <Button
                  variant="primary"
                  size="sm"
                  className="mt-4"
                  onClick={() => setCurrentPage(0)}
                >
                  Retry
                </Button>
              </div>
            ) : activeDestinations.length > 0 ? (
              <>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {activeDestinations.map((dest) => (
                    <DestinationCard
                      key={dest.id}
                      destination={dest}
                      onSelect={(d) => setSelectedDestination(d)}
                    />
                  ))}
                </div>

                {/* Pagination Controls */}
                {activeTotalPages > 1 && (
                  <div className="flex flex-col sm:flex-row items-center justify-between gap-4 pt-8 border-t border-slate-200/80 mt-10">
                    <p className="text-xs text-slate-600 font-medium">
                      Showing{' '}
                      <span className="font-semibold text-slate-900">
                        {currentPage * pageSize + 1}
                      </span>
                      -
                      <span className="font-semibold text-slate-900">
                        {Math.min((currentPage + 1) * pageSize, activeTotalCount)}
                      </span>{' '}
                      of{' '}
                      <span className="font-semibold text-slate-900">{activeTotalCount}</span> destinations
                    </p>

                    <div className="flex items-center gap-2">
                      <Button
                        variant="secondary"
                        size="sm"
                        disabled={currentPage === 0}
                        onClick={() => setCurrentPage((p) => Math.max(0, p - 1))}
                        className="disabled:opacity-40 disabled:cursor-not-allowed text-xs"
                      >
                        Previous
                      </Button>
                      <span className="text-xs font-semibold text-slate-700 px-3 py-1 bg-white rounded-lg border border-slate-200 shadow-2xs">
                        Page {currentPage + 1} of {activeTotalPages}
                      </span>
                      <Button
                        variant="secondary"
                        size="sm"
                        disabled={currentPage >= activeTotalPages - 1}
                        onClick={() => setCurrentPage((p) => Math.min(activeTotalPages - 1, p + 1))}
                        className="disabled:opacity-40 disabled:cursor-not-allowed text-xs"
                      >
                        Next
                      </Button>
                    </div>
                  </div>
                )}
              </>
            ) : (
              <EmptyState onReset={handleResetFilters} />
            )}
          </div>
        )}

        {/* STAYS TAB (CURATED ACCOMMODATION VIEW) */}
        {activeTab === 'stays' && (
          <div className="space-y-6">
            <div className="bg-white rounded-3xl p-8 border border-slate-200/80 shadow-xs">
              <div className="flex items-center gap-2 mb-2 text-brand-900 font-semibold text-xs uppercase tracking-wider">
                <Compass className="w-4 h-4" />
                <span>Architectural & Heritage Living</span>
              </div>
              <h2 className="text-3xl font-bold font-display text-slate-900 tracking-tight mb-3">
                Curated Stays & Boutique Havelis
              </h2>
              <p className="text-slate-600 max-w-2xl text-sm leading-relaxed mb-4">
                Explore hand-vetted royal havelis, plantation eco-villas, and boutique coastal retreats. Each stay is selected for cultural provenance, transparent pricing, and exceptional guest hospitality.
              </p>
              <div className="p-3 rounded-xl bg-slate-50 border border-slate-200 text-xs text-slate-600 flex items-center gap-2 mb-8">
                <Info className="w-4 h-4 text-brand-700 shrink-0" />
                <span>
                  <strong className="text-slate-900 font-semibold">Illustrative Benchmark Rates:</strong> Stay prices per night represent seasonal historical averages for early planning.
                </span>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {MOCK_DESTINATIONS.flatMap((d) =>
                  d.stays.map((stay) => ({ ...stay, destinationName: d.name, destinationState: d.state, destObj: d }))
                ).map((stay) => (
                  <div
                    key={stay.id}
                    className="flex flex-col sm:flex-row gap-4 p-4 rounded-2xl border border-slate-200 bg-white shadow-xs hover:border-slate-300 transition-colors"
                  >
                    <div className="w-full sm:w-44 h-36 rounded-xl overflow-hidden shrink-0 bg-slate-900">
                      <ImageWithFallback
                        src={stay.imageUrl}
                        alt={stay.name}
                        fallbackTitle={stay.name}
                        fallbackSubtitle={stay.type}
                        className="w-full h-full object-cover"
                      />
                    </div>
                    <div className="flex-1 flex flex-col justify-between">
                      <div>
                        <div className="flex items-center justify-between text-xs mb-1">
                          <span className="text-[10px] font-bold uppercase tracking-wider text-brand-800 bg-brand-50 px-2 py-0.5 rounded-md border border-brand-200/50">
                            {stay.type}
                          </span>
                          <span className="text-slate-600 font-medium">
                            {stay.destinationName}, {stay.destinationState}
                          </span>
                        </div>
                        <h3 className="text-base font-bold font-display text-slate-900">
                          {stay.name}
                        </h3>
                        <div className="flex flex-wrap gap-1 mt-2">
                          {stay.highlights.map((h, idx) => (
                            <span key={idx} className="text-[10px] text-slate-600 bg-slate-100 px-2 py-0.5 rounded-md">
                              {h}
                            </span>
                          ))}
                        </div>
                      </div>
                      <div className="mt-3 pt-2 border-t border-slate-100 flex items-center justify-between">
                        <span className="text-xs font-bold text-slate-900 font-sans">
                          ₹{stay.pricePerNight.toLocaleString()}{' '}
                          <span className="text-[11px] font-normal text-slate-600">/ night</span>
                        </span>
                        <Button
                          variant="secondary"
                          size="sm"
                          onClick={() => setSelectedDestination(stay.destObj)}
                        >
                          View Destination
                        </Button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* DINING TAB (SIGNATURE CULINARY TRAILS) */}
        {activeTab === 'dining' && (
          <div className="space-y-6">
            <div className="bg-white rounded-3xl p-8 border border-slate-200/80 shadow-xs">
              <div className="flex items-center gap-2 mb-2 text-terracotta-700 font-semibold text-xs uppercase tracking-wider">
                <Sparkles className="w-4 h-4" />
                <span>Culinary Heritage & Signature Trails</span>
              </div>
              <h2 className="text-3xl font-bold font-display text-slate-900 tracking-tight mb-3">
                Regional Dining & Epicurean Specialties
              </h2>
              <p className="text-slate-600 max-w-2xl text-sm leading-relaxed mb-8">
                From Banarasi street-side chaat alleys to Royal Rajputana courtly banquets and Goan Saraswat fish thalis—savor authentic Indian food with honest price guides.
              </p>

              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
                {MOCK_DESTINATIONS.flatMap((d) =>
                  d.dining.map((dine) => ({ ...dine, destinationName: d.name, destinationState: d.state, destObj: d }))
                ).map((dine) => (
                  <div
                    key={dine.id}
                    className="p-5 rounded-2xl border border-slate-200 bg-white shadow-xs flex flex-col justify-between"
                  >
                    <div>
                      <div className="flex items-center justify-between text-xs mb-2">
                        <span className="text-[10px] font-bold text-terracotta-800 bg-terracotta-50 px-2 py-0.5 rounded-md border border-terracotta-200/50">
                          {dine.cuisine}
                        </span>
                        <span className="font-bold text-slate-700">{dine.priceRange}</span>
                      </div>
                      <h3 className="text-base font-bold font-display text-slate-900 mb-1">
                        {dine.name}
                      </h3>
                      <p className="text-xs text-slate-600 mb-3">
                        <MapPin className="w-3 h-3 inline mr-1 text-slate-600" />
                        {dine.destinationName}, {dine.destinationState}
                      </p>
                      <div className="p-2.5 rounded-xl bg-slate-50 border border-slate-100 text-xs text-slate-700">
                        <span className="font-bold text-slate-900">Must Try: </span>
                        {dine.mustTry}
                      </div>
                    </div>
                    <div className="mt-4 pt-3 border-t border-slate-100 flex items-center justify-between">
                      <span className="text-xs text-slate-600 font-semibold">
                        ★ {dine.rating} Rating
                      </span>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setSelectedDestination(dine.destObj)}
                      >
                        Explore →
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* TRIP PLANNER TAB (PREVIEW STUDIO) */}
        {activeTab === 'planner' && (
          <div className="space-y-6">
            <div className="bg-white rounded-3xl p-8 border border-slate-200/80 shadow-xs">
              <div className="flex items-center gap-2 mb-2 text-brand-900 font-semibold text-xs uppercase tracking-wider">
                <Calendar className="w-4 h-4" />
                <span>Connected Itinerary Studio</span>
              </div>
              <h2 className="text-3xl font-bold font-display text-slate-900 tracking-tight mb-3">
                Seamless Itinerary & Budget Studio
              </h2>
              <p className="text-slate-600 max-w-2xl text-sm leading-relaxed mb-6">
                Your destination selections seamlessly feed into our day-by-day itinerary builder with transparent budget estimation, scheduled activities, and transit optimization.
              </p>

              {savedDestinationIds.size > 0 ? (
                <div className="space-y-4">
                  <h3 className="text-sm font-bold text-slate-900 uppercase tracking-wider">
                    Saved For Itinerary ({savedDestinationIds.size})
                  </h3>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    {Array.from(savedDestinationIds).map((id) => {
                      const dest = MOCK_DESTINATIONS.find((d) => d.id === id);
                      if (!dest) return null;
                      return (
                        <div
                          key={dest.id}
                          className="flex items-center justify-between p-4 rounded-2xl border border-slate-200 bg-white shadow-xs"
                        >
                          <div className="flex items-center gap-3">
                            <div className="w-12 h-12 rounded-xl overflow-hidden shrink-0 bg-slate-900">
                              <ImageWithFallback
                                src={dest.imageUrl}
                                alt={dest.name}
                                fallbackTitle={dest.name}
                                className="w-full h-full object-cover"
                              />
                            </div>
                            <div>
                              <h4 className="text-sm font-bold text-slate-900 font-display">
                                {dest.name}
                              </h4>
                              <p className="text-xs text-slate-600">
                                {dest.recommendedDurationDays} Days • Avg ₹{dest.budgetBreakdown.averageDaily.toLocaleString()}/day
                              </p>
                            </div>
                          </div>
                          <Button
                            variant="secondary"
                            size="sm"
                            onClick={() => setSelectedDestination(dest)}
                          >
                            Details
                          </Button>
                        </div>
                      );
                    })}
                  </div>
                </div>
              ) : (
                <div className="p-8 text-center bg-slate-50 rounded-2xl border border-slate-200">
                  <Heart className="w-8 h-8 text-slate-300 mx-auto mb-2" />
                  <p className="text-sm font-semibold text-slate-700">No destinations saved yet</p>
                  <p className="text-xs text-slate-600 mt-1 max-w-sm mx-auto">
                    Explore our curated destinations collection and click &quot;Save Destination&quot; to build your trip.
                  </p>
                  <Button
                    variant="primary"
                    size="sm"
                    className="mt-4"
                    onClick={() => setActiveTab('destinations')}
                  >
                    Explore Destinations
                  </Button>
                </div>
              )}
            </div>

            {/* AI Travel Assistant powered by Google Gemini 3.5 Flash-Lite */}
            <AiTravelAssistant apiBaseUrl={apiBaseUrl} />
          </div>
        )}
      </main>

      {/* Destination Detail Modal */}
      <DestinationDetailModal
        destination={selectedDestination}
        onClose={() => setSelectedDestination(null)}
        isSaved={selectedDestination ? savedDestinationIds.has(selectedDestination.id) : false}
        onToggleSave={handleToggleSave}
      />

      {/* Editorial Footer */}
      <footer className="border-t border-slate-200 bg-white py-10 mt-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-6 text-xs text-slate-600">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-xl bg-brand-900 text-white flex items-center justify-center">
              <Compass className="w-4 h-4" />
            </div>
            <div>
              <p className="font-bold text-slate-900 font-display text-sm">Voyage-AI</p>
              <p className="text-[11px] text-slate-600">
                Design Foundation & Curated Discovery Prototype (Phase 1)
              </p>
            </div>
          </div>

          <div className="flex flex-wrap items-center gap-4 text-[11px]">
            <span className="flex items-center gap-1">
              <Layers className="w-3.5 h-3.5 text-brand-700" />
              <span>Spring Boot 4.1.1 (Port 8081)</span>
            </span>
            <span>•</span>
            <span>MySQL 8.0 (Port 3307)</span>
            <span>•</span>
            <span>Emil Kowalski Design Tokens</span>
            <span>•</span>
            <span>WCAG 2.2 AA</span>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default App;
