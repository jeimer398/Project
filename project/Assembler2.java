package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler2 {
    public static void assemble(File input, File output, ArrayList<String> errors){
        ArrayList<String> code = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        String temp = "";
        try(Scanner in = new Scanner(input)){
            ArrayList<String> inText = new ArrayList<>();
            while(in.hasNextLine()){
                inText.add(in.nextLine());
            }
            for (int i = 0; i < inText.size() - 1; i++) {
                if (inText.get(i).trim().length() == 0 && inText.get(i + 1).trim().length() > 0) {
                    errors.add("Error: line " + i + 1 + " is a blank line");  //DO LINE NUMBERS INDEX AT 0 OR 1??
                }
            }
            for (int i = 0; i < inText.size(); i++) {
                if (inText.get(i).charAt(0) == ' ' || inText.get(i).charAt(0) == '\t') {
                    errors.add("Error: line " + i + 1 + " starts with white space");
                }
            }
            boolean ds1Found = false;
            for (int i = 0; i < inText.size(); i++) {
                if (inText.get(i).startsWith("--")) {
                    if (!ds1Found && inText.get(i).trim().replace("-", "").length() == 0) {
                        errors.add("Error: line " + i + 1 + " has a badly formatted data separator");
                    } else if (ds1Found) {
                        errors.add("Error: line " + i + 1 + " has a duplicate data separator");
                    } else {
                        ds1Found = true;
                    }
                }
            }
            if (errors.size() == 0) {
                boolean separate = false;
                for (int i = 0; i < inText.size(); i++) {
                    //TODO----------------------------------------
                }
            }
        } catch(FileNotFoundException e){
            errors.add(0, "Input file does not exist.");
            return;
        }
        ArrayList<String>outText = new ArrayList<>();
        for(String line : code){
            String[] parts = line.trim().split("\\s+");
            int indirLvl = 0;
            if(parts.length == 2){
                indirLvl = 1;
                if(parts[1].startsWith("[")){
                    parts[1] = parts[1].substring(1, parts[1].length()-1);
                    indirLvl = 2;
                }
            }
            if(parts[0].endsWith("I")){
                indirLvl = 0;
            } else if(parts[0].endsWith("A")){
                indirLvl = 3;
            }
            int opcode = InstructionMap.opcode.get(parts[0]);
            if(parts.length == 1){
                outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
            }
            if(parts.length == 2){
                outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
            }
        }
        outText.add("-1");
        outText.addAll(data);
        try (PrintWriter out = new PrintWriter(output)){
            for(String s : outText) out.println(s);
        } catch (FileNotFoundException e) {
            errors.add("Cannot create output file");
        }
    }
}
