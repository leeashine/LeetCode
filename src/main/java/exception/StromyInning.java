package exception;

/**
 * Created by LIZIXUAN560 on 2020/10/15.
 *
 * @author LIZIXUAN560
 */
public class StromyInning extends Inning implements Strom{

    public StromyInning() throws RainedOut,BaseballException {
    }
    public StromyInning(String s) throws Foul,BaseballException {
    }
    //Regular methods must conform to base class; compile error
//    void walk() throws PopException{}

    //Interface CANNOT add exceptions to existing methods from the base class;
//    public void event() throws RainedOut{}

    @Override
    public void atBat() throws PopFoul {

    }

    //if the method doesn't already exist in the base class,the exception is Ok;
    @Override
    public void rainHard() throws RainedOut {

    }

    @Override
    public void event(){}

    public static void main(String[] args) {

        try {
            StromyInning si=new StromyInning();
            si.atBat();
        }catch (PopFoul e){
            System.out.println("Pop foul");
        }catch (RainedOut e){
            System.out.println("Rained out");
        }catch (BaseballException e){
            System.out.println("Generic baseball exception");
        }
        try {
            Inning i=new StromyInning();
            i.atBat();
        }catch (Strike e){
            System.out.println("Strike");
        }catch (Foul e){
            System.out.println("Foul");
        }catch (RainedOut e){
            System.out.println("Rained out");
        }catch (BaseballException e){
            System.out.println("Generic baseball exception");
        }



    }



}
