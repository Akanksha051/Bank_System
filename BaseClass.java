import java.util.List;
import java.util.ArrayList;

class Account {
    String id;
    int balance;

    public Account(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }
}

abstract class BaseClass {
    public abstract void createAccount(String id, int count);
    public abstract List<Integer> getTopK(int k);
    public abstract int getBalance(String id);
    public abstract void addTransaction(String id, int count);
    public abstract boolean doesExist(String id);
    public abstract boolean deleteAccount(String id);
    public abstract int databaseSize();
    public abstract int hash(String id);

    protected List<Account> bankStorage1d = new ArrayList<>();
    protected List<List<Account>> bankStorage2d = new ArrayList<>();
}
