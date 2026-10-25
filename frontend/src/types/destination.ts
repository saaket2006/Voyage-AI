export type Region = 
  | 'All' 
  | 'North India' 
  | 'South India' 
  | 'Western Ghats' 
  | 'Himalayas' 
  | 'Coastal & Islands';

export type TravelStyle = 
  | 'All'
  | 'Heritage & Culture' 
  | 'Nature & Wildlife' 
  | 'Wellness & Spiritual' 
  | 'Adventure & Trekking' 
  | 'Coastal & Relaxation';

export type BudgetTier = 'Budget' | 'Comfort' | 'Luxury';

export interface StaySpot {
  id: string;
  name: string;
  type: 'Heritage Haveli' | 'Eco Resort' | 'Boutique Stay' | 'Luxury Villa' | 'Homestay';
  pricePerNight: number;
  rating: number;
  imageUrl: string;
  highlights: string[];
}

export interface DiningSpot {
  id: string;
  name: string;
  cuisine: string;
  priceRange: '₹' | '₹₹' | '₹₹₹' | '₹₹₹₹';
  mustTry: string;
  rating: number;
}

export interface Attraction {
  id: string;
  name: string;
  category: 'Monument' | 'Nature' | 'Cultural' | 'Activity';
  estimatedHours: number;
  entryFee: number;
  description: string;
}

export interface BudgetBreakdown {
  currency: string;
  averageDaily: number;
  estimatedTotalForTwo: number;
  durationDays: number;
  tiers: {
    budget: number;
    comfort: number;
    luxury: number;
  };
}

export interface DestinationProvenance {
  provider: string;
  sourceUrl?: string;
  license?: string;
  retrievedAt?: string;
  imageAttributionText?: string;
  imageLicenseType?: string;
  imageLicenseUrl?: string;
  imageSourceUrl?: string;
  curationStatus?: string;
}

export interface Destination {
  id: string;
  name: string;
  state: string;
  country: string;
  region: Region;
  tagline: string;
  description: string;
  imageUrl: string;
  heroImageUrl: string;
  travelStyles: TravelStyle[];
  budgetTier: BudgetTier;
  recommendedDurationDays: number;
  bestTimeToVisit: string;
  averageRating: number;
  reviewCount: number;
  budgetBreakdown: BudgetBreakdown;
  stays: StaySpot[];
  dining: DiningSpot[];
  attractions: Attraction[];
  highlights: string[];
  weather: {
    tempC: number;
    condition: string;
  };
  provenance?: DestinationProvenance;
}
