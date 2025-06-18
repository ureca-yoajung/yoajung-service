async function loadReviewsData(planId) {
    try {
        const result = await apiRequest(`/review/${planId}?page=${currentPage}&size=${pageSize}`);
        if (result && result.status === 'OK' && result.data) {
            responseReview = result.data;
            totalPages = result.data.totalPages;

            const reviewWriteBtn = document.getElementById('write-review-btn');
            if (reviewWriteBtn) {
                reviewWriteBtn.style.display = result.data.isPlanUser ? 'inline-block' : 'none';
            }
        }
    } catch (error) {
        console.error('Reviews data loading failed:', error);
    } finally {
        updateReviewsInfo();
    }
}

function updateReviewsInfo() {
    if (!responseReview.content) return;

    reviewsData = responseReview.content.map(review => ({
        id: review.reviewId,
        userId: review.userId,
        userName: review.userName,
        content: review.content,
        rating: review.star,
        helpful: review.likeCnt,
        notHelpful: 0,
        createdAt: review.createDate,
        isAuthor: review.author,
        isLiked: review.liked
    }));

    updateReviewsStats();
    renderReviews();
    renderPagination();
}

function updateReviewsStats() {
    const avgRating = parseFloat(responseReview.avgStar || 0);
    const totalElements = responseReview.totalElements || 0;

    document.getElementById('averageRating').textContent = avgRating.toFixed(1);
    document.getElementById('reviewCount').textContent = `(${totalElements}개 리뷰)`;

    const starsContainer = document.getElementById('averageStars');
    starsContainer.innerHTML = '';
    const rating = Math.round(avgRating);

    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('span');
        star.className = i <= rating ? 'star' : 'star empty';
        star.textContent = '★';
        starsContainer.appendChild(star);
    }
}

// Review rendering functions
function renderReviews() {
    const reviewsList = document.getElementById('reviewsList');
    reviewsList.innerHTML = '';

    if (reviewsData.length === 0) {
        reviewsList.innerHTML = '<p style="text-align: center; color: var(--text-secondary); padding: 40px 0;">아직 리뷰가 없습니다. 첫 번째 리뷰를 작성해보세요!</p>';
        return;
    }

    reviewsData.forEach(review => {
        const reviewElement = createReviewElement(review);
        reviewsList.appendChild(reviewElement);
    });
}

function renderPagination() {
    const pagination = document.getElementById('reviewsPagination');
    pagination.innerHTML = '';

    if (totalPages <= 1) return;

    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement('button');
        btn.textContent = i + 1;
        btn.className = (i === currentPage) ? 'active' : '';
        btn.addEventListener('click', () => {
            if (i !== currentPage) {
                currentPage = i;
                loadReviewsData(planId);
            }
        });
        pagination.appendChild(btn);
    }
}

function createReviewElement(review) {
    const reviewDiv = document.createElement('div');
    reviewDiv.className = 'review-item';
    reviewDiv.setAttribute('data-review-id', review.id);

    const avatar = review.userName ? review.userName.charAt(0) : '?';
    const formattedDate = formatDate(review.createdAt);

    reviewDiv.innerHTML = `
        <div class="review-header">
            <div class="reviewer-info">
                <div class="reviewer-avatar">${avatar}</div>
                <div class="reviewer-details">
                    <h4>${review.userName || '익명'}</h4>
                    <span class="review-date">${formattedDate}</span>
                </div>
            </div>
            <div class="review-rating">
                ${'<span class="star">★</span>'.repeat(review.rating)}
                ${'<span class="star empty">★</span>'.repeat(5 - review.rating)}
            </div>
        </div>
        <div class="review-content">
            <br>${review.content}
        </div>
        <div class="review-helpful">
            <button class="helpful-btn ${review.isLiked ? 'active' : ''}" onclick="toggleHelpful(this, true)">
                👍 도움됨 <span>${review.helpful || 0}</span>
            </button>
        </div>
        ${review.isAuthor ? `
            <div class="review-actions">
                <button class="edit-btn" onclick="editReview(${review.id})">✏️ 수정</button>
                <button class="delete-btn" onclick="deleteReview(${review.id})">🗑 삭제</button>
            </div>
        ` : ''}
    `;

    return reviewDiv;
}

// Utility functions
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Rating system functions
function initializeRatingSystem() {
    const ratingStars = document.querySelectorAll('.rating-star');

    ratingStars.forEach(star => {
        star.addEventListener('click', function () {
            selectedRating = parseInt(this.dataset.rating);
            updateRatingDisplay();
        });

    });

    document.getElementById('ratingInput').addEventListener('mouseleave', function () {
        updateRatingDisplay();
    });
}

function highlightStars(rating) {
    const stars = document.querySelectorAll('.rating-star');
    stars.forEach((star, index) => {
        star.classList.toggle('active', index < rating);
    });
}

function updateRatingDisplay() {
    highlightStars(selectedRating);
}

// Review form functions
function toggleReviewForm() {
    const form = document.getElementById('reviewForm');
    form.classList.toggle('active');
}

function submitReview(event) {
    event.preventDefault();

    const content = document.getElementById('reviewContent').value;

    if (selectedRating === 0) {
        alert('평점을 선택해주세요.');
        return;
    }

    if (!content.trim()) {
        alert('리뷰 내용을 입력해주세요.');
        return;
    }

    const newReview = {
        rating: selectedRating,
        content: content,
    };

    insertReview(newReview);

    // Reset form
    document.getElementById('reviewContent').value = '';
    selectedRating = 0;
    updateRatingDisplay();
    toggleReviewForm();
}

// Review CRUD operations
async function insertReview(newReview) {
    try {
        const result = await apiRequest(`/review/${planId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: newReview.content,
                star: newReview.rating
            })
        });

        if (result) {
            alert("리뷰가 등록되었습니다.");
            // Reset to first page to show new review
            currentPage = 0;
            await loadReviewsData(planId);
        }
    } catch (error) {
        alert(error.message || "리뷰 등록에 실패했습니다.");
    }
}

async function deleteReview(reviewId) {
    if (!confirm("정말로 이 리뷰를 삭제하시겠습니까?")) {
        return;
    }

    try {
        await apiRequest(`/review/${reviewId}`, {
            method: 'DELETE'
        });

        alert("리뷰가 삭제되었습니다.");

        // Update local data
        reviewsData = reviewsData.filter(review => review.id !== reviewId);

        // If current page becomes empty and it's not the first page, go to previous page
        if (reviewsData.length === 0 && currentPage > 0) {
            currentPage--;
        }

        await loadReviewsData(planId);
    } catch (error) {
        alert(error.message || "리뷰 삭제에 실패했습니다.");
    }
}

// Review editing functions
function editReview(reviewId) {
    if (editingReviewId && editingReviewId !== reviewId) {
        cancelEdit(editingReviewId);
    }

    const reviewItem = document.querySelector(`[data-review-id="${reviewId}"]`);
    const contentElement = reviewItem.querySelector('.review-content');
    const ratingElement = reviewItem.querySelector('.review-rating');
    const actionsElement = reviewItem.querySelector('.review-actions');

    const currentRating = ratingElement.querySelectorAll('.star:not(.empty)').length;
    const currentContent = contentElement.textContent.trim();

    originalData[reviewId] = {
        rating: currentRating,
        content: currentContent
    };

    reviewItem.classList.add('editing');
    editingReviewId = reviewId;

    const editForm = createEditForm(reviewId, currentRating, currentContent);

    contentElement.style.display = 'none';
    actionsElement.style.display = 'none';

    reviewItem.appendChild(editForm);
}

function createEditForm(reviewId, currentRating, currentContent) {
    const editDiv = document.createElement('div');
    editDiv.className = 'edit-form';
    editDiv.innerHTML = `
        <div class="edit-form-group">
            <label class="edit-form-label">평점</label>
            <div class="edit-rating-input" data-review-id="${reviewId}">
                ${Array.from({length: 5}, (_, i) =>
        `<span class="edit-rating-star ${i < currentRating ? 'active' : ''}" data-rating="${i + 1}">★</span>`
    ).join('')}
            </div>
        </div>
        <div class="edit-form-group">
            <label for="editContent${reviewId}" class="edit-form-label">내용</label>
            <textarea id="editContent${reviewId}" class="edit-form-textarea" required>${currentContent}</textarea>
        </div>
        <div class="edit-form-actions">
            <button type="button" class="btn-cancel-edit" onclick="cancelEdit(${reviewId})">취소</button>
            <button type="button" class="btn-save-edit" onclick="saveEdit(${reviewId})">저장</button>
        </div>
    `;

    // Add rating star event listeners
    const ratingStars = editDiv.querySelectorAll('.edit-rating-star');
    ratingStars.forEach(star => {
        star.addEventListener('click', function() {
            const rating = parseInt(this.dataset.rating);
            updateEditRatingDisplay(reviewId, rating);
        });

        star.addEventListener('mouseover', function() {
            const rating = parseInt(this.dataset.rating);
            highlightEditStars(reviewId, rating);
        });
    });

    editDiv.addEventListener('mouseleave', function() {
        const currentRating = getCurrentEditRating(reviewId);
        updateEditRatingDisplay(reviewId, currentRating);
    });

    return editDiv;
}

function updateEditRatingDisplay(reviewId, rating) {
    highlightEditStars(reviewId, rating);
}

function highlightEditStars(reviewId, rating) {
    const stars = document.querySelectorAll(`[data-review-id="${reviewId}"] .edit-rating-star`);
    stars.forEach((star, index) => {
        star.classList.toggle('active', index < rating);
    });
}

function getCurrentEditRating(reviewId) {
    const activeStars = document.querySelectorAll(`[data-review-id="${reviewId}"] .edit-rating-star.active`);
    return activeStars.length;
}

async function saveEdit(reviewId) {
    const newContent = document.getElementById(`editContent${reviewId}`).value.trim();
    const newRating = getCurrentEditRating(reviewId);

    if (!newContent) {
        alert('리뷰 내용을 입력해주세요.');
        return;
    }

    if (newRating === 0) {
        alert('평점을 선택해주세요.');
        return;
    }

    try {
        await apiRequest(`/review/${reviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ content: newContent, star: newRating })
        });

        // Update local data
        const review = reviewsData.find(r => r.id == reviewId);
        if (review) {
            review.content = newContent;
            review.rating = newRating;
        }

        updateReviewDisplay(reviewId, newRating, newContent);
        exitEditMode(reviewId);

        alert('리뷰가 수정되었습니다.');

        // Refresh to ensure data consistency
        await loadReviewsData(planId);
    } catch (error) {
        alert(error.message || '리뷰 수정에 실패했습니다.');
    }
}

function cancelEdit(reviewId) {
    exitEditMode(reviewId);
}

function exitEditMode(reviewId) {
    const reviewItem = document.querySelector(`[data-review-id="${reviewId}"]`);
    const editForm = reviewItem.querySelector('.edit-form');
    const contentElement = reviewItem.querySelector('.review-content');
    const actionsElement = reviewItem.querySelector('.review-actions');

    if (editForm) {
        editForm.remove();
    }

    contentElement.style.display = '';
    actionsElement.style.display = '';

    reviewItem.classList.remove('editing');

    editingReviewId = null;
    delete originalData[reviewId];
}

function updateReviewDisplay(reviewId, newRating, newContent) {
    const reviewItem = document.querySelector(`[data-review-id="${reviewId}"]`);
    const contentElement = reviewItem.querySelector('.review-content');
    const ratingElement = reviewItem.querySelector('.review-rating');

    contentElement.textContent = newContent;

    ratingElement.innerHTML = '';
    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('span');
        star.className = i <= newRating ? 'star' : 'star empty';
        star.textContent = '★';
        ratingElement.appendChild(star);
    }
}

// Helpful button functions
function toggleHelpful(button) {
    const span = button.querySelector('span');
    const currentCount = parseInt(span.textContent);
    const reviewItem = button.closest('.review-item');
    const reviewId = reviewItem.getAttribute('data-review-id');

    const isActive = button.classList.contains('active');
    const newCount = isActive ? currentCount - 1 : currentCount + 1;

    // Update UI immediately
    button.classList.toggle('active', !isActive);
    span.textContent = Math.max(0, newCount);

    // Update local data
    const review = reviewsData.find(r => r.id == reviewId);
    if (review) {
        review.helpful = Math.max(0, newCount);
        review.isLiked = !isActive;
    }

    handleLikeClick(reviewId, !isActive);
}

async function handleLikeClick(reviewId, isAdd) {
    try {
        await apiRequest(`/review/like/${reviewId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ isAdd })
        });
    } catch (error) {
        console.error('좋아요 처리 실패:', error);
        // Revert UI changes on error
        const reviewItem = document.querySelector(`[data-review-id="${reviewId}"]`);
        const button = reviewItem.querySelector('.helpful-btn');
        const span = button.querySelector('span');
        const currentCount = parseInt(span.textContent);

        button.classList.toggle('active');
        span.textContent = isAdd ? currentCount - 1 : currentCount + 1;

        const review = reviewsData.find(r => r.id == reviewId);
        if (review) {
            review.helpful = isAdd ? currentCount - 1 : currentCount + 1;
            review.isLiked = !isAdd;
        }
    }
}