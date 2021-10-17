import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class BibCreator {
    final static String path = "src/Files/Latex1.bib";
    ArrayList<Integer> indexBegin = new ArrayList<>();
    ArrayList<Integer> indexEnd = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();

    public BibCreator(){
        // get each part from file (start with @)
        //updated indexBegin & indexEnd list
        fileSectionExtractionDetection();

        for(int i=0; i< indexBegin.size();i++){
            extractInfo(indexBegin.get(i),indexEnd.get(i));
        }

        /*Iterator<String> iterator = contentList.iterator();

        while (iterator.hasNext()){
            System.out.println(iterator.next());

        }*/

        saveInfoInMap(contentList.get(0));


    }
    // line number for every part(start index & end index)
    public void fileSectionExtractionDetection(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String input;
            int lineNum = 0,occurNum=0,endNum=0;

            while ((input = br.readLine()) != null){
                lineNum++;
                if (input.startsWith("@ARTICLE{")){
                    occurNum = lineNum;
                    indexBegin.add(occurNum);
                }

                if (input.endsWith("}")){
                    endNum = lineNum;
                    indexEnd.add(endNum);
                }

            }

            /*for(int i=0;i<indexBegin.size();i++){
                System.out.print("Begin Index (@ARTICLE): " + indexBegin.get(i) + "\n"
                + "End index: " + indexEnd.get(i) + "\n");
                System.out.println("*****************");
            } */
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void extractInfo(int startIndex, int endIndex){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String lines;
            String content = "";

            int lineNumber=0;

            while ((lines=br.readLine()) != null){
                lineNumber++;

                if(lineNumber >=startIndex && lineNumber <= endIndex){
                    content = content + lines;
                }
            }

            contentList.add(content);

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void saveInfoInMap(String input){
        String[] InputList= input.split(",");

        for(String s : InputList){
            if(s.contains("=")){
                int indexOfEqual = s.indexOf('=');
                String key = s.substring(0,indexOfEqual);
                String value = s.substring((s.indexOf('{')+1),s.indexOf('}'));


            }
        }



    }

    public static void main(String[] args) {
        new BibCreator();
    }

    }

