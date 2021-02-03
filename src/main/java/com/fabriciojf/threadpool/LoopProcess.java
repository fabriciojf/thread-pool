package com.fabriciojf.threadpool;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstração de processo auto-executável. Classes interessadas em prover
 * processos auto-executáveis podem implementar esta base e definir o método
 * {@link #run()}. Um processo pode ser executado apenas uma vez, agendado para
 * execução periódica ou para execução sequencial. Pode ser definido um atraso
 * em segundos para a primeira execução do processo e um intervalo também em
 * segundos entre a execução. No caso de agendamento periódico o intervalo é
 * contado a partir do início da execução do processo anterior, para garantir um
 * intervalo fixo entre execuções. No caso de agendamento sequencial o intervalo
 * é contado a partir do fim da execução do processo anterior. Caso uma execução
 * do processo demore mais que o intervalo definido a próxima execução será
 * atrasada para evitar duas execuções do mesmo processo.
 *
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 30/09/2012
 * @version 1.0
 */
public abstract class LoopProcess implements Runnable {

    static Logger log = LoggerFactory.getLogger(LoopProcess.class);

    private String nome;
    private String poolDeThreads;
    private RepetitionEnum repeticao;
    private long atraso;
    private long segundos;
    private Future<?> handler;
    private Date ultimaExecucao;

    /**
     * Constrói um processo com um nome definido. O nome do processo ajuda a
     * identificar o processo em logs e ferramentas de diagnóstico.
     *
     * @param nome O nome do processo.
     */
    public LoopProcess(String nome) {
        setNome(nome);
        setPoolDeThreads("padrao");
        setRepeticao(RepetitionEnum.ONCE);
    }

    /**
     * Nome do processo. O nome do processo ajuda a identificar o processo em
     * logs e ferramentas de diagnóstico.
     *
     * @return Nome do processo.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Nome do processo. O nome do processo ajuda a identificar o processo em
     * logs e ferramentas de diagnóstico.
     *
     * @param nome Nome do processo.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Nome do pool de threads usado para execução deste processo.
     *
     * Uma instância de {@link PoolDeThreads} com este nome será procurada no
     * {@link Multiton}.
     *
     * @return Nome do pool de threads usado para execução deste processo.
     */
    public String getPoolDeThreads() {
        return poolDeThreads;
    }

    /**
     * Nome do pool de threads usado para execução deste processo.
     *
     * Uma instância de {@link PoolDeThreads} com este nome será procurada no
     * {@link Multiton}.
     *
     * @param poolDeThreads Nome do pool de threads usado para execução deste
     * processo.
     */
    public void setPoolDeThreads(String poolDeThreads) {
        this.poolDeThreads = poolDeThreads;
    }

    /**
     * Tipo de repetição do processo. Os valores possíveis são
     * {@link RepetitionEnum#ONCE}, para uma única execução;
     * {@link RepetitionEnum#PERIODIC}, para execução em períodos de tempo
     * definido; ou, {@link RepetitionEnum#SEQUENTIAL}, para execução em
     * sequencia. Para a execução periódica o intervalo de execução é contado a
     * partir do início da execução anterior, para a execução sequencial o
     * intervalo de execução é contado a partir do fim da execução anterior.
     *
     * @return Tipo de repetição do processo. Os valores possíveis são
     * {@link RepetitionEnum#ONCE}, para uma única execução;
     * {@link RepetitionEnum#PERIODIC}, para execução em períodos de tempo
     * definido; ou, {@link RepetitionEnum#SEQUENTIAL}, para execução em
     * sequencia.
     */
    public RepetitionEnum getRepeticao() {
        return repeticao;
    }

    /**
     * Tipo de repetição do processo. Os valores possíveis são
     * {@link RepetitionEnum#ONCE}, para uma única execução;
     * {@link RepetitionEnum#PERIODIC}, para execução em períodos de tempo
     * definido; ou, {@link RepetitionEnum#SEQUENTIAL}, para execução em
     * sequencia. Para a execução periódica o intervalo de execução é contado a
     * partir do início da execução anterior, para a execução sequencial o
     * intervalo de execução é contado a partir do fim da execução anterior.
     *
     * @param repeticao Tipo de repetição do processo. Os valores possíveis são
     * {@link RepetitionEnum#ONCE}, para uma única execução;
     * {@link RepetitionEnum#PERIODIC}, para execução em períodos de tempo
     * definido; ou, {@link RepetitionEnum#SEQUENTIAL}, para execução em
     * sequencia.
     */
    public void setRepeticao(RepetitionEnum repeticao) {
        this.repeticao = repeticao;
    }

    /**
     * Atraso em segundos para a primeira execução do processo.
     *
     * @return Atraso em segundos para a primeira execução do processo.
     */
    public long getAtraso() {
        return atraso;
    }

    /**
     * Atraso em segundos para a primeira execução do processo.
     *
     * @param atrasoEmSegundos Atraso em segundos para a primeira execução do
     * processo.
     */
    public void setAtraso(long atrasoEmSegundos) {
        this.atraso = atrasoEmSegundos;
    }

    /**
     * Intervalo em segundos para execução do processo quando agendado como
     * período ou sequencia.
     *
     * @return Intervalo em segundos para execução do processo
     */
    public long getSegundos() {
        return segundos;
    }

    /**
     * Intervalo em segundos para execução do processo quando agendado como
     * período ou sequencia.
     *
     * @param segundos Intervalo em segundos para execução do processo.
     */
    public void setSegundos(long segundos) {
        this.segundos = segundos;
    }

    /**
     * Instante da última execução do processo.
     *
     * @return Instante da última execução do processo.
     */
    public Date getUltimaExecucao() {
        return ultimaExecucao;
    }

    /**
     * Instante da última execução do processo.
     *
     * @param ultimaExecucao Instante da última execução do processo.
     */
    public void setUltimaExecucao(Date ultimaExecucao) {
        this.ultimaExecucao = ultimaExecucao;
    }

    /**
     * Define o objeto de manipulação da execução do processo. Por este objeto é
     * possível consultar a situação da execução e interromper e execução.
     *
     * @return Objeto de manipulação da execução do processo.
     */
    public Future<?> getHandler() {
        return handler;
    }

    /**
     * Define o objeto de manipulação da execução do processo. Por este objeto é
     * possível consultar a situação da execução e interromper e execução.
     *
     * @param handler Objeto de manipulação da execução do processo.
     */
    void setHandler(Future<?> handler) {
        this.handler = handler;
    }

    /**
     * Diz se o processo está em execução no momento.
     *
     * @return Verdadeiro se o processo estiver em execução no momento.
     */
    public boolean isExecutando() {
        return getHandler() != null && !getHandler().isDone();
    }

    /**
     * Executa o processo de acordo com o agendamento de repetição, atraso e
     * intervalo. Para interromper a execução de um processo utilize os métodos
     * {@link #interromper()} e {@link #interromper(boolean)}.
     *
     * @return Verdadeiro caso o processo tenha sido posto em execução com
     * sucesso.
     */
    public synchronized boolean executar() {
        try {
            if (isExecutando()) {
                log.warn(
                        "Solicitacao de execucao de processo já em execucao. "
                        + "Nada será feito: " + getNome() + "\n"
                        + Thread.currentThread().getStackTrace().toString());
                return false;
            }

            PoolDeThreads pool = Multiton.getInstance().
                    obter(getPoolDeThreads(), PoolDeThreads.class);

            log.info("Tentando executar o processo " + getNome());

            Runnable wrapper = new Runnable() {
                @Override
                public void run() {
                    // dá um nome à thread para facilitar a depuração
                    Thread.currentThread().setName(LoopProcess.this.getNome());
                    LoopProcess.this.run();
                    LoopProcess.this.setUltimaExecucao(
                            Calendar.getInstance().getTime());
                }
            };

            Future<?> handler = null;
            switch (getRepeticao()) {
                default:
                case ONCE:
                    handler = pool.agendarUmaExecucao(wrapper, getAtraso());
                    break;
                case PERIODIC:
                    handler = pool.agendarPeriodo(
                            wrapper, getAtraso(), getSegundos());
                    break;
                case SEQUENTIAL:
                    handler = pool.agendarSequencia(
                            wrapper, getAtraso(), getSegundos());
                    break;
            }
            setHandler(handler);

            log.info("Agendamento do processo " + getNome() + " realizado: "
                    + "pool:" + getPoolDeThreads()
                    + ", repeticao:" + getRepeticao().toString()
                    + ", atraso:" + getAtraso()
                    + ", segundos:" + getSegundos());

            return true;

        } catch (Exception ex) {
            log.error("Exceção tentando executar o processo: " + getNome(), ex);
            return false;
        }
    }

    /**
     * Interrompe o agendamento do processo. O processador espera o término da
     * execução atual do processo caso esteja em andamento e em seguida cancela
     * o agendamento.
     *
     * @see #interromper(boolean)
     */
    public void interromper() {
        try {
            getHandler().cancel(false);
            log.info("Processo interrompido: " + getNome());
        } catch (Exception ex) {
            log.error("Exceção tentando interromper o processo "
                    + getNome(), ex);
        }
    }

    /**
     * Interrompe o agendamento do processo e se necessário a sua execução. Caso
     * o processo esteja em execução no momento e seja informado verdadeiro no
     * parâmetro 'imediatamente' a execução do processo será interrompida, nos
     * demais casos o processador aguarda o término da execução atual do
     * processo e interrompe o agendamento.
     *
     * @param imediatamente Quando verdadeiro força a interrupção do processo em
     * execução, quando falso, permite o término do processamento antes do
     * cancelamento.
     * @see #interromper()
     */
    public void interromper(boolean imediatamente) {
        try {
            getHandler().cancel(imediatamente);
            log.info("Processo interrompido: " + getNome());
        } catch (Exception ex) {
            log.error("Exceção tentando interromper o processo "
                    + getNome(), ex);
        }
    }

}
