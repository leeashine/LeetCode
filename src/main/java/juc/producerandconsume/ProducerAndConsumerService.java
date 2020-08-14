package juc.producerandconsume;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerAndConsumerService {
    private ReentrantLock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    private int sum=0;
    private int MAX=10;
    public void produce(){
        try{
            lock.lock();
            while (sum==MAX){
                condition.await();
            }
            System.out.println("生产消息");
            sum++;
            condition.signalAll();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void consume(){
        try{
            lock.lock();
            while (sum==0){
                condition.await();
            }
            System.out.println("消费消息");
            sum--;
            condition.signalAll();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
