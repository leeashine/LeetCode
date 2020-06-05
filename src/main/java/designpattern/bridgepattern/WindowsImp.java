package designpattern.bridgepattern;

public class WindowsImp implements ImageImp{
    @Override
    public void doPaint(Matrix matrix) {
        //调用Windows系统的绘制函数绘制像素矩阵
        System.out.print("在Windows操作系统中显示图像：");
    }
}
