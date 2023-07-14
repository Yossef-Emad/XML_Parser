package com.example.ds_xml;

import java.util.ArrayList;

public class Node {

    public Node left;
    public Node right;
    Integer data;
Character chData;
 Node left_node=null;
 Node right_node=null;
 Node(char chData,int data){
     this.chData=chData;
     this.data=data;
 }
 Node(){}
public Node(Character chData,Integer data,Node left,Node right){
    this.chData=chData;
    this.data=data;
    this.left_node=left;
    this.right_node=right;
}




}
