package aleetcode;

import java.util.concurrent.atomic.AtomicInteger;

public class Thread01 {

	static AtomicInteger num=new AtomicInteger(0);
	static volatile Boolean b=false;
	public static void main(String[] args) {
		
		Thread thread1=new Thread( ()->{
		
			for(;num.get()<100;){
				if( !b && (num.get()==0 ||num.incrementAndGet()%2==0)){
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(num.get());
					b=true;
					
				}
				
			}
			
			
		});
		
		Thread thread2=new Thread( ()->{
			
			for(;num.get()<100;){
				if( b && (num.incrementAndGet()%2!=0)){
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(num.get());
					b=false;
					
				}
				
			}
			
			
		});
		thread1.start();
		thread2.start();
	}
	
	
}
