package designpattern.buildpattern;

//抽象建造者
abstract class ActorBuilder {

    protected static Actor actor = new Actor();

    public abstract void buildType();

    public abstract void buildSex();

    public abstract void buildFace();

    //    public Actor createActor(){
//        return actor;
//    }

    //钩子方法
    public boolean isShuangYanPi() {
        return false;
    }


    public static Actor construct(ActorBuilder ab) {

        ab.buildType();
        ab.buildSex();
        ab.buildFace();
        return actor;

    }

}
