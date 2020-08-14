package juc.producerandconsume;

public class ThreadProducer extends Thread{
    private ProducerAndConsumerService service;
    public ThreadProducer(ProducerAndConsumerService service){
        this.service=service;
    }

    @Override
    public void run() {

        while (true){
            service.produce();
        }

    }
}
