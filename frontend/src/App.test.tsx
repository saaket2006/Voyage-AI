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

  it('renders the navigation, hero section, and curated destinations with transparency labels', async () => {
    render(<App />);

    expect(screen.getAllByText('Voyage-AI')[0]).toBeInTheDocument();
    expect(
      screen.getByText('Craft Your Journey Across the Subcontinent')
    ).toBeInTheDocument();
    expect(screen.getByText('Jaipur')).toBeInTheDocument();
    expect(screen.getByText('Munnar')).toBeInTheDocument();
    expect(screen.getByText('Varanasi')).toBeInTheDocument();
    expect(screen.getByText('Hampi')).toBeInTheDocument();

    // Verify illustrative estimate labels
    expect(screen.getAllByText('Illustrative Est.').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Curated').length).toBeGreaterThan(0);

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

  it('opens destination detail modal and closes via close button', async () => {
    render(<App />);

    const exploreButtons = screen.getAllByText('Explore Plan');
    fireEvent.click(exploreButtons[0]);

    const modal = screen.getByRole('dialog');
    expect(modal).toBeInTheDocument();
    expect(within(modal).getByText('The Pink City of Royal Fortresses & Timeless Artistry')).toBeInTheDocument();
    expect(within(modal).getByText('Signature Experiences')).toBeInTheDocument();

    const closeBtn = screen.getByLabelText('Close modal');
    fireEvent.click(closeBtn);

    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('dismisses modal on Escape key press and restores focus', async () => {
    render(<App />);

    const exploreButton = screen.getAllByRole('button', { name: /explore details for/i })[0];
    exploreButton.focus();
    expect(document.activeElement).toBe(exploreButton);

    fireEvent.click(exploreButton);

    const modal = screen.getByRole('dialog');
    expect(modal).toBeInTheDocument();

    // Fire Escape key
    fireEvent.keyDown(window, { key: 'Escape', code: 'Escape' });

    await waitFor(() => {
      expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });

    // Verify focus restoration
    expect(document.activeElement).toBe(exploreButton);
  });
});
