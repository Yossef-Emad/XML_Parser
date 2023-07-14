package com.example.ds_xml;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class ConsistencyCheck
{
    private String[] Lines = null;
    private ArrayList<String> WrongTags;

    private int errorCounter = 0;

    public int getErrorCounter() {
        return errorCounter;
    }

    public ConsistencyCheck(String file) {

        Lines = file.replaceAll(" ", "")
                .replaceAll("<", "\n<")
                .replaceAll(">", ">\n")
                .split("\n");

        WrongTags = new ArrayList<>();
    }

    public ArrayList<String> getWrongTags(){

        return WrongTags;
    }
    private boolean isOpeningTag(String s) {
        int len = s.length();
        if (len > 1)
            return (s.charAt(0) == '<') && (s.charAt(1) != '/') && (s.charAt(len - 1) == '>');

        return false;
    }

    private boolean isClosingTag(String s) {
        int len = s.length();
        if (len > 1)
            return (s.charAt(0) == '<') && (s.charAt(1) == '/') && (s.charAt(len - 1) == '>');

        return false;
    }

    /* No Data or Incorrect Tag */
    private boolean ContainsNoTag(String s) {
        return !isOpeningTag(s) && !isClosingTag(s);
    }

    private boolean compareWithTop(String Tag, String topStack) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < Tag.length(); i++) {
            if (Tag.charAt(i) != '/') {
                s.append(Tag.charAt(i));
            }
        }

        return topStack.equals(s.toString());
    }

    public String correctTagBrackets()
    {
        StringBuilder correctedFile = new StringBuilder();

        for (String line : Lines)
        {
            /* Either contains an Incorrect Tag or Data */
            if (ContainsNoTag(line))
            {
                if ((line.startsWith("<")) && !(line.endsWith(">")))
                {
                    correctedFile.append(line).append(">").append("\n");
                }
                else if (!(line.startsWith("<")) && (line.endsWith(">")))
                {
                    correctedFile.append("<").append(line).append("\n");
                }
                else
                {
                    /* Append only non-empty lines */
                    if (!line.equals(""))
                        correctedFile.append(line).append("\n");
                }
            }
            else
                /* Append all correct tags */
                correctedFile.append(line).append("\n");
        }

        return correctedFile.toString();
    }

    public String correctTagBalance(String file)
    {
        String[] Lines = file.split("\n");
        StringBuilder correctedFile = new StringBuilder();
        Stack<String> TagsHolder = new Stack<>();

        for (int i = 0; i < Lines.length; i++)
        {
            if (!ContainsNoTag(Lines[i]))
            {
                if (isOpeningTag(Lines[i]))
                {
                    TagsHolder.push(Lines[i]);
                    correctedFile.append(Lines[i]).append("\n");
                }
                else if ((isClosingTag(Lines[i])) && !(TagsHolder.isEmpty()))
                {
                    if (compareWithTop(Lines[i], TagsHolder.peek()))
                    {
                        TagsHolder.pop();
                        correctedFile.append(Lines[i]).append("\n");
                    }
                    else
                    {
                        if (TagsHolder.contains(handleSlash(Lines[i])))
                        {
                            i--;
                            correctedFile.append(handleSlash(TagsHolder.peek())).append("\n");
                            TagsHolder.pop();
                        }
                        // Closing without opening
                        else
                        {
                            correctedFile.append(handleSlash(Lines[i])).append("\n").append(Lines[i]).append("\n");
                        }
                    }
                }
                else if (isClosingTag(Lines[i]))
                {
                    String lastFile = handleSlash(Lines[i]) + "\n" + correctedFile.toString() + Lines[i];
                    correctedFile = new StringBuilder(lastFile);
                }
            }
            else
                correctedFile.append(Lines[i]).append("\n");
        }

        return correctedFile.toString();
    }


    private static String[] splitFile(String file)
    {
        String[] lines;
        file = file.trim().replaceAll("<", "\n<").replaceAll(">", ">\n");
        lines = file.split("\n");
        return lines;
    }

    public boolean CheckTagsBalance()
    {
        boolean hasAtLeastOnePush = false;
        Stack<String> TagsHolder = new Stack<>();
        WrongTags.clear();
        errorCounter = 0;

        for (int i = 0 ; i < Lines.length; i++)
        {
            if (!ContainsNoTag(Lines[i]))
            {
                if (isOpeningTag(Lines[i]))
                {
                    TagsHolder.push(Lines[i]);
                    hasAtLeastOnePush = true;
                }
                else if ((isClosingTag(Lines[i])) && !(TagsHolder.isEmpty()))
                {
                    if (compareWithTop(Lines[i], TagsHolder.peek()))
                    {
                        TagsHolder.pop();
                    }
                    else
                    {
                        errorCounter++;
                        /* Opening tag without closing */
                        if (TagsHolder.contains(handleSlash(Lines[i])))
                        {
                            i--;
                            WrongTags.add(TagsHolder.peek());
                            TagsHolder.pop();
                        }
                        /* Closing without opening */
                        else
                            WrongTags.add(Lines[i]);

                    }
                }
                else if (isClosingTag(Lines[i]))
                {
                    errorCounter++;
                    WrongTags.add(Lines[i]);
                }
            }
        }

        while(!TagsHolder.isEmpty()){
            errorCounter++;
            WrongTags.add(TagsHolder.peek());
            TagsHolder.pop();
        }

        /* error counter return */
        return (errorCounter == 0) && hasAtLeastOnePush;
    }

    private static String handleSlash(String Tag)
    {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < Tag.length(); i++)
        {
            if (i == 1)
            {
                if(Tag.charAt(i) != '/')
                {
                    s.append('/').append(Tag.charAt(i));
                }
            }
            else
                s.append(Tag.charAt(i));
        }
        return s.toString();
    }

    public boolean CheckTagsCorrectness()
    {
        errorCounter = 0;
        WrongTags.clear();
        boolean isTagCorrect = true;

        for (String line : Lines)
        {
            if (ContainsNoTag(line)) {
                if ((line.startsWith("<")) && !(line.endsWith(">")))
                {
                    errorCounter++;
                    WrongTags.add(line);
                    isTagCorrect = false;
                }
                else if (!(line.startsWith("<")) && (line.endsWith(">")))
                {
                    errorCounter++;
                    WrongTags.add(line);
                    isTagCorrect = false;
                }
            }
        }
        return isTagCorrect;
    }

    public boolean isFileConsistent()
    {
        return CheckTagsBalance() && CheckTagsCorrectness();
    }

    public static String ReadFile(String filePath) throws IOException {
        String line;
        StringBuilder file = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        while ((line = reader.readLine()) != null) {
            file.append(line).append("\n");
        }
        reader.close();

        return file.toString();
    }

    public static void WriteFile(String inputFile, String outputFilePath) throws IOException
    {
        if ((inputFile == null) || (inputFile.trim().length() == 0))
            return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        writer.write(inputFile);
        writer.close();
    }

    public static void main(String[] args) throws IOException
    {
        String file = ReadFile("formatted_sample1.xml");

        ConsistencyCheck c1 = new ConsistencyCheck(file);

        //System.out.println(c1.CheckTagsCorrectness());

        System.out.println(c1.CheckTagsBalance());

        //System.out.println(c1.isFileConsistent());

        System.out.println("Errors --> " + c1.getErrorCounter());

        System.out.println(c1.getWrongTags());

        /*
        String s ="<users><user><name>Mohamed</users>";
        Check2 c1 = new Check2(s);
        System.out.println(c1.CheckTagsBalance());
        System.out.println(c1.getErrorCounter());
        System.out.println(c1.getWrongTags());
        //System.out.println(c1.getCorrectedFile());
        //System.out.println(Format(c1.getCorrectedFile()));
         */
    }

}