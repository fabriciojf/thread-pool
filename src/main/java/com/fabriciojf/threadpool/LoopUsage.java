package com.fabriciojf.threadpool;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @class LoopUsage
 * @version 1.0
 * @since 03/02/2021
 */
public class LoopUsage {

    public static void main(String[] args) {

        final Delay delay = new Delay();
        Callback callback = new Callback() {
            public void run() {
                System.out.println(new Date().toString() + ":> The callback runs");
            }
        };

        delay.setNome("Monitor de Teste");
        delay.setCallback(callback);
        delay.setSegundos(5);
        /**
         * See 
         * RepetitionEnum.SEQUENTIAL,  
         * RepetitionEnum.ONCE,  
         * RepetitionEnum.PERIODIC
         */ 
        delay.setRepeticao(RepetitionEnum.PERIODIC);
        delay.setAtraso(1);
        delay.executar();

        sleep(20);
        delay.interromper();
    }

    /**
     * Aguarda x segundos
     */
    public static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
