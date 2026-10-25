import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { AiTravelAssistant } from './AiTravelAssistant';

describe('AiTravelAssistant Component', () => {
  const mockApiBaseUrl = 'http://localhost:8081/api/v1';

  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('renders correctly and loads Gemini health status', async () => {
    global.fetch = vi.fn().mockImplementation((url: string) => {
      if (url.endsWith('/ai/health')) {
        return Promise.resolve({
          ok: true,
          json: () =>
            Promise.resolve({
              status: 'OPERATIONAL',
              model: 'gemini-3.5-flash-lite',
              configured: true,
              reachable: true,
              message: 'Gemini API is reachable and responding successfully.',
            }),
        });
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    render(<AiTravelAssistant apiBaseUrl={mockApiBaseUrl} />);

    expect(screen.getByText('AI Travel Concierge')).toBeInTheDocument();
    expect(screen.getByText('Curated Travel Intelligence')).toBeInTheDocument();
    expect(screen.getAllByText('gemini-3.5-flash-lite').length).toBeGreaterThan(0);

    await waitFor(() => {
      expect(screen.getByText('OPERATIONAL')).toBeInTheDocument();
    });
  });

  it('validates empty prompt and shows notice', async () => {
    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: () => Promise.resolve({ status: 'OPERATIONAL', reachable: true }),
    });

    render(<AiTravelAssistant apiBaseUrl={mockApiBaseUrl} />);

    const submitBtn = screen.getByRole('button', { name: /Ask Gemini/i });
    expect(submitBtn).toBeDisabled();
  });

  it('submits prompt and renders generated Gemini response', async () => {
    global.fetch = vi.fn().mockImplementation((url: string, options?: RequestInit) => {
      if (url.endsWith('/ai/health')) {
        return Promise.resolve({
          ok: true,
          json: () =>
            Promise.resolve({
              status: 'OPERATIONAL',
              model: 'gemini-3.5-flash-lite',
              configured: true,
              reachable: true,
              message: 'Gemini API reachable',
            }),
        });
      }
      if (url.endsWith('/ai/generate') && options?.method === 'POST') {
        return Promise.resolve({
          ok: true,
          json: () =>
            Promise.resolve({
              success: true,
              model: 'gemini-3.5-flash-lite',
              response: 'Jaipur 3-day itinerary: Day 1 Amer Fort & Jal Mahal. Day 2 City Palace & Hawa Mahal. Day 3 Nahargarh.',
              promptTokens: 25,
              candidateTokens: 35,
              totalTokens: 60,
            }),
        });
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    render(<AiTravelAssistant apiBaseUrl={mockApiBaseUrl} />);

    const textarea = screen.getByPlaceholderText(/E.g., Suggest a 2-day walking tour/i);
    fireEvent.change(textarea, { target: { value: 'Jaipur 3-day itinerary' } });

    const submitBtn = screen.getByRole('button', { name: /Ask Gemini/i });
    expect(submitBtn).not.toBeDisabled();
    fireEvent.click(submitBtn);

    await waitFor(() => {
      expect(
        screen.getByText(/Jaipur 3-day itinerary: Day 1 Amer Fort & Jal Mahal/i)
      ).toBeInTheDocument();
    });

    expect(screen.getByText('60 tokens')).toBeInTheDocument();
  });

  it('handles backend error response safely', async () => {
    global.fetch = vi.fn().mockImplementation((url: string, _options?: RequestInit) => {
      if (url.endsWith('/ai/health')) {
        return Promise.resolve({
          ok: true,
          json: () =>
            Promise.resolve({
              status: 'OPERATIONAL',
              model: 'gemini-3.5-flash-lite',
              configured: true,
              reachable: true,
              message: 'Gemini API reachable',
            }),
        });
      }
      if (url.endsWith('/ai/generate')) {
        return Promise.resolve({
          ok: false,
          status: 429,
          json: () =>
            Promise.resolve({
              success: false,
              code: 'GEMINI_RATE_LIMITED',
              message: 'Gemini rate limit or quota exceeded. Please try again shortly.',
            }),
        });
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    render(<AiTravelAssistant apiBaseUrl={mockApiBaseUrl} />);

    const textarea = screen.getByPlaceholderText(/E.g., Suggest a 2-day walking tour/i);
    fireEvent.change(textarea, { target: { value: 'Trigger rate limit' } });

    const submitBtn = screen.getByRole('button', { name: /Ask Gemini/i });
    fireEvent.click(submitBtn);

    await waitFor(() => {
      expect(
        screen.getByText(/Gemini rate limit or quota exceeded/i)
      ).toBeInTheDocument();
    });
  });
});
