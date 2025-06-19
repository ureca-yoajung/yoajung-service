/**
 * 커스텀 알림 시스템
 * 기본 alert 대신 사용할 수 있는 더 예쁜 알림 컴포넌트
 */

// 토스트 컨테이너가 없으면 생성
function ensureToastContainer() {
    let container = document.querySelector(".custom-toast-container")
    if (!container) {
        container = document.createElement("div")
        container.className = "custom-toast-container"
        document.body.appendChild(container)

        // 스타일 추가
        const style = document.createElement("style")
        style.textContent = `
      .custom-toast-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 9999;
        display: flex;
        flex-direction: column;
        gap: 10px;
        max-width: 350px;
      }
      
      .custom-toast {
        background: white;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        overflow: hidden;
        margin-bottom: 10px;
        transform: translateX(120%);
        transition: transform 0.3s ease;
        border-left: 4px solid #4f46e5;
        opacity: 0;
      }
      
      .custom-toast.show {
        transform: translateX(0);
        opacity: 1;
      }
      
      .custom-toast.hide {
        transform: translateX(120%);
        opacity: 0;
      }
      
      .custom-toast.success {
        border-left-color: #10b981;
      }
      
      .custom-toast.error {
        border-left-color: #ef4444;
      }
      
      .custom-toast.warning {
        border-left-color: #f59e0b;
      }
      
      .custom-toast.info {
        border-left-color: #4f46e5;
      }
      
      .toast-header {
        display: flex;
        align-items: center;
        padding: 12px 15px 5px;
        background-color: rgba(255, 255, 255, 0.95);
        border-bottom: 1px solid rgba(0, 0, 0, 0.05);
      }
      
      .toast-icon {
        margin-right: 8px;
        font-size: 1.1rem;
      }
      
      .toast-icon i {
        vertical-align: middle;
      }
      
      .custom-toast.success .toast-icon i {
        color: #10b981;
      }
      
      .custom-toast.error .toast-icon i {
        color: #ef4444;
      }
      
      .custom-toast.warning .toast-icon i {
        color: #f59e0b;
      }
      
      .custom-toast.info .toast-icon i {
        color: #4f46e5;
      }
      
      .toast-body {
        padding: 10px 15px 12px;
        word-wrap: break-word;
      }
      
      .btn-close {
        background: transparent;
        border: none;
        font-size: 1.2rem;
        cursor: pointer;
        padding: 0;
        margin-left: auto;
        color: #64748b;
        opacity: 0.7;
        transition: opacity 0.2s;
      }
      
      .btn-close:hover {
        opacity: 1;
      }
      
      .btn-close::before {
        content: "×";
      }
    `
        document.head.appendChild(style)
    }
    return container
}

/**
 * 커스텀 알림 표시
 * @param {string} message - 표시할 메시지
 * @param {string} type - 알림 유형 (success, error, warning, info)
 * @param {number} duration - 표시 시간(ms), 기본값 3000ms
 */
function showAlert(message, type = "info", duration = 3000) {
    const container = ensureToastContainer()

    // 토스트 요소 생성
    const toast = document.createElement("div")
    toast.className = `custom-toast ${type}`

    // 아이콘 결정
    let icon = ""
    switch (type) {
        case "success":
            icon = "bi-check-circle-fill"
            break
        case "error":
            icon = "bi-exclamation-circle-fill"
            break
        case "warning":
            icon = "bi-exclamation-triangle-fill"
            break
        default:
            icon = "bi-info-circle-fill"
    }

    // 토스트 내용 구성
    toast.innerHTML = `
    <div class="toast-header">
      <div class="toast-icon">
        <i class="bi ${icon}"></i>
      </div>
      <strong class="me-auto">${type.charAt(0).toUpperCase() + type.slice(1)}</strong>
      <button type="button" class="btn-close" aria-label="Close"></button>
    </div>
    <div class="toast-body">
      ${message}
    </div>
  `

    // 닫기 버튼 이벤트 연결
    const closeBtn = toast.querySelector(".btn-close")
    closeBtn.addEventListener("click", () => {
        hideToast(toast)
    })

    // 토스트 추가 및 표시
    container.appendChild(toast)

    // 애니메이션을 위한 지연
    setTimeout(() => {
        toast.classList.add("show")
    }, 10)

    // 자동 닫기 타이머 설정
    if (duration > 0) {
        setTimeout(() => {
            hideToast(toast)
        }, duration)
    }

    return toast
}

// 토스트 숨기기 함수
function hideToast(toast) {
    toast.classList.add("hide")
    toast.classList.remove("show")

    // 애니메이션 완료 후 요소 제거
    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast)
        }
    }, 300)
}

/**
 * 커스텀 확인 대화 상자 표시
 * @param {string} message - 표시할 메시지
 * @param {string} title - 대화 상자 제목 (기본값: '확인')
 * @param {string} confirmText - 확인 버튼 텍스트 (기본값: '확인')
 * @param {string} cancelText - 취소 버튼 텍스트 (기본값: '취소')
 * @returns {Promise<boolean>} - 사용자가 확인을 누르면 true, 취소를 누르면 false
 */
function showConfirm(message, title = "확인", confirmText = "확인", cancelText = "취소") {
    return new Promise((resolve) => {
        // 스타일 추가
        const styleElement = document.createElement('style');
        styleElement.textContent = `
      /* 확인 대화 상자 스타일 */
      .custom-confirm-backdrop {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 10000;
        opacity: 0;
        transition: opacity 0.3s ease;
      }
      
      .custom-confirm-backdrop.show {
        opacity: 1;
      }
      
      .custom-confirm {
        background: white;
        border-radius: 8px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
        width: 90%;
        max-width: 400px;
        overflow: hidden;
        transform: translateY(20px);
        transition: transform 0.3s ease;
      }
      
      .custom-confirm-backdrop.show .custom-confirm {
        transform: translateY(0);
      }
      
      .custom-confirm-header {
        display: flex;
        align-items: center;
        padding: 15px 20px 10px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
      }
      
      .custom-confirm-icon {
        margin-right: 10px;
        font-size: 1.2rem;
        color: #f59e0b;
      }
      
      .custom-confirm-title {
        font-weight: 600;
        font-size: 1.1rem;
        margin: 0;
      }
      
      .custom-confirm-body {
        padding: 20px;
      }
      
      .custom-confirm-message {
        margin: 0 0 20px 0;
        line-height: 1.5;
      }
      
      .custom-confirm-actions {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
      }
      
      .custom-confirm-btn {
        padding: 8px 16px;
        border-radius: 4px;
        font-weight: 500;
        cursor: pointer;
        border: none;
        transition: background-color 0.2s;
      }
      
      .custom-confirm-btn-cancel {
        background-color: #e5e7eb;
        color: #4b5563;
      }
      
      .custom-confirm-btn-cancel:hover {
        background-color: #d1d5db;
      }
      
      .custom-confirm-btn-confirm {
        background-color: #ef4444;
        color: white;
      }
      
      .custom-confirm-btn-confirm:hover {
        background-color: #dc2626;
      }
    `;
        document.head.appendChild(styleElement);

        // 배경 요소 생성
        const backdrop = document.createElement("div");
        backdrop.className = "custom-confirm-backdrop";

        // 대화 상자 요소 생성
        const confirmDialog = document.createElement("div");
        confirmDialog.className = "custom-confirm";

        // 대화 상자 내용 구성
        confirmDialog.innerHTML = `
      <div class="custom-confirm-header">
        <div class="custom-confirm-icon">
          <i class="bi bi-question-circle"></i>
        </div>
        <h3 class="custom-confirm-title">${title}</h3>
      </div>
      <div class="custom-confirm-body">
        <p class="custom-confirm-message">${message}</p>
        <div class="custom-confirm-actions">
          <button class="custom-confirm-btn custom-confirm-btn-cancel">${cancelText}</button>
          <button class="custom-confirm-btn custom-confirm-btn-confirm">${confirmText}</button>
        </div>
      </div>
    `;

        // 대화 상자를 배경에 추가
        backdrop.appendChild(confirmDialog);

        // 문서에 추가
        document.body.appendChild(backdrop);

        // 애니메이션을 위한 지연
        setTimeout(() => {
            backdrop.classList.add("show");
        }, 10);

        // 버튼 이벤트 연결
        const cancelBtn = confirmDialog.querySelector(".custom-confirm-btn-cancel");
        const confirmBtn = confirmDialog.querySelector(".custom-confirm-btn-confirm");

        // 취소 버튼 클릭
        cancelBtn.addEventListener("click", () => {
            closeConfirm(false);
        });

        // 확인 버튼 클릭
        confirmBtn.addEventListener("click", () => {
            closeConfirm(true);
        });

        // ESC 키 누름
        document.addEventListener("keydown", handleKeyDown);

        // 대화 상자 닫기 함수
        function closeConfirm(result) {
            backdrop.classList.remove("show");

            // 애니메이션 완료 후 요소 제거
            setTimeout(() => {
                document.body.removeChild(backdrop);
                document.removeEventListener("keydown", handleKeyDown);
                document.head.removeChild(styleElement);
                resolve(result);
            }, 300);
        }

        // ESC 키 처리
        function handleKeyDown(e) {
            if (e.key === "Escape") {
                closeConfirm(false);
            }
        }
    });
}

// 유형별 편의 함수
function showSuccessAlert(message, duration = 3000) {
    return showAlert(message, "success", duration)
}

function showErrorAlert(message, duration = 3000) {
    return showAlert(message, "error", duration)
}

function showWarningAlert(message, duration = 3000) {
    return showAlert(message, "warning", duration)
}

function showInfoAlert(message, duration = 3000) {
    return showAlert(message, "info", duration)
}

// 기존 alert 함수 오버라이드 (선택 사항)
// 주의: 이 코드는 기본 alert를 완전히 대체합니다
const originalAlert = window.alert
window.alert = (message) => {
    showAlert(message, "info")
}
