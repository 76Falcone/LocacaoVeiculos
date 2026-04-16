<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Veiculo"%>
<%
    Veiculo veiculo = (Veiculo) request.getAttribute("veiculo");
    if (veiculo == null) {
        response.sendRedirect("html/listarVeiculos.html");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Novare | Editar Veículo</title>
  <meta name="description" content="Edite os dados de um veículo no sistema Novare.">

  <!-- Fontes -->
  <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:ital,opsz,wght@0,9..40,300;0,9..40,400;0,9..40,500;1,9..40,300&display=swap" rel="stylesheet">

  <!-- Estilos ajustados para a raiz do contexto de JSPs -->
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/variables.css">
  <link rel="stylesheet" href="css/dashboard.css">
  <link rel="stylesheet" href="css/cadastroVeiculo.css">
</head>
<body>

  <div class="dashboard">

    <!-- ─── SIDEBAR ─── -->
    <aside class="sidebar" id="sidebar">
      <a href="index.html" class="sidebar-logo">
        <span class="logo">NO<span>VARE</span></span>
      </a>

      <span class="sidebar-label">Gerenciamento</span>

      <nav class="sidebar-nav">
        <a href="html/listarVeiculos.html">
          <span class="nav-icon">🚘</span>
          Veículos
        </a>
        <a href="html/listarUsuarios.html">
          <span class="nav-icon">👤</span>
          Usuários
        </a>
        <a href="html/listarReservas.html">
          <span class="nav-icon">📋</span>
          Reservas
        </a>

        <span class="sidebar-label" style="padding-left:0; padding-right:0;">Cadastros</span>

        <a href="html/cadastroVeiculo.html">
          <span class="nav-icon">➕</span>
          Novo Veículo
        </a>
        <a href="html/reserva.html">
          <span class="nav-icon">📝</span>
          Nova Reserva
        </a>
      </nav>

      <div class="sidebar-footer">
        <a href="index.html">
          Sair do painel
        </a>
      </div>
    </aside>

    <!-- ─── OVERLAY MOBILE ─── -->
    <div class="sidebar-overlay" id="sidebarOverlay"></div>

    <!-- ─── CONTEÚDO PRINCIPAL ─── -->
    <div class="main-content">

      <!-- Topbar -->
      <header class="topbar">
        <div class="topbar-left">
          <button class="menu-toggle" id="menuToggle" aria-label="Abrir menu">☰</button>
          <div class="topbar-breadcrumb">
            <a href="html/listarVeiculos.html">Veículos</a>
            <span class="sep">›</span>
            <span>Editar veículo</span>
          </div>
        </div>
        <div class="topbar-user">
          <div class="topbar-user-avatar">AD</div>
          <span class="topbar-user-name">Admin</span>
        </div>
      </header>

      <!-- Área de conteúdo -->
      <main class="content-area">
        <h1 class="page-title">Editar Veículo</h1>
        <p class="page-subtitle">Altere os dados do veículo e salve as mudanças.</p>

        <!-- Card do formulário -->
        <div class="card form-card">
          <form id="editarVeiculoForm" action="ControleVeiculo" method="post" novalidate>
            <input type="hidden" name="op" value="ATUALIZAR">
            <input type="hidden" id="idVeiculo" name="id" value="<%= veiculo.getIdVeiculo() %>">

            <div class="form-row">
              <div class="form-group" id="placaGroup">
                <label for="placa">Placa</label>
                <div class="input-wrap">
                  <input
                    type="text"
                    id="placa"
                    name="placa"
                    placeholder="ABC-1D23"
                    maxlength="8"
                    value="<%= veiculo.getPlacaVeiculo() %>"
                    required
                  >
                </div>
                <span class="form-error" id="placaError"></span>
              </div>

              <div class="form-group" id="modeloGroup">
                <label for="modelo">Modelo</label>
                <div class="input-wrap">
                  <input
                    type="text"
                    id="modelo"
                    name="modelo"
                    placeholder="Ex: Toyota Corolla"
                    value="<%= veiculo.getModeloVeiculo() %>"
                    required
                  >
                </div>
                <span class="form-error" id="modeloError"></span>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group" id="corGroup">
                <label for="cor">Cor</label>
                <div class="input-wrap">
                  <input
                    type="text"
                    id="cor"
                    name="cor"
                    placeholder="Ex: Preto"
                    value="<%= veiculo.getCorVeiculo() %>"
                    required
                  >
                </div>
                <span class="form-error" id="corError"></span>
              </div>

              <div class="form-group" id="valorGroup">
                <label for="valorDiaria">Valor da Diária (R$)</label>
                <div class="input-wrap">
                  <input
                    type="number"
                    id="valorDiaria"
                    name="valorDiaria"
                    placeholder="0.00"
                    step="0.01"
                    min="0"
                    value="<%= veiculo.getValorDiaria() %>"
                    required
                  >
                </div>
                <span class="form-error" id="valorError"></span>
              </div>
            </div>

            <div class="form-group" id="funcionalidadeGroup">
              <label for="funcionalidade">Funcionalidade</label>
              <div class="input-wrap">
                <input
                  type="text"
                  id="funcionalidade"
                  name="funcionalidade"
                  placeholder="Ex: Passeio, Trabalho, Utilitário"
                  value="<%= veiculo.getFuncionalidadeVeiculo() %>"
                  required
                >
              </div>
              <span class="form-error" id="funcionalidadeError"></span>
            </div>

            <div class="form-row">
              <div class="form-group" id="disponibilidadeGroup">
                <label for="disponibilidade">Disponibilidade</label>
                <select id="disponibilidade" name="disponibilidade" class="form-select" required>
                  <option value="true" <%= veiculo.isDisponibilidade() ? "selected" : "" %>>Disponível</option>
                  <option value="false" <%= !veiculo.isDisponibilidade() ? "selected" : "" %>>Indisponível</option>
                </select>
              </div>

              <div class="form-group" id="arCondicionadoGroup">
                <label for="arCondicionado">Ar Condicionado</label>
                <select id="arCondicionado" name="arCondicionado" class="form-select" required>
                  <option value="true" <%= veiculo.isArCondicionadoVeiculo() ? "selected" : "" %>>Sim</option>
                  <option value="false" <%= !veiculo.isArCondicionadoVeiculo() ? "selected" : "" %>>Não</option>
                </select>
              </div>
            </div>

            <div class="form-group" id="tipoCambioGroup">
              <label for="tipoCambio">Tipo de Câmbio</label>
              <select id="tipoCambio" name="tipoCambio" class="form-select" required>
                <option value="">Selecione...</option>
                <option value="Automático" <%= "Automático".equalsIgnoreCase(veiculo.getTipoCambio()) ? "selected" : "" %>>Automático</option>
                <option value="Manual" <%= "Manual".equalsIgnoreCase(veiculo.getTipoCambio()) ? "selected" : "" %>>Manual</option>
              </select>
              <span class="form-error" id="tipoCambioError"></span>
            </div>

            <button type="submit" class="btn-submit" id="btnSalvar">
              <span class="btn-text">Salvar Alterações</span>
              <span class="btn-loader" aria-hidden="true"></span>
            </button>
          </form>
        </div>
      </main>

    </div>
  </div>

  <!-- Toast de feedback -->
  <div class="toast" id="toast"></div>

  <script src="js/editarVeiculo.js"></script>
</body>
</html>
