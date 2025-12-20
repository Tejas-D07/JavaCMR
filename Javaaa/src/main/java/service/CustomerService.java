package service;

import model.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final List<Customer> customers = new ArrayList<>();

    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public static List<Customer> getAllCustomers() {
        return customers;
    }
}
