package model;

/**
 * Defines a Customer model to store and access information from DB.
 */
public class Customer {
    /**
     * The id of the customer.
     */
    private long id;
    /**
     * The customer dni.
     */
    private String dni;

    /**
     * The customer full name.
     */
    private String fullName;

    /**
     * The customer state.
     */
    private boolean isActive;

    /**
     * The customer credit amount.
     */
    private float creditAmount;

    public Customer(long id, String dni, String fullName, boolean isActive, float amount) {
        this.id = id;
        this.dni = dni;
        this.fullName = fullName;
        this.isActive = isActive;
        this.creditAmount = amount;
    }

    /**
     * Gets the customer's id.
     *
     * @return the customer's id.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the customer's DNI.
     *
     * @return the customer's DNI.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Gets the customer full name.
     *
     * @return the customer full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the customer active state.
     *
     * @return the customer active state.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the customer credit amount.
     *
     * @return the customer credit amount.
     */
    public float getCreditAmount() {
        return creditAmount;
    }
}
