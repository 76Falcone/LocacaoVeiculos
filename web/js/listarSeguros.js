/* ═══════════════════════════════════════════════════════════
   LISTARSEGUROS.JS — Lógica da tela de gerenciamento de seguros
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {
  // ─── Elementos do DOM ───
  const segurosBody    = document.getElementById('segurosBody');
  const resultCount    = document.getElementById('resultCount');
  const emptyState     = document.getElementById('emptyState');
  const btnNovoSeguro  = document.getElementById('btnNovoSeguro');
  
  // Modal e Formulário
  const seguroModal    = document.getElementById('seguroModal');
  const modalClose     = document.getElementById('modalClose');
  const btnCancelar    = document.getElementById('btnCancelar');
  const seguroForm     = document.getElementById('seguroForm');
  const modalTitle     = document.getElementById('modalTitle');
  const formOp         = document.getElementById('formOp');
  const formId         = document.getElementById('formId');
  const tipoInput      = document.getElementById('tipo');
  const tipoCalculo    = document.getElementById('tipoCalculo');
  const valorInput     = document.getElementById('valor');
  
  const labelValor     = document.getElementById('labelValor');
  const valorTip       = document.getElementById('valorTip');

  // Sidebar e Navbar Mobile
  const menuToggle     = document.getElementById('menuToggle');
  const sidebar        = document.getElementById('sidebar');
  const sidebarOverlay = document.getElementById('sidebarOverlay');

  let listSeguros = [];

  // ─── Menu Mobile ───
  if (menuToggle) {
    menuToggle.addEventListener('click', () => {
      sidebar.classList.toggle('active');
      sidebarOverlay.classList.toggle('active');
    });
  }

  if (sidebarOverlay) {
    sidebarOverlay.addEventListener('click', () => {
      sidebar.classList.remove('active');
      sidebarOverlay.classList.remove('active');
    });
  }

  // ─── Mudar modelo de cálculo dinamicamente no Modal ───
  tipoCalculo.addEventListener('change', () => {
    ajustarCamposValor();
  });

  function ajustarCamposValor() {
    if (tipoCalculo.value === 'percentual') {
      labelValor.textContent = 'Taxa Percentual (0.01 a 0.99)';
      valorInput.placeholder = 'Ex: 0.10 (para 10%)';
      valorTip.textContent = 'A taxa será multiplicada pelo valor base das diárias da locação.';
      if (parseFloat(valorInput.value) >= 1) {
        valorInput.value = '0.10';
      }
    } else {
      labelValor.textContent = 'Valor cobrado (R$)';
      valorInput.placeholder = 'Ex: 50.00';
      valorTip.textContent = 'Este valor fixo será somado diretamente ao valor final da reserva.';
      if (parseFloat(valorInput.value) < 1) {
        valorInput.value = '50.00';
      }
    }
  }

  // ─── Abrir Modal (Cadastro) ───
  btnNovoSeguro.addEventListener('click', () => {
    modalTitle.textContent = 'Cadastrar Novo Seguro';
    formOp.value = 'CADASTRAR';
    formId.value = '';
    seguroForm.reset();
    ajustarCamposValor();
    abrirModal();
  });

  // ─── Fechar Modal ───
  modalClose.addEventListener('click', fecharModal);
  btnCancelar.addEventListener('click', fecharModal);

  function abrirModal() {
    seguroModal.classList.add('active');
  }

  function fecharModal() {
    seguroModal.classList.remove('active');
  }

  // ─── Validação do formulário ───
  seguroForm.addEventListener('submit', (e) => {
    let hasError = false;
    
    // Validar nome
    const tipoVal = tipoInput.value.trim();
    const tipoError = document.getElementById('tipoError');
    if (!tipoVal) {
      tipoInput.parentElement.classList.add('has-error');
      tipoError.textContent = 'Informe o nome da cobertura.';
      hasError = true;
    } else {
      tipoInput.parentElement.classList.remove('has-error');
      tipoError.textContent = '';
    }

    // Validar valor
    const valorVal = parseFloat(valorInput.value);
    const valorError = document.getElementById('valorError');
    if (isNaN(valorVal) || valorVal <= 0) {
      valorInput.parentElement.classList.add('has-error');
      valorError.textContent = 'Informe um valor maior que zero.';
      hasError = true;
    } else if (tipoCalculo.value === 'percentual' && valorVal >= 1) {
      valorInput.parentElement.classList.add('has-error');
      valorError.textContent = 'A taxa percentual deve ser menor que 1.0 (Ex: 0.15 para 15%).';
      hasError = true;
    } else {
      valorInput.parentElement.classList.remove('has-error');
      valorError.textContent = '';
    }

    if (hasError) {
      e.preventDefault();
    }
  });

  // ─── Carregar e renderizar dados via AJAX ───
  function carregarSeguros() {
    fetch('../ControleTipoSeguro')
      .then(response => response.json())
      .then(data => {
        listSeguros = data;
        renderTable(data);
      })
      .catch(error => {
        console.error('Erro ao carregar seguros:', error);
        resultCount.textContent = 'Erro ao carregar seguros.';
      });
  }

  function renderTable(data) {
    segurosBody.innerHTML = '';
    
    if (data.length === 0) {
      emptyState.style.display = '';
      resultCount.textContent = 'Nenhum seguro cadastrado';
      return;
    }

    emptyState.style.display = 'none';
    resultCount.textContent = `${data.length} ${data.length === 1 ? 'seguro cadastrado' : 'seguros cadastrados'}`;

    data.forEach(s => {
      const isPercentual = s.valor < 1.0;
      const formatoText  = isPercentual ? 'Percentual' : 'Valor Fixo';
      const formatoClass = isPercentual ? 'percentual' : 'fixo';
      
      const valorExibido = isPercentual 
        ? `${Math.round(s.valor * 100)}%` 
        : `R$ ${s.valor.toFixed(2).replace('.', ',')}`;

      const row = document.createElement('tr');
      row.innerHTML = `
        <td><strong>#${s.id}</strong></td>
        <td>${s.tipo}</td>
        <td><span class="badge-formato ${formatoClass}">${formatoText}</span></td>
        <td><strong>${valorExibido}</strong></td>
        <td>
          <button class="btn-edit" data-id="${s.id}">Editar</button>
          <button class="btn-delete" data-id="${s.id}">Excluir</button>
        </td>
      `;

      // Evento de Editar
      row.querySelector('.btn-edit').addEventListener('click', () => {
        carregarDadosEdicao(s);
      });

      // Evento de Excluir
      row.querySelector('.btn-delete').addEventListener('click', () => {
        confirmarExclusao(s);
      });

      segurosBody.appendChild(row);
    });
  }

  function carregarDadosEdicao(s) {
    modalTitle.textContent = 'Editar Seguro';
    formOp.value = 'ATUALIZAR';
    formId.value = s.id;
    tipoInput.value = s.tipo;
    valorInput.value = s.valor;
    tipoCalculo.value = s.valor < 1.0 ? 'percentual' : 'fixo';
    
    ajustarCamposValor();
    abrirModal();
  }

  function confirmarExclusao(s) {
    if (confirm(`Tem certeza que deseja excluir o seguro "${s.tipo}"?`)) {
      const formData = new URLSearchParams();
      formData.append('op', 'DELETAR');
      formData.append('id', s.id);

      fetch('../ControleTipoSeguro', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
      })
      .then(response => {
        if (response.ok) {
          carregarSeguros();
        } else {
          alert('Erro ao excluir seguro.');
        }
      })
      .catch(error => {
        console.error('Erro na requisição:', error);
        alert('Erro ao processar exclusão.');
      });
    }
  }

  // Inicializa a carga de seguros
  carregarSeguros();
});
