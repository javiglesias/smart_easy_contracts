<%if(session.getAttribute("isSuccess") == "true"){ %>
<%@include file="success.jsp" %>
<%} else if(session.getAttribute("isSuccess") == "false"){ %>
<%@include file="fail.jsp" %>
<%} 
session.removeAttribute("isSuccess");
session.removeAttribute("notificationMessage");
%>