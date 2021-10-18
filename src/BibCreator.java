import java.io.*;
import java.util.*;
import Exception.FileInvalidException;


public class BibCreator {
    static int counter = 10;

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

            processFilesForValidation(scanners, pwIEEEs, pwACMs, pwNJs, IEEE_file_Output_List, ACM_file_Output_List, NJ_file_Output_List, inputFileList);

            System.out.print("A total of " + (10 - counter)  + " files were invalid, and could not be processed.");
            System.out.println(" All other " + counter + " \"Valid\" files have been created.\n");

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
    static void processFilesForValidation(Scanner[] sc, PrintWriter[] pwIEEEs, PrintWriter[] pwACMs,
                                          PrintWriter[] pwNJs, List<File> IEEE_file_Output_List, List<File> ACM_file_Output_List,
                                        List<File> NJ_file_Output_List,List<String> file){
        // Scan all input files
        for(int i =0; i<10; i++){
            String content = null;
            boolean isValid = true;
            while(sc[i].hasNextLine()){
                content = content + sc[i].nextLine();
            }
            // Split articles from each file.
            String[] article = content.split("@ARTICLE");
            String fileName = file.get(i);
            for(int j = 1; j< article.length; j++){
                String IEEE , ACM , NJ ;
                String IEEEAuthor = null, ACMAuthor = null, NJAuthor = null;
                String thisAuthor = null;
                String thisTitle = null;
                String thisJournal = null;
                String thisVolume = null;
                String thisNumber = null;
                String thisPages = null;
                String thisMonth = null;
                String thisYear = null;
                String thisISSN = null;
                String thisDoi = null;
                String thisKeywords = null;
                String[] item = article[j].split("\\},");
                // Split individual items from each article.
                for (int k=0; k< item.length; k++){
                    String[] itemDetail = item[k].split("=\\{",-1);
                    if(itemDetail[0].equals("author")){
                        try{
                            thisAuthor = itemDetail[1];
                            if (thisAuthor.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            } else {
                                IEEEAuthor =  thisAuthor.replace(" and", ",") +".";
                                ACMAuthor = thisAuthor.split("and")[0]+"et al. ";
                                NJAuthor =  thisAuthor.replace("and", "&") +". ";
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                        }
                    if(itemDetail[0].equals("title")){
                        try {
                            thisTitle = itemDetail[1];
                            if (thisTitle.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("year")){
                        try {
                            thisYear = itemDetail[1];
                            if (thisYear.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("journal")){
                        try {
                            thisJournal = itemDetail[1];
                            if (thisJournal.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("volume")){
                        try {
                            thisVolume = itemDetail[1];
                            if (thisVolume.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("number")){
                        try {
                            thisNumber = itemDetail[1];
                            if (thisNumber.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("pages")){
                        try {
                            thisPages = itemDetail[1];
                            if (thisPages.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("month")){
                        try {
                            thisMonth = itemDetail[1];
                            if (thisMonth.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("ISSN")){
                        try {
                            thisISSN = itemDetail[1];
                            if (thisISSN.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("keywords")){
                        try {
                            thisKeywords = itemDetail[1];
                            if (thisKeywords.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            }
                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    if(itemDetail[0].equals("doi")) {
                        try {
                            thisDoi = itemDetail[1];
                            if (thisDoi.length() == 0) {
                                throw new FileInvalidException("Error: Detected Empty Field!");
                            } else {
                                thisDoi = "DOI:https://doi.org/" +  thisDoi;
                            }

                        } catch (FileInvalidException e) {
                            displayErrorMessage(fileName, e, itemDetail);
                            isValid = false;
                            break;
                        }
                        continue;
                    }
                    // Once catch FileInvalidException, isValid is false, break the loop.
                    if (!isValid) {
                        break;
                    } else {
                        IEEE = IEEEAuthor +" \"" + thisTitle + "\", " + thisJournal + ", vol. " + thisVolume + ", no. " + thisNumber +", p. "+ thisPages + ", " + thisMonth +" "+thisYear +"." ;
                        ACM = "["+ j +"] "+ ACMAuthor + thisYear+". "+ thisTitle + ". " + thisJournal + ". " + thisVolume + ", " + thisNumber + " (" + thisYear + "), "+ thisPages + ". " + thisDoi +"." ;
                        NJ = NJAuthor + thisTitle +". " + thisJournal + ". " + thisVolume + ", " + thisPages + "("+thisYear +").";
                        pwIEEEs[i].println(IEEE);
                        pwIEEEs[i].println();
                        pwACMs[i].println(ACM);
                        pwACMs[i].println();
                        pwNJs[i].println(NJ);
                        pwNJs[i].println();
                    }

                }
                // Once FileInvalidException is thrown, the corresponding output file is deleted
                if (!isValid) {
                    counter--;
                    pwIEEEs[i].close();
                    pwACMs[i].close();
                    pwNJs[i].close();
                    IEEE_file_Output_List.get(i).delete();
                    ACM_file_Output_List.get(i).delete();
                    NJ_file_Output_List.get(i).delete();
                }
                sc[i].close();
                pwIEEEs[i].close();
                pwACMs[i].close();
                pwNJs[i].close();

                }
            }
        }
    // Display message as required.
    static void displayErrorMessage(String fileName, FileInvalidException e, String[] itemDetail) {
        System.out.println(e.getMessage());
        System.out.println("--------------------------------");
        System.out.println();
        System.out.println("Problem detected with input file: "+ fileName);
        System.out.println("Field is Invalid: Field \"" + itemDetail[0] + "\" is Empty. Processing stopped at this point. "
                            + "Other empty fields may be present as well!" + '\n');
    }
    }


