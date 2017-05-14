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

            int curr = inText.size() - 1;
            while (curr >= 0 && inText.get(curr).trim().length() == 0) {
                inText.remove(curr);
                curr--;
            }

            for (int i = 0; i < inText.size(); i++) {
                if (inText.get(i).trim().length() == 0) {
                    errors.add("Error: line " + (i + 1) + " is a blank line");
                }
            }
            for (int i = 0; i < inText.size(); i++) {
                if (inText.get(i).startsWith(" ") || inText.get(i).startsWith("\t")) {
                    errors.add("Error: line " + (i + 1) + " starts with white space");
                }
            }
            boolean ds1Found = false;
            for (int i = 0; i < inText.size(); i++) {
                if (inText.get(i).startsWith("--")) {
                    if (inText.get(i).trim().replace("-", "").length() != 0) {
                        errors.add("Error: line " + (i + 1) + " has a badly formatted data separator");
                    } else if (ds1Found) {
                        errors.add("Error: line " + (i + 1) + " has a duplicate data separator");
                    } else {
                        ds1Found = true;
                    }
                }
            }
            boolean separate = false;
            int i = 0;
            while (i < inText.size() && !inText.get(i).startsWith("--")) {
                code.add(inText.get(i).trim());
                i++;
            }
            for (i++; i < inText.size(); i++) {
                data.add(inText.get(i).trim());
            }
        } catch(FileNotFoundException e){
            errors.add(0, "Input file does not exist.");
            return;
        }
        ArrayList<String> outText = new ArrayList<>();
        for(int i = 0; i < code.size(); i++) {
            String line = code.get(i);
            String[] parts = line.trim().split("\\s+");
             if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) && !InstructionMap.sourceCodes.contains(parts[0])) {
                errors.add("Error: line " + (i + 1) + " does not have the instruction mnemonic in upper case");
            } else if (!InstructionMap.sourceCodes.contains(parts[0]) && parts[0].length() > 0) {
                errors.add("Error: line " + (i + 1) + " illegal mnemonic");
            } else if (InstructionMap.noArgument.contains(parts[0]) && parts.length != 1) {
                errors.add("Error: line " + (i + 1) + " can not take arguments");
            } else if (InstructionMap.sourceCodes.contains(parts[0]) && !InstructionMap.noArgument.contains(parts[0])) {
                if (parts.length == 1) {
                    errors.add("Error: line " + (i + 1) + " is missing an argument");
                } else if (parts.length > 2) {
                    errors.add("Error: line " + (i + 1) + " has more than one argument");
                }
            }
            int indirLvl = 0;
            if(parts.length == 2){
                indirLvl = 1;
                if(parts[1].startsWith("[")){
                    if (InstructionMap.sourceCodes.contains(parts[0]) && !InstructionMap.indirectOK.contains(parts[0])) {
                        errors.add("Error: line " + (i + 1) + " this opcode does not support indirect arguments");
                    } else if (!parts[1].endsWith("]")) {
                        errors.add("Error: line " + (i + 1) + " open brace with no closing brace");
                        parts[1] = parts[1].substring(1);
                        indirLvl = 2;
                    } else {
                        parts[1] = parts[1].substring(1, parts[1].length() - 1);
                        indirLvl = 2;
                    }
                }
                int arg = 0;
                try {
                    arg = Integer.parseInt(parts[1],16);
                } catch (NumberFormatException e) {
                    errors.add("Error: line " + (i + 1)
                            + " argument is not a hex number");
                }
            }
            //PLACEHOLDER IN CASE WE REMEMBER ANY OTHER ERRORS
            if(parts[0].endsWith("I")){
                indirLvl = 0;
            } else if(parts[0].endsWith("A")){
                indirLvl = 3;
            }
            if (errors.size() == 0) {
                int opcode = InstructionMap.opcode.get(parts[0]);
                if (parts.length == 1) {
                    outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
                }
                if (parts.length == 2) {
                    outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
                }
            }
        }
        outText.add("-1");
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 2 && data.get(i).trim().length() != 0) {
                errors.add("Error: line " + (i + code.size() + 2) + " incorrect number of terms in data line");
            } else  if (parts.length > 1) {
                int arg = 0;
                try {
                    arg = Integer.parseInt(parts[0],16);
                } catch (NumberFormatException e) {
                    errors.add("Error: line " + (i + code.size() + 2)
                            + " data address is not a hex number");
                }
                try {
                    arg = Integer.parseInt(parts[1], 16);
                } catch (NumberFormatException e) {
                    errors.add("Error: line " + (i + code.size() + 2)
                            + " data value is not a hex number");
                }
            }
        }
        outText.addAll(data);
        if (errors.size() > 0) {
            return;
        }
        try (PrintWriter out = new PrintWriter(output)){
            for(String s : outText) out.println(s);
        } catch (FileNotFoundException e) {
            errors.add("Cannot create output file");
        }
    }
/*
    public static void main(String[] args) {
        ArrayList<String> errors = new ArrayList<>();
        assemble(new File("in.pasm"), new File("out.pexe"), errors);
        for (String er: errors) {
            System.out.println(er);
        }
    }*/
}