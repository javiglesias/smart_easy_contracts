<%@include file="index.jsp" %>
<div class="wrapper">
	<div class="main-panel">
		<div class="limiter">
		<%@include file="notifications.jsp" %>
			<h1>Gestionar cartera</h1>
			<div class="newWalletForm" id="block">
				<h2>Crear nueva cartera</h2>
				<form action="/creationProcess" method="POST">
					<div class="row">
						<div class="col-md-4">
							<input type="password" id="contrasena" name="pwd" class="form-control" placeholder="Contraseña de la cartera" pattern=".{8,}" required>
							<input id="button" type="submit" class="btn-primary btn-lg" value="Crear cartera">
						</div>
					</div>
				</form>
			</div>
			<br>
			<div id ="block">
			<%@include	file="restorePassword.jsp" %>
			</div> <br>
			<div id="block">
			<%@include	file="changePassword.jsp" %>
		</div>
		</div>
	</div>
</div>
