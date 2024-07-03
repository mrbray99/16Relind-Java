package com.mrb.relind16;



public class CliCmdType
{
    String name;
    int namePos;
    RelayFunction func;
    String help;
    String usage1;
    String usage2;
    String example;
    public CliCmdType (String name, int namePos, RelayFunction func, String help, String usage1, String usage2, String example){
        this.name = name;
        this.namePos = namePos;
        this.func = func;
        this.help = help;
        this.usage1 = usage1;
        this.usage2 = usage2;
        this.example = example;
    }

    public String getName() {
        return name;
    }

    public int getNamePos() {
        return namePos;
    }

    public RelayFunction getFunc() {
        return func;
    }

    public String getHelp() {
        return help;
    }

    public String getUsage1() {
        return usage1;
    }

    public String getUsage2() {
        return usage2;
    }

    public String getExample() {
        return example;
    }
}
