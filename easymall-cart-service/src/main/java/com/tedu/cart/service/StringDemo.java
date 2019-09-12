package com.tedu.cart.service;

import java.util.Arrays;

public class StringDemo {
    public static void main(String[] args) {
        String[] this_is_abcs = listReserv("this is abc");
        char[] str = resvers(this_is_abcs[0]);
        this_is_abcs[0]= new String(str);
        System.out.println(Arrays.toString(this_is_abcs));
    }
    private static char[] resvers(String str){
        char[] iniString = str.toCharArray();
        int start = 0;
        int end = iniString.length -1;
        char temp;
        while (start < end) {
            temp=iniString[start];
            iniString[start]=iniString[end];
            iniString[end] = temp;
            start++;
            end--;
        }
        return iniString;
    }
    private static String[] listReserv(String str){
        String[] strings = str.split(" ");
        int start =0;
        int end =strings.length-1;
        while (start<end){
            String temp = strings[start];
            strings[start]=strings[end];
            strings[end] = temp;
            start++;
            end--;
        }
        return strings;
    }
}

