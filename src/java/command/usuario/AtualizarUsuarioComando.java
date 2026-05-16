package command.usuario;

import command.IComando;
import dao.DAOFactory;
import dao.IUsuarioDAO;
import model.Usuario;
import model.UsuarioBuilder;

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

        Usuario u = new UsuarioBuilder()
                .comId(idUsuario)
                .comNomeUsuario(nome)
                .comCpfUsuario(cpf)
                .comCnhUsuario(cnh)
                .comEmailUsuario(email)
                .comSenhaUsuario(senha)
                .comCelularUsuario(celular)
                .build();

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();
        dao.atualizarUsuario(u);

        response.sendRedirect(request.getContextPath() + "/sucessoUsuario.jsp");
    }
}
