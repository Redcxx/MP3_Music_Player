import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

























































































































































































class PlayListener
  extends PlaybackListener
{
  PlayFinishedListener listener;
  
  public PlayListener()
  {
    listener = null;
  }
  
  public PlayListener(PlayFinishedListener paramPlayFinishedListener)
  {
    listener = paramPlayFinishedListener;
  }
  
  public void playbackStarted(PlaybackEvent paramPlaybackEvent) {}
  
  public void playbackFinished(PlaybackEvent paramPlaybackEvent)
  {
    if (listener != null) {
      listener.playFinished();
    }
  }
}
