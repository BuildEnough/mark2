const API_BASE = '';

let activeProducts = [];
let inactiveProducts = [];
let activeWarehouses = [];
let inactiveWarehouses = [];

function setMessage(text, isError = false) {
    const msg = document.getElementById('message');
    if (!msg) return;

    msg.textContent = text || '';
    if (!text) {
        msg.className = '';
        return;
    }
    msg.className = isError ? 'error' : 'success';
}

// 날짜/시간 포맷 함수
function formatDateTime(value) {
    if (!value) return '';

    try {
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            // 파싱이 안 되면 원본 그대로
            return value;
        }

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hour = String(date.getHours()).padStart(2, '0');
        const minute = String(date.getMinutes()).padStart(2, '0');
        const second = String(date.getSeconds()).padStart(2, '0');

        return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분 ${second}초`;
    } catch (e) {
        console.error('날짜 포맷 오류:', e);
        return value;
    }
}

/* =========================
 *  상품 활성/비활성 관리
 * ========================= */

async function loadActiveProducts() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/products');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('활성 상품 조회 실패: ' + text);
        }

        const list = await res.json();
        activeProducts = list;
        renderActiveProductTable(list);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function loadInactiveProducts() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/products/inactive');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('비활성 상품 조회 실패: ' + text);
        }

        const list = await res.json();
        inactiveProducts = list;
        renderInactiveProductTable(list);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

function renderActiveProductTable(products) {
    const tbody = document.querySelector('#activeProductTable tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    products.forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${p.id}</td>
            <td>${p.code}</td>
            <td>${p.name}</td>
            <td>${p.unit}</td>
            <td>${p.status}</td>
            <td>${formatDateTime(p.createdAt)}</td>
            <td>
                <button onclick="softDeleteProduct(${p.id})">비활성화</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function renderInactiveProductTable(products) {
    const tbody = document.querySelector('#inactiveProductTable tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    products.forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${p.id}</td>
            <td>${p.code}</td>
            <td>${p.name}</td>
            <td>${p.unit}</td>
            <td>${p.status}</td>
            <td>${formatDateTime(p.createdAt)}</td>
            <td>
                <button onclick="activateProduct(${p.id})">복구</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function softDeleteProduct(id) {
    if (!confirm(
        '이 상품을 비활성화하시겠습니까?\n' +
        '※ 기존 입출고 이력은 유지되고, 앞으로만 선택 목록에서 제외됩니다.'
    )) {
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/products/' + id, {
            method: 'DELETE'
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('상품 비활성화 실패: ' + text);
        }

        setMessage('상품이 비활성화되었습니다.');
        await loadActiveProducts();
        await loadInactiveProducts();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function activateProduct(id) {
    if (!confirm('이 상품을 다시 활성화하시겠습니까?')) {
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/products/' + id + '/activate', {
            method: 'PATCH'
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('상품 복구 실패: ' + text);
        }

        setMessage('상품이 다시 활성화되었습니다.');
        await loadActiveProducts();
        await loadInactiveProducts();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

/* =========================
 *  창고 활성/비활성 관리
 * ========================= */

async function loadActiveWarehouses() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/warehouses');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('활성 창고 조회 실패: ' + text);
        }

        const list = await res.json();
        activeWarehouses = list;
        renderActiveWarehouseTable(list);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function loadInactiveWarehouses() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/warehouses/inactive');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('비활성 창고 조회 실패: ' + text);
        }

        const list = await res.json();
        inactiveWarehouses = list;
        renderInactiveWarehouseTable(list);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

function renderActiveWarehouseTable(warehouses) {
    const tbody = document.querySelector('#activeWarehouseTable tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    warehouses.forEach(w => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${w.id}</td>
            <td>${w.code}</td>
            <td>${w.name}</td>
            <td>${w.location ?? ''}</td>
            <td>${w.description ?? ''}</td>
            <td>${w.status ?? ''}</td>
            <td>${formatDateTime(w.createdAt)}</td>
            <td>
                <button onclick="softDeleteWarehouse(${w.id})">비활성화</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function renderInactiveWarehouseTable(warehouses) {
    const tbody = document.querySelector('#inactiveWarehouseTable tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    warehouses.forEach(w => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${w.id}</td>
            <td>${w.code}</td>
            <td>${w.name}</td>
            <td>${w.location ?? ''}</td>
            <td>${w.description ?? ''}</td>
            <td>${w.status ?? ''}</td>
            <td>${formatDateTime(w.createdAt)}</td>
            <td>
                <button onclick="activateWarehouse(${w.id})">복구</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function softDeleteWarehouse(id) {
    if (!confirm(
        '이 창고를 비활성화하시겠습니까?\n' +
        '※ 기존 입출고/재고 이력은 유지되고, 앞으로만 선택 목록에서 제외됩니다.'
    )) {
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/warehouses/' + id, {
            method: 'DELETE'
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('창고 비활성화 실패: ' + text);
        }

        setMessage('창고가 비활성화되었습니다.');
        await loadActiveWarehouses();
        await loadInactiveWarehouses();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function activateWarehouse(id) {
    if (!confirm('이 창고를 다시 활성화하시겠습니까?')) {
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/warehouses/' + id + '/activate', {
            method: 'PATCH'
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('창고 복구 실패: ' + text);
        }

        setMessage('창고가 다시 활성화되었습니다.');
        await loadActiveWarehouses();
        await loadInactiveWarehouses();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

/* =========================
 *  초기 로딩
 * ========================= */

window.onload = () => {
    loadActiveProducts();
    loadInactiveProducts();
    loadActiveWarehouses();
    loadInactiveWarehouses();
};
