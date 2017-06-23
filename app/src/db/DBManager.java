package db;

import model.Customer;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to manage the database operations.
 */
public class DBManager {
    /**
     * The name of the data base.
     */
    private static final String DB_NAME = "java_app_db";

    /**
     * The driver to use over SQL connections.
     */
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    /**
     * The MySQL database host information.
     */
    private static final String DB_HOST = "jdbc:mysql://localhost:3306/";
    /**
     * The user name used as valid database credential.
     */
    public static String USER_NAME = "root";
    ;
    /**
     * The password used as valid database credential.
     */
    public static String PASSWORD = "";
    /**
     * Defines the default table name. Modify this attribute's value before execute getInstance() call to change the
     * table name.
     */
    public static String TABLE_NAME = "tb_customer_account";
    /**
     * The mysql select statement.
     */
    private static final String SELECT_FILTER_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE vl_total > %s AND id_customer BETWEEN %s AND %s ORDER BY vl_total DESC";
    /**
     * The mysql insert statement.
     */
    private static final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " (id_customer, cpf_cnpj, nm_customer, is_active, vl_total) VALUES (?,?,?,?,?)";
    /**
     * Singleton instance of the DBManager class.
     */
    private static DBManager instance = null;

    /**
     * The connection instance.
     */
    private Connection con;

    private DBManager() {
        // Execute the initialize routine.
        try {
            Class.forName(DRIVER);
            getConnection();
        } catch (SQLException e) {
            createDatabase();
            defineTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of the DBManager class.
     *
     * @return The singleton instance of the DBManager class.
     */
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * Gets a connection instance.
     *
     * @return The current connection instance.
     * @throws SQLException If there's a problem during the connection opening.
     */
    private Connection getConnection() throws SQLException {
        return con = DriverManager.getConnection(DB_HOST + DB_NAME, USER_NAME, PASSWORD);
    }

    /**
     * Create the default database.
     */
    private void createDatabase() {
        try {
            // Open a connection
            con = DriverManager.getConnection(DB_HOST, USER_NAME, PASSWORD);
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a query with no result expected.
     *
     * @param query The query to execute.
     */
    public void executeQuery(String query) {
        try {
            // Get the connection
            getConnection();

            // Create the statement
            Statement statement = con.createStatement();

            //The next line has the issue
            statement.executeUpdate(query);
            con.close();
        } catch (SQLException e) {
            System.out.println("An error has occurred on query execution");
        }
    }

    /**
     * Creates the default table.
     */
    public void defineTable() {
        String CREATE_TABLE_QUERY = "CREATE OR REPLACE TABLE " + TABLE_NAME + " ("
                + "id INT(64) NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + "id_customer BIGINT NOT NULL,"
                + "cpf_cnpj VARCHAR(12),"
                + "nm_customer VARCHAR(50),"
                + "is_active BOOLEAN DEFAULT TRUE,"
                + "vl_total FLOAT(10,4))";

        // Executes the query
        executeQuery(CREATE_TABLE_QUERY);
    }

    /**
     * Method used internally to store the customer instances.
     *
     * @param customer The customer instance to store.
     */
    private void internalInsertClient(Customer customer) {
        if (customer != null) {
            try {
                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt;
                preparedStmt = con.prepareStatement(INSERT_QUERY);
                preparedStmt.setLong(1, customer.getId());
                preparedStmt.setString(2, customer.getDni());
                preparedStmt.setString(3, customer.getFullName());
                preparedStmt.setBoolean(4, customer.isActive());
                preparedStmt.setFloat(5, customer.getCreditAmount());

                // execute the preparedstatement
                preparedStmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserts a client instance into the data base.
     *
     * @param customers The customers instances to store.
     */
    public void insertClient(Customer... customers) {
        if (customers != null && customers.length > 0) {
            try {
                // Open the connection
                getConnection();

                // Store all instances each by one.
                for (Customer c : customers) {
                    internalInsertClient(c);
                }
                // Close the connection
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Gets the clients stored in data base which data matches with the supplied conditions:
     * The vl_total value > totalLimit and the id value is between minId and maxId values. The result will be DESC
     * ordered by the vl_total column.
     *
     * @param totalLimit The minimum credit value the clients must posses.
     * @param minId      The min id range.
     * @param maxId      The max id range.
     * @return The clients results.
     */
    public List<Customer> getClients(int totalLimit, int minId, int maxId) {
        // Create the client list
        List<Customer> customers = new LinkedList<>();
        try {
            // Open the connection
            getConnection();
            // create the java statement
            Statement st = con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(String.format(SELECT_FILTER_QUERY, totalLimit, minId, maxId));

            // iterate through the java resultset
            while (rs.next()) {
                long id = rs.getLong("id_customer");
                String fullName = rs.getString("nm_customer");
                String dni = rs.getString("cpf_cnpj");
                boolean isActive = rs.getBoolean("is_active");
                float vlTotal = rs.getFloat("vl_total");

                // Add the record to the client list.
                customers.add(new Customer(id, dni, fullName, isActive, vlTotal));
            }

            // Close the statement
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
}