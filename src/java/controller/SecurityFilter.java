package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filtro de Segurança e Controle de Acesso da Novare.
 * Protege áreas confidenciais e operações restritas com base no papel do usuário.
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath.length());
        
        // Evita loop infinito em recursos estáticos, tela de login, cadastro público e imagens
        if (path.endsWith(".css") || path.endsWith(".js") || path.contains("/img/") || 
            path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".svg") ||
            path.endsWith("login.html") || path.endsWith("cadastroUsuario.html")) {
            chain.doFilter(req, res);
            return;
        }

        // Obter sessão atual
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("usuarioLogado") != null);
        String role = loggedIn ? (String) session.getAttribute("role") : null;
        boolean isAdmin = "admin".equals(role);
        
        boolean needsLogin = false;
        boolean needsAdmin = false;
        
        // 1. Verificação de Páginas Estáticas e JSPs
        if (path.contains("/html/") || path.endsWith(".jsp") || path.endsWith(".html")) {
            // Páginas restritas a qualquer usuário autenticado
            if (path.contains("minhasLocacoes.html") || path.contains("reserva.html") || path.contains("sucessoReserva.jsp")) {
                needsLogin = true;
            }
            // Páginas restritas a administradores
            if (path.contains("listarVeiculos.html") || path.contains("listarUsuarios.html") || 
                path.contains("listarReservas.html") || path.contains("cadastroVeiculo.html") || 
                path.contains("editarVeiculo.jsp") || path.contains("editarUsuario.jsp") || 
                path.contains("sucessoVeiculo.jsp") || path.contains("sucessoUsuario.jsp")) {
                needsLogin = true;
                needsAdmin = true;
            }
        }
        
        // 2. Verificação de Operações nos Servlets (Backend)
        String op = request.getParameter("op");
        if (op != null) {
            // Controle de Locações
            if (path.contains("ControleLocacao")) {
                if ("CADASTRAR".equals(op) || "ATUALIZAR".equals(op) || "DELETAR".equals(op) || 
                    "LISTAR_MINHAS".equals(op) || "BUSCAR_POR_ID".equals(op)) {
                    needsLogin = true;
                }
                if ("LISTAR".equals(op)) {
                    needsLogin = true;
                    needsAdmin = true;
                }
            }
            // Controle de Veículos
            if (path.contains("ControleVeiculo")) {
                if ("CADASTRAR".equals(op) || "ATUALIZAR".equals(op) || "DELETAR".equals(op) || "EDITAR".equals(op)) {
                    needsLogin = true;
                    needsAdmin = true;
                }
            }
            // Controle de Usuários
            if (path.contains("ControleUsuario")) {
                if ("LISTAR".equals(op) || "DELETAR".equals(op)) {
                    needsLogin = true;
                    needsAdmin = true;
                }
                if ("BUSCAR_POR_ID".equals(op) || "ATUALIZAR".equals(op)) {
                    needsLogin = true;
                }
            }
        }
        
        // 3. Aplicação das Regras de Acesso
        if (needsLogin && !loggedIn) {
            // Se for requisição AJAX/JSON, retorna 401
            String acceptHeader = request.getHeader("Accept");
            if ((acceptHeader != null && acceptHeader.contains("application/json")) || 
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"erro\":\"Acesso restrito. Por favor, faça login para continuar.\"}");
                return;
            }
            // Caso contrário, redireciona para login com parâmetro erro=2
            response.sendRedirect(contextPath + "/html/login.html?erro=2");
            return;
        }
        
        if (needsAdmin && !isAdmin) {
            // Se for requisição AJAX/JSON, retorna 403
            String acceptHeader = request.getHeader("Accept");
            if ((acceptHeader != null && acceptHeader.contains("application/json")) || 
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"erro\":\"Acesso negado. Apenas administradores podem realizar esta ação.\"}");
                return;
            }
            // Para navegação normal, exibe a tela de erro customizada
            request.setAttribute("mensagem", "Acesso Negado: Esta área é restrita a administradores.");
            request.getRequestDispatcher("/erro.jsp").forward(request, response);
            return;
        }
        
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}
