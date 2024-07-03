package com.mrb.relind16;

import com.sun.jna.Native;
import com.sun.jna.Platform;

public class Main {
    public static Comm commsLayer;
    public static Relay body;
    public static void main(String[] args) {
        int argc;
        String[] argv;
        commsLayer = Native.load(Platform.isARM()?"comm":"c",Comm.class);
        argc=args.length+1;
        argv= new String[argc];
        argv[0] = "com/mrb/relind16";
        System.arraycopy(args, 0, argv, 1, args.length);
        body = new Relay();
        body.main(argc,argv);

    }
}