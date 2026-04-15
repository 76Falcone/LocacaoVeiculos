/* ═══════════════════════════════════════════════════════════
   LISTARVEICULOS.JS — Lógica da listagem de veículos
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Dados carregados do backend ───
  let veiculos = [];

  // Buscar dados do banco de dados
  fetch('ControleVeiculo?op=LISTAR')
    .then(r => r.json())
    .then(data => { veiculos = data; renderTable(); })
    .catch(err => { console.error('Erro ao carregar veículos:', err); renderTable(); });

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

  // ─── Confirmar exclusão ───
  window.confirmarExclusao = function(id, modelo) {
    if (confirm(`Tem certeza que deseja excluir o veículo "${modelo}" (ID: ${id})?`)) {
      fetch(`ControleVeiculo?op=DELETAR&id=${id}`, { method: 'POST' })
        .then(() => { location.reload(); })
        .catch(err => { console.error(err); alert('Erro ao excluir veículo.'); });
    }
  };

  // ─── Render inicial ───
  renderTable();
});
