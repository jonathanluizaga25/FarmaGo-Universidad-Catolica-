export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export function fetchWithAuth(url, options = {}) {
  const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };
  return fetch(url, { ...options, headers });
}
