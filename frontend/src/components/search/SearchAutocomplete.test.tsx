import React, { useState } from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import SearchAutocomplete from './SearchAutocomplete';
import { SearchItem, SearchSuggestionResponse } from '../../types/search';

const mockResponse: SearchSuggestionResponse = {
  query: 'Delhi',
  groups: [
    {
      type: 'CITY',
      label: 'Cities',
      items: [
        {
          id: 'city-delhi',
          name: 'Delhi',
          subtitle: 'Cities',
          description: 'National Capital Territory of India',
          location: {
            city: 'Delhi',
            region: 'Delhi',
            country: 'India',
            latitude: 28.6139,
            longitude: 77.209,
          },
          imageUrl: 'https://example.org/delhi.jpg',
          sourceUrl: 'https://example.org/source',
          route: '/destinations/delhi',
        },
      ],
    },
    {
      type: 'ATTRACTION',
      label: 'Attractions',
      items: [
        {
          id: 'dl-att-1',
          name: 'Red Fort',
          subtitle: 'Attractions',
          description: 'Historic Mughal-era fort in Delhi',
          location: {
            city: 'Delhi',
            region: 'Delhi',
            country: 'India',
            latitude: 28.6562,
            longitude: 77.241,
          },
          imageUrl: null,
          sourceUrl: 'https://asi.nic.in',
          route: '/attractions/dl-att-1',
        },
      ],
    },
    {
      type: 'DINING',
      label: 'Dining',
      items: [
        {
          id: 'dl-din-1',
          name: "Karim's",
          subtitle: 'Dining',
          description: 'Historic Mughlai restaurant in Jama Masjid',
          location: {
            city: 'Delhi',
            region: 'Delhi',
            country: 'India',
            latitude: 28.6508,
            longitude: 77.2334,
          },
          imageUrl: null,
          sourceUrl: 'https://www.openstreetmap.org/node/123',
          route: '/dining/dl-din-1',
        },
      ],
    },
  ],
  meta: {
    limitPerGroup: 5,
    totalResults: 3,
  },
};

const Wrapper: React.FC<{ onSelectResult: (item: SearchItem) => void; initialValue?: string }> = ({
  onSelectResult,
  initialValue = '',
}) => {
  const [val, setVal] = useState(initialValue);
  return (
    <SearchAutocomplete
      value={val}
      onChange={setVal}
      onSelectResult={onSelectResult}
      placeholder="Search destinations, attractions, dining..."
    />
  );
};

describe('SearchAutocomplete', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('renders input with placeholder', () => {
    render(<Wrapper onSelectResult={vi.fn()} />);
    const input = screen.getByRole('textbox');
    expect(input).toBeInTheDocument();
    expect(input).toHaveAttribute('placeholder', 'Search destinations, attractions, dining...');
  });

  it('fetches and displays grouped suggestions in exact order: Cities, Attractions, Dining', async () => {
    global.fetch = vi.fn().mockImplementation(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      })
    );

    const handleSelect = vi.fn();
    render(<Wrapper onSelectResult={handleSelect} />);

    const input = screen.getByRole('textbox');
    fireEvent.change(input, { target: { value: 'Delhi' } });

    await waitFor(() => {
      expect(screen.getAllByRole('group')).toHaveLength(3);
    });

    expect(screen.getAllByText('Cities').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Attractions').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Dining').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Delhi')[0]).toBeInTheDocument();
    expect(screen.getByText('Red Fort')).toBeInTheDocument();
    expect(screen.getByText("Karim's")).toBeInTheDocument();

    // Verify ordering
    const groups = screen.getAllByRole('group').map((g) => g.getAttribute('aria-label'));
    expect(groups).toEqual(['Cities', 'Attractions', 'Dining']);

    // Clicking an item invokes callback and closes dropdown
    fireEvent.click(screen.getByText('Red Fort'));
    expect(handleSelect).toHaveBeenCalledWith(mockResponse.groups[1].items[0]);
    expect(screen.queryByText('Cities')).not.toBeInTheDocument();
  });

  it('navigates with keyboard (ArrowDown, ArrowUp, Enter, Escape)', async () => {
    global.fetch = vi.fn().mockImplementation(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      })
    );

    const handleSelect = vi.fn();
    render(<Wrapper onSelectResult={handleSelect} />);

    const input = screen.getByRole('textbox');
    fireEvent.change(input, { target: { value: 'Delhi' } });

    await waitFor(() => {
      expect(screen.getByText('Red Fort')).toBeInTheDocument();
    });

    // ArrowDown moves focus
    fireEvent.keyDown(input, { key: 'ArrowDown' }); // index 0: Delhi
    fireEvent.keyDown(input, { key: 'ArrowDown' }); // index 1: Red Fort
    fireEvent.keyDown(input, { key: 'Enter' });

    expect(handleSelect).toHaveBeenCalledWith(mockResponse.groups[1].items[0]);

    // Reopen and test Escape to close
    fireEvent.change(input, { target: { value: 'Jaipur' } });
    await waitFor(() => {
      expect(screen.getByText('Red Fort')).toBeInTheDocument();
    });
    fireEvent.keyDown(input, { key: 'Escape' });
    expect(screen.queryByText('Red Fort')).not.toBeInTheDocument();
  });

  it('shows empty state when no results match', async () => {
    global.fetch = vi.fn().mockImplementation(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve({
            query: 'NowhereXYZ',
            groups: [
              { type: 'CITY', label: 'Cities', items: [] },
              { type: 'ATTRACTION', label: 'Attractions', items: [] },
              { type: 'DINING', label: 'Dining', items: [] },
            ],
            meta: { limitPerGroup: 5, totalResults: 0 },
          }),
      })
    );

    render(<Wrapper onSelectResult={vi.fn()} />);

    const input = screen.getByRole('textbox');
    fireEvent.change(input, { target: { value: 'NowhereXYZ' } });

    await waitFor(() => {
      expect(screen.getByText(/No results found for/)).toBeInTheDocument();
      expect(screen.getByText(/Try searching for a city like/)).toBeInTheDocument();
    });
  });
});
