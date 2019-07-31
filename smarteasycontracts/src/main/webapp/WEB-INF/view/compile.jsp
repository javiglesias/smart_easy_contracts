<%@include file="index.jsp" %>
<script src="/assets/js/solc.min.js" type="text/javascript"></script>
<script src="/assets/js/web3.min.js" type="text/javascript"></script>
<script src="/assets/js/compile.js" type="text/javascript"></script>
<script src="/assets/js/mui.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<style>
	.compile-container {
		width: 100%;
		height: 100%;
	}
	#status {
		float: right;
		position: absolute;
		color: black;
	}
	#versions {
		color: black;
	}
	#source {
		height: 100%;
		width: 100%;
		min-height:80%;
	}
	.mui-panel {
		width: 100%;
		height: 70%;
		min-height:70%;
		padding-bottom: 1%;
		padding-top: 1%;
	}
	#contract-compile {
		text-align: center;
		color: black;
	}
	#contract-compile:hover {
		text-align: center;
		color: black;
		border-style: double;
	}
</style>
<div class="wrapper">
	<div class="main-panel">
		<div class="limiter" id="compiler">
			<h1>Compilador Online</h1>
			  <p id="status"></p>
			  <div class="mui-select">
			    <select id="versions"></select>
			  </div>
			    <div class="mui-panel">
			      <textarea id="source" onclick="this.select()" placeholder="Codigo fuente"></textarea><br>
				</div>
				<input id="contract-compile" type="submit" class="btn-primary btn-lg" value="Compile">
			  <div id="contract-list"></div>
		</div>
	</div>
</div>
