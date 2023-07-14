/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package dssproj;
package com.example.ds_xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class XML_TO_JSON {
       public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String Data = "";
        try {
            File Obj = new File("C:\\Users\\H\\Desktop\\ds_project\\sample.xml");
            Scanner Reader = new Scanner(Obj);
            while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                // System.out.println(data);
                Data += data;
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }

        System.out.println(xml2json(Data));}
//  finally function to convert from xml to json
    public static String xml2json(String xml) {
        ArrayList<Node> arr = xml2arr(xml);// call the function convert xml into array
        Node node = arr2tree(arr);// call the function convert array into tree
        StringBuilder sb = new StringBuilder();
        tree2json(node, 0, sb);// call the function convert tree into json
        return "{\n" + sb + "\n}";
    }
//for the repeated words 
    private static String repeated(String st, int count){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<count; i++){
            sb.append(st);
        }
        return sb.toString();
    }
    // thirdly convert this tree into json by converting every symbol in xml into symbol in json and put the data
    private static void tree2json(Node node, int tab, StringBuilder p) {
        tab++;

        p.append(repeated("    ", tab));
        if (node.type == NodeType.root) {
            p.append("\"" + node.data + "\"");
            return;
        }


        if (node.type == NodeType.parent) {
            p.append("\"" + node.data + "\": \"" + node.children.get(0).data + "\"");
            return;
        }

        if (node.data != "") p.append("\"" + node.data + "\": ");

        if (node.type == NodeType.repeatedtag)
            p.append("[\n");
        else
           p.append("{\n");

        for (int i = 0; i < node.children.size(); i++) {
            tree2json(node.children.get(i), tab,p);

            if (i < node.children.size() - 1)
                p.append(", \n");
            else {
                p.append('\n');
                p.append(repeated("    ", tab));

                if (node.type == NodeType.repeatedtag)
                    p.append("]");
                else
                    p.append("}");
            }
        }
    }
// secondly we convert this array into tree to manage the data of every node and what relates to what as know every parent with his children  
    private static Node arr2tree(ArrayList<Node> arr) {
        Stack<Node> stack = new Stack<>();
        for (Node current : arr) {
            if (current.type == NodeType.closetag) {
                Node temp = new Node(NodeType.child, current.data);
                Node top = stack.pop();
                while (top.type != NodeType.opentag) {
                    temp.children.add(top);
                    top = stack.pop();
                }
                top = stack.isEmpty() ? null : stack.peek();
                if (!stack.isEmpty() && top.data.equals(current.data)) {
                    top.type = NodeType.repeatedtag;
                    if (temp.children.size() == 1)
                        top.children.add(temp.children.get(0));
                    else {
                        temp.data = "";
                        if (top.duplicated)
                            top.children.add(temp);
                        else {
                            Node ele = new Node(NodeType.child, "");
                            ele.children = top.children;
                            top.children = new ArrayList<>();
                            top.children.add(ele);
                            top.children.add(temp);
                            top.duplicated = true;
                        }
                    }

                } else if (temp.children.size() == 1 && temp.children.get(0).type == NodeType.root) {
                    temp.type = NodeType.parent;
                    stack.push(temp);
                } else
                    stack.push(temp);

            } else {
                stack.push(current);
            }

        }
        return stack.pop();
    }
// first we should the xml components into the array and try to know when to start ehe node and when to end
    private static ArrayList<Node> xml2arr(String xml) {
        ArrayList<Node> arr = new ArrayList<>();
        for (int i = 0; i < xml.length(); i++) {
            if (xml.charAt(i) == ' ' || xml.charAt(i) == '\n')
                continue;
            StringBuilder sb = new StringBuilder();
            if (xml.charAt(i) == '<') {
                i++;
                boolean ct = false;
                if (xml.charAt(i) == '/') {
                    ct = true;
                    i++;
                }
                while (xml.charAt(i) != '>')
                    sb.append(xml.charAt(i++));
                Node n = new Node(ct ? NodeType.closetag : NodeType.opentag, sb.toString().trim());
                arr.add(n);
            } else {
                while (xml.charAt(i) != '<')
                    sb.append(xml.charAt(i++));
                Node n = new Node(NodeType.root, sb.toString().trim());
                arr.add(n);
                i--;
            }
        }
        return arr;
    }

    private enum NodeType {opentag, closetag, root , parent ,child , repeatedtag} // Node components 
// Every node should have type as in the enum sunction , data , array of children and if it was duplicated and not the first or not 
    private static class Node {
        private NodeType type;
        private String data;
        private ArrayList<Node> children;
        private boolean duplicated = false;

        public Node(NodeType t, String d) {
            type = t;
            data = d;
            children = new ArrayList<>();
        }
    }

}



