/* ═══════════════════════════════════════════════════════════
   LISTARUSUARIOS.JS — Lógica da listagem de usuários
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Dados mockados (substituir por fetch ao backend futuramente) ───
  const usuarios = [
    { id: 1,  nome: 'João Silva',       cpf: '123.456.789-00', cnh: '12345678901', email: 'joao.silva@email.com',     celular: '(11) 99876-5432' },
    { id: 2,  nome: 'Maria Santos',     cpf: '234.567.890-11', cnh: '23456789012', email: 'maria.santos@email.com',   celular: '(21) 98765-4321' },
    { id: 3,  nome: 'Carlos Oliveira',  cpf: '345.678.901-22', cnh: '34567890123', email: 'carlos.oliveira@email.com', celular: '(31) 97654-3210' },
    { id: 4,  nome: 'Ana Costa',        cpf: '456.789.012-33', cnh: '45678901234', email: 'ana.costa@email.com',      celular: '(41) 96543-2109' },
    { id: 5,  nome: 'Pedro Almeida',    cpf: '567.890.123-44', cnh: '56789012345', email: 'pedro.almeida@email.com',  celular: '(51) 95432-1098' },
    { id: 6,  nome: 'Juliana Ferreira', cpf: '678.901.234-55', cnh: '67890123456', email: 'juliana.f@email.com',      celular: '(61) 94321-0987' },
    { id: 7,  nome: 'Lucas Rodrigues',  cpf: '789.012.345-66', cnh: '78901234567', email: 'lucas.r@email.com',        celular: '(71) 93210-9876' },
    { id: 8,  nome: 'Fernanda Lima',    cpf: '890.123.456-77', cnh: '89012345678', email: 'fernanda.lima@email.com',  celular: '(81) 92109-8765' },
  ];

  // ─── Elementos do DOM ───
  const tableBody    = document.getElementById('usuariosBody');
  const resultCount  = document.getElementById('resultCount');
  const emptyState   = document.getElementById('emptyState');
  const tableEl      = document.getElementById('usuariosTable');

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

  // ─── Eventos de filtro por coluna ───
  const allFilters = [filterId, filterNome, filterCpf, filterCnh, filterEmail, filterCelular];

  allFilters.forEach(input => {
    input.addEventListener('input', renderTable);
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

  // ─── Renderizar tabela ───
  function renderTable() {
    // Pegar valores dos filtros
    const fId      = filterId.value.trim().toLowerCase();
    const fNome    = filterNome.value.trim().toLowerCase();
    const fCpf     = filterCpf.value.trim();
    const fCnh     = filterCnh.value.trim();
    const fEmail   = filterEmail.value.trim().toLowerCase();
    const fCelular = filterCelular.value.trim().replace(/\D/g, '');

    // Filtrar
    let filtered = usuarios.filter(u => {
      if (fId && !String(u.id).includes(fId)) return false;
      if (fNome && !u.nome.toLowerCase().includes(fNome)) return false;
      if (fCpf && !u.cpf.includes(fCpf)) return false;
      if (fCnh && !u.cnh.includes(fCnh)) return false;
      if (fEmail && !u.email.toLowerCase().includes(fEmail)) return false;
      if (fCelular && !u.celular.replace(/\D/g, '').includes(fCelular)) return false;
      return true;
    });

    // Ordenar
    filtered.sort((a, b) => {
      let valA = a[sortCol];
      let valB = b[sortCol];

      if (typeof valA === 'string') {
        valA = valA.toLowerCase();
        valB = valB.toLowerCase();
      }

      if (valA < valB) return sortAsc ? -1 : 1;
      if (valA > valB) return sortAsc ? 1 : -1;
      return 0;
    });

    // Atualizar contador
    resultCount.innerHTML = `<strong>${filtered.length}</strong> usuário${filtered.length !== 1 ? 's' : ''} encontrado${filtered.length !== 1 ? 's' : ''}`;

    // Renderizar
    if (filtered.length === 0) {
      tableEl.querySelector('tbody').innerHTML = '';
      emptyState.style.display = 'block';
      return;
    }

    emptyState.style.display = 'none';

    tableBody.innerHTML = filtered.map(u => `
      <tr>
        <td><strong>${u.id}</strong></td>
        <td>
          <div class="user-name-cell">
            <div class="user-avatar-table">${getInitials(u.nome)}</div>
            <span class="user-name-text">${u.nome}</span>
          </div>
        </td>
        <td><span class="cpf-masked" title="Passe o mouse para ver">${u.cpf}</span></td>
        <td>${u.cnh}</td>
        <td>${u.email}</td>
        <td>${u.celular}</td>
      </tr>
    `).join('');
  }

  // ─── Render inicial ───
  renderTable();
});
