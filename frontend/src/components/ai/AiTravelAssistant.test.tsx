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

    const submitBtn = screen.getByRole('button', { name: /Ask AI/i });
    expect(submitBtn).toBeDisabled();
  });

  it('submits prompt, shows Thinking… state, and renders formatted Markdown AI response', async () => {
    let resolveGenerate: (val: any) => void;
    const generatePromise = new Promise((resolve) => {
      resolveGenerate = resolve;
    });

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
              message: 'AI service reachable',
            }),
        });
      }
      if (url.endsWith('/ai/generate') && options?.method === 'POST') {
        return generatePromise;
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    render(<AiTravelAssistant apiBaseUrl={mockApiBaseUrl} />);

    const textarea = screen.getByPlaceholderText(/E.g., Suggest a 2-day walking tour/i);
    fireEvent.change(textarea, { target: { value: 'Jaipur 3-day itinerary' } });

    const submitBtn = screen.getByRole('button', { name: /Ask AI/i });
    expect(submitBtn).not.toBeDisabled();
    fireEvent.click(submitBtn);

    // Verify loading/thinking state text is 'Thinking…' without Gemini branding
    expect(screen.getByText('Thinking…')).toBeInTheDocument();
    expect(screen.queryByText(/Thinking with Gemini/i)).not.toBeInTheDocument();

    // Resolve the generate call with rich Markdown content
    resolveGenerate!({
      ok: true,
      json: () =>
        Promise.resolve({
          success: true,
          model: 'gemini-3.5-flash-lite',
          response:
            '### Jaipur Highlights\n\n' +
            'Here is your **curated** itinerary:\n\n' +
            '* Visit the iconic **Amer Fort**\n' +
            '* Walk through *Jal Mahal*\n\n' +
            'Check the [Official Tourism Portal](https://tourism.rajasthan.gov.in) for entry passes.\n\n' +
            '| Day | Location |\n|---|---|\n| Day 1 | City Palace |\n\n' +
            '`console.log("Safe Journey");`\n\n' +
            '[Unsafe Link](javascript:alert("XSS"))',
          promptTokens: 25,
          candidateTokens: 35,
          totalTokens: 60,
        }),
    });

    await waitFor(() => {
      expect(screen.getByRole('heading', { level: 3, name: 'Jaipur Highlights' })).toBeInTheDocument();
    });

    // Verify headings render without raw ###
    const heading = screen.getByRole('heading', { level: 3, name: 'Jaipur Highlights' });
    expect(heading.textContent).toBe('Jaipur Highlights');
    expect(heading.textContent).not.toContain('###');

    // Verify bold rendering without raw **
    const strongElement = screen.getByText('curated');
    expect(strongElement.tagName.toLowerCase()).toBe('strong');

    const amerFortStrong = screen.getByText('Amer Fort');
    expect(amerFortStrong.tagName.toLowerCase()).toBe('strong');

    // Verify list items render as <li>
    const listItems = screen.getAllByRole('listitem');
    expect(listItems.length).toBe(2);
    expect(listItems[0].textContent).toContain('Visit the iconic Amer Fort');
    expect(listItems[0].textContent).not.toContain('*');

    // Verify safe link renders with target="_blank" and rel="noopener noreferrer"
    const safeLink = screen.getByRole('link', { name: 'Official Tourism Portal' });
    expect(safeLink).toHaveAttribute('href', 'https://tourism.rajasthan.gov.in');
    expect(safeLink).toHaveAttribute('target', '_blank');
    expect(safeLink).toHaveAttribute('rel', 'noopener noreferrer');

    // Verify unsafe link does not render as an anchor tag
    expect(screen.queryByRole('link', { name: 'Unsafe Link' })).not.toBeInTheDocument();
    expect(screen.getByText('Unsafe Link')).toBeInTheDocument();

    // Verify table rendered
    expect(screen.getByRole('table')).toBeInTheDocument();
    expect(screen.getByText('City Palace')).toBeInTheDocument();

    // Verify token metadata
    expect(screen.getByText('60 tokens')).toBeInTheDocument();
    expect(screen.getAllByText('gemini-3.5-flash-lite').length).toBeGreaterThan(0);
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

    const submitBtn = screen.getByRole('button', { name: /Ask AI/i });
    fireEvent.click(submitBtn);

    await waitFor(() => {
      expect(
        screen.getByText(/Gemini rate limit or quota exceeded/i)
      ).toBeInTheDocument();
    });
  });
});
