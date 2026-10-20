import React from 'react';
import { Compass, Sparkles, MapPin, Bed, Utensils, CalendarDays, Search } from 'lucide-react';

export interface NavbarProps {
  activeTab: string;
  onTabChange: (tab: string) => void;
  searchQuery: string;
  onSearchChange: (query: string) => void;
  backendConnected: boolean;
}

export const Navbar: React.FC<NavbarProps> = ({
  activeTab,
  onTabChange,
  searchQuery,
  onSearchChange,
  backendConnected,
}) => {
  const navItems = [
    { id: 'destinations', label: 'Destinations', icon: <MapPin className="w-4 h-4" /> },
    { id: 'stays', label: 'Curated Stays', icon: <Bed className="w-4 h-4" /> },
    { id: 'dining', label: 'Signature Dining', icon: <Utensils className="w-4 h-4" /> },
    { id: 'planner', label: 'Itinerary Planner', icon: <CalendarDays className="w-4 h-4" /> },
  ];

  return (
    <header className="sticky top-0 z-40 bg-white/90 backdrop-blur-md border-b border-slate-200/80 transition-colors">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20 gap-4">
          {/* Logo & Brand Identity */}
          <div className="flex items-center gap-3 shrink-0">
            <div className="w-11 h-11 rounded-2xl bg-gradient-to-br from-brand-900 to-brand-700 text-white flex items-center justify-center shadow-md shadow-brand-900/10 border border-brand-950/20 transition-transform active:scale-95 duration-150">
              <Compass className="w-6 h-6 stroke-[1.8]" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="text-xl font-bold font-display tracking-tight text-slate-900">
                  Voyage-AI
                </span>
                <span className="text-[10px] font-bold tracking-wider uppercase px-2 py-0.5 rounded-full bg-terracotta-50 text-terracotta-700 border border-terracotta-200/70">
                  Discovery
                </span>
              </div>
              <p className="text-[11px] font-medium text-slate-600 hidden sm:block">
                Curated Subcontinent Journeys
              </p>
            </div>
          </div>

          {/* Navigation Items (Desktop) */}
          <nav className="hidden md:flex items-center gap-1 bg-slate-100/80 p-1.5 rounded-2xl border border-slate-200/60" aria-label="Main Navigation">
            {navItems.map((item) => {
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => onTabChange(item.id)}
                  className={`flex items-center gap-2 px-3.5 py-1.5 rounded-xl text-xs font-medium font-sans transition-[background-color,color,box-shadow,transform] duration-150 ease-[cubic-bezier(0.23,1,0.32,1)] active:scale-95 ${
                    isActive
                      ? 'bg-white text-slate-900 shadow-xs border border-slate-200/60 font-semibold'
                      : 'text-slate-600 hover:text-slate-900 hover:bg-white/50'
                  }`}
                >
                  <span className={isActive ? 'text-brand-900' : 'text-slate-600'}>
                    {item.icon}
                  </span>
                  <span>{item.label}</span>
                </button>
              );
            })}
          </nav>

          {/* Right Section: Search & System Status */}
          <div className="flex items-center gap-3">
            {/* Search Input Box */}
            <div className="relative w-44 sm:w-64">
              <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-600 pointer-events-none" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => onSearchChange(e.target.value)}
                placeholder="Search Jaipur, tea, fort..."
                aria-label="Search destinations, stays, or attractions"
                className="w-full pl-9 pr-3 py-1.5 text-xs bg-slate-100/70 hover:bg-slate-100 focus:bg-white border border-slate-200/80 rounded-xl text-slate-900 placeholder-slate-600 focus:outline-none focus:ring-2 focus:ring-brand-600/30 focus:border-brand-600 transition-colors"
              />
            </div>

            {/* Health Status Indicator */}
            <div
              className="flex items-center gap-2 px-2.5 py-1.5 rounded-xl bg-slate-100/70 border border-slate-200/60 shrink-0"
              title={backendConnected ? 'Backend REST API connected' : 'Connecting to backend...'}
            >
              <span className="relative flex h-2 w-2">
                <span
                  className={`animate-ping absolute inline-flex h-full w-full rounded-full opacity-75 ${
                    backendConnected ? 'bg-emerald-400' : 'bg-amber-400'
                  }`}
                />
                <span
                  className={`relative inline-flex rounded-full h-2 w-2 ${
                    backendConnected ? 'bg-emerald-500' : 'bg-amber-500'
                  }`}
                />
              </span>
              <span className="text-[11px] font-semibold text-slate-600 hidden lg:inline">
                {backendConnected ? 'v4.1.1 API Active' : 'Connecting...'}
              </span>
            </div>

            <button
              onClick={() => onTabChange('planner')}
              className="hidden sm:inline-flex items-center gap-1.5 px-3.5 py-1.5 text-xs font-medium rounded-xl bg-brand-900 text-white hover:bg-brand-800 transition-[transform,background-color] active:scale-95 duration-150 shadow-xs"
            >
              <Sparkles className="w-3.5 h-3.5 text-amber-300" />
              <span>Trip Studio</span>
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};
