package smarteasycontracts.smarteasycontracts;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import smarteasycontracts.smarteasycontracts.DatabaseConnector.ConditionOperator;
/**
 * @author Javier Iglesias Sanz
 * @version 1.0
 * @since 2019-02-21
 *	This thread is the one that updates the information of the Pending transaction.
 */
public class TransactionUpdater implements Runnable {
	public TransactionUpdater() {}
	
	@Override
	public void run() {
		DatabaseConnector database = new DatabaseConnector();
		Utils utility = new Utils();
		if(utility == null || !database.Ping()) {
			System.out.println("Finalizing the Thread because there is no OK connection to DB or Eth");
			try {
				this.finalize();
				Thread.currentThread().join();
				return;
			} catch (Exception e) {
				System.err.println("Error generic Thread.");
				e.printStackTrace();
			} catch (Throwable e) {
				System.err.println("Error finalizing Thread.");
				e.printStackTrace();
			}
		}
		ResultSet pendingTxs;
		while((pendingTxs = database.Select("TRANSACTIONS", ConditionOperator.EQ, "TOADDRESS", "0x0")) != null) {
			try {
				int maxTries = 0;
				while(pendingTxs.next()) {
					String txhash = pendingTxs.getString("TXHASH");
					String contractAddress;
					do {
						EthGetTransactionReceipt txReceip = utility.web3.ethGetTransactionReceipt(txhash).send();
						if(txReceip.getTransactionReceipt().isPresent()) {
							 contractAddress = txReceip.getResult().getContractAddress();
							 if(contractAddress != null) {
								 if(database.Update("TRANSACTIONS", "TOADDRESS", contractAddress,
										 "TXHASH", txhash, ConditionOperator.EQ) > 0);
							 }
						}
						TimeUnit.SECONDS.sleep(2);
						maxTries ++;
					} while(maxTries <= 3);
				}
				Thread.sleep(2000);
			} catch(Exception ex) {
				System.err.println("Error while sleeping the thread.");
				ex.printStackTrace();
			}
		}
	}
}
