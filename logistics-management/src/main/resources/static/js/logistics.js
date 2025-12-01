const API_BASE = '';

let cachedProducts = [];
let cachedWarehouses = [];
let cachedStocks = [];

let cachedInactiveProducts = [];
let cachedInactiveWarehouses = [];


// 날짜/시간 포맷 함수
function formatDateTime(value) {
    if (!value) return '';

    try {
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            // 파싱이 안 되면 원래 문자열 그대로
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

function setMessage(text, isError = false) {
    const msg = document.getElementById('message');
    msg.textContent = text;
    if (!text) {
        msg.className = '';
        return;
    }
    msg.className = isError ? 'error' : 'success';
}

// ===== 상품 관련 =====
async function createProduct() {
    setMessage('');

    const code = document.getElementById('productCode').value.trim();
    const name = document.getElementById('productName').value.trim();
    const unit = document.getElementById('productUnit').value.trim();

    if (!code || !name || !unit) {
        setMessage('상품 코드, 이름, 단위를 모두 입력해주세요.', true);
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/products', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({code, name, unit})
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('상품 등록 실패: ' + text);
        }

        const data = await res.json();
        setMessage('상품이 등록되었습니다. ID=' + data.id);

        document.getElementById('productCode').value = '';
        document.getElementById('productName').value = '';
        document.getElementById('productUnit').value = '';

        await loadProducts();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function loadProducts() {
    try {
        const res = await fetch(API_BASE + '/api/products');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('상품 조회 실패: ' + text);
        }

        const products = await res.json();
        cachedProducts = products;

        renderProductTable(products);
        populateProductSelects();
        populateStockProductFilter();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

function renderProductTable(products) {
    const tbody = document.querySelector('#productTable tbody');
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
                <button onclick="deleteProduct(${p.id})">삭제</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function filterProducts() {
    const keyword = document.getElementById('productSearch').value.trim().toLowerCase();
    const filtered = cachedProducts.filter(p =>
        p.code.toLowerCase().includes(keyword) ||
        p.name.toLowerCase().includes(keyword)
    );
    renderProductTable(filtered);
}

// ===== 창고 관련 =====
async function createWarehouse() {
    setMessage('');

    const code = document.getElementById('warehouseCode').value.trim();
    const name = document.getElementById('warehouseName').value.trim();
    const location = document.getElementById('warehouseLocation').value.trim();
    const description = document.getElementById('warehouseDesc').value.trim();

    if (!code || !name) {
        setMessage('창고 코드와 이름은 필수입니다.', true);
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/warehouses', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({code, name, location, description})
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('창고 등록 실패: ' + text);
        }

        const data = await res.json();
        setMessage('창고가 등록되었습니다. ID=' + data.id);

        document.getElementById('warehouseCode').value = '';
        document.getElementById('warehouseName').value = '';
        document.getElementById('warehouseLocation').value = '';
        document.getElementById('warehouseDesc').value = '';

        await loadWarehouses();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function loadWarehouses() {
    try {
        const res = await fetch(API_BASE + '/api/warehouses');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('창고 조회 실패: ' + text);
        }

        const warehouses = await res.json();
        cachedWarehouses = warehouses;

        renderWarehouseTable(warehouses);
        populateWarehouseSelects();
        populateStockWarehouseFilter();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

function renderWarehouseTable(warehouses) {
    const tbody = document.querySelector('#warehouseTable tbody');
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
            <td>${formatDateTime(w.createdAt)}</td>
            <td>
                <button onclick="deleteWarehouse(${w.id})">삭제</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function filterWarehouses() {
    const keyword = document.getElementById('warehouseSearch').value.trim().toLowerCase();
    const filtered = cachedWarehouses.filter(w =>
        w.code.toLowerCase().includes(keyword) ||
        w.name.toLowerCase().includes(keyword)
    );
    renderWarehouseTable(filtered);
}

// ===== 재고 관련 =====
async function loadStocks() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/stocks');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('재고 조회 실패: ' + text);
        }

        const stocks = await res.json();
        cachedStocks = stocks;

        renderStockTable(stocks);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

function renderStockTable(stocks) {
    const tbody = document.querySelector('#stockTable tbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    stocks.forEach(s => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${s.id}</td>
            <td>${s.warehouseCode}</td>
            <td>${s.warehouseName}</td>
            <td>${s.productCode}</td>
            <td>${s.productName}</td>
            <td>${s.quantity}</td>
        `;
        tbody.appendChild(tr);
    });
}

function filterStocks() {
    const wFilter = document.getElementById('stockWarehouseFilter').value;
    const pFilter = document.getElementById('stockProductFilter').value;

    const filtered = cachedStocks.filter(s => {
        const warehouseOk = !wFilter || String(s.warehouseId) === wFilter || s.warehouseCode === wFilter;
        const productOk = !pFilter || String(s.productId) === pFilter || s.productCode === pFilter;
        return warehouseOk && productOk;
    });

    renderStockTable(filtered);
}

// ===== 입고 처리 =====
async function createInbound() {
    setMessage('');

    const warehouseId = document.getElementById('inboundWarehouseSelect').value;
    const productId = document.getElementById('inboundProductSelect').value;
    const quantityStr = document.getElementById('inboundQuantity').value;
    const remark = document.getElementById('inboundRemark').value.trim();

    const quantity = parseInt(quantityStr, 10);

    if (!warehouseId || !productId || !quantity || quantity <= 0) {
        setMessage('입고 창고, 상품, 수량을 올바르게 입력해주세요.', true);
        return;
    }

    const body = {
        warehouseId: Number(warehouseId),
        remark: remark,
        items: [
            {
                productId: Number(productId),
                quantity: quantity
            }
        ]
    };

    try {
        const res = await fetch(API_BASE + '/api/inbounds', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('입고 등록 실패: ' + text);
        }

        const data = await res.json();
        setMessage('입고가 등록되었습니다. ID=' + data.id);

        document.getElementById('inboundQuantity').value = 1;
        document.getElementById('inboundRemark').value = '';

        await loadStocks();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

// ===== 출고 처리 =====
async function createOutbound() {
    setMessage('');

    const warehouseId = document.getElementById('outboundWarehouseSelect').value;
    const productId = document.getElementById('outboundProductSelect').value;
    const quantityStr = document.getElementById('outboundQuantity').value;
    const customerName = document.getElementById('outboundCustomer').value.trim();
    const remark = document.getElementById('outboundRemark').value.trim();

    const quantity = parseInt(quantityStr, 10);

    if (!warehouseId || !productId || !quantity || quantity <= 0) {
        setMessage('출고 창고, 상품, 수량을 올바르게 입력해주세요.', true);
        return;
    }

    const body = {
        warehouseId: Number(warehouseId),
        customerName: customerName,
        remark: remark,
        items: [
            {
                productId: Number(productId),
                quantity: quantity
            }
        ]
    };

    try {
        const res = await fetch(API_BASE + '/api/outbounds', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('출고 등록 실패: ' + text);
        }

        const data = await res.json();
        setMessage('출고가 등록되었습니다. ID=' + data.id);

        document.getElementById('outboundQuantity').value = 1;
        document.getElementById('outboundCustomer').value = '';
        document.getElementById('outboundRemark').value = '';

        await loadStocks();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

// ===== 입출고 내역 조회 =====
async function loadInbounds() {
    setMessage('');
    try {
        const from = document.getElementById('inboundFrom').value;
        const to = document.getElementById('inboundTo').value;

        let url = API_BASE + '/api/inbounds';
        const params = [];
        if (from) params.push('from=' + from);
        if (to) params.push('to=' + to);
        if (params.length > 0) {
            url += '?' + params.join('&');
        }

        const res = await fetch(url);
        if (!res.ok) {
            const text = await res.text();
            throw new Error('입고 내역 조회 실패: ' + text);
        }

        const list = await res.json();
        const tbody = document.querySelector('#inboundTable tbody');
        tbody.innerHTML = '';

        list.forEach(i => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${i.id}</td>
                <td>${i.warehouseCode}</td>
                <td>${i.warehouseName}</td>
                <td>${formatDateTime(i.inboundDate)}</td>
                <td>${i.remark ?? ''}</td>
                <td>${i.itemCount}</td>
                <td>${i.totalQuantity}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function loadOutbounds() {
    setMessage('');
    try {
        const from = document.getElementById('outboundFrom').value;
        const to = document.getElementById('outboundTo').value;

        let url = API_BASE + '/api/outbounds';
        const params = [];
        if (from) params.push('from=' + from);
        if (to) params.push('to=' + to);
        if (params.length > 0) {
            url += '?' + params.join('&');
        }

        const res = await fetch(url);
        if (!res.ok) {
            const text = await res.text();
            throw new Error('출고 내역 조회 실패: ' + text);
        }

        const list = await res.json();
        const tbody = document.querySelector('#outboundTable tbody');
        tbody.innerHTML = '';

        list.forEach(o => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${o.id}</td>
                <td>${o.warehouseCode}</td>
                <td>${o.warehouseName}</td>
                <td>${formatDateTime(o.outboundDate)}</td>
                <td>${o.customerName ?? ''}</td>
                <td>${o.remark ?? ''}</td>
                <td>${o.itemCount}</td>
                <td>${o.totalQuantity}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

// ===== 셀렉트/필터 채우기 =====
function populateProductSelects() {
    const inboundSelect = document.getElementById('inboundProductSelect');
    const outboundSelect = document.getElementById('outboundProductSelect');

    // 이 페이지에 입고/출고 상품 셀렉트가 아예 없으면 아무 것도 안 함
    if (!inboundSelect && !outboundSelect) {
        return;
    }

    if (inboundSelect) {
        inboundSelect.innerHTML = '';
    }
    if (outboundSelect) {
        outboundSelect.innerHTML = '';
    }

    cachedProducts.forEach(p => {
        const label = `${p.code} - ${p.name}`;

        const option1 = document.createElement('option');
        option1.value = p.id;
        option1.textContent = label;
        inboundSelect.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = p.id;
        option2.textContent = label;
        outboundSelect.appendChild(option2);
    });
}

function populateWarehouseSelects() {
    const inboundSelect = document.getElementById('inboundWarehouseSelect');
    const outboundSelect = document.getElementById('outboundWarehouseSelect');

    if (!inboundSelect && !outboundSelect) {
        return;
    }

    if (inboundSelect) {
        inboundSelect.innerHTML = '';
    }
    if (outboundSelect) {
        outboundSelect.innerHTML = '';
    }

    cachedWarehouses.forEach(w => {
        const label = `${w.code} - ${w.name}`;

        const option1 = document.createElement('option');
        option1.value = w.id;
        option1.textContent = label;
        inboundSelect.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = w.id;
        option2.textContent = label;
        outboundSelect.appendChild(option2);
    });
}

function populateStockWarehouseFilter() {
    const select = document.getElementById('stockWarehouseFilter');

    // 이 페이지에 재고 창고 필터 없으면 스킵
    if (!select) return;

    const current = select.value;
    select.innerHTML = '<option value="">전체</option>';

    cachedWarehouses.forEach(w => {
        const option = document.createElement('option');
        option.value = String(w.id);
        option.textContent = `${w.code} - ${w.name}`;
        select.appendChild(option);
    });

    if (current) {
        select.value = current;
    }
}

function populateStockProductFilter() {
    const select = document.getElementById('stockProductFilter');

    // 재고 상품 필터 없으면 스킵
    if (!select) return;

    const current = select.value;
    select.innerHTML = '<option value="">전체</option>';

    cachedProducts.forEach(p => {
        const option = document.createElement('option');
        option.value = String(p.id);
        option.textContent = `${p.code} - ${p.name}`;
        select.appendChild(option);
    });

    if (current) {
        select.value = current;
    }
}

// 페이지 로드시 기본 데이터 로드
window.onload = () => {
    const hasProductSection   = document.getElementById('productSection')   !== null;
    const hasWarehouseSection = document.getElementById('warehouseSection') !== null;
    const hasInboundSection   = document.getElementById('inboundSection')   !== null;
    const hasOutboundSection  = document.getElementById('outboundSection')  !== null;
    const hasStockSection     = document.getElementById('stockSection')     !== null;
    const hasHistorySection   = document.getElementById('historySection')   !== null;

    // 상품 데이터 필요:
    // - 상품 화면
    // - 입고/출고 화면(상품 선택 셀렉트)
    // - 재고 화면(상품 필터)
    if (hasProductSection || hasInboundSection || hasOutboundSection || hasStockSection) {
        loadProducts();
    }

    // 창고 데이터 필요:
    // - 창고 화면
    // - 입고/출고 화면(창고 셀렉트)
    // - 재고 화면(창고 필터)
    if (hasWarehouseSection || hasInboundSection || hasOutboundSection || hasStockSection) {
        loadWarehouses();
    }

    // 재고 화면에서만 재고 로딩
    if (hasStockSection) {
        loadStocks();
    }

    // 입·출고 내역 화면에서만 내역 로딩
    if (hasHistorySection) {
        loadInbounds();
        loadOutbounds();
    }
};


async function deleteProduct(id) {
    if (!confirm('정말 이 상품을 삭제(미사용 처리) 하시겠습니까?')) {
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/products/' + id, {
            method: 'DELETE'
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('상품 삭제 실패: ' + text);
        }

        setMessage('상품이 삭제(미사용 처리)되었습니다.');
        await loadProducts();   // 목록/셀렉트 갱신
        await loadStocks();     // 재고 화면도 같이 갱신
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

async function deleteWarehouse(id) {
    if (!confirm('정말 이 창고를 삭제(미사용 처리) 하시겠습니까?\n' +
        '※ 해당 창고에 입출고 이력이 있더라도, 이력은 남고 앞으로만 안 쓰이게 됩니다.')) {
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/warehouses/' + id, {
            method: 'DELETE'
        });

        if (!res.ok) {
            const text = await res.text();
            throw new Error('창고 삭제 실패: ' + text);
        }

        setMessage('창고가 삭제(미사용 처리)되었습니다.');
        await loadWarehouses(); // 목록/셀렉트 갱신
        await loadStocks();     // 재고 갱신
        await loadInbounds();   // 입고 내역 갱신(선택지에서 빠지도록)
        await loadOutbounds();  // 출고 내역도 갱신
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}


// ===== 비활성 상품 관련 =====
async function loadInactiveProducts() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/products/inactive');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('비활성 상품 조회 실패: ' + text);
        }

        const products = await res.json();
        cachedInactiveProducts = products;
        renderInactiveProductTable(products);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}

function renderInactiveProductTable(products) {
    const tbody = document.querySelector('#inactiveProductTable tbody');
    if (!tbody) return; // 아직 섹션이 없으면 그냥 무시
    tbody.innerHTML = '';

    products.forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${p.id}</td>
            <td>${p.code}</td>
            <td>${p.name}</td>
            <td>${p.unit}</td>
            <td>${p.status}</td>
            <td>${p.createdAt ?? ''}</td>
            <td>
                <button onclick="activateProduct(${p.id})">복구</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
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
        await loadProducts();          // ACTIVE 목록 갱신
        await loadInactiveProducts();  // 비활성 목록 갱신
        await loadStocks();            // 재고도 갱신
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}


// ===== 비활성 창고 관련 =====
async function loadInactiveWarehouses() {
    setMessage('');
    try {
        const res = await fetch(API_BASE + '/api/warehouses/inactive');
        if (!res.ok) {
            const text = await res.text();
            throw new Error('비활성 창고 조회 실패: ' + text);
        }

        const warehouses = await res.json();
        cachedInactiveWarehouses = warehouses;
        renderInactiveWarehouseTable(warehouses);
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
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
            <td>${w.createdAt ?? ''}</td>
            <td>
                <button onclick="activateWarehouse(${w.id})">복구</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
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
        await loadWarehouses();           // ACTIVE 목록 갱신
        await loadInactiveWarehouses();   // 비활성 목록 갱신
        await loadStocks();               // 재고, 입출고 선택지 등 갱신
        await loadInbounds();
        await loadOutbounds();
    } catch (e) {
        console.error(e);
        setMessage(e.message, true);
    }
}
