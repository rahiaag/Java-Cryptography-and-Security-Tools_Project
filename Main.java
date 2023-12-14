import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    private static final String EXPECTED_PASSWORD = "java";
    private static final int MAX_ATTEMPTS = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (checkPassword(scanner)) {
            while (true) {
                System.out.println("\n");
                
                System.out.println("1. Modified Caesar Cipher ");
                System.out.println("2. Huffman Text File Compression ");
                System.out.println("3. Password Strength Checker ");
                System.out.println("4. Exit");
                System.out.println("\n");
                System.out.println("Choose operation: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        runCustomAlphabetCaesarCipher();

                        break;
                    case 2:
                        runHuffmanCompression();
                        break;
                    case 3:
                        runPasswordStrengthCheck();
                        break;
                    case 4:
                        exit();
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }
        scanner.close();
    }

    private static boolean checkPassword(Scanner scanner) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {

            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();
            if (enteredPassword.equals(EXPECTED_PASSWORD)) {
                return true; // Correct password, exit the loop
            } else {
                attempts++;
                System.out.println("Incorrect password. Attempts left: " + (MAX_ATTEMPTS - attempts));
            }
            if (attempts >= MAX_ATTEMPTS) {
                restartComputer();
            }
        }

        return false; // Max attempts reached
    }

    private static void runHuffmanCompression() {
        String inputFileName = "input.txt";
        String outputFileName = "compressed.txt";

        try {
            FileInputStream fileInputStream = new FileInputStream(inputFileName);
            int[] frequencies = new int[256]; // Assuming ASCII characters

            int data;
            while ((data = fileInputStream.read()) != -1) {
                frequencies[data]++;
            }
            fileInputStream.close();

            PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
            for (char c = 0; c < 256; c++) {
                if (frequencies[c] > 0) {
                    priorityQueue.add(new HuffmanNode(c, frequencies[c]));
                }
            }

            while (priorityQueue.size() > 1) {
                HuffmanNode left = priorityQueue.poll();
                HuffmanNode right = priorityQueue.poll();
                HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency);
                parent.left = left;
                parent.right = right;
                priorityQueue.add(parent);
            }

            HuffmanNode root = priorityQueue.poll();
            Map<Character, String> huffmanCodes = new HashMap<>();
            generateHuffmanCodes(root, "", huffmanCodes);

            FileInputStream fileInputStream2 = new FileInputStream(inputFileName);
            BitOutputStream bitOutputStream = new BitOutputStream(outputFileName);

            int data2;
            while ((data2 = fileInputStream2.read()) != -1) {
                String code = huffmanCodes.get((char) data2);
                for (char bit : code.toCharArray()) {
                    bitOutputStream.writeBit(bit == '1');
                }
            }

            bitOutputStream.close();
            fileInputStream2.close();

            System.out.println("Compression completed. Compressed file: " + outputFileName);
            pause();
            clearScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateHuffmanCodes(HuffmanNode node, String currentCode,
            Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }
        if (node.character != '\0') {
            huffmanCodes.put(node.character, currentCode);
        }
        generateHuffmanCodes(node.left, currentCode + "0", huffmanCodes);
        generateHuffmanCodes(node.right, currentCode + "1", huffmanCodes);
    }

    static class HuffmanNode implements Comparable<HuffmanNode> {
        char character;
        int frequency;
        HuffmanNode left, right;

        public HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }

    static class BitOutputStream {
        private FileOutputStream outputStream;
        private int currentByte;
        private int bitCount;

        public BitOutputStream(String fileName) throws IOException {
            outputStream = new FileOutputStream(fileName);
            currentByte = 0;
            bitCount = 0;
        }

        public void writeBit(boolean bit) throws IOException {
            if (bitCount == 8) {
                outputStream.write(currentByte);
                currentByte = 0;
                bitCount = 0;
            }
            currentByte = (currentByte << 1) | (bit ? 1 : 0);
            bitCount++;
        }

        public void close() throws IOException {
            if (bitCount > 0) {
                currentByte <<= (8 - bitCount);
                outputStream.write(currentByte);
            }
            outputStream.close();
        }
    }

    private static void runCustomAlphabetCaesarCipher() {
        String var1 = "ZYXWVUTSRQPONMLKJIHGFEDCBA";
        byte var2 = 3;
        CustomAlphabetCaesarCipher var3 = new CustomAlphabetCaesarCipher(var1, var2);
        String var4 = "Hello, World!";
        String var5 = var3.encode(var4);
        System.out.println("Encoded message: " + var5);
        String var6 = var3.decode(var5);
        System.out.println("Decoded message: " + var6);
        System.out.println("Custom Alphabet Caesar Cipher completed.");
        pause();
        clearScreen();
    }

    private static void runPasswordStrengthCheck() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Your password: ");
        String userPassword = scanner.nextLine();

        PasswordValidator passwordValidator = new PasswordValidator(userPassword);

        if (passwordValidator.isStrong()) {
            System.out.println("Password is strong!");
        } else {
            System.out.println(
                    "Password is weak. It should be at least 8 characters long and include uppercase, lowercase, digit, and special character.");
        }
        System.out.println("Password Strength Check completed.");
        pause();
        clearScreen();
    }

    static class CustomAlphabetCaesarCipher {
        private String customAlphabet;
        private int shift;

        public CustomAlphabetCaesarCipher(String var1, int var2) {
            this.customAlphabet = var1;
            this.shift = var2;
        }

        public String encode(String var1) {
            StringBuilder var2 = new StringBuilder();
            char[] var3 = var1.toCharArray();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                char var6 = var3[var5];
                if (Character.isLetter(var6)) {
                    int var7 = Character.isLowerCase(var6) ? 97 : 65;
                    int var8 = (var6 - var7 + this.shift + this.customAlphabet.length()) % this.customAlphabet.length();
                    char var9 = this.customAlphabet.charAt(var8);
                    var2.append(var9);
                } else {
                    var2.append(var6);
                }
            }

            return var2.toString();
        }

        public String decode(String var1) {
            StringBuilder var2 = new StringBuilder();
            char[] var3 = var1.toCharArray();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                char var6 = var3[var5];
                if (Character.isLetter(var6)) {
                    int var7 = Character.isLowerCase(var6) ? 97 : 65;
                    int var8 = this.customAlphabet.indexOf(var6);
                    int var9 = (var8 - this.shift + this.customAlphabet.length()) % this.customAlphabet.length();
                    char var10 = (char) (var9 + var7);
                    var2.append(var10);
                } else {
                    var2.append(var6);
                }
            }

            return var2.toString();
        }
    }

    static class PasswordValidator {
        private String password;

        public PasswordValidator(String password) {
            this.password = password;
        }

        public boolean isStrong() {
            return isLongEnough() && containsUppercase() && containsLowercase() && containsDigit()
                    && containsSpecialChar();
        }

        private boolean isLongEnough() {
            return password.length() >= 8;
        }

        private boolean containsUppercase() {
            return !password.equals(password.toLowerCase());
        }

        private boolean containsLowercase() {
            return !password.equals(password.toUpperCase());
        }

        private boolean containsDigit() {
            return password.matches(".*\\d.*");
        }

        private boolean containsSpecialChar() {
            Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+");
            Matcher matcher = specialCharPattern.matcher(password);
            return matcher.find();
        }

    }

    private static void exit() {

        System.out.println(" _______ Thank you for Your Time _________");
        System.out.println(" ");
        System.out.println(" ");

        System.out.println(" Team Member :-RAHI AGARWAL");
        System.out.println(" Enroll No. :-9921103145");
        System.out.println();

        pause();
        System.out.println(" Team Member :-Navi pandey");
        System.out.println(" Enroll No. :-9921103023");
        System.out.println();

        pause();
        System.out.println(" Team Member :-Satvik Bhuttan");
        System.out.println(" Enroll No. :-9921103098");
        System.out.println();

        pause();
        clearScreen();
        System.out.println("Exiting program. Goodbye!");
        System.exit(0);
    }

    private static void pause() {
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clearScreen() {
        System.out.flush();
    }

    private static void restartComputer() {
        System.out.println("Maximum incorrect password attempts reached. Restarting computer...");

        String operatingSystem = System.getProperty("os.name").toLowerCase();

        try {
            ProcessBuilder processBuilder;

            if (operatingSystem.contains("win")) {
                // For Windows
                processBuilder = new ProcessBuilder("shutdown", "-r", "-t", "0");
            } else {
                System.out.println("Unsupported operating system for restart.");
                return;
            }
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
