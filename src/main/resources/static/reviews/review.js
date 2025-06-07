const planId = 1; // 실제로는 서버에서 주입하거나 URL에서 추출

let currentPage = 0;
const pageSize = 5;


const modal = document.getElementById("review-modal");
const openBtn = document.getElementById("review-write-btn");
const closeBtn = document.querySelector(".close");
const cancelBtn = document.getElementById("cancel-review");
const stars = document.querySelectorAll(".star-rating .star");

let selectedRating = 5; // 기본값


// 모달 열기
openBtn.onclick = () => {
    modal.style.display = "flex";
    highlightStars(selectedRating);
};

// 모달 닫기
closeBtn.onclick = cancelBtn.onclick = () => {
    modal.style.display = "none";
};

// 별점 클릭
stars.forEach(star => {
    star.addEventListener("click", () => {
        selectedRating = parseInt(star.dataset.value);
        highlightStars(selectedRating);
    });
});

function highlightStars(rating) {
    const stars = document.querySelectorAll("#star-rating .star");
    stars.forEach(star => {
        const value = parseInt(star.getAttribute("data-value"));
        if (value <= rating) {
            star.classList.add("selected");
        } else {
            star.classList.remove("selected");
        }
    });

    // 숫자 텍스트 표시
    const insertRatingText = document.getElementById("insert-selected-rating-text");

    if (insertRatingText) {
        insertRatingText.textContent = `${rating}`;
    }
}

function setEditStars(rating) {
    const editStars = document.querySelectorAll('#edit-star-rating .star');
    editStars.forEach(star => {
        const val = parseInt(star.getAttribute('data-value'));
        if (val <= rating) {
            star.classList.add('selected');
        } else {
            star.classList.remove('selected');
        }
    });

    const updateRatingText = document.getElementById("edit-selected-rating-text");
    if (updateRatingText) {
        updateRatingText.textContent = `${rating}`;
    }

    // 별 클릭 시 갱신
    editStars.forEach(star => {
        star.onclick = () => {
            setEditStars(parseInt(star.getAttribute('data-value')));
        };
    });
}

// 클릭 이벤트 바인딩
document.querySelectorAll("#star-rating .star").forEach(star => {
    star.addEventListener("click", () => {
        selectedRating = parseInt(star.getAttribute("data-value"));
        highlightStars(selectedRating);
    });
});

// 초기 렌더링 시 하이라이트
highlightStars(selectedRating);

document.getElementById("submit-review").addEventListener("click", insertReview);


function openEditModal(review) {
    document.getElementById('edit-modal').style.display = 'flex';
    document.getElementById('edit-review-content').value = review.content;
    document.getElementById('edit-modal').setAttribute('data-review-id', review.reviewId);
    setEditStars(review.star); // 기존 별점 표시
}




// 리뷰 삭제
async function deleteReview(reviewId) {
    try {
        const response = await fetch(`/review/${reviewId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error("삭제 실패");
        }

        alert("리뷰가 삭제되었습니다.");
        loadReviews();
    } catch (error) {
        console.error("리뷰 삭제 오류:", error);
        alert("리뷰 삭제에 실패했습니다.");
    }
}

// 리뷰 수정
async function submitEditReview() {
    const reviewId = document.getElementById('edit-modal').getAttribute('data-review-id');
    const content = document.getElementById('edit-review-content').value;
    const stars = document.querySelectorAll('#edit-star-rating .star.selected').length;

    if (!content.trim()) {
        alert("수정할 내용을 입력해주세요.");
        return;
    }

    try {
        const response = await fetch(`/review/${reviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ content, star: stars })
        });

        if (!response.ok) {
            throw new Error("수정 실패");
        }

        alert("리뷰가 수정되었습니다.");
        closeEditModal();
        loadReviews();

    } catch (error) {
        console.error("수정 중 오류:", error);
        alert("리뷰 수정에 실패했습니다.");
    }
}

function closeEditModal() {
    document.getElementById('edit-modal').style.display = 'none';
}


// 리뷰 등록
async function insertReview() {
    const content = document.getElementById("review-insert-content").value;

    if (!content.trim()) {
        alert("리뷰 내용을 입력해주세요.");
        return;
    }

    try {
        const response = await fetch(`/review/${planId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: content,
                star: selectedRating
            })
        });

        const result = await response.json();

        if (!response.ok) {
            // 서버에서 내려준 커스텀 예외 메시지를 alert로 출력
            alert(result.message || "리뷰 등록 실패");
            return;
        }

        alert("리뷰가 등록되었습니다.");
        modal.style.display = "none";
        document.getElementById("review-insert-content").value = "";
        selectedRating = 5;
        highlightStars(selectedRating);
        loadReviews(); // 등록 후 리뷰 새로고침

    } catch (error) {
        console.error("리뷰 등록 중 오류:", error);
        alert("리뷰 등록에 실패했습니다.");
    }
}


// 리뷰 조회
async function loadReviews(page = 0) {

    try {
        const res = await fetch(`/review/${planId}?page=${page}&size=${pageSize}`);
        const data = await res.json();
        const reviews = data.data.content;

        const container = document.getElementById('reviews-container');
        const template = document.getElementById('review-template');

        const totalReviewElement = document.getElementById('total-review');
        totalReviewElement.textContent = data.data.totalElements; // 전체 개수
        const avgReviewStar = document.getElementById('avg-stars');
        avgReviewStar.textContent = Number(data.data.avgStar).toFixed(1); // 리뷰 평점

        const reviewWriteBtn = document.getElementById('review-write-btn');
        const isPlanUser = data.data.isPlanUser;
        if (isPlanUser) {
            reviewWriteBtn.style.display = 'inline-block'; // 혹은 'block'
        }


        container.innerHTML = '';
        reviews.forEach(review => {
            renderReview(review, template, container);
        });

        // 페이지 정보 갱신
        currentPage = data.data.page;
        totalPages = data.data.totalPages;

        renderPaginationButtons(totalPages, currentPage);
    } catch (error) {
        console.error('리뷰 로딩 실패:', error);
    }
}

// 이름 masking
function maskName(name) {
    if (name.length <= 2) return name[0] + '*';
    return name[0] + '*'.repeat(name.length - 2) + name[name.length - 1];
}


// 페이지네이션
function renderPaginationButtons(totalPages, currentPage) {
    const paginationContainer = document.getElementById('pagination-buttons');
    paginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement('button');
        btn.textContent = i + 1;
        btn.classList.add('page-btn'); // ✅ 이 부분이 중요
        if (i === currentPage) btn.classList.add('active');

        btn.disabled = i === currentPage;
        btn.addEventListener('click', () => loadReviews(i));
        paginationContainer.appendChild(btn);
    }
}


// 리뷰 item
function renderReview(review, template, container) {
    const clone = template.content.cloneNode(true);

    clone.querySelector('.username').textContent = maskName(review.userName);
    clone.querySelector('.review-date').textContent = new Date(review.createDate).toLocaleDateString();

    const filled = '<span class="star">★</span>'.repeat(review.star);
    const empty = '<span class="star empty">☆</span>'.repeat(5 - review.star);
    clone.querySelector('.stars').innerHTML = filled + empty;
    clone.querySelector('.rating-text').textContent = `${review.star}점`;
    clone.querySelector('.like-count').textContent = review.likeCnt;
    const contentElement = clone.querySelector('.review-content');
    const expandBtn = clone.querySelector('.expand-btn');
    contentElement.textContent = review.content;

    // 100자 이상이면 펼치기 버튼 표시
    if (review.content.length > 100) {
        expandBtn.style.display = 'inline'; // 버튼 보이기
        contentElement.classList.add('collapsed');

        expandBtn.addEventListener('click', () => {
            const isCollapsed = contentElement.classList.toggle('collapsed');
            expandBtn.textContent = isCollapsed ? '펼치기 ⌄' : '접기 ⌃';
        });
    }

    if (review.author) {
        const actionBtns = clone.querySelector('.action-buttons');
        actionBtns.style.display = 'flex'; // 작성자일 경우만 표시

        const editBtn = clone.querySelector('.edit-btn');
        editBtn.addEventListener('click', () => openEditModal(review));

        const deleteBtn = clone.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', () => {
            if (confirm("정말 이 리뷰를 삭제하시겠습니까?")) {
                deleteReview(review.reviewId);
            }
        });
    }

    // 좋아요 수
    const likeBtn = clone.querySelector('.like-btn');
    const likeCountSpan = clone.querySelector('.like-count');
    likeCountSpan.textContent = review.likeCnt;

    // isLiked가 true면 클래스 추가
    if (review.liked) {
        likeBtn.classList.add('liked');
    }

    likeBtn.addEventListener('click', () => {
        handleLikeClick(review.reviewId, likeBtn, likeCountSpan);
    });

    container.appendChild(clone);
}

// 좋아요 등록/취소
function handleLikeClick(reviewId, likeBtn, likeCountSpan) {
    const liked = likeBtn.classList.contains('liked');

    fetch(`/review/like/${reviewId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) throw new Error('Failed to update like');

        // 성공 시 UI 업데이트
        likeBtn.classList.toggle('liked');
        let count = parseInt(likeCountSpan.textContent);
        likeCountSpan.textContent = liked ? count - 1 : count + 1;
    }).catch(error => {
        console.error('좋아요 처리 실패:', error);
    });
}

loadReviews();