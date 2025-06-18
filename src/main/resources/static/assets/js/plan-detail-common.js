// Global variables
let selectedRating = 0;
let planData = null;
let benefitsData = null;
let productsData = null;
let reviewsData = [];
let responseReview = [];
let planId;

let currentPage = 0;
const pageSize = 5;
let totalPages = 1;

let editingReviewId = null;
let originalData = {};

// Initialize page
document.addEventListener('DOMContentLoaded', async () => {
    await loadNavbar();
    planId = getPlanIdFromUrl() || 1;

    await Promise.all([
        loadPlanData(planId),
        loadBenefitsData(planId),
        loadProductsData(planId),
        loadReviewsData(planId)
    ]);

    initializeRatingSystem();
});

async function apiRequest(url, options = {}) {
    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            window.location.href = "/login";
            return null;
        }

        if (!response.ok) {
            const result = await response.json().catch(() => ({}));
            throw new Error(result.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(`API 요청 실패 (${url}):`, error);
        throw error;
    }
}

// Load navbar
async function loadNavbar() {
    try {
        const response = await fetch('/assets/navbar.html');
        if (response.ok) {
            const html = await response.text();
            document.getElementById('navbar').innerHTML = html;
        }
    } catch (err) {
        console.error('navbar 로딩 실패:', err);
    }
}

// Extract plan ID from URL
function getPlanIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id') || window.location.pathname.split('/').pop();
}

// 맨 위로 스크롤
function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

const scrollTopBtn = document.getElementById('scrollTopBtn');

window.addEventListener('scroll', function () {
    if (window.scrollY > 800) {
        scrollTopBtn.style.display = 'flex';
    } else {
        scrollTopBtn.style.display = 'none';
    }
});

scrollTopBtn.addEventListener('click', scrollToTop);