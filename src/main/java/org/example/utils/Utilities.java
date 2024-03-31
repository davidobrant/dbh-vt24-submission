package org.example.utils;

import de.svenjacobs.loremipsum.LoremIpsum;
import org.example.annotations.*;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class Utilities {

    private final LoremIpsum lorem = new LoremIpsum();
    private final Printer p = new Printer();
    Random random;

    public String camelToSnakeCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return input.replaceAll(regex, replacement).toLowerCase();
    }

    public String snakeToCamelCase(String input) {
        String[] words = input.split("_");
        StringBuilder result = new StringBuilder();

        result.append(words[0]);

        for (int i = 1; i < words.length; i++) {
            result.append(words[i].substring(0, 1).toUpperCase());
            result.append(words[i].substring(1));
        }

        return result.toString();
    }

    /* ----- Annotations ----- */
    public String getPrimaryKey(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return camelToSnakeCase(field.getName());
            }
        }
        throw new RuntimeException("No primary key field found in entity class: " + entityClass.getName());
    }

    public String getIdentityFieldName(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IdentityNo.class)) {
                return camelToSnakeCase(field.getName());
            }
        }
        throw new RuntimeException("No identityNo key field found in entity class: " + entityClass.getName());
    }

    public String getIdentifier(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IdentityNo.class)) {
                return field.getAnnotation(IdentityNo.class).identifier();
            }
        }
        throw new RuntimeException("No identityNo key field found in entity class: " + entityClass.getName());
    }

    public String getForeignKey(Class<?> entityClass, String foreignKeyName) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ForeignKey.class)) {
                return camelToSnakeCase(field.getName());
            }
        }
        throw new RuntimeException("No foreign key field found in entity class: " + entityClass.getName());
    }

    public String[] getReferencedTables(Class<?> entityClass) {
        Referenced annotation = entityClass.getAnnotation(Referenced.class);
        if (annotation != null) {
            return annotation.tables();
        } else {
            throw new RuntimeException("No table name annotation found in class: " + entityClass.getName());
        }
    }

    public String getTableName(Class<?> entityClass) {
        Table tableNameAnnotation = entityClass.getAnnotation(Table.class);
        if (tableNameAnnotation != null) {
            return tableNameAnnotation.name();
        } else {
            throw new RuntimeException("No table name annotation found in class: " + entityClass.getName());
        }
    }
    /* --x-- Annotations --x-- */

    public <T> Integer printChoosingList(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + list.get(i));
        }
        System.out.println("[0] Exit");

        try {
            var cmd = p.promptCommand();
            if (cmd.equals("0") || cmd.equals("exit")) return null;
            return Integer.parseInt(cmd) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and " + list.size() + ". (Exit 0)");
            printChoosingList(list);
        }
        return null;
    }

    public String getFieldName(Field field) {
        return camelToSnakeCase(field.getName());
    }

    public String generateText(Integer amountOfWords) {
        return lorem.getWords(amountOfWords);
    }

    public BigDecimal bigDecimal(Double n) {
        return BigDecimal.valueOf(n);
    }

    public String generateIdentityNo(Integer identifier) {
        return identifier.toString() + new Random().nextInt(100000,999999);
    }

    public <T> String generateIdentityNo(Class<T> entityClass) {
        random = new Random();
        var identifier = getIdentifier(entityClass);
        var prefix = identifier + "000";
        var unique = random.nextInt(1000, 9999);
        return prefix + unique;
    }

    public void deleteLocalFolder(File folder) {
        if (!folder.exists()) return;
        System.out.print("Deleting folder... ");
        emptyLocalFolder(folder);
        if (folder.delete()) {
            p.printMessage("SUCCESS");
        } else {
            if (folder.exists()) {
                folder.deleteOnExit();
                return;
            }
            p.printMessage("FAILED");
        }
    }

    public void emptyLocalFolder(File folder) {
        if (folder == null || !folder.exists()) {
            return;
        }

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteLocalFolder(file);
                } else {
                    file.delete();
                }
                file.deleteOnExit();
            }
        }
    }

    public void createFolderIfNotExists(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
