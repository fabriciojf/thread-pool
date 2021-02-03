package com.fabriciojf.threadpool;

/**
 * Callback para postergar a execução de procedimentos. Um callback é
 * basicamente um {@link Runnable} mas com esta função específica de atrasar a
 * execução de parte de um procedimento.
 *
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 03/10/2012
 * @version 1.0
 */
public interface Callback extends Runnable {

}
