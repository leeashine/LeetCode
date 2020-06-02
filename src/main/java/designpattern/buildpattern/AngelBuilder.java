package designpattern.buildpattern;

//具体建造者
public class AngelBuilder extends ActorBuilder{
    @Override
    public void buildType() {
        actor.setType("天使");
    }

    @Override
    public void buildSex() {
        actor.setSex("女");
    }

    @Override
    public void buildFace() {
        actor.setFace("美丽");
    }

    @Override
    public boolean isShuangYanPi() {
        return true;
    }
}
