/* ═══════════════════════════════════════════════════════════
   LISTARRESERVAS.JS — Lógica da listagem de reservas
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Dados carregados do backend ───
  let reservas = [];

  // Buscar dados do banco de dados
  fetch('ControleLocacao?op=LISTAR')
    .then(r => r.json())
    .then(data => { reservas = data; renderTable(); })
    .catch(err => { console.error('Erro ao carregar reservas:', err); renderTable(); });

  // ─── Configuração de paginação ───
  const ITEMS_PER_PAGE = 15;
  let currentPage = 1;

  // ─── Elementos do DOM ───
  const tableBody   = document.getElementById('reservasBody');
  const resultCount = document.getElementById('resultCount');
  const emptyState  = document.getElementById('emptyState');
  const pagination  = document.getElementById('pagination');

  // Filtros por coluna
  const filterId       = document.getElementById('filterId');
  const filterUsuario  = document.getElementById('filterUsuario');
  const filterVeiculo  = document.getElementById('filterVeiculo');
  const filterDias     = document.getElementById('filterDias');
  const filterLocal    = document.getElementById('filterLocal');
  const filterRetirada = document.getElementById('filterRetirada');
  const filterEntrega  = document.getElementById('filterEntrega');
  const filterTotal    = document.getElementById('filterTotal');

  // Sidebar mobile
  const menuToggle     = document.getElementById('menuToggle');
  const sidebar        = document.getElementById('sidebar');
  const sidebarOverlay = document.getElementById('sidebarOverlay');

  // Estado de ordenação
  let sortCol = 'idLocacao';
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

  // ─── Eventos de filtro ───
  const allFilters = [filterId, filterUsuario, filterVeiculo, filterDias, filterLocal, filterRetirada, filterEntrega, filterTotal];

  allFilters.forEach(input => {
    input.addEventListener('input', () => { currentPage = 1; renderTable(); });
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

  // ─── Formatar data BR ───
  function formatDate(dateStr) {
    if (!dateStr) return '—';
    const parts = dateStr.split('-');
    return `${parts[2]}/${parts[1]}/${parts[0]}`;
  }

  // ─── Obter dados filtrados e ordenados ───
  function getFilteredData() {
    const fId       = filterId.value.trim().toLowerCase();
    const fUsuario  = filterUsuario.value.trim().toLowerCase();
    const fVeiculo  = filterVeiculo.value.trim().toLowerCase();
    const fDias     = filterDias.value.trim();
    const fLocal    = filterLocal.value.trim().toLowerCase();
    const fRetirada = filterRetirada.value.trim();
    const fEntrega  = filterEntrega.value.trim();
    const fTotal    = filterTotal.value.trim();

    let filtered = reservas.filter(r => {
      if (fId && !String(r.idLocacao).includes(fId)) return false;
      if (fUsuario && !r.nomeUsuario.toLowerCase().includes(fUsuario)) return false;
      if (fVeiculo && !r.modeloVeiculo.toLowerCase().includes(fVeiculo)) return false;
      if (fDias && !String(r.qtdDias).includes(fDias)) return false;
      if (fLocal && !r.localRetirada.toLowerCase().includes(fLocal)) return false;
      if (fRetirada && !r.dataRetirada.includes(fRetirada) && !formatDate(r.dataRetirada).includes(fRetirada)) return false;
      if (fEntrega && !r.dataEntrega.includes(fEntrega) && !formatDate(r.dataEntrega).includes(fEntrega)) return false;
      if (fTotal && !String(r.valorTotal.toFixed(2)).includes(fTotal)) return false;
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

    if (currentPage > totalPages) currentPage = totalPages;

    const start = (currentPage - 1) * ITEMS_PER_PAGE;
    const end = start + ITEMS_PER_PAGE;
    const pageData = filtered.slice(start, end);

    // Atualizar contador
    resultCount.innerHTML = `Exibindo <strong>${start + 1}–${Math.min(end, filtered.length)}</strong> de <strong>${filtered.length}</strong> reserva${filtered.length !== 1 ? 's' : ''}`;

    // Renderizar linhas
    if (filtered.length === 0) {
      tableBody.innerHTML = '';
      emptyState.style.display = 'block';
      pagination.innerHTML = '';
      return;
    }

    emptyState.style.display = 'none';

    tableBody.innerHTML = pageData.map((r, i) => `
      <tr class="table-row" style="animation-delay: ${i * 0.03}s">
        <td><span class="id-badge">${r.idLocacao}</span></td>
        <td class="modelo-cell">${r.nomeUsuario}</td>
        <td class="modelo-cell">${r.modeloVeiculo}</td>
        <td>${r.qtdDias} dias</td>
        <td>${r.localRetirada}</td>
        <td>${formatDate(r.dataRetirada)}</td>
        <td>${formatDate(r.dataEntrega)}</td>
        <td class="valor-cell">R$ ${r.valorTotal.toFixed(2)}</td>
        <td>
          <div class="action-buttons">
            <button class="btn-action btn-edit" title="Editar" onclick="window.location.href='editarReserva.html?idLocacao=${r.idLocacao}&idUsuario=${r.idUsuario}&nomeUsuario=${encodeURIComponent(r.nomeUsuario)}&idVeiculo=${r.idVeiculo}&modeloVeiculo=${encodeURIComponent(r.modeloVeiculo)}&qtdDias=${r.qtdDias}&seguroLocacao=${r.seguroLocacao}&localRetirada=${encodeURIComponent(r.localRetirada)}&valorTotal=${r.valorTotal}&dataRetirada=${r.dataRetirada}&dataEntrega=${r.dataEntrega}'">✏️</button>
            <button class="btn-action btn-delete" title="Deletar" onclick="confirmarExclusao(${r.idLocacao}, '${r.nomeUsuario}')">🗑️</button>
          </div>
        </td>
      </tr>
    `).join('');

    // Renderizar paginação
    renderPagination(totalPages);
  }

  // ─── Renderizar controles de paginação ───
  function renderPagination(totalPages) {
    if (totalPages <= 1) {
      pagination.innerHTML = '';
      return;
    }

    let html = '<div class="pagination-wrap">';
    html += `<span class="pagination-info">Página <strong>${currentPage}</strong> de <strong>${totalPages}</strong></span>`;
    html += '<div class="pagination-buttons">';

    html += `<button class="page-btn ${currentPage === 1 ? 'disabled' : ''}" data-page="${currentPage - 1}" ${currentPage === 1 ? 'disabled' : ''}>‹ Anterior</button>`;

    const range = getPageRange(currentPage, totalPages);
    range.forEach(p => {
      if (p === '...') {
        html += '<span class="page-ellipsis">…</span>';
      } else {
        html += `<button class="page-btn page-num ${p === currentPage ? 'active' : ''}" data-page="${p}">${p}</button>`;
      }
    });

    html += `<button class="page-btn ${currentPage === totalPages ? 'disabled' : ''}" data-page="${currentPage + 1}" ${currentPage === totalPages ? 'disabled' : ''}>Próximo ›</button>`;
    html += '</div></div>';

    pagination.innerHTML = html;

    pagination.querySelectorAll('.page-btn:not(.disabled)').forEach(btn => {
      btn.addEventListener('click', () => {
        currentPage = parseInt(btn.dataset.page);
        renderTable();
        document.querySelector('.data-table-wrap').scrollIntoView({ behavior: 'smooth', block: 'start' });
      });
    });
  }

  function getPageRange(current, total) {
    if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1);
    if (current <= 3) return [1, 2, 3, 4, '...', total];
    if (current >= total - 2) return [1, '...', total - 3, total - 2, total - 1, total];
    return [1, '...', current - 1, current, current + 1, '...', total];
  }

  // ─── Confirmar exclusão ───
  window.confirmarExclusao = function(id, nome) {
    if (confirm(`Tem certeza que deseja excluir a reserva #${id} do usuário "${nome}"?`)) {
      fetch(`ControleLocacao?op=DELETAR&id=${id}`, { method: 'POST' })
        .then(() => { location.reload(); })
        .catch(err => { console.error(err); alert('Erro ao excluir reserva.'); });
    }
  };

  // ─── Render inicial ───
  renderTable();
});
