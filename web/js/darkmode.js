/* ═══════════════════════════════════════════════════════════
   DARKMODE.JS — Toggle claro/escuro com persistência
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

(function () {
  const STORAGE_KEY = 'novare-theme';

  // Aplica o tema antes do render para evitar flash
  function applyTheme(theme) {
    console.log('[DarkMode] Aplicando tema:', theme);
    document.documentElement.setAttribute('data-theme', theme);
    try {
      localStorage.setItem(STORAGE_KEY, theme);
    } catch (e) {
      console.warn('[DarkMode] Storage do tema indisponível:', e);
    }
    updateAllButtons(theme);
  }

  function getTheme() {
    let saved = null;
    try {
      saved = localStorage.getItem(STORAGE_KEY);
    } catch (e) {
      console.warn('[DarkMode] Storage do tema indisponível:', e);
    }
    if (saved) return saved;
    // Respeita preferência do sistema
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }

  function updateAllButtons(theme) {
    const buttons = document.querySelectorAll('.btn-dark-toggle');
    console.log(`[DarkMode] Atualizando ${buttons.length} botões de toggle para o tema:`, theme);
    buttons.forEach(btn => {
      btn.setAttribute('aria-label', theme === 'dark' ? 'Ativar modo claro' : 'Ativar modo escuro');
      btn.setAttribute('title',      theme === 'dark' ? 'Modo claro' : 'Modo escuro');
      btn.innerHTML = theme === 'dark' ? '☀️' : '🌙';
    });
  }

  // Aplica imediatamente (antes do DOMContentLoaded para evitar flash)
  applyTheme(getTheme());

  document.addEventListener('DOMContentLoaded', () => {
    // Verifica se variables.css está vinculado na página
    const links = Array.from(document.querySelectorAll('link[rel="stylesheet"]'));
    const hasVariables = links.some(link => link.getAttribute('href').includes('variables.css'));
    if (!hasVariables) {
      console.error('[DarkMode] ERRO: O arquivo "variables.css" não está vinculado na tag <head> desta página! O modo escuro não funcionará.');
    } else {
      console.log('[DarkMode] Sucesso: "variables.css" está corretamente vinculado.');
    }

    // Atualiza botões já existentes no DOM
    updateAllButtons(getTheme());

    // Delegação de evento — funciona para qualquer botão com a classe
    document.addEventListener('click', (e) => {
      const btn = e.target.closest('.btn-dark-toggle');
      if (btn) {
        console.log('[DarkMode] Botão clicado!');
        const current = document.documentElement.getAttribute('data-theme') || 'light';
        const nextTheme = current === 'dark' ? 'light' : 'dark';
        applyTheme(nextTheme);
      }
    });
  });
})();
