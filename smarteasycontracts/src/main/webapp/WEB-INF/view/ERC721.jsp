<h2>ERC-721</h2>
<%
	if(session.getAttribute("wallet") == null)
		out.print("No ha cargado ninguna cartera, por favor cargue una antes de desplegar.");
	else out.print("Este contrato se desplegara con la wallet: "+session.getAttribute("wallet"));
%>
<form action="deploy" method="POST" class="form-inline" role="form">
	<div class="row">
		<div class="col-md-2"><label>Nombre del token: </label></div>
		<div class="col-md-2"><input type="text" name="TokenName" class="form-control"></div>
	</div>
	<div class="row">
		<div class="col-md-2"><label>Simbolo del token: </label></div>
		<div class="col-md-2"><input type="text" name="SYM" class="form-control"></div>
	</div>
	<div class="row">
		<div class="col-md-2"><label>Decimales: </label></div>
		<div class="col-md-2"><input type="text" name="Decimals" class="form-control"></div>
	</div>
	<div class="row">
		<div class="col-md-2"><label>Total supply: </label></div>
		<div class="col-md-2"><input type="text" name="totalSypply" class="form-control"></div>
	</div>
	<div class="row">
		<div class="col-md-2"><label>Contraseña de la cartera</label></div>
		<div class="col-md-2"><input type="password" class="form-control" name="pwd"></div>
	</div>
	<input type="submit" id="button" class="btn-success btn-lg" value="Desplegar">
</form>
