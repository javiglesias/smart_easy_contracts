<h2>Comprobar integridad de un fichero</h2>
<form action="/integrityCheck" method="POST" enctype="multipart/form-data">
	<div class="form-group">
		<input type="file" name="integrityFile">
		<div class="row">
		<div class="col-md-4"><input type="password" class="form-control" name="pwd" placeholder="Contraseña de la cartera" required></div>
	</div>
		<input  type="submit" id="button" class="btn-primary btn-lg" value="Comprobar integridad">
	</div>
</form>
