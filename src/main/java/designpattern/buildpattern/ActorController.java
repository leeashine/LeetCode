package designpattern.buildpattern;

//指挥者 为了简化 可以不用
//以上两种对Director类的省略方式都不影响系统的灵活性和可扩展性，同时还简化了系统结
//构，但加重了抽象建造者类的职责，如果construct()方法较为复杂，待构建产品的组成部分较
//多，建议还是将construct()方法单独封装在Director中，这样做更符合“单一职责原则”。
//通过引入钩子方法，我们可以在Director中对复杂产品的构建进行精细的控制，不仅指定buildPartX()方法的执
//行顺序，还可以控制是否需要执行某个buildPartX()方法。
public class ActorController {

//    public Actor construct(ActorBuilder ab){
//        Actor actor;
//        ab.buildFace();
//        ab.buildSex();
//        ab.buildType();
//        if(!ab.isShuangYanPi){
//              ab.buildHairstyle();
//        }
//        actor=ab.createActor();
//        return actor;
//    }

}
