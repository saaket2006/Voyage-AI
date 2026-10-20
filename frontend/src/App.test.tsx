import { render, screen, waitFor, fireEvent, within } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import App from './App';

describe('Voyage-AI Phase 1 Discovery Prototype', () => {
  beforeEach(() => {
    // Mock global fetch for health check
    global.fetch = vi.fn().mockImplementation(() =>
      Promise.resolve({
        ok: true,
        json: () =>
          Promise.resolve({
            success: true,
            data: {
              status: 'UP',
              service: 'voyage-ai-backend',
              profile: 'dev',
              version: '0.0.1-SNAPSHOT',
            },
          }),
      })
    );
  });

  it('renders the navigation, hero section, and curated destinations', async () => {
    render(<App />);

    expect(screen.getAllByText('Voyage-AI')[0]).toBeInTheDocument();
    expect(
      screen.getByText('Craft Your Journey Across the Subcontinent')
    ).toBeInTheDocument();
    expect(screen.getByText('Jaipur')).toBeInTheDocument();
    expect(screen.getByText('Munnar')).toBeInTheDocument();
    expect(screen.getByText('Varanasi')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('v4.1.1 API Active')).toBeInTheDocument();
    });
  });

  it('filters destinations by search query', async () => {
    render(<App />);

    const searchInput = screen.getByPlaceholderText(/Search Jaipur, tea, fort.../i);
    fireEvent.change(searchInput, { target: { value: 'Munnar' } });

    expect(screen.getByText('Munnar')).toBeInTheDocument();
    expect(screen.queryByText('Varanasi')).not.toBeInTheDocument();
  });

  it('opens and closes destination detail modal with accessible controls', async () => {
    render(<App />);

    // Click on "Explore Plan" for Jaipur
    const exploreButtons = screen.getAllByText('Explore Plan');
    fireEvent.click(exploreButtons[0]);

    // Modal should be visible
    const modal = screen.getByRole('dialog');
    expect(modal).toBeInTheDocument();
    expect(within(modal).getByText('The Pink City of Royal Fortresses & Timeless Artistry')).toBeInTheDocument();
    expect(within(modal).getByText('Signature Experiences')).toBeInTheDocument();

    // Close button
    const closeBtn = screen.getByLabelText('Close modal');
    fireEvent.click(closeBtn);

    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });
});
