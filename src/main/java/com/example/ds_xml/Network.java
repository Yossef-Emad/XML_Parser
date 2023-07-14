package com.example.ds_xml;

import java.io.FilterOutputStream;
import java.util.*;

public class Network {
    Map<Integer, User> users;


    public Network() {
        this.users = new HashMap<>();

    }

    public void addUser(User user) {
        users.put(user.id, user);
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
    public static List<User> getUsers(String xml){
        ArrayList<String>m=xmlRows(xml);
        List<User>k=new ArrayList<>();
        for(int i=0;i<m.size();i++){
            if(m.get(i).equals("<user>")&&m.get(i+1).equals("<id>")&&m.get(i+4).equals("<name>")){
                k.add(new User(Integer.parseInt(m.get(i+2)),m.get(i+5)));
            }
        }
        return k;
    }
    public static User getUser(String xml,int id){
        List<User> k=getUsers(xml);
        for (User s:k) {
            if(s.id==id){return s;}
        }
        return null;
    }
    public static Map <Integer,User> xmlToNetwork(String xml)
    {
        ArrayList<String>m=xmlRows(xml);
        User user=null;
        Post e=null;
        Map<Integer,User> mp=new HashMap<>();
        for(int i=0;i<m.size();i++){
            if(m.get(i).equals("<user>")&&m.get(i+1).equals("<id>")&&m.get(i+4).equals("<name>")){
                user=new User(Integer.parseInt(m.get(i+2)),m.get(i+5));
            }
            else if(m.get(i).equals("<post>")&&m.get(i+1).equals("<body>")){
                e=new Post(m.get(i+2));
            }
           else if(m.get(i).equals("<topic>")){
                e.topics.add(m.get(i+1));
            }
           else if(m.get(i).equals("</post>")){user.posts.add(e);}
           else if(m.get(i).equals("<follower>")&&m.get(i+1).equals("<id>")){
                user.followers.add(getUser(xml,Integer.parseInt(m.get(i+2))));}
           else if(m.get(i).equals("</user>")){mp.put(user.id,user);}
        }
        //for most active
      //  for(Map.Entry<Integer,User> entry: mp.entrySet()){}
        return mp;
    }


    //return user with most followers or(connected for most users)
    public User getMostInfluencer() {
        User mostInfluencer = null;
        int maxFollowers = Integer.MIN_VALUE;
        for (User user : users.values()) {
            if (user.followers.size() > maxFollowers) {
                maxFollowers = user.followers.size();
                mostInfluencer = user;
            }
        }
        return mostInfluencer;
    }
    public static String getMostInfluencer(String xml){
        Network n=new Network();
       n.users =xmlToNetwork(xml);
        User user=n.getMostInfluencer();
        return user.name+" "+user.id;

    }

    //return the most active user on network (that connected for most users)
    public User getMostActive() {
        User mostActive = null;
        int maxFollowings = Integer.MIN_VALUE;
        for (User user : users.values()) {
            int followings = 0;
            for (Post post : user.posts) {
                followings += post.topics.size();
            }
            if (followings > maxFollowings) {
                maxFollowings = followings;
                mostActive = user;
            }
        }
        return mostActive;
    }
    public static String getMostActive(String xml){
        Network n=new Network();
       n.users =xmlToNetwork(xml);
        User user=n.getMostActive();
        return user.name+" "+user.id;
    }
    public void SendXml(String xml){}
//return the mutual followers between two users
    public static List<User> getMutualFollowers(User user1, User user2) {
        List<User> mutualFollowers = new ArrayList<>();
        //System.out.println(user1.followers.get(0).name+" "+user2.followers.get(0).name);
        for (User follower1 : user1.followers) {

            for (User follower2 : user2.followers) {
                if (follower1.id == follower2.id) {
                    mutualFollowers.add(follower1);
                   //System.out.println("sdcxc");
                }
            }
        }
        return mutualFollowers;
    }
    public static String getMutualFollowers(String xml,String id1,String id2){
        Network n=new Network();
        n.users =xmlToNetwork(xml);
        User user1 =n.users.get(Integer.parseInt(id1));
        User user2 =n.users.get(Integer.parseInt(id2));
       List<User>user= getMutualFollowers(user1,user2);
       String k=new String("");
        for (User s:user) {
            k+= s.name+" "+s.id+'\n';

        }
        return k;
    }
//return list of suggested followers for a user
    public static List<User> suggestFollowers(User user) {
        Set<User> suggestedFollowers = new HashSet<>();
        for (User follower : user.followers) {
            for (User subFollower : follower.followers) {
                if (subFollower.id != user.id) {
                    suggestedFollowers.add(subFollower);
                }
            }
        }
        return new ArrayList<>(suggestedFollowers);
    }
    public static String suggestFollowers(String xml,String id){
        Network n=new Network();
        n.users =xmlToNetwork(xml);
        User user1=n.users.get(Integer.parseInt(id));
        List<User> user=suggestFollowers(user1);
        String k=new String("");
        for (User s:user) {
            k+= s.name+" "+s.id+'\n';

        }
        return k;

    }
//return a list of posts that where these word or topic was mentioned
public List<Post> searchPosts(String word) {
    List<Post> foundPosts = new ArrayList<>();
    for (User user : users.values()) {
        for (Post post : user.posts) {
            if (post.body.contains(word)) {
                foundPosts.add(post);
            }
            for (String topic : post.topics) {
                if (topic.contains(word)) {
                    foundPosts.add(post);
                    break;
                }
            }
        }
    }
    return foundPosts;
}
public static String searchPosts(String xml,String word){
    Network n=new Network();
    n.users =xmlToNetwork(xml);
    List<Post> post=n.searchPosts(word);
    String k=new String("");
    for (Post s:post) {
        k+= s.body+" "+s.topics.toString()+'\n';

    }
    return k;

}
    //test
    public static void main(String[]args)throws Exception{
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
                "        </followers>\n" +
                "    </user>\n" +
                "</users>";
        Network n=new Network();
      n.users=xmlToNetwork(xml);
      List<User> h=getMutualFollowers(n.users.get(2),n.users.get(3));
//n.users.get(0).followers.get(0).name
       System.out.println(getMutualFollowers(xml,"2","3"));
    }
}









