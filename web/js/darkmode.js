/* ═══════════════════════════════════════════════════════════
   DARKMODE.JS — Toggle claro/escuro com persistência
   Novare · Sistema de Gestão de Veículos
   ═══════════════════════════════════════════════════════════ */

(function () {
  const STORAGE_KEY = 'novare-theme';

  // Helper para obter cookies
  const getCookie = (name) => {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : null;
  };

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
    // Padrão do sistema agora é o tema escuro (dark)
    return 'dark';
  }

  function updateAllButtons(theme) {
    const buttons = document.querySelectorAll('.btn-dark-toggle');
    console.log(`[DarkMode] Atualizando ${buttons.length} botões de toggle para o tema:`, theme);
    buttons.forEach(btn => {
      btn.setAttribute('aria-label', theme === 'dark' ? 'Ativar modo claro' : 'Ativar modo escuro');
      btn.setAttribute('title',      theme === 'dark' ? 'Modo claro' : 'Modo escuro');
      btn.innerHTML = theme === 'dark' ? '☀️' : '🌙';
      btn.style.opacity = '';
      btn.style.cursor = '';
    });
  }

  // Aplica imediatamente (antes do DOMContentLoaded para evitar flash)
  applyTheme(getTheme());

  document.addEventListener('DOMContentLoaded', () => {
    // Verifica se variables.css está vinculado na página
    const links = Array.from(document.querySelectorAll('link[rel="stylesheet"]'));
    const hasVariables = links.some(link => {
      const href = link.getAttribute('href');
      return href && href.includes('variables.css');
    });
    if (!hasVariables) {
      console.error('[DarkMode] ERRO: O arquivo "variables.css" não está vinculado na tag <head> desta página! O modo escuro não funcionará.');
    } else {
      console.log('[DarkMode] Sucesso: "variables.css" está corretamente vinculado.');
    }

    // --- Atualização do Nome do Usuário Logado ---
    const nomeRaw = getCookie('nomeUsuario');
    if (nomeRaw) {
      try {
        const nomeReal = decodeURIComponent(nomeRaw).replace(/\+/g, ' ');
        
        // Atualiza o texto do nome
        const userNameEl = document.querySelector('.topbar-user-name');
        if (userNameEl) {
          userNameEl.textContent = nomeReal;
        }

        // Atualiza as iniciais no avatar
        const userAvatarEl = document.querySelector('.topbar-user-avatar');
        if (userAvatarEl) {
          const parts = nomeReal.trim().split(/\s+/);
          let initials = 'AD';
          if (parts.length >= 2) {
            initials = (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
          } else if (parts.length === 1 && parts[0].length > 0) {
            initials = parts[0].slice(0, Math.min(2, parts[0].length)).toUpperCase();
          }
          userAvatarEl.textContent = initials;
        }
      } catch (err) {
        console.warn('[DarkMode] Erro ao decodificar cookie nomeUsuario:', err);
      }
    }

    // Atualiza botões já existentes no DOM
    updateAllButtons(getTheme());

    // Delegação de evento — funciona para qualquer botão com a classe
    document.addEventListener('click', (e) => {
      const btn = e.target.closest('.btn-dark-toggle');
      if (btn) {
        console.log('[DarkMode] Botão clicado!');
        const current = document.documentElement.getAttribute('data-theme') || 'dark';
        const nextTheme = current === 'dark' ? 'light' : 'dark';
        applyTheme(nextTheme);
      }
    });
  });
})();
