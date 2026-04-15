<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Novare | Operação Concluída</title>
  <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@400;500;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="css/variables.css">
  <link rel="stylesheet" href="css/sucesso.css">
</head>
<body>
  <div class="container">
    <div class="card">
      <div class="icon">
        <svg width="72" height="72" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
          <polyline points="22 4 12 14.01 9 11.01" />
        </svg>
      </div>
      <h1>Operação realizada com sucesso!</h1>
      <p>Os dados do usuário foram salvos corretamente no sistema Novare.</p>
      <div class="actions">
        <a href="html/listarUsuarios.html" class="btn-primary">Ver Usuários</a>
        <a href="html/cadastroUsuario.html" class="btn-secondary">Cadastrar Outro</a>
      </div>
    </div>
  </div>
</body>
</html>
