<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Novare | Cadastro</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 20px;
        }

        .card {
            background: #ffffff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            padding: 40px;
            width: 100%;
            max-width: 500px;
        }

        .card h1 {
            color: #6A26CD;
            font-size: 24px;
            margin-bottom: 6px;
        }

        .card p.sub {
            color: #888;
            font-size: 14px;
            margin-bottom: 28px;
        }

        label {
            display: block;
            font-size: 13px;
            font-weight: bold;
            color: #444;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="tel"] {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 16px;
            transition: border-color 0.2s;
        }

        input:focus {
            outline: none;
            border-color: #6A26CD;
        }

        button[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #6A26CD;
            color: #fff;
            border: none;
            border-radius: 6px;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.2s;
        }

        button[type="submit"]:hover {
            background-color: #5519a8;
        }

        .link-login {
            text-align: center;
            margin-top: 16px;
            font-size: 13px;
            color: #666;
        }

        .link-login a {
            color: #6A26CD;
            text-decoration: none;
            font-weight: bold;
        }

        .link-login a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="card">
    <h1>Criar conta</h1>
    <p class="sub">Preencha os dados para se cadastrar na Novare.</p>

    <form action="ControleUsuario" method="post">
        <input type="hidden" name="op" value="CADASTRAR">

        <label for="nome">Nome completo</label>
        <input type="text" id="nome" name="nome" placeholder="Ex: Mizuta" required>

        <label for="cpf">CPF</label>
        <input type="text" id="cpf" name="cpf" placeholder="Somente números (11 dígitos)" maxlength="11" required>

        <label for="cnh">CNH</label>
        <input type="text" id="cnh" name="cnh" placeholder="Somente números (11 dígitos)" maxlength="11" required>

        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" placeholder="mizuta@exemplo.com" required>

        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" placeholder="Mínimo 6 caracteres" required>

        <label for="celular">Celular</label>
        <input type="tel" id="celular" name="celular" placeholder="Ex: 11999999999" maxlength="15" required>

        <button type="submit">Cadastrar</button>
    </form>

    <div class="link-login">
        Já tem conta? <a href="login.jsp">Entrar</a>
    </div>
</div>

</body>
</html>
