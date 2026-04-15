/* ═══════════════════════════════════════════════════════════
   EDITARUSUARIO.JS — Lógica da tela de edição de usuário
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Elementos do DOM ───
  const form          = document.getElementById('editarUsuarioForm');
  const idInput       = document.getElementById('idUsuario');
  const nomeInput     = document.getElementById('nome');
  const cpfInput      = document.getElementById('cpf');
  const cnhInput      = document.getElementById('cnh');
  const emailInput    = document.getElementById('email');
  const celularInput  = document.getElementById('celular');
  const btnSalvar     = document.getElementById('btnSalvar');

  // ─── Preencher campos com dados da URL ───
  function carregarDados() {
    const params = new URLSearchParams(window.location.search);

    idInput.value      = params.get('id')      || '';
    nomeInput.value    = params.get('nome')    || '';
    cpfInput.value     = params.get('cpf')     || '';
    cnhInput.value     = params.get('cnh')     || '';
    emailInput.value   = params.get('email')   || '';
    celularInput.value = params.get('celular') || '';
  }

  // ─── Máscara de CPF ───
  cpfInput.addEventListener('input', () => {
    let v = cpfInput.value.replace(/\D/g, '');
    if (v.length > 11) v = v.slice(0, 11);
    if (v.length > 9)       v = v.slice(0, 3) + '.' + v.slice(3, 6) + '.' + v.slice(6, 9) + '-' + v.slice(9);
    else if (v.length > 6)  v = v.slice(0, 3) + '.' + v.slice(3, 6) + '.' + v.slice(6);
    else if (v.length > 3)  v = v.slice(0, 3) + '.' + v.slice(3);
    cpfInput.value = v;
  });

  // ─── Máscara de celular ───
  celularInput.addEventListener('input', () => {
    let v = celularInput.value.replace(/\D/g, '');
    if (v.length > 11) v = v.slice(0, 11);
    if (v.length > 6)       v = '(' + v.slice(0, 2) + ') ' + v.slice(2, 7) + '-' + v.slice(7);
    else if (v.length > 2)  v = '(' + v.slice(0, 2) + ') ' + v.slice(2);
    celularInput.value = v;
  });

  // ─── Limpar erros ao digitar ───
  const textFields = [
    { input: nomeInput,    group: 'nomeGroup',    error: 'nomeError' },
    { input: cpfInput,     group: 'cpfGroup',     error: 'cpfError' },
    { input: cnhInput,     group: 'cnhGroup',     error: 'cnhError' },
    { input: emailInput,   group: 'emailGroup',   error: 'emailError' },
    { input: celularInput, group: 'celularGroup', error: 'celularError' },
  ];

  textFields.forEach(({ input, group, error }) => {
    input.addEventListener('input', () => {
      if (document.getElementById(group).classList.contains('has-error')) {
        clearError(group, error);
      }
    });
  });

  // ─── Submissão do formulário ───
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const results = [
      validateNome(),
      validateCpf(),
      validateCnh(),
      validateEmail(),
      validateCelular(),
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

  function validateNome() {
    const val = nomeInput.value.trim();
    if (!val) { setError('nomeGroup', 'nomeError', 'Informe o nome completo.'); return false; }
    if (val.length < 3) { setError('nomeGroup', 'nomeError', 'Nome deve ter ao menos 3 caracteres.'); return false; }
    clearError('nomeGroup', 'nomeError'); return true;
  }

  function validateCpf() {
    const val = cpfInput.value.replace(/\D/g, '');
    if (!val) { setError('cpfGroup', 'cpfError', 'Informe o CPF.'); return false; }
    if (val.length !== 11) { setError('cpfGroup', 'cpfError', 'CPF deve ter 11 dígitos.'); return false; }
    clearError('cpfGroup', 'cpfError'); return true;
  }

  function validateCnh() {
    const val = cnhInput.value.trim();
    if (!val) { setError('cnhGroup', 'cnhError', 'Informe a CNH.'); return false; }
    if (val.length !== 11) { setError('cnhGroup', 'cnhError', 'CNH deve ter 11 dígitos.'); return false; }
    clearError('cnhGroup', 'cnhError'); return true;
  }

  function validateEmail() {
    const val = emailInput.value.trim();
    if (!val) { setError('emailGroup', 'emailError', 'Informe o e-mail.'); return false; }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val)) { setError('emailGroup', 'emailError', 'E-mail inválido.'); return false; }
    clearError('emailGroup', 'emailError'); return true;
  }

  function validateCelular() {
    const val = celularInput.value.replace(/\D/g, '');
    if (!val) { setError('celularGroup', 'celularError', 'Informe o celular.'); return false; }
    if (val.length < 10) { setError('celularGroup', 'celularError', 'Celular deve ter ao menos 10 dígitos.'); return false; }
    clearError('celularGroup', 'celularError'); return true;
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

  // ─── Inicialização ───
  carregarDados();
});
