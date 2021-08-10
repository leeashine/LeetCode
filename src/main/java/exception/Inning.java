package exception;

/**
 * Created by LIZIXUAN560 on 2020/10/15.
 *
 * @author LIZIXUAN560
 */
abstract class Inning {
    public Inning() throws BaseballException{ }
    public void event() throws BaseballException{ }
    public abstract void atBat() throws Strike,Foul;
    public void walk(){}

}
