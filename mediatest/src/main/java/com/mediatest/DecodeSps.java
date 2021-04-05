package com.mediatest;

/**
 * 手写解码sps
 */
public class DecodeSps {
    public static void main(String[] args) {

        int nStartBits = 3;
        byte data = 5 & 0xff;//转化成一个字节0000 0101
        int zeroNum = 0;
        //sps 只有后5位是哥伦布编码 0x80 = 1000 0000, 0x80>>3 = 0001 0000   data&(0x80>>3)意思就是取这一位上的byte值
        while ((data &(0x80 >> nStartBits)) == 0) {
            zeroNum++;
            nStartBits++;
        }

        System.out.print("" + zeroNum + " " + nStartBits);


    }
}
