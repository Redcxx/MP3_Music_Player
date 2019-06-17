package javazoom.jl.player.advanced;

public class PlaybackEvent
{
  public static int STOPPED = 1;
  public static int STARTED = 2;
  private AdvancedPlayer source;
  private int frame;
  private int id;
  
  public PlaybackEvent(AdvancedPlayer paramAdvancedPlayer, int paramInt1, int paramInt2)
  {
    id = paramInt1;
    source = paramAdvancedPlayer;
    frame = paramInt2;
  }
  
  public int getId()
  {
    return id;
  }
  
  public void setId(int paramInt)
  {
    id = paramInt;
  }
  
  public int getFrame()
  {
    return frame;
  }
  
  public void setFrame(int paramInt)
  {
    frame = paramInt;
  }
  
  public AdvancedPlayer getSource()
  {
    return source;
  }
  
  public void setSource(AdvancedPlayer paramAdvancedPlayer)
  {
    source = paramAdvancedPlayer;
  }
}
