import React, { useState, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Sparkles, Send, Copy, Check, AlertCircle, RefreshCw, Bot, Zap } from 'lucide-react';
import { Button } from '../common/Button';

interface AiTravelAssistantProps {
  apiBaseUrl: string;
}

interface GenerateResponse {
  success: boolean;
  model: string;
  response: string;
  promptTokens?: number;
  candidateTokens?: number;
  totalTokens?: number;
}

interface HealthResponse {
  status: string;
  model: string;
  configured: boolean;
  reachable: boolean;
  message: string;
}

const SAMPLE_PROMPTS = [
  'Plan a 3-day heritage and fort tour in Jaipur on a ₹15,000 budget.',
  'What are the best authentic local delicacies to try in Munnar?',
  'Give me photography and sunrise tips for visiting Hampi ruins.',
  'Recommend 4 off-beat cultural rituals to observe respectfully in Varanasi.',
];

export const AiTravelAssistant: React.FC<AiTravelAssistantProps> = ({ apiBaseUrl }) => {
  const [prompt, setPrompt] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<GenerateResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [copied, setCopied] = useState<boolean>(false);
  const [health, setHealth] = useState<HealthResponse | null>(null);
  const [healthLoading, setHealthLoading] = useState<boolean>(true);

  // Check AI connectivity on mount
  useEffect(() => {
    let isMounted = true;
    const fetchHealth = async () => {
      try {
        setHealthLoading(true);
        const res = await fetch(`${apiBaseUrl}/ai/health`);
        if (res.ok) {
          const body = await res.json();
          const data: HealthResponse = body.data || body;
          if (isMounted) setHealth(data);
        } else {
          if (isMounted) {
            setHealth({
              status: 'DEGRADED',
              model: 'gemini-3.5-flash-lite',
              configured: false,
              reachable: false,
              message: 'AI service returned non-200 status.',
            });
          }
        }
      } catch {
        if (isMounted) {
          setHealth({
            status: 'UNREACHABLE',
            model: 'gemini-3.5-flash-lite',
            configured: false,
            reachable: false,
            message: 'Unable to reach backend AI service.',
          });
        }
      } finally {
        if (isMounted) setHealthLoading(false);
      }
    };

    fetchHealth();
    return () => {
      isMounted = false;
    };
  }, [apiBaseUrl]);

  const handleGenerate = async (textToSubmit?: string) => {
    const text = (textToSubmit !== undefined ? textToSubmit : prompt).trim();
    if (!text) {
      setError('Please enter a question or travel prompt.');
      return;
    }

    if (text.length > 4000) {
      setError('Prompt exceeds maximum allowed length of 4,000 characters.');
      return;
    }

    setIsLoading(true);
    setError(null);
    setResponse(null);

    try {
      const res = await fetch(`${apiBaseUrl}/ai/generate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify({ prompt: text }),
      });

      const body = await res.json();

      if (!res.ok) {
        const errorMsg = body.message || `AI service returned error HTTP ${res.status}`;
        setError(errorMsg);
      } else {
        const payload: GenerateResponse = body.data || body;
        setResponse(payload);
      }
    } catch {
      setError('Network error: Unable to contact the Voyage-AI backend server.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCopy = () => {
    if (response?.response) {
      navigator.clipboard.writeText(response.response);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  return (
    <div className="bg-white rounded-3xl p-6 sm:p-8 border border-slate-200/80 shadow-xs space-y-6">
      {/* Header and Model Badge */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-6 border-b border-slate-100">
        <div>
          <div className="flex items-center gap-2 mb-1.5 text-brand-900 font-semibold text-xs uppercase tracking-wider">
            <Bot className="w-4 h-4 text-brand-900" />
            <span>AI Travel Concierge</span>
          </div>
          <h3 className="text-2xl font-bold font-display text-slate-900 tracking-tight">
            Curated Travel Intelligence
          </h3>
          <p className="text-xs text-slate-600 mt-1 max-w-xl">
            Ask for customized itineraries, local dining secrets, cultural etiquette, or transit insights.
          </p>
        </div>

        {/* Live Connectivity Badge */}
        <div className="flex items-center gap-2.5 px-3 py-2 rounded-2xl bg-slate-50 border border-slate-200/80 shrink-0 self-start sm:self-center">
          <div className="relative flex h-2.5 w-2.5">
            {health?.reachable ? (
              <>
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75" />
                <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-emerald-500" />
              </>
            ) : (
              <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-amber-500" />
            )}
          </div>
          <div className="text-left">
            <div className="flex items-center gap-1.5">
              <span className="text-[11px] font-bold text-slate-800">{health?.model || 'gemini-3.5-flash-lite'}</span>
              <span className="text-[9px] font-semibold px-1.5 py-0.2 rounded bg-brand-50 text-brand-900 border border-brand-200/60 uppercase">
                {healthLoading ? 'Checking...' : health?.status || 'Unknown'}
              </span>
            </div>
            <p className="text-[10px] text-slate-600">Travel AI Engine</p>
          </div>
        </div>
      </div>

      {/* Suggested Quick Prompt Chips */}
      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wider mb-2.5 flex items-center gap-1.5">
          <Zap className="w-3.5 h-3.5 text-amber-500" />
          <span>Quick Prompts</span>
        </p>
        <div className="flex flex-wrap gap-2">
          {SAMPLE_PROMPTS.map((sample, idx) => (
            <button
              key={idx}
              type="button"
              disabled={isLoading}
              onClick={() => {
                setPrompt(sample);
                handleGenerate(sample);
              }}
              className="text-xs text-left px-3 py-1.5 rounded-xl bg-slate-50 hover:bg-slate-100 border border-slate-200/70 text-slate-700 hover:text-slate-900 transition-colors disabled:opacity-50"
            >
              {sample}
            </button>
          ))}
        </div>
      </div>

      {/* Input Form */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleGenerate();
        }}
        className="space-y-3"
      >
        <div className="relative">
          <textarea
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            disabled={isLoading}
            rows={3}
            maxLength={4000}
            placeholder="E.g., Suggest a 2-day walking tour of Jaipur with artisanal textile workshops and heritage sweets..."
            className="w-full p-4 text-sm bg-slate-50/70 hover:bg-slate-50 focus:bg-white border border-slate-200/80 rounded-2xl text-slate-900 placeholder-slate-600 focus:outline-none focus:ring-2 focus:ring-brand-600/30 focus:border-brand-600 transition-colors resize-none disabled:opacity-60"
          />
          <div className="absolute right-3 bottom-3 text-[10px] font-medium text-slate-600">
            {prompt.length}/4000
          </div>
        </div>

        <div className="flex items-center justify-between gap-3">
          <div className="text-xs text-slate-600 flex items-center gap-1.5">
            <Sparkles className="w-3.5 h-3.5 text-brand-900" />
            <span>Direct generation via secured Spring Boot backend</span>
          </div>

          <Button
            type="submit"
            variant="primary"
            size="md"
            disabled={isLoading || !prompt.trim()}
            className="inline-flex items-center gap-2"
          >
            {isLoading ? (
              <>
                <RefreshCw className="w-4 h-4 animate-spin" />
                <span>Thinking…</span>
              </>
            ) : (
              <>
                <Send className="w-4 h-4" />
                <span>Ask AI</span>
              </>
            )}
          </Button>
        </div>
      </form>

      {/* Error Alert */}
      {error && (
        <div
          role="alert"
          className="p-4 rounded-2xl bg-rose-50 border border-rose-200 text-rose-800 text-xs flex items-start gap-3"
        >
          <AlertCircle className="w-4 h-4 shrink-0 text-rose-600 mt-0.5" />
          <div className="flex-1">
            <p className="font-semibold text-rose-900">AI Generation Notice</p>
            <p className="mt-0.5 text-rose-700">{error}</p>
          </div>
          <button
            type="button"
            onClick={() => handleGenerate()}
            className="px-2 py-1 rounded-lg bg-rose-100 hover:bg-rose-200 text-rose-900 font-semibold transition-colors"
          >
            Retry
          </button>
        </div>
      )}

      {/* Generated Result Output */}
      {response && (
        <div className="space-y-3 pt-2">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-slate-900 uppercase tracking-wider">
                Response
              </span>
              <span className="text-[10px] px-2 py-0.5 rounded-full bg-slate-100 text-slate-700 font-mono">
                {response.model}
              </span>
              {response.totalTokens !== undefined && (
                <span className="text-[10px] px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 font-mono">
                  {response.totalTokens} tokens
                </span>
              )}
            </div>

            <button
              type="button"
              onClick={handleCopy}
              className="inline-flex items-center gap-1.5 text-xs text-slate-600 hover:text-slate-900 px-2.5 py-1 rounded-lg hover:bg-slate-100 transition-colors"
            >
              {copied ? (
                <>
                  <Check className="w-3.5 h-3.5 text-emerald-600" />
                  <span className="text-emerald-700 font-semibold">Copied</span>
                </>
              ) : (
                <>
                  <Copy className="w-3.5 h-3.5" />
                  <span>Copy</span>
                </>
              )}
            </button>
          </div>

          <div
            data-testid="ai-response-content"
            className="p-5 rounded-2xl bg-slate-50 border border-slate-200/90 text-sm text-slate-800 font-sans ai-response-content whitespace-pre-wrap break-words"
          >
            <ReactMarkdown
              remarkPlugins={[remarkGfm]}
              components={{
                a: ({ href, children, ...props }) => {
                  const isSafe =
                    href &&
                    (href.startsWith('https://') ||
                      href.startsWith('http://') ||
                      href.startsWith('mailto:'));
                  if (!isSafe) {
                    return <span className="text-slate-800 font-medium">{children}</span>;
                  }
                  return (
                    <a
                      href={href}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-brand-900 hover:text-brand-700 font-medium underline underline-offset-2 transition-colors"
                      {...props}
                    >
                      {children}
                    </a>
                  );
                },
                h1: ({ children }) => (
                  <h1 className="text-xl font-bold font-display text-slate-900 mt-4 mb-2 first:mt-0 tracking-tight">
                    {children}
                  </h1>
                ),
                h2: ({ children }) => (
                  <h2 className="text-lg font-bold font-display text-slate-900 mt-3.5 mb-1.5 first:mt-0 tracking-tight">
                    {children}
                  </h2>
                ),
                h3: ({ children }) => (
                  <h3 className="text-base font-bold font-display text-slate-900 mt-3 mb-1.5 first:mt-0">
                    {children}
                  </h3>
                ),
                h4: ({ children }) => (
                  <h4 className="text-sm font-bold font-display text-slate-900 mt-2 mb-1 first:mt-0">
                    {children}
                  </h4>
                ),
                p: ({ children }) => <p className="mb-2.5 last:mb-0 leading-relaxed text-slate-800">{children}</p>,
                ul: ({ children }) => <ul className="list-disc pl-5 mb-3 space-y-1 text-slate-800">{children}</ul>,
                ol: ({ children }) => <ol className="list-decimal pl-5 mb-3 space-y-1 text-slate-800">{children}</ol>,
                li: ({ children }) => <li className="leading-relaxed pl-1">{children}</li>,
                strong: ({ children }) => <strong className="font-semibold text-slate-900">{children}</strong>,
                em: ({ children }) => <em className="italic text-slate-800">{children}</em>,
                blockquote: ({ children }) => (
                  <blockquote className="border-l-4 border-brand-500/80 bg-brand-50/50 rounded-r-xl pl-3.5 py-1.5 my-3 text-slate-700 italic">
                    {children}
                  </blockquote>
                ),
                pre: ({ children }) => (
                  <pre className="p-3.5 my-3 rounded-xl bg-slate-900 text-slate-100 font-mono text-xs overflow-x-auto">
                    {children}
                  </pre>
                ),
                code: ({ className, children, ...props }) => {
                  const isBlock = className && className.startsWith('language-');
                  if (isBlock) {
                    return <code className={className} {...props}>{children}</code>;
                  }
                  return (
                    <code className="px-1.5 py-0.5 rounded-md bg-slate-200/80 text-slate-900 font-mono text-xs" {...props}>
                      {children}
                    </code>
                  );
                },
                table: ({ children }) => (
                  <div className="overflow-x-auto my-3 rounded-xl border border-slate-200 shadow-xs">
                    <table className="min-w-full divide-y divide-slate-200 text-xs text-left">{children}</table>
                  </div>
                ),
                thead: ({ children }) => <thead className="bg-slate-100 font-semibold text-slate-900">{children}</thead>,
                tbody: ({ children }) => <tbody className="divide-y divide-slate-100 bg-white">{children}</tbody>,
                tr: ({ children }) => <tr className="hover:bg-slate-50/50 transition-colors">{children}</tr>,
                th: ({ children }) => <th className="px-3.5 py-2 font-semibold text-slate-900">{children}</th>,
                td: ({ children }) => <td className="px-3.5 py-2 text-slate-700">{children}</td>,
                hr: () => <hr className="my-3.5 border-slate-200" />,
              }}
            >
              {response.response}
            </ReactMarkdown>
          </div>
        </div>
      )}
    </div>
  );
};
