<form action="/contract" method="GET">
			<%
	if(session.getAttribute("contracts") != null && session.getAttribute("contracts") != ""){
		String contracts = session.getAttribute("contracts").toString();
		if(contracts != "" || contracts != null){
			out.print("<label>Contratos desplegados:</label><br>");
			String[] contractsArray = contracts.split(";");
			out.print("<ul style=\"list-style: none;\">");
			for(String temp : contractsArray)
				out.print("<li><input type=\"radio\" name=\"address\" value=\""+temp+"\" required>"+temp+"</input></li>");
			out.print("</ul>");
		}
		out.print("<input type=\"submit\" id=\"button\" class=\"btn-primary btn-lg\" value=\"Ir a contrato\"/>");
	}
	%>
</form>
