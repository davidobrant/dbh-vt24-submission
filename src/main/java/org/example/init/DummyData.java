package org.example.init;

import org.example.models.*;
import org.example.repositories.BaseRepository;
import org.example.s3.S3Repository;
import org.example.services.OrderService;
import org.example.utils.Utilities;

import java.math.BigDecimal;
import java.util.*;

public class DummyData extends BaseRepository {

    Random random;

    private final int numberOfCustomers = 12;
    private final int numberOfEmployees = 5;

    private final HashMap<String, BigDecimal> orderTypes = new HashMap<>(){
        {
            put("Screen", u.bigDecimal(499.0));
            put("Battery", u.bigDecimal(699.0));
            put("Microphone", u.bigDecimal(399.0));
            put("Speakers", u.bigDecimal(499.0));
            put("Shell", u.bigDecimal(299.0));
            put("Other", u.bigDecimal(499.0));
        }
    };
    private final String[] orderStatuses = new String[]{"Open", "In progress", "Closed"};
    private final String[] invoiceStatuses = new String[]{"Pending", "Closed"};

    List<OrderType> createdOrderTypes;
    List<OrderStatus> createdOrderStatuses;
    List<InvoiceStatus> createdInvoiceStatuses;
    List<Employee> createdEmployees;
    List<Customer> createdCustomers;
    List<Product> createdProducts;
    List<Order> createdOrders;
    List<Order> assignedOrders;
    List<OrderImage> addedImages = new ArrayList<>();
    List<Invoice> addedInvoices;

    public DummyData() {
        super();
        run();
    }

    public void run() {
        System.out.println("Running scripts...");
        createdOrderTypes = createOrderTypes(orderTypes);
        createdOrderStatuses = createOrderStatuses(orderStatuses);
        createdInvoiceStatuses = createInvoiceStatuses(invoiceStatuses);
        createdEmployees = createEmployees(numberOfEmployees);
        createdCustomers = createCustomers(numberOfCustomers);
        createdProducts = createProductsForCustomers(createdCustomers);
        createdOrders = createOrdersForProducts(createdProducts);
        assignedOrders = assignOrdersRandom(createdOrders, createdEmployees);
        addedInvoices = closeOrdersRandom(assignedOrders);
        System.out.println("CREATED");
    }

    /* ----- CUSTOMERS ----- */
    private List<Customer> createCustomers(int amount) {
        var list = new ArrayList<Customer>();
        for (Customer customer : generateCustomers(amount)) {
            var createdCustomer = create(Customer.class, customer);
            list.add(createdCustomer);
        }
        return list;
    }
    private List<Customer> generateCustomers(int amount) {
        List<Customer> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(createCustomer(generateIdentifierByIndex(i)));
        }
        return list;
    }
    private Customer createCustomer(String identifier) {
        var secondIdentifier = generateRandomIdentifier();
        return new Customer(identifier + " " + secondIdentifier, generateEmail(identifier + secondIdentifier, "customer.com"), generatePhone(), generateAddress(identifier));
    }
    /* --x-- CUSTOMERS --x-- */
    /* ----- EMPLOYEES ----- */
    private List<Employee> createEmployees(int amount) {
        var list = new ArrayList<Employee>();
        for (Employee employee : generateEmployees(amount)) {
            list.add(create(Employee.class, employee));
        }
        return list;
    }
    private List<Employee> generateEmployees(int amount) {
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(createEmployee(generateIdentifierByIndex(i)));
        }
        return list;
    }
    private Employee createEmployee(String identifier) {
        var secondIdentifier = generateRandomIdentifier();
        return new Employee(identifier + " " + secondIdentifier, generateEmail(identifier + secondIdentifier, "employee.com"), generatePhone());
    }
    /* --x-- EMPLOYEES --x-- */
    /* ----- PRODUCTS ----- */
    private List<Product> createProductsForCustomers(List<Customer> customers) {
        List<Product> list = new ArrayList<>();
        for (var customer : customers) {
            list.add(create(Product.class, createProduct(customer.getCustomerId())));
            if (random.nextBoolean() && random.nextBoolean()) {
                list.add(create(Product.class, createProduct(customer.getCustomerId())));
            }
        }
        return list;
    }
    private Product createProduct(Integer customerId) {
        var brand = generateIdentifierByIndex(random.nextInt(0,25));
        var model = generateRandomLetters(2) + "-" + random.nextInt(10,100) * 100;
        var productionYear = random.nextInt(1990, 2024);
        return new Product(brand, model, productionYear, customerId);
    }
    /* --x-- PRODUCTS --x-- */
    /* ----- ORDERS ----- */
    private List<Order> createOrdersForProducts(List<Product> createdProducts) {
        List<Order> list = new ArrayList<>();
        var s3Repo = new S3Repository();
        random = new Random();

        for (var product : createdProducts) {
            var order = create(Order.class, createOrder(product.getCustomerId(), product.getProductId()));
            if (random.nextBoolean() || random.nextBoolean()) {
                addedImages.add(addImageToOrder(order, s3Repo));
                if (random.nextBoolean()) {
                    addedImages.add(addImageToOrder(order, s3Repo));
                }
                order.setHasAttachments(true);
                update(Order.class, order);
            }
            list.add(order);
        }
        return list;
    }
    private Order createOrder(Integer customerId, Integer productId) {
        random = new Random();
        var orderTypeId = createdOrderTypes.get(random.nextInt(createdOrderTypes.size())).getOrderTypeId();
        var description = u.generateText(random.nextInt(8,12));
        return new Order(customerId, productId, orderTypeId, description);
    }
    /* --x-- ORDERS --x-- */
    /* ----- ORDER TYPES ----- */
    private OrderType createOrderType(Map.Entry<String, BigDecimal> entry) {
        var orderTypeName = entry.getKey();
        var price = entry.getValue();
        return new OrderType(orderTypeName, price);
    }
    private List<OrderType> createOrderTypes(HashMap<String, BigDecimal> orderTypes) {
        List<OrderType> list = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : orderTypes.entrySet()) {
            list.add(create(OrderType.class, createOrderType(entry)));
        }
        return list;
    }
    /* --x-- ORDER STATUSES --x-- */
    /* ----- ORDER STATUSES ----- */
    private List<OrderStatus> createOrderStatuses(String[] orderStatuses) {
        List<OrderStatus> list = new ArrayList<>();
        for (var orderStatus : orderStatuses) {
            list.add(create(OrderStatus.class, createOrderStatus(orderStatus)));
        }
        return list;
    }
    private OrderStatus createOrderStatus(String orderStatus) {
        return new OrderStatus(orderStatus);
    }
    /* --x-- ORDER STATUSES --x-- */
    /* ----- INVOICE STATUSES ----- */
    private List<InvoiceStatus> createInvoiceStatuses(String[] invoiceStatuses) {
        List<InvoiceStatus> list = new ArrayList<>();
        for (var invoiceStatus : invoiceStatuses) {
            list.add(create(InvoiceStatus.class, createInvoiceStatus(invoiceStatus)));
        }
        return list;
    }
    private InvoiceStatus createInvoiceStatus(String invoiceStatus) {
        return new InvoiceStatus(invoiceStatus);
    }
    /* --x-- INVOICE STATUSES --x-- */
    /* ----- ASSIGN ORDERS ----- */
    private List<Order> assignOrdersRandom(List<Order> orders, List<Employee> employees) {
        List<Order> list = new ArrayList<>();
        random = new Random();

        for (var order : orders) {
            if (random.nextBoolean()) {
                order.setEmployeeId(random.nextInt(0, employees.size()) + 1);
                order.setOrderStatusId(2);
                list.add(update(Order.class, order));
            }
        }

        return list;
    }
    private OrderImage addImageToOrder(Order order, S3Repository s3Repo) {
        var s3Key = s3Repo.uploadAuto(order);
        return create(OrderImage.class, new OrderImage(order.getOrderId(), s3Key));
    }
    /* --x-- ASSIGN ORDERS --x-- */
    /* ----- CLOSE ORDERS ----- */
    private List<Invoice> closeOrdersRandom(List<Order> orders) {
        OrderService orderService = new OrderService();
        List<Invoice> list = new ArrayList<>();
        for (var order : orders) {
            if (random.nextBoolean()) {
                list.add(orderService.closeOrder(order));
            }
        }
        return list;
    }
    /* --x-- CLOSE ORDERS --x-- */
    /* ----- MISC UTILS ----- */
    private String generateIdentifierByIndex(int index) {
        if (index < 0 || index > 25) {
            throw new IllegalArgumentException("Index must be between 0 and 25");
        }
        char c = (char) ('a' + index);
        return String.valueOf(c).toUpperCase() + c + c;
    }
    private String generateRandomIdentifier() {
        random = new Random();
        return generateIdentifierByIndex(random.nextInt(0,25));
    }
    private String generateRandomLetters(int amount) {
        StringBuilder letters = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            int index = random.nextInt(0,25);
            char c = (char) ('A' + index);
            letters.append(c);
        }
        return letters.toString();
    }
    private String generateEmail(String identifier, String domain) {
        return identifier.toLowerCase() + "@" + domain;
    }
    private String generatePhone() {
        random = new Random();
        StringBuilder phone = new StringBuilder();

        phone.append("07");

        for (int i = 0; i < 8; i++) {
            phone.append(random.nextInt(10));
        }

        return phone.toString();
    }
    private String generateAddress(String identifier) {
        random = new Random();
        return identifier.charAt(0) + "'s Street " + random.nextInt(0,100);
    }
    /* --x-- MISC UTILS --x-- */

}
