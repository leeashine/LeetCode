package juc.producerandconsume;

public class ThreadConsumer extends Thread{
    private ProducerAndConsumerService service;
    public ThreadConsumer(ProducerAndConsumerService service){
        this.service=service;
    }

    @Override
    public void run() {

        while (true){
            service.consume();
        }

    }
}
