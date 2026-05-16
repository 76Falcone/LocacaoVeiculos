package command;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Padrão Command (GoF) — cada operação de negócio implementa esta interface
public interface IComando {
    void executar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception;
}
