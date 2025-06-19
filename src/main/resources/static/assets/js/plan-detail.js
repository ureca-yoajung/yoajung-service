/* ---------- AI REVIEW SUMMARY ---------- */
async function loadSummaryData(planId) {
    try {
        const result = await apiRequest(`/api/plans/${planId}/summary`);
        if (result && result.status === 'OK' && result.data) {
            const container = document.getElementById('aiReviewSummary');
            const textEl = document.getElementById('aiSummaryText');
            textEl.textContent = result.data.summaryText || '요약 정보가 없습니다.';
            container.style.display = 'block';
        }
    } catch (error) {
        console.error('Summary data loading failed:', error);
    }
}

// 호출: 상세 페이지 로드 시 planId 파라미터를 읽어 요약 요청
document.addEventListener('DOMContentLoaded', () => {
    const planId = new URLSearchParams(window.location.search).get('id');
    if (planId) {
        loadSummaryData(planId);
    }
});

// Data loading functions
async function loadPlanData(planId) {
    try {
        const result = await apiRequest(`/api/plan/${planId}`);
        if (result && result.status === 'OK' && result.data) {
            planData = result.data;
        }
    } catch (error) {
        console.error('Plan data loading failed:', error);
    } finally {
        updatePlanInfo();
    }
}

async function loadBenefitsData(planId) {
    try {
        const result = await apiRequest(`/api/plan/benefits/${planId}`);
        if (result && result.status === 'OK' && result.data) {
            benefitsData = result.data;
        }
    } catch (error) {
        console.error('Benefits data loading failed:', error);
    } finally {
        updateBenefitsInfo();
    }
}

async function loadProductsData(planId) {
    try {
        const result = await apiRequest(`/api/plan/products/${planId}`);
        if (result && result.status === 'OK' && result.data) {
            productsData = result.data;
        }
    } catch (error) {
        console.error('Products data loading failed:', error);
    } finally {
        updateProductsInfo();
    }
}

function updatePlanInfo() {
    if (!planData) return;

    const get = (id) => document.getElementById(id);

    const dataAllowance = planData.dataAllowance;
    const tethering = planData.tetheringSharingAllowance;
    const speed = planData.speedAfterLimit;

    const setFeatureVisibility = (value, textId, wrapperId, formatter) => {
        const textEl = get(textId);
        const wrapperEl = get(wrapperId);
        if (!textEl || !wrapperEl) return;

        if (value === 0) {
            wrapperEl.style.display = 'none';
        } else {
            textEl.textContent = formatter(value);
            wrapperEl.style.display = 'flex';
        }
    };

    setFeatureVisibility(
        dataAllowance,
        'dataAllowance',
        'dataIcon',
        (val) => val === 9999 ? '무제한' : `${val}GB`
    );

    setFeatureVisibility(
        tethering,
        'tetheringSharingAllowance',
        'tetheringIcon',
        (val) => val === 9999 ? '무제한' : `${val}GB`
    );

    setFeatureVisibility(
        speed,
        'speedAfterLimit',
        'speedIcon',
        (val) => val === 9999 ? '속도제한 없음' : `다 쓰면 최대 +${val}Mbps`
    );

    const categoryMap = {
        ALL: '전체',
        LTE_FIVE_G: 'LTE/5G',
        ONLINE_ONLY: '온라인 전용'
    };

    const updates = {
        planName: planData.name,
        planDetailName: planData.name,
        basePrice: planData.basePrice.toLocaleString(),
        planDescription: planData.description,
        planCategory: categoryMap[planData.planCategory] || planData.planCategory,
        planTarget: planData.planTarget
    };

    Object.entries(updates).forEach(([id, value]) => {
        const element = get(id);
        if (element) element.textContent = value;
    });
}


function updateBenefitsInfo() {
    if (!benefitsData) return;

    const container = document.getElementById('baseBenefitsContainer');
    const grid = document.getElementById('baseBenefits');

    grid.innerHTML = '';

    const categoryTypes = {
        voice: 'VOICE',
        sms: 'SMS',
        smartDevice: 'SMARTDEVICE',
        base: 'BASE',
        other: 'OTHER'
    };

    let hasBenefits = false;

    for (const [key, type] of Object.entries(categoryTypes)) {
        const benefit = benefitsData[key];
        if (benefit) {
            const benefitCard = createBenefitCard({
                id: `${type.toLowerCase()}_benefit`,
                name: benefit.name,
                description: benefit.description,
                type: type,
                discountAmount: benefit.discountAmount
            });
            grid.appendChild(benefitCard);
            hasBenefits = true;
        }
    }

    // 표시 여부 결정
    container.style.display = hasBenefits ? 'flex' : 'none';
}

function updateProductsInfo() {
    const containers = {
        premium: document.getElementById('premiumContainer'),
        media: document.getElementById('mediaContainer')
    };

    const sections = {
        premium: document.getElementById('premiumBenefitsSection'),
        media: document.getElementById('mediaBenefitsSection')
    };

    const grids = {
        premium: document.getElementById('premiumBenefits'),
        media: document.getElementById('mediaBenefits')
    };

    // 초기화
    Object.values(grids).forEach(grid => grid.innerHTML = '');
    Object.values(sections).forEach(section => section.classList.remove('has-benefits'));
    Object.values(containers).forEach(container => container.style.display = 'none');

    if (!productsData) return;

    ['media', 'premium'].forEach(type => {
        if (productsData[type] && productsData[type].length > 0) {
            containers[type].style.display = 'flex'; // 또는 'block' 등 레이아웃에 맞게
            sections[type].classList.add('has-benefits');

            productsData[type].forEach((product, index) => {
                const productCard = createProductCard({
                    id: `${type}_${index}`,
                    name: product.name,
                    description: product.description,
                    type: type.toUpperCase(),
                    imageUrl: product.productImage
                });
                grids[type].appendChild(productCard);
            });
        }
    });
}

// Card creation functions
function createBenefitCard(benefit) {
    const card = document.createElement('div');
    card.className = 'benefit-card';
    card.setAttribute('data-benefit-id', benefit.id);

    const limitText = (benefit.discountAmount && benefit.discountAmount > 0)
        ? `${benefit.discountAmount.toLocaleString()}원 할인`
        : '제공';

    card.innerHTML = `
                    <div class="benefit-header">
                        <div class="benefit-info">
                            <h4 class="benefit-name">${benefit.name}</h4>
                        </div>
                    </div>
                    <p class="benefit-description">${benefit.description}</p>
                    <div class="benefit-details">
                        <span class="benefit-limit">${limitText}</span>
                    </div>
                `;

    return card;
}

function createProductCard(product) {
    const card = document.createElement('div');
    card.className = 'product-card';

    // 이미지 파싱 로직
    let imgText = '';
    if (product.imageUrl) {
        if (typeof product.imageUrl === 'object') {
            imgText = product.imageUrl.data ?? '';
        } else {
            try {
                const parsed = JSON.parse(product.imageUrl);
                imgText = parsed.data ?? '';
            } catch {
                imgText = product.imageUrl;
            }
        }
    }

    // 이미지 유효성 체크 및 기본 이미지 처리
    const invalidValues = ['/', '', null, undefined];
    const isValidUrl =
        typeof imgText === 'string' &&
        !invalidValues.includes(imgText.trim()) &&
        imgText.trim().length > 0;

    const finalImageUrl = isValidUrl ? imgText : '/assets/image/defaultImg.jpg';

    card.innerHTML = `
                    <img src="${finalImageUrl}" alt="${product.name}" class="product-image">
                    <div class="product-name">${product.name}</div>
                    <div class="product-description">${product.description}</div>
                `;

    return card;
}

function scrollMedia(containerId, direction) {
    const container = document.getElementById(containerId);
    const scrollAmount = 320;
    container.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
    });
}

function scrollPremium(containerId, direction) {
    const container = document.getElementById(containerId);
    const scrollAmount = 450;
    container.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
    });
}