<%@include file="index.jsp" %>
<div class="wrapper">
	<div class="main-panel">
		<div class="limiter">
			<%@include file="notifications.jsp" %>
			<div style="display: flex; align-items: center; height: 100%;">
				<div class="col-md-4 col-md-offset-4" style=" align-items: center;">
					<h1>Iniciar sesión</h1>
					<div id="inputDataForm">
			            <form action="login_process" method="POST">
							<div class="row">
								<input name="uname" class="form-control" type="text" placeholder="Username">
			                </div>
			                <div class="row" style="padding-top: 2px;">
								<input name="pwd" class="form-control" type="password" placeholder="Contraseña">
			                </div>
			                <br>
			             	<input id="button" class="btn-primary btn-lg" type="submit" value="Entrar"/>
			            </form>
		            </div>
	            </div>
            </div>
		</div>
	</div>
</div>
