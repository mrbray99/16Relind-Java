package com.mrb.relind16;

import com.sun.jna.Pointer;

public interface Comm extends com.sun.jna.Library{
    int i2cSetup(int stack);
    int i2cMem8Read(int dev, int add, Pointer buff,int size);
    int i2cMem8Write(int dev, int add, Pointer buff,int size);
}
