package aleetcode.baidu;

class ExceptionA extends Exception{ }
class ExceptionB extends ExceptionA{ }
public class Exception01 {

    public static void main(String[] args) {

        try {
            try {
                throw new ExceptionB();
            }catch (ExceptionB exceptionB){
                System.out.println("Inner exceptionB");
                throw exceptionB;
            }catch (ExceptionA exceptionA){
                System.out.println("Inner exceptionA");
                throw exceptionA;
            }catch (Exception e){
                System.out.println("Inner Exception");
                throw e;
            }
        } catch (ExceptionA e) {
            System.out.println("Catch Outer ExceptionA");
        }finally {
            System.out.println("finally");
        }


    }

}
