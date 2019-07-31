package smarteasycontracts.smarteasycontracts;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import smarteasycontracts.smarteasycontracts.DatabaseConnector.ConditionOperator;
import smarteasycontracts.smarteasycontracts.Contracts.ERC20;
import smarteasycontracts.smarteasycontracts.Contracts.ERC721;
import smarteasycontracts.smarteasycontracts.Contracts.IntegrityCheck;
/**
 * @author Javier Iglesias Sanz
 * @version 1.0
 * @since 2019-02-21
 * This class contains the utility functions
 */
public class Utils {
	DatabaseConnector database;
	Credentials _credentials;
	static EmailConnector emailService;
	Web3j web3;
	private static String infuraDeploy = "https://rinkeby.infura.io/v3/d49e7f310f0b47e0b8e4cc591ecc81bd";
	public Utils() {
		database = new DatabaseConnector(); 
		emailService = new EmailConnector();
		try {
			web3 = Web3j.build(new HttpService(infuraDeploy));
		} catch (Exception e) {
			System.err.println("Error initialing the Web3 API");
			e.printStackTrace();
		}
	}
	/**
	 * Returns the Credentials of the wallet file provided
	 * */
	public Credentials LoadCredentials(String password, String filename) {
		String source = "";
		if(!filename.contains("/walletFiles/"))
			source = "./walletFiles/"+filename;
		else source = filename;
		Credentials returnCredentials = null;
		try {
			_credentials = returnCredentials = WalletUtils.loadCredentials(password, source);
		} catch(Exception ex) {
			System.err.println("Error loading credentials.");
			ex.printStackTrace();
		}
		return returnCredentials;
	}
	/**
	 * deploy the ERC20 smart contract
	 * */
	public boolean WriteContractERC20(String password, String walletFile,
			BigInteger _supply, String _tokenName, String _symbol, 
			BigInteger _decimals) {
		ContractGasProvider contractGasProvider = new DefaultGasProvider();
		Credentials credentials;
		String contractAddress;
		try {
			System.out.println("Deploying ERC20...");
			credentials = LoadCredentials(password, walletFile);
			ERC20 twenty = ERC20.deploy(web3, 
					credentials, contractGasProvider,
					_supply,_tokenName,_symbol,_decimals).send();
			contractAddress = twenty.getContractAddress();
			SaveContract(credentials.getAddress().toString(), contractAddress, "ERC20");
			java.util.Optional<TransactionReceipt> tx = twenty.getTransactionReceipt();
			CreateTransactionLog(tx.get().getTransactionHash(), 
					credentials.getAddress(), 
					contractAddress, 
					tx.get().getBlockNumber().toString(), 
					tx.get().getGasUsed().toString());
		} catch(Exception e) {
			System.err.println("Contract not deployed: ");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Deploy the contract given.
	 * */
	public String Deploy(String password, String walletFile, BigInteger _supply,
			String _tokenName, String _symbol, BigInteger _decimals) {
		ContractGasProvider contractGasProvider = new DefaultGasProvider();
		Credentials credentials;
		String contractAddress;
		try {
			System.out.println("Deploying ERC721...");
			credentials = LoadCredentials(password, walletFile);
			ERC20 sevenTwOne = ERC721.deploy(web3, credentials, 
					contractGasProvider, _supply,
					_tokenName,_symbol,_decimals).send();
			contractAddress = sevenTwOne.getContractAddress();
			SaveContract(credentials.getAddress().toString(), contractAddress, "ERC721");
			java.util.Optional<TransactionReceipt> tx = sevenTwOne.getTransactionReceipt();
			CreateTransactionLog(tx.get().getTransactionHash(), 
					credentials.getAddress(), 
					contractAddress, 
					tx.get().getBlockNumber().toString(), 
					tx.get().getGasUsed().toString());
			System.out.println("Transaction hash: "+tx.get().getTransactionHash());
			return contractAddress;
		} catch(Exception e) {
			System.err.println("Contract not deployed: ");
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * Creates a new wallet from scratch associated to the user already existed,
	 * also register this wallet on DB.
	 * */
	public boolean CreateWallet(String password, String username) {
		try {
			String walletFileName = WalletUtils.generateNewWalletFile(
					password,
				new File("./walletFiles/"));
			System.out.println("Creating new wallet...");
			Credentials credentials = LoadCredentials(password, walletFileName);
			if(RegisterNewWallet(username, credentials.getAddress(), password)) {
				InitialTx(credentials.getAddress());
				return true;
			}
		} catch(Exception ex) {
			System.err.println("Error while creating the new Wallet");
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * Write a generic body on a file.
	 * */
	public boolean WriteToFile(String fileName, String body) {
		try {
		FileWriter fw = new FileWriter(new File(fileName));
		fw.write(body);
		fw.close();
		return true;
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
	}
	/**
	 * Encode the password with md5 algorithm, returns the hash of the password.
	 * */
	public String Encode(String passwordToEncrypt) {
		String generatedPassword = "";
		char key = 'K';
		try {
			for(int i = 0; i < passwordToEncrypt.length(); ++i) {
				generatedPassword += passwordToEncrypt.charAt(i) ^ key; 
			}
		}catch (Exception ex) {
			System.err.println(ex.getMessage());
			return "";
		}
		return generatedPassword;
	}
	/**
	 * Saves the generated contract with the name of the user that created it.
	 * BBDD
	 * */
	public boolean SaveContract(String address, String contractAddress, String type) {
		try {
			System.out.println("Registrando contrato en la BBDD");
			if(!database.Ping()) {
				System.err.println("Conexcion con la BBDD perdida. Reconectando...");
				database = new DatabaseConnector();
			}
			if(database.Insert("CONTRACTS", new String[] {"ADDRESS","CONTRACTADDRESS", "TYPE"},new String[] {address, contractAddress, type}) > 0)
				return true;
		} catch(Exception ex) {
			System.err.println("Insert:");
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * Returns the contracts associated to an address given.
	 * */
	public ArrayList<String> AssociatedContracts(String address) {
		
		return new ArrayList<>();
	}
	/**
	 * Import the Wallet file into the system database and link with the username.
	 * */
	public boolean ImportWallet(String filename, String username, String _password) {
		String password = "";
		try {
			//password = GetPassword(username);
			if(_password != "") {
				Credentials newWallet = LoadCredentials(_password, filename);
				if(this.RegisterNewWallet(username, newWallet.getAddress(), _password)) return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean RegisterNewWallet(String username, String address, String password) {
		System.out.println("Registering new wallet...");
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		if(database.Insert("WALLETS", new String[] {"UNAME","ADDRESS"}, new String[] {username,address}) > 0) {
			if(database.Insert("WALLETSLOGIN", new String[] {"ADDRESS", "PASSWORD"}, new String[] {address, password}) > 0)
				return true;
			return false;
		}
		return false;
	}
	/**
	 * Register the new user on the database, table LOGIN
	 * */
	public boolean RegisterNewUser(String username, String password, String email) {
		System.out.println("Registering new user...");
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		password = Encode(password);
		if(database.Insert("LOGIN",new String[] {"UNAME", "PASSWORD"}, new String[] {username, password})>0) {
			if(database.Insert("USERSEMAIL",new String[] {"UNAME", "EMAIL"}, new String[] {username, email})>0) {
				System.out.println("email registered correctly.");
				return true;
			}
			System.err.println("Error while registering the email.");
			return false;
		}
		else return false;
	}
	/**
	 * Get the wallet address associated to a username.
	 * */
	public ArrayList<String> GetAddress(String username) {
		ArrayList<String> address = new ArrayList<>();
		ResultSet results;
		try {
			if(!database.Ping()) {
				System.err.println("Conexcion con la BBDD perdida. Reconectando...");
				database = new DatabaseConnector();
			}
			results = database.Select("WALLETS", ConditionOperator.EQ, "UNAME", username);
			if(results != null) {
				while(!results.isAfterLast()) {
					address.add(results.getNString("ADDRESS"));
					results.next();
				}
			}
			return address;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * Updates the password associated to the wallet for login
	 * **/
	public boolean UpdatePassword(String address, String newPassword) {
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		if(database.Update("WALLETSLOGIN", "PASSWORD", newPassword, "ADDRESS", address, ConditionOperator.EQ) > 0)
			return false;
		return true;
	}
	/**
	 * Get the hash of the password of the username provided.
	 * */
	public String GetPassword(String username) {
		String password = "";
		ResultSet results;
		try {
			if(!database.Ping()) {
				System.err.println("Conexcion con la BBDD perdida. Reconectando...");
				database = new DatabaseConnector();
			}
			results = database.Select("LOGIN", ConditionOperator.EQ, "UNAME", username);
			password = results.getNString("PASSWORD");
			return password;
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * Get the contracts associated to the username.
	 * */
	public ArrayList<String> GetContracts(String address) {
		ArrayList<String> contracts = new ArrayList<>();
		ResultSet results;
		try {
			if(!database.Ping()) {
				System.err.println("Conexcion con la BBDD perdida. Reconectando...");
				database = new DatabaseConnector();
			}
			results = database.Select("CONTRACTS", ConditionOperator.EQ, 
					"ADDRESS", address);
			if(results == null) return null;
			while(!results.isAfterLast()) {
				contracts.add(results.getNString("CONTRACTADDRESS"));
				results.next();
			}
			return contracts;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the type of the SmartContract provided.
	 * */
	public String GetType(String address) {
		String type = "";
		try {
			if(!database.Ping()) {
				System.err.println("Conexcion con la BBDD perdida. Reconectando...");
				database = new DatabaseConnector();
			}
			ResultSet results = database.Select("CONTRACTS", ConditionOperator.EQ, "CONTRACTADDRESS", address);
			if(results != null)
				type = results.getNString("TYPE");
		} catch(Exception ex) {
			System.err.println("Error retrieving the type of the contract");
			ex.printStackTrace();
		}
		return type;
	}
	/**
	 * Returns all the information about the contract in ETH blockchain
	 * */
	public HashMap<String,String> GetInfoContract(String address, String username, String contractAddress, String type) {
		HashMap<String,String> returnValue = new HashMap<>();
		String walletFile, password;
		switch(type.toUpperCase()) {
			case "ERC20":
				walletFile = GetWalletFile(address);
				password = GetPassword(username);
				try {
					ContractGasProvider contractGasProvider = new DefaultGasProvider();
					//Credentials credentials = LoadCredentials(password, walletFile);
					//ERC20 contract = ERC20.load(address, web3, credentials, contractGasProvider);
					//returnValue.put("binary", contract.getContractBinary());
					returnValue.put("name", "");
					String txhash = GetTxHashContract(contractAddress);
					returnValue.put("txHash", txhash);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				break;
			case "ERC721":
				walletFile = GetWalletFile(address);
				password = GetPassword(username);
				try {
					ContractGasProvider contractGasProvider = new DefaultGasProvider();
					//Credentials credentials = LoadCredentials(password, walletFile);
					//ERC721 contract = ERC721.load(address, web3, credentials, contractGasProvider);
					//returnValue.put("binary", contract.getContractBinary());
					//returnValue.put("name", "");
					String txhash = GetTxHashContract(contractAddress);
					returnValue.put("txHash", txhash);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				break;
			case "INTEGRITY":
				walletFile = GetWalletFile(address);
				password = GetPassword(username);
				try {
					ContractGasProvider contractGasProvider = new DefaultGasProvider();
					String txhash = GetTxHashContract(contractAddress);
					returnValue.put("name", "Integridad de fichero");
					returnValue.put("txHash", txhash);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				break;
			default:
				break;
		}
		return returnValue;
	}
	/**
	 * Get walletFile associated to this wallet address.
	 * */
	public String GetWalletFile(String walletAddress) {
		if(walletAddress.contains("0x"))
			walletAddress = (walletAddress.split("0x")[1]).toLowerCase();
		String regexPrefix = "^UTC--[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}-[0-9]{2}-[0-9]{2}.[0-9]{1,}Z--";
		Pattern pattern = Pattern.compile(regexPrefix+walletAddress+".json");
		Matcher matcher;
		try {
			File directory = new File("./walletFiles/");
			File[] files = directory.listFiles();
			for(File f : files) {
				String name = f.getName();
				matcher = pattern.matcher(name);
				if(matcher.matches()) {
					return name;
				}
			} 
		} catch(Exception ex) {
			  System.err.println("Wallet File not found.");
			  ex.printStackTrace();
		}
		return "";
	}
	/**
	 * Send 2ETH to the new account when register happends
	 * */
	public boolean InitialTx(String toAddress) {
		String fromFileName = "UTC--2019-04-20T22-05-13.822429700Z--01905c286ad7d9912b8b71046ff61f4b49729fda.json";
		String password = "Whatajokepasswordis1234";
		System.out.println("Sending initial ETH");
		try {
			Credentials credentials = LoadCredentials(password, fromFileName);
			TransactionReceipt tx = Transfer.sendFunds(web3, credentials, 
					toAddress, BigDecimal.valueOf(2.0), Convert.Unit.ETHER).send();
			CreateTransactionLog(tx.getTransactionHash(), tx.getFrom(), tx.getTo(), 
					tx.getBlockNumber().toString(), 
					tx.getGasUsed().toString());
		} catch(Exception ex) {
			System.err.println("Error tx initial ETH");
			ex.printStackTrace();
			return false;
		} 
		return true;
	}
	/**
	 * Generate the new wallet file with the same address but the new password.
	 * */ 
	public boolean ChangeWalletPassword(Credentials _credentials, String new_password) {
		ECKeyPair pairKeys = _credentials.getEcKeyPair();
		String wallet_file = GetWalletFile(_credentials.getAddress().toString());
		try {
			if(database.Update("WALLETSLOGIN", "PASSWORD", new_password, "ADDRESS", 
					_credentials.getAddress().toString(), ConditionOperator.EQ) >= 0) {
				System.out.println("Generamos un nuevo wallet file con la clave privada" + _credentials.getAddress().toString());
				WalletUtils.generateWalletFile(new_password, pairKeys, new File("./walletFiles"), true);
				return true;
			}
			else {
				System.out.println("dejamos la wallet file como estaba");
				//Si no somos capaces de cambiar la contraseña de la cartera la dejamos como estaba
				String old_password = (database.Select("WALLETSLOGIN", ConditionOperator.EQ, 
						"ADDRESS", _credentials.getAddress().toString())).getString("PASSWORD");
				Credentials new_credentials = WalletUtils.loadCredentials(new_password, wallet_file);
				ECKeyPair newPairKeys = new_credentials.getEcKeyPair(); 
				WalletUtils.generateWalletFile(old_password, newPairKeys, new File("./walletFiles"), true);
			}
		} catch(Exception ex) {
			System.err.println("Error changing the Password of the wallet.");
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * Return the password associated to the wallet address.
	 * */
	public String GetWalletPassword(String address) {
		ResultSet results = null;
		String password = "";
		try {
			if(!database.Ping()) {
				System.err.println("Conexcion con la BBDD perdida. Reconectando...");
				database = new DatabaseConnector();
			}
			results = database.Select("WALLETSLOGIN", ConditionOperator.EQ, "ADDRESS", address);
			password = results.getNString("PASSWORD");
			return password;
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * Send an email to an address.
	 * */
	public boolean SendEmail(String username) {
		String to_Email = "";
		String body = "";
		ArrayList<String> address = new ArrayList<>();
		to_Email = GetEmail(username);
		address = GetAddress(username);
		body = "<h1>"+GetWalletPassword(address.get(0))+"</h1>";
		if(emailService.Send(to_Email, body))
			return true;
		return false;
	}
	/**
	 * Returns the email associated to the UNAME
	 * */
	public String GetEmail(String username) {
		String email = "";
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		ResultSet results = database.Select("USERSEMAIL", ConditionOperator.EQ, "UNAME", username);
		try {
			email = results.getNString("EMAIL");
		} catch(Exception ex) {
			System.err.println("Error while retrieving the Email.");
			ex.printStackTrace();
			return "";
		}
		return email;
	}
	/**
	 * Deploy a IntegrityCheck SmartContract where is stored the md5 Hash of the file.
	 * */
	public boolean IntegrityDeploy (File file, String address, String password, String username) {
		byte[] fileHash = null;
		String walletFile = GetWalletFile(address);
		ContractGasProvider contractGasProvider = new DefaultGasProvider();
		String contractAddress = "", txhash = "", block = "", value = "";
		try {
			System.out.println("Hashing "+ file.getName());
			MessageDigest hash = MessageDigest.getInstance("MD5");
			Credentials credentials = LoadCredentials(password, walletFile);
			fileHash = hash.digest();
			IntegrityCheck fileHashToStore = IntegrityCheck.deploy(web3, 
					credentials, contractGasProvider, new String(fileHash)).send();
			contractAddress = fileHashToStore.getContractAddress();
			txhash = fileHashToStore.getTransactionReceipt().get().getTransactionHash();
			block = fileHashToStore.getTransactionReceipt().get().getBlockNumber().toString();
			value = fileHashToStore.getTransactionReceipt().get().getGasUsed().toString();
			if(database.Insert("INTEGRITYCONTRACTS", new String[] {"UNAME","CONTRACTADDRESS","ADDRESS"},
					new String[] {username,contractAddress,address}) > 0) {
				if(CreateTransactionLog(txhash, address, contractAddress, block, value))
					System.out.println("Se inserto el contrato de integridad en la BD");
			}
			SaveContract(address, contractAddress, "INTEGRITY");
			return true;
		} catch(Exception ex) {
			System.err.println("Error while hashing the file.");
			ex.printStackTrace();
			return false;
		}
	}
	/**
	 * Returns the md5 hash of the file provided
	 * */
	public String Hashes(File file) {
		byte[]  fileHash = null;
		try {
			MessageDigest hash = MessageDigest.getInstance("MD5");
			fileHash = hash.digest();
			return Base64.getEncoder().encodeToString(fileHash);
		} catch(Exception ex) {
			System.err.println("Error hashing the file");
			ex.printStackTrace();
		}
		return "-1";
	}
	/**
	 * Compare the md5 hash of the file provided with all the smartcontracts deploy by this user.
	 * */
	public boolean IntegrityCheck(File file, String username, String _password) {
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		ResultSet contracts= database.Select("INTEGRITYCONTRACTS", ConditionOperator.EQ, 
				"UNAME", username);
		String password = GetPassword(username);
		ContractGasProvider contractGasProvider = new DefaultGasProvider();
		try {
			if(contracts != null) {
				int totalRows = contracts.getRow();
				for(int i=0; i < totalRows; i++) {
					String contractAddress = contracts.getNString("CONTRACTADDRESS");
					String address = contracts.getNString("ADDRESS");
					String walletFile = GetWalletFile(address);
					if(walletFile.equals("")) continue;
					if(_password.equals(GetWalletPassword(address))) {
						Credentials credentials = LoadCredentials(_password, walletFile);
						IntegrityCheck hashToCheck = IntegrityCheck.load(contractAddress, web3, 
								credentials, contractGasProvider);
						String hashContract = Base64.getEncoder().encodeToString(hashToCheck.getHash().send().getBytes());
						String hashFile = Hashes(file);
						if(hashFile.equals(hashContract)) {
							System.out.println("Hash SmartContract: "+hashContract);
							System.out.println("Hash Ficher: "+hashFile);
							return true;
						}
					}
				}
			}
		}catch(Exception ex) {
			System.err.println("Error getting contract");
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * Create a new transaction between 2 wallets
	 * */
	public void CreateTransaction() {
		
	}
	/**
	 * Returns the count of transaction for a smart contract
	 * */
	public BigInteger NextAvailableTransaction(String address) {
		BigInteger nonce = null;
		try {
			EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
			         address, DefaultBlockParameterName.LATEST).sendAsync().get();
			nonce = ethGetTransactionCount.getTransactionCount();
		} catch (Exception e) {
			System.err.println("Error while obtaining the Transaction count");
			e.printStackTrace();
		}
		return nonce;
	}
	/**
	 * Retrieve the transactions of the contract
	 * */
	public String GetTransactions(String address) {
		String returnResult = "";
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		System.out.println("Get transactions from DB of: "+address);
		try {
			ResultSet contractsFrom = database.Select("TRANSACTIONS", ConditionOperator.EQ, 
					"FROMADDRESS", address);
			if(contractsFrom != null && contractsFrom.getRow() != 0) {
				do {
					String tuple = contractsFrom.getString("TXHASH") + "#" +
							contractsFrom.getString("FROMADDRESS") + "#" +
							contractsFrom.getString("TOADDRESS") + "#" +
							contractsFrom.getString("BLOCK") + "#" +
							contractsFrom.getString("VALUE") + "#" +
							contractsFrom.getString("TXDATE");
					returnResult += tuple +";";
				} while(contractsFrom.next());
			}
			ResultSet contractsTo = database.Select("TRANSACTIONS", ConditionOperator.EQ, 
					"TOADDRESS", address);
			if(contractsFrom != null && contractsFrom.getRow() != 0) {
				do {
					String tuple = contractsTo.getString("TXHASH") + "#" +
							contractsTo.getString("FROMADDRESS") + "#" +
							contractsTo.getString("TOADDRESS") + "#" +
							contractsTo.getString("BLOCK") + "#" +
							contractsTo.getString("VALUE") + "#" +
							contractsTo.getString("TXDATE");
					returnResult += tuple +";";
				} while(contractsTo.next());
			}
		} catch(Exception ex) {
			System.err.println("Error while getting transactions");
			ex.printStackTrace();
		}
		return returnResult;
	}
	/**
	 * Create a new tuple on the Log of transactions
	 * */
	public boolean CreateTransactionLog(String txhash, String fromaddress, String toaddress,
			String block, String value) {
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		if(database.Insert("TRANSACTIONS", 
				new String[] {"TXHASH","FROMADDRESS", "TOADDRESS", "BLOCK","VALUE"},
				new String[] {txhash,fromaddress, toaddress, block, value}) > 0)
			return true;
		return false;
	}
	/**
	 * Get the hash of the tx when the contract was created
	 * */
	public String GetTxHashContract(String contractAddress) {
		String result = "";
		if(!database.Ping()) {
			System.err.println("Conexcion con la BBDD perdida. Reconectando...");
			database = new DatabaseConnector();
		}
		try {
			ResultSet contractsTo= database.Select("TRANSACTIONS", ConditionOperator.EQ, 
					"TOADDRESS", contractAddress);
			if(contractsTo != null)
				result = contractsTo.getString("TXHASH");
		} catch(Exception ex) {
			System.err.println("Error while selecting tx hash");
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * Updates the info of the pending transaction for this wallet.
	 * */
	public void UpdatePendingTransactions(String wallet) {
		String[] txObjects;
		String txs;
		if((txs = GetTransactions(wallet)) != "") {
			txObjects = txs.split(";");
			for(String var : txObjects) {
				String txHash = var.split("#")[0];
				if(var.split("#")[1] == "0x0" || var.split("#")[2] == "0x0") {
					if(web3.ethGetTransactionReceipt(txHash) != null)
						System.out.println("Update for the transaction: "+txHash);
				}
			}
		}
	}
	/**
	 * Make a SmartContract transaction.
	 * */
	public boolean DeployCustom(String address, String binary, 
			String encodedConstructor, Credentials _credentials) {
		String contractAddress = "0x0", txHash = "0x0", value;
		try {
			EthGasPrice gas = web3.ethGasPrice().send();
			BigInteger actualGas = gas.getGasPrice();
			BigInteger gasPrice = DefaultGasProvider.GAS_PRICE;
			EthBlock lastBlock =  web3.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
			RawTransaction tx = RawTransaction.createContractTransaction(
					this.NextAvailableTransaction(address), 
					gasPrice, 
					DefaultGasProvider.GAS_LIMIT,
					actualGas,
					"0x"+binary);
			value = actualGas.toString();
			byte[] signMessage = TransactionEncoder.signMessage(tx, _credentials);
			String signedTransactionData = Numeric.toHexString(signMessage);
			EthSendTransaction txResponse = web3.ethSendRawTransaction(signedTransactionData).send();
			if(txResponse.hasError()) {
				System.err.println(txResponse.getError());
				return false;
			}
			else {
				txHash = txResponse.getTransactionHash();
				System.out.println("TxHash: "+txHash);
			}
			EthGetTransactionReceipt txReceip = web3.ethGetTransactionReceipt(txResponse.getTransactionHash()).send();
			int maxTries = 0; //3
			do {
				if(txReceip.getTransactionReceipt().isPresent()) {
					 contractAddress = txReceip.getResult().getContractAddress();
				}
				else {
					System.err.println("cant obtain the Transaction Receipt: "+txReceip.getError());
					System.err.println("TXhash: "+txResponse.getTransactionHash());
				}
				TimeUnit.SECONDS.sleep(2);
				maxTries ++;
			} while(maxTries <= 3);
			System.out.println("Contract address: "+contractAddress);
			if(contractAddress != null && contractAddress != "0x0") {
				CreateTransactionLog(txHash, 
						_credentials.getAddress(), 
						contractAddress, 
						txReceip.getResult().getBlockNumber().toString(),
						txReceip.getResult().getGasUsed().toString());
			}
			else {
				System.out.println("The transaction is pending. Insert temp log on the database");
				CreateTransactionLog(txHash, 
						_credentials.getAddress().toString(), 
						contractAddress, 
						"-1", 
						value);
			}
		} catch(Exception ex) {
			System.err.println("Error while deploying custom contract.");
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Returns the balance of the wallet address provided
	 * */
	public String GetBalance(String wallet) {
		String balanceOf = "";
		try {
			EthGetBalance balance = web3.ethGetBalance(wallet, DefaultBlockParameterName.LATEST).sendAsync().get();
			balanceOf = Convert.fromWei(balance.getBalance().toString(), Unit.ETHER).toString();
		} catch(Exception ex) {
			System.err.println("Error retrieving the balance of:"+wallet);
			ex.printStackTrace();
		}
		return balanceOf;
	}
	/**
	 * Returns an Arraylist with the contracts associated to an address
	 * */
	public String ContractsAssociated(String address){
		ArrayList<String> contracts = GetContracts(address);
		if(contracts != null) {
			String joinContracts = "";
			for(String var : contracts) {
				joinContracts += var+";";
			}
			return joinContracts;
		}
		return "";
	}
	/**
	 * Close all the connections and buffers.
	 * */
	public void CloseAll() {
		database.EndConnection();
	}
}
