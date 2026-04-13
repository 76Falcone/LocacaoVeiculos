<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Novare | Resultado Cadastro</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        .card {
            background: #ffffff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            padding: 50px 40px;
            width: 100%;
            max-width: 420px;
            text-align: center;
        }

        .icon {
            font-size: 60px;
            margin-bottom: 20px;
        }

        h2 {
            color: #333;
            font-size: 20px;
            margin-bottom: 30px;
        }

        a.button {
            display: inline-block;
            background-color: #6A26CD;
            color: #fff;
            padding: 12px 28px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            font-size: 14px;
            transition: background 0.2s;
            margin: 5px;
        }

        a.button:hover {
            background-color: #5519a8;
        }

        a.button.outline {
            background-color: #fff;
            color: #6A26CD;
            border: 2px solid #6A26CD;
        }

        a.button.outline:hover {
            background-color: #f0eaff;
        }
    </style>
</head>
<body>

<div class="card">
    <div class="icon">✅</div>
    <h2>${msg}</h2>
    <a href="login.jsp" class="button">Fazer login</a>
    <a href="index.html" class="button outline">Voltar ao início</a>
</div>

</body>
</html>
