package org.example.s3;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class FileHandler {

    public static File selectFileFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null || listOfFiles.length == 0) {
            System.out.println("No files found in the directory.");
            return null;
        }

        System.out.println("Select a file by entering its number:");
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println(i + 1 + ". " + listOfFiles[i].getName());
            }
        }

        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("Enter your choice (number between 1 and " + listOfFiles.length + "):");
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a number! Please enter a number:");
                scanner.next(); // this is important!
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > listOfFiles.length);

        // Adjust choice to match array indexing
        return listOfFiles[choice - 1];
    }

    public static File selectFileFromFolderAuto(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null || listOfFiles.length == 0) {
            System.out.println("No files found in the directory.");
            return null;
        }
        return listOfFiles[new Random().nextInt(0, listOfFiles.length)];
    }
}
