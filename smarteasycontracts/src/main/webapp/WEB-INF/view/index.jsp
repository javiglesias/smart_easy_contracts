<!doctype html>
<html lang="es">
<head>
    <meta charset="ISO-8859-1"/>
    <title>Smart & easy contracts</title>
	<link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/1200px-Ethereum_logo_2014.svg.png">
	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />
    <style><%@include file="/assets/css/bootstrap.min.css"%></style>
    <style><%@include file="/assets/css/light-bootstrap-dashboard.css"%></style>
    <style><%@include file="/assets/css/mui.min.css"%></style>
	<link href="/assets/css/main.css" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<style>
	    #logo {
	        position: block;
	        width: 25%;
	    }
	</style>
</head>
<body>

<div class="sidebar">
	<div class="sidebar-wrapper">
	       <div class="logo">
	      <img id="logo" src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/1200px-Ethereum_logo_2014.svg.png">
	      <a href="/index" class="simple-text">
	          Smart & Easy Contracts
	      </a>
	      </div>
	      <ul class="nav">
	          <%String login = "",
                  registration = "",
                  createWallet="",
                  deployContracts="",
                  compile="",
                  profile = "";
              if(session.getAttribute("uname") == null){
                    switch(request.getRequestURL().toString().split("/")[5].replace(".jsp", "")){
                        case "registration":
                            registration = "class=\"active\"";
                            break;
                        case "login":
                            login = "class=\"active\"";
                            break;
                        default:
                            break;
                    }
                    out.print("<li><a "+registration+" href=\"registration\">Registro</a></li>");
                    out.print("<li><a "+login+" href=\"login\">Entrar</a></li>");
                } else {
                    switch(request.getRequestURL().toString().split("/")[5].replace(".jsp", "")){
                        case "createWallet":
                            createWallet = "class=\"active\"";
                            break;
                        case "deployContracts":
                            deployContracts = "class=\"active\"";
                            break;
                        case "compile":
                            compile = "class=\"active\"";
                            break;
                        case "profile":
                            profile = "class=\"active\"";
                            break;
                        default:
                            break;
                    }
                }
                    if(session.getAttribute("uname") != null)
                        out.print("<li><a "+ profile +" href=\"profile\">Mi Perfil</a></li>");
                    if(session.getAttribute("wallet") != null) {
                        out.print("<li><a "+createWallet+" href=\"createWallet\">Gestion de cartera</a></li>");
                        out.print("<li><a "+deployContracts+" href=\"deployContracts\">Desplegar contrato</a></li>");
                        out.print("<li><a "+compile+" href=\"compile\">Compilacion Online</a></li>");
                    }
                    if(session.getAttribute("uname") != null)
          	            out.print("<li><a href=\"logout\">Log out</a></li>");
      	         %>
	        </ul>
		</div>
	</div>
</body>
</html>
