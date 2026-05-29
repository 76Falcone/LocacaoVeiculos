/* ═══════════════════════════════════════════════════════════
   RESERVA.JS — Lógica da tela de reserva de veículo
   Novare · Sistema de Gestão de Veículos

   Seguros carregados dinamicamente via GET /ControleTipoSeguro
   Lógica de cálculo:
     • valor < 1  → percentual sobre o total das diárias (ex: 0.10 = +10%)
     • valor >= 1 → valor fixo em R$ (ex: 100.00 = +R$ 100,00)
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Elementos do DOM ───────────────────────────────────────
  const form               = document.getElementById('reservaForm');
  const idUsuarioInput     = document.getElementById('idUsuario');
  const idVeiculoInput     = document.getElementById('idVeiculo');
  const dataRetiradaInput  = document.getElementById('dataRetirada');
  const dataEntregaInput   = document.getElementById('dataEntrega');
  const qtdDiasInput       = document.getElementById('qtdDias');
  const localRetiradaInput = document.getElementById('localRetirada');
  const seguroLocacaoInput = document.getElementById('seguroLocacao'); // hidden
  const valorTotalInput    = document.getElementById('valorTotal');
  const btnReservar        = document.getElementById('btnReservar');
  const toast              = document.getElementById('toast');

  // Área de seguros dinâmica
  const seguroOpcoes  = document.getElementById('seguroOpcoes');
  const seguroLoading = document.getElementById('seguroLoading');

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

  // Estado da aplicação
  let valorDiaria = 0;
  let segurosDisponiveis = []; // lista de {id, tipo, valor} vindos do banco

  // ─── Sidebar mobile toggle ───────────────────────────────────
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

  // ─── Carregar parâmetros da URL ──────────────────────────────
  function carregarParametrosURL() {
    const params = new URLSearchParams(window.location.search);

    const idVeiculo = params.get('idVeiculo') || '';
    let   idUsuario = params.get('idUsuario') || '';
    const modelo    = params.get('modelo')    || 'Veículo não informado';
    const categoria = params.get('categoria') || '';
    const preco     = parseFloat(params.get('preco')) || 0;

    // Fallback: ler idUsuario do cookie de sessão
    if (!idUsuario || idUsuario === '0') {
      const match = document.cookie.match(new RegExp('(^| )idUsuario=([^;]+)'));
      idUsuario = match ? match[2] : '';
    }

    idVeiculoInput.value = idVeiculo;
    idUsuarioInput.value = idUsuario;

    resumoModelo.textContent   = modelo;
    resumoDetalhes.textContent = categoria ? `Categoria: ${categoria}` : '';

    valorDiaria = preco;
    resumoDiaria.textContent = preco > 0
      ? `R$ ${fmt(preco)}`
      : '—';

    atualizarTotal();
  }

  // ─── Buscar seguros do banco (/ControleTipoSeguro) ───────────
  function carregarSeguros() {
    fetch('ControleTipoSeguro')
      .then(res => {
        if (!res.ok) throw new Error('Status HTTP ' + res.status);
        return res.json();
      })
      .then(seguros => {
        segurosDisponiveis = seguros;
        renderizarCardsSeguro(seguros);
      })
      .catch(err => {
        console.error('Erro ao carregar seguros:', err);
        if (seguroLoading) {
          seguroLoading.innerHTML = '⚠️ Não foi possível carregar as opções de seguro.';
        }
      });
  }

  // ─── Gerar descrição legível para cada tipo de seguro ────────
  function descricaoSeguro(tipo) {
    const descricoes = {
      'Terceiros':          'Cobre danos causados a terceiros durante a locação.',
      'Pane Elétrica':      'Assistência em caso de pane elétrica no veículo.',
      'Vidros e Espelhos':  'Cobre quebra de vidros, retrovisores e espelhos.',
      'Pneu':               'Cobre furos e danos nos pneus durante a locação.',
    };
    return descricoes[tipo] || 'Proteção adicional para a sua locação.';
  }

  // ─── Renderizar cards de seguro dinamicamente ────────────────
  function renderizarCardsSeguro(seguros) {
    // Remove spinner
    if (seguroLoading) seguroLoading.remove();

    seguros.forEach(seguro => {
      const isPercentual = seguro.valor < 1;
      const taxaLabel    = isPercentual
        ? `+${Math.round(seguro.valor * 100)}%`
        : `+R$ ${fmt(seguro.valor)}`;

      const cardId     = `cardSeguro_${seguro.id}`;
      const checkId    = `chkSeguro_${seguro.id}`;
      const valorElId  = `valorSeguro_${seguro.id}`;

      const label = document.createElement('label');
      label.className = 'seguro-card';
      label.id        = cardId;
      label.innerHTML = `
        <input type="checkbox" id="${checkId}" name="seguroId" value="${seguro.id}">
        <div class="seguro-card-body">
          <div class="seguro-card-header">
            <span class="seguro-card-titulo">${seguro.tipo}</span>
            <span class="seguro-card-taxa">${taxaLabel}</span>
          </div>
          <div class="seguro-card-desc">${descricaoSeguro(seguro.tipo)}</div>
          <div class="seguro-card-valor" id="${valorElId}">R$ 0,00</div>
        </div>
      `;

      // Evento de seleção
      const chk = label.querySelector(`#${checkId}`);
      chk.addEventListener('change', () => {
        label.classList.toggle('selecionado', chk.checked);
        atualizarTotal();
      });

      seguroOpcoes.appendChild(label);
    });

    atualizarTotal(); // Recalcula após renderizar
  }

  // ─── Calcular qtdDias automaticamente ───────────────────────
  function calcularDias() {
    const dataRet = dataRetiradaInput.value;
    const dataEnt = dataEntregaInput.value;

    if (!dataRet || !dataEnt) {
      qtdDiasInput.value = '';
      atualizarTotal();
      return;
    }

    const ret  = new Date(dataRet);
    const ent  = new Date(dataEnt);
    const diff = Math.ceil((ent - ret) / (1000 * 60 * 60 * 24));

    qtdDiasInput.value = diff > 0 ? diff : '';
    atualizarTotal();
  }

  // ─── Atualizar card de valor total ──────────────────────────
  function atualizarTotal() {
    const dias      = parseInt(qtdDiasInput.value) || 0;
    const valorBase = valorDiaria * dias; // diária × dias (base sem seguro)

    // Variáveis para o total encadeado (lógica Decorator)
    let totalAcumulado  = valorBase;
    let totalSeguroAcum = 0;

    segurosDisponiveis.forEach(seguro => {
      const chk   = document.getElementById(`chkSeguro_${seguro.id}`);
      const valEl = document.getElementById(`valorSeguro_${seguro.id}`);
      const isChecked = chk && chk.checked;

      // ── Calcula valor de exibição no card (sempre visível) ──
      // Para fixos: sempre R$ X,00 independente de dias
      // Para percentuais: baseDias × taxa (ou zero se sem datas)
      let valorExibicao;
      if (seguro.valor < 1) {
        valorExibicao = valorBase * seguro.valor;
      } else {
        valorExibicao = seguro.valor; // fixo: sempre mostra o preço
      }
      if (valEl) valEl.textContent = `R$ ${fmt(valorExibicao)}`;

      // ── Só acumula no total se estiver selecionado ──
      if (!isChecked) return;

      let valorParaTotal;
      if (seguro.valor < 1) {
        // Percentual encadeado (Decorator): incide sobre o acumulado até agora
        valorParaTotal = totalAcumulado * seguro.valor;
      } else {
        valorParaTotal = seguro.valor;
      }

      totalAcumulado  += valorParaTotal;
      totalSeguroAcum += valorParaTotal;
    });

    const total = totalAcumulado; // diárias + seguros selecionados

    // Atualiza resumo de baixo
    totalDiaria.textContent = `R$ ${fmt(valorDiaria)}`;
    totalDias.textContent   = `${dias} dia${dias !== 1 ? 's' : ''}`;
    totalSeguro.textContent = `R$ ${fmt(totalSeguroAcum)}`;
    totalValor.textContent  = `R$ ${fmt(total)}`;

    // Preenche campos ocultos para o backend
    seguroLocacaoInput.value = totalSeguroAcum.toFixed(2);
    valorTotalInput.value    = total.toFixed(2);
  }

  // ─── Utilitário: formata número como moeda BR ────────────────
  function fmt(n) {
    return n.toFixed(2).replace('.', ',');
  }

  // ─── Eventos ────────────────────────────────────────────────
  dataRetiradaInput.addEventListener('change', () => {
    calcularDias();
    clearError('dataRetiradaGroup', 'dataRetiradaError');
  });

  dataEntregaInput.addEventListener('change', () => {
    calcularDias();
    clearError('dataEntregaGroup', 'dataEntregaError');
  });

  localRetiradaInput.addEventListener('input', () => {
    clearError('localRetiradaGroup', 'localRetiradaError');
  });

  // ─── Submissão do formulário ─────────────────────────────────
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const results = [
      validateDataRetirada(),
      validateDataEntrega(),
      validateQtdDias(),
      validateLocalRetirada(),
    ];

    if (results.includes(false)) return;

    btnReservar.classList.add('loading');
    btnReservar.disabled = true;

    setTimeout(() => { form.submit(); }, 600);
  });

  // ─── Funções de validação ────────────────────────────────────

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

  // ─── Helpers ─────────────────────────────────────────────────

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
    toast.className   = 'toast ' + type + ' show';
    setTimeout(() => { toast.classList.remove('show'); }, 3000);
  }

  // ─── Inicialização ───────────────────────────────────────────
  carregarParametrosURL();
  carregarSeguros();         // Busca seguros do banco e monta os cards
});
