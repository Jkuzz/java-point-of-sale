package cz.cuni.mff.java.projects.posapp.plugins.payment;

import cz.cuni.mff.java.projects.posapp.database.Database;
import cz.cuni.mff.java.projects.posapp.database.DevUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;


/**
 * Singleton connector for the Payment module. Provides database saving functionality for
 * payment and transaction saving.
 */
public class DatabaseConnector {

    private final Database db;

    /**
     * Obtains the database connection.
     */
    public DatabaseConnector() {
        this.db = Database.getInstance(new PaymentClient());
    }

    /**
     * Get the DatabaseConnector Singleton instance. Create if not exists.
     * @return the singleton
     */
    public static DatabaseConnector getInstance() {
        if(instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    /**
     * The Singleton instance.
     */
    private static DatabaseConnector instance;


    /**
     * Save the payment for the selected tab to the database.
     * Saves into `transaction` the transaction data
     * and into `transaction_products` the binding about how many products were present in the tab.
     * @param tabToSave tab whose items to save as a transaction
     * @return if save was successful
     */
    public boolean savePayment(Tab tabToSave) {
        if(tabToSave.getTabItems().size() == 0) {  // Don't save transaction if tab is empty
            return true;
        }
        LocalDateTime timeOpened = tabToSave.getTimeCreated();
        Timestamp dateOpened = new Timestamp(timeOpened.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        String query = "INSERT INTO `transactions` (`id`, `price`, `time_opened`, `time_closed`) VALUES (NULL, ?, ?, NULL);";

        try {
            ResultSet rs = db.query("""
                SELECT AUTO_INCREMENT
                FROM information_schema.tables
                WHERE table_name = 'transactions'""");
            rs.next();
            int nextId = rs.getInt(1);

            PreparedStatement stmt = db.prepareStatement(query);
            stmt.setFloat(1, tabToSave.getTotalCost());
            stmt.setTimestamp(2, dateOpened);

            stmt.execute();
            String transactionItemsQuery = getTransactionItemsQuery(tabToSave.getTabItems(), nextId);
            db.query(transactionItemsQuery);

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Create an insert statement of the tab's items. Chains all items.
     * Creates (`transaction_id`, `amount`, `product_id`) objects for each tabItem.
     * @param tabItems list of the tab's items
     * @param transactionId id of the new transaction.
     * @return  the insert query
     */
    private String getTransactionItemsQuery(ArrayList<TabItem> tabItems, int transactionId) {
        StringBuilder query = new StringBuilder(
                "INSERT INTO `transaction_products` (`transaction_id`, `amount`, `product_id`) VALUES ");
        ArrayList<String> sqlTabItems = new ArrayList<>();
        tabItems.forEach(tabItem -> sqlTabItems.add("(" +
                transactionId + ", " +
                tabItem.getAmount() + ", " +
                tabItem.getProductId() + ")")
        );

        query.append(String.join(", ", sqlTabItems))
                .append(";");
        return query.toString();
    }
}
