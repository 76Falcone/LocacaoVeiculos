/* ═══════════════════════════════════════════════════════════
   LISTARVEICULOS.JS — Lógica da listagem de veículos
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Dados mockados (substituir por fetch ao backend futuramente) ───
  const veiculos = [
    { id: 1, placa: 'ABC-1D23', modelo: 'Toyota Corolla', cor: 'Preto', valorDiaria: 179.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 2, placa: 'DEF-2E45', modelo: 'Honda Civic', cor: 'Prata', valorDiaria: 169.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 3, placa: 'GHI-3F67', modelo: 'Renault Kwid', cor: 'Branco', valorDiaria: 79.00, funcionalidade: 'Passeio', disponibilidade: false, arCondicionado: false, tipoCambio: 'Manual' },
    { id: 4, placa: 'JKL-4G89', modelo: 'Jeep Compass', cor: 'Cinza', valorDiaria: 219.00, funcionalidade: 'Utilitário', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 5, placa: 'MNO-5H01', modelo: 'Volkswagen Polo', cor: 'Vermelho', valorDiaria: 98.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 6, placa: 'PQR-6I23', modelo: 'Fiat Mobi', cor: 'Azul', valorDiaria: 72.00, funcionalidade: 'Trabalho', disponibilidade: false, arCondicionado: false, tipoCambio: 'Manual' },
    { id: 7, placa: 'STU-7J45', modelo: 'BMW 320i', cor: 'Preto', valorDiaria: 349.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 8, placa: 'VWX-8K67', modelo: 'Hyundai Creta', cor: 'Branco', valorDiaria: 189.00, funcionalidade: 'Utilitário', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 9, placa: 'YZA-9L89', modelo: 'Chevrolet Onix', cor: 'Prata', valorDiaria: 89.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 10, placa: 'BCD-0M01', modelo: 'Nissan Leaf', cor: 'Verde', valorDiaria: 199.00, funcionalidade: 'Passeio', disponibilidade: false, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 11, placa: 'EFG-1N23', modelo: 'Fiat Argo', cor: 'Vermelho', valorDiaria: 85.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Manual' },
    { id: 12, placa: 'HIJ-2O45', modelo: 'Chevrolet Tracker', cor: 'Cinza', valorDiaria: 205.00, funcionalidade: 'Utilitário', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 13, placa: 'KLM-3P67', modelo: 'Toyota Hilux', cor: 'Branco', valorDiaria: 289.00, funcionalidade: 'Trabalho', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 14, placa: 'NOP-4Q89', modelo: 'Volkswagen T-Cross', cor: 'Azul', valorDiaria: 175.00, funcionalidade: 'Passeio', disponibilidade: false, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 15, placa: 'QRS-5R01', modelo: 'Honda HRV', cor: 'Prata', valorDiaria: 210.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 16, placa: 'TUV-6S23', modelo: 'Fiat Strada', cor: 'Branco', valorDiaria: 135.00, funcionalidade: 'Trabalho', disponibilidade: true, arCondicionado: false, tipoCambio: 'Manual' },
    { id: 17, placa: 'WXY-7T45', modelo: 'Renault Duster', cor: 'Cinza', valorDiaria: 155.00, funcionalidade: 'Utilitário', disponibilidade: true, arCondicionado: true, tipoCambio: 'Manual' },
    { id: 18, placa: 'ZAB-8U67', modelo: 'Mercedes C200', cor: 'Preto', valorDiaria: 420.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 19, placa: 'CDE-9V89', modelo: 'Audi A3', cor: 'Branco', valorDiaria: 380.00, funcionalidade: 'Passeio', disponibilidade: false, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 20, placa: 'FGH-0W01', modelo: 'Hyundai HB20', cor: 'Vermelho', valorDiaria: 78.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Manual' },
    { id: 21, placa: 'IJK-1X23', modelo: 'Toyota Yaris', cor: 'Prata', valorDiaria: 95.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 22, placa: 'LMN-2Y45', modelo: 'Ford Ranger', cor: 'Preto', valorDiaria: 310.00, funcionalidade: 'Trabalho', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 23, placa: 'OPQ-3Z67', modelo: 'Chevrolet S10', cor: 'Branco', valorDiaria: 275.00, funcionalidade: 'Trabalho', disponibilidade: false, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 24, placa: 'RST-4A89', modelo: 'Fiat Toro', cor: 'Bronze', valorDiaria: 225.00, funcionalidade: 'Utilitário', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
    { id: 25, placa: 'UVW-5B01', modelo: 'Jeep Renegade', cor: 'Verde', valorDiaria: 195.00, funcionalidade: 'Passeio', disponibilidade: true, arCondicionado: true, tipoCambio: 'Automático' },
  ];

  // ─── Configuração de paginação ───
  const ITEMS_PER_PAGE = 15;
  let currentPage = 1;

  // ─── Elementos do DOM ───
  const tableBody = document.getElementById('veiculosBody');
  const resultCount = document.getElementById('resultCount');
  const emptyState = document.getElementById('emptyState');
  const tableEl = document.getElementById('veiculosTable');
  const pagination = document.getElementById('pagination');

  // Filtros por coluna
  const filterId = document.getElementById('filterId');
  const filterPlaca = document.getElementById('filterPlaca');
  const filterModelo = document.getElementById('filterModelo');
  const filterCor = document.getElementById('filterCor');
  const filterValor = document.getElementById('filterValor');
  const filterFunc = document.getElementById('filterFunc');
  const filterDisp = document.getElementById('filterDisp');
  const filterAr = document.getElementById('filterAr');
  const filterCambio = document.getElementById('filterCambio');

  // Sidebar mobile
  const menuToggle = document.getElementById('menuToggle');
  const sidebar = document.getElementById('sidebar');
  const sidebarOverlay = document.getElementById('sidebarOverlay');

  // Estado de ordenação
  let sortCol = 'id';
  let sortAsc = true;

  // ─── Sidebar mobile toggle ───
  if (menuToggle) {
    menuToggle.addEventListener('click', () => {
      sidebar.classList.toggle('open');
      sidebarOverlay.classList.toggle('active');
    });

    sidebarOverlay.addEventListener('click', () => {
      sidebar.classList.remove('open');
      sidebarOverlay.classList.remove('active');
    });
  }

  // ─── Eventos de filtro (resetar para página 1 ao filtrar) ───
  const allInputFilters = [filterId, filterPlaca, filterModelo, filterCor, filterValor, filterFunc];
  const allSelectFilters = [filterDisp, filterAr, filterCambio];

  allInputFilters.forEach(input => {
    input.addEventListener('input', () => { currentPage = 1; renderTable(); });
  });

  allSelectFilters.forEach(select => {
    select.addEventListener('change', () => { currentPage = 1; renderTable(); });
  });

  // ─── Ordenação por coluna ───
  document.querySelectorAll('th.sortable').forEach(th => {
    th.addEventListener('click', () => {
      const col = th.dataset.col;
      if (sortCol === col) {
        sortAsc = !sortAsc;
      } else {
        sortCol = col;
        sortAsc = true;
      }
      renderTable();
    });
  });

  // ─── Obter dados filtrados e ordenados ───
  function getFilteredData() {
    const fId = filterId.value.trim().toLowerCase();
    const fPlaca = filterPlaca.value.trim().toLowerCase();
    const fModelo = filterModelo.value.trim().toLowerCase();
    const fCor = filterCor.value.trim().toLowerCase();
    const fValor = filterValor.value.trim();
    const fFunc = filterFunc.value.trim().toLowerCase();
    const fDisp = filterDisp.value;
    const fAr = filterAr.value;
    const fCambio = filterCambio.value;

    let filtered = veiculos.filter(v => {
      if (fId && !String(v.id).includes(fId)) return false;
      if (fPlaca && !v.placa.toLowerCase().includes(fPlaca)) return false;
      if (fModelo && !v.modelo.toLowerCase().includes(fModelo)) return false;
      if (fCor && !v.cor.toLowerCase().includes(fCor)) return false;
      if (fValor && !String(v.valorDiaria.toFixed(2)).includes(fValor)) return false;
      if (fFunc && !v.funcionalidade.toLowerCase().includes(fFunc)) return false;
      if (fDisp && String(v.disponibilidade) !== fDisp) return false;
      if (fAr && String(v.arCondicionado) !== fAr) return false;
      if (fCambio && v.tipoCambio !== fCambio) return false;
      return true;
    });

    filtered.sort((a, b) => {
      let valA = a[sortCol];
      let valB = b[sortCol];
      if (typeof valA === 'string') { valA = valA.toLowerCase(); valB = valB.toLowerCase(); }
      if (valA < valB) return sortAsc ? -1 : 1;
      if (valA > valB) return sortAsc ? 1 : -1;
      return 0;
    });

    return filtered;
  }

  // ─── Renderizar tabela com paginação ───
  function renderTable() {
    const filtered = getFilteredData();
    const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE) || 1;

    // Corrigir página atual
    if (currentPage > totalPages) currentPage = totalPages;

    const start = (currentPage - 1) * ITEMS_PER_PAGE;
    const end = start + ITEMS_PER_PAGE;
    const pageData = filtered.slice(start, end);

    // Atualizar contador
    resultCount.innerHTML = `Exibindo <strong>${start + 1}–${Math.min(end, filtered.length)}</strong> de <strong>${filtered.length}</strong> veículo${filtered.length !== 1 ? 's' : ''}`;

    // Renderizar linhas
    if (filtered.length === 0) {
      tableBody.innerHTML = '';
      emptyState.style.display = 'block';
      pagination.innerHTML = '';
      return;
    }

    emptyState.style.display = 'none';

    tableBody.innerHTML = pageData.map((v, i) => `
      <tr class="table-row" style="animation-delay: ${i * 0.03}s">
        <td><span class="id-badge">${v.id}</span></td>
        <td><span class="placa-badge">${v.placa}</span></td>
        <td class="modelo-cell">${v.modelo}</td>
        <td><span class="cor-dot" style="background:${getColorHex(v.cor)}"></span>${v.cor}</td>
        <td class="valor-cell">R$ ${v.valorDiaria.toFixed(2)}</td>
        <td>${v.funcionalidade}</td>
        <td>
          <span class="status-badge ${v.disponibilidade ? 'available' : 'unavailable'}">
            <span class="status-badge-dot"></span>
            ${v.disponibilidade ? 'Disponível' : 'Indisponível'}
          </span>
        </td>
        <td>
          <span class="bool-badge ${v.arCondicionado ? 'yes' : 'no'}">
            ${v.arCondicionado ? '✓ Sim' : '✗ Não'}
          </span>
        </td>
        <td><span class="cambio-badge ${v.tipoCambio === 'Automático' ? 'auto' : 'manual'}">${v.tipoCambio}</span></td>
        <td>
          <div class="action-buttons">
            <button class="btn-action btn-edit" title="Editar" onclick="window.location.href='editarVeiculo.html?id=${v.id}&placa=${encodeURIComponent(v.placa)}&modelo=${encodeURIComponent(v.modelo)}&cor=${encodeURIComponent(v.cor)}&valorDiaria=${v.valorDiaria}&funcionalidade=${encodeURIComponent(v.funcionalidade)}&disponibilidade=${v.disponibilidade}&arCondicionado=${v.arCondicionado}&tipoCambio=${encodeURIComponent(v.tipoCambio)}'">✏️</button>
            <button class="btn-action btn-delete" title="Deletar" onclick="confirmarExclusao(${v.id}, '${v.modelo}')">🗑️</button>
          </div>
        </td>
      </tr>
    `).join('');

    // Renderizar paginação
    renderPagination(totalPages, filtered.length);
  }

  // ─── Renderizar controles de paginação ───
  function renderPagination(totalPages, totalItems) {
    if (totalPages <= 1) {
      pagination.innerHTML = '';
      return;
    }

    let html = '<div class="pagination-wrap">';

    // Info
    html += `<span class="pagination-info">Página <strong>${currentPage}</strong> de <strong>${totalPages}</strong></span>`;

    // Botões
    html += '<div class="pagination-buttons">';

    // Anterior
    html += `<button class="page-btn ${currentPage === 1 ? 'disabled' : ''}" data-page="${currentPage - 1}" ${currentPage === 1 ? 'disabled' : ''}>‹ Anterior</button>`;

    // Números
    const range = getPageRange(currentPage, totalPages);
    range.forEach(p => {
      if (p === '...') {
        html += '<span class="page-ellipsis">…</span>';
      } else {
        html += `<button class="page-btn page-num ${p === currentPage ? 'active' : ''}" data-page="${p}">${p}</button>`;
      }
    });

    // Próximo
    html += `<button class="page-btn ${currentPage === totalPages ? 'disabled' : ''}" data-page="${currentPage + 1}" ${currentPage === totalPages ? 'disabled' : ''}>Próximo ›</button>`;

    html += '</div></div>';

    pagination.innerHTML = html;

    // Eventos de clique na paginação
    pagination.querySelectorAll('.page-btn:not(.disabled)').forEach(btn => {
      btn.addEventListener('click', () => {
        currentPage = parseInt(btn.dataset.page);
        renderTable();
        // Scroll suave para o topo da tabela
        document.querySelector('.data-table-wrap').scrollIntoView({ behavior: 'smooth', block: 'start' });
      });
    });
  }

  // ─── Calcular range de páginas visíveis ───
  function getPageRange(current, total) {
    if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1);

    if (current <= 3) return [1, 2, 3, 4, '...', total];
    if (current >= total - 2) return [1, '...', total - 3, total - 2, total - 1, total];
    return [1, '...', current - 1, current, current + 1, '...', total];
  }

  // ─── Helper: cor → hex para o dot ───
  function getColorHex(cor) {
    const map = {
      'preto': '#2D2D2D', 'prata': '#C0C0C0', 'branco': '#E8E8E8',
      'cinza': '#8B8B8B', 'vermelho': '#E63946', 'azul': '#457B9D',
      'verde': '#2D6A4F', 'bronze': '#CD7F32', 'amarelo': '#F4D35E'
    };
    return map[cor.toLowerCase()] || '#A78BFA';
  }

  // ─── Render inicial ───
  renderTable();
});
