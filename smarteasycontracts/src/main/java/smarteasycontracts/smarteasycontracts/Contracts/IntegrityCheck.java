package smarteasycontracts.smarteasycontracts.Contracts;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class IntegrityCheck extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516102b23803806102b28339810180604052602081101561003357600080fd5b81019080805164010000000081111561004b57600080fd5b8201602081018481111561005e57600080fd5b815164010000000081118282018710171561007857600080fd5b505080519093506100929250600091506020840190610099565b5050610134565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100da57805160ff1916838001178555610107565b82800160010185558215610107579182015b828111156101075782518255916020019190600101906100ec565b50610113929150610117565b5090565b61013191905b80821115610113576000815560010161011d565b90565b61016f806101436000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c8063d13319c414610030575b600080fd5b6100386100ad565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561007257818101518382015260200161005a565b50505050905090810190601f16801561009f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60008054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156101395780601f1061010e57610100808354040283529160200191610139565b820191906000526020600020905b81548152906001019060200180831161011c57829003601f168201915b505050505090509056fea165627a7a723058204a5a1c4def66c6442d91ee902f053d93997cdded6fd4cf1f8aea1e373da7a6790029";

    public static final String FUNC_GETHASH = "getHash";

    @Deprecated
    protected IntegrityCheck(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IntegrityCheck(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IntegrityCheck(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IntegrityCheck(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> getHash() {
        final Function function = new Function(FUNC_GETHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static IntegrityCheck load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IntegrityCheck(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IntegrityCheck load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IntegrityCheck(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static IntegrityCheck load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new IntegrityCheck(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IntegrityCheck load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IntegrityCheck(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IntegrityCheck> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _fileHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_fileHash)));
        return deployRemoteCall(IntegrityCheck.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<IntegrityCheck> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _fileHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_fileHash)));
        return deployRemoteCall(IntegrityCheck.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<IntegrityCheck> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _fileHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_fileHash)));
        return deployRemoteCall(IntegrityCheck.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<IntegrityCheck> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _fileHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_fileHash)));
        return deployRemoteCall(IntegrityCheck.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
