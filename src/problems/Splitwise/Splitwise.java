package problems.Splitwise;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Supplier;

/**
 * 
 * Splitwise
 * 
 * Functional requirement
 * 
 * 1. User can create groups
 * 2. Group can have many users
 * 3. User can create Expense
 * 4. Exense can have multiple splits
 * 5. Split can be of different types - Equal, Precentage
 * 6. Group can have its own balance sheet
 * 7. Group can have its own settlement
 * 
 * 
 * Non-Functional requirement
 * 
 * 1. Thread safety
 * 2. Correctness
 * 3. Extensibility
 * 4. SOLID Principles
 * 
 * 
 * Core Classes/Interfaces/Enums
 * 
 * User
 * Group
 * Expense
 * Split
 * SplitStrategy
 * ExpenseRequest
 * SplitwiseService
 * 
 */

enum SplitType {
    EQUAL,
    EXACT,
    PERCENTAGE
}

interface SplitStrategy {
    List<Split> calculateSplit(SplitRequest splitRequest);
}

class EqualSplit implements SplitStrategy {
    @Override
    public List<Split> calculateSplit(SplitRequest splitRequest) {
        double amount = splitRequest.getAmount();

        if (amount <= 0) {
            System.out.println("Error: Expense amount cannot be negative");
        }

        List<User> users = splitRequest.getUsers();

        double totalMember = users.size();

        List<Split> splits = new ArrayList<>();

        for (User user : users) {
            splits.add(new Split(user, amount / totalMember));
        }

        return splits;
    }

}

class ExactSplit implements SplitStrategy {
    @Override
    public List<Split> calculateSplit(SplitRequest splitRequest) {
        if (splitRequest.getSplitPairs().size() != splitRequest.getUsers().size()) {
            System.out.println("Error: Mismatch involved members and exact split");
            return null;
        }

        List<User> users = splitRequest.getUsers();
        Map<User, Double> splitPairs = splitRequest.getSplitPairs();

        List<Split> splits = new ArrayList<>();

        for (User user : users) {
            splits.add(new Split(user, splitPairs.get(user)));
        }

        return splits;
    }
}

class PercentageSplit implements SplitStrategy {
    @Override
    public List<Split> calculateSplit(SplitRequest splitRequest) {
        if (splitRequest.getSplitPairs().size() != splitRequest.getUsers().size()) {
            System.out.println("Error: Mismatch involved members and percentage split");
            return null;
        }

        List<User> users = splitRequest.getUsers();
        Map<User, Double> splitPairs = splitRequest.getSplitPairs();

        double totalPercentage = splitPairs.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        if (Math.abs(totalPercentage - 100.0) > 0.001) {
            System.out.println("Error: Percentages must sum to 100%, got: " + totalPercentage + "%");
            return null;
        }

        List<Split> splits = new ArrayList<>();

        for (User user : users) {
            double amount = splitRequest.getAmount() * (splitPairs.get(user) / 100);
            splits.add(new Split(user, amount));
        }

        return splits;
    }
}

class SplitFactory {
    private final static Map<SplitType, Supplier<SplitStrategy>> strategies = Map.of(
            SplitType.EQUAL,
            () -> new EqualSplit(),

            SplitType.EXACT,
            () -> new ExactSplit(),

            SplitType.PERCENTAGE,
            () -> new PercentageSplit());

    public static SplitStrategy getSplittrategy(SplitType type) {
        Supplier<SplitStrategy> strategy = strategies.get(type);

        if (strategy == null) {
            throw new RuntimeException("Unsupported split: " + type);
        }

        return strategy.get();
    }
}

class SplitRequest {
    private final double amount;
    private final List<User> users;
    private final Map<User, Double> splitPairs;

    public SplitRequest(Builder builder) {
        amount = builder.amount;
        users = builder.users;
        splitPairs = builder.splitPairs;
    }

    public static class Builder {
        private double amount;
        private List<User> users;
        private Map<User, Double> splitPairs;

        public Builder() {
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setUsers(List<User> users) {
            this.users = users;
            return this;
        }

        public Builder setSplitPairs(Map<User, Double> splitPairs) {
            this.splitPairs = splitPairs;
            return this;
        }

        public SplitRequest build() {
            return new SplitRequest(this);
        }
    }

    public double getAmount() {
        return amount;
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<User, Double> getSplitPairs() {
        return splitPairs;
    }
}

class User {
    private static int userId = 0;
    private final String id;
    private final String name;
    private final String email;

    public User(String name, String email) {
        this.id = "User-" + ++userId;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

class DebtSimplifier {
    public static Map<User, Map<User, Double>> simplifyGroupDebts(Map<User, Map<User, Double>> balanceSheet) {
        Map<User, Double> netBalances = new HashMap<>();

        // Calculate all user's net balance
        for (Map.Entry<User, Map<User, Double>> entry : balanceSheet.entrySet()) {
            User userA = entry.getKey();
            double net = 0.00;

            for (double amount : entry.getValue().values()) {
                net += amount;
            }

            netBalances.put(userA, net);
        }

        // Define creditors and debitors

        // Max heap for creditors
        PriorityQueue<Map.Entry<User, Double>> creditors = new PriorityQueue<>(
                (a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Min heap for debitors
        PriorityQueue<Map.Entry<User, Double>> debtors = new PriorityQueue<>(
                (a, b) -> Double.compare(a.getValue(), b.getValue()));

        for (Map.Entry<User, Double> entry : netBalances.entrySet()) {
            double balance = entry.getValue();

            if (balance > 0.001) {
                // User should receive money
                creditors.offer(new AbstractMap.SimpleEntry<>(entry.getKey(), balance));
            } else {
                // User must pay money
                debtors.offer(new AbstractMap.SimpleEntry<>(entry.getKey(), balance));
            }
        }

        // Based on the net balance simplified debt
        Map<User, Map<User, Double>> simplifiedBalanceSheet = new HashMap<>();

        /**
         * 
         * Time complexity: O(UlogU)
         * Space complexity: O(U) for queues
         * 
         * U - no. of users
         * 
         */
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Map.Entry<User, Double> creditor = creditors.poll();
            Map.Entry<User, Double> debtor = debtors.poll();

            User receiver = creditor.getKey();
            User payer = debtor.getKey();

            /**
             * Get minimum balance of both
             * Case 1: payer is paying less than receiver balance
             * Case 2: payer is paying more than receiver balance
             */
            double amount = Math.min(creditor.getValue(), -debtor.getValue());

            // Payer owes receiver
            simplifiedBalanceSheet.computeIfAbsent(payer, k -> new HashMap<>()).put(receiver, -amount);

            // Receiver gets money from payer
            simplifiedBalanceSheet.computeIfAbsent(receiver, k -> new HashMap<>()).put(payer, amount);

            double creditorRemaining = creditor.getValue() - amount;
            double debtorRemaining = debtor.getValue() + amount;

            if (creditorRemaining > 0.001) {
                creditors.offer(new AbstractMap.SimpleEntry<>(receiver, creditorRemaining));
            }

            if (debtorRemaining < -0.001) {
                debtors.offer(new AbstractMap.SimpleEntry<>(payer, debtorRemaining));
            }
        }

        return simplifiedBalanceSheet;
    }
}

class Group {
    private static int groupId = 0;
    private final String id;
    private final String name;

    private List<User> users = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();
    private Map<User, Map<User, Double>> balanceSheet = new HashMap<>();

    public Group(String name) {
        this.id = "Group-" + ++groupId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void addUser(User user) {
        users.add(user);

        // Initialize balance sheet of added user
        balanceSheet.put(user, new HashMap<User, Double>());
    }

    public void removeUser(User user) {
        // Validate if user has any outstanding

        // Remove user if no outstanding
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean checkUserBelongToGroup(User user) {
        for (User member : users) {
            if (user == member) {
                return true;
            }
        }

        return false;
    }

    public void updateGroupBalance(User fromUser, User toUser, double amount) {
        if (fromUser.equals(toUser)) {
            return;
        }

        /**
         * Positive balance for user1 -> user2 indicate user2 owes user1
         * Negative balance for user1 -> user2 indicate user1 owes user2
         */
        Map<User, Double> fromBalances = balanceSheet.get(fromUser);
        Map<User, Double> toBalances = balanceSheet.get(toUser);

        if (fromBalances == null || toBalances == null) {
            throw new IllegalArgumentException("Both users must belong to the group");
        }

        fromBalances.merge(toUser, amount, Double::sum);
        toBalances.merge(fromUser, -amount, Double::sum);

        // Remove zero balances safely
        if (Math.abs(fromBalances.get(toUser)) < 0.001) {
            fromBalances.remove(toUser);
            toBalances.remove(fromUser);
        }
    }

    public void addGroupExpense(ExpenseRequest request, List<User> users) {
        // validate paid and all users if they are part of this group
        if (!checkUserBelongToGroup(request.getPaidByUser())) {
            System.out.println("Error: Paid user doesn't belong to this group");
            return;
        }

        for (User user : request.getInvolvedUsers()) {
            if (!checkUserBelongToGroup(user)) {
                System.out.println("Error: Some users doesn't belong to this group");
                return;
            }
        }

        // Get split strategy and generate split
        SplitStrategy splitStrategy = SplitFactory.getSplittrategy(request.getSplitType());
        SplitRequest splitRequest = new SplitRequest.Builder()
                .setAmount(request.getAmount())
                .setUsers(request.getInvolvedUsers())
                .setSplitPairs(request.getSplitPairs())
                .build();

        List<Split> splits = splitStrategy.calculateSplit(splitRequest);

        if (splits == null) {
            System.out.println(String.format("Cannot add expense %s\n", request.getDescription()));
            return;
        }

        // Create expense with this groupId
        Expense expense = new Expense(request.getDescription(), request.getPaidByUser(), request.getAmount(),
                request.getSplitType(), splits, getId());

        // Add expense in expenses
        expenses.add(expense);

        // Update balance
        for (Split split : splits) {
            updateGroupBalance(request.getPaidByUser(), split.getUser(), split.getAmount());
        }

        System.out.println(String.format("Expense added for %s (%s)", request.getDescription(), request.getAmount()));

        String members = "";
        for (User user : request.getInvolvedUsers()) {
            members += user.getName() + " ";
        }

        System.out.println(String.format("        Involved members: %s", members));
        System.out.println(String.format("        Split Type: %s", request.getSplitType()));
        System.out.println(String.format("        Paid By: %s\n", request.getPaidByUser().getName()));
    }

    public void settlePayment(User fromUser, User toUser, double amount) {
        // update balance of both from and to users
        // if any of the user's balance reaches 0 remove them
        updateGroupBalance(fromUser, toUser, amount);
    }

    public void showUserBalanceSheet(User user) {
        Map<User, Double> userBalances = balanceSheet.get(user);

        System.out.println(user.getName() + ":");

        if (userBalances == null || userBalances.isEmpty()) {
            System.out.println("    No outstanding balances\n");
            return;
        }

        for (Map.Entry<User, Double> entry : userBalances.entrySet()) {
            User otherUser = entry.getKey();
            double amount = entry.getValue();

            if (amount < 0) {
                System.out.println(
                        "    Owes " + otherUser.getName()
                                + ": " + String.format("%.2f", -amount));
            } else if (amount > 0) {
                System.out.println(
                        "    " + otherUser.getName() + " owes " + user.getName()
                                + ": " + String.format("%.2f", amount));
            }
        }

        System.out.println();
    }

    public void showGroupBalanceSheet() {
        for (Map.Entry<User, Map<User, Double>> entry : balanceSheet.entrySet()) {
            showUserBalanceSheet(entry.getKey());
        }
        System.out.println("============================================");
    }

    public void simplifyGroupDebts() {
        balanceSheet = DebtSimplifier.simplifyGroupDebts(balanceSheet);
    }
}

class Expense {
    private static int expenseId = 0;
    private final String id;
    private final String description;
    private final double amount;
    private final User paidBy;
    private final SplitType splitType;
    private final String groupId;

    private List<Split> splits = new ArrayList<>();

    public Expense(String description, User paidBy, double amount, SplitType splitType, List<Split> splits,
            String groupId) {
        this.id = "Expense-" + ++expenseId;
        this.description = description;
        this.paidBy = paidBy;
        this.amount = amount;
        this.splitType = splitType;
        this.groupId = groupId;
        this.splits = splits;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<Split> getSplits() {
        return splits;
    }
}

class ExpenseRequest {
    private final String description;
    private final double amount;
    private final List<User> involvedUsers;
    private final User paidBy;
    private final SplitType splitType;
    private final Map<User, Double> splitPairs;

    public ExpenseRequest(Builder builder) {
        description = builder.description;
        amount = builder.amount;
        involvedUsers = builder.involvedUsers;
        paidBy = builder.paidBy;
        splitType = builder.splitType;
        splitPairs = builder.splitPairs;
    }

    public static class Builder {
        private String description;
        private double amount;
        private User paidBy;
        private SplitType splitType;
        private List<User> involvedUsers;
        private Map<User, Double> splitPairs;

        public Builder() {
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setPaidByUser(User paidBy) {
            this.paidBy = paidBy;
            return this;
        }

        public Builder setSplitType(SplitType splitType) {
            this.splitType = splitType;
            return this;
        }

        public Builder setInvolvedUsers(List<User> involvedUsers) {
            this.involvedUsers = involvedUsers;
            return this;
        }

        public Builder setSplitPairs(Map<User, Double> splitPairs) {
            this.splitPairs = splitPairs;
            return this;
        }

        public ExpenseRequest build() {
            return new ExpenseRequest(this);
        }
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public List<User> getInvolvedUsers() {
        return involvedUsers;
    }

    public User getPaidByUser() {
        return paidBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public Map<User, Double> getSplitPairs() {
        return splitPairs;
    }
}

class Split {
    private final User user;
    private final double amount;

    public Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }
}

class SplitwiseService {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();

    // Register a user
    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
        return user;
    }

    // Create a group
    public Group createGroup(String name, List<User> members) {
        Group group = new Group(name);

        for (User user : members) {
            validateUser(user);
            group.addUser(user);
        }

        groups.put(group.getId(), group);
        return group;
    }

    // Add a user to an existing group
    public void addUserToGroup(Group group, User user) {
        validateUser(user);
        group.addUser(user);
    }

    // Add an expense to a group
    public void addExpense(Group group, ExpenseRequest request) {
        group.addGroupExpense(request, request.getInvolvedUsers());
    }

    // Settle payment between two users
    public void settlePayment(
            Group group,
            User fromUser,
            User toUser,
            double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Settlement amount must be positive");
        }

        // Payment from fromUser to toUser
        // reduces what fromUser owes toUser.
        group.settlePayment(toUser, fromUser, -amount);
    }

    // Show a user's balance in a group
    public void showUserGroupBalance(Group group, User user) {
        group.showUserBalanceSheet(user);
    }

    // Show all group balances
    public void showGroupBalance(Group group) {
        group.showGroupBalanceSheet();
    }

    // Simplify group debts
    public void simplifyGroupDebts(Group group) {
        group.simplifyGroupDebts();
    }

    // Get user by ID
    public User getUser(String userId) {
        return users.get(userId);
    }

    // Get group by ID
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    // Get all groups
    public Collection<Group> getAllGroups() {
        return groups.values();
    }

    private void validateUser(User user) {
        if (user == null || !users.containsKey(user.getId())) {
            throw new IllegalArgumentException("User not registered");
        }
    }

}

public class Splitwise {
    public static void main(String[] args) {

        SplitwiseService splitwise = new SplitwiseService();

        // Create users
        User akash = splitwise.createUser("Akash", "akash@gmail.com");
        User abhishek = splitwise.createUser("Abhishek", "abhishek@gmail.com");
        User sachin = splitwise.createUser("Sachin", "sachin@gmail.com");
        User asif = splitwise.createUser("Asif", "asif@gmail.com");

        // Create group
        Group friends = splitwise.createGroup(
                "Friends",
                List.of(akash, abhishek, sachin, asif));

        // Add equal expense
        ExpenseRequest equalExpense = new ExpenseRequest.Builder()
                .setDescription("Dinner")
                .setAmount(900)
                .setPaidByUser(akash)
                .setSplitType(SplitType.EQUAL)
                .setInvolvedUsers(List.of(akash, abhishek, sachin, asif))
                .build();

        splitwise.addExpense(friends, equalExpense);

        // Add percent expense
        ExpenseRequest expense = new ExpenseRequest.Builder()
                .setDescription("Movie Night")
                .setAmount(1200)
                .setPaidByUser(asif)
                .setSplitType(SplitType.PERCENTAGE)
                .setInvolvedUsers(List.of(asif, abhishek, sachin))
                .setSplitPairs(Map.of(asif, 40.00, abhishek, 30.00, sachin, 30.00))
                .build();

        splitwise.addExpense(friends, expense);

        // Show balances
        System.out.println("Balance Sheet:\n");
        splitwise.showGroupBalance(friends);

        // Simplify debts
        System.out.println("Simplified Balance Sheet:\n");
        splitwise.simplifyGroupDebts(friends);
        splitwise.showGroupBalance(friends);

        // Abhishek pays Akash 200
        System.out.println("Abhishek Pays to Akash (90):\n");
        splitwise.settlePayment(friends, abhishek, akash, 90);

        System.out.println("After Settlement:");
        splitwise.showGroupBalance(friends);

        // Simplify debts
        System.out.println("Simplified Balance Sheet After Settlement:\n");
        splitwise.simplifyGroupDebts(friends);
        splitwise.showGroupBalance(friends);
    }
}