import java.util.ArrayList;
import java.util.List;

public class Chaining extends BaseClass {

    private int size;

    public Chaining() {
        List<Account> emptyChain = new ArrayList<>();
        Account emptyBucket = new Account("emptystring", 0);
        emptyChain.add(emptyBucket);
        bankStorage2d = new ArrayList<>(100000);
        for (int i = 0; i < 100000; i++) {
            bankStorage2d.add(new ArrayList<>(emptyChain));
        }
        size = 0;
    }

    @Override
    public void createAccount(String id, int count) {
        Account newAccount = new Account(id, count);
        int k = hash(id);
        if (bankStorage2d.get(k).get(0).id.equals("emptystring") && bankStorage2d.get(k).size() == 1) {
            List<Account> newChain = new ArrayList<>();
            newChain.add(newAccount);
            bankStorage2d.set(k, newChain);
        } else {
            bankStorage2d.get(k).add(newAccount);
        }
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
        List<Account> result = new ArrayList<>();
        int i = 0, j = 0, k = 0;
        int leftSize = leftVector.size();
        int rightSize = rightVector.size();

        while (i < leftSize && j < rightSize) {
            if (leftVector.get(i).balance <= rightVector.get(j).balance) {
                result.add(leftVector.get(i));
                i++;
            } else {
                result.add(rightVector.get(j));
                j++;
            }
            k++;
        }

        while (i < leftSize) {
            result.add(leftVector.get(i));
            i++;
            k++;
        }

        while (j < rightSize) {
            result.add(rightVector.get(j));
            j++;
            k++;
        }

        return result;
    }

    @Override
    public List<Integer> getTopK(int k) {
        List<Account> bigTemp = new ArrayList<>();
        for (List<Account> chain : bankStorage2d) {
            bigTemp.addAll(chain);
        }
        List<Account> sortedAccounts = mergeSort(bigTemp, 0, bigTemp.size() - 1);
        List<Integer> topBalances = new ArrayList<>();
        if (k <= size) {
            for (int i = 0; i < k; i++) {
                topBalances.add(sortedAccounts.get(sortedAccounts.size() - 1 - i).balance);
            }
        } else {
            for (int i = 0; i < size; i++) {
                topBalances.add(sortedAccounts.get(sortedAccounts.size() - 1 - i).balance);
            }
        }
        return topBalances;
    }

    @Override
    public int getBalance(String id) {
        if (!doesExist(id)) {
            return -1;
        } else {
            int i = 0;
            int k = hash(id);
            while (!bankStorage2d.get(k).get(i).id.equals(id)) {
                i++;
            }
            return bankStorage2d.get(k).get(i).balance;
        }
    }

    @Override
    public void addTransaction(String id, int count) {
        if (!doesExist(id)) {
            createAccount(id, count);
        } else {
            int i = 0;
            int k = hash(id);
            while (!bankStorage2d.get(k).get(i).id.equals(id)) {
                i++;
            }
            bankStorage2d.get(k).get(i).balance += count;
        }
    }

    @Override
    public boolean doesExist(String id) {
        int k = hash(id);
        if (bankStorage2d.get(k).get(0).id.equals(id)) {
            return true;
        } else {
            for (int i = 1; i < bankStorage2d.get(k).size(); i++) {
                if (bankStorage2d.get(k).get(i).id.equals(id)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean deleteAccount(String id) {
        if (!doesExist(id)) {
            return false;
        } else {
            int k = hash(id);
            int i = 0;
            while (!bankStorage2d.get(k).get(i).id.equals(id)) {
                i++;
            }
            Account emptyAccount = new Account("emptystring", 0);
            bankStorage2d.get(k).set(i, emptyAccount);
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
