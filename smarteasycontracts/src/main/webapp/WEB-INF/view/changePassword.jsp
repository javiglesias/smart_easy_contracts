<h2>Cambiar contraseNa</h2>
<form action="/changePasswrd" method="POST">
	<%
	if(session.getAttribute("addresses") != null){
		String addresses = session.getAttribute("addresses").toString();
		String[] splitAddress = addresses.split(";");
		out.print("<ul>");
		for(String temp : splitAddress){
			out.print("<input required name=\"walletRecovery\" type=\"radio\" value=\""+temp+"\">"+temp+"</input><br>");
		}
		out.print("</ul>");
	}
	%>
	<div class="form-group">
		<div class="row">
			<div class="col-md-4">
			<input type="password" class="form-control" name="old_password" placeholder="Antigua contraseña" required>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
			<input type="password" class="form-control" placeholder="Nueva contraseña" name="new_password" pattern=".{8,}" required>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
			<input type="password" class="form-control" name="new_rep_password" placeholder="Repetir contraseña" pattern=".{8,}" required>
			</div>
		</div>
		<input type="submit" id="button" class="btn-primary btn-lg" value="Cambiar contraseña">
	</div>
</form>
