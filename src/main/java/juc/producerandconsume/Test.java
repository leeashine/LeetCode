package juc.producerandconsume;

public class Test {
    public static void main(String[] args) {
        ProducerAndConsumerService service=new ProducerAndConsumerService();
//        new Thread(()->{
//            for (int i=0;i<20;i++) {
//                service.produce();
//            }
//        },"producerThread").start();
//        new Thread(()->{
//            for (int i=0;i<20;i++) {
//                service.consume();
//            }
//        },"consumerThread").start();
        //
        ThreadProducer[] threadProducers=new ThreadProducer[100];
        ThreadConsumer[] threadConsumers=new ThreadConsumer[100];
        for(int i=0;i<100;i++){
            threadProducers[i]=new ThreadProducer(service);
            threadConsumers[i]=new ThreadConsumer(service);
            threadProducers[i].start();
            threadConsumers[i].start();
        }
    }
}
