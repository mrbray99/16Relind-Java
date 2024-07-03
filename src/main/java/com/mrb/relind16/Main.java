package com.mrb.relind16;

import com.sun.jna.Native;
import com.sun.jna.Platform;

public class Main {
    public static Comm commsLayer;
    public static Relay body;
    private static int argc;
    private static String[] argv;
    public static void main(String[] args) {
        commsLayer = Native.load(Platform.isARM()?"comm":"c",Comm.class);
        argc=args.length+1;
        argv= new String[argc];
        argv[0] = "com/mrb/relind16";
        for (int i=0;i<args.length;i++)
            argv[i+1] = args[i];
        body = new Relay();
        body.main(argc,argv);

    }
}