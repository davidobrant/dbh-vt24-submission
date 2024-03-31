package org.example.ui.menus;

import org.example.init.Setup;
import org.example.s3.S3Client;
import org.example.s3.S3Repository;
import org.example.services.SettingsService;
import org.example.supers.Controls;
import org.example.supers.Menu;

import java.io.File;
import java.sql.SQLException;

public class SettingsMenu extends Menu {

    Controls controls;
    private final SettingsService settingsService = new SettingsService();

    public void run() {
        controls = new Controls();
        while (controls.isRunning()) {
            settingsMenu();
        }
    }

    private void settingsMenu() {
        p.menu("SETTINGS MENU");
        System.out.print("""
            [1] Setup
            [2] Populate with Dummy Data
            [3] Print Tables
            [4] Print S3 Bucket
            [x] Drop Tables
            [y] Empty S3 bucket
            [z] Empty Downloads Folder
            [0] Exit
            """);
        handleSettingsMenu(p.promptCommand());
    }

    private void handleSettingsMenu(String cmd) {
        switch (cmd) {
            case "0", "exit"  -> controls.exit();
            case "1", "setup" -> createTables();
            case "2", "dummy" -> dummyData();
            case "3", "tables" -> printTables();
            case "4", "printBucket" -> printBucket();
            case "x", "drop" -> drop();
            case "y", "empty" -> emptyBucket();
            case "z", "downloads" -> emptyDownloads();
            default -> p.invalidCommand();
        }
    }

    private void drop() {
        settingsService.dropAllTables();
    }

    private void createTables() {
        new Setup();
    }

    private void dummyData() {
        new Setup();
        settingsService.createDummyData();
    }

    private void printTables() {
        var entities = settingsService.getEverything();
        if (entities.isEmpty()) {
            p.printError("Nothing in DB...");
            return;
        }
        p.printH1("ALL Entities");
        p.printEntities(entities);
    }

    private void printBucket() {
        p.printH1("S3 Files");
        new S3Repository().listBucket();
    }

    private void emptyBucket() {
        System.out.print("Emptying S3 Bucket... ");
        new S3Client().emptyBucket();
        p.printMessage("DONE");
    }

    private void emptyDownloads() {
        System.out.print("Emptying Downloads... ");
        File DOWNLOADS = new File(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "downloads");
        u.emptyLocalFolder(DOWNLOADS);
        u.createFolderIfNotExists(DOWNLOADS);
        p.printMessage("DONE");
    }
}
