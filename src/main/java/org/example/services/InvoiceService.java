package org.example.services;

import org.example.models.Invoice;
import org.example.models.Order;
import org.example.repositories.InvoiceRepository;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceService {

    InvoiceRepository invoiceRepository = new InvoiceRepository();

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.getAllInvoices();
    }

    public Invoice getInvoiceById(Integer invoiceId) {
        return invoiceRepository.getInvoiceById(invoiceId);
    }

    public List<Invoice> getAllInvoicesByCustomerId(Integer customerId) {
        return invoiceRepository.getAllInvoicesByCustomerId(customerId);
    }

    public List<Invoice> getPendingInvoicesByCustomerId(Integer customerId) {
        return invoiceRepository.getAllInvoicesByCustomerId(customerId).stream()
                .filter(invoice -> invoice.getInvoiceStatusId() != 2)
                .toList();
    }
    public List<Invoice> getClosedInvoicesByCustomerId(Integer customerId) {
        return invoiceRepository.getAllInvoicesByCustomerId(customerId).stream()
                .filter(invoice -> invoice.getInvoiceStatusId() == 2)
                .toList();
    }

    public Invoice createInvoice(Order order, BigDecimal price) {
        return invoiceRepository.createInvoice(new Invoice(order.getOrderNo(), order.getCustomerId(), price));
    }

    public Invoice payInvoice(Integer invoiceId) {
        var invoice = getInvoiceById(invoiceId);
        invoice.setInvoiceStatusId(2);

        return invoiceRepository.updateInvoice(invoice);
    }

}
