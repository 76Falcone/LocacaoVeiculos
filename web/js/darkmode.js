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

  // Verifica se o usuário está logado/cadastrado
  const isUserRegistered = () => {
    return !!getCookie('usuarioLogado');
  };

  // Exibe toast animado personalizado informando que o tema escuro é exclusivo
  function showLockedToast() {
    let toast = document.getElementById('novare-lock-toast');
    if (!toast) {
      toast = document.createElement('div');
      toast.id = 'novare-lock-toast';
      toast.style.cssText = `
        position: fixed;
        bottom: 24px;
        left: 50%;
        transform: translateX(-50%) translateY(100px);
        background: #1A0E3D;
        color: #FAF8FF;
        padding: 12px 24px;
        border-radius: 12px;
        font-family: 'DM Sans', sans-serif;
        font-size: 0.9rem;
        font-weight: 500;
        box-shadow: 0 8px 32px rgba(106,38,205,0.25);
        border: 1.5px solid #6A26CD;
        z-index: 10000;
        transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275), opacity 0.3s ease;
        opacity: 0;
        display: flex;
        align-items: center;
        gap: 8px;
        pointer-events: none;
      `;
      toast.innerHTML = `<span>🔒</span> <span>O tema escuro é exclusivo para usuários cadastrados!</span>`;
      document.body.appendChild(toast);
    }
    
    // Mostra o toast
    setTimeout(() => {
      toast.style.transform = 'translateX(-50%) translateY(0)';
      toast.style.opacity = '1';
    }, 50);

    // Esconde o toast após 3.5 segundos
    setTimeout(() => {
      toast.style.transform = 'translateX(-50%) translateY(100px)';
      toast.style.opacity = '0';
    }, 3500);
  }

  // Aplica o tema antes do render para evitar flash
  function applyTheme(theme) {
    if (!isUserRegistered()) {
      theme = 'light';
    }
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
    if (!isUserRegistered()) {
      return 'light'; // Força tema claro se não estiver cadastrado/logado
    }
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
    const registered = isUserRegistered();
    console.log(`[DarkMode] Atualizando ${buttons.length} botões de toggle para o tema:`, theme, `Cadastrado: ${registered}`);
    buttons.forEach(btn => {
      if (!registered) {
        btn.setAttribute('aria-label', 'Tema escuro indisponível');
        btn.setAttribute('title', 'Cadastre-se para liberar o tema escuro');
        btn.innerHTML = '🔒';
        btn.style.opacity = '0.7';
        btn.style.cursor = 'not-allowed';
      } else {
        btn.setAttribute('aria-label', theme === 'dark' ? 'Ativar modo claro' : 'Ativar modo escuro');
        btn.setAttribute('title',      theme === 'dark' ? 'Modo claro' : 'Modo escuro');
        btn.innerHTML = theme === 'dark' ? '☀️' : '🌙';
        btn.style.opacity = '';
        btn.style.cursor = '';
      }
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
        if (!isUserRegistered()) {
          showLockedToast();
          return;
        }
        const current = document.documentElement.getAttribute('data-theme') || 'light';
        const nextTheme = current === 'dark' ? 'light' : 'dark';
        applyTheme(nextTheme);
      }
    });
  });
})();
