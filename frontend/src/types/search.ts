export interface SearchItemLocation {
  city?: string;
  region?: string;
  country?: string;
  latitude?: number;
  longitude?: number;
}

export interface SearchItem {
  id: string;
  name: string;
  subtitle: string;
  description?: string;
  location?: SearchItemLocation;
  imageUrl?: string | null;
  sourceUrl?: string | null;
  route: string;
}

export interface SearchGroup {
  type: 'CITY' | 'ATTRACTION' | 'DINING';
  label: string;
  items: SearchItem[];
}

export interface SearchSuggestionResponse {
  query: string;
  groups: SearchGroup[];
  meta: {
    limitPerGroup: number;
    totalResults: number;
  };
}
