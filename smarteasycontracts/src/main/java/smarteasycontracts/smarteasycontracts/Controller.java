package smarteasycontracts.smarteasycontracts;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
/**
 * @author Javier Iglesias Sanz
 * @version 1.0
 * @since 2019-02-21
 * <p>This class contains the controller</p>
 */
@org.springframework.stereotype.Controller
public class Controller {
	Utils utility = new Utils();
	Thread txPendingThread = new Thread(new TransactionUpdater());
	/*Views: this only return the .jsp to load*/
	@GetMapping({"/","/index"})
	public String index() {
		if(!txPendingThread.isAlive())
			txPendingThread.start();
		return "redirect:/login";
	}
	/**
	 * @custom.GetMapping /login"
	 * */
	@GetMapping({"/login","/login"})
	public String Login(HttpServletRequest request) {//this is only used to load the login page view
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null) {
			return "redirect:/profile";
		}
		return "login";
	}
	@GetMapping({"/registration","/registration"})
	public String Registration(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "redirect:/profile";
		return "registration";
	}
	@GetMapping({"/logout","/logout"})
	public String Lougot(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			session. invalidate();
		return "redirect:/index";
	}
	@RequestMapping(value="/createNewWallet", method=RequestMethod.POST)
	@GetMapping({"/createNewWallet", "/createNewWallet"})
	public String CreateNewWallet(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "createWallet";
		return "redirect:/index";
	}
	@GetMapping({"/success","/success"})
	public String Success() {
		return "success";
	}
	@GetMapping({"/fail","/fail"})
	public String Fail() {
		return "fail";
	}
	@GetMapping({"/changePassword","/changePassword"})
	public String ChangePassword(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "changePassword";
		return "redirect:/index";
	}
	@GetMapping({"/createWallet","/createWallet"})
	public String CreateWallet(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "createWallet";
		return "redirect:/index";
	}
	@GetMapping({"/restorePassword","/restorePassword"})
	public String RestorePassword(HttpServletRequest request) {
		return "restorePassword";
	}
	@GetMapping({"/integrityCheck","/integrityCheck"})
	public String IntegrityCheck(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "integrityCheck";
		return "redirect:/index";
	}
	@GetMapping({"/deployContracts","/deployContracts"})
	public String DeployContract(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "deployContracts";
		return "redirect:/index";
	}
	@GetMapping({"/compile","/compile"})
	public String Compile(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("uname") != null)
			return "compile";
		return "redirect:/index";
	}
	//Views End
	@GetMapping({"/profile","/profile"})
	public String Profile(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ArrayList<String> address = new ArrayList<>();
		String username, email, wallet;
		if(session.getAttribute("uname")== null) {
			session.setAttribute("notificationMessage", "Error retrieving information.");
			session.setAttribute("isSuccess", "false");
			return "redirect:/login";
		}
		try {
			username = session.getAttribute("uname").toString();
			email = utility.GetEmail(username);
			session.setAttribute("email", email);
			address = utility.GetAddress(username);
			if(address != null) {
				String joinAddresses = "";
				boolean first = true;
				for(String temp : address) {
					if(first && session.getAttribute("wallet") == null ) session.setAttribute("wallet", temp);
					joinAddresses += temp + ";";
				}
				session.setAttribute("addresses", joinAddresses);
			}
			if(session.getAttribute("wallet") != null) {
				wallet = session.getAttribute("wallet").toString();
				ArrayList<String> contracts = utility.GetContracts(wallet);
				session.setAttribute("contracts", "");
				session.setAttribute("wallet", wallet);
				if(contracts != null) {
					String joinContracts = "";
					for(String var : contracts) {
						joinContracts += var+";";
					}
					session.setAttribute("contracts",joinContracts);
				}
				session.setAttribute("balance", utility.GetBalance(wallet));
			}
		} catch (Exception ex) {
			System.err.println("Error retrieving information.");
			ex.printStackTrace();
			session.setAttribute("notificationMessage", "Error retrieving information.");
			 session.setAttribute("isSuccess", "false");
		}
		return "profile";
	}
	/**
	 * Manage the process of login.
	 * */
	@RequestMapping(value = "/login_process", method=RequestMethod.POST)
	@GetMapping({"/login_process","/login_process"})
	public String Login_process(
			@RequestParam(value="email", required=true,
			defaultValue="null")String email,
			@RequestParam(value="uname", required=true,
			defaultValue="null")String username,
			@RequestParam(value="pwd", required=true,
			defaultValue="null")String password,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(password == "" || username =="") return "redirect:/login";
		 try {
			System.out.println("Logging in as " + username);
			String passwd = utility.GetPassword(username);
			String cryptPassword = utility.Encode(password);
			/*encryptamos la contraseña y la comparamos con la que esta guardadad en la bbdd*/
			if(cryptPassword.compareTo("") != 0)
				System.out.println("Crypt: "+cryptPassword);
			//TODO comprobar que funciona la encruiptacion
			if(cryptPassword.equals(passwd)){//if the password is the correct
				session.setAttribute("uname", username);
				session.setAttribute("notificationMessage", "You are now logged in");
				session.setAttribute("isSuccess", "true");
				return "redirect:/profile";
			}
		 } catch(Exception ex) {
			 System.err.println("Error while loggin process.");
			 ex.printStackTrace();
			 session.setAttribute("notificationMessage", "The logging process failed");
			 session.setAttribute("isSuccess", "false");
		 }
		return "redirect:/login";
	}
	/**
	 * Manage the operations of registration.
	 * */
	@RequestMapping(value = "/registration_process", method	=
			RequestMethod.POST)
	@GetMapping({"/registration_process","/registration_process"})
	public String Registration_process(
			@RequestParam(value="email", required=true,
			defaultValue="null")String email,
			@RequestParam(value="uname", required=true,
			defaultValue="null")String username,
			@RequestParam(value="pwd", required=true,
			defaultValue="null")String password,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		String cryptPassword = utility.Encode(password);
		if(cryptPassword.compareTo("") != 0)
			System.out.println("Crypt: "+cryptPassword);
		if(utility.RegisterNewUser(username, password, email)) {
			try {
				String walletFileName = WalletUtils.generateNewWalletFile(
						password,
					new File("./walletFiles/"));
				System.out.println("Creamos cartera nueva: "+walletFileName);
				Credentials _credentials = utility.LoadCredentials(
						password,walletFileName);
				String address = _credentials.getAddress();
				session.setAttribute("address", address);
				utility.InitialTx(address);
				if(utility.RegisterNewWallet(username, address, password)) {
					session.setAttribute("isSuccess", "true");
					session.setAttribute("notificationMessage", "The registration process was success");
					return "redirect:/login";
				}
				else return "redirect:/registration";
			} catch (Exception ex) {
				System.err.println("Error while creating the new wallet.");
				ex.printStackTrace();
				session.setAttribute("notificationMessage", "The registration process failed");
				session.setAttribute("isSuccess", "false");
				return "redirect:/registration";
			}
		} else return "redirect:/registration";
	}
	/**
	 * Generate and compile the ERC20 with the solc compiler.
	 * */
	@RequestMapping(value = "/generateERC20", method=RequestMethod.POST)
	@GetMapping({"/generateERC20","/generateECR20"})
	private String GenerateERC20(HttpServletRequest request,
			@RequestParam(value="TokenName", required=true,
			defaultValue="default_token")String tokenName,
			@RequestParam(value="SYM", required = true,
			defaultValue="SYM")String SYM,
			@RequestParam(value="Decimals", required=true,
			defaultValue="18")String decimals,
			@RequestParam(value="totalSupply", required=true,
			defaultValue="100")String totalSupply,
			@RequestParam(value="pwd", required=true)String password) {
		HttpSession session = request.getSession();
		//ProcessBuilder compiler = new ProcessBuilder();
		//String compile = "solc "+tokenName+".sol --abi >> "+tokenName+".abi;";
		String address = session.getAttribute("wallet").toString();
		String walletFile = utility.GetWalletFile(address);//WalletFile name
		System.out.println("wallet file name match: "+walletFile);
		try {
			//String password = utility.GetPassword(session.getAttribute("uname").toString());
			if(utility.WriteContractERC20(password,walletFile,
					new BigInteger(totalSupply), tokenName, SYM,
					new BigInteger(decimals))) {
				session.setAttribute("notificationMessage", "The deloy of the token was success.");
				session.setAttribute("isSuccess", "true");
				return "redirect:/deployContracts";
			}
			//compiler.command("bash", "-c", compile);
			//compiler.start();
			/*if(utility.CompileContract(tokenName))
					System.out.println("Correct contract compilation;");*/
		} catch(Exception ex) {
			System.err.println("Error saving the Contract:");
			ex.printStackTrace();
		}
		session.setAttribute("notificationMessage", "The deloy of the token failed");
		session.setAttribute("isSuccess", "false");
		return "redirect:/deployContracts";
	}
	/**
	 * To Deploy a Contract
	 **/
	@RequestMapping(value = "/deploy", method=RequestMethod.POST)
	@GetMapping({"/deploy","/deploy"})
	public String GenerateWrapper(HttpServletRequest request,
			@RequestParam(value="TokenName", required=true,
			defaultValue="default_token")String tokenName,
			@RequestParam(value="SYM", required = true,
			defaultValue="SYM")String SYM,
			@RequestParam(value="Decimals", required=true,
			defaultValue="18")String decimals,
			@RequestParam(value="totalSupply", required=true,
			defaultValue="100.0")String totalSupply,
			@RequestParam(value="pwd") String _password) {
		HttpSession session = request.getSession();
		try {
			String wallet = session.getAttribute("wallet").toString();
			String walletFile = utility.GetWalletFile(wallet.split("0x")[1]);
			System.out.println("wallet file name match: "+walletFile);
			String password = utility.GetPassword(session.getAttribute("uname").toString());
			if(_password.equals(password)) {
				String contract = utility.Deploy(password, walletFile,
						new BigInteger(totalSupply), tokenName, SYM,
						new BigInteger(decimals));
				System.out.println("ERC721: "+contract);
				session.setAttribute("notificationMessage", "The deloy of the token was success");
				session.setAttribute("isSuccess", "true");
				return "redirect:/deployContracts";
			}
		} catch(Exception ex) {
			System.err.println("Error while deploying ERC721");
			ex.printStackTrace();
		}
		session.setAttribute("notificationMessage", "The deloy of the token failed");
		session.setAttribute("isSuccess", "false");
		return "redirect:/deployContracts";
	}
	/**
	 * Deploy a Smartcontract with the hash of a file
	 * */
	@RequestMapping(value = "/integrityDeploy", method=RequestMethod.POST)
	@GetMapping({"/integrityDeploy","/integrityDeploy"})
	public String IntegrityDeploy(HttpServletRequest request,
			@RequestParam(value = "file", required=true) MultipartFile file,
			@RequestParam(value = "pwd", required=true) String password) {
		HttpSession session = request.getSession();
		String username = session.getAttribute("uname").toString();
		try {
			if(file == null); //return "redirect:/profile";
			byte[] bytes = file.getBytes();
			Path filePath = Paths.get("./integrityFiles/"+file.getOriginalFilename());
			Files.write(filePath, bytes);
			if(utility.IntegrityDeploy(filePath.toFile(), session.getAttribute("wallet").toString(),
					password, username)) {
				session.setAttribute("notificationMessage", "The deloy of the integrity contract was success");
				session.setAttribute("isSuccess", "true");
				return "redirect:/deployContracts";
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		session.setAttribute("notificationMessage", "The deloy of the integrity contract failed");
		session.setAttribute("isSuccess", "false");
		return "redirect:/deployContracts";
	}
	/**
	 * Checks if the file provide has a smartcontract deployed with the same hash.
	 * */
	@RequestMapping(value = "/integrityCheck", method=RequestMethod.POST)
	@GetMapping({"/integrityCheck","/integrityCheck"})
	public String IntegrityCheck(HttpServletRequest request,
			@RequestParam(value = "integrityFile", required=true) MultipartFile file,
			@RequestParam(value="pwd") String password) {
		HttpSession session = request.getSession();
		String username = session.getAttribute("uname").toString();
		if(file == null); //return "redirect:/profile";
		try {
			byte[] bytes = file.getBytes();
			Path filePath = Paths.get("./integrityFiles/"+file.getOriginalFilename());
			Files.write(filePath, bytes);
			if(utility.IntegrityCheck(filePath.toFile(), username, password)) {
				System.out.println("Fichero Integro.");
				session.setAttribute("notificationMessage", "The integrity check was success");
				session.setAttribute("isSuccess", "true");
			}
		} catch(Exception ex) {
			System.err.println("Error uploading the file to check.");
			ex.printStackTrace();
			session.setAttribute("notificationMessage", "The integrity check failed");
			session.setAttribute("isSuccess", "false");
		}
		return "redirect:/profile";
	}
	/**
	 * Let the user upload a new Wallet file and associate to its username.
	 * */
	@RequestMapping(value = "/uploadFile", method=RequestMethod.POST)
	@GetMapping({"/uploadFile","/uploadFile"})
	private String UploadFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile file, 
			@RequestParam("password") String _password) {
		HttpSession session = request.getSession();
		try {
			System.out.println("uploading...");
			byte[] bytes = file.getBytes();
			Path filePath;
			if(!file.getOriginalFilename().contains(".json"))
				filePath = Paths.get("./walletFiles/"+file.getOriginalFilename()+".json");
			else filePath = Paths.get("./walletFiles/"+file.getOriginalFilename());
			Files.write(filePath, bytes);
			if(utility.ImportWallet(filePath.toAbsolutePath().toString(), session.getAttribute("uname").toString(), _password)) {
				session.setAttribute("notificationMessage", "File uploaded");
				session.setAttribute("isSuccess", "true");
			} else {
				session.setAttribute("notificationMessage", "Error while importing the new wallet, try again.");
				session.setAttribute("isSuccess", "false");
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			session.setAttribute("notificationMessage", "File was not uploaded");
			session.setAttribute("isSuccess", "false");
		}
		return "redirect:/profile";
	}
	/**
	 * Changes the password of the wallet already generate.
	 * */
	@RequestMapping(value="/changePasswrd", method=RequestMethod.POST)
	@GetMapping({"/changePasswrd", "/changePasswrd"})
	public String ChangePassword(HttpServletRequest request,
			@RequestParam(value = "old_password",required=true) String old_password,
			@RequestParam(value = "new_password", required=true) String new_password,
			@RequestParam(value = "walletRecovery", required=true) String address) {
		HttpSession session = request.getSession();
		if(old_password == new_password) {
			session.setAttribute("notificationMessage", "The new password cant be the same already stablish");
			session.setAttribute("isSuccess", "false");
			return "redirect:/createWallet";
		}
		try {
			String walletFile = utility.GetWalletFile(address);
			Credentials _credentials = utility.LoadCredentials(old_password, walletFile);
			if(utility.ChangeWalletPassword(_credentials, new_password)) {
				File toDelete = new File("./walletFiles/" + walletFile);
				if(toDelete.delete()) {
					session.setAttribute("notificationMessage", "Password changed correctly");
					session.setAttribute("isSuccess", "true");
				}else {
					session.setAttribute("notificationMessage", "The sistem couldnt delete the old wallet file, contact with the administrator.");
					session.setAttribute("isSuccess", "false");
				}
				return "redirect:/createWallet";
			}
		} catch(Exception ex) {
			System.err.println("Error while changing the password web.");
			ex.printStackTrace();
			session.setAttribute("notificationMessage", "Error while changing the password");
			session.setAttribute("isSuccess", "false");
		}
		session.setAttribute("notificationMessage", "Error while changing the password");
		session.setAttribute("isSuccess", "false");
		return "redirect:/createWallet";
	}
	/**
	 * Send an email with the password of the wallet.
	 * */
	@RequestMapping(value="/restoreEmail", method=RequestMethod.POST)
	@GetMapping({"/restoreEmail", "/restoreEmail"})
	public String RestoreEmal(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String username = session.getAttribute("uname").toString();
		if(utility.SendEmail(username)) {
			session.setAttribute("notificationMessage", "Check your email, the mail is already on route");
			session.setAttribute("isSuccess", "true");
		}
		else {
			session.setAttribute("notificationMessage", "The email was not sent, please try again");
			session.setAttribute("isSuccess", "false");
		}
		
		return "redirect:/createWallet";
	}
	/**
	 * Create a new wallet associated to an already exists app account and
	 * register it on the DB.
	 * */
	@RequestMapping(value="/creationProcess", method=RequestMethod.POST)
	@GetMapping({"/creationProcess", "/creationProcess"})
	public String CreationProcess(HttpServletRequest request,
			@RequestParam("pwd") String password) {
		HttpSession session = request.getSession();
		String username = session.getAttribute("uname").toString();
		if(utility.CreateWallet(password, username)) {
			session.setAttribute("notificationMessage", "Wallet created correctly, now you can use it.");
			session.setAttribute("isSuccess", "true");
			return "redirect:/createWallet";
		}
		session.setAttribute("notificationMessage", "The wallet was not created.");
		session.setAttribute("isSuccess", "false");
		return "redirect:/createWallet";
	}
	@RequestMapping(value="/changeWalletSelecion", method=RequestMethod.POST)
	@GetMapping({"/changeWalletSelecion", "/changeWalletSelecion"})
	public String ChangeWalletSelection(HttpServletRequest request,
			@RequestParam(value = "wallet",required=false,
			defaultValue="null")  String txAddress,
			@RequestParam(value = "pwd",required=true) String password) {
		HttpSession session = request.getSession();
		if(txAddress.equals("null")) return "redirect:/profile";
		try {
			//String username = session.getAttribute("uname").toString();
			//String txAddress = session.getAttribute("addresses").todString().split(";")[0];
			//String password = utility.GetPassword(username);
			String walletFile = utility.GetWalletFile(txAddress);
			String passwordHash= utility.GetWalletPassword(txAddress);
			if(passwordHash.equals(password)){
				Credentials credentials = utility.LoadCredentials(password, walletFile);
				if(credentials != null) {
					utility._credentials = credentials;
					session.setAttribute("WalletFile", walletFile);
					ArrayList<String> contracts = utility.GetContracts(txAddress);
					System.out.println("cargamos contratos para: "+txAddress);
					session.setAttribute("contracts", "");
					session.setAttribute("wallet", txAddress);
					if(contracts != null) {
						String joinContracts = "";
						for(String var : contracts) {
							joinContracts += var+";";
						}
						session.setAttribute("contracts",joinContracts);
					}
					session.setAttribute("notificationMessage", "Wallet changed");
					session.setAttribute("isSuccess", "true");
					return "redirect:/profile";
				}
			}	
		} catch(Exception ex) {
			System.err.println("Error while retrieving the contracts.");
			ex.printStackTrace();
			session.setAttribute("notificationMessage", "Cant change the wallet, try again, please");
			session.setAttribute("isSuccess", "false");
		}
		session.setAttribute("notificationMessage", "Password provided is not correct, try again.");
		session.setAttribute("isSuccess", "false");
		return "redirect:/profile";
	}
	/**
	 * Get the information of the Contract provided
	 * */
	@RequestMapping(value = "/contract", method=RequestMethod.GET)
	@GetMapping({"/contract","/contract"})
	public String Contract(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String type = "";
		if(session.getAttribute("uname") != null) {
			ShowTransactions(request);
			String username = session.getAttribute("uname").toString();
			String wallet = session.getAttribute("wallet").toString();
			String contractAddress =request.getParameter("address");
			type = utility.GetType(contractAddress);
			session.setAttribute("type", type);
			HashMap<String,String> information = utility.GetInfoContract(wallet,username, contractAddress, type);
			for(String key : information.keySet()) {
				session.setAttribute(key, information.get(key));
			}
			return "contract";
		}
		session.setAttribute("notificationMessage", "The contract is not available, try again later.");
		session.setAttribute("isSuccess", "false");
		return "redirect:/profile";
	}
	/**
	 * Get the transactions of an address.
	 * */
	@RequestMapping(value="/transactions", method=RequestMethod.GET)
	@GetMapping({"/transactions", "/transactions"})
	public String ShowTransactions(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String address = request.getParameter("address");
		utility.UpdatePendingTransactions(address);
		String transactionsLog = utility.GetTransactions(address);
		session.setAttribute("transactions", transactionsLog);
		return "";//"redirect:/contract?address="+request.getParameter("address");
	}
	/**
	 * Deploy a custom made Smart contract
	 * */
	@RequestMapping(value="/deployCustom", method=RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String DeployCustom(HttpServletRequest request,
			@RequestBody String binary) {
		HttpSession session = request.getSession();
		session.setAttribute("deployed", "true");
		String address = session.getAttribute("wallet").toString();
		String walletFile = utility.GetWalletFile(address);
		String password = utility.GetWalletPassword(address);
		//String password = session.getAttribute("password").toString();
		Credentials credentials = null;
		try {
			if(utility._credentials != null) {
				credentials = utility._credentials;
			} else { 
				credentials = WalletUtils.loadCredentials(password, walletFile);
			}
			if(utility.DeployCustom(address, binary, "", credentials)) {
				session.setAttribute("notificationMessage", "Custom contract deployed");
				session.setAttribute("isSuccess", "true");
				return "redirect:/compile";
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		session.setAttribute("notificationMessage", "Cant deploy the custom  contract, try changing the wallet please.");
		session.setAttribute("isSuccess", "false");
		return "redirect:/compile";
	}
}
