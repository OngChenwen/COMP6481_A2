import java.io.*;
import java.util.*;


public class BibCreator {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        List<String> inputFileList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            inputFileList.add("Latex" + (i+1)+".bib");
        }

        Scanner[] scanners = new Scanner[10];

        System.out.println("Welcome to BibCreator");
        // open files
        String fileName = "";
        for (int i = 0; i < inputFileList.size(); i++) {
            try{
                fileName = inputFileList.get(i);
                scanners[i] = new Scanner(new File(fileName));
            } catch (FileNotFoundException e){
                System.out.println("Could not open input file " + fileName + " for reading.");
                System.out.println();
                System.out.println("Please check if file exists! Program will terminate after closing any opened files.");
                // close opened files and exit the system
                for (int j = 0; j < i; j++) {
                    scanners[j].close();
                }
                System.exit(0);
            }

        }

        PrintWriter[] pwIEEEs = new PrintWriter[10];
        PrintWriter[] pwACMs = new PrintWriter[10];
        PrintWriter[] pwNJs = new PrintWriter[10];

        List<File> IEEE_file_Output_List = new ArrayList<>();
        List<File> ACM_file_Output_List = new ArrayList<>();
        List<File> NJ_file_Output_List = new ArrayList<>();

        String IEEE_file, ACM_file, NJ_file;
        boolean flagOfIEEE = false, flagOfACM = false, flagOfNJ = false;
        for (int i = 0; i < 10; i++) {
            IEEE_file = "IEEE" + (i + 1) + ".json";
            ACM_file = "ACM" + (i + 1) + ".json";
            NJ_file = "NJ" + (i + 1) + ".json";

            try{
                pwIEEEs[i] = new PrintWriter(new FileOutputStream(IEEE_file));
                File IEEEFile = new File(IEEE_file);
                IEEE_file_Output_List.add(IEEEFile);
                flagOfIEEE = true;

                pwACMs[i] = new PrintWriter(new FileOutputStream(ACM_file));
                File ACMFile = new File(ACM_file);
                ACM_file_Output_List.add(ACMFile);
                flagOfACM = true;

                pwNJs[i] = new PrintWriter(new FileOutputStream(NJ_file));
                File NJFile = new File(NJ_file);
                NJ_file_Output_List.add(NJFile);
                flagOfNJ = true;

            }catch (FileNotFoundException e){
                // close all the PrintWriter
                if(!flagOfIEEE){
                    System.out.println("!! ** The " + IEEE_file + " can not be opened/created for writing! ** !!");
                    for (int j = 0; j < i; j++) {
                        pwIEEEs[j].close();
                        pwACMs[j].close();
                        pwNJs[j].close();
                    }
                } else if(!flagOfACM){
                    System.out.println("!! ** The " + ACM_file + " can not be opened/created for writing! ** !!");
                    // whole ACM file can not be opened, the output IEEE file already has been created.
                        pwIEEEs[0].close();
                    for (int j = 0; j < i; j++) {
                        pwIEEEs[j + 1].close();
                        pwACMs[j].close();
                        pwNJs[j].close();
                    }
                } else if(!flagOfNJ){
                    System.out.println("!! ** The " + NJ_file + " can not be opened/created for writing! ** !!");
                    // whole ACM file can not be opened, the output IEEE file already has been created.
                    pwIEEEs[0].close();
                    pwACMs[0].close();
                    for (int j = 0; j < i; j++) {
                        pwIEEEs[j + 1].close();
                        pwACMs[ j + 1].close();
                        pwNJs[j].close();
                    }
                }

                System.out.println("Error Occurred! All of the output files will be deleted");

                for (File files: IEEE_file_Output_List) {
                    files.delete();
                }
                for (File files: ACM_file_Output_List) {
                    files.delete();
                }for (File files: NJ_file_Output_List) {
                    files.delete();
                }

                System.out.println("Program will be terminated");
                for (Scanner s: scanners) {
                    s.close();
                }

                System.exit(0);
            }

            System.out.println("Please enter the name of one of the files you need to review: ");
            String fileNameFromKB = kb.nextLine();
            try{
                displayFileContent(new BufferedReader(new FileReader(fileNameFromKB)),fileNameFromKB);
            }catch (FileNotFoundException e){
                System.out.println("Could not open input file. File does not exist. Pleas try again!");
                System.out.println("Please enter the name of one of the files you need to review: ");
                fileNameFromKB = kb.nextLine();
                try{
                    displayFileContent(new BufferedReader(new FileReader(fileNameFromKB)),fileNameFromKB);
                }catch (FileNotFoundException fe){
                    System.out.println("Could not open input file. File does not exist. Pleas try again!");
                    System.out.println("Sorry i can not get the correct file to display");
                    System.exit(0);
                }catch (IOException ie){
                    System.out.println("Error: An error has occurred while reading from the " + fileNameFromKB + " file.");
                    System.out.println("Program will be terminated");
                    System.exit(0);
                }
            }catch (IOException e){
                System.out.println("Error: An error has occurred while reading from the " + fileNameFromKB + " file.");
                System.out.println("Program will be terminated");
                System.exit(0);
            }


        }


    }

    static void displayFileContent(BufferedReader br, String fileName) throws IOException {
        System.out.println("Here are the contents of the successfully created Json file: " + fileName);
        String s = br.readLine();
        while (s != null){
            System.out.println(s);
            s = br.readLine();
        }

        br.close();
    }

    }

