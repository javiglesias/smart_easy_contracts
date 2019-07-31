let compiler
let optimize = 1
let compiledContract
window.onload = function () {
	document.getElementById('versions').onchange = loadSolcVersion
	addCompileEvent()
	if (!BrowserSolc) {
	console.log('You have to load browser-solc.js in the page. We recommend using a <script> tag.')
	throw new Error()
}

status('Loading Compiler Versions...')

BrowserSolc.getVersions(function (soljsonSources, soljsonReleases) {
populateVersions(soljsonSources)
setVersion(soljsonReleases['0.4.18'])
loadSolcVersion()
})
}
function getVersion() {
	return document.getElementById('versions').value
	}
function loadSolcVersion() {
	status(`Loading Solc: ${getVersion()}`)
	BrowserSolc.loadVersion(getVersion(), function (c) {
	status('Solc loaded.')
	compiler = c
	})
}

function setVersion(version) {
document.getElementById('versions').value = version
}

function populateVersions(versions) {
sel = document.getElementById('versions')
sel.innerHTML = ''

for (let i = 0; i < versions.length; i++) {
let opt = document.createElement('option')
opt.appendChild(document.createTextNode(versions[i]))
opt.value = versions[i]
sel.appendChild(opt)
}
}

function status(txt) {
document.getElementById('status').innerHTML = txt
}
function addCompileEvent() {
const compileBtn = document.getElementById('contract-compile')
compileBtn.addEventListener('click', solcCompile)
}

function solcCompile() {
if (!compiler) return alert('Please select a compiler version.')

setCompileButtonState(true)
status("Compiling contract...")
compiledContract = compiler.compile(getSourceCode(), optimize)

if (compiledContract) setCompileButtonState(true)

renderContractList()
status("Compile Complete.")
setCompileButtonState(true)
}

function getSourceCode() {
return document.getElementById("source").value
}

function setCompileButtonState(state) {
document.getElementById("contract-compile").disabled = state
}
function renderContractList() {
const contractListContainer = document.getElementById('contract-list')
const { contracts } = compiledContract

Object.keys(contracts).forEach((contract, index) => {
const label = `contract-id-${index}`
const gas = contracts[contract].gasEstimates.creation

createContractInfo(gas, contract, label, function(el){
  contractListContainer.appendChild(el)
  const btnContainer = document.getElementById(label)

  btnContainer.appendChild(
    buttonFactory('default', contract, contracts[contract], 'details')
  )
  btnContainer.appendChild(
    buttonFactory('success', contract, contracts[contract], 'deploy')
  )
})
})
}

function createContractInfo(gas, contractName, label, callback) {
const el = document.createElement('DIV')

el.innerHTML = `
<div class="mui-panel">
  <div id="${label}" class="mui-row">
    <div>
      Contract Name: <strong>${contractName.substring(1, contractName.length)}</strong>
    </div>
    <div>
      Gas Estimate: <strong style="color: green;">
        ${sumArrayOfInts(gas)}
      </strong>
    </div>
  <div id="contract-list"></div>
  <div class="mui-panel">
    <p style="font-size:25px; font-weight:bold">
  	Compile Contract
    </p>
  </div>
  </div>
</div>
`

callback(el)
}

function sumArrayOfInts(array) {
return array.reduce((acc, el) => (acc += el), 0)
}

function buttonFactory(color, contractName, contract, type) {
const btn = document.createElement('BUTTON')
const btnContainer = document.createElement('DIV')

btn.className = `class=""`
btn.innerText = type
btn.addEventListener('click', () => type === 'details'
? renderContractDetails(contractName, contract)
: deployContract(contract)
)

btnContainer.className = 'mui-col-md-3'
btnContainer.appendChild(btn)

return btnContainer
}
function renderContractDetails(name, contract) {
  const modalEl = document.createElement('DIV')
  modalEl.style.width = '700px';
  modalEl.style.margin = '100px auto';
  modalEl.style.padding = '50px';
  modalEl.style.backgroundColor = '#fff';

  modalEl.appendChild(renderContractInfo(name, contract))
  mui.overlay('on', modalEl);
}

function renderContractInfo(contractName, contract) {
  const contractContainer = document.createElement('DIV')
  contractContainer.innerHTML = `
    <h3>
      Contract Name: <strong>${contractName.substring(1, contractName.length)}</strong>
    </h3>
    <h4>Bytecode:</h4>
    <textarea style="width:320px; height:100px;" readonly>${contract.bytecode}</textarea>
    <h4>ABI:</h4>
    <textarea style="width:320px; height:150px;" readonly>${contract.interface}</textarea>
    <h4>Function Hashes</h4>
    <textarea style="width:320px; height:100px;" readonly>${renderFunctionWithHashes(contract.functionHashes)}</textarea>
    <h4>Opcodes:</h4>
    <textarea style="width:320px; height:100px;" readonly>${contract.opcodes}</textarea>
  `

  return contractContainer
}

function renderFunctionWithHashes(functionHashes) {
  let functionHashContainer = ''

  Object.keys(functionHashes)
    .forEach((functionHash, index) => (
      functionHashContainer += `${++index}. ${functionHashes[functionHash]}: ${functionHash} \n`
    ))

  return functionHashContainer
}
let Web3 = require('web3');
function deployContract(contract) {
	$.ajax({
		type: "POST",
		contentType: "application/json",
		beforeSend: function(){
			console.log("before: "+contract.bytecode);
		},
		url: "/deployCustom",
		data: contract.bytecode,
		dataType: 'json',
		success: function(response){
			console.log("OK"+response);
		},
		error: function(error){
			console.log(error);
		},
		done: function(d){
			console.log("Done: "+d);
		}
	});
}
/*
 * let web3 = new Web3();
	web3.setProvider(new web3.providers.HttpProvider('https://rinkeby.infura.io/v3/d49e7f310f0b47e0b8e4cc591ecc81bd'));
	let bytecode = contract.bytecode;
	let abi = web3.eth.contract(JSON.parse(contract.interface));
	try{
		let account = web3.personal.unlockAccount('0x01905c286ad7d9912b8b71046ff61f4b49729fda','Whatajokepasswordis1234', 10000);
	}catch(e) {
		console.log(e);
		return;
	}
	let objectContract = smartContract.new({
		from: account,
		gas: 50,
		data: bytecode
	});
	var deployedContract = abi.new(objectContract);
 * */
