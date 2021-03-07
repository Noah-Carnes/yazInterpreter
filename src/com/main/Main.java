package com.main;
import java.io.*;
import java.util.Scanner;

public class Main {
    private static PrintStream output;

    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        createDirectory();
        welcomeStrings();

        String chooseCommandString = console.next().toLowerCase();


        while (!chooseCommandString.equalsIgnoreCase("q")) {
            if (chooseCommandString.equalsIgnoreCase("v")) {
                File file = fileScanner(console);
                printFile(file);
            }
            if (chooseCommandString.equalsIgnoreCase("i")) {
                File file = fileScanner(console);
                if(file.toString().contains(".yzy")){
                    interpretYAZ(file);
                }else{
                    System.out.println("File must contain a .yzy extension.");
                }
            }

            System.out.println("(I)nterperet .yzy program, (V)iew .yzy output, (Q)uit?");
            chooseCommandString = console.next().toLowerCase();
        }
    }
    public static void createDirectory(){
        File mainDirectory = new File("YazInterpreter");
        File inputDirectory = new File("YazInterpreter\\input");
        File outputDirectory = new File("YazInterpreter\\output");
        if(!mainDirectory.exists()){
            mainDirectory.mkdirs();
            inputDirectory.mkdirs();
            outputDirectory.mkdirs();
        }
    }

    public static void welcomeStrings(){
        System.out.println("Welcome to YazInterpreter!");
        System.out.println("You may interpret a YazLang program and output");
        System.out.println("the results to a file or view a previously");
        System.out.println("interpreted YazLang program.");
        System.out.println(" ");
        System.out.print("(I)nterperet .yzy program, (V)iew .yzy output, (Q)uit?");
    }

    public static void interpretYAZ(File file ) throws FileNotFoundException{

        try {
            Scanner scanner = new Scanner(file);
            Scanner fileNameScan = new Scanner(System.in);
            System.out.print("Output file name:");
            String fileName = fileNameScan.next();
            File outputFile = new File("YazInterpreter\\output\\" + fileName);
            PrintStream output = new PrintStream(outputFile);

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String newData = determineCommand(data);

               output.println(newData);
            }

            scanner.close();
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File fileScanner(Scanner fileName) {
        System.out.print("Input file name: ");
        File file = null;

        while (file == null) {
            String fileTitle = fileName.next();
            if (new File("YazInterpreter\\input\\" + fileTitle).exists()) {
                file = new File("YazInterpreter\\input\\" + fileTitle);
            } else if (new File("YazInterpreter\\output\\" + fileTitle).exists()) {
                file = new File("YazInterpreter\\output\\" + fileTitle);
            } else {
                System.out.print("File not found. Try again: ");
            }
        }

        return file;
    }

    public static String determineCommand(String s){
        Scanner scanner = new Scanner(s);
        String command = scanner.next();

        if(command.equals("CONVERT")){
            return convertCommand(scanner);
        }else if(command.equals("RANGE")){
            return "Range";
        }else if(command.equals("REPEAT")){
           return (repeatCommand(scanner));
        }else{
            return "Syntax error!";
        }
    }

    public static void printFile(File file)throws FileNotFoundException{

        Scanner scanner = new Scanner(file);
        System.out.println(" ");
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            System.out.println(data);
        }
        System.out.println(" ");
        scanner.close();

    }

    public static String convertCommand ( Scanner scanner ){
        int value = scanner.nextInt();
        String type = scanner.next();
        String result = "";
        while (scanner.hasNext()){
            if(type.equals("C")){
                String c = (1.8 * value + 32) + "F";
                return c += result;
            } else if(type.equals("F")){
                String f = ((value - 32) / 1.8) + "C";
                return f += result;
            }
        }
        return result;
    }

    public static String repeatCommand ( Scanner scanner) {
        String endValue = "";
        while (scanner.hasNext()) {
            String repeatedString = scanner.next();
            repeatedString = repeatedString.substring(1, repeatedString.length() - 1);
            repeatedString = repeatedString.replace('_', ' ');
            int repetitions = scanner.nextInt();
            for (int e = 0; e < repetitions; e++) {
                return endValue += repeatedString;
            }

        }
        return endValue;
    }
}
