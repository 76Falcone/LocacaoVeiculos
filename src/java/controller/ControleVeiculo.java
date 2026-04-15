package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.VeiculoDAO;
import model.Veiculo;

@WebServlet(name = "ControleVeiculo", urlPatterns = { "/ControleVeiculo", "/html/ControleVeiculo" })
public class ControleVeiculo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacao = request.getParameter("op");

        try {
            VeiculoDAO dao = new VeiculoDAO();

            if (operacao == null) {
                response.sendRedirect(request.getContextPath() + "/html/listarVeiculos.html");

            } else if (operacao.equals("CADASTRAR")) {
                String placa = request.getParameter("placa").replaceAll("[^A-Za-z0-9]", ""); // Remove hífen e
                                                                                             // caracteres extras
                String modelo = request.getParameter("modelo");
                String cor = request.getParameter("cor");
                double valorDiaria = Double.parseDouble(request.getParameter("valorDiaria"));
                String funcionalidade = request.getParameter("funcionalidade");
                boolean arCondicionado = "true".equals(request.getParameter("arCondicionado"));
                boolean disponibilidade = "true".equals(request.getParameter("disponibilidade"));
                String cambio = request.getParameter("tipoCambio"); // Corrigido: era "cambio", mas o HTML envia
                                                                    // "tipoCambio"

                Veiculo v = new Veiculo();
                v.setPlacaVeiculo(placa);
                v.setModeloVeiculo(modelo);
                v.setCorVeiculo(cor);
                v.setValorDiaria(valorDiaria);
                v.setFuncionalidadeVeiculo(funcionalidade);
                v.setArCondicionadoVeiculo(arCondicionado);
                v.setDisponibilidade(disponibilidade);
                v.setTipoCambio(cambio);

                dao.cadastrarVeiculo(v);
                response.sendRedirect(request.getContextPath() + "/sucessoVeiculo.jsp");

            } else if (operacao != null && (operacao.equals("ATUALIZAR") || operacao.equals("EDITAR"))) {
                // Aceitar tanto 'id' quanto 'idVeiculo' para compatibilidade
                String idParam = request.getParameter("id");
                if (idParam == null) {
                    idParam = request.getParameter("idVeiculo");
                }
                int idVeiculo = Integer.parseInt(idParam);
                String placa = request.getParameter("placa").replaceAll("[^A-Za-z0-9]", "");
                String modelo = request.getParameter("modelo");
                String cor = request.getParameter("cor");
                double valorDiaria = Double.parseDouble(request.getParameter("valorDiaria"));
                String funcionalidade = request.getParameter("funcionalidade");
                boolean arCondicionado = "true".equals(request.getParameter("arCondicionado"));
                boolean disponibilidade = "true".equals(request.getParameter("disponibilidade"));
                String cambio = request.getParameter("tipoCambio");

                Veiculo v = new Veiculo();
                v.setIdVeiculo(idVeiculo);
                v.setPlacaVeiculo(placa);
                v.setModeloVeiculo(modelo);
                v.setCorVeiculo(cor);
                v.setValorDiaria(valorDiaria);
                v.setFuncionalidadeVeiculo(funcionalidade);
                v.setArCondicionadoVeiculo(arCondicionado);
                v.setDisponibilidade(disponibilidade);
                v.setTipoCambio(cambio);

                dao.atualizarVeiculo(v);
                response.sendRedirect(request.getContextPath() + "/sucessoVeiculo.jsp");

            } else if (operacao.equals("DELETAR")) {
                int idVeiculo = Integer.parseInt(request.getParameter("id"));
                Veiculo v = new Veiculo();
                v.setIdVeiculo(idVeiculo);

                dao.deletarVeiculo(v);
                response.sendRedirect(request.getContextPath() + "/sucessoVeiculo.jsp");

            } else if (operacao.equals("LISTAR")) {
                List<Veiculo> veiculos = dao.visualizarTodosVeiculos();
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < veiculos.size(); i++) {
                    Veiculo v = veiculos.get(i);
                    if (i > 0)
                        json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(v.getIdVeiculo()).append(",");
                    json.append("\"placa\":\"").append(v.getPlacaVeiculo()).append("\",");
                    json.append("\"modelo\":\"").append(v.getModeloVeiculo()).append("\",");
                    json.append("\"cor\":\"").append(v.getCorVeiculo()).append("\",");
                    json.append("\"valorDiaria\":").append(v.getValorDiaria()).append(",");
                    json.append("\"funcionalidade\":\"").append(v.getFuncionalidadeVeiculo()).append("\",");
                    json.append("\"disponibilidade\":").append(v.isDisponibilidade()).append(",");
                    json.append("\"arCondicionado\":").append(v.isArCondicionadoVeiculo()).append(",");
                    json.append("\"tipoCambio\":\"").append(v.getTipoCambio()).append("\"");
                    json.append("}");
                }
                json.append("]");
                out.print(json.toString());
                out.flush();

            } else if (operacao.equals("BUSCAR_POR_ID")) {
                int idVeiculo = Integer.parseInt(request.getParameter("id"));
                Veiculo param = new Veiculo();
                param.setIdVeiculo(idVeiculo);

                Veiculo veiculo = dao.visualizarVeiculoByID(param);
                request.setAttribute("veiculo", veiculo);
                request.getRequestDispatcher("editarVeiculo.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro na operacao " + operacao + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao processar requisição: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}