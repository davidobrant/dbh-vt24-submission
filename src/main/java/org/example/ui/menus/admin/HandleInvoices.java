package org.example.ui.menus.admin;

import org.example.models.Invoice;
import org.example.services.InvoiceService;
import org.example.supers.Menu;

import java.util.List;

public class HandleInvoices extends Menu {


    private final InvoiceService invoiceService = new InvoiceService();

    List<Invoice> invoices;
    List<Invoice> pending;
    List<Invoice> closed;

    public void run() {
        super.run();
        this.invoices = invoiceService.getAllInvoices();

        while (running) menu();
    }

    private void menu() {
        pending = invoices.stream().filter(invoice -> invoice.getInvoiceStatusId() == 1).toList();
        closed = invoices.stream().filter(invoice -> invoice.getInvoiceStatusId() == 2).toList();

        p.menu("INVOICES");
        System.out.print(
                "[1] List Pending (" + pending.size() + ")\n" +
                "[2] List History (" + closed.size() + ")\n" +
                "[0] Exit \n"
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "pending" -> listPending(pending);
            case "2", "history" -> listHistory(closed);
            default -> p.invalidCommand();
        }
    }

    private void listPending(List<Invoice> list) {
        p.printH1("PENDING INVOICES");
        p.printEntities(list);
    }

    private void listHistory(List<Invoice> list) {
        p.printH1("CLOSED INVOICES");
        p.printEntities(list);
    }

}
