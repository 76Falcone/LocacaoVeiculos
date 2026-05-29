/* ═══════════════════════════════════════════════════════════
   MINHASLOCACOES.JS — Lógica da página de locações do usuário
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Helper: ler cookie ──────────────────────────────────
  const getCookie = (name) => {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : null;
  };

  const authWall   = document.getElementById('authWall');
  const mainContent = document.getElementById('mainContent');
  const resultCount = document.getElementById('resultCount');

  // ─── Verifica autenticação ───────────────────────────────
  const loggedIn = getCookie('usuarioLogado');

  if (!loggedIn) {
    // Usuário não logado: mostra tela de bloqueio
    authWall.style.display   = 'block';
    mainContent.style.display = 'none';
    if (resultCount) resultCount.textContent = '';
    return;
  }

  // Usuário logado: exibe conteúdo
  authWall.style.display    = 'none';
  mainContent.style.display = 'block';

  // ─── Estado global ────────────────────────────────────────
  let locacoes = [];
  const ITEMS_PER_PAGE = 10;
  let currentPage = 1;
  let sortCol = 'idLocacao';
  let sortAsc  = false; // mais recente primeiro

  // ─── Elementos do DOM ─────────────────────────────────────
  const tableBody   = document.getElementById('locacoesBody');
  const emptyState  = document.getElementById('emptyState');
  const pagination  = document.getElementById('pagination');

  const filterId       = document.getElementById('filterId');
  const filterVeiculo  = document.getElementById('filterVeiculo');
  const filterDias     = document.getElementById('filterDias');
  const filterLocal    = document.getElementById('filterLocal');
  const filterRetirada = document.getElementById('filterRetirada');
  const filterEntrega  = document.getElementById('filterEntrega');
  const filterTotal    = document.getElementById('filterTotal');

  // ─── Busca dados do backend ───────────────────────────────
  fetch('../ControleLocacao?op=LISTAR_MINHAS')
    .then(r => r.json())
    .then(data => {
      locacoes = data;
      updateStats(data);
      renderTable();
    })
    .catch(err => {
      console.error('[MinhasLocações] Erro ao carregar:', err);
      if (resultCount) resultCount.textContent = 'Erro ao carregar as locações.';
    });

  // ─── Cards de Estatísticas ────────────────────────────────
  function updateStats(data) {
    const totalGasto  = data.reduce((acc, l) => acc + l.valorTotal, 0);
    const totalDias   = data.reduce((acc, l) => acc + l.qtdDias, 0);
    const veiculosIds = new Set(data.map(l => l.idVeiculo));

    document.getElementById('statTotal').textContent    = data.length;
    document.getElementById('statGasto').textContent    = `R$ ${fmt(totalGasto)}`;
    document.getElementById('statDias').textContent     = totalDias;
    document.getElementById('statVeiculos').textContent = veiculosIds.size;
  }

  // ─── Utilitário de formatação ─────────────────────────────
  function fmt(n) {
    return n.toFixed(2).replace('.', ',');
  }

  function formatDate(dateStr) {
    if (!dateStr) return '—';
    const [y, m, d] = dateStr.split('-');
    return `${d}/${m}/${y}`;
  }

  // ─── Ordenação ────────────────────────────────────────────
  document.querySelectorAll('.ml-sortable').forEach(th => {
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

  // ─── Filtros ──────────────────────────────────────────────
  const allFilters = [filterId, filterVeiculo, filterDias, filterLocal, filterRetirada, filterEntrega, filterTotal];
  allFilters.forEach(input => {
    if (input) input.addEventListener('input', () => { currentPage = 1; renderTable(); });
  });

  // ─── Dados filtrados e ordenados ─────────────────────────
  function getFilteredData() {
    const fId       = filterId      ? filterId.value.trim().toLowerCase()       : '';
    const fVeiculo  = filterVeiculo ? filterVeiculo.value.trim().toLowerCase()  : '';
    const fDias     = filterDias    ? filterDias.value.trim()                   : '';
    const fLocal    = filterLocal   ? filterLocal.value.trim().toLowerCase()    : '';
    const fRetirada = filterRetirada ? filterRetirada.value.trim()              : '';
    const fEntrega  = filterEntrega ? filterEntrega.value.trim()               : '';
    const fTotal    = filterTotal   ? filterTotal.value.trim()                 : '';

    let filtered = locacoes.filter(l => {
      if (fId      && !String(l.idLocacao).includes(fId))                    return false;
      if (fVeiculo && !l.modeloVeiculo.toLowerCase().includes(fVeiculo))      return false;
      if (fDias    && !String(l.qtdDias).includes(fDias))                     return false;
      if (fLocal   && !l.localRetirada.toLowerCase().includes(fLocal))        return false;
      if (fRetirada && !l.dataRetirada.includes(fRetirada)
                    && !formatDate(l.dataRetirada).includes(fRetirada))        return false;
      if (fEntrega  && !l.dataEntrega.includes(fEntrega)
                    && !formatDate(l.dataEntrega).includes(fEntrega))          return false;
      if (fTotal   && !String(l.valorTotal.toFixed(2)).includes(fTotal))       return false;
      return true;
    });

    filtered.sort((a, b) => {
      let valA = a[sortCol];
      let valB = b[sortCol];
      if (typeof valA === 'string') { valA = valA.toLowerCase(); valB = valB.toLowerCase(); }
      if (valA < valB) return sortAsc ? -1 :  1;
      if (valA > valB) return sortAsc ?  1 : -1;
      return 0;
    });

    return filtered;
  }

  // ─── Renderização da tabela ───────────────────────────────
  function renderTable() {
    const filtered   = getFilteredData();
    const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE) || 1;

    if (currentPage > totalPages) currentPage = totalPages;

    const start    = (currentPage - 1) * ITEMS_PER_PAGE;
    const end      = start + ITEMS_PER_PAGE;
    const pageData = filtered.slice(start, end);

    // Atualiza contador
    if (resultCount) {
      resultCount.innerHTML = filtered.length > 0
        ? `Exibindo <strong>${start + 1}–${Math.min(end, filtered.length)}</strong> de <strong>${filtered.length}</strong> locaç${filtered.length !== 1 ? 'ões' : 'ão'}`
        : 'Nenhuma locação encontrada';
    }

    if (filtered.length === 0) {
      tableBody.innerHTML   = '';
      emptyState.style.display = 'block';
      pagination.innerHTML  = '';
      return;
    }

    emptyState.style.display = 'none';

    tableBody.innerHTML = pageData.map((l, i) => `
      <tr style="animation-delay:${i * 0.03}s">
        <td><span class="ml-id-badge">${l.idLocacao}</span></td>
        <td class="ml-modelo">${l.modeloVeiculo}</td>
        <td>${l.qtdDias} dia${l.qtdDias !== 1 ? 's' : ''}</td>
        <td>${l.localRetirada || '—'}</td>
        <td>${formatDate(l.dataRetirada)}</td>
        <td>${formatDate(l.dataEntrega)}</td>
        <td>
          <span class="ml-seguro-badge ${l.seguroLocacao > 0 ? 'com-seguro' : 'sem-seguro'}">
            ${l.seguroLocacao > 0 ? `✓ R$ ${fmt(l.seguroLocacao)}` : '— Sem seguro'}
          </span>
        </td>
        <td class="ml-valor">R$ ${fmt(l.valorTotal)}</td>
      </tr>
    `).join('');

    renderPagination(totalPages);
  }

  // ─── Paginação ────────────────────────────────────────────
  function renderPagination(totalPages) {
    if (totalPages <= 1) { pagination.innerHTML = ''; return; }

    let html = '<div class="ml-pagination-wrap">';
    html += `<span class="ml-pagination-info">Página <strong>${currentPage}</strong> de <strong>${totalPages}</strong></span>`;
    html += '<div class="ml-pagination-buttons">';
    html += `<button class="ml-page-btn ${currentPage === 1 ? 'disabled' : ''}" data-page="${currentPage - 1}" ${currentPage === 1 ? 'disabled' : ''}>‹ Anterior</button>`;

    getPageRange(currentPage, totalPages).forEach(p => {
      if (p === '...') {
        html += '<span style="padding:0 6px;color:var(--muted);">…</span>';
      } else {
        html += `<button class="ml-page-btn ${p === currentPage ? 'active' : ''}" data-page="${p}">${p}</button>`;
      }
    });

    html += `<button class="ml-page-btn ${currentPage === totalPages ? 'disabled' : ''}" data-page="${currentPage + 1}" ${currentPage === totalPages ? 'disabled' : ''}>Próximo ›</button>`;
    html += '</div></div>';

    pagination.innerHTML = html;

    pagination.querySelectorAll('.ml-page-btn:not(.disabled)').forEach(btn => {
      btn.addEventListener('click', () => {
        currentPage = parseInt(btn.dataset.page);
        renderTable();
        window.scrollTo({ top: 0, behavior: 'smooth' });
      });
    });
  }

  function getPageRange(current, total) {
    if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1);
    if (current <= 3) return [1, 2, 3, 4, '...', total];
    if (current >= total - 2) return [1, '...', total - 3, total - 2, total - 1, total];
    return [1, '...', current - 1, current, current + 1, '...', total];
  }
});
