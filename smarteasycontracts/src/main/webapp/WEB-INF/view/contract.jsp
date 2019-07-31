<%@include file="index.jsp" %>
<div class="wrapper">
	<div class="main-panel">
		<div class="limiter">
			<style>
			body {
			  font: 18px/1.5 sans-serif;
			  padding: 1rem;
			}
			
			dl {
			  margin-bottom: 50px;
			}
			
			
			/**
			 * tab panel widget
			 */
			.tabPanel-widget {
			  position: relative;  /* containing block for headings (top:0) */
			  background: #FA755B;
			}
			
			/**
			 * because labels come first in source order - we use z-index to move them in front of the headings
			 */
			.tabPanel-widget > label {
			  position: absolute;
			  z-index: 1;
			}
			
			/**
			 * labels and headings must share same values so grouping declarations in this rule prevents async edits (risk of breakage)
			 * line-height == height -> vertical centering
			 * the width dictates the offset for all headings but the first one: left offset = width * number of previous heading(s)
			 * note that width and offset of label/heading pair can be customized if necessary
			 */
			
			.tabPanel-widget > label,
			.tabPanel-widget > h2 {
			  font-size: 1.1em;
			  width: 9em;
			  height: 2em;
			  line-height: 2em;
			}
			
			/**
			 * position:relative is for the markers (the down arrow in tabs)
			 */
			.tabPanel-widget > h2 {
			  position: relative;
			  margin: 0;
			  text-align: center;
			}
			
			.tabPanel-widget > label {
			  border-right: 1px solid #fff;
			}
			
			/**
			 * all first level labels and headings after the very first ones
			 */
			.tabPanel-widget > label ~ label,
			.tabPanel-widget > h2 ~ h2 {
			  position: absolute;
			  top: 0;
			}
			
			
			/**
			 * We target all the label/heading pairs
			 * we increment the :nth-child() params by 4 as well as the left value (according to "tab" width)
			 */
			
			.tabPanel-widget label:nth-child(1),
			.tabPanel-widget h2:nth-child(3) {
			  left: 0em;
			}
			
			.tabPanel-widget label:nth-child(5),
			.tabPanel-widget h2:nth-child(7) {
			  left: 9em;
			}
			
			.tabPanel-widget label:nth-child(9),
			.tabPanel-widget h2:nth-child(11) {
			  left: 18em;
			}
			
			/**
			 * we visually hide all the panels
			 * https://developer.yahoo.com/blogs/ydn/clip-hidden-content-better-accessibility-53456.html
			 */
			.tabPanel-widget input + h2 + div {
			  position: absolute !important;
			  clip: rect(1px, 1px, 1px, 1px);
			  padding:0 !important;
			  border:0 !important;
			  height: 1px !important;
			  width: 1px !important;
			  overflow: hidden;
			}
			/**
			 * we reveal a panel depending on which control is selected
			 */
			.tabPanel-widget input:checked + h2 + div {
			  position: static !important;
			  padding: 1em !important;
			  height: auto !important;
			  width: auto !important;
			}
			
			/**
			 * shows a hand cursor only to pointing device users
			 */
			.tabPanel-widget label:hover {
			  cursor: pointer;
			}
			
			.tabPanel-widget > div {
			  background: #f0f0f0;
			  padding: 1em;
			}
			
			/**
			 * we hide radio buttons and also remove them from the flow
			 */
			.tabPanel-widget input[name="tabs"] {
			  opacity: 0;
			  position: absolute;
			}
			
			
			/**
			 * this is to style the tabs when they get focus (visual cue)
			 */
			
			.tabPanel-widget input[name="tabs"]:focus + h2 {
			  outline-offset: 10px;
			}
			
			
			/**
			 * reset of the above within the tab panel (for pointing-device users)
			 */
			.tabPanel-widget:hover h2 {
			  outline: none !important;
			}
			
			/**
			 * visual cue of the selection
			 */
			.tabPanel-widget input[name="tabs"]:checked + h2 {
			  background: #fa9581;
			  opacity: 1;
			}
			
			/**
			 * the marker for tabs (down arrow)
			 */
			.tabPanel-widget input[name="tabs"]:checked + h2:after {
			  content: '';
			  margin: auto;
			  position: absolute;
			  bottom: -10px;
			  left: 0;
			  right: 0;
			  width: 0;
			  height: 0;
			  border-left: 10px solid transparent;
			  border-right: 10px solid transparent;
			  border-top: 10px solid #fa9581;
			}
			
			/**
			 * Make it plain/simple below 45em (stack everything)
			 */
			@media screen and (max-width: 45em) {
			
			  /* hide unecessary label/control pairs */
			  .tabPanel-widget label,
			  .tabPanel-widget input[name="tabs"] {
			    display: none;
			  }
			
			  /* reveal all panels */
			  .tabPanel-widget > input + h2 + div {
			    display: block !important;
			    position: static !important;
			    padding: 1em !important;
			    height: auto !important;
			    width: auto !important;
			  }
			
			  /* "unstyle" the heading */
			  .tabPanel-widget h2 {
			    width: auto;
			    position: static !important;
			    background: #fa9581;
			  }
			
			  /* "kill" the marker */
			  .tabPanel-widget h2:after {
			    display: none !important;
			  }
			
			}
			</style>
			<h1>Informacion de direccion</h1>
			<h3>Dirección <%
				out.print("<a href=\"https://rinkeby.etherscan.io/address/"
					+ request.getParameter("address") + "\">"
					+ request.getParameter("address") + "</a>");
				session.setAttribute("contract", request.getParameter("address"));
				%></h3>
			
			<div class="tabPanel-widget">
			  <label for="tab-1" tabindex="0"></label>
			    <input id="tab-1" type="radio" name="tabs" checked="true" aria-hidden="true">
			  <h2>Informacion</h2>
			  <div>
			    <%	out.print("<label>Nombre: "+request.getParameter("name")+"</label><br>");
			    	out.print("<label>Direccion: " + request.getParameter("address") + "</label><br>");
			    	out.print("<label>Tipo de contrato: "+ session.getAttribute("type") +"</label><br>");
			    	out.print("<label>Binary:</label><br><textarea readonly> "+ session.getAttribute("binary") +"</textarea><br>");
			    	out.print("<label>Hash de la transaccion de creacion:" +
			    			"<a href=\"https://rinkeby.etherscan.io/tx/" +
			    			session.getAttribute("txHash")+"\">" +
			    			session.getAttribute("txHash")+"</a></label>");
			    %>
			  </div>
			  <label for="tab-2" tabindex="1"></label>
			  <input id="tab-2" type="radio" name="tabs" aria-hidden="true">
				  <!--%if(session.getAttribute("transactions") == null)
				  	response.sendRedirect("/transactions?address="+request.getParameter("address")); %-->
			  <h2>Transacciones</h2>
			  <div>
			  <style>
			  	table {
				  border-collapse: collapse;
				}
			
				table, th, td {
				  border: 1px solid black;
				}
			  </style>
				<div style="overflow-x:auto;">
				  	<table>
				  		<tr>
					  		<th>Txhash</th>
					  		<th>From address</th>
					  		<th>To address</th>
					  		<th>Block</th>
					  		<th>Value</th>
					  		<th>Datetime</th>
				  		</tr>
				  		<%@include file="transaction.jsp"%>
				  	</table>
				</div>
			  </div>
			  <label for="tab-3" tabindex="2"></label>
			  <input id="tab-3" type="radio" name="tabs" aria-hidden="true">
			  <h2>Interactuar</h2>
			  <div>
			  <form action="">
			    <%
			    	out.print("<label>From "+session.getAttribute("wallet")+"</label><br>");
					  out.print("<label>To </label><input name=\"toAddress\"type=\"text\"><br>");
			    	out.print("<button type=\"submit\">Crear Transaccion</button>");
			    %>
			    </form>
			  </div>
			</div>
		</div>
	</div>
</div>