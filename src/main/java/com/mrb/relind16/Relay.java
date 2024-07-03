package com.mrb.relind16;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import javax.swing.*;
import java.io.*;
import java.util.BitSet;

public class Relay {


    CliCmdType CMD_HELP = new CliCmdType(
            "-h", 1, (this::doHelp),
            "\t-h           Display the list of command options or one command option details\n",
            "\tUsage:       16relind -h    Display command options list\n",
            "\tUsage:       16relind -h <param>   Display help for <param> command option\n",
            "\tExample:     16relind -h write    Display help for \"write\" command option\n");

    CliCmdType CMD_VERSION = new CliCmdType("-v", 1, ((a, b) -> doVersion(a, b)),
            "\t-v           Display the version number\n",
            "\tUsage:       16relind -v\n", "",
            "\tExample:     16relind -v  Display the version number\n");

    CliCmdType CMD_WAR = new CliCmdType("-warranty", 1, ((a, b) -> doWarranty(a, b)),
            "\t-warranty    Display the warranty\n",
            "\tUsage:       16relind -warranty\n", "",
            "\tExample:     16relind -warranty  Display the warranty text\n");


    CliCmdType CMD_LIST = new CliCmdType("list", 1, ((a, b) -> doList(a, b)),
            "\t-list:       List all 16relind boards connected, returnsb oards no and stack level for every board\n",
            "\tUsage:       16relind -list\n", "",
            "\tExample:     16relind -list display: 1,0 \n");


    CliCmdType CMD_WRITE = new CliCmdType("write", 2, ((a, b) -> doRelayWrite(a, b)),
            "\twrite:       Set relays On/Off\n",
            "\tUsage:       16relind <id> write <channel> <on/off>\n",
            "\tUsage:       16relind <id> write <value>\n",
            "\tExample:     16relind 0 write 2 On; Set com.sdr.cdl.relays. #2 on Board #0 On\n");

    CliCmdType CMD_READ = new CliCmdType("read", 2, ((a, b) -> doRelayRead(a, b)),
            "\tread:        Read relays status\n",
            "\tUsage:       16relind <id> read <channel>\n",
            "\tUsage:       16relind <id> read\n",
            "\tExample:     16relind 0 read 2; Read Status of com.sdr.cdl.relays. #2 on Board #0\n");

    CliCmdType CMD_LED_BLINK = new CliCmdType("pled", 2, ((a, b) -> doLedSet(a, b)),
            "\tpled:        Set the power led mode (blink | on | off) \n",
            "\tUsage:       16relind <id> pled <blink/off/on>\n", "",
            "\tExample:     16relind 0 pled on; Set power led to always on state \n");

    CliCmdType CMD_TEST = new CliCmdType("test", 2, ((a, b) -> doTest(a, b)),
            "\ttest:        Turn ON and OFF the relays until press a key\n",
            "\tUsage:       16relind <id> test\n", " ",
            "\tExample:     16relind 0 test\n");

    CliCmdType CMD_WDT_SET_OFF_PERIOD = new CliCmdType(
            "wdtopwr", 2, ((a,b)->doWdtSetOffPeriod(a,b)),
            "\twdtopwr:	Set the watchdog off period in seconds (max 48 days), This is the time that watchdog mantain Raspberry turned off \n",
            "\tUsage:		16relind <stack> wdtopwr <val> \n", "",
            "\tExample:		16relind 0 wdtopwr 10; Set the watchdog off interval on Board #0 at 10 seconds \n");
    CliCmdType CMD_WDT_SET_INIT_PERIOD = new CliCmdType(
            "wdtipwr", 2, ((a,b)->doWdtSetInitPeriod(a,b)),
            "\twdtipwr:	Set the watchdog initial period in seconds, This period is loaded after power cycle, giving Raspberry time to boot\n",
            "\tUsage:		16relind <stack> wdtipwr <val> \n", "",
            "\tExample:		16relind 0 wdtipwr 10; Set the watchdog timer initial period on Board #0 at 10 seconds \n");
    CliCmdType CMD_WDT_RELOAD =new CliCmdType("wdtr", 2, ((a,b)-> doWdtReload(a,b)),
            "\twdtr:		Reload the watchdog timer and enable the watchdog if is disabled\n",
            "\tUsage:		16relind <stack> wdtr\n","",
            "\tExample:		16relind 0 wdtr; Reload the watchdog timer on Board #0 with the period \n"
    );
    CliCmdType CMD_WDT_SET_PERIOD = new CliCmdType("wdtpwr", 2,((a,b)->doWdtSetPeriod(a,b)),
            "\twdtpwr:		Set the watchdog period in seconds, reload command must be issue in this interval to prevent Raspberry Pi power off\n",
            "\tUsage:		16relind <stack> wdtpwr <val> \n", "",
            "\tExample:		16relind 0 wdtpwr 10; Set the watchdog timer period on Board #0 at 10 seconds \n");
    String usage = "Usage:	 java -jar 16relind.jar -h <command>\n" +
            "         16relind -v\n" +
            "         16relind -warranty\n" +
            "         16relind -list\n" +
            "         16relind <id> write <channel> <on/off>\n" +
            "         16relind <id> write <value>\n" +
            "         16relind <id> read <channel>\n" +
            "         16relind <id> read\n" +
            "         16relind <id> test\n" +
            "Where: <id> = Board level id = 0..7\n" +
            "Type java -jar 16relind.jar  -h <command> for more help"; // No trailing newline needed here.

    String warranty =
            "	       Copyright (c) 2016-2020 Sequent Microsystems and (c) 2024 - Mike Bray\n" +
                    "                                                             \n" +
                    "       This software is a Java implementation of the          \n" +
                    "       Sequent Microsystems C software for the 16 relay       \n" +
                    "       HAT. The Java software has been created by Mike Bray.   \n" +
                    "                                                             \n" +
                    "                                                             \n" +
                    "		This program is free software; you can redistribute it and/or modify\n" +
                    "		it under the terms of the GNU Leser General Public License as published\n" +
                    "		by the Free Software Foundation, either version 3 of the License, or\n" +
                    "		(at your option) any later version.\n" +
                    "                                    \n" +
                    "		This program is distributed in the hope that it will be useful,\n" +
                    "		but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                    "		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                    "		GNU Lesser General Public License for more details.\n" +
                    "			\n" +
                    "		You should have received a copy of the GNU Lesser General Public License\n" +
                    "		along with this program. If not, see <http://www.gnu.org/licenses/>.";
   CliCmdType CMD_WDT_GET_OFF_PERIOD = new CliCmdType(
            "wdtoprd", 2,((a,b)->doWdtGetOffPeriod(a,b)),
            "\twdtoprd:	Get the watchdog off period in seconds (max 48 days), This is the time that watchdog mantain Raspberry turned off \n",
            "\tUsage:		16relind <stack> wdtoprd \n", "",
            "\tExample:		16relind 0 wdtoprd; Get the watchdog off period on Board #0\n");
    CliCmdType CMD_RS485_WRITE = new CliCmdType(
            "cfg485wr", 2, ((a,b)->doRs485Write(a,b)),
            "\tcfg485wr:    Write the RS485 communication settings\n",
            "\tUsage:      16relind <id> cfg485wr <mode> <baudrate> <stopBits> <parity> <slaveAddr>\n",
            "",
            "\tExample:		 16relind 0 cfg485wr 1 9600 1 0 1; Write the RS485 settings on Board #0 \n\t\t\t(mode = Modbus RTU; baudrate = 9600 bps; stop bits one; parity none; modbus slave address = 1)\n");
    CliCmdType CMD_WDT_GET_PERIOD = new CliCmdType("wdtprd", 2,((a,b)->doWdtGetPeriod(a,b)),
            "\twdtprd:		Get the watchdog period in seconds, reload command must be issue in this interval to prevent Raspberry Pi power off\n",
            "\tUsage:		16relind <stack> wdtprd \n", "",
            "\tExample:		16relind 0 wdtprd; Get the watchdog timer period on Board #0\n");
    CliCmdType CMD_WDT_GET_INIT_PERIOD = new CliCmdType(
            "wdtiprd", 2, ((a,b)->doWdtGetInitPeriod(a,b)),
            "\twdtiprd:	Get the watchdog initial period in seconds. This period is loaded after power cycle, giving Raspberry time to boot\n",
            "\tUsage:		16relind <stack> wdtiprd \n", "",
            "\tExample:		16relind 0 wdtiprd; Get the watchdog timer initial period on Board #0\n");
    CliCmdType CMD_RS485_READ = new CliCmdType("cfg485rd", 2, ((a,b)->doRs485Read(a,b)),
            "\tcfg485rd:    Read the RS485 communication settings\n",
            "\tUsage:      16relind <id> cfg485rd\n", "",
            "\tExample:		16relind 0 cfg485rd; Read the RS485 settings on Board #0\n");
    CliCmdType[] gCmdArray = {CMD_HELP, CMD_WAR, CMD_VERSION, CMD_LIST,
            CMD_WRITE, CMD_READ, CMD_TEST, CMD_LED_BLINK, CMD_WDT_GET_INIT_PERIOD,
            CMD_WDT_GET_OFF_PERIOD, CMD_WDT_GET_PERIOD, CMD_WDT_RELOAD,
            CMD_WDT_SET_INIT_PERIOD, CMD_WDT_SET_OFF_PERIOD, CMD_WDT_SET_PERIOD,
            CMD_RS485_READ, CMD_RS485_WRITE,null};

    short relayToIO(int relay) {
        byte i;
        short val = 0;
        for (i = 0; i < 16; i++) {
            if ((relay & (1 << i)) != 0)
                val += Constants.relayMaskRemap[i];
        }
        return val;
    }

    short IOToRelay(int io) {
        byte i;
        short val = 0;
        for (i = 0; i < 16; i++) {
            if ((io & Constants.relayMaskRemap[i]) != 0) {
                val += 1 << i;
            }
        }
        return val;
    }

    public int relayChSet(int dev, int channel, Constants.OutStateEnumType state) {
        int resp;
        byte[] buff = new byte[2];
        Pointer buffAdd = new Memory(2);
        BitSet reg=new BitSet(16);
        short val = 0;

        if ((channel < Constants.CHANNEL_NR_MIN) || (channel > Constants.RELAY_CH_NR_MAX)) {
            System.out.println("Invalid relay nr!\n");
            return Constants.ERROR;
        }
        if (Constants.FAIL == Main.commsLayer.i2cMem8Read(dev, Constants.RELAY16_INPORT_REG_ADD, buffAdd, 2)) {
            return Constants.FAIL;
        }
        byte[] regValue;
        val = buffAdd.getShort(0);
        switch (state) {
            case OFF:
                val &= ~ (1 << Constants.relayChRemap[channel - 1]);
                buffAdd.setShort(0,val);
                resp = Main.commsLayer.i2cMem8Write(dev, Constants.RELAY16_OUTPORT_REG_ADD, buffAdd, 2);
                break;
            case ON:
                val |= 1 << Constants.relayChRemap[channel - 1];
                buffAdd.setShort(0,val);
                resp = Main.commsLayer.i2cMem8Write(dev, Constants.RELAY16_OUTPORT_REG_ADD, buffAdd, 2);
                break;
            default:
                System.out.println("Invalid relay state!\n");
                return Constants.ERROR;
        }
        return resp;
    }

    public int relayChGet(int dev, int channel, Pointer state) {
        short val;
        Pointer buffAdd = new Memory(2);
        if (state == null) {
            return Constants.ERROR;
        }

        if ((channel < Constants.CHANNEL_NR_MIN) || (channel > Constants.RELAY_CH_NR_MAX)) {
            System.out.println("Invalid relay nr!\n");
            return Constants.ERROR;
        }

        if (Constants.FAIL == Main.commsLayer.i2cMem8Read(dev, Constants.RELAY16_INPORT_REG_ADD, buffAdd, 2)) {
            return Constants.ERROR;
        }

        val=buffAdd.getShort(0);
        if((val & (1 << Constants.relayChRemap[channel - 1]))!=0){
            state.setString(0,Constants.OutStateEnumType.ON.toString());
        } else {
            state.setString(0,Constants.OutStateEnumType.OFF.toString());
        }
        return Constants.OK;
    }

    public int relaySet(int dev, int val) {
        short rVal;
        Pointer buffAdd = new Memory(2);

        rVal = relayToIO(0xffff & val);
        buffAdd.setShort(0,rVal);

        return Main.commsLayer.i2cMem8Write(dev, Constants.RELAY16_OUTPORT_REG_ADD, buffAdd, 2);
    }

    public int relayGet(int dev, Pointer valRAdd) {
        short rVal;
        Pointer buffAdd = new Memory(2);

        if (valRAdd == null) {
            return Constants.ERROR;
        }
        if (Constants.FAIL == Main.commsLayer.i2cMem8Read(dev, Constants.RELAY16_INPORT_REG_ADD, buffAdd, 2)) {
            return Constants.ERROR;
        }
        rVal = buffAdd.getShort(0);
        valRAdd.setShort(0,IOToRelay(rVal));
        return Constants.OK;
    }

    int doBoardInit(int stack) {
        int dev;
        int add;
        byte[] buff;
        Pointer buffAdd = new Memory(8);
        if ((stack < 0) || (stack > 7)) {
            System.out.println("Invalid stack level [0..7]!");
            return Constants.ERROR;
        }
        add = (stack + Constants.RELAY16_HW_I2C_BASE_ADD) ^ 0x07;
        dev = Main.commsLayer.i2cSetup(add);
        if (dev == -1) {
            return Constants.ERROR;

        }
        if (Constants.ERROR == Main.commsLayer.i2cMem8Read(dev, Constants.RELAY16_CFG_REG_ADD, buffAdd, 1)) {
            add = (stack + Constants.RELAY16_HW_I2C_ALTERNATE_BASE_ADD) ^ 0x07;
            dev = Main.commsLayer.i2cSetup(add);
            if (dev == -1) {
                return Constants.ERROR;
            }
            if (Constants.ERROR == Main.commsLayer.i2cMem8Read(dev, Constants.RELAY16_CFG_REG_ADD, buffAdd, 1)) {
                System.out.println("16relind board id " + stack + " not detected\n");
                return Constants.ERROR;
            }
        }
        buff = buffAdd.getByteArray(0,8);
        if (buff[0] != 0) //non initialized I/O Expander
        {
            // make all I/O pins output
            buff[0] = 0;
            buff[1] = 0;
            buffAdd.setByte(0,buff[0]);
            buffAdd.setByte(1,buff[1]);
            if (0 > Main.commsLayer.i2cMem8Write(dev, Constants.RELAY16_CFG_REG_ADD, buffAdd, 2)) {
                return Constants.ERROR;
            }
            // put all pins in 0-logic state
            buff[0] = 0;
            buffAdd.setByte(0,buff[0]);
            if (0 > Main.commsLayer.i2cMem8Write(dev, Constants.RELAY16_OUTPORT_REG_ADD, buffAdd, 2)) {
                return Constants.ERROR;
            }
        }

        return dev;
    }

    int boardCheck(int hwAdd) {
        int dev;
        Pointer buffAdd = new Memory(8);

        hwAdd ^= 0x07;
        dev = Main.commsLayer.i2cSetup(hwAdd);
        if (dev == -1) {
            return Constants.FAIL;
        }
        if (Constants.ERROR == Main.commsLayer.i2cMem8Read(dev, Constants.RELAY16_CFG_REG_ADD, buffAdd, 1)) {
            return Constants.ERROR;
        }
        return Constants.OK;
    }

    /*
     * doRelayWrite:
     *	Write coresponding relay channel
     **************************************************************************************
     */
    void doRelayWrite(int argc, String[] argv) {
        Integer pin;
        Constants.OutStateEnumType state = Constants.OutStateEnumType.STATE_COUNT;
        int val = 0;
        int dev = 0;
        Constants.OutStateEnumType stateR = Constants.OutStateEnumType.STATE_COUNT;
        int valR = 0;
        Pointer valRAdd = new Memory(2);
        Pointer stateRAdd   = new Memory(4);
        int retry = 0;

        if ((argc != 5) && (argc != 4)) {
            System.out.println("Usage: 16relind <id> write <relay number> <on/off> \n");
            System.out.println("Usage: 16relind <id> write <relay reg value> \n");
            System.exit(1);
        }

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0) {
            System.exit(1);
        }
        if (argc == 5) {
            pin = (Integer.parseInt(argv[3]));
            if ((pin < Constants.CHANNEL_NR_MIN) || (pin > Constants.RELAY_CH_NR_MAX)) {
                System.out.println("com.sdr.cdl.relays. number value out of range\n");
                System.exit(1);
            }

            if ((argv[4].equalsIgnoreCase("up"))
                    || (argv[4].equalsIgnoreCase("on")))
                state = Constants.OutStateEnumType.ON;
            else if ((argv[4].equalsIgnoreCase("down"))
                    || ((argv[4].equalsIgnoreCase("off"))))
                state = Constants.OutStateEnumType.OFF;

            else
            {
                if ((Integer.parseInt(argv[4]) >= Constants.OutStateEnumType.STATE_COUNT.ordinal()) || (Integer.parseInt(argv[4]) < 0)) {
                    System.out.println("Invalid relay state!\n");
                    System.exit(1);
                }
                Constants.OutStateEnumType stateType=Constants.OutStateEnumType.ON;
                state = (Constants.OutStateEnumType) stateType.findState(Integer.valueOf(argv[4]));
            }

            retry = Constants.RETRY_TIMES;

            while ((retry > 0) && (stateR != state)) {
                if (Constants.OK != relayChSet(dev, pin, state)) {
                    System.out.println("Fail to write relay\n");
                    System.exit(1);
                }
                if (Constants.OK != relayChGet(dev, pin, stateRAdd))
                {
                    System.out.println("Fail to read relay\n");
                    System.exit(1);
                }
                stateR = Constants.OutStateEnumType.valueOf(stateRAdd.getString(0));
                retry--;
            }
            if (retry < Constants.RETRY_TIMES) {
                System.out.println("retry 3 times\n");
            }
            if (retry == 0) {
                System.out.println("Fail to write relay\n");
                System.exit(1);
            }
        } else {
            try {
                val = Integer.parseInt(argv[3]);
                if (val < 0 || val > 255) {
                    System.out.println("Invalid relay value\n");
                    System.exit(1);
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid relay value\n");
                System.exit(1);
                }

            retry = Constants.RETRY_TIMES;

            valR = -1;
            while ((retry > 0) && (valR != val)) {

                if (Constants.OK != relaySet(dev, val)) {
                    System.out.println("Fail to write relay!\n");
                    System.exit(1);
                }
                if (Constants.OK != relayGet(dev,valRAdd))
                {
                    System.out.println("Fail to read relay!\n");
                    System.exit(1);
                }
                valR = valRAdd.getShort(0);
            }
            if (retry == 0) {
                System.out.println("Fail to write relay!\n");
                System.exit(1);
            }
        }
    }

    /*
     * doRelayRead:
     *	Read relay state
     ******************************************************************************************
     */
    void doRelayRead(int argc,String []argv) {
        Integer pin = 0;
        byte pinByte;
        int val = 0;
        int dev = 0;
        Constants.OutStateEnumType state = Constants.OutStateEnumType.STATE_COUNT;
        Pointer stateRAdd = new Memory(4);
        Pointer valRAdd = new Memory(2);

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0) {
            System.exit(1);
        }

        if (argc == 4) {
            pin = Integer.parseInt(argv[3]);
            if ((pin < Constants.CHANNEL_NR_MIN) || (pin > Constants.RELAY_CH_NR_MAX)) {
                System.out.println("com.sdr.cdl.relays. number value out of range!\n");
                System.exit(1);
            }
            pinByte=pin.byteValue();
            if (Constants.OK != relayChGet(dev, pinByte,stateRAdd))
            {
                System.out.println("Fail to read!\n");
                System.exit(1);
            }
            state = Constants.OutStateEnumType.valueOf(stateRAdd.getString(0));
            System.out.println(state.getState()+"\n");
        } else if (argc == 3) {
            if (Constants.OK != relayGet(dev,valRAdd))
            {
                System.out.println("Fail to read!\n");
                System.exit(1);
            }
            val = valRAdd.getShort(0);
            System.out.println(val+"\n");
        } else {
            System.out.println("Usage: "+argv[0] +" read relay value\n");
            System.exit(1);
        }
    }

    void doLedSet(int argc, String[] argv) {
        int dev = 0;
        byte[] buff=new byte[ 2];
        Pointer buffAdd = new Memory(2);

        if (argc == 4) {
            dev = doBoardInit(Integer.parseInt(argv[1]));
            if (dev <= 0) {
                System.exit(1);
            }
            if (argv[3].equalsIgnoreCase("on")) {
                buff[0] = 1;
            } else if (argv[3].equalsIgnoreCase( "off")) {
                buff[0] = 2;
            } else if (argv[3].equalsIgnoreCase( "blink")) {
                buff[0] = 0;
            } else {
                System.out.println("Invalid led mode (blink/on/off)\n");
                System.exit(1);
            }
            buffAdd.setByte(0,buff[0]);
            buffAdd.setByte(1,buff[1]);
            if (0 > Main.commsLayer.i2cMem8Write(dev, Constants.I2C_MEM_LED_MODE, buffAdd, 1)) {
                System.out.println(
                        "Fail to write, check if your card version supports the command\n");
                System.exit(1);
            }

        } else {
            System.out.println(CMD_LED_BLINK.usage1);
            System.exit(1);
        }
    }

//************************************************************* WDT *************************************************


void doWdtReload(int argc, String[]argv)
{
    int dev = 0;
    byte[] buff = {0, 0};
    Pointer buffAdd = new Memory(2);

    dev = doBoardInit(Integer.parseInt(argv[1]));
    if (dev <= 0)
    {
        System.exit(1);
    }

    if (argc == 3)
    {
        buff[0] = Constants.WDT_RESET_SIGNATURE;
        if (Constants.OK != Main.commsLayer.i2cMem8Write(dev, Constants.I2C_MEM_WDT_RESET_ADD, buffAdd, 1))
        {
            System.out.println("Fail to write watchdog reset key!\n");
            System.exit(1);
        }
    }
    else
    {
        System.out.println("Invalid params number:\n"+CMD_WDT_RELOAD.usage1);
        System.exit(1);
    }
}



void doWdtSetPeriod(int argc, String[] argv)
{
    int dev = 0;
    short period;
    byte[] buff = {0, 0};
    Pointer buffAdd = new Memory(2);

    dev = doBoardInit(Integer.parseInt(argv[1]));
    if (dev <= 0)
    {
        System.exit(1);
    }

    if (argc == 4)
    {
        period = (short)Integer.parseInt(argv[3]);
        if (0 == period)
        {
            System.out.println("Invalid period!\n");
            System.exit(1);
        }
        buffAdd.setShort(0,period);
        if (Constants.OK !=  Main.commsLayer.i2cMem8Write(dev, Constants.I2C_MEM_WDT_INTERVAL_SET_ADD, buffAdd, 2))
        {
            System.out.println("Fail to write watchdog period!\n");
            System.exit(1);
        }
    }
    else
    {
        System.out.println("Invalid params number:\n"+ CMD_WDT_SET_PERIOD.usage1);
        System.exit(1);
    }

}


    void doWdtGetPeriod(int argc, String[] argv)
    {
        int dev = 0;
        short period;
        byte[] buff= new byte[2];
        Pointer buffAdd = new Memory(2);

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0)
        {
            System.exit(1);
        }

        if (argc == 3)
        {
            if (Constants.OK != Main.commsLayer.i2cMem8Read(dev, Constants.I2C_MEM_WDT_INTERVAL_GET_ADD, buffAdd, 2))
            {
                System.out.println("Fail to read watchdog period!\n");
                System.exit(1);
            }
            period = buffAdd.getShort(0);
            System.out.println((int)period+"\n");
        }
        else
        {
            System.out.println("Invalid params number:\n "+ CMD_WDT_GET_PERIOD.getUsage1());
            System.exit(1);
        }

    }

    void doWdtSetInitPeriod(int argc, String [] argv)
    {
        int dev = 0;
        short period;
        byte[] buff = {0, 0};
        Pointer buffAdd = new Memory(2);

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0)
        {
            System.exit(1);
        }

        if (argc == 4)
        {
            period = (short)Integer.parseInt(argv[3]);
            if (0 == period)
            {
                System.out.println("Invalid period!\n");
                System.exit(1);
            }
            buffAdd.setShort(0,(short)period);
            if (Constants.OK != Main.commsLayer.i2cMem8Write(dev, Constants.I2C_MEM_WDT_INIT_INTERVAL_SET_ADD, buffAdd, 2))
            {
                System.out.println("Fail to write watchdog period!\n");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("Invalid params number:\n "+CMD_WDT_SET_INIT_PERIOD.getUsage1());
            System.exit(1);
        }

    }




    void doWdtGetInitPeriod(int argc, String [] argv)
    {
        int dev = 0;
        short period;
        byte[] buff=new byte[2];
        Pointer buffAdd = new Memory(2);

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0)
        {
            System.exit(1);
        }

        if (argc == 3)
        {
            if (Constants.OK != Main.commsLayer.i2cMem8Read(dev, Constants.I2C_MEM_WDT_INIT_INTERVAL_GET_ADD, buffAdd, 2))
            {
                System.out.println("Fail to read watchdog period!\n");
                System.exit(1);
            }
            period = buffAdd.getShort(0);
            System.out.println((int)period+"\n");
        }
        else
        {
            System.out.println("Invalid params number:\n "+CMD_WDT_GET_INIT_PERIOD.getUsage1());
            System.exit(1);
        }

    }



    void doWdtSetOffPeriod(int argc, String[]argv)
    {
        int dev = 0;
        int period;
        byte[] buff = {0, 0, 0, 0};
        Pointer buffAdd = new Memory(4);

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0)
        {
            System.exit(1);
        }

        if (argc == 4)
        {
            period = Integer.parseInt(argv[3]);
            if ( (0 == period) || (period > Constants.WDT_MAX_OFF_INTERVAL_S))
            {
                System.out.println("Invalid period!\n");
                System.exit(1);
            }
            buffAdd.setInt(0, period);
            if (Constants.OK
                    != Main.commsLayer.i2cMem8Write(dev, Constants.I2C_MEM_WDT_POWER_OFF_INTERVAL_SET_ADD, buffAdd, 4))
            {
                System.out.println("Fail to write watchdog period!\n");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("Invalid params number:\n "+CMD_WDT_SET_OFF_PERIOD.getUsage1());
            System.exit(1);
        }

    }




    void doWdtGetOffPeriod(int argc, String []argv)
    {
        int dev = 0;
        int period;
        byte[] buff= new byte[4];
        Pointer buffAdd = new Memory(4);

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0)
        {
            System.exit(1);
        }

        if (argc == 3)
        {
            if (Constants.OK
                    != Main.commsLayer.i2cMem8Read(dev, Constants.I2C_MEM_WDT_POWER_OFF_INTERVAL_GET_ADD, buffAdd, 4))
            {
                System.out.println("Fail to read watchdog period!\n");
                System.exit(1);
            }
            period=buffAdd.getInt(0);
            System.out.println((int)period+"\n");
        }
        else
        {
            System.out.println("Invalid params number:\n "+CMD_WDT_GET_OFF_PERIOD.getUsage1());
            System.exit(1);
        }

    }

//********************************************** RS485 *******************************************************

    int cfg485Set(int dev, byte mode, int baud, byte stopB, byte parity, byte add)
    {
        ModbusSetingsType settings = new ModbusSetingsType();
        Pointer buff = new Memory(settings.getSettingsSize());

        if (mode > 1)
        {
            System.out.println("Invalid RS485 mode : 0 = disable, 1= Modbus RTU (Slave)!\n");
            return Constants.ERROR;
        }

        if (baud > 921600 || baud < 1200)
        {
            if (mode == 0)
            {
                baud = 38400;
            }
            else
            {
                System.out.println("Invalid RS485 Baudrate [1200, 921600]!\n");
                return Constants.ERROR;
            }
        }

        if (stopB < 1 || stopB > 2)
        {
            if (mode == 0)
            {
                stopB = 1;
            }
            else
            {
                System.out.println("Invalid RS485 stop bits [1, 2]!\n");
                return Constants.ERROR;
            }
        }

        if (parity > 2)
        {
            if (mode == 0)
            {
                parity = 0;
            }
            else
            {
                System.out.println("Invalid RS485 parity 0 = none; 1 = even; 2 = odd! \n");
                return Constants.ERROR;
            }
        }
        if (add < 1)
        {
            if (mode == 0)
            {
                add = 1;
            }
            else
            {
                System.out.println("Invalid MODBUS device address: [1, 255]!\n");
                return Constants.ERROR;
            }
        }
        settings.setMbBaud(baud);
        settings.setMbType(mode);
        settings.setMbParity(parity);
        settings.setMbStopB(stopB);
        settings.setAdd(add);

        settings.getSettings(buff);
        if (Constants.OK != Main.commsLayer.i2cMem8Write(dev, Constants.I2C_MODBUS_SETINGS_ADD, buff, 5))
        {
            System.out.println("Fail to write RS485 settings!\n");
            return Constants.ERROR;
        }
        return Constants.OK;
    }

    int cfg485Get(int dev)
    {
        ModbusSetingsType settings = new ModbusSetingsType();
        Pointer buff = new Memory(settings.getSettingsSize());

        if (Constants.OK != Main.commsLayer.i2cMem8Read(dev, Constants.I2C_MODBUS_SETINGS_ADD, buff, 5))
        {
            System.out.println("Fail to read RS485 settings!\n");
            return Constants.ERROR;
        }
        settings.setSettings(buff);
        System.out.println("<mode> <baudrate> <stopbits> <parity> <add> "+settings.getMbType()+" "+settings.getMbBaud()
                        +" "+settings.getMbStopB()+" "+settings.getMbParity()+" "+settings.getAdd()+"\n");
        return Constants.OK;
    }


    void doRs485Read(int argc, String[]argv)
    {
       int dev = 0;

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0)
        {
            System.exit(1);
        }

        if (argc == 3)
        {
            if (Constants.OK != cfg485Get(dev))
            {
                System.exit(1);
            }
        }
        else
        {
            System.out.println(CMD_RS485_READ.getUsage1());
            System.exit(1);
        }

    }



    void doRs485Write(int argc, String[] argv) {
        int dev = 0;
        byte mode = 0;
        int baud = 1200;
        byte stopB = 1;
        byte parity = 0;
        byte add = 0;

        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0) {
            System.exit(1);
        }
        if (argc == 8) {
            mode = (byte) (0xff & Integer.parseInt(argv[3]));
            baud = Integer.parseInt(argv[4]);
            stopB = (byte) (0xff & Integer.parseInt(argv[5]));
            parity = (byte) (0xff & Integer.parseInt(argv[6]));
            add = (byte) (0xff & Integer.parseInt(argv[7]));
            if (Constants.OK != cfg485Set(dev, mode, baud, stopB, parity, add)) {
                System.exit(1);
            }
            System.out.println("done\n");
        } else {
            System.out.println(CMD_RS485_WRITE.getUsage1());
            System.exit(1);
        }
    }
    void doHelp(int argc, String[]argv)
    {
        int i = 0;
        if (argc == 3)
        {
            while (null != gCmdArray[i])
            {
                if ( (gCmdArray[i].getName() != null))
                {
                    if (argv[2].equalsIgnoreCase(gCmdArray[i].getName()))
                    {
                        System.out.println(gCmdArray[i].getHelp()+" "+ gCmdArray[i].getUsage1()+" " +
                                gCmdArray[i].getUsage2()+" "+gCmdArray[i].getExample());
                        break;
                    }
                }
                i++;
            }
            if (Constants.CMD_ARRAY_SIZE == i)
            {
                System.out.println("Option "+ argv[2] +" not found\n");
                for (i = 0; i < Constants.CMD_ARRAY_SIZE; i++)
                {
                    System.out.println(gCmdArray[i].getHelp());
                }
            }
        }
        else
        {
            while (null != gCmdArray[i])
            {
                System.out.println(gCmdArray[i].getHelp());
                i++;
            }
        }
    }

    void doVersion(int argc, String[]argv)
    {
        System.out.println("16relind v"+Constants.VERSION_BASE+"."+Constants.VERSION_MAJOR+"."+Constants.VERSION_MINOR+" Copyright (c) 2016 - 2020 Sequent Microsystems\n");
        System.out.println("\nThis is free software with ABSOLUTELY NO WARRANTY.\n");
        System.out.println("For details type: 16relind -warranty\n");

    }

    void doList(int argc, String[]argv)
    {
        int[] ids = new int[8];
        int i;
        int cnt = 0;


        for (i = 0; i < 8; i++)
        {
            if (boardCheck(Constants.RELAY16_HW_I2C_BASE_ADD + i) == Constants.OK)
            {
                ids[cnt] = i;
                cnt++;
            }
            else
            {
                if (boardCheck(Constants.RELAY16_HW_I2C_ALTERNATE_BASE_ADD + i) == Constants.OK)
                {
                    ids[cnt] = i;
                    cnt++;
                }
            }
        }
        System.out.println(cnt+" board(s) detected\n");
        if (cnt > 0)
        {
            System.out.println("Id:");
        }
        while (cnt > 0)
        {
            cnt--;
            System.out.println(ids[cnt]);
        }
        System.out.println("\n");
    }

    /*
     * Self test for production
     */
    void doTest(int argc, String[] argv) {
        int dev = 0;
        int i = 0;
        int retry = 0;
        int relVal;
        int valR;
        int relayResult = -1;
        RelayThread runnable;
        Thread thread;
        PrintStream file = null;
        short[] relayOrder = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16};
        dev = doBoardInit(Integer.parseInt(argv[1]));
        if (dev <= 0) {
            System.exit(1);
        }
        if (argc == 4) {
            System.out.println("Outputting results to file not supported\n");
            System.exit(1);
        }
        int result=-1;
        runnable = new RelayThread(this, dev,relayOrder);
        thread = new Thread(runnable);
        if (argv[2].equalsIgnoreCase( "test") ) {
            relVal = 0;
            try {
                thread.start();
            } catch (IllegalStateException e) {
                runnable.setRelayResult(255);
            }
        result = JOptionPane.showConfirmDialog(null,"Are all relays and LEDs turning on and off in sequence?\nPress y for Yes or any key for No....");
        }
        if (result == JOptionPane.YES_OPTION)
        {
            runnable.setRelayResult(255);
            System.out.println("Relay Test ............................ PASS\n");
        }
        else
        {
            System.out.println("Relay Test ............................ FAIL!\n");
        }


    }
    void doWarranty(int argc, String [] argv)
    {
        System.out.println(warranty+"\n");
    }



/*    int waitForI2C(Semaphore sem)
    {
        int semVal = 2;
        struct timespec ts;
        int s = 0;

        semInst.sem_getvalue(sem, semVal);
        System.out.println("Semaphore initial value "+semVal+"\n");
        semVal = 2;
        while (semVal > 0)
        {
            if (clock_gettime(CLOCK_REALTIME, &ts) == -1)
            {
                /* handle error */
               /* System.out.println("Fail to read time \n");
                return -1;
            }
            ts.tv_sec += TIMEOUT_S;
            while ( (s = sem_timedwait(sem, &ts)) == -1 && errno == EINTR)
            continue; /* Restart if interrupted by handler */
            /* semInst.sem_getvalue(sem, semVal);
        }
        semInst.sem_getvalue(sem, semVal);
        System.out.println("Semaphore after wait "+ semVal+"\n");
        return 0;
    }

    int releaseI2C(Semaphore sem)
    {
        int semVal = 2;
        semInst.sem_getvalue(sem, &semVal);
        if (semVal < 1)
        {
            if (semInst.sem_post(sem) == -1)
            {
                System.out.println("Fail to post SMI2C_SEM \n");
                return -1;
            }
        }
        semInst.sem_getvalue(sem, semVal);
        System.out.println("Semaphore after post "+semVal+"\n");
        return 0;
    }*/
int main(int argc, String[]argv)
{
    int i = 0;

    //cliInit();

    if (argc == 1)
    {
        while (null != gCmdArray[i])
        {
            System.out.println(gCmdArray[i].getHelp());
            i++;
        }
        return 1;
    }

        i = 0;
    while (null != gCmdArray[i])
    {
        if ( (gCmdArray[i].getName() != null) && (gCmdArray[i].getNamePos() < argc))
        {
            if ((argv[gCmdArray[i].getNamePos()].equalsIgnoreCase(gCmdArray[i].getName())) )
            {
                gCmdArray[i].getFunc().pFunc(argc, argv);
                return 0;
            }
        }
        i++;
    }
    System.out.println("Invalid command option\n");
    i = 0;
    while (null != gCmdArray[i])
    {
        System.out.println(gCmdArray[i].getHelp());
        i++;
    }
    return 0;
}
}