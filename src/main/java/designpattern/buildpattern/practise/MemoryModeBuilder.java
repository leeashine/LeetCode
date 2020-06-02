package designpattern.buildpattern.practise;

public class MemoryModeBuilder extends VideoPlayerBuilder{
    @Override
    public void buildMenu() {
        System.out.println("构建菜单。。。。");
        videoPlayer.setMenu("主菜单");
    }

    @Override
    public void buildList() {
        System.out.println("构建播放列表。。。。");
        videoPlayer.setList("播放列表");
    }

    @Override
    public void buildMainFrame() {
        System.out.println("构建主窗口。。。。");
        videoPlayer.setMainFrame("主窗口");
    }

    @Override
    public void buildControlBar() {
        System.out.println("构建控制条。。。。");
        videoPlayer.setBar("控制条");
    }

    @Override
    public boolean isBuildList() {
        return true;
    }

    @Override
    public boolean isBuildMainFrame() {
        return true;
    }

    @Override
    public boolean isBuildConBar() {
        return true;
    }
}
