import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Search, MapPin, Landmark, Utensils, Loader2, X, AlertCircle } from 'lucide-react';
import { SearchItem, SearchSuggestionResponse } from '../../types/search';
import { ImageWithFallback } from '../common/ImageWithFallback';

export interface SearchAutocompleteProps {
  value: string;
  onChange: (value: string) => void;
  onSelectResult: (item: SearchItem) => void;
  apiBaseUrl?: string;
  placeholder?: string;
  className?: string;
}

export const SearchAutocomplete: React.FC<SearchAutocompleteProps> = ({
  value,
  onChange,
  onSelectResult,
  apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api/v1',
  placeholder = 'Search Jaipur, tea, fort...',
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [suggestions, setSuggestions] = useState<SearchSuggestionResponse | null>(null);
  const [activeIndex, setActiveIndex] = useState<number>(-1);
  const [error, setError] = useState<string | null>(null);

  const containerRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const abortControllerRef = useRef<AbortController | null>(null);

  // Flattened items for easy keyboard index traversal
  const allItems: SearchItem[] = React.useMemo(() => {
    if (!suggestions || !suggestions.groups) return [];
    return suggestions.groups.flatMap((g) => g.items || []);
  }, [suggestions]);

  // Debounced search effect
  useEffect(() => {
    const trimmed = value.trim();
    if (!trimmed) {
      setSuggestions(null);
      setIsLoading(false);
      setError(null);
      setIsOpen(false);
      return;
    }

    setIsLoading(true);
    setError(null);

    const timer = setTimeout(async () => {
      // Abort any in-flight request
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }
      const controller = new AbortController();
      abortControllerRef.current = controller;

      try {
        const res = await fetch(
          `${apiBaseUrl}/search/suggestions?q=${encodeURIComponent(trimmed)}&limit=5`,
          { signal: controller.signal }
        );

        if (!res.ok) {
          const errData = await res.json().catch(() => null);
          throw new Error(errData?.message || `Search failed (${res.status})`);
        }

        const data: SearchSuggestionResponse = await res.json();
        setSuggestions(data);
        setIsOpen(true);
        setActiveIndex(-1);
      } catch (err: any) {
        if (err.name !== 'AbortError') {
          console.warn('Autocomplete search failed:', err);
          setError(err.message || 'Unable to load suggestions');
          setSuggestions(null);
          setIsOpen(true);
        }
      } finally {
        setIsLoading(false);
      }
    }, 280);

    return () => {
      clearTimeout(timer);
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }
    };
  }, [value, apiBaseUrl]);

  // Click outside listener to close dropdown
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleSelect = useCallback(
    (item: SearchItem) => {
      onSelectResult(item);
      setIsOpen(false);
      setActiveIndex(-1);
    },
    [onSelectResult]
  );

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (!isOpen) {
      if (e.key === 'ArrowDown' && allItems.length > 0) {
        setIsOpen(true);
        setActiveIndex(0);
        e.preventDefault();
      }
      return;
    }

    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault();
        setActiveIndex((prev) => (prev < allItems.length - 1 ? prev + 1 : 0));
        break;
      case 'ArrowUp':
        e.preventDefault();
        setActiveIndex((prev) => (prev > 0 ? prev - 1 : allItems.length - 1));
        break;
      case 'Enter':
        e.preventDefault();
        if (activeIndex >= 0 && activeIndex < allItems.length) {
          handleSelect(allItems[activeIndex]);
        }
        break;
      case 'Escape':
        e.preventDefault();
        setIsOpen(false);
        setActiveIndex(-1);
        inputRef.current?.blur();
        break;
    }
  };

  const getGroupIcon = (type: string) => {
    switch (type) {
      case 'CITY':
        return <MapPin className="w-3.5 h-3.5 text-brand-700" />;
      case 'ATTRACTION':
        return <Landmark className="w-3.5 h-3.5 text-amber-600" />;
      case 'DINING':
        return <Utensils className="w-3.5 h-3.5 text-terracotta-600" />;
      default:
        return <Search className="w-3.5 h-3.5 text-slate-400" />;
    }
  };

  // Compute absolute item index across groups
  let runningIndex = 0;

  return (
    <div ref={containerRef} className={`relative ${className}`}>
      {/* Search Input Container */}
      <div className="relative flex items-center w-full">
        <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-500 pointer-events-none" />
        <input
          ref={inputRef}
          type="text"
          aria-expanded={isOpen}
          aria-autocomplete="list"
          aria-controls="autocomplete-dropdown"
          aria-activedescendant={
            activeIndex >= 0 && allItems[activeIndex] ? `suggestion-${allItems[activeIndex].id}` : undefined
          }
          value={value}
          onChange={(e) => onChange(e.target.value)}
          onFocus={() => {
            if (value.trim() && (suggestions || error)) {
              setIsOpen(true);
            }
          }}
          onKeyDown={handleKeyDown}
          placeholder={placeholder}
          aria-label="Search destinations, attractions, or dining"
          className="w-full pl-9 pr-8 py-1.5 text-xs bg-slate-100/70 hover:bg-slate-100 focus:bg-white border border-slate-200/80 rounded-xl text-slate-900 placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-brand-600/30 focus:border-brand-600 transition-colors shadow-2xs"
        />

        {/* Loading Spinner or Clear Button */}
        <div className="absolute right-2.5 top-1/2 -translate-y-1/2 flex items-center">
          {isLoading ? (
            <Loader2 className="w-3.5 h-3.5 text-brand-600 animate-spin" aria-label="Loading search results" />
          ) : value ? (
            <button
              type="button"
              onClick={() => {
                onChange('');
                setSuggestions(null);
                setIsOpen(false);
                inputRef.current?.focus();
              }}
              className="text-slate-400 hover:text-slate-600 transition-colors p-0.5 rounded-full hover:bg-slate-200/50"
              aria-label="Clear search input"
            >
              <X className="w-3.5 h-3.5" />
            </button>
          ) : null}
        </div>
      </div>

      {/* Autocomplete Dropdown Menu */}
      {isOpen && (
        <div
          id="autocomplete-dropdown"
          role="listbox"
          aria-label="Search suggestions"
          className="fixed left-4 right-4 top-[84px] sm:absolute sm:top-full sm:left-0 sm:right-auto sm:mt-2 sm:w-96 md:w-[420px] max-h-[480px] overflow-y-auto bg-white/95 backdrop-blur-md rounded-2xl border border-slate-200/90 shadow-2xl z-50 p-2 divide-y divide-slate-100 animate-in fade-in-0 zoom-in-95 duration-150"
        >
          {error ? (
            <div className="p-4 text-center">
              <AlertCircle className="w-5 h-5 text-red-500 mx-auto mb-1.5" />
              <p className="text-xs font-semibold text-slate-800">Search Unavailable</p>
              <p className="text-[11px] text-slate-500 mt-0.5">{error}</p>
            </div>
          ) : suggestions?.meta.totalResults === 0 ? (
            <div className="p-6 text-center">
              <div className="w-9 h-9 rounded-full bg-slate-100 flex items-center justify-center mx-auto mb-2 text-slate-400">
                <Search className="w-4 h-4" />
              </div>
              <p className="text-xs font-semibold text-slate-800">No results found for &ldquo;{suggestions.query}&rdquo;</p>
              <p className="text-[11px] text-slate-500 mt-1 max-w-xs mx-auto">
                Try searching for a city like <span className="font-medium text-slate-700">Delhi</span> or <span className="font-medium text-slate-700">Jaipur</span>, or an attraction like <span className="font-medium text-slate-700">Amber Fort</span>.
              </p>
            </div>
          ) : (
            suggestions?.groups.map((group) => {
              if (group.items.length === 0) return null;

              return (
                <div key={group.type} className="py-2 first:pt-1 last:pb-1" role="group" aria-label={group.label}>
                  {/* Category Group Header */}
                  <div className="flex items-center gap-1.5 px-3 py-1 mb-1 text-[11px] font-bold uppercase tracking-wider text-slate-500">
                    {getGroupIcon(group.type)}
                    <span>{group.label}</span>
                    <span className="text-[10px] font-normal text-slate-400 ml-auto">
                      {group.items.length} {group.items.length === 1 ? 'match' : 'matches'}
                    </span>
                  </div>

                  {/* Group Items */}
                  <div className="space-y-1">
                    {group.items.map((item) => {
                      const itemIndex = runningIndex++;
                      const isSelected = activeIndex === itemIndex;

                      return (
                        <div
                          key={item.id}
                          id={`suggestion-${item.id}`}
                          role="option"
                          aria-selected={isSelected}
                          onClick={() => handleSelect(item)}
                          onMouseEnter={() => setActiveIndex(itemIndex)}
                          className={`flex items-center gap-3 p-2.5 rounded-xl cursor-pointer transition-colors duration-150 ${
                            isSelected
                              ? 'bg-brand-50/80 border border-brand-200/60 shadow-xs'
                              : 'hover:bg-slate-50 border border-transparent'
                          }`}
                        >
                          {/* Item Thumbnail / Icon */}
                          <div className="w-10 h-10 rounded-lg overflow-hidden bg-slate-100 shrink-0 border border-slate-200/60 flex items-center justify-center">
                            {item.imageUrl ? (
                              <ImageWithFallback
                                src={item.imageUrl}
                                alt={item.name}
                                fallbackTitle={item.name}
                                className="w-full h-full object-cover"
                              />
                            ) : (
                              <div className="text-slate-400">{getGroupIcon(group.type)}</div>
                            )}
                          </div>

                          {/* Item Metadata */}
                          <div className="flex-1 min-w-0">
                            <div className="flex items-center justify-between gap-2">
                              <h4 className="text-xs font-semibold text-slate-900 truncate">{item.name}</h4>
                              <span
                                className={`text-[10px] font-medium px-1.5 py-0.5 rounded-md shrink-0 ${
                                  group.type === 'CITY'
                                    ? 'bg-brand-100/70 text-brand-800'
                                    : group.type === 'ATTRACTION'
                                    ? 'bg-amber-100/70 text-amber-800'
                                    : 'bg-terracotta-100/70 text-terracotta-800'
                                }`}
                              >
                                {item.subtitle || group.label}
                              </span>
                            </div>

                            {/* Location Details & Brief Description */}
                            <div className="flex items-center gap-1.5 text-[11px] text-slate-500 mt-0.5">
                              {item.location && (
                                <span className="flex items-center gap-1 truncate">
                                  <MapPin className="w-2.5 h-2.5 text-slate-400 shrink-0" />
                                  <span>
                                    {item.location.city}
                                    {item.location.region && item.location.region !== item.location.city
                                      ? `, ${item.location.region}`
                                      : ''}
                                  </span>
                                </span>
                              )}
                              {item.description && (
                                <>
                                  <span className="text-slate-300">•</span>
                                  <span className="truncate max-w-[170px] text-slate-500 text-[10px]">
                                    {item.description}
                                  </span>
                                </>
                              )}
                            </div>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              );
            })
          )}
        </div>
      )}
    </div>
  );
};

export default SearchAutocomplete;
