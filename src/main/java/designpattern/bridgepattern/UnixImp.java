package designpattern.bridgepattern;

public class UnixImp implements ImageImp{
    @Override
    public void doPaint(Matrix matrix) {
        //调用Windows系统的绘制函数绘制像素矩阵
        System.out.print("在Unix操作系统中显示图像：");
    }
}
