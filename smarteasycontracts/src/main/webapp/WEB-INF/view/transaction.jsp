<% 
if(session.getAttribute("transactions") != null){
	String transactions = session.getAttribute("transactions").toString();
	if(transactions != "" || transactions != null){
		String[] transactionsLog = transactions.split(";");
		for(String temp : transactionsLog){
			out.print("<tr>");
			for(String tmp : temp.split("#")){
				if(tmp.equals(temp.split("#")[0]))
					out.print("<td><a href=\"https://rinkeby.etherscan.io/tx/"+tmp+"\">"+tmp+"</a></td>");
				else
					out.print("<td>"+tmp+"</td>");
			}
			out.print("</tr>");
		}
	}
}
session.removeAttribute("transactions");
%>