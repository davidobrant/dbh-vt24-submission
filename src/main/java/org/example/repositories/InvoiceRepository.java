package org.example.repositories;

import org.example.models.Customer;
import org.example.models.Invoice;

import java.util.List;


public class InvoiceRepository extends BaseRepository {

    public InvoiceRepository() {
        super();
    }

    public List<Invoice> getAllInvoices() {
        return findAll(Invoice.class);
    }

    public Invoice getInvoiceById(Integer invoiceId) {
        return findById(Invoice.class, invoiceId);
    }

    public Invoice createInvoice(Invoice invoice) {
        return create(Invoice.class, invoice);
    }

    public Invoice updateInvoice(Invoice invoice) {
        return update(Invoice.class, invoice);
    }

    public List<Invoice> getAllInvoicesByCustomerId(Integer customerId) {
        return findAllByForeignKey(Invoice.class, Customer.class, customerId);
    }
}
