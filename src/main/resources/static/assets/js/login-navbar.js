document.addEventListener('DOMContentLoaded', () => {
    fetch('/assets/navbar.html')
        .then(res => res.text())
        .then(html => {
            document.getElementById('navbar').innerHTML = html;
            renderNavButtons && renderNavButtons();
        })
        .catch(err => console.error('navbar 로딩 실패:', err));
});

async function renderNavButtons() {
    const navBtns = document.getElementById('nav-auth-buttons');
    if (!navBtns) return;
    try {
        const res = await fetch('/api/user/me', {credentials: 'include'});
        if (res.ok) {
            // 로그인 상태
            navBtns.innerHTML = `
                <a href="mypage.html" class="btn btn-ghost">마이페이지</a>
                <button id="logout-btn" class="btn btn-primary">로그아웃</button>
            `;
            document.getElementById('logout-btn').onclick = async function () {
                await fetch('/api/auth/logout', {method: 'POST', credentials: 'include'});
                location.href = "login.html";
            };
        } else {
            // 비로그인 상태
            navBtns.innerHTML = `
                <a href="login.html" class="btn btn-ghost">로그인</a>
                <a href="signup.html" class="btn btn-primary">시작하기</a>
            `;
        }
    } catch {
        navBtns.innerHTML = `
            <a href="login.html" class="btn btn-ghost">로그인</a>
            <a href="signup.html" class="btn btn-primary">시작하기</a>
        `;
    }
}