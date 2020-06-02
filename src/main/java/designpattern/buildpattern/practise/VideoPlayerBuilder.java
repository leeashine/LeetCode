package designpattern.buildpattern.practise;

abstract class VideoPlayerBuilder {

    protected VideoPlayer videoPlayer=new VideoPlayer();

    public abstract void buildMenu();

    public abstract void buildList();

    public abstract void buildMainFrame();

    public abstract void buildControlBar();

    public boolean isBuildMenu(){return false;}
    public boolean isBuildList(){return false;}
    public boolean isBuildMainFrame(){return false;}
    public boolean isBuildConBar(){return false;}

    public VideoPlayer createVideoPlayer(){
        return videoPlayer;
    }

}
