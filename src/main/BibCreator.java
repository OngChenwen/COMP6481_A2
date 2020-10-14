package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BibCreator {
    final static String path = "src/Files/Latex1.bib";

    // display line number for every part(start index & end index)
    public static void fileSectionExtractionDetection(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String input;
            int lineNum = 0,occurNum=0,endNum=0;

            ArrayList<Integer> indexBegin = new ArrayList<>();
            ArrayList<Integer> indexEnd = new ArrayList<>();

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

            for(int i=0;i<indexBegin.size();i++){
                System.out.print("Begin Index (@ARTICLE): " + indexBegin.get(i) + "\n"
                + "End index: " + indexEnd.get(i) + "\n");
                System.out.println("*****************");
            }


        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        fileSectionExtractionDetection();


    }
}
