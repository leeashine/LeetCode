package designpattern.statuspatern.sample3;

//在上述代码中，所有的状态转换操作都由环境类Screen来实现，此时，环境类充当了状态管理
//        器角色。如果需要增加新的状态，例如“八倍状态类”，需要修改环境类，这在一定程度上违背
//        了“开闭原则”，但对其他状态类没有任何影响。
//屏幕类
class Screen {
    //枚举所有的状态，currentState表示当前状态
    private State currentState, normalState, largerState, largestState;
    public Screen() {
        this.normalState = new NormalState(); //创建正常状态对象
        this.largerState = new LargerState(); //创建二倍放大状态对象
        this.largestState = new LargestState(); //创建四倍放大状态对象
        this.currentState = normalState; //设置初始状态
        this.currentState.display();
    }
    public void setState(State state) {
        this.currentState = state;
    }
    //单击事件处理方法，封转了对状态类中业务方法的调用和状态的转换
    public void onClick() {
        if (this.currentState == normalState) {
            this.setState(largerState);
            this.currentState.display();
        }
        else if (this.currentState == largerState) {
            this.setState(largestState);
            this.currentState.display();
        }
        else if (this.currentState == largestState) {
            this.setState(normalState);
            this.currentState.display();
        }
    }
}

