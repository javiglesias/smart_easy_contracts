<%@include file="index.jsp" %>
<div class="wrapper">
	<div class="main-panel">
	<%@include file="notifications.jsp" %>
		<div class="limiter">
			<h1>Desplegar contrato</h1>
			<br>
			<div id="block">
			<form action="">
			<label>Seleccione el tipo de contrato</label>
			<select name="tokenType">
				<option value="ERC20">ERC-20</option>
				<!--option value="ERC223">ERC-223</option>
				<option value="ERC777">ERC-777</option-->
				<option value="ERC721">ERC-721</option>
				<option value="filesIntegrity">Integrity check</option>
			</select>
			<input type="submit" value="Generar contrato" id="button" class="btn-primary btn-lg">
			</form>
			</div>
			<br>
			<%
			if(request.getParameter("tokenType")!=null){
				String tokenToInclude = "";
				if(request.getParameter("tokenType").equals("ERC20")) {
					tokenToInclude = "ERC20.jsp";%>
					<div id="block">
					<%@include file="ERC20.jsp"%>
					</div>
				<%} else if(request.getParameter("tokenType").equals("ERC223")) {
					tokenToInclude = "ERC223.jsp";%>
					<div id="block">
					<%@include file="ERC223.jsp"%>
					</div>
				<%} else if(request.getParameter("tokenType").equals("ERC777")) {
					tokenToInclude = "ERC777.jsp";%>
					<div id="block">
					<%@include file="ERC777.jsp"%>
					</div>
				<%}else if(request.getParameter("tokenType").equals("ERC721")) {
					tokenToInclude = "ERC721.jsp";%>
					<div id="block">
					<%@include file="ERC721.jsp"%>
					</div>
				<%}else if(request.getParameter("tokenType").equals("filesIntegrity")) {
					tokenToInclude = "filesIntegrity.jsp";%>
					<div id="block">
					<%@include file="filesIntegrity.jsp"%>
					</div>
				<%}
			}
			%>
		</div>
	</div>
</div>
