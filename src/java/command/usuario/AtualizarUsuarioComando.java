package command.usuario;

import command.IComando;
import dao.DAOFactory;
import dao.IUsuarioDAO;
import model.Usuario;
import model.UsuarioBuilder;
import util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Command: atualiza os dados de um usuário existente
public class AtualizarUsuarioComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        int idUsuario = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf").replaceAll("[^0-9]", "");
        String cnh = request.getParameter("cnh").replaceAll("[^0-9]", "");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String celular = request.getParameter("celular");
        boolean isAdmin = "true".equals(request.getParameter("isAdmin"));

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();

        // Se a senha estiver vazia (ex: vindo da tela HTML que não edita senha),
        // recuperamos a senha atual do banco de dados para não sobrescrever com nulo/vazio.
        if (senha == null || senha.trim().isEmpty()) {
            Usuario usuarioExistente = dao.visualizarUsuarioByID(new UsuarioBuilder().comId(idUsuario).build());
            if (usuarioExistente != null) {
                senha = usuarioExistente.getSenhaUsuario(); // já está em hash, mantém
            }
        } else {
            senha = HashUtil.sha256(senha); // Hasheando a nova senha
        }

        Usuario u = new UsuarioBuilder()
                .comId(idUsuario)
                .comNomeUsuario(nome)
                .comCpfUsuario(cpf)
                .comCnhUsuario(cnh)
                .comEmailUsuario(email)
                .comSenhaUsuario(senha)
                .comCelularUsuario(celular)
                .comAdmin(isAdmin)
                .build();

        dao.atualizarUsuario(u);

        response.sendRedirect(request.getContextPath() + "/sucessoUsuario.jsp");
    }
}
