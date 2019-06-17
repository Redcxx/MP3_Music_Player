package javazoom.jl.decoder;

public abstract interface Control
{
  public abstract void start();
  
  public abstract void stop();
  
  public abstract boolean isPlaying();
  
  public abstract void pause();
  
  public abstract boolean isRandomAccess();
  
  public abstract double getPosition();
  
  public abstract void setPosition(double paramDouble);
}
