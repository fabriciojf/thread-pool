package com.fabriciojf.threadpool;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Gerenciar de cópias nomeadas de objetos. Enquanto o padrao singleton garante
 * uma unica instancia de uma classe para toda a aplicacao o multiton permite
 * mais de uma instancia porem nomeada, e para cada nome garante a existencia de
 * uma unica instancia.
 * 
 * @author Fabricio S Costa fabriciojf@gmail.com
 * @since 23/09/2012
 * @version 1.0
 */
@SuppressWarnings("all")
public class Multiton {

    private static Multiton instance;
	private Map<Class, Map<String, Object>> instanciasPorTipo;

    private Object lock = new Object();
    
    /**
     * Instancia padrao do multiton.
     * 
     * @return Instancia padrao do multiton.
     */
    public static Multiton getInstance() {
        if (instance == null) {
            instance = new Multiton();
        }
        return instance;
    }

    private Multiton() {
        this.instanciasPorTipo = new HashMap<Class, Map<String, Object>>();
    }

    /**
     * Determina se existe a instância indicada do tipo indicado com o nome
     * indicado.
     * 
     * @param nome
     *            O nome da instancia requerida.
     * @param tipo
     *            O tipo da instancia requerida.
     * @return Verdadeiro se a instância do tipo com o nome definido existe.
     */
    public <T> boolean existe(String nome, Class<T> tipo) {
        synchronized (lock) {
            if (!this.instanciasPorTipo.containsKey(tipo)) {
                return false;
            }
            return this.instanciasPorTipo.get(tipo).containsKey(nome);
        }
    }

    /**
     * Obtem a instancia da classe indicada com o nome especificado. Caso ainda
     * nao exista uma instancia com o nome indicado uma instancia sera criada. E
     * garantido que o metodo retornara sempre a mesma instancia da classe para
     * o nome indicado a menos que o metodo {@link #descartar(String)} tenha
     * sido invocado previamente. É esperado que a classe possua um construtor
     * vazio.
     * 
     * @param nome
     *            O nome da instancia requerida.
     * @param tipo
     *            O tipo da instancia requerida.
     * @return A instancia da classe com o nome especificado.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T obter(String nome, Class<T> tipo) {
        synchronized (lock) {
            try {
                Map<String, Object> instancias = this.instanciasPorTipo.get(tipo);
                if (instancias == null) {
                    instancias = new HashMap<String, Object>();
                    this.instanciasPorTipo.put(tipo, instancias);
                }
    
                Object instancia = instancias.get(nome);
                if (instancia == null) {
                    instancia = tipo.newInstance();
                    instancias.put(nome, instancia);
                }
    
                return (T) instancia;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Obtem a instancia da classe indicada com o nome especificado. Caso ainda
     * nao exista uma instancia com o nome indicado uma instancia sera criada. E
     * garantido que o metodo retornara sempre a mesma instancia da classe para
     * o nome indicado a menos que o metodo {@link #descartar(String)} tenha
     * sido invocado previamente. Caso seja necessario instanciar um objeto da
     * classe a fabrica informada sera utilizada.
     * 
     * @param nome
     *            O nome da instancia requerida.
     * @param tipo
     *            O tipo da instancia requerida.
     * @fabrica Fabrica para criacao da instância do tipo se necessario.
     * @return A instancia da classe com o nome especificado.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T obter(String nome, Class<T> tipo, LoopFactory<T> fabrica) {
        synchronized (lock) {
            try {
                Map<String, Object> instancias = this.instanciasPorTipo.get(tipo);
                if (instancias == null) {
                    instancias = new HashMap<String, Object>();
                    this.instanciasPorTipo.put(tipo, instancias);
                }
    
                Object instancia = instancias.get(nome);
                if (instancia == null) {
                    instancia = fabrica.fabricar();
                    instancias.put(nome, instancia);
                }
    
                return (T) instancia;
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

	/**
	 * Define uma instancia nomeada. Caso ja exista uma instancia com o nome
	 * indicado para o tipo indicado uma excecao sera lancada.
	 * 
	 * @param nome
	 *            O nome da instancia requerida.
	 * @param tipo
	 *            O tipo da instancia requerida.
	 * @param instancia
	 *            A instancia que deve ser registrada.
	 */
    public <T> void definir(String nome, Class<T> tipo, T instancia) {
        synchronized (lock) {
            try {
                Map<String, Object> instancias = this.instanciasPorTipo.get(tipo);
                if (instancias == null) {
                    instancias = new HashMap<String, Object>();
                    this.instanciasPorTipo.put(tipo, instancias);
                }
                
                if (instancias.containsKey(nome)) {
                    throw new RuntimeException(
                    		"Ja existe uma instancia com o nome " +
                    		nome + " para o tipo " + tipo.getName());
                }
                
	            instancias.put(nome, instancia);
	            
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Obtpem uma lista de nomes de instâncias do tipo indicado.
     * 
     * @param tipo
     *            O tipo procurado.
     * @return A lista de nomes de instâncias.
     */
    public <T> Collection<String> obterInstancias(Class<T> tipo) {
        synchronized (lock) {
            Map<String, Object> instancias = this.instanciasPorTipo.get(tipo);
            if (instancias == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableCollection(instancias.keySet());
        }
    }

    /**
     * Descarta a instancia com o nome especificado.
     * 
     * @param nome
     *            O nome da instancia a ser descartada.
     */
    public <T> void descartar(String nome, Class<T> tipo) {
        synchronized (lock) {
            if (!this.instanciasPorTipo.containsKey(tipo)) {
                return;
            }
    
            Map<String, Object> instancias = this.instanciasPorTipo.get(tipo);
            if (instancias.containsKey(nome)) {
                instancias.remove(nome);
    
                if (instancias.size() == 0) {
                    this.instanciasPorTipo.remove(tipo);
                }
            }
        }
    }
}
