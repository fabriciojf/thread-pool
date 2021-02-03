package com.fabriciojf.threadpool;

/**
 * Interface para fábrica de objetos.
 * 
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 06/10/2012
 * @version 1.0
 */
public interface LoopFactory<T> {

    /**
     * LoopFactory uma instância do objeto.
     * 
     * @return O objeto fabricado.
     */
    T fabricar();
}
