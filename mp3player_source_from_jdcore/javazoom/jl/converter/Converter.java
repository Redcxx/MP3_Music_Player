package javazoom.jl.converter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Decoder.Params;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Obuffer;

public class Converter
{
  public Converter() {}
  
  public synchronized void convert(String paramString1, String paramString2)
    throws JavaLayerException
  {
    convert(paramString1, paramString2, null, null);
  }
  
  public synchronized void convert(String paramString1, String paramString2, ProgressListener paramProgressListener)
    throws JavaLayerException
  {
    convert(paramString1, paramString2, paramProgressListener, null);
  }
  
  public void convert(String paramString1, String paramString2, ProgressListener paramProgressListener, Decoder.Params paramParams)
    throws JavaLayerException
  {
    if (paramString2.length() == 0) {
      paramString2 = null;
    }
    try
    {
      InputStream localInputStream = openInput(paramString1);
      convert(localInputStream, paramString2, paramProgressListener, paramParams);
      localInputStream.close();
    }
    catch (IOException localIOException)
    {
      throw new JavaLayerException(localIOException.getLocalizedMessage(), localIOException);
    }
  }
  
  public synchronized void convert(InputStream paramInputStream, String paramString, ProgressListener paramProgressListener, Decoder.Params paramParams)
    throws JavaLayerException
  {
    if (paramProgressListener == null) {
      paramProgressListener = PrintWriterProgressListener.newStdOut(0);
    }
    try
    {
      if (!(paramInputStream instanceof BufferedInputStream)) {
        paramInputStream = new BufferedInputStream(paramInputStream);
      }
      int i = -1;
      if (paramInputStream.markSupported())
      {
        paramInputStream.mark(-1);
        i = countFrames(paramInputStream);
        paramInputStream.reset();
      }
      paramProgressListener.converterUpdate(1, i, 0);
      WaveFileObuffer localWaveFileObuffer = null;
      Decoder localDecoder = new Decoder(paramParams);
      Bitstream localBitstream = new Bitstream(paramInputStream);
      if (i == -1) {
        i = Integer.MAX_VALUE;
      }
      int j = 0;
      long l = System.currentTimeMillis();
      try
      {
        while (j < i)
        {
          try
          {
            Header localHeader = localBitstream.readFrame();
            if (localHeader == null) {
              break;
            }
            paramProgressListener.readFrame(j, localHeader);
            if (localWaveFileObuffer == null)
            {
              int m = localHeader.mode() == 3 ? 1 : 2;
              int i1 = localHeader.frequency();
              localWaveFileObuffer = new WaveFileObuffer(m, i1, paramString);
              localDecoder.setOutputBuffer(localWaveFileObuffer);
            }
            Obuffer localObuffer = localDecoder.decodeFrame(localHeader, localBitstream);
            if (localObuffer != localWaveFileObuffer) {
              throw new InternalError("Output buffers are different.");
            }
            paramProgressListener.decodedFrame(j, localHeader, localWaveFileObuffer);
            localBitstream.closeFrame();
          }
          catch (Exception localException)
          {
            int n = !paramProgressListener.converterException(localException) ? 1 : 0;
            if (n != 0) {
              throw new JavaLayerException(localException.getLocalizedMessage(), localException);
            }
          }
          j++;
        }
      }
      finally
      {
        if (localWaveFileObuffer != null) {
          localWaveFileObuffer.close();
        }
      }
      int k = (int)(System.currentTimeMillis() - l);
      paramProgressListener.converterUpdate(2, k, j);
    }
    catch (IOException localIOException)
    {
      throw new JavaLayerException(localIOException.getLocalizedMessage(), localIOException);
    }
  }
  
  protected int countFrames(InputStream paramInputStream)
  {
    return -1;
  }
  
  protected InputStream openInput(String paramString)
    throws IOException
  {
    File localFile = new File(paramString);
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
    return localBufferedInputStream;
  }
  
  public static class PrintWriterProgressListener
    implements Converter.ProgressListener
  {
    public static final int NO_DETAIL = 0;
    public static final int EXPERT_DETAIL = 1;
    public static final int VERBOSE_DETAIL = 2;
    public static final int DEBUG_DETAIL = 7;
    public static final int MAX_DETAIL = 10;
    private PrintWriter pw;
    private int detailLevel;
    
    public static PrintWriterProgressListener newStdOut(int paramInt)
    {
      return new PrintWriterProgressListener(new PrintWriter(System.out, true), paramInt);
    }
    
    public PrintWriterProgressListener(PrintWriter paramPrintWriter, int paramInt)
    {
      pw = paramPrintWriter;
      detailLevel = paramInt;
    }
    
    public boolean isDetail(int paramInt)
    {
      return detailLevel >= paramInt;
    }
    
    public void converterUpdate(int paramInt1, int paramInt2, int paramInt3)
    {
      if (isDetail(2)) {
        switch (paramInt1)
        {
        case 2: 
          if (paramInt3 == 0) {
            paramInt3 = 1;
          }
          pw.println();
          pw.println("Converted " + paramInt3 + " frames in " + paramInt2 + " ms (" + paramInt2 / paramInt3 + " ms per frame.)");
        }
      }
    }
    
    public void parsedFrame(int paramInt, Header paramHeader)
    {
      String str;
      if ((paramInt == 0) && (isDetail(2)))
      {
        str = paramHeader.toString();
        pw.println("File is a " + str);
      }
      else if (isDetail(10))
      {
        str = paramHeader.toString();
        pw.println("Prased frame " + paramInt + ": " + str);
      }
    }
    
    public void readFrame(int paramInt, Header paramHeader)
    {
      String str;
      if ((paramInt == 0) && (isDetail(2)))
      {
        str = paramHeader.toString();
        pw.println("File is a " + str);
      }
      else if (isDetail(10))
      {
        str = paramHeader.toString();
        pw.println("Read frame " + paramInt + ": " + str);
      }
    }
    
    public void decodedFrame(int paramInt, Header paramHeader, Obuffer paramObuffer)
    {
      if (isDetail(10))
      {
        String str = paramHeader.toString();
        pw.println("Decoded frame " + paramInt + ": " + str);
        pw.println("Output: " + paramObuffer);
      }
      else if (isDetail(2))
      {
        if (paramInt == 0)
        {
          pw.print("Converting.");
          pw.flush();
        }
        if (paramInt % 10 == 0)
        {
          pw.print('.');
          pw.flush();
        }
      }
    }
    
    public boolean converterException(Throwable paramThrowable)
    {
      if (detailLevel > 0)
      {
        paramThrowable.printStackTrace(pw);
        pw.flush();
      }
      return false;
    }
  }
  
  public static abstract interface ProgressListener
  {
    public static final int UPDATE_FRAME_COUNT = 1;
    public static final int UPDATE_CONVERT_COMPLETE = 2;
    
    public abstract void converterUpdate(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void parsedFrame(int paramInt, Header paramHeader);
    
    public abstract void readFrame(int paramInt, Header paramHeader);
    
    public abstract void decodedFrame(int paramInt, Header paramHeader, Obuffer paramObuffer);
    
    public abstract boolean converterException(Throwable paramThrowable);
  }
}
