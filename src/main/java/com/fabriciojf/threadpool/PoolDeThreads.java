package com.fabriciojf.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Gerenciador de processos. O gerenciador de processos controla um pool de
 * threads para otimizar a execução dos processos.
 *
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 23/09/2012
 * @version 1.0
 */
public class PoolDeThreads {

    static Logger log = LogManager.getLogger(PoolDeThreads.class.getName());

    private int maximoDeThreads;
    private ScheduledExecutorService servico;

    /**
     * Instancia o pool de threads com uma única thread.
     */
    public PoolDeThreads() {
        this(1);
    }

    /**
     * Instancia o pool de threads com o número de threads informado e o tempo
     * de máximo de inatividade indicado.
     *
     * @param threads
     */
    public PoolDeThreads(int threads) {
        setMaximoDeThreads(threads);
    }

    /**
     * Define o número máximo de threads disponível no pool.
     *
     * @return Número máximo de threads disponível no pool.
     */
    public int getMaximoDeThreads() {
        return maximoDeThreads;
    }

    /**
     * Define o número máximo de threads disponível no pool.
     *
     * @param threads Número máximo de threads disponível no pool.
     */
    public void setMaximoDeThreads(int threads) {
        this.maximoDeThreads = threads;
    }

    /**
     * Instância do agendador de execução de processos. Por esta instância é
     * possível agendar processos para rodar uma única vez ou em períodos.
     *
     * @return Instância do agendador de execução de processos.
     */
    public ScheduledExecutorService getServico() {
        if (servico == null) {
            servico = Executors.newScheduledThreadPool(getMaximoDeThreads());
        }
        return servico;
    }

    /**
     * Executa uma única vez o processo indicado.
     *
     * @param processo O processo a ser executado.
     * @param atrasoEmSegundos Segundos a esperar antes da primeira execução.
     * @return O objeto para gerenciamento da execução do processo.
     */
    public Future<?> agendarUmaExecucao(Runnable processo, long atrasoEmSegundos) {
        return getServico().schedule(processo, atrasoEmSegundos, TimeUnit.SECONDS);
    }

    /**
     * Executa o processo em intervalo de tempo pré-definido.
     *
     * @param processo O processo a ser executado.
     * @param atrasoEmSegundos Segundos a esperar antes da primeira execução.
     * @param intervaloEmSegundos Tempo a considerar para a próxima execução do
     * processo contando a partir do início da execução do processo atual.
     * @return O objeto para gerenciamento da execução do processo.
     */
    public Future<?> agendarPeriodo(
            Runnable processo, long atrasoEmSegundos, long intervaloEmSegundos) {
        return getServico().scheduleAtFixedRate(processo, atrasoEmSegundos,
                intervaloEmSegundos, TimeUnit.SECONDS);
    }

    /**
     * Executa o processo em sequencia.
     *
     * @param processo O processo a ser executado.
     * @param atrasoEmSegundos Segundos a esperar antes da primeira execução.
     * @param intervaloEmSegundos Tempo a considerar para a próxima execução do
     * processo contando a partir do fim da execução do processo atual.
     * @return O objeto para gerenciamento da execução do processo.
     */
    public Future<?> agendarSequencia(
            Runnable processo, long atrasoEmSegundos, long intervaloEmSegundos) {
        return getServico().scheduleWithFixedDelay(processo, atrasoEmSegundos,
                intervaloEmSegundos, TimeUnit.SECONDS);
    }

    /**
     * Encerra o pool de threads.
     *
     * @param desligarForcado Quando verdadeiro força a interrupção de threads
     * em andamento.
     */
    public void shutdown(boolean desligarForcado) {
        if (desligarForcado) {
            for (Runnable processo : servico.shutdownNow()) {
                log.warn("Processo interrompido: " + processo);
            }
        } else {
            servico.shutdown();
        }
    }
}
