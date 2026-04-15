/* ═══════════════════════════════════════════════════════════
   LISTARUSUARIOS.JS — Lógica da listagem de usuários
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Dados carregados do backend ───
  let usuarios = [];

  // Buscar dados do banco de dados
  fetch('ControleUsuario?op=LISTAR')
    .then(r => r.json())
    .then(data => { usuarios = data; renderTable(); })
    .catch(err => { console.error('Erro ao carregar usuários:', err); renderTable(); });

  // ─── Configuração de paginação ───
  const ITEMS_PER_PAGE = 15;
  let currentPage = 1;

  // ─── Elementos do DOM ───
  const tableBody    = document.getElementById('usuariosBody');
  const resultCount  = document.getElementById('resultCount');
  const emptyState   = document.getElementById('emptyState');
  const tableEl      = document.getElementById('usuariosTable');
  const pagination   = document.getElementById('pagination');

  // Filtros por coluna
  const filterId      = document.getElementById('filterId');
  const filterNome    = document.getElementById('filterNome');
  const filterCpf     = document.getElementById('filterCpf');
  const filterCnh     = document.getElementById('filterCnh');
  const filterEmail   = document.getElementById('filterEmail');
  const filterCelular = document.getElementById('filterCelular');

  // Sidebar mobile
  const menuToggle     = document.getElementById('menuToggle');
  const sidebar        = document.getElementById('sidebar');
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
  const allFilters = [filterId, filterNome, filterCpf, filterCnh, filterEmail, filterCelular];

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

  // ─── Gerar iniciais do nome ───
  function getInitials(name) {
    const parts = name.trim().split(' ');
    if (parts.length >= 2) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return parts[0].substring(0, 2).toUpperCase();
  }

  // ─── Obter dados filtrados e ordenados ───
  function getFilteredData() {
    const fId      = filterId.value.trim().toLowerCase();
    const fNome    = filterNome.value.trim().toLowerCase();
    const fCpf     = filterCpf.value.trim();
    const fCnh     = filterCnh.value.trim();
    const fEmail   = filterEmail.value.trim().toLowerCase();
    const fCelular = filterCelular.value.trim().replace(/\D/g, '');

    let filtered = usuarios.filter(u => {
      if (fId && !String(u.id).includes(fId)) return false;
      if (fNome && !u.nome.toLowerCase().includes(fNome)) return false;
      if (fCpf && !u.cpf.includes(fCpf)) return false;
      if (fCnh && !u.cnh.includes(fCnh)) return false;
      if (fEmail && !u.email.toLowerCase().includes(fEmail)) return false;
      if (fCelular && !u.celular.replace(/\D/g, '').includes(fCelular)) return false;
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
    const end   = start + ITEMS_PER_PAGE;
    const pageData = filtered.slice(start, end);

    // Atualizar contador
    resultCount.innerHTML = `Exibindo <strong>${start + 1}–${Math.min(end, filtered.length)}</strong> de <strong>${filtered.length}</strong> usuário${filtered.length !== 1 ? 's' : ''}`;

    // Renderizar linhas
    if (filtered.length === 0) {
      tableBody.innerHTML = '';
      emptyState.style.display = 'block';
      pagination.innerHTML = '';
      return;
    }

    emptyState.style.display = 'none';

    tableBody.innerHTML = pageData.map((u, i) => `
      <tr class="table-row" style="animation-delay: ${i * 0.03}s">
        <td><span class="id-badge">${u.id}</span></td>
        <td>
          <div class="user-name-cell">
            <div class="user-avatar-table">${getInitials(u.nome)}</div>
            <span class="user-name-text">${u.nome}</span>
          </div>
        </td>
        <td><span class="cpf-masked" title="Passe o mouse para ver">${u.cpf}</span></td>
        <td><span class="cnh-cell">${u.cnh}</span></td>
        <td><span class="email-cell">${u.email}</span></td>
        <td>${u.celular}</td>
        <td>
          <div class="action-buttons">
            <button class="btn-action btn-edit" title="Editar" onclick="window.location.href='editarUsuario.html?id=${u.id}&nome=${encodeURIComponent(u.nome)}&cpf=${encodeURIComponent(u.cpf)}&cnh=${encodeURIComponent(u.cnh)}&email=${encodeURIComponent(u.email)}&celular=${encodeURIComponent(u.celular)}'">✏️</button>
            <button class="btn-action btn-delete" title="Deletar" onclick="confirmarExclusao(${u.id}, '${u.nome}')">🗑️</button>
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
    if (confirm(`Tem certeza que deseja excluir o usuário "${nome}" (ID: ${id})?`)) {
      fetch(`ControleUsuario?op=DELETAR&id=${id}`, { method: 'POST' })
        .then(() => { location.reload(); })
        .catch(err => { console.error(err); alert('Erro ao excluir usuário.'); });
    }
  };

  // ─── Render inicial ───
  renderTable();
});
