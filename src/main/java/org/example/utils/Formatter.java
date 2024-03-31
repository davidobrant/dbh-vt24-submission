package org.example.utils;

public class Formatter {

    public String prompt(String value) {
        return "\n> Enter " + value + ": ";
    }

    public String menu(String title) {
        return "\n..... " + title + " .....";
    }
    public String subMenu(String title) {
        return "\n..... " + title + " .....";
    }


    public String h1(String heading) {
        return "\n----- " + heading + " -----";
    }
    public String h2(String heading) {
        return "\n--- " + heading + " ---";
    }
    public String h3(String heading) {
        return "\n- " + heading + " -";
    }

    public String parentheses(String value) {
        return " (" + value + ")";
    }
}
