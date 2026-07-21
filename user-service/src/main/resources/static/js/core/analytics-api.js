import { state } from './state.js';

const BASE = '/api';

async function apiRequest(endpoint, options = {}) {
    const res = await fetch(`${BASE}${endpoint}`, { credentials: 'same-origin', ...options });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return res.json();
}

export const AnalyticsAPI = {
    getMe: () => apiRequest('/auth/me'),
    getSubscription: () => apiRequest('/player/subscription'),
    getLeagueAvg: () => apiRequest(`/tournaments/${state.playerId}/analytics`),
    getMonthly: (year) => apiRequest(`/tournaments/${state.playerId}/monthly-income?year=${year}`),
    getDaily: (year, month) => apiRequest(`/tournaments/${state.playerId}/daily-income?year=${year}&month=${month}`),
    getBestTime: (params) => {
        const query = new URLSearchParams(params).toString();
        return apiRequest(`/tournaments/${state.playerId}/best-time?${query}`);
    }
};

export async function checkSubscription() {
    try {
        const sub = await AnalyticsAPI.getSubscription();
        return sub && sub.active;
    } catch (e) {
        return false;
    }
}