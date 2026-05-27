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

// Command: cadastra um novo usuário
public class CadastrarUsuarioComando implements IComando {

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf").replaceAll("[^0-9]", "");
        String cnh = request.getParameter("cnh").replaceAll("[^0-9]", "");
        String email = request.getParameter("email");
        String senhaRaw = request.getParameter("senha");
        String senha = HashUtil.sha256(senhaRaw); // Hasheando a senha antes de salvar
        String celular = request.getParameter("celular");

        Usuario u = new UsuarioBuilder()
                .comNomeUsuario(nome)
                .comCpfUsuario(cpf)
                .comCnhUsuario(cnh)
                .comEmailUsuario(email)
                .comSenhaUsuario(senha)
                .comCelularUsuario(celular)
                .build();

        IUsuarioDAO dao = DAOFactory.getUsuarioDAO();
        dao.cadastrarUsuario(u);

        response.sendRedirect(request.getContextPath() + "/index.html");
    }
}
