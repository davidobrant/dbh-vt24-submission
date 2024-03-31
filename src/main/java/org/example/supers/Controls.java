package org.example.supers;

public class Controls {

    private boolean run;

    public Controls() {
        this.run = true;
    }

    public boolean isRunning() {
        return run;
    }

    public void exit() {
        this.run = false;
    }

}
