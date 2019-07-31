<%@include file="index.jsp" %>
<script>
var cols = document.querySelectorAll('#blocks .block');
[].forEach.call(cols, function(col) {
  col.addEventListener('dragstart', handleDragStart, false);
  col.addEventListener('dragenter', handleDragEnter, false)
  col.addEventListener('dragover', handleDragOver, false);
  col.addEventListener('dragleave', handleDragLeave, false);
  col.addEventListener('drop', handleDrop, false);
  col.addEventListener('dragend', handleDragEnd, false);
});
function handleDragStart(e) {
	  this.style.opacity = '0.4';  // this / e.target is the source node.
	}
function handleDragStart(e) {
	  this.style.opacity = '0.4';  // this / e.target is the source node.
	}

function handleDragOver(e) {
  if (e.preventDefault) {
    e.preventDefault(); // Necessary. Allows us to drop.
  }

  e.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.

  return false;
}

function handleDragEnter(e) {
  // this / e.target is the current hover target.
  this.classList.add('over');
}

function handleDragLeave(e) {
  this.classList.remove('over');  // this / e.target is previous target element.
}
function handleDrop(e) {
	  // this / e.target is current target element.

	  if (e.stopPropagation) {
	    e.stopPropagation(); // stops the browser from redirecting.
	  }

	  // See the section on the DataTransfer object.

	  return false;
	}

function handleDragEnd(e) {
  // this/e.target is the source node.

  [].forEach.call(cols, function (col) {
    col.classList.remove('over');
  });
}
var dragSrcEl = null;

function handleDragStart(e) {
  // Target (this) element is the source node.
  this.style.opacity = '0.4';

  dragSrcEl = this;

  e.dataTransfer.effectAllowed = 'move';
  e.dataTransfer.setData('text/html', this.innerHTML);
}
function handleDrop(e) {
	  // this/e.target is current target element.

	  if (e.stopPropagation) {
	    e.stopPropagation(); // Stops some browsers from redirecting.
	  }

	  // Don't do anything if dropping the same column we're dragging.
	  if (dragSrcEl != this) {
	    // Set the source column's HTML to the HTML of the columnwe dropped on.
	    dragSrcEl.innerHTML = this.innerHTML;
	    this.innerHTML = e.dataTransfer.getData('text/html');
	  }

	  return false;
	}
</script>
<div class="wrapper">
	<div class="main-panel">
		<div class="limiter">
			<%@include file="notifications.jsp" %>
			<div>
			<div id="topBar" style="background-color: #fa9581; overflow: hidden;">
			<label id="section_title">Login as: <strong style="color: #a40000;">${uname}</strong></label>
			<label>|</label>
			<label>Email asociado: </label>
			<%if(session.getAttribute("email") != null)
				out.print(session.getAttribute("email").toString());%>
			<label>|</label>
			<% if(session.getAttribute("balance") != null)
				out.print("<label>Balance de la cartera: "+ session.getAttribute("balance") +" ETHER</label>");
			%>
			</div><br>
			<h1>Mi Perfil</h1>
			<div class="blocks">
			<div id="block">
			<label>Cartera cargada: </label>
			<%out.print("<a href=\"contract?address="+
				session.getAttribute("wallet") +"\">"+
				session.getAttribute("wallet")+"</a>"); %><br>
			<%@include file="contracts.jsp" %>
			</div>
			<br>
			<div id="block" draggable="true">
				<h2>Selecciona una cartera</h2>
				<div>
					<form action="/changeWalletSelecion" method="POST">
					<%
					if(session.getAttribute("addresses") != null){
						String addresses = session.getAttribute("addresses").toString();
						String[] splitAddress = addresses.split(";");
						out.print("<ul>");
						boolean first = true;
						for(String temp : splitAddress){
							out.print("<input name=\"wallet\" type=\"radio\" value=\""+temp+"\">"+temp+"</input><br>");
						}
						out.print("</ul>");
					}
					%>
					<div class="row"><div class="col-md-4"><input type="password" placeholder="Contraseña" class="form-control" id="inputSuccess" name="pwd" required></div></div>
					<input id="button" class="btn-primary btn-lg" type="submit" value="Cargar cartera">
					</form>
				</div>
			</div>
			<br>
			<div id="block" draggable="true">
			<h2>Importar cartera:</h2>
			<div>
				<form action="/uploadFile" method="POST" enctype="multipart/form-data">
					<div class="form-group">
						<label>Seleccione el fichero de wallet: </label>
						<input type="file" name="file" size=1 accept="" required>
						<div class="row">
							<div class="col-md-4">
							<input type="password" placeholder="Contraseña" class="form-control" id="inputSuccess" name="password" required>
						</div></div>
						<input type="submit" id="button" class="btn-primary btn-lg" value="Importar">
					</div>
				</form>
			</div>
			</div>
			<br>
			<div id="block" draggable="true">
			<%@include file="integrityCheck.jsp"%>
			</div>
			</div>
			</div>
	</div>
</div>
