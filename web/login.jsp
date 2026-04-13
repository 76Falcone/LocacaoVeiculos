<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Novare | Login</title>
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
            max-width: 420px;
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

        input[type="email"],
        input[type="password"] {
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

        .link-cadastro {
            text-align: center;
            margin-top: 16px;
            font-size: 13px;
            color: #666;
        }

        .link-cadastro a {
            color: #6A26CD;
            text-decoration: none;
            font-weight: bold;
        }

        .link-cadastro a:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>

<div class="card">
    <h1>Entrar</h1>
    <p class="sub">Acesse sua conta na Novare.</p>

    <form action="ControleUsuario" method="post">
        <input type="hidden" name="op" value="LOGIN">

        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" placeholder="email@exemplo.com" required>

        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" placeholder="Sua senha" required>

        <button type="submit">Entrar</button>
    </form>

    <div class="link-cadastro">
        Não tem conta? <a href="cadastro.jsp">Cadastre-se</a>
    </div>
</div>

</body>
</html>
