/* ═══════════════════════════════════════════════════════════
   RESERVA.JS — Lógica da tela de reserva de veículo
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Elementos do DOM ───
  const form               = document.getElementById('reservaForm');
  const idUsuarioInput     = document.getElementById('idUsuario');
  const idVeiculoInput     = document.getElementById('idVeiculo');
  const dataRetiradaInput  = document.getElementById('dataRetirada');
  const dataEntregaInput   = document.getElementById('dataEntrega');
  const qtdDiasInput       = document.getElementById('qtdDias');
  const localRetiradaInput = document.getElementById('localRetirada');
  const seguroLocacaoInput = document.getElementById('seguroLocacao');
  const valorTotalInput    = document.getElementById('valorTotal');
  const btnReservar        = document.getElementById('btnReservar');
  const toast              = document.getElementById('toast');

  // Elementos do resumo
  const resumoModelo   = document.getElementById('resumoModelo');
  const resumoDetalhes = document.getElementById('resumoDetalhes');
  const resumoDiaria   = document.getElementById('resumoDiaria');

  // Elementos do total
  const totalDiaria = document.getElementById('totalDiaria');
  const totalDias   = document.getElementById('totalDias');
  const totalSeguro = document.getElementById('totalSeguro');
  const totalValor  = document.getElementById('totalValor');

  // Sidebar mobile
  const menuToggle     = document.getElementById('menuToggle');
  const sidebar        = document.getElementById('sidebar');
  const sidebarOverlay = document.getElementById('sidebarOverlay');

  // Valor da diária (vindo da URL ou mockado)
  let valorDiaria = 0;

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

  // ─── Ler parâmetros da URL e preencher campos ocultos ───
  function carregarParametrosURL() {
    const params = new URLSearchParams(window.location.search);

    const idVeiculo  = params.get('idVeiculo')  || '';
    const idUsuario  = params.get('idUsuario')  || '';
    const modelo     = params.get('modelo')     || 'Veículo não informado';
    const categoria  = params.get('categoria')  || '';
    const preco      = parseFloat(params.get('preco')) || 0;

    // Preenche campos ocultos — NÃO visíveis ao usuário
    idVeiculoInput.value = idVeiculo;
    idUsuarioInput.value = idUsuario;

    // Atualiza card de resumo
    resumoModelo.textContent = modelo;
    resumoDetalhes.textContent = categoria ? `Categoria: ${categoria}` : '';

    valorDiaria = preco;
    if (preco > 0) {
      resumoDiaria.textContent = `R$ ${preco.toFixed(2).replace('.', ',')}`;
    } else {
      resumoDiaria.textContent = '—';
    }

    atualizarTotal();
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
    const total  = (valorDiaria * dias) + seguro;

    totalDiaria.textContent = `R$ ${valorDiaria.toFixed(2).replace('.', ',')}`;
    totalDias.textContent   = `${dias} dia${dias !== 1 ? 's' : ''}`;
    totalSeguro.textContent = `R$ ${seguro.toFixed(2).replace('.', ',')}`;
    totalValor.textContent  = `R$ ${total.toFixed(2).replace('.', ',')}`;

    // Atualiza campo oculto valorTotal para envio ao backend
    valorTotalInput.value = total.toFixed(2);
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

    if (results.includes(false)) {
      return;
    }

    // Loading
    btnReservar.classList.add('loading');
    btnReservar.disabled = true;

    setTimeout(() => {
      form.submit();
    }, 600);
  });

  // ─── Funções de validação ───

  function validateDataRetirada() {
    const val = dataRetiradaInput.value;
    if (!val) {
      setError('dataRetiradaGroup', 'dataRetiradaError', 'Informe a data de retirada.');
      return false;
    }
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    if (new Date(val) < hoje) {
      setError('dataRetiradaGroup', 'dataRetiradaError', 'A data de retirada não pode ser no passado.');
      return false;
    }
    clearError('dataRetiradaGroup', 'dataRetiradaError');
    return true;
  }

  function validateDataEntrega() {
    const valRet = dataRetiradaInput.value;
    const valEnt = dataEntregaInput.value;
    if (!valEnt) {
      setError('dataEntregaGroup', 'dataEntregaError', 'Informe a data de entrega.');
      return false;
    }
    if (valRet && new Date(valEnt) <= new Date(valRet)) {
      setError('dataEntregaGroup', 'dataEntregaError', 'A data de entrega deve ser após a retirada.');
      return false;
    }
    clearError('dataEntregaGroup', 'dataEntregaError');
    return true;
  }

  function validateQtdDias() {
    const val = parseInt(qtdDiasInput.value);
    if (!val || val < 1) {
      setError('qtdDiasGroup', 'qtdDiasError', 'O período mínimo é 1 dia.');
      return false;
    }
    clearError('qtdDiasGroup', 'qtdDiasError');
    return true;
  }

  function validateLocalRetirada() {
    const val = localRetiradaInput.value.trim();
    if (!val) {
      setError('localRetiradaGroup', 'localRetiradaError', 'Informe o local de retirada.');
      return false;
    }
    clearError('localRetiradaGroup', 'localRetiradaError');
    return true;
  }

  function validateSeguro() {
    const val = parseFloat(seguroLocacaoInput.value);
    if (isNaN(val) || val < 0) {
      setError('seguroLocacaoGroup', 'seguroLocacaoError', 'Informe um valor de seguro válido (mínimo R$ 0,00).');
      return false;
    }
    clearError('seguroLocacaoGroup', 'seguroLocacaoError');
    return true;
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

  /**
   * Exibe notificação toast
   * @param {string} message - Mensagem
   * @param {string} type - 'success' ou 'error'
   */
  function showToast(message, type) {
    toast.textContent = message;
    toast.className = 'toast ' + type + ' show';
    setTimeout(() => {
      toast.classList.remove('show');
    }, 3000);
  }

  // ─── Inicialização ───
  carregarParametrosURL();
});
