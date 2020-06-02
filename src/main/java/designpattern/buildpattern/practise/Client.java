package designpattern.buildpattern.practise;

public class Client {
    public static void main(String[] args) {

        VideoPlayerBuilder videoPlayerBuilder;
        videoPlayerBuilder=(VideoPlayerBuilder)XMLUtil.getBean();
        VideoPlayer videoPlayer;
        videoPlayer=new VideoPlayerController().construct(videoPlayerBuilder);
        System.out.println(videoPlayer.getBar());

    }
}
