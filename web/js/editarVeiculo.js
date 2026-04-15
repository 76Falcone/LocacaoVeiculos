/* ═══════════════════════════════════════════════════════════
   EDITARVEICULO.JS — Lógica da tela de edição de veículo
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Elementos do DOM ───
  const form            = document.getElementById('editarVeiculoForm');
  const idInput         = document.getElementById('idVeiculo');
  const placaInput      = document.getElementById('placa');
  const modeloInput     = document.getElementById('modelo');
  const corInput        = document.getElementById('cor');
  const valorInput      = document.getElementById('valorDiaria');
  const funcInput       = document.getElementById('funcionalidade');
  const dispSelect      = document.getElementById('disponibilidade');
  const arSelect        = document.getElementById('arCondicionado');
  const tipoCambioSel   = document.getElementById('tipoCambio');
  const btnSalvar       = document.getElementById('btnSalvar');
  const toast           = document.getElementById('toast');

  // Sidebar mobile
  const menuToggle     = document.getElementById('menuToggle');
  const sidebar        = document.getElementById('sidebar');
  const sidebarOverlay = document.getElementById('sidebarOverlay');

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

  // ─── Preencher campos com dados da URL ───
  function carregarDados() {
    const params = new URLSearchParams(window.location.search);

    if (params.has('placa')) {
      idInput.value     = params.get('id')              || '';
      placaInput.value  = params.get('placa')           || '';
      modeloInput.value = params.get('modelo')          || '';
      corInput.value    = params.get('cor')             || '';
      valorInput.value  = params.get('valorDiaria')     || '';
      funcInput.value   = params.get('funcionalidade')  || '';

      const disp = params.get('disponibilidade');
      if (disp !== null) dispSelect.value = disp;

      const ar = params.get('arCondicionado');
      if (ar !== null) arSelect.value = ar;

      const cambio = params.get('tipoCambio');
      if (cambio) tipoCambioSel.value = cambio;
    }
  }

  // ─── Máscara de placa ───
  placaInput.addEventListener('input', () => {
    let v = placaInput.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
    if (v.length > 7) v = v.slice(0, 7);
    if (v.length > 3) {
      v = v.slice(0, 3) + '-' + v.slice(3);
    }
    placaInput.value = v;
  });

  // ─── Limpar erros ao digitar ───
  const textFields = [
    { input: placaInput,  group: 'placaGroup',          error: 'placaError' },
    { input: modeloInput, group: 'modeloGroup',         error: 'modeloError' },
    { input: corInput,    group: 'corGroup',             error: 'corError' },
    { input: valorInput,  group: 'valorGroup',           error: 'valorError' },
    { input: funcInput,   group: 'funcionalidadeGroup',  error: 'funcionalidadeError' },
  ];

  textFields.forEach(({ input, group, error }) => {
    input.addEventListener('input', () => {
      if (document.getElementById(group).classList.contains('has-error')) {
        clearError(group, error);
      }
    });
  });

  tipoCambioSel.addEventListener('change', () => {
    if (document.getElementById('tipoCambioGroup').classList.contains('has-error')) {
      clearError('tipoCambioGroup', 'tipoCambioError');
    }
  });

  // ─── Submissão do formulário ───
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const results = [
      validatePlaca(),
      validateModelo(),
      validateCor(),
      validateValor(),
      validateFuncionalidade(),
      validateTipoCambio(),
    ];

    if (results.includes(false)) return;

    // Loading
    btnSalvar.classList.add('loading');
    btnSalvar.disabled = true;

    setTimeout(() => {
      form.submit();
    }, 600);
  });

  // ─── Funções de validação ───

  function validatePlaca() {
    const val = placaInput.value.replace(/[^A-Za-z0-9]/g, '');
    if (!val) { setError('placaGroup', 'placaError', 'Informe a placa do veículo.'); return false; }
    if (val.length !== 7) { setError('placaGroup', 'placaError', 'Placa deve ter 7 caracteres (ABC1D23).'); return false; }
    clearError('placaGroup', 'placaError'); return true;
  }

  function validateModelo() {
    if (!modeloInput.value.trim()) { setError('modeloGroup', 'modeloError', 'Informe o modelo do veículo.'); return false; }
    clearError('modeloGroup', 'modeloError'); return true;
  }

  function validateCor() {
    if (!corInput.value.trim()) { setError('corGroup', 'corError', 'Informe a cor do veículo.'); return false; }
    clearError('corGroup', 'corError'); return true;
  }

  function validateValor() {
    const val = parseFloat(valorInput.value);
    if (isNaN(val) || val <= 0) { setError('valorGroup', 'valorError', 'Informe um valor válido maior que zero.'); return false; }
    clearError('valorGroup', 'valorError'); return true;
  }

  function validateFuncionalidade() {
    if (!funcInput.value.trim()) { setError('funcionalidadeGroup', 'funcionalidadeError', 'Informe a funcionalidade.'); return false; }
    clearError('funcionalidadeGroup', 'funcionalidadeError'); return true;
  }

  function validateTipoCambio() {
    if (!tipoCambioSel.value) { setError('tipoCambioGroup', 'tipoCambioError', 'Selecione o tipo de câmbio.'); return false; }
    clearError('tipoCambioGroup', 'tipoCambioError'); return true;
  }

  // ─── Helpers ───

  function setError(groupId, errorId, message) {
    document.getElementById(groupId).classList.add('has-error');
    document.getElementById(errorId).textContent = message;
  }

  function clearError(groupId, errorId) {
    document.getElementById(groupId).classList.remove('has-error');
    document.getElementById(errorId).textContent = '';
  }

  function showToast(message, type) {
    toast.textContent = message;
    toast.className = 'toast ' + type + ' show';
    setTimeout(() => { toast.classList.remove('show'); }, 3000);
  }

  // ─── Inicialização ───
  carregarDados();
});
