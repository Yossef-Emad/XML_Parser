package com.example.ds_xml;

import java.io.*;

public class Formatting {
    /*
    * function to Format & prettify the XML file
    * */
    public static String Format(String file)
    {
        /*check if the inputPath file is empty*/
        if ((file == null) || (file.trim().length() == 0))
            return "";

        int level = 0;  /* variable to hold the value of each level */
        StringBuilder fileBuilder = new StringBuilder();

        /* calling split function to separate the file into lines and store each line in array of strings */
        String[] line = splitFile(file);

        /* looping on each line in the array of strings */
        for (int i = 0; i < line.length; i++)
        {
            /* if this line is empty then skip this line */
            if (line[i] == null || line[i].trim().length() == 0)
                continue;

            line[i] = line[i].trim();
            /*if this line is a closing tag then will make indentation then append the file with this line with a new line */
            if (line[i].startsWith("</"))
            {
                level--;
                String indent = generateIndentation(level);
                fileBuilder.append(indent).append(line[i]).append("\n");
            }
            /*if this line is an XML start tag then will append the file with the line without changes or indentation */
            else if (line[i].startsWith("<?"))
            {
                fileBuilder.append(line[i]).append("\n");
            }
            /*if this line is a opening tag then will make indentation then append the file with this line with a new line */
            else if (line[i].startsWith("<"))
            {
                String indent = generateIndentation(level);
                fileBuilder.append(indent).append(line[i]).append("\n");
                level++;
            }
            /*if this line is not a tag ( data ) then will append the data on the same level without indentation */
            else
            {
                String indent = generateIndentation(level);
                fileBuilder.append(indent).append(line[i]).append("\n");
            }
        }
        return fileBuilder.toString();
    }

    /*
     * function to trim and split all the file into separate lines by adding \n
     * */
    public static String[] splitFile(String file)
    {
        String[] lines;
        file = file.trim().replaceAll("<", "\n<").replaceAll(">", ">\n");
        lines = file.split("\n");
        return lines;
    }

    /*
    * function to generate spaces ( indentation ) depending on the level of each line in the XML file
    * */
    public static String generateIndentation(int level)
    {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++)
        {
            indent.append("    ");
        }
        return indent.toString();
    }

    /*
     * Function to read the XML file from a specific path and returns a String
     * */
    public static String ReadFile(String filePath) throws IOException
    {
        String line;
        StringBuilder file = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        /* check if the line is empty */
        while((line = reader.readLine()) != null)
        {
            file.append(line).append("\n");
        }
        reader.close();

        return file.toString();
    }

    /*
    * Function to write the new formatted file into a specific path
    * */
    public static void WriteFile(String inputFile, String outputFilePath) throws IOException
    {
        /* check if the inputPath file is empty */
        if ((inputFile == null) || (inputFile.trim().length() == 0))
            return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        writer.write(inputFile);
        writer.close();
    }

    public static void main(String[] args) throws IOException {

        String fileName = "sample1.xml";

        String formattedFile = Format(ReadFile(fileName));

        System.out.println(formattedFile);

        WriteFile(formattedFile,"formatted_" + fileName);
    }
}
