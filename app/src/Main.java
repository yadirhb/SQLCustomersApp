import db.DBManager;
import model.Customer;
import model.ClientFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This class is the entry point of the application.
 */
public class Main {

    /**
     * The singleton instance of DBManager.
     */
    private static DBManager myDBManager;

    /**
     * This method is executed externally by the SO to inject the arguments through CLI.
     *
     * @param args The CLI arguments.
     */
    public static void main(String[] args) {
        // Instantiate the DBManager
        myDBManager = DBManager.getInstance();

        // Gets the clients insertion
        insertClients();

        // Request the filter (seja maior que 560 e o campo id_customer  esteja entre 1500 e 2700)
        Customer[] filtered = filterClients().toArray(new Customer[0]);

        // Print the average
        printAverage(filtered);

        // Print the client list.
        printClients(filtered);
        try {
            // Await to end the program.
            System.out.println("Press ENTER key to end the program!");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printClients(Customer[] filtered) {
        // If there's clients then print a table
        if (filtered.length > 0) {
            String leftAlignFormat = "| %-30s | %-10s | %-10s | %-6s |";
            System.out.println("+--------------------------------+-------------+------------+--------+");
            System.out.println("| Nome                           | CPF/CNPJ    | VL Total   | Active |");
            System.out.println("+--------------------------------+-------------+------------+--------+");
            for (Customer customer : filtered) {
                System.out.println(String.format(leftAlignFormat, customer.getFullName(), customer.getDni(), customer.getCreditAmount(), customer.isActive()));
            }
            System.out.println("+--------------------------------+-------------+------------+--------+");
        }
    }

    /**
     * Prints the average after filter the clients.
     *
     * @param filtered
     */
    private static void printAverage(Customer[] filtered) {
        float sum = 0;
        for (Customer customer : filtered) {
            sum += customer.getCreditAmount();
        }
        System.out.println(String.format("The average value among the selected clients is: %s", sum / filtered.length));
    }

    /**
     * Filters the clients with credit above 560 and id between 1500 and 2700.
     *
     * @return The filtered clients.
     */
    private static List<Customer> filterClients() {
        // Filters the clients with credit above 560 and id between 1500 and 2700
        return myDBManager.getClients(560, 1500, 2700);
    }

    /**
     * Creates the database table and inserts the N client records.
     */
    private static void insertClients() {
        System.out.println("Welcome!!!\nbefore continue define the amount of clients you would like to create (0 - N):");

        try {
            InputStreamReader read = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(read);
            int clientAmount = Integer.parseInt(in.readLine());

            while (clientAmount < 0) {
                System.out.println("Please the amount of customers should be (0 - N):");
                clientAmount = Integer.parseInt(in.readLine());
            }

            // Create the table
            myDBManager.defineTable();


            // Inserts N Customer instances into the database.
            Customer[] customers = new Customer[clientAmount];
            for (int i = 0; i < clientAmount; i++) {
                customers[i] = ClientFactory.createClient();
            }

            // Insert the customers instances.
            myDBManager.insertClient(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
