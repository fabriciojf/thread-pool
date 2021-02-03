package com.fabriciojf.threadpool;

/**
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 03/10/2012
 * @version 1.0
 */
public class Delay extends LoopProcess {

    private Callback callback;

    public Delay() {
        super("Delay");
    }

    public Delay(Callback callback) {
        super("Delay");
        this.callback = callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        callback.run();
    }

}
