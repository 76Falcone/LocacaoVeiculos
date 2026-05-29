<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br" data-theme="dark">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Novare | Ops! Ocorreu um Erro</title>
  <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@400;500;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="css/style.css?v=2">
  <link rel="stylesheet" href="css/variables.css?v=2">
  <link rel="stylesheet" href="css/erro.css">
</head>
<body>
  <!-- Botão Dark Mode Flutuante -->
  <button class="btn-dark-toggle" id="darkToggle" aria-label="Ativar modo escuro" title="Modo escuro" style="position: fixed; top: 20px; right: 20px; z-index: 1000;">🌙</button>

  <%
      String msg = (String) request.getAttribute("mensagem");
      if (msg == null || msg.trim().isEmpty()) {
          msg = request.getParameter("mensagem");
      }
      if (msg == null || msg.trim().isEmpty()) {
          msg = (String) request.getAttribute("javax.servlet.error.message");
      }
      if (msg == null || msg.trim().isEmpty()) {
          Object exc = request.getAttribute("javax.servlet.error.exception");
          if (exc instanceof Throwable) {
              msg = ((Throwable) exc).getMessage();
          }
      }
      if (msg == null || msg.trim().isEmpty()) {
          msg = "Ocorreu um erro desconhecido ao processar sua solicitação.";
      }
  %>

  <div class="container">
    <div class="card">
      <div class="icon">
        <svg width="72" height="72" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="10" />
          <line x1="12" y1="8" x2="12" y2="12" />
          <line x1="12" y1="16" x2="12.01" y2="16" />
        </svg>
      </div>
      <h1>Ops! Ocorreu um erro</h1>
      <p class="error-desc">Não foi possível completar a operação solicitada:</p>
      
      <p class="error-msg"><%= msg %></p>
      
      <div class="actions">
        <a href="javascript:history.back()" class="btn-primary">Voltar e Corrigir</a>
        <a href="index.html" class="btn-secondary">Voltar ao Início</a>
      </div>
    </div>
  </div>
  <script src="js/darkmode.js?v=2"></script>
</body>
</html>
