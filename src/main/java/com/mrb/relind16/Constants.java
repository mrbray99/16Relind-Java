package com.mrb.relind16;

public class Constants {
    public static int OFFSTATE=0;
    public static int ONSTATE=1;
    public static int STATE_STATECOUNT=2;
    public enum OutStateEnumType {
        OFF(OFFSTATE),
        ON(ONSTATE),
        STATE_COUNT(STATE_STATECOUNT);
        private final int state;
        OutStateEnumType(int state) {
            this.state = state;
        }
        public int getState() {
            return state;
        }
        public OutStateEnumType findState(int state){
            for (OutStateEnumType type :OutStateEnumType.values())
                if (type.getState()==state)
                    return type;
            return STATE_COUNT;
        }
    }
    public static int CMD_ARRAY_SIZE=8;
    public static boolean THREAD_SAFE=true;
    public static int[] relayMaskRemap = {0x8000, 0x4000, 0x2000, 0x1000, 0x800, 0x400,
            0x200, 0x100, 0x80, 0x40, 0x20, 0x10, 0x8, 0x4, 0x2, 0x1};
    public static int[] relayChRemap = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
            0};
    public static int RETRY_TIMES=10;
    public static byte RELAY16_HW_I2C_BASE_ADD=0x20;
    public static byte RELAY16_HW_I2C_ALTERNATE_BASE_ADD= 0x38;
    public static byte CHANNEL_NR_MIN=	1;
    public static byte  RELAY_CH_NR_MAX=16;
    public static byte ERROR = -1;
    public static byte OK=0;
    public static byte FAIL=-1;
    public static byte WDT_RESET_SIGNATURE=(byte)202;
    public static int WDT_MAX_OFF_INTERVAL_S= 4147200;
    public static byte RELAY16_INPORT_REG_ADD=0x00;
    public static byte RELAY16_OUTPORT_REG_ADD=0x02;
    public static byte RELAY16_POLINV_REG_ADD=0x04;
    public static byte RELAY16_CFG_REG_ADD=0x06;
    public static int I2C_INPORT_REG_ADD=0;
    public static int I2C_OUTPORT_REG_ADD = 2;
    public static int I2C_POLINV_REG_ADD = 4;
    public static int I2C_CFG_REG_ADD = 6;
    public static int I2C_SW_MOM_ADD = 8;
    public static int I2C_SW_INT_ADD=9;
    public static int I2C_SW_INT_EN_ADD=10;
    public static int I2C_MEM_DIAG_3V3_MV_ADD=11;
    public static int I2C_MEM_DIAG_TEMPERATURE_ADD = I2C_MEM_DIAG_3V3_MV_ADD +2; // 13
    public static int I2C_MEM_DIAG_5V_ADD=14;
    public static int I2C_MEM_WDT_RESET_ADD = I2C_MEM_DIAG_5V_ADD + 2;//16
    public static int I2C_MEM_WDT_INTERVAL_SET_ADD=17;
    public static int I2C_MEM_WDT_INTERVAL_GET_ADD = I2C_MEM_WDT_INTERVAL_SET_ADD + 2; //19
    public static int I2C_MEM_WDT_INIT_INTERVAL_SET_ADD = I2C_MEM_WDT_INTERVAL_GET_ADD + 2;//21
    public static int I2C_MEM_WDT_INIT_INTERVAL_GET_ADD = I2C_MEM_WDT_INIT_INTERVAL_SET_ADD + 2;//23
    public static int I2C_MEM_WDT_RESET_COUNT_ADD = I2C_MEM_WDT_INIT_INTERVAL_GET_ADD + 2;//25
    public static int I2C_MEM_WDT_CLEAR_RESET_COUNT_ADD = I2C_MEM_WDT_RESET_COUNT_ADD + 2;//27
    public static int I2C_MEM_WDT_POWER_OFF_INTERVAL_SET_ADD=28;
    public static int I2C_MEM_WDT_POWER_OFF_INTERVAL_GET_ADD = I2C_MEM_WDT_POWER_OFF_INTERVAL_SET_ADD + 4;//32
    public static int I2C_MODBUS_SETINGS_ADD  = I2C_MEM_WDT_POWER_OFF_INTERVAL_GET_ADD + 4;//36 bytes

    public static int I2C_MEM_CPU_RESET = 0xaa;//170
    public static int I2C_MEM_REVISION_HW_MAJOR_ADD=171;
    public static int I2C_MEM_REVISION_HW_MINOR_ADD=172;
    public static int I2C_MEM_REVISION_MAJOR_ADD=173;
    public static int I2C_MEM_REVISION_MINOR_ADD=174;
    public static int I2C_MEM_LED_MODE = 254;
    public static int SLAVE_BUFF_SIZE = 255;

    public static int VERSION_BASE=	1;
    public static int VERSION_MAJOR=1;
    public static int  VERSION_MINOR=2;
}
