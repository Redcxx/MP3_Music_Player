package javazoom.jl.decoder;

public class Decoder
  implements DecoderErrors
{
  private static final Params DEFAULT_PARAMS = new Params();
  private Obuffer output;
  private SynthesisFilter filter1;
  private SynthesisFilter filter2;
  private LayerIIIDecoder l3decoder;
  private LayerIIDecoder l2decoder;
  private LayerIDecoder l1decoder;
  private int outputFrequency;
  private int outputChannels;
  private Equalizer equalizer = new Equalizer();
  private Params params;
  private boolean initialized;
  
  public Decoder()
  {
    this(null);
  }
  
  public Decoder(Params paramParams)
  {
    if (paramParams == null) {
      paramParams = DEFAULT_PARAMS;
    }
    params = paramParams;
    Equalizer localEqualizer = params.getInitialEqualizerSettings();
    if (localEqualizer != null) {
      equalizer.setFrom(localEqualizer);
    }
  }
  
  public static Params getDefaultParams()
  {
    return (Params)DEFAULT_PARAMS.clone();
  }
  
  public void setEqualizer(Equalizer paramEqualizer)
  {
    if (paramEqualizer == null) {
      paramEqualizer = Equalizer.PASS_THRU_EQ;
    }
    equalizer.setFrom(paramEqualizer);
    float[] arrayOfFloat = equalizer.getBandFactors();
    if (filter1 != null) {
      filter1.setEQ(arrayOfFloat);
    }
    if (filter2 != null) {
      filter2.setEQ(arrayOfFloat);
    }
  }
  
  public Obuffer decodeFrame(Header paramHeader, Bitstream paramBitstream)
    throws DecoderException
  {
    if (!initialized) {
      initialize(paramHeader);
    }
    int i = paramHeader.layer();
    output.clear_buffer();
    FrameDecoder localFrameDecoder = retrieveDecoder(paramHeader, paramBitstream, i);
    localFrameDecoder.decodeFrame();
    output.write_buffer(1);
    return output;
  }
  
  public void setOutputBuffer(Obuffer paramObuffer)
  {
    output = paramObuffer;
  }
  
  public int getOutputFrequency()
  {
    return outputFrequency;
  }
  
  public int getOutputChannels()
  {
    return outputChannels;
  }
  
  public int getOutputBlockSize()
  {
    return 2304;
  }
  
  protected DecoderException newDecoderException(int paramInt)
  {
    return new DecoderException(paramInt, null);
  }
  
  protected DecoderException newDecoderException(int paramInt, Throwable paramThrowable)
  {
    return new DecoderException(paramInt, paramThrowable);
  }
  
  protected FrameDecoder retrieveDecoder(Header paramHeader, Bitstream paramBitstream, int paramInt)
    throws DecoderException
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 3: 
      if (l3decoder == null) {
        l3decoder = new LayerIIIDecoder(paramBitstream, paramHeader, filter1, filter2, output, 0);
      }
      localObject = l3decoder;
      break;
    case 2: 
      if (l2decoder == null)
      {
        l2decoder = new LayerIIDecoder();
        l2decoder.create(paramBitstream, paramHeader, filter1, filter2, output, 0);
      }
      localObject = l2decoder;
      break;
    case 1: 
      if (l1decoder == null)
      {
        l1decoder = new LayerIDecoder();
        l1decoder.create(paramBitstream, paramHeader, filter1, filter2, output, 0);
      }
      localObject = l1decoder;
    }
    if (localObject == null) {
      throw newDecoderException(513, null);
    }
    return localObject;
  }
  
  private void initialize(Header paramHeader)
    throws DecoderException
  {
    float f = 32700.0F;
    int i = paramHeader.mode();
    int j = paramHeader.layer();
    int k = i == 3 ? 1 : 2;
    if (output == null) {
      output = new SampleBuffer(paramHeader.frequency(), k);
    }
    float[] arrayOfFloat = equalizer.getBandFactors();
    filter1 = new SynthesisFilter(0, f, arrayOfFloat);
    if (k == 2) {
      filter2 = new SynthesisFilter(1, f, arrayOfFloat);
    }
    outputChannels = k;
    outputFrequency = paramHeader.frequency();
    initialized = true;
  }
  
  public static class Params
    implements Cloneable
  {
    private OutputChannels outputChannels = OutputChannels.BOTH;
    private Equalizer equalizer = new Equalizer();
    
    public Params() {}
    
    public Object clone()
    {
      try
      {
        return super.clone();
      }
      catch (CloneNotSupportedException localCloneNotSupportedException)
      {
        throw new InternalError(this + ": " + localCloneNotSupportedException);
      }
    }
    
    public void setOutputChannels(OutputChannels paramOutputChannels)
    {
      if (paramOutputChannels == null) {
        throw new NullPointerException("out");
      }
      outputChannels = paramOutputChannels;
    }
    
    public OutputChannels getOutputChannels()
    {
      return outputChannels;
    }
    
    public Equalizer getInitialEqualizerSettings()
    {
      return equalizer;
    }
  }
}
