<h2>Contrato para la comprobacion de integridad de ficheros.</h2>
<%
	if(session.getAttribute("wallet") == null)
		out.print("No ha cargado ninguna cartera, por favor cargue una antes de desplegar.");
	else out.print("Este contrato se desplegara con la wallet: "+session.getAttribute("wallet"));
%>
<div style="background-color: #fa9581;">
	<h3>Instrucciones</h3>
	<p style="color: black;">
	Este contrato funciona de la siguiente manera: Usted genera un contrato de tipo Integrity Check y sube su fichero.
	Cuando quiera comprobar si el fichero sigue siendo el mismo, solo debera ir a la pestaNa Integridad de ficheros, selecionar el fichero y se comprobara si el md5 del fichero acutal es igual al que se encuntera en el contrato Integrity check generado.
	</p>
</div>
<form action="integrityDeploy" method="POST" class="form-inline" enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-4"><label>Seleccione el fichero </label></div>
		<div class="col-md-4"><input type="file" name="file" required></div>
	</div>
	<div class="row">
		<div class="col-md-4"><label>Introduzca la contraseña de la wallet </label></div>
		<div class="col-md-4"><input type="password" name="pwd" class="form-control" required></div>
	</div>
	<input type="submit" id="button" class="btn-success btn-lg" value="Desplegar">
</form>
