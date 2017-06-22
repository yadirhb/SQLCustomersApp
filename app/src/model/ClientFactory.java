package model;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Class used to instantiate Customer.
 */
public class ClientFactory {
    /**
     * The max credit amount a client can own.
     */
    private static final int MAX_VL_TOTAL_VALUE = 3000;
    /**
     * Faker instance to create fake information.
     */
    private static Faker faker = new Faker(new Locale("pt-BR"));

    /**
     * Creates fakes client instances.
     *
     * @return Return a valid client instance with fake information.
     */
    public static Customer createClient() {
        return new Customer(faker.number().randomNumber(4, true), faker.idNumber().valid(), faker.name().fullName(), true, (float) faker.number().randomDouble(2, 0, MAX_VL_TOTAL_VALUE));
    }
}
