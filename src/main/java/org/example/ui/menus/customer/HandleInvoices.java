package org.example.ui.menus.customer;

import org.example.models.Customer;
import org.example.models.Invoice;
import org.example.services.InvoiceService;
import org.example.supers.Login;

import java.util.List;

public class HandleInvoices extends Login<Customer> {

    InvoiceService invoiceService = new InvoiceService();

    List<Invoice> pendingInvoices;
    List<Invoice> closedInvoices;

    public void run(Customer currentUser) {
        this.currentUser = currentUser;
        super.run();

        while (running) {
            this.pendingInvoices = invoiceService.getPendingInvoicesByCustomerId(currentUser.getCustomerId());
            this.closedInvoices = invoiceService.getClosedInvoicesByCustomerId(currentUser.getCustomerId());
            menu();
        }
    }

    private void menu() {
        p.menu("MY INVOICES");
        System.out.print(
                "[1] Pay Invoice\n" +
                "[2] Pending (" + pendingInvoices.size() + ")\n" +
                "[3] History (" + closedInvoices.size() + ")\n" +
                "[0] Exit\n"
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "pay" -> payInvoice();
            case "2", "pending" -> p.printEntities(pendingInvoices);
            case "3", "history" -> p.printEntities(closedInvoices);
            default -> p.invalidCommand();
        }
    }

    private void payInvoice() {
        var index = u.printChoosingList(pendingInvoices);
        if (index == null) return;

        try {
            var invoice = invoiceService.payInvoice(pendingInvoices.get(index).getInvoiceId());
            p.printMessage("Invoice paid in full, Thank you for your business!");
        } catch (Exception e) {
            p.printError("Error with payment: " + e.getMessage());
        }

    }
}
