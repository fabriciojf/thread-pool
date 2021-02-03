package com.fabriciojf.threadpool;

/**
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 23/09/2012
 * @version 1.0
 */
public enum RepetitionEnum {

    /**
     * Run just one time
     */
    ONCE,
    
    /**
     * Run in periodic ranges, repecting the delay for new executions 
     * to prevent simultanous executions of this event
     */
    
    PERIODIC,
   
    /**
     * Run in fixed times
     */
    SEQUENTIAL,
}
