/* ═══════════════════════════════════════════════════════════
   EDITARRESERVA.JS — Lógica da tela de edição de reserva
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Elementos do DOM ───
  const form               = document.getElementById('editarReservaForm');
  const idLocacaoInput     = document.getElementById('idLocacao');
  const idUsuarioInput     = document.getElementById('idUsuario');
  const idVeiculoInput     = document.getElementById('idVeiculo');
  const dataRetiradaInput  = document.getElementById('dataRetirada');
  const dataEntregaInput   = document.getElementById('dataEntrega');
  const qtdDiasInput       = document.getElementById('qtdDias');
  const localRetiradaInput = document.getElementById('localRetirada');
  const seguroLocacaoInput = document.getElementById('seguroLocacao');
  const valorTotalInput    = document.getElementById('valorTotal');
  const btnSalvar          = document.getElementById('btnSalvar');
  const toast              = document.getElementById('toast');

  // Elementos do resumo
  const resumoModelo   = document.getElementById('resumoModelo');
  const resumoDetalhes = document.getElementById('resumoDetalhes');

  // Elementos do total
  const totalDias   = document.getElementById('totalDias');
  const totalSeguro = document.getElementById('totalSeguro');
  const totalValor  = document.getElementById('totalValor');

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

    idLocacaoInput.value     = params.get('idLocacao')     || '';
    idUsuarioInput.value     = params.get('idUsuario')     || '';
    idVeiculoInput.value     = params.get('idVeiculo')     || '';
    dataRetiradaInput.value  = params.get('dataRetirada')  || '';
    dataEntregaInput.value   = params.get('dataEntrega')   || '';
    localRetiradaInput.value = params.get('localRetirada') || '';
    seguroLocacaoInput.value = params.get('seguroLocacao') || '';

    const nomeUsuario  = params.get('nomeUsuario')  || '';
    const modeloVeiculo = params.get('modeloVeiculo') || 'Veículo não informado';

    resumoModelo.textContent   = modeloVeiculo;
    resumoDetalhes.textContent = nomeUsuario ? `Usuário: ${nomeUsuario}` : '';

    calcularDias();
  }

  // ─── Calcular qtdDias automaticamente ───
  function calcularDias() {
    const dataRet = dataRetiradaInput.value;
    const dataEnt = dataEntregaInput.value;

    if (!dataRet || !dataEnt) {
      qtdDiasInput.value = '';
      atualizarTotal();
      return;
    }

    const ret = new Date(dataRet);
    const ent = new Date(dataEnt);
    const diff = Math.ceil((ent - ret) / (1000 * 60 * 60 * 24));

    if (diff > 0) {
      qtdDiasInput.value = diff;
    } else {
      qtdDiasInput.value = '';
    }

    atualizarTotal();
  }

  // ─── Atualizar card de valor total ───
  function atualizarTotal() {
    const dias   = parseInt(qtdDiasInput.value) || 0;
    const seguro = parseFloat(seguroLocacaoInput.value) || 0;
    // Recalcular com os dados disponíveis
    const total = seguro + (dias > 0 ? dias * 0 : 0); // sem valorDiaria na edição, usa valorTotal original
    const valorOriginal = parseFloat(new URLSearchParams(window.location.search).get('valorTotal')) || 0;

    totalDias.textContent   = `${dias} dia${dias !== 1 ? 's' : ''}`;
    totalSeguro.textContent = `R$ ${seguro.toFixed(2).replace('.', ',')}`;
    totalValor.textContent  = `R$ ${valorOriginal.toFixed(2).replace('.', ',')}`;
    valorTotalInput.value   = valorOriginal.toFixed(2);
  }

  // ─── Eventos ───
  dataRetiradaInput.addEventListener('change', () => {
    calcularDias();
    if (document.getElementById('dataRetiradaGroup').classList.contains('has-error')) {
      clearError('dataRetiradaGroup', 'dataRetiradaError');
    }
  });

  dataEntregaInput.addEventListener('change', () => {
    calcularDias();
    if (document.getElementById('dataEntregaGroup').classList.contains('has-error')) {
      clearError('dataEntregaGroup', 'dataEntregaError');
    }
  });

  localRetiradaInput.addEventListener('input', () => {
    if (document.getElementById('localRetiradaGroup').classList.contains('has-error')) {
      clearError('localRetiradaGroup', 'localRetiradaError');
    }
  });

  seguroLocacaoInput.addEventListener('input', () => {
    atualizarTotal();
    if (document.getElementById('seguroLocacaoGroup').classList.contains('has-error')) {
      clearError('seguroLocacaoGroup', 'seguroLocacaoError');
    }
  });

  // ─── Submissão do formulário ───
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const results = [
      validateDataRetirada(),
      validateDataEntrega(),
      validateQtdDias(),
      validateLocalRetirada(),
      validateSeguro(),
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

  function validateDataRetirada() {
    if (!dataRetiradaInput.value) { setError('dataRetiradaGroup', 'dataRetiradaError', 'Informe a data de retirada.'); return false; }
    clearError('dataRetiradaGroup', 'dataRetiradaError'); return true;
  }

  function validateDataEntrega() {
    const valRet = dataRetiradaInput.value;
    const valEnt = dataEntregaInput.value;
    if (!valEnt) { setError('dataEntregaGroup', 'dataEntregaError', 'Informe a data de entrega.'); return false; }
    if (valRet && new Date(valEnt) <= new Date(valRet)) { setError('dataEntregaGroup', 'dataEntregaError', 'A data de entrega deve ser após a retirada.'); return false; }
    clearError('dataEntregaGroup', 'dataEntregaError'); return true;
  }

  function validateQtdDias() {
    const val = parseInt(qtdDiasInput.value);
    if (!val || val < 1) { setError('qtdDiasGroup', 'qtdDiasError', 'O período mínimo é 1 dia.'); return false; }
    clearError('qtdDiasGroup', 'qtdDiasError'); return true;
  }

  function validateLocalRetirada() {
    if (!localRetiradaInput.value.trim()) { setError('localRetiradaGroup', 'localRetiradaError', 'Informe o local de retirada.'); return false; }
    clearError('localRetiradaGroup', 'localRetiradaError'); return true;
  }

  function validateSeguro() {
    const val = parseFloat(seguroLocacaoInput.value);
    if (isNaN(val) || val < 0) { setError('seguroLocacaoGroup', 'seguroLocacaoError', 'Informe um valor de seguro válido.'); return false; }
    clearError('seguroLocacaoGroup', 'seguroLocacaoError'); return true;
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
