let accessToken = localStorage.getItem("accessToken") || null;

export function setAccessToken(token) {
    accessToken = token;
    localStorage.setItem("accessToken", token);
}

async function apiRequest(endpoint, options = {}) {
    const token = localStorage.getItem("accessToken");
    console.log('>>> apiRequest', endpoint, 'token:', !!token);
    const res = await fetch(endpoint, {
        ...options,
        headers: {
            ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
            ...options.headers
        },
        credentials: 'same-origin'
    });

    if (res.status === 401 && !endpoint.includes('/api/auth/')) {
        const refreshed = await fetch('/api/auth/refresh', {
            method: 'POST',
            credentials: 'same-origin'
        });

        if (refreshed.ok) {
            const data = await refreshed.json();
            setAccessToken(data.accessToken);

            const retry = await fetch(endpoint, {
                ...options,
                headers: {
                    'Authorization': `Bearer ${data.accessToken}`,
                    ...options.headers
                },
                credentials: 'same-origin'
            });

            if (retry.ok) return retry.json();
        }

        localStorage.removeItem("accessToken");
        window.location.href = '/';
        return;
    }

    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return res.json();
}

export const API = {
    getMe: () => apiRequest('/api/auth/me'),
    logout: () => {
        localStorage.removeItem("accessToken");
        return fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'same-origin'
        });
    },
    getHalls: () => apiRequest('/api/player/halls'),
    saveHalls: (hallsStr) => fetch('/api/player/halls', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ halls: hallsStr })
    }),
    getVapidKey: () => fetch('/api/push/vapid-public-key', {
        headers: { 'Accept': 'text/plain' }
    }).then(r => r.text()),
    subscribePush: (data) => fetch('/api/push/subscribe', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    }),
    unsubscribePush: (data) => fetch('/api/push/unsubscribe', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    }),
    getDashboard: (playerId) => apiRequest(`/api/tournaments/${playerId}/last-result`),
    getUpcomingLineups: (playerName) => apiRequest(`/api/tournaments/upcoming-lineups?playerName=${encodeURIComponent(playerName)}`),
    getTop: (playerId, period, league) => {
        const url = league
            ? `/api/tournaments/${playerId}/top/${period}/${league}`
            : `/api/tournaments/${playerId}/top/${period}`;
        return apiRequest(url);
    },
    getSum: (playerId, params) => {
        const query = new URLSearchParams(params).toString();
        return apiRequest(`/api/tournaments/${playerId}/sum?${query}`);
    },
    updateResult: (resultId, amount, bonus) => fetch(`/api/tournaments/result/${resultId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount, bonus })
    }),
    getMyLineups: (date) => apiRequest(`/api/tournaments/lineups/my?date=${date}`),
    getAllLineups: (date) => apiRequest(`/api/tournaments/lineups/all?date=${date}`),
};