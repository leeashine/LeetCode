package designpattern.buildpattern.practise;

public class VideoPlayerController {

    public VideoPlayer construct(VideoPlayerBuilder vb){
        VideoPlayer videoPlayer;
        if(vb.isBuildConBar()){
            vb.buildControlBar();
        }
        if(vb.isBuildList()){
            vb.buildList();
        }
        if(vb.isBuildMenu()){
            vb.buildMenu();
        }
        if(vb.isBuildMainFrame()){
            vb.buildMainFrame();
        }
        videoPlayer=vb.createVideoPlayer();
        return videoPlayer;
    }

}
