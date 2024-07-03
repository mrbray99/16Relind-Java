package com.mrb.relind16;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.util.concurrent.TimeUnit;

public class RelayThread implements Runnable{
    private final short[] relayOrder;
    private int result;
    private final Relay relay;
    private final int dev;
    public RelayThread(Relay relay,int dev, short[] relayOrder){
        super();
        this.relay = relay;
        this.dev=dev;
        this.relayOrder = relayOrder;
    }
    @Override
    public void run(){
        short valR;
        short relVal;
        int retry;
        result = 0;
        Pointer valRAdd = new Memory(4);
        while (result==0){
            {
                for (int i = 0; i < Constants.RELAY_CH_NR_MAX; i++)
                {

                    valR = 0;
                    relVal = (short)(1 << (relayOrder[i] - 1));

                    retry = Constants.RETRY_TIMES;
                    while ( (retry > 0) && ( (valR & relVal) == 0))
                    {
                        if (Constants.OK != relay.relayChSet(dev, relayOrder[i], Constants.OutStateEnumType.ON))
                        {
                            retry = 0;
                            break;
                        }

                        if (Constants.OK != relay.relayGet(dev, valRAdd))
                        {
                            retry = 0;
                        }
                        valR = valRAdd.getShort(0);
                    }
                    if (retry == 0)
                    {
                        System.out.println("Fail to write relay\n");
                        System.exit(1);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(150L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (int i = 0; i < Constants.RELAY_CH_NR_MAX; i++)
                {

                    valR = (short)0xffff;
                    relVal = (short)(1 << (relayOrder[i] - 1));
                    retry = Constants.RETRY_TIMES;
                    while ( (retry > 0) && ( (valR & relVal) != 0))
                    {
                        if (Constants.OK != relay.relayChSet(dev, relayOrder[i], Constants.OutStateEnumType.OFF))
                        {
                            retry = 0;
                        }
                        if (Constants.OK != relay.relayGet(dev,valRAdd))
                        {
                            retry = 0;
                        }
                        valR = valRAdd.getShort(0);
                    }
                    if (retry == 0)
                    {
                        System.out.println("Fail to write relay\n");
                        System.exit(1);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(150L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void setRelayResult(int result){
        this.result = result;
    }
}
