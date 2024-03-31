package org.example.supers;

public class Menu extends Utils {

    protected boolean running;

    public Menu() {
        this.running = true;
    }

    public void exit() {
        this.running = false;
    }

    public void run() { this.running = true; }
}
