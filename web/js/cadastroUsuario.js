/* ═══════════════════════════════════════════════════════════
   CADASTROUSUARIO.JS — Lógica e validações do cadastro
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {

  // ─── Elementos do DOM ───
  const form          = document.getElementById('cadastroForm');
  const nomeInput     = document.getElementById('nome');
  const cpfInput      = document.getElementById('cpf');
  const cnhInput      = document.getElementById('cnh');
  const emailInput    = document.getElementById('email');
  const senhaInput    = document.getElementById('senha');
  const confirmInput  = document.getElementById('confirmSenha');
  const celularInput  = document.getElementById('celular');
  const btnCadastro   = document.getElementById('btnCadastro');
  const toggleSenha   = document.getElementById('toggleSenha');
  const toggleConfirm = document.getElementById('toggleConfirm');

  // Indicador de força
  const strengthWrap = document.getElementById('passwordStrength');
  const strengthFill = document.getElementById('strengthFill');
  const strengthText = document.getElementById('strengthText');

  // ─── Máscaras de input ───

  /**
   * Aplica máscara de CPF: 000.000.000-00
   */
  cpfInput.addEventListener('input', () => {
    let v = cpfInput.value.replace(/\D/g, '');
    if (v.length > 11) v = v.slice(0, 11);

    if (v.length > 9)      v = v.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
    else if (v.length > 6) v = v.replace(/(\d{3})(\d{3})(\d{1,3})/, '$1.$2.$3');
    else if (v.length > 3) v = v.replace(/(\d{3})(\d{1,3})/, '$1.$2');

    cpfInput.value = v;
  });

  /**
   * Aplica máscara de Celular: (00) 00000-0000
   */
  celularInput.addEventListener('input', () => {
    let v = celularInput.value.replace(/\D/g, '');
    if (v.length > 11) v = v.slice(0, 11);

    if (v.length > 6)      v = v.replace(/(\d{2})(\d{5})(\d{1,4})/, '($1) $2-$3');
    else if (v.length > 2) v = v.replace(/(\d{2})(\d{1,5})/, '($1) $2');
    else if (v.length > 0) v = v.replace(/(\d{1,2})/, '($1');

    celularInput.value = v;
  });

  /**
   * CNH: somente números
   */
  cnhInput.addEventListener('input', () => {
    cnhInput.value = cnhInput.value.replace(/\D/g, '').slice(0, 11);
  });

  // ─── Toggle mostrar/esconder senha ───
  toggleSenha.addEventListener('click', () => {
    const isPassword = senhaInput.type === 'password';
    senhaInput.type = isPassword ? 'text' : 'password';
    toggleSenha.textContent = isPassword ? '🙈' : '👁️';
  });

  toggleConfirm.addEventListener('click', () => {
    const isPassword = confirmInput.type === 'password';
    confirmInput.type = isPassword ? 'text' : 'password';
    toggleConfirm.textContent = isPassword ? '🙈' : '👁️';
  });

  // ─── Indicador de força da senha ───
  senhaInput.addEventListener('input', () => {
    const val = senhaInput.value;

    if (!val) {
      strengthWrap.classList.remove('visible');
      return;
    }

    strengthWrap.classList.add('visible');

    // Calcular força
    let score = 0;
    if (val.length >= 6)  score++;
    if (val.length >= 10) score++;
    if (/[A-Z]/.test(val)) score++;
    if (/[0-9]/.test(val)) score++;
    if (/[^A-Za-z0-9]/.test(val)) score++;

    // Classificar
    strengthFill.className = 'strength-fill';
    strengthText.className = 'strength-text';

    if (score <= 2) {
      strengthFill.classList.add('weak');
      strengthText.classList.add('weak');
      strengthText.textContent = 'Fraca';
    } else if (score <= 3) {
      strengthFill.classList.add('medium');
      strengthText.classList.add('medium');
      strengthText.textContent = 'Média';
    } else {
      strengthFill.classList.add('strong');
      strengthText.classList.add('strong');
      strengthText.textContent = 'Forte';
    }

    // Limpar erro se estiver digitando
    if (document.getElementById('senhaGroup').classList.contains('has-error')) {
      clearError('senhaGroup', 'senhaError');
    }
  });

  // ─── Validação em tempo real (limpar erros ao digitar) ───
  const fields = [
    { input: nomeInput,    group: 'nomeGroup',         error: 'nomeError' },
    { input: cpfInput,     group: 'cpfGroup',          error: 'cpfError' },
    { input: cnhInput,     group: 'cnhGroup',          error: 'cnhError' },
    { input: emailInput,   group: 'emailGroup',        error: 'emailError' },
    { input: confirmInput, group: 'confirmSenhaGroup', error: 'confirmSenhaError' },
    { input: celularInput, group: 'celularGroup',      error: 'celularError' },
  ];

  fields.forEach(({ input, group, error }) => {
    input.addEventListener('input', () => {
      if (document.getElementById(group).classList.contains('has-error')) {
        clearError(group, error);
      }
    });
  });

  // ─── Submissão do formulário ───
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Validar todos os campos
    const results = [
      validateNome(),
      validateCPF(),
      validateCNH(),
      validateEmail(),
      validateSenha(),
      validateConfirmSenha(),
      validateCelular(),
    ];

    const firstError = results.indexOf(false);
    if (firstError !== -1) {
      // Focar no primeiro campo com erro
      const errorInputs = [nomeInput, cpfInput, cnhInput, emailInput, senhaInput, confirmInput, celularInput];
      errorInputs[firstError].focus();
      return;
    }

    // Loading
    btnCadastro.classList.add('loading');
    btnCadastro.disabled = true;

    setTimeout(() => {
      form.submit();
    }, 600);
  });

  // ─── Funções de validação ───

  function validateNome() {
    const val = nomeInput.value.trim();
    if (!val) {
      setError('nomeGroup', 'nomeError', 'Informe seu nome completo.');
      return false;
    }
    if (val.length < 3) {
      setError('nomeGroup', 'nomeError', 'Nome deve ter no mínimo 3 caracteres.');
      return false;
    }
    clearError('nomeGroup', 'nomeError');
    return true;
  }

  function validateCPF() {
    const val = cpfInput.value.replace(/\D/g, '');
    if (!val) {
      setError('cpfGroup', 'cpfError', 'Informe seu CPF.');
      return false;
    }
    if (val.length !== 11) {
      setError('cpfGroup', 'cpfError', 'CPF deve ter 11 dígitos.');
      return false;
    }
    clearError('cpfGroup', 'cpfError');
    return true;
  }

  function validateCNH() {
    const val = cnhInput.value.replace(/\D/g, '');
    if (!val) {
      setError('cnhGroup', 'cnhError', 'Informe sua CNH.');
      return false;
    }
    if (val.length !== 11) {
      setError('cnhGroup', 'cnhError', 'CNH deve ter 11 dígitos.');
      return false;
    }
    clearError('cnhGroup', 'cnhError');
    return true;
  }

  function validateEmail() {
    const val = emailInput.value.trim();
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!val) {
      setError('emailGroup', 'emailError', 'Informe seu e-mail.');
      return false;
    }
    if (!regex.test(val)) {
      setError('emailGroup', 'emailError', 'Formato de e-mail inválido.');
      return false;
    }
    clearError('emailGroup', 'emailError');
    return true;
  }

  function validateSenha() {
    const val = senhaInput.value;
    if (!val) {
      setError('senhaGroup', 'senhaError', 'Informe uma senha.');
      return false;
    }
    if (val.length < 6) {
      setError('senhaGroup', 'senhaError', 'Mínimo de 6 caracteres.');
      return false;
    }
    clearError('senhaGroup', 'senhaError');
    return true;
  }

  function validateConfirmSenha() {
    const val = confirmInput.value;
    if (!val) {
      setError('confirmSenhaGroup', 'confirmSenhaError', 'Confirme sua senha.');
      return false;
    }
    if (val !== senhaInput.value) {
      setError('confirmSenhaGroup', 'confirmSenhaError', 'As senhas não coincidem.');
      return false;
    }
    clearError('confirmSenhaGroup', 'confirmSenhaError');
    return true;
  }

  function validateCelular() {
    const val = celularInput.value.replace(/\D/g, '');
    if (!val) {
      setError('celularGroup', 'celularError', 'Informe seu celular.');
      return false;
    }
    if (val.length < 10 || val.length > 11) {
      setError('celularGroup', 'celularError', 'Celular inválido.');
      return false;
    }
    clearError('celularGroup', 'celularError');
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
});
