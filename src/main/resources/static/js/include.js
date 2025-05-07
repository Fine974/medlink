window.addEventListener('DOMContentLoaded', () => {
    includeHTML().then(setActiveNavLink); // ✅ 先加载组件，再设置激活状态
});

// 包含 HTML 组件
function includeHTML() {
    const elements = document.querySelectorAll('[data-include]');
    const promises = Array.from(elements).map(el => {
        const file = el.getAttribute('data-include');
        return fetch(file)
            .then(response => {
                if (!response.ok) throw new Error(`加载失败: ${file}`);
                return response.text();
            })
            .then(data => {
                el.innerHTML = data;
                return el; // 返回处理后的元素
            })
            .catch(error => {
                el.innerHTML = `<p class="text-danger text-center">无法加载组件：${file}</p>`;
                return el;
            });
    });
    return Promise.all(promises); // ⚡ 返回所有 Promise 的集合
}

// 自动设置导航栏激活状态
function setActiveNavLink() {
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        // 精确匹配路径（如 "/search.html"）
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
}