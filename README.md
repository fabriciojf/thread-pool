# thread-pool

My implementation of thread pools, running loops using pools of thread: once, sequential or periodic

### Usage

 - see: [LoopUsage.java](https://github.com/fabriciojf/thread-pool/blob/main/src/main/java/com/fabriciojf/threadpool/LoopUsage.java)

```java
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
``` 
