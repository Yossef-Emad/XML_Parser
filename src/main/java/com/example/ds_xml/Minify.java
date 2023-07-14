package com.example.ds_xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Minify {
    String minify=new String();
    File file;
    Scanner sc;
    Minify() throws FileNotFoundException {

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
    public static String toMinify(String s)
    {
        String y = "";
        s=s.replaceAll(">\n",">");
        s=s.replaceAll("\n<","<");
        s=s.replaceAll("\n","");
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)!=' '){
                y += s.charAt(i);
            }
        }
        return y;
    }
public static void main(String[]args){
        String d="<users><user><id>1</id><name>AhmedAli</name><posts><post><body>Loremipsumdolorsitamet,consecteturadipiscingelit,seddoeiusmodtemporincididuntutlaboreetdoloremagnaaliqua.Ucurtlrir>rili.iam,quisnostrudexercitationullamcolaborisnisiutaliquipexeacommodoconsequat.</body><topics><topic>economy</topic><topic>finance</topic></topics></post><post><body>Loremipsumdolorsitamet,consecteturadipiscingelit,seddoeiusmodtemporincididuntutlaboreetdoloremagnaaliqua.Utenimadminimveniam,quisnostrudexercitationullamcolaborisnisiutaliquipexeacommodoconsequat.</body><topics><topic>solar_energy</topic></topictsiopdarsiopdq/<followers><follower><id>2</id></follower><follower><id>3</id></follower></followers></user><user><id>2</id><name>Yassied>usae>name><posts><post><body>Loremipsumdolorsitamet,consecteturadipiscingelit,seddoeiusmodcrou>iocididuntutlaboreetdoloremagnaaliqua.Utenima>ipiue/tph>ooquisnostrudexi<itationullamcolaborisnisiutaliquipexeacommodoconsequat.</body><topict,p>rtil<i<ation</topic></topics></post></posts><followers><follower><id>1</id></followrmditglawers></user><user><id>3</id><name>MohamedSherif</nammopoa<<post><body>Loremipsumdolorsitamet,consecw>radipiscingelit,seddoeiusmodtemporincididuntutlaboreetdoloremagnaaliqua.Utenimadminimveniam,quisnostrudexercitationullamcolaborisnisiutaliquipexeacommodoconsequat.</body><topics><topic>sports</topic></topics></post></posts><followers><follower><id>1</id></followrmditglawers></user></users><users>\n" +
                "<user>\n" +
                "<id>\n" +
                "1</id>\n" +
                "<name>\n" +
                "AhmedAli</name>\n" +
                "<posts>\n" +
                "<post>\n" +
                "<body>\n" +
                "Loremipsumdolorsitamet,consecteturadipiscingelit,seddoeiusmodtemporincididuntutlaboreetdoloremagnaaliqua.Ucurtlrir>\n" +
                "rili.iam,quisnostrudexercitationullamcolaborisnisiutaliquipexeacommodoconsequat.</body>\n" +
                "<topics>\n" +
                "<topic>\n" +
                "economy</topic>\n" +
                "<topic>\n" +
                "finance</topic>\n" +
                "</topics>\n" +
                "</post>\n" +
                "<post>\n" +
                "<body>\n" +
                "Loremipsumdolorsitamet,consecteturadipiscingelit,seddoeiusmodtemporincididuntutlaboreetdoloremagnaaliqua.Utenimadminimveniam,quisnostrudexercitationullamcolaborisnisiutaliquipexeacommodoconsequat.</body>\n" +
                "<topics>\n" +
                "<topic>\n" +
                "solar_energy</topic>\n" +
                "</topictsiopdarsiopdq/<followers>\n" +
                "<follower>\n" +
                "<id>\n" +
                "2</id>\n" +
                "</follower>\n" +
                "<follower>\n" +
                "<id>\n" +
                "3</id>\n" +
                "</follower>\n" +
                "</followers>\n" +
                "</user>\n" +
                "<user>\n" +
                "<id>\n" +
                "2</id>\n" +
                "<name>\n" +
                "Yassied>\n" +
                "usae>\n" +
                "name>\n" +
                "<posts>\n" +
                "<post>\n" +
                "<body>\n" +
                "Loremipsumdolorsitamet,consecteturadipiscingelit,seddoeiusmodcrou>\n" +
                "iocididuntutlaboreetdoloremagnaaliqua.Utenima>\n" +
                "ipiue/tph>\n" +
                "ooquisnostrudexi<itationullamcolaborisnisiutaliquipexeacommodoconsequat.</body>\n" +
                "<topict,p>\n" +
                "rtil<i<ation</topic>\n" +
                "</topics>\n" +
                "</post>\n" +
                "</posts>\n" +
                "<followers>\n" +
                "<follower>\n" +
                "<id>\n" +
                "1</id>\n" +
                "</followrmditglawers>\n" +
                "</user>\n" +
                "<user>\n" +
                "<id>\n" +
                "3</id>\n" +
                "<name>\n" +
                "MohamedSherif</nammopoa<<post>\n" +
                "<body>\n" +
                "Loremipsumdolorsitamet,consecw>\n" +
                "radipiscingelit,seddoeiusmodtemporincididuntutlaboreetdoloremagnaaliqua.Utenimadminimveniam,quisnostrudexercitationullamcolaborisnisiutaliquipexeacommodoconsequat.</body>\n" +
                "<topics>\n" +
                "<topic>\n" +
                "sports</topic>\n" +
                "</topics>\n" +
                "</post>\n" +
                "</posts>\n" +
                "<followers>\n" +
                "<follower>\n" +
                "<id>\n" +
                "1</id>\n" +
                "</followrmditglawers>\n" +
                "</user>\n" +
                "</users>\n";
        System.out.println(xmlRows(d));
        System.out.println(toMinify(d));
}

}
