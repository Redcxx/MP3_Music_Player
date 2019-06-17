package javazoom.jl.decoder;

import java.io.PrintStream;

public class JavaLayerException
  extends Exception
{
  private Throwable exception;
  
  public JavaLayerException() {}
  
  public JavaLayerException(String paramString)
  {
    super(paramString);
  }
  
  public JavaLayerException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    exception = paramThrowable;
  }
  
  public Throwable getException()
  {
    return exception;
  }
  
  public void printStackTrace()
  {
    printStackTrace(System.err);
  }
  
  public void printStackTrace(PrintStream paramPrintStream)
  {
    if (exception == null) {
      super.printStackTrace(paramPrintStream);
    } else {
      exception.printStackTrace();
    }
  }
}
