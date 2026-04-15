package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;   
import javax.servlet.http.HttpServletResponse;

import dao.LocacaoDAO;
import dao.UsuarioDAO;
import dao.VeiculoDAO;
import model.Locacao;
import model.Usuario;
import model.Veiculo;

@WebServlet(name = "ControleLocacao", urlPatterns = { "/ControleLocacao", "/html/ControleLocacao" })
public class ControleLocacao extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String operacao = request.getParameter("op");

        try {
            LocacaoDAO dao = new LocacaoDAO();

            if (operacao == null) {
                response.sendRedirect("../index.html");

            } else if (operacao.equals("CADASTRAR")) {
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                int idVeiculo = Integer.parseInt(request.getParameter("idVeiculo"));
                LocalDate dataRetirada = LocalDate.parse(request.getParameter("dataRetirada"));
                LocalDate dataEntrega = request.getParameter("dataEntrega") != null
                        && !request.getParameter("dataEntrega").isEmpty()
                                ? LocalDate.parse(request.getParameter("dataEntrega"))
                                : null;

                int qtdDias = 1;
                try {
                    qtdDias = Integer.parseInt(request.getParameter("qtdDias"));
                } catch (NumberFormatException e) {
                }

                String localRetirada = request.getParameter("localRetirada");
                double seguroLocacao = Double.parseDouble(request.getParameter("seguroLocacao"));
                double valorTotal = Double.parseDouble(request.getParameter("valorTotal"));

                Usuario u = new Usuario();
                
                // --- PROTEÇÃO DE RESILIÊNCIA PARA ADMIN OU COOKIE ANTIGO ---
                // Se receber ID 1, verificamos se existe. Se não existir, pegamos um ID válido para teste
                UsuarioDAO uDao = new UsuarioDAO();
                Usuario target = new Usuario();
                target.setIdUsuario(idUsuario);
                
                if (uDao.visualizarUsuarioByID(target).getIdUsuario() == 0) {
                    List<Usuario> list = uDao.visualizarTodosUsuarios();
                    if (!list.isEmpty()) {
                        idUsuario = list.get(0).getIdUsuario();
                    }
                }
                
                u.setIdUsuario(idUsuario);

                Veiculo v = new Veiculo();
                v.setIdVeiculo(idVeiculo);

                Locacao locacao = new Locacao();
                locacao.setUsuario(u);
                locacao.setVeiculo(v);
                locacao.setDataRetirada(dataRetirada);
                locacao.setDataEntrega(dataEntrega);
                locacao.setQtdDias(qtdDias);
                locacao.setLocalRetirada(localRetirada);
                locacao.setSeguroLocacao(seguroLocacao);
                locacao.setValorTotal(valorTotal);

                dao.cadastrarLocacao(locacao);

                VeiculoDAO veiculoDAO = new VeiculoDAO();
                Veiculo vAtualizar = veiculoDAO.visualizarVeiculoByID(v);
                vAtualizar.setDisponibilidade(false);
                veiculoDAO.atualizarVeiculo(vAtualizar);

                response.sendRedirect("../index.html");

            } else if (operacao.equals("ATUALIZAR")) {
                int idLocacao = Integer.parseInt(request.getParameter("id"));
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                int idVeiculo = Integer.parseInt(request.getParameter("idVeiculo"));
                LocalDate dataRetirada = LocalDate.parse(request.getParameter("dataRetirada"));
                LocalDate dataEntrega = request.getParameter("dataEntrega") != null
                        && !request.getParameter("dataEntrega").isEmpty()
                                ? LocalDate.parse(request.getParameter("dataEntrega"))
                                : null;

                int qtdDias = Integer.parseInt(request.getParameter("qtdDias"));
                String localRetirada = request.getParameter("localRetirada");
                double seguroLocacao = Double.parseDouble(request.getParameter("seguroLocacao"));
                double valorTotal = Double.parseDouble(request.getParameter("valorTotal"));

                Usuario u = new Usuario();
                
                UsuarioDAO uDao = new UsuarioDAO();
                Usuario target = new Usuario();
                target.setIdUsuario(idUsuario);
                
                if (uDao.visualizarUsuarioByID(target).getIdUsuario() == 0) {
                    List<Usuario> list = uDao.visualizarTodosUsuarios();
                    if (!list.isEmpty()) {
                        idUsuario = list.get(0).getIdUsuario();
                    }
                }
                
                u.setIdUsuario(idUsuario);

                Veiculo v = new Veiculo();
                v.setIdVeiculo(idVeiculo);

                Locacao locacao = new Locacao();
                locacao.setIdLocacao(idLocacao);
                locacao.setUsuario(u);
                locacao.setVeiculo(v);
                locacao.setDataRetirada(dataRetirada);
                locacao.setDataEntrega(dataEntrega);
                locacao.setQtdDias(qtdDias);
                locacao.setLocalRetirada(localRetirada);
                locacao.setSeguroLocacao(seguroLocacao);
                locacao.setValorTotal(valorTotal);

                dao.atualizarLocacao(locacao);
                response.sendRedirect("listarReservas.html");

            } else if (operacao.equals("DELETAR")) {
                int idLocacao = Integer.parseInt(request.getParameter("id"));
                Locacao param = new Locacao();
                param.setIdLocacao(idLocacao);

                Locacao locacaoExistente = dao.visualizarLocacaoByID(param);

                dao.deletarLocacao(param);

                if (locacaoExistente.getVeiculo() != null) {
                    VeiculoDAO veiculoDAO = new VeiculoDAO();
                    Veiculo vLiberar = veiculoDAO.visualizarVeiculoByID(locacaoExistente.getVeiculo());
                    vLiberar.setDisponibilidade(true);
                    veiculoDAO.atualizarVeiculo(vLiberar);
                }

                response.sendRedirect("listarReservas.html");

            } else if (operacao.equals("LISTAR")) {
                List<Locacao> locacoes = dao.visualizarTodasLocacoes();
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < locacoes.size(); i++) {
                    Locacao l = locacoes.get(i);
                    if (i > 0)
                        json.append(",");
                    json.append("{");
                    json.append("\"idLocacao\":").append(l.getIdLocacao()).append(",");
                    json.append("\"idUsuario\":").append(l.getUsuario().getIdUsuario()).append(",");
                    json.append("\"nomeUsuario\":\"")
                            .append(l.getUsuario().getNomeUsuario() != null ? l.getUsuario().getNomeUsuario() : "")
                            .append("\",");
                    json.append("\"idVeiculo\":").append(l.getVeiculo().getIdVeiculo()).append(",");
                    json.append("\"modeloVeiculo\":\"")
                            .append(l.getVeiculo().getModeloVeiculo() != null ? l.getVeiculo().getModeloVeiculo() : "")
                            .append("\",");
                    json.append("\"qtdDias\":").append(l.getQtdDias()).append(",");
                    json.append("\"seguroLocacao\":").append(l.getSeguroLocacao()).append(",");
                    json.append("\"localRetirada\":\"").append(l.getLocalRetirada() != null ? l.getLocalRetirada() : "")
                            .append("\",");
                    json.append("\"valorTotal\":").append(l.getValorTotal()).append(",");
                    json.append("\"dataRetirada\":\"")
                            .append(l.getDataRetirada() != null ? l.getDataRetirada().toString() : "").append("\",");
                    json.append("\"dataEntrega\":\"")
                            .append(l.getDataEntrega() != null ? l.getDataEntrega().toString() : "").append("\"");
                    json.append("}");
                }
                json.append("]");
                out.print(json.toString());
                out.flush();
            } else if (operacao.equals("BUSCAR_POR_ID")) {
                int idLocacao = Integer.parseInt(request.getParameter("id"));
                Locacao param = new Locacao();
                param.setIdLocacao(idLocacao);

                Locacao locacao = dao.visualizarLocacaoByID(param);
                request.setAttribute("locacao", locacao);
                request.getRequestDispatcher("editarReserva.jsp").forward(request, response);
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