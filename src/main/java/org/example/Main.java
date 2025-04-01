package org.example;

import java.io.*;
import java.util.*;

public class Main {
    private static final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final int ALPHABET_SIZE = ALPHABET.length();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите режим работы:");
        System.out.println("1. Шифровка текста");
        System.out.println("2. Расшифровка текста с помощью ключа");
        System.out.println("3. Расшифровка текста методом brute force");
        System.out.println("4. Расшифровка текста методом статистического анализа");

        int mode = scanner.nextInt();
        scanner.nextLine();

        switch (mode) {
            case 1:
                encryptText(scanner);
                break;
            case 2:
                decryptWithKey(scanner);
                break;
            case 3:
                decryptBruteForce(scanner);
                break;
            case 4:
                decryptStatisticalAnalysis(scanner);
                break;
            default:
                System.out.println("Неверный режим работы.");
        }
    }

    private static void encryptText(Scanner scanner) {
        System.out.println("Введите путь к файлу с оригинальным текстом:");
        String inputFile = scanner.nextLine();
        System.out.println("Введите путь к файлу для записи зашифрованного текста:");
        String outputFile = scanner.nextLine();
        System.out.println("Введите ключ шифрования (сдвиг):");
        int key = scanner.nextInt();

        try {
            String text = readFile(inputFile);
            String encryptedText = encrypt(text, key);
            writeFile(outputFile, encryptedText);
            System.out.println("Текст успешно зашифрован.");
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }

    private static void decryptWithKey(Scanner scanner) {
        System.out.println("Введите путь к зашифрованному файлу:");
        String inputFile = scanner.nextLine();
        System.out.println("Введите путь к файлу для записи расшифрованного текста:");
        String outputFile = scanner.nextLine();
        System.out.println("Введите ключ шифрования (сдвиг):");
        int key = scanner.nextInt();

        try {
            String text = readFile(inputFile);
            String decryptedText = decrypt(text, key);
            writeFile(outputFile, decryptedText);
            System.out.println("Текст успешно расшифрован.");
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }

    private static void decryptBruteForce(Scanner scanner) {
        System.out.println("Введите путь к зашифрованному файлу:");
        String inputFile = scanner.nextLine();
        System.out.println("Введите путь к файлу для записи расшифрованного текста:");
        String outputFile = scanner.nextLine();

        try {
            String text = readFile(inputFile);
            for (int key = 0; key < ALPHABET_SIZE; key++) {
                String decryptedText = decrypt(text, key);
                if (isMeaningfulText(decryptedText)) {
                    writeFile(outputFile, decryptedText);
                    System.out.println("Текст успешно расшифрован с ключом: " + key);
                    return;
                }
            }
            System.out.println("Не удалось расшифровать текст.");
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }

    private static void decryptStatisticalAnalysis(Scanner scanner) {
        System.out.println("Введите путь к зашифрованному файлу:");
        String inputFile = scanner.nextLine();
        System.out.println("Введите путь к файлу для записи расшифрованного текста:");
        String outputFile = scanner.nextLine();

        try {
            String text = readFile(inputFile);
            int key = findKeyByStatisticalAnalysis(text);
            String decryptedText = decrypt(text, key);
            writeFile(outputFile, decryptedText);
            System.out.println("Текст успешно расшифрован с ключом: " + key);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }

    private static String encrypt(String text, int key) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                int newIndex = (index + key) % ALPHABET_SIZE;
                encryptedText.append(ALPHABET.charAt(newIndex));
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    private static String decrypt(String text, int key) {
        return encrypt(text, ALPHABET_SIZE - key);
    }

    private static boolean isMeaningfulText(String text) {
        return text.contains(" ") && text.length() > 10;
    }

    private static int findKeyByStatisticalAnalysis(String text) {
        int[] frequencies = new int[ALPHABET_SIZE];
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                frequencies[index]++;
            }
        }

        int maxFreqIndex = 0;
        for (int i = 1; i < frequencies.length; i++) {
            if (frequencies[i] > frequencies[maxFreqIndex]) {
                maxFreqIndex = i;
            }
        }

        int spaceIndex = ALPHABET.indexOf(' ');
        return (maxFreqIndex - spaceIndex + ALPHABET_SIZE) % ALPHABET_SIZE;
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        }
        return text.toString();
    }

    private static void writeFile(String filePath, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(text);
        }
    }
}