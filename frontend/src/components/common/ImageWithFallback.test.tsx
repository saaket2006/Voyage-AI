import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { ImageWithFallback } from './ImageWithFallback';

describe('ImageWithFallback Component', () => {
  it('renders image when source is provided', () => {
    render(
      <ImageWithFallback
        src="https://images.unsplash.com/valid-image.jpg"
        alt="Test Destination"
        fallbackTitle="Test Destination"
      />
    );

    const img = screen.getByRole('img');
    expect(img).toBeInTheDocument();
    expect(img).toHaveAttribute('src', 'https://images.unsplash.com/valid-image.jpg');
    expect(img).toHaveAttribute('alt', 'Test Destination');
  });

  it('renders graceful fallback when image triggers error', () => {
    render(
      <ImageWithFallback
        src="https://images.unsplash.com/broken-link-404.jpg"
        alt="Broken Image"
        fallbackTitle="Hampi"
        fallbackSubtitle="Karnataka, India"
      />
    );

    const img = screen.getByRole('img');
    fireEvent.error(img);

    const fallback = screen.getByTestId('image-fallback');
    expect(fallback).toBeInTheDocument();
    expect(screen.getByText('Hampi')).toBeInTheDocument();
    expect(screen.getByText('Karnataka, India')).toBeInTheDocument();
  });

  it('renders fallback immediately when src is empty', () => {
    render(
      <ImageWithFallback
        src=""
        alt="Empty Source"
        fallbackTitle="Fallback Title"
      />
    );

    expect(screen.getByTestId('image-fallback')).toBeInTheDocument();
    expect(screen.getByText('Fallback Title')).toBeInTheDocument();
  });
});
