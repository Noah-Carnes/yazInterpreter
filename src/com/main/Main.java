package com.main;
import java.io.*;
import java.util.Scanner;
/*
Noah Carnes
Rieber
Yaz Interpreter
3/7/2021

Interpreter for the programming language YazLang.
When interpretting a YazLang file, the program prompts the user for input and output file
names. Then the program reads and executes the YazLang commmands in the input file and outputs the
results to a different file. The user can later view the output file that was created or quit the program.


Expected Output:

Welcome to YazInterpreter!
You may interpret a YazLang program and output
the results to a file or view a previously
interpreted YazLang program.
(I)nterpret .yzy program, (V)iew .yzy output, (Q)uit? I
Input file name: input.yzy
File not found. Try again: interpret.txt
File not found. Try again: interpret.yzy
Output file name: interpret-out.txt
YazLang interpreted and output to a file!
(I)nterpret .yzy program, (V)iew .yzy output, (Q)uit? View
(I)nterpret .yzy program, (V)iew .yzy output, (Q)uit? vi
(I)nterpret .yzy program, (V)iew .yzy output, (Q)uit? v
Input file name: interpret-out.txt
-9 -6 -3 0 3 6
39F
gucci ganggucci ganggucci ganggucci ganggucci ganggucci ganggucci gang
11C
humuhumunukunukuapua'a
5 12 19 26 33
24F
(I)nterpret .yzy program, (V)iew .yzy output, (Q)uit? q


 */
public class Main {
    private static PrintStream output;

    /*

   *  The main method calls createDirectory as well as welcomeStrings upon the start of the program, these create files and give
   * the user the welcome message and instructions on program useage
   * Furthermore the main method controls the commands the user can input and calls other methods such as interpretYAZ
   *to interpret and view files
     */
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

    /*
    This method creates directory's for the user to upload input and output files, it wasnt part of the spec
    but allows me as well as the user to stay organised as they use the YAZ interpreter.
     */
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
/*
The simple system.out.println statements that generate the welcome message and instructions for the user.
 */
    public static void welcomeStrings(){
        System.out.println("Welcome to YazInterpreter!");
        System.out.println("You may interpret a YazLang program and output");
        System.out.println("the results to a file or view a previously");
        System.out.println("interpreted YazLang program.");
        System.out.println(" ");
        System.out.print("(I)nterperet .yzy program, (V)iew .yzy output, (Q)uit?");
    }

    /*
    Interprets the yaz files the user selects then creates and writes the interpreted information to the user
     named output file
    Calls on the runCommand function to figure out which arguments and lines in the file correspond to different YAZ
    commands.
     */
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
                String newData = runCommand(data);

               output.println(newData);
            }

            scanner.close();
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Scans and detects if files the user wants to create exist or not as well as asks the user what the name of their
    file is.
     */
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

    /*
    *This string takes each line of the user submitted yaz file and determines the command each line gives
    * It also returns the command to the main method for it to be writen as a file.
    * If the command on a line is not CONVERT, RANGE, or REPEAT the program will return "syntax error!"
    * to signal the user that there is an error.
     */
    public static String runCommand(String s){
        Scanner scanner = new Scanner(s);
        String command = scanner.next();

        if(command.equals("CONVERT")){
            return convertCommand(scanner);
        }else if(command.equals("RANGE")){
            return rangeCommand(scanner);
        }else if(command.equals("REPEAT")){
           return (repeatCommand(scanner));
        }else{
            return "Syntax error!";
        }
    }
/*
*This function prints the passed in data to a file, this function essentially controls the view command
 */
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
/*
 * This command converts Celsius to Fahrenheit when called then returns the result to be written to the interpreted file.
 * It detects if the string has either an F or a C:
 * If it has an F the program converts the integer to Celsius
 * If it has a C the program converts the integer to Fahrenheit
 * both result in the converted value being returned to the main method to be written to the interpreted file
 */
    public static String convertCommand ( Scanner scanner ){
        int value = scanner.nextInt();
        String type = scanner.next();
        String result = "";
       // while (scanner.hasNext()){
            if(type.equals("C")){
                String c = Math.round(1.8 * value + 32) + "F";
                return c += result;
            } else if(type.equals("F")){
                String f = Math.round((value - 32) / 1.8) + "C";
                return f += result;
            }
      //  }
        return result;
    }

    /*
    Controls the repeat YAZ command, whenever the program comes across a repeat command this function repeats the
    statement in "quotes" and removes the _ and replaces it with spaces.
     */
    public static String repeatCommand ( Scanner scanner) {
        String endValue = "";
        while (scanner.hasNext()) {

            String repeatedString = scanner.next();
            repeatedString = repeatedString.substring(1, repeatedString.length() - 1);
            repeatedString = repeatedString.replace('_', ' ');
            int repetitions = scanner.nextInt();
            for (int e = 0; e < repetitions; e++) {
                endValue += repeatedString;
            }

        }
        return endValue;
    }

     /*
     Takes the YAZ files range command and proforms its actions, taking the first value as the start, the second as the
     end and the third as the factor then puts them in a loop.
      */

    public static String rangeCommand (Scanner scanner) {
        String rangeEndValue = "";
        int start = scanner.nextInt();
        int end = scanner.nextInt();
        int factor = scanner.nextInt();
        for (int r = start; r < end; r += factor){
            rangeEndValue += r + " ";
        }
        return rangeEndValue;
    }
}
