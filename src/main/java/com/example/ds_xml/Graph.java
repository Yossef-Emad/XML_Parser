package com.example.ds_xml;

import java.util.*;

public class Graph {
private Map<User, List<User>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public List<User> getNeighbors(User user) {
        return adjacencyList.get(user);
    }

    public int getNumberOfVertices() {
        return adjacencyList.keySet().size();
    }

    public String printGraph() {
        String s=new String("");
        for (Map.Entry<User, List<User>> entry : adjacencyList.entrySet()) {
            s += entry.getKey().name + "-> ";
           // System.out.print(entry.getKey().name + ": ");
            for (User neighbor : entry.getValue()) {
               // System.out.print(neighbor.name + " ");
                s += neighbor.name + " ";
            }
           // System.out.println();
            s+='\n';
        }
        return s;
    }
    public static ArrayList<String> xmlRows(String xml){
        String[] rowsArray = xml.trim().replace(" ", "").replaceAll(">", ">\n").replaceAll("<", "\n<").split("\n");
        ArrayList<String> rows=new ArrayList<>();
        for (String s : rowsArray){
            if (!s.isEmpty()){
                rows.add(s);
            }
        }
        return rows;
    }
    public static boolean IsOpenTag(String Tag) {

        return Tag.charAt(0)=='<'&&Tag.charAt(1)!='/'&&Tag.charAt(Tag.length()-1)=='>';
    }
    public static boolean IsClosedTag(String Tag){
        return Tag.charAt(0)=='<'&&Tag.charAt(1)=='/'&&Tag.charAt(Tag.length()-1)=='>';
    }
    public static boolean IsData(String data){
        if(IsClosedTag(data)||IsOpenTag(data)){return false;}
        return true;
    }
    public static String cutTag(String g){
        String s=g.replace("/","");;
        return s;
    }
    public void addEdge(User source, User destination) {
        if (adjacencyList.containsKey(source)) {
            adjacencyList.get(source).add(destination);
        } else {
            List<User> edges = new ArrayList<>();
            edges.add(destination);
            adjacencyList.put(source, edges);
        }
    }
    //public void addUser(User user) {adjacencyList.put(user);}

    public static Graph xmlToGraph(String xml){
        Graph g=new Graph();
        Map<Integer,User>mp= Network.xmlToNetwork(xml);
        for(Map.Entry<Integer,User> entry: mp.entrySet()){
            for (int i=0;i<entry.getValue().followers.size();i++){
                g.addEdge(mp.get(entry.getValue().followers.get(i).id),entry.getValue());

            }
        }
        return g;
    }



public static void main(String[] args)throws Exception{
        String xml ="<users>\n" +
                "    <user>\n" +
                "        <id>1</id>\n" +
                "        <name>Ahmed Ali</name>\n" +
                "        <posts>\n" +
                "            <post>\n" +
                "                <body>\n" +
                "                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "                </body>\n" +
                "                <topics>\n" +
                "                    <topic>\n" +
                "                        economy\n" +
                "                    </topic>\n" +
                "                    <topic>\n" +
                "                        finance\n" +
                "                    </topic>\n" +
                "                </topics>\n" +
                "            </post>\n" +
                "            <post>\n" +
                "                <body>\n" +
                "                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "                </body>\n" +
                "                <topics>\n" +
                "                    <topic>\n" +
                "                        solar_energy\n" +
                "                    </topic>\n" +
                "                </topics>\n" +
                "            </post>\n" +
                "        </posts>\n" +
                "        <followers>\n" +
                "            <follower>\n" +
                "                <id>2</id>\n" +
                "            </follower>\n" +
                "            <follower>\n" +
                "                <id>3</id>\n" +
                "            </follower>\n" +
                "        </followers>\n" +
                "    </user>\n" +
                "    <user>\n" +
                "        <id>2</id>\n" +
                "        <name>Yasser Ahmed</name>\n" +
                "        <posts>\n" +
                "            <post>\n" +
                "                <body>\n" +
                "                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "                </body>\n" +
                "                <topics>\n" +
                "                    <topic>\n" +
                "                        education\n" +
                "                    </topic>\n" +
                "                </topics>\n" +
                "            </post>\n" +
                "        </posts>\n" +
                "        <followers>\n" +
                "            <follower>\n" +
                "                <id>1</id>\n" +
                "            </follower>\n" +
                "        </followers>\n" +
                "    </user>\n" +
                "    <user>\n" +
                "        <id>3</id>\n" +
                "        <name>Mohamed Sherif</name>\n" +
                "        <posts>\n" +
                "            <post>\n" +
                "                <body>\n" +
                "                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "                </body>\n" +
                "                <topics>\n" +
                "                    <topic>\n" +
                "                        sports\n" +
                "                    </topic>\n" +
                "                </topics>\n" +
                "            </post>\n" +
                "        </posts>\n" +
                "        <followers>\n" +
                "            <follower>\n" +
                "                <id>1</id>\n" +
                "            </follower>\n" +
                "            <follower>\n" +
                "                <id>2</id>\n" +
                "            </follower>\n" +
                "        </followers>\n" +
                "    </user>\n" +
                "</users";

        Graph g= xmlToGraph(xml);
             g.printGraph();
}
}
