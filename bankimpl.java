import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;

public class BankImpl extends UnicastRemoteObject implements Bank {

    // Simulasi data akun dan saldo
    private HashMap<String, Double> accounts;

    public BankImpl() throws RemoteException {
        super();
        accounts = new HashMap<>();
        // Menambahkan beberapa akun untuk simulasi
        accounts.put("ACC123", 5000.00);
        accounts.put("ACC456", 3000.00);
        accounts.put("ACC789", 7000.00);
    }

    // Mengecek saldo
    public double checkBalance(String accountNumber) throws RemoteException {
        if (accounts.containsKey(accountNumber)) {
            return accounts.get(accountNumber);
        } else {
            throw new RemoteException("Account not found.");
        }
    }

    // Transfer dana antar akun
    public String transferFunds(String fromAccount, String toAccount, double amount) throws RemoteException {
        if (!accounts.containsKey(fromAccount) || !accounts.containsKey(toAccount)) {
            return "One or both accounts not found.";
        }

        if (accounts.get(fromAccount) < amount) {
            return "Insufficient funds in the account.";
        }

        // Proses transfer
        accounts.put(fromAccount, accounts.get(fromAccount) - amount);
        accounts.put(toAccount, accounts.get(toAccount) + amount);
        return "Transfer of $" + amount + " from " + fromAccount + " to " + toAccount + " successful.";
    }
}
