/* ═══════════════════════════════════════════════════════════
   LISTARVEICULOS.JS — Lógica da listagem de veículos
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Dados mockados (substituir por fetch ao backend futuramente) ───
  const veiculos = [
    { id: 1,  placa: 'ABC-1D23', modelo: 'Toyota Corolla',   cor: 'Preto',    valorDiaria: 179.00, funcionalidade: 'Passeio',     disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 2,  placa: 'DEF-2E45', modelo: 'Honda Civic',      cor: 'Prata',    valorDiaria: 169.00, funcionalidade: 'Passeio',     disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 3,  placa: 'GHI-3F67', modelo: 'Renault Kwid',     cor: 'Branco',   valorDiaria: 79.00,  funcionalidade: 'Passeio',     disponibilidade: false, arCondicionado: false, tipoCambio: 'Manual' },
    { id: 4,  placa: 'JKL-4G89', modelo: 'Jeep Compass',     cor: 'Cinza',    valorDiaria: 219.00, funcionalidade: 'Utilitário',  disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 5,  placa: 'MNO-5H01', modelo: 'Volkswagen Polo',  cor: 'Vermelho', valorDiaria: 98.00,  funcionalidade: 'Passeio',     disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 6,  placa: 'PQR-6I23', modelo: 'Fiat Mobi',        cor: 'Azul',     valorDiaria: 72.00,  funcionalidade: 'Trabalho',    disponibilidade: false, arCondicionado: false, tipoCambio: 'Manual' },
    { id: 7,  placa: 'STU-7J45', modelo: 'BMW 320i',         cor: 'Preto',    valorDiaria: 349.00, funcionalidade: 'Passeio',     disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 8,  placa: 'VWX-8K67', modelo: 'Hyundai Creta',    cor: 'Branco',   valorDiaria: 189.00, funcionalidade: 'Utilitário',  disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 9,  placa: 'YZA-9L89', modelo: 'Chevrolet Onix',   cor: 'Prata',    valorDiaria: 89.00,  funcionalidade: 'Passeio',     disponibilidade: true,  arCondicionado: true,  tipoCambio: 'Automático' },
    { id: 10, placa: 'BCD-0M01', modelo: 'Nissan Leaf',      cor: 'Verde',    valorDiaria: 199.00, funcionalidade: 'Passeio',     disponibilidade: false, arCondicionado: true,  tipoCambio: 'Automático' },
  ];

  // ─── Elementos do DOM ───
  const tableBody    = document.getElementById('veiculosBody');
  const resultCount  = document.getElementById('resultCount');
  const emptyState   = document.getElementById('emptyState');
  const tableEl      = document.getElementById('veiculosTable');

  // Filtros por coluna
  const filterId     = document.getElementById('filterId');
  const filterPlaca  = document.getElementById('filterPlaca');
  const filterModelo = document.getElementById('filterModelo');
  const filterCor    = document.getElementById('filterCor');
  const filterValor  = document.getElementById('filterValor');
  const filterFunc   = document.getElementById('filterFunc');
  const filterDisp   = document.getElementById('filterDisp');
  const filterAr     = document.getElementById('filterAr');
  const filterCambio = document.getElementById('filterCambio');

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
  const allInputFilters = [filterId, filterPlaca, filterModelo, filterCor, filterValor, filterFunc];
  const allSelectFilters = [filterDisp, filterAr, filterCambio];

  allInputFilters.forEach(input => {
    input.addEventListener('input', renderTable);
  });

  allSelectFilters.forEach(select => {
    select.addEventListener('change', renderTable);
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

  // ─── Renderizar tabela ───
  function renderTable() {
    // Pegar valores dos filtros
    const fId     = filterId.value.trim().toLowerCase();
    const fPlaca  = filterPlaca.value.trim().toLowerCase();
    const fModelo = filterModelo.value.trim().toLowerCase();
    const fCor    = filterCor.value.trim().toLowerCase();
    const fValor  = filterValor.value.trim();
    const fFunc   = filterFunc.value.trim().toLowerCase();
    const fDisp   = filterDisp.value;
    const fAr     = filterAr.value;
    const fCambio = filterCambio.value;

    // Filtrar
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
    resultCount.innerHTML = `<strong>${filtered.length}</strong> veículo${filtered.length !== 1 ? 's' : ''} encontrado${filtered.length !== 1 ? 's' : ''}`;

    // Renderizar
    if (filtered.length === 0) {
      tableEl.querySelector('tbody').innerHTML = '';
      emptyState.style.display = 'block';
      return;
    }

    emptyState.style.display = 'none';

    tableBody.innerHTML = filtered.map(v => `
      <tr>
        <td><strong>${v.id}</strong></td>
        <td>${v.placa}</td>
        <td>${v.modelo}</td>
        <td>${v.cor}</td>
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
            ${v.arCondicionado ? 'Sim' : 'Não'}
          </span>
        </td>
        <td>${v.tipoCambio}</td>
      </tr>
    `).join('');
  }

  // ─── Render inicial ───
  renderTable();
});
