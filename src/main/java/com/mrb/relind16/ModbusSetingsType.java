package com.mrb.relind16;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class ModbusSetingsType {
    int mbBaud; //24 bits
    int mbType; //4 bits
    int mbParity; //2 bits
    int mbStopB; //2 bits
    Integer add; //8bits

    public int getMbBaud() {
        return mbBaud;
    }

    public void setMbBaud(int mbBaud) {
        this.mbBaud = mbBaud;
    }

    public int getMbType() {
        return mbType;
    }

    public void setMbType(int mbType) {
        this.mbType = mbType;
    }

    public int getMbParity() {
        return mbParity;
    }

    public void setMbParity(int mbParity) {
        this.mbParity = mbParity;
    }

    public int getMbStopB() {
        return mbStopB;
    }

    public void setMbStopB(int mbStopB) {
        this.mbStopB = mbStopB;
    }

    public int getAdd() {
        return add;
    }

    public void setAdd(int add) {
        this.add = add;
    }
    public void getSettings(Pointer memory){
        int tmpBaud = mbBaud<<8; // shift baud to first 3 bytes
        int tmpStopB = mbStopB<<6;
        tmpBaud |= tmpStopB;
        int tmpParity = mbParity<<4;
        tmpBaud |= tmpParity;
        tmpBaud |= mbType;
        byte [] bytes = ByteBuffer.allocate(4).putInt(tmpBaud).array();
        memory.write(0,bytes,0,4);
        memory.setByte(4,add.byteValue());
    }
    public void setSettings(Pointer memory){
        byte[] bytes = memory.getByteArray(0,5);
        int tmpBaud =((int)bytes[0]);
        tmpBaud = tmpBaud<<16;
        if (tmpBaud<0)
            tmpBaud = tmpBaud^0xFFFF0000;
        mbBaud = tmpBaud;
        tmpBaud=((int)bytes[1]);
        tmpBaud = tmpBaud<<8;
        if (tmpBaud<0)
            tmpBaud = tmpBaud^0xFFFF0000;
        mbBaud += tmpBaud;
        mbBaud+=bytes[2];
        mbType = bytes[3]&0x0F;
        mbParity = (bytes[3]&0x30)>>4;
        mbStopB = (bytes[3]&0xc0)>>6;
        add = (int)bytes[4];
    }
    public int getSettingsSize(){
        return 5;
    }
}
