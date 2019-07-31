<%@include file="index.jsp" %>
<div class="wrapper">
	<div class="main-panel">
		<div class="limiter">
		<%@include file="notifications.jsp" %>
            <div style="display: flex; align-items: center; height: 100%;">
				<div class="col-md-4 col-md-offset-4" style=" align-items: center;">
	            <h1>Registro</h1>
					<div id="inputDataForm">
						<form action="registration_process" method="POST">
							<input type="text" class="form-control" name="email"  placeholder="Email" required>
							<span class="focus-input100"></span>
							<input type="text" name="uname" class="form-control" placeholder="Username" required>
							<input type="password" name="pwd" pattern=".{8,}" class="form-control" placeholder="Contraseña" required>
							<input type="password" name="rp_pwd" class="form-control" pattern=".{8,}" placeholder="Repetir Contraseña" required>
							<br>
							<input type="submit" value="Registrar" id="button" class="btn-primary btn-lg">
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
