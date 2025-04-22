function renderPagination(totalItems, currentPage, pageSize, onPageChange) {
    const totalPages = Math.ceil(totalItems / pageSize);
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    // 如果总页数小于等于1，就不显示分页栏
    if (totalPages <= 1) return;

    const createPageItem = (page, label, isActive = false, disabled = false) => {
        const li = document.createElement('li');
        li.className = `page-item ${isActive ? 'active' : ''} ${disabled ? 'disabled' : ''}`;

        const a = document.createElement('a');
        a.className = 'page-link';
        a.href = '#';
        a.innerText = label;
        a.onclick = (e) => {
            e.preventDefault();
            if (!disabled && page !== currentPage) {
                onPageChange(page);
            }
        };

        li.appendChild(a);
        return li;
    };

    // 上一页按钮
    pagination.appendChild(createPageItem(currentPage - 1, '«', false, currentPage === 1));

    // 中间页码按钮（最多显示 5 个页码）
    const range = 2;
    const start = Math.max(1, currentPage - range);
    const end = Math.min(totalPages, currentPage + range);
    for (let i = start; i <= end; i++) {
        pagination.appendChild(createPageItem(i, i, i === currentPage));
    }

    // 下一页按钮
    pagination.appendChild(createPageItem(currentPage + 1, '»', false, currentPage === totalPages));
}
