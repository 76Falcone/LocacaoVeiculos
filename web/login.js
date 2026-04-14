/* ═══════════════════════════════════════════════════════════
   LOGIN.JS — Lógica e validações da tela de login
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', () => {
  // ─── Elementos do DOM ───
  const form       = document.getElementById('loginForm');
  const emailInput = document.getElementById('email');
  const senhaInput = document.getElementById('senha');
  const btnLogin   = document.getElementById('btnLogin');
  const toggleBtn  = document.getElementById('toggleSenha');

  // ─── Toggle mostrar/esconder senha ───
  toggleBtn.addEventListener('click', () => {
    const isPassword = senhaInput.type === 'password';
    senhaInput.type = isPassword ? 'text' : 'password';
    toggleBtn.textContent = isPassword ? '🙈' : '👁️';
    toggleBtn.setAttribute('aria-label', isPassword ? 'Esconder senha' : 'Mostrar senha');
  });

  // ─── Validação em tempo real: E-mail ───
  emailInput.addEventListener('blur', () => {
    validateEmail();
  });

  emailInput.addEventListener('input', () => {
    // Remove erro enquanto digita
    const group = document.getElementById('emailGroup');
    if (group.classList.contains('has-error')) {
      clearError('emailGroup', 'emailError');
    }
  });

  // ─── Validação em tempo real: Senha ───
  senhaInput.addEventListener('blur', () => {
    validateSenha();
  });

  senhaInput.addEventListener('input', () => {
    const group = document.getElementById('senhaGroup');
    if (group.classList.contains('has-error')) {
      clearError('senhaGroup', 'senhaError');
    }
  });

  // ─── Submissão do formulário ───
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Validar todos os campos
    const isEmailValid = validateEmail();
    const isSenhaValid = validateSenha();

    if (!isEmailValid || !isSenhaValid) {
      // Focar no primeiro campo com erro
      if (!isEmailValid) {
        emailInput.focus();
      } else {
        senhaInput.focus();
      }
      return;
    }

    // Estado de loading
    btnLogin.classList.add('loading');
    btnLogin.disabled = true;

    // Submeter o formulário após breve delay (feedback visual)
    setTimeout(() => {
      form.submit();
    }, 600);
  });

  // ─── Funções de validação ───

  /**
   * Valida o campo de e-mail
   * @returns {boolean} true se válido
   */
  function validateEmail() {
    const value = emailInput.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!value) {
      setError('emailGroup', 'emailError', 'Informe seu e-mail.');
      return false;
    }

    if (!emailRegex.test(value)) {
      setError('emailGroup', 'emailError', 'Formato de e-mail inválido.');
      return false;
    }

    clearError('emailGroup', 'emailError');
    return true;
  }

  /**
   * Valida o campo de senha
   * @returns {boolean} true se válido
   */
  function validateSenha() {
    const value = senhaInput.value;

    if (!value) {
      setError('senhaGroup', 'senhaError', 'Informe sua senha.');
      return false;
    }

    if (value.length < 6) {
      setError('senhaGroup', 'senhaError', 'A senha deve ter no mínimo 6 caracteres.');
      return false;
    }

    clearError('senhaGroup', 'senhaError');
    return true;
  }

  // ─── Helpers de erro ───

  /**
   * Exibe mensagem de erro em um campo
   * @param {string} groupId - ID do form-group
   * @param {string} errorId - ID do span de erro
   * @param {string} message - Mensagem de erro
   */
  function setError(groupId, errorId, message) {
    const group = document.getElementById(groupId);
    const error = document.getElementById(errorId);
    group.classList.add('has-error');
    error.textContent = message;
  }

  /**
   * Limpa mensagem de erro de um campo
   * @param {string} groupId - ID do form-group
   * @param {string} errorId - ID do span de erro
   */
  function clearError(groupId, errorId) {
    const group = document.getElementById(groupId);
    const error = document.getElementById(errorId);
    group.classList.remove('has-error');
    error.textContent = '';
  }
});
