import java.util.ArrayList;
import java.util.List;

class Account {
    String id;
    int balance;
}

public class QuadraticProbing {
    private List<Account> bankStorage1d;
    private int size;

    public QuadraticProbing() {
        Account emptyAccount = new Account();
        emptyAccount.id = "emptybucket";
        emptyAccount.balance = 0;
        bankStorage1d = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            bankStorage1d.add(emptyAccount);
        }
        size = 0;
    }

    public void createAccount(String id, int count) {
        Account newAccount = new Account();
        newAccount.id = id;
        newAccount.balance = count;
        long k = hash(id);
        if (bankStorage1d.get((int)k).id.equals("emptybucket")) {
        } else {
            long i = 1;
            while (!bankStorage1d.get((int)k).id.equals("emptybucket")) {
                k = (k + i * i) % 100000;
                i++;
            }
        }
        bankStorage1d.set((int)k, newAccount);
        size++;
    }

    private List<Account> mergeSort(List<Account> bigvector, int l, int m) {
        if (bigvector.size() <= 1) {
            return bigvector;
        } else {
            int mid = (l + m) / 2;
            List<Account> leftvector = new ArrayList<>(bigvector.subList(0, mid + 1));
            List<Account> rightvector = new ArrayList<>(bigvector.subList(mid + 1, m + 1));

            leftvector = mergeSort(leftvector, 0, leftvector.size() - 1);
            rightvector = mergeSort(rightvector, 0, rightvector.size() - 1);
            return merge(leftvector, rightvector);
        }
    }

    private List<Account> merge(List<Account> leftvector, List<Account> rightvector) {
        List<Account> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < leftvector.size() && j < rightvector.size()) {
            if (leftvector.get(i).balance <= rightvector.get(j).balance) {
                result.add(leftvector.get(i));
                i++;
            } else {
                result.add(rightvector.get(j));
                j++;
            }
        }

        while (i < leftvector.size()) {
            result.add(leftvector.get(i));
            i++;
        }

        while (j < rightvector.size()) {
            result.add(rightvector.get(j));
            j++;
        }

        return result;
    }

    public List<Integer> getTopK(int k) {
        List<Account> sortedAccounts = mergeSort(bankStorage1d, 0, bankStorage1d.size() - 1);
        List<Integer> topKBalances = new ArrayList<>();

        int limit = Math.min(k, sortedAccounts.size());
        for (int i = 0; i < limit; i++) {
            topKBalances.add(sortedAccounts.get(sortedAccounts.size() - 1 - i).balance);
        }
        return topKBalances;
    }

    public int getBalance(String id) {
        if (!doesExist(id)) {
            return -1;
        } else {
            long i = 1;
            long k = hash(id);
            while (!bankStorage1d.get((int)k).id.equals(id)) {
                k = (k + i * i) % 100000;
                i++;
            }
            return bankStorage1d.get((int)k).balance;
        }
    }

    public void addTransaction(String id, int count) {
        if (!doesExist(id)) {
            createAccount(id, count);
        } else {
            long i = 1;
            long k = hash(id);
            while (!bankStorage1d.get((int)k).id.equals(id)) {
                k = (k + i * i) % 100000;
                i++;
            }
            Account account = bankStorage1d.get((int)k);
            account.balance += count;
            bankStorage1d.set((int)k, account);
        }
    }

    public boolean doesExist(String id) {
        long k = hash(id);
        long i = 1;
        while (i < bankStorage1d.size()) {
            if (bankStorage1d.get((int)k).id.equals(id)) {
                return true;
            }
            k = (k + i * i) % 100000;
            i++;
        }
        return false;
    }

    public boolean deleteAccount(String id) {
        if (!doesExist(id)) {
            return false;
        } else {
            long k = hash(id);
            long i = 1;
            while (!bankStorage1d.get((int)k).id.equals(id)) {
                k = (k + i * i) % 100000;
                i++;
            }
            Account emptyAccount = new Account();
            emptyAccount.id = "emptybucket";
            emptyAccount.balance = 0;
            bankStorage1d.set((int)k, emptyAccount);
            size--;
            return true;
        }
    }

    public int databaseSize() {
        return size;
    }

    private int hash(String id) {
        long hash = 0;
        final int prime = 31; // Prime number for better distribution
        final long modulo = 100000L; // Size of the hash table

        for (char c : id.toCharArray()) {
            hash = (hash * prime + c) % modulo;
        }

        return (int) hash;
    }
}
