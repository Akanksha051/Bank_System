import java.util.ArrayList;
import java.util.List;

public class LinearProbing extends BaseClass {

    private int size;

    public LinearProbing() {
        Account emptyAccount = new Account("emptybucket", 0);
        bankStorage1d = new ArrayList<>(100000);
        for (int i = 0; i < 100000; i++) {
            bankStorage1d.add(emptyAccount);
        }
        size = 0;
    }

    @Override
    public void createAccount(String id, int count) {
        Account newAccount = new Account(id, count);
        int k = hash(id);
        while (!bankStorage1d.get(k).id.equals("emptybucket")) {
            k = (k + 1) % 100000;
        }
        bankStorage1d.set(k, newAccount);
        size++;
    }

    private List<Account> mergeSort(List<Account> bigVector, int l, int m) {
        if (bigVector.size() <= 1) {
            return bigVector;
        } else {
            int mid = (l + m) / 2;
            List<Account> leftVector = new ArrayList<>(bigVector.subList(0, mid + 1));
            List<Account> rightVector = new ArrayList<>(bigVector.subList(mid + 1, m + 1));
            leftVector = mergeSort(leftVector, 0, leftVector.size() - 1);
            rightVector = mergeSort(rightVector, 0, rightVector.size() - 1);
            return merge(leftVector, rightVector);
        }
    }

    private List<Account> merge(List<Account> leftVector, List<Account> rightVector) {
        List<Account> result = new ArrayList<>(leftVector.size() + rightVector.size());
        int i = 0, j = 0, k = 0;
        while (i < leftVector.size() && j < rightVector.size()) {
            if (leftVector.get(i).balance <= rightVector.get(j).balance) {
                result.add(leftVector.get(i));
                i++;
            } else {
                result.add(rightVector.get(j));
                j++;
            }
            k++;
        }
        while (i < leftVector.size()) {
            result.add(leftVector.get(i));
            i++;
            k++;
        }
        while (j < rightVector.size()) {
            result.add(rightVector.get(j));
            j++;
            k++;
        }
        return result;
    }

    @Override
    public List<Integer> getTopK(int k) {
        List<Account> sortedAccounts = mergeSort(bankStorage1d, 0, bankStorage1d.size() - 1);
        List<Integer> topBalances = new ArrayList<>();
        for (int i = 0; i < Math.min(k, bankStorage1d.size()); i++) {
            topBalances.add(sortedAccounts.get(sortedAccounts.size() - 1 - i).balance);
        }
        return topBalances;
    }

    @Override
    public int getBalance(String id) {
        if (!doesExist(id)) {
            return -1;
        } else {
            int k = hash(id);
            while (!bankStorage1d.get(k).id.equals(id)) {
                k = (k + 1) % 100000;
            }
            return bankStorage1d.get(k).balance;
        }
    }

    @Override
    public void addTransaction(String id, int count) {
        if (!doesExist(id)) {
            createAccount(id, count);
        } else {
            int k = hash(id);
            while (!bankStorage1d.get(k).id.equals(id)) {
                k = (k + 1) % 100000;
            }
            bankStorage1d.get(k).balance += count;
        }
    }

    @Override
    public boolean doesExist(String id) {
        int k = hash(id);
        while (!bankStorage1d.get(k).id.equals(id) && !bankStorage1d.get(k).id.equals("emptybucket")) {
            k = (k + 1) % 100000;
        }
        return bankStorage1d.get(k).id.equals(id);
    }

    @Override
    public boolean deleteAccount(String id) {
        if (!doesExist(id)) {
            return false;
        } else {
            int k = hash(id);
            while (!bankStorage1d.get(k).id.equals(id)) {
                k = (k + 1) % 100000;
            }
            bankStorage1d.set(k, new Account("emptybucket", 0));
            size--;
            return true;
        }
    }

    @Override
    public int databaseSize() {
        return size;
    }

    @Override
    public int hash(String id) {
        long hash = 0;
        final int prime = 31;
        final long modulo = 100000L;
        for (char c : id.toCharArray()) {
            hash = (hash * prime + c) % modulo;
        }
        return (int) hash;
    }
}
