package javazoom.jl.decoder;

final class LayerIIIDecoder
  implements FrameDecoder
{
  final double d43 = 1.3333333333333333D;
  public int[] scalefac_buffer;
  private int CheckSumHuff = 0;
  private int[] is_1d;
  private float[][][] ro;
  private float[][][] lr;
  private float[] out_1d;
  private float[][] prevblck;
  private float[][] k;
  private int[] nonzero;
  private Bitstream stream;
  private Header header;
  private SynthesisFilter filter1;
  private SynthesisFilter filter2;
  private Obuffer buffer;
  private int which_channels;
  private BitReserve br;
  private III_side_info_t si;
  private temporaire2[] III_scalefac_t;
  private temporaire2[] scalefac;
  private int max_gr;
  private int frame_start;
  private int part2_start;
  private int channels;
  private int first_channel;
  private int last_channel;
  private int sfreq;
  private float[] samples1 = new float[32];
  private float[] samples2 = new float[32];
  private final int[] new_slen = new int[4];
  int[] x = { 0 };
  int[] y = { 0 };
  int[] v = { 0 };
  int[] w = { 0 };
  int[] is_pos = new int['ɀ'];
  float[] is_ratio = new float['ɀ'];
  float[] tsOutCopy = new float[18];
  float[] rawout = new float[36];
  private int counter = 0;
  private static final int SSLIMIT = 18;
  private static final int SBLIMIT = 32;
  private static final int[][] slen = { { 0, 0, 0, 0, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4 }, { 0, 1, 2, 3, 0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 2, 3 } };
  public static final int[] pretab = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 3, 2, 0 };
  private SBI[] sfBandIndex;
  public static final float[] two_to_negative_half_pow = { 1.0F, 0.70710677F, 0.5F, 0.35355338F, 0.25F, 0.17677669F, 0.125F, 0.088388346F, 0.0625F, 0.044194173F, 0.03125F, 0.022097087F, 0.015625F, 0.011048543F, 0.0078125F, 0.0055242716F, 0.00390625F, 0.0027621358F, 0.001953125F, 0.0013810679F, 9.765625E-4F, 6.9053395E-4F, 4.8828125E-4F, 3.4526698E-4F, 2.4414062E-4F, 1.7263349E-4F, 1.2207031E-4F, 8.6316744E-5F, 6.1035156E-5F, 4.3158372E-5F, 3.0517578E-5F, 2.1579186E-5F, 1.5258789E-5F, 1.0789593E-5F, 7.6293945E-6F, 5.3947965E-6F, 3.8146973E-6F, 2.6973983E-6F, 1.9073486E-6F, 1.3486991E-6F, 9.536743E-7F, 6.7434956E-7F, 4.7683716E-7F, 3.3717478E-7F, 2.3841858E-7F, 1.6858739E-7F, 1.1920929E-7F, 8.4293696E-8F, 5.9604645E-8F, 4.2146848E-8F, 2.9802322E-8F, 2.1073424E-8F, 1.4901161E-8F, 1.0536712E-8F, 7.4505806E-9F, 5.268356E-9F, 3.7252903E-9F, 2.634178E-9F, 1.8626451E-9F, 1.317089E-9F, 9.313226E-10F, 6.585445E-10F, 4.656613E-10F, 3.2927225E-10F };
  public static final float[] t_43 = create_t_43();
  public static final float[][] io = { { 1.0F, 0.8408964F, 0.70710677F, 0.59460354F, 0.5F, 0.4204482F, 0.35355338F, 0.29730177F, 0.25F, 0.2102241F, 0.17677669F, 0.14865088F, 0.125F, 0.10511205F, 0.088388346F, 0.07432544F, 0.0625F, 0.052556027F, 0.044194173F, 0.03716272F, 0.03125F, 0.026278013F, 0.022097087F, 0.01858136F, 0.015625F, 0.013139007F, 0.011048543F, 0.00929068F, 0.0078125F, 0.0065695033F, 0.0055242716F, 0.00464534F }, { 1.0F, 0.70710677F, 0.5F, 0.35355338F, 0.25F, 0.17677669F, 0.125F, 0.088388346F, 0.0625F, 0.044194173F, 0.03125F, 0.022097087F, 0.015625F, 0.011048543F, 0.0078125F, 0.0055242716F, 0.00390625F, 0.0027621358F, 0.001953125F, 0.0013810679F, 9.765625E-4F, 6.9053395E-4F, 4.8828125E-4F, 3.4526698E-4F, 2.4414062E-4F, 1.7263349E-4F, 1.2207031E-4F, 8.6316744E-5F, 6.1035156E-5F, 4.3158372E-5F, 3.0517578E-5F, 2.1579186E-5F } };
  public static final float[] TAN12 = { 0.0F, 0.2679492F, 0.57735026F, 1.0F, 1.7320508F, 3.732051F, 9.9999998E10F, -3.732051F, -1.7320508F, -1.0F, -0.57735026F, -0.2679492F, 0.0F, 0.2679492F, 0.57735026F, 1.0F };
  private static int[][] reorder_table;
  private static final float[] cs = { 0.8574929F, 0.881742F, 0.94962865F, 0.9833146F, 0.9955178F, 0.9991606F, 0.9998992F, 0.99999315F };
  private static final float[] ca = { -0.51449573F, -0.47173196F, -0.31337744F, -0.1819132F, -0.09457419F, -0.040965583F, -0.014198569F, -0.0036999746F };
  public static final float[][] win = { { -0.016141215F, -0.05360318F, -0.100707136F, -0.16280818F, -0.5F, -0.38388735F, -0.6206114F, -1.1659756F, -3.8720753F, -4.225629F, -1.519529F, -0.97416484F, -0.73744076F, -1.2071068F, -0.5163616F, -0.45426053F, -0.40715656F, -0.3696946F, -0.3387627F, -0.31242222F, -0.28939587F, -0.26880082F, -0.5F, -0.23251417F, -0.21596715F, -0.20004979F, -0.18449493F, -0.16905846F, -0.15350361F, -0.13758625F, -0.12103922F, -0.20710678F, -0.084752575F, -0.06415752F, -0.041131172F, -0.014790705F }, { -0.016141215F, -0.05360318F, -0.100707136F, -0.16280818F, -0.5F, -0.38388735F, -0.6206114F, -1.1659756F, -3.8720753F, -4.225629F, -1.519529F, -0.97416484F, -0.73744076F, -1.2071068F, -0.5163616F, -0.45426053F, -0.40715656F, -0.3696946F, -0.33908543F, -0.3151181F, -0.29642227F, -0.28184548F, -0.5411961F, -0.2621323F, -0.25387916F, -0.2329629F, -0.19852729F, -0.15233535F, -0.0964964F, -0.03342383F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { -0.0483008F, -0.15715657F, -0.28325045F, -0.42953748F, -1.2071068F, -0.8242648F, -1.1451749F, -1.769529F, -4.5470223F, -3.489053F, -0.7329629F, -0.15076515F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F }, { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.15076514F, -0.7329629F, -3.489053F, -4.5470223F, -1.769529F, -1.1451749F, -0.8313774F, -1.306563F, -0.54142016F, -0.46528974F, -0.4106699F, -0.3700468F, -0.3387627F, -0.31242222F, -0.28939587F, -0.26880082F, -0.5F, -0.23251417F, -0.21596715F, -0.20004979F, -0.18449493F, -0.16905846F, -0.15350361F, -0.13758625F, -0.12103922F, -0.20710678F, -0.084752575F, -0.06415752F, -0.041131172F, -0.014790705F } };
  public Sftable sftable;
  public static final int[][][] nr_of_sfb_block = { { { 6, 5, 5, 5 }, { 9, 9, 9, 9 }, { 6, 9, 9, 9 } }, { { 6, 5, 7, 3 }, { 9, 9, 12, 6 }, { 6, 9, 12, 6 } }, { { 11, 10, 0, 0 }, { 18, 18, 0, 0 }, { 15, 18, 0, 0 } }, { { 7, 7, 7, 0 }, { 12, 12, 12, 0 }, { 6, 15, 12, 0 } }, { { 6, 6, 6, 3 }, { 12, 9, 9, 6 }, { 6, 12, 9, 6 } }, { { 8, 8, 5, 0 }, { 15, 12, 9, 0 }, { 6, 18, 9, 0 } } };
  
  public LayerIIIDecoder(Bitstream paramBitstream, Header paramHeader, SynthesisFilter paramSynthesisFilter1, SynthesisFilter paramSynthesisFilter2, Obuffer paramObuffer, int paramInt)
  {
    huffcodetab.inithuff();
    is_1d = new int['Ʉ'];
    ro = new float[2][32][18];
    lr = new float[2][32][18];
    out_1d = new float['ɀ'];
    prevblck = new float[2]['ɀ'];
    k = new float[2]['ɀ'];
    nonzero = new int[2];
    III_scalefac_t = new temporaire2[2];
    III_scalefac_t[0] = new temporaire2();
    III_scalefac_t[1] = new temporaire2();
    scalefac = III_scalefac_t;
    sfBandIndex = new SBI[9];
    int[] arrayOfInt1 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
    int[] arrayOfInt2 = { 0, 4, 8, 12, 18, 24, 32, 42, 56, 74, 100, 132, 174, 192 };
    int[] arrayOfInt3 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 114, 136, 162, 194, 232, 278, 330, 394, 464, 540, 576 };
    int[] arrayOfInt4 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 136, 180, 192 };
    int[] arrayOfInt5 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
    int[] arrayOfInt6 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
    int[] arrayOfInt7 = { 0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 52, 62, 74, 90, 110, 134, 162, 196, 238, 288, 342, 418, 576 };
    int[] arrayOfInt8 = { 0, 4, 8, 12, 16, 22, 30, 40, 52, 66, 84, 106, 136, 192 };
    int[] arrayOfInt9 = { 0, 4, 8, 12, 16, 20, 24, 30, 36, 42, 50, 60, 72, 88, 106, 128, 156, 190, 230, 276, 330, 384, 576 };
    int[] arrayOfInt10 = { 0, 4, 8, 12, 16, 22, 28, 38, 50, 64, 80, 100, 126, 192 };
    int[] arrayOfInt11 = { 0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 54, 66, 82, 102, 126, 156, 194, 240, 296, 364, 448, 550, 576 };
    int[] arrayOfInt12 = { 0, 4, 8, 12, 16, 22, 30, 42, 58, 78, 104, 138, 180, 192 };
    int[] arrayOfInt13 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
    int[] arrayOfInt14 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
    int[] arrayOfInt15 = { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
    int[] arrayOfInt16 = { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
    int[] arrayOfInt17 = { 0, 12, 24, 36, 48, 60, 72, 88, 108, 132, 160, 192, 232, 280, 336, 400, 476, 566, 568, 570, 572, 574, 576 };
    int[] arrayOfInt18 = { 0, 8, 16, 24, 36, 52, 72, 96, 124, 160, 162, 164, 166, 192 };
    sfBandIndex[0] = new SBI(arrayOfInt1, arrayOfInt2);
    sfBandIndex[1] = new SBI(arrayOfInt3, arrayOfInt4);
    sfBandIndex[2] = new SBI(arrayOfInt5, arrayOfInt6);
    sfBandIndex[3] = new SBI(arrayOfInt7, arrayOfInt8);
    sfBandIndex[4] = new SBI(arrayOfInt9, arrayOfInt10);
    sfBandIndex[5] = new SBI(arrayOfInt11, arrayOfInt12);
    sfBandIndex[6] = new SBI(arrayOfInt13, arrayOfInt14);
    sfBandIndex[7] = new SBI(arrayOfInt15, arrayOfInt16);
    sfBandIndex[8] = new SBI(arrayOfInt17, arrayOfInt18);
    if (reorder_table == null)
    {
      reorder_table = new int[9][];
      for (int i = 0; i < 9; i++) {
        reorder_table[i] = reorder(sfBandIndex[i].s);
      }
    }
    int[] arrayOfInt19 = { 0, 6, 11, 16, 21 };
    int[] arrayOfInt20 = { 0, 6, 12 };
    sftable = new Sftable(arrayOfInt19, arrayOfInt20);
    scalefac_buffer = new int[54];
    stream = paramBitstream;
    header = paramHeader;
    filter1 = paramSynthesisFilter1;
    filter2 = paramSynthesisFilter2;
    buffer = paramObuffer;
    which_channels = paramInt;
    frame_start = 0;
    channels = (header.mode() == 3 ? 1 : 2);
    max_gr = (header.version() == 1 ? 2 : 1);
    sfreq = (header.sample_frequency() + (header.version() == 2 ? 6 : header.version() == 1 ? 3 : 0));
    if (channels == 2) {
      switch (which_channels)
      {
      case 1: 
      case 3: 
        first_channel = (this.last_channel = 0);
        break;
      case 2: 
        first_channel = (this.last_channel = 1);
        break;
      case 0: 
      default: 
        first_channel = 0;
        last_channel = 1;
        break;
      }
    } else {
      first_channel = (this.last_channel = 0);
    }
    for (int j = 0; j < 2; j++) {
      for (int m = 0; m < 576; m++) {
        prevblck[j][m] = 0.0F;
      }
    }
    nonzero[1] = 'ɀ';
    nonzero[0] = 'ɀ';
    br = new BitReserve();
    si = new III_side_info_t();
  }
  
  public void seek_notify()
  {
    frame_start = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 576; j++) {
        prevblck[i][j] = 0.0F;
      }
    }
    br = new BitReserve();
  }
  
  public void decodeFrame()
  {
    decode();
  }
  
  public void decode()
  {
    int i = header.slots();
    get_side_info();
    for (int i6 = 0; i6 < i; i6++) {
      br.hputbuf(stream.get_bits(8));
    }
    int i4 = br.hsstell() >>> 3;
    int j;
    if ((j = br.hsstell() & 0x7) != 0)
    {
      br.hgetbits(8 - j);
      i4++;
    }
    int i5 = frame_start - i4 - si.main_data_begin;
    frame_start += i;
    if (i5 < 0) {
      return;
    }
    if (i4 > 4096)
    {
      frame_start -= 4096;
      br.rewindNbytes(4096);
    }
    while (i5 > 0)
    {
      br.hgetbits(8);
      i5--;
    }
    for (int m = 0; m < max_gr; m++)
    {
      for (int n = 0; n < channels; n++)
      {
        part2_start = br.hsstell();
        if (header.version() == 1) {
          get_scale_factors(n, m);
        } else {
          get_LSF_scale_factors(n, m);
        }
        huffman_decode(n, m);
        dequantize_sample(ro[n], n, m);
      }
      stereo(m);
      if ((which_channels == 3) && (channels > 1)) {
        do_downmix();
      }
      for (n = first_channel; n <= last_channel; n++)
      {
        reorder(lr[n], n, m);
        antialias(n, m);
        hybrid(n, m);
        int i1;
        for (int i3 = 18; i3 < 576; i3 += 36) {
          for (i1 = 1; i1 < 18; i1 += 2) {
            out_1d[(i3 + i1)] = (-out_1d[(i3 + i1)]);
          }
        }
        if ((n == 0) || (which_channels == 2)) {
          i1 = 0;
        }
        while (i1 < 18)
        {
          int i2 = 0;
          for (i3 = 0; i3 < 576; i3 += 18)
          {
            samples1[i2] = out_1d[(i3 + i1)];
            i2++;
          }
          filter1.input_samples(samples1);
          filter1.calculate_pcm_samples(buffer);
          i1++;
          continue;
          for (i1 = 0; i1 < 18; i1++)
          {
            i2 = 0;
            for (i3 = 0; i3 < 576; i3 += 18)
            {
              samples2[i2] = out_1d[(i3 + i1)];
              i2++;
            }
            filter2.input_samples(samples2);
            filter2.calculate_pcm_samples(buffer);
          }
        }
      }
    }
    counter += 1;
    buffer.write_buffer(1);
  }
  
  private boolean get_side_info()
  {
    if (header.version() == 1)
    {
      si.main_data_begin = stream.get_bits(9);
      if (channels == 1) {
        si.private_bits = stream.get_bits(5);
      } else {
        si.private_bits = stream.get_bits(3);
      }
      for (i = 0; i < channels; i++)
      {
        si.ch[i].scfsi[0] = stream.get_bits(1);
        si.ch[i].scfsi[1] = stream.get_bits(1);
        si.ch[i].scfsi[2] = stream.get_bits(1);
        si.ch[i].scfsi[3] = stream.get_bits(1);
      }
      for (int j = 0; j < 2; j++) {
        for (i = 0; i < channels; i++)
        {
          si.ch[i].gr[j].part2_3_length = stream.get_bits(12);
          si.ch[i].gr[j].big_values = stream.get_bits(9);
          si.ch[i].gr[j].global_gain = stream.get_bits(8);
          si.ch[i].gr[j].scalefac_compress = stream.get_bits(4);
          si.ch[i].gr[j].window_switching_flag = stream.get_bits(1);
          if (si.ch[i].gr[j].window_switching_flag != 0)
          {
            si.ch[i].gr[j].block_type = stream.get_bits(2);
            si.ch[i].gr[j].mixed_block_flag = stream.get_bits(1);
            si.ch[i].gr[j].table_select[0] = stream.get_bits(5);
            si.ch[i].gr[j].table_select[1] = stream.get_bits(5);
            si.ch[i].gr[j].subblock_gain[0] = stream.get_bits(3);
            si.ch[i].gr[j].subblock_gain[1] = stream.get_bits(3);
            si.ch[i].gr[j].subblock_gain[2] = stream.get_bits(3);
            if (si.ch[i].gr[j].block_type == 0) {
              return false;
            }
            if ((si.ch[i].gr[j].block_type == 2) && (si.ch[i].gr[j].mixed_block_flag == 0)) {
              si.ch[i].gr[j].region0_count = 8;
            } else {
              si.ch[i].gr[j].region0_count = 7;
            }
            si.ch[i].gr[j].region1_count = (20 - si.ch[i].gr[j].region0_count);
          }
          else
          {
            si.ch[i].gr[j].table_select[0] = stream.get_bits(5);
            si.ch[i].gr[j].table_select[1] = stream.get_bits(5);
            si.ch[i].gr[j].table_select[2] = stream.get_bits(5);
            si.ch[i].gr[j].region0_count = stream.get_bits(4);
            si.ch[i].gr[j].region1_count = stream.get_bits(3);
            si.ch[i].gr[j].block_type = 0;
          }
          si.ch[i].gr[j].preflag = stream.get_bits(1);
          si.ch[i].gr[j].scalefac_scale = stream.get_bits(1);
          si.ch[i].gr[j].count1table_select = stream.get_bits(1);
        }
      }
    }
    si.main_data_begin = stream.get_bits(8);
    if (channels == 1) {
      si.private_bits = stream.get_bits(1);
    } else {
      si.private_bits = stream.get_bits(2);
    }
    for (int i = 0; i < channels; i++)
    {
      si.ch[i].gr[0].part2_3_length = stream.get_bits(12);
      si.ch[i].gr[0].big_values = stream.get_bits(9);
      si.ch[i].gr[0].global_gain = stream.get_bits(8);
      si.ch[i].gr[0].scalefac_compress = stream.get_bits(9);
      si.ch[i].gr[0].window_switching_flag = stream.get_bits(1);
      if (si.ch[i].gr[0].window_switching_flag != 0)
      {
        si.ch[i].gr[0].block_type = stream.get_bits(2);
        si.ch[i].gr[0].mixed_block_flag = stream.get_bits(1);
        si.ch[i].gr[0].table_select[0] = stream.get_bits(5);
        si.ch[i].gr[0].table_select[1] = stream.get_bits(5);
        si.ch[i].gr[0].subblock_gain[0] = stream.get_bits(3);
        si.ch[i].gr[0].subblock_gain[1] = stream.get_bits(3);
        si.ch[i].gr[0].subblock_gain[2] = stream.get_bits(3);
        if (si.ch[i].gr[0].block_type == 0) {
          return false;
        }
        if ((si.ch[i].gr[0].block_type == 2) && (si.ch[i].gr[0].mixed_block_flag == 0))
        {
          si.ch[i].gr[0].region0_count = 8;
        }
        else
        {
          si.ch[i].gr[0].region0_count = 7;
          si.ch[i].gr[0].region1_count = (20 - si.ch[i].gr[0].region0_count);
        }
      }
      else
      {
        si.ch[i].gr[0].table_select[0] = stream.get_bits(5);
        si.ch[i].gr[0].table_select[1] = stream.get_bits(5);
        si.ch[i].gr[0].table_select[2] = stream.get_bits(5);
        si.ch[i].gr[0].region0_count = stream.get_bits(4);
        si.ch[i].gr[0].region1_count = stream.get_bits(3);
        si.ch[i].gr[0].block_type = 0;
      }
      si.ch[i].gr[0].scalefac_scale = stream.get_bits(1);
      si.ch[i].gr[0].count1table_select = stream.get_bits(1);
    }
    return true;
  }
  
  private void get_scale_factors(int paramInt1, int paramInt2)
  {
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    int m = scalefac_compress;
    int n = slen[0][m];
    int i1 = slen[1][m];
    if ((window_switching_flag != 0) && (block_type == 2))
    {
      if (mixed_block_flag != 0)
      {
        for (int i = 0; i < 8; i++) {
          scalefac[paramInt1].l[i] = br.hgetbits(slen[0][scalefac_compress]);
        }
        for (i = 3; i < 6; i++) {
          for (j = 0; j < 3; j++) {
            scalefac[paramInt1].s[j][i] = br.hgetbits(slen[0][scalefac_compress]);
          }
        }
        for (i = 6; i < 12; i++) {
          for (j = 0; j < 3; j++) {
            scalefac[paramInt1].s[j][i] = br.hgetbits(slen[1][scalefac_compress]);
          }
        }
        i = 12;
        for (int j = 0; j < 3; j++) {
          scalefac[paramInt1].s[j][i] = 0;
        }
      }
      scalefac[paramInt1].s[0][0] = br.hgetbits(n);
      scalefac[paramInt1].s[1][0] = br.hgetbits(n);
      scalefac[paramInt1].s[2][0] = br.hgetbits(n);
      scalefac[paramInt1].s[0][1] = br.hgetbits(n);
      scalefac[paramInt1].s[1][1] = br.hgetbits(n);
      scalefac[paramInt1].s[2][1] = br.hgetbits(n);
      scalefac[paramInt1].s[0][2] = br.hgetbits(n);
      scalefac[paramInt1].s[1][2] = br.hgetbits(n);
      scalefac[paramInt1].s[2][2] = br.hgetbits(n);
      scalefac[paramInt1].s[0][3] = br.hgetbits(n);
      scalefac[paramInt1].s[1][3] = br.hgetbits(n);
      scalefac[paramInt1].s[2][3] = br.hgetbits(n);
      scalefac[paramInt1].s[0][4] = br.hgetbits(n);
      scalefac[paramInt1].s[1][4] = br.hgetbits(n);
      scalefac[paramInt1].s[2][4] = br.hgetbits(n);
      scalefac[paramInt1].s[0][5] = br.hgetbits(n);
      scalefac[paramInt1].s[1][5] = br.hgetbits(n);
      scalefac[paramInt1].s[2][5] = br.hgetbits(n);
      scalefac[paramInt1].s[0][6] = br.hgetbits(i1);
      scalefac[paramInt1].s[1][6] = br.hgetbits(i1);
      scalefac[paramInt1].s[2][6] = br.hgetbits(i1);
      scalefac[paramInt1].s[0][7] = br.hgetbits(i1);
      scalefac[paramInt1].s[1][7] = br.hgetbits(i1);
      scalefac[paramInt1].s[2][7] = br.hgetbits(i1);
      scalefac[paramInt1].s[0][8] = br.hgetbits(i1);
      scalefac[paramInt1].s[1][8] = br.hgetbits(i1);
      scalefac[paramInt1].s[2][8] = br.hgetbits(i1);
      scalefac[paramInt1].s[0][9] = br.hgetbits(i1);
      scalefac[paramInt1].s[1][9] = br.hgetbits(i1);
      scalefac[paramInt1].s[2][9] = br.hgetbits(i1);
      scalefac[paramInt1].s[0][10] = br.hgetbits(i1);
      scalefac[paramInt1].s[1][10] = br.hgetbits(i1);
      scalefac[paramInt1].s[2][10] = br.hgetbits(i1);
      scalefac[paramInt1].s[0][11] = br.hgetbits(i1);
      scalefac[paramInt1].s[1][11] = br.hgetbits(i1);
      scalefac[paramInt1].s[2][11] = br.hgetbits(i1);
      scalefac[paramInt1].s[0][12] = 0;
      scalefac[paramInt1].s[1][12] = 0;
      scalefac[paramInt1].s[2][12] = 0;
    }
    else
    {
      if ((si.ch[paramInt1].scfsi[0] == 0) || (paramInt2 == 0))
      {
        scalefac[paramInt1].l[0] = br.hgetbits(n);
        scalefac[paramInt1].l[1] = br.hgetbits(n);
        scalefac[paramInt1].l[2] = br.hgetbits(n);
        scalefac[paramInt1].l[3] = br.hgetbits(n);
        scalefac[paramInt1].l[4] = br.hgetbits(n);
        scalefac[paramInt1].l[5] = br.hgetbits(n);
      }
      if ((si.ch[paramInt1].scfsi[1] == 0) || (paramInt2 == 0))
      {
        scalefac[paramInt1].l[6] = br.hgetbits(n);
        scalefac[paramInt1].l[7] = br.hgetbits(n);
        scalefac[paramInt1].l[8] = br.hgetbits(n);
        scalefac[paramInt1].l[9] = br.hgetbits(n);
        scalefac[paramInt1].l[10] = br.hgetbits(n);
      }
      if ((si.ch[paramInt1].scfsi[2] == 0) || (paramInt2 == 0))
      {
        scalefac[paramInt1].l[11] = br.hgetbits(i1);
        scalefac[paramInt1].l[12] = br.hgetbits(i1);
        scalefac[paramInt1].l[13] = br.hgetbits(i1);
        scalefac[paramInt1].l[14] = br.hgetbits(i1);
        scalefac[paramInt1].l[15] = br.hgetbits(i1);
      }
      if ((si.ch[paramInt1].scfsi[3] == 0) || (paramInt2 == 0))
      {
        scalefac[paramInt1].l[16] = br.hgetbits(i1);
        scalefac[paramInt1].l[17] = br.hgetbits(i1);
        scalefac[paramInt1].l[18] = br.hgetbits(i1);
        scalefac[paramInt1].l[19] = br.hgetbits(i1);
        scalefac[paramInt1].l[20] = br.hgetbits(i1);
      }
      scalefac[paramInt1].l[21] = 0;
      scalefac[paramInt1].l[22] = 0;
    }
  }
  
  private void get_LSF_scale_data(int paramInt1, int paramInt2)
  {
    int m = header.mode_extension();
    int i2 = 0;
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    int i = scalefac_compress;
    int i1;
    if (block_type == 2)
    {
      if (mixed_block_flag == 0) {
        i1 = 1;
      } else if (mixed_block_flag == 1) {
        i1 = 2;
      } else {
        i1 = 0;
      }
    }
    else {
      i1 = 0;
    }
    if (((m != 1) && (m != 3)) || (paramInt1 != 1)) {
      if (i < 400)
      {
        new_slen[0] = ((i >>> 4) / 5);
        new_slen[1] = ((i >>> 4) % 5);
        new_slen[2] = ((i & 0xF) >>> 2);
        new_slen[3] = (i & 0x3);
        si.ch[paramInt1].gr[paramInt2].preflag = 0;
        i2 = 0;
      }
      else if (i < 500)
      {
        new_slen[0] = ((i - 400 >>> 2) / 5);
        new_slen[1] = ((i - 400 >>> 2) % 5);
        new_slen[2] = (i - 400 & 0x3);
        new_slen[3] = 0;
        si.ch[paramInt1].gr[paramInt2].preflag = 0;
        i2 = 1;
      }
      else if (i < 512)
      {
        new_slen[0] = ((i - 500) / 3);
        new_slen[1] = ((i - 500) % 3);
        new_slen[2] = 0;
        new_slen[3] = 0;
        si.ch[paramInt1].gr[paramInt2].preflag = 1;
        i2 = 2;
      }
    }
    if (((m == 1) || (m == 3)) && (paramInt1 == 1))
    {
      int j = i >>> 1;
      if (j < 180)
      {
        new_slen[0] = (j / 36);
        new_slen[1] = (j % 36 / 6);
        new_slen[2] = (j % 36 % 6);
        new_slen[3] = 0;
        si.ch[paramInt1].gr[paramInt2].preflag = 0;
        i2 = 3;
      }
      else if (j < 244)
      {
        new_slen[0] = ((j - 180 & 0x3F) >>> 4);
        new_slen[1] = ((j - 180 & 0xF) >>> 2);
        new_slen[2] = (j - 180 & 0x3);
        new_slen[3] = 0;
        si.ch[paramInt1].gr[paramInt2].preflag = 0;
        i2 = 4;
      }
      else if (j < 255)
      {
        new_slen[0] = ((j - 244) / 3);
        new_slen[1] = ((j - 244) % 3);
        new_slen[2] = 0;
        new_slen[3] = 0;
        si.ch[paramInt1].gr[paramInt2].preflag = 0;
        i2 = 5;
      }
    }
    for (int i3 = 0; i3 < 45; i3++) {
      scalefac_buffer[i3] = 0;
    }
    int n = 0;
    for (i3 = 0; i3 < 4; i3++) {
      for (int i4 = 0; i4 < nr_of_sfb_block[i2][i1][i3]; i4++)
      {
        scalefac_buffer[n] = (new_slen[i3] == 0 ? 0 : br.hgetbits(new_slen[i3]));
        n++;
      }
    }
  }
  
  private void get_LSF_scale_factors(int paramInt1, int paramInt2)
  {
    int i = 0;
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    get_LSF_scale_data(paramInt1, paramInt2);
    int j;
    int m;
    if ((window_switching_flag != 0) && (block_type == 2))
    {
      if (mixed_block_flag != 0)
      {
        for (j = 0; j < 8; j++)
        {
          scalefac[paramInt1].l[j] = scalefac_buffer[i];
          i++;
        }
        for (j = 3; j < 12; j++) {
          for (m = 0; m < 3; m++)
          {
            scalefac[paramInt1].s[m][j] = scalefac_buffer[i];
            i++;
          }
        }
        for (m = 0; m < 3; m++) {
          scalefac[paramInt1].s[m][12] = 0;
        }
      }
      for (j = 0; j < 12; j++) {
        for (m = 0; m < 3; m++)
        {
          scalefac[paramInt1].s[m][j] = scalefac_buffer[i];
          i++;
        }
      }
      m = 0;
    }
    while (m < 3)
    {
      scalefac[paramInt1].s[m][12] = 0;
      m++;
      continue;
      for (j = 0; j < 21; j++)
      {
        scalefac[paramInt1].l[j] = scalefac_buffer[i];
        i++;
      }
      scalefac[paramInt1].l[21] = 0;
      scalefac[paramInt1].l[22] = 0;
    }
  }
  
  private void huffman_decode(int paramInt1, int paramInt2)
  {
    x[0] = 0;
    y[0] = 0;
    v[0] = 0;
    w[0] = 0;
    int i = part2_start + si.ch[paramInt1].gr[paramInt2].part2_3_length;
    int m;
    int n;
    if ((si.ch[paramInt1].gr[paramInt2].window_switching_flag != 0) && (si.ch[paramInt1].gr[paramInt2].block_type == 2))
    {
      m = sfreq == 8 ? 72 : 36;
      n = 576;
    }
    else
    {
      int i2 = si.ch[paramInt1].gr[paramInt2].region0_count + 1;
      int i3 = i2 + si.ch[paramInt1].gr[paramInt2].region1_count + 1;
      if (i3 > sfBandIndex[sfreq].l.length - 1) {
        i3 = sfBandIndex[sfreq].l.length - 1;
      }
      m = sfBandIndex[sfreq].l[i2];
      n = sfBandIndex[sfreq].l[i3];
    }
    int i1 = 0;
    for (int i4 = 0; i4 < si.ch[paramInt1].gr[paramInt2].big_values << 1; i4 += 2)
    {
      if (i4 < m) {
        localHuffcodetab = huffcodetab.ht[si.ch[paramInt1].gr[paramInt2].table_select[0]];
      } else if (i4 < n) {
        localHuffcodetab = huffcodetab.ht[si.ch[paramInt1].gr[paramInt2].table_select[1]];
      } else {
        localHuffcodetab = huffcodetab.ht[si.ch[paramInt1].gr[paramInt2].table_select[2]];
      }
      huffcodetab.huffman_decoder(localHuffcodetab, x, y, v, w, br);
      is_1d[(i1++)] = x[0];
      is_1d[(i1++)] = y[0];
      CheckSumHuff = (CheckSumHuff + x[0] + y[0]);
    }
    huffcodetab localHuffcodetab = huffcodetab.ht[(si.ch[paramInt1].gr[paramInt2].count1table_select + 32)];
    for (int j = br.hsstell(); (j < i) && (i1 < 576); j = br.hsstell())
    {
      huffcodetab.huffman_decoder(localHuffcodetab, x, y, v, w, br);
      is_1d[(i1++)] = v[0];
      is_1d[(i1++)] = w[0];
      is_1d[(i1++)] = x[0];
      is_1d[(i1++)] = y[0];
      CheckSumHuff = (CheckSumHuff + v[0] + w[0] + x[0] + y[0]);
    }
    if (j > i)
    {
      br.rewindNbits(j - i);
      i1 -= 4;
    }
    j = br.hsstell();
    if (j < i) {
      br.hgetbits(i - j);
    }
    if (i1 < 576) {
      nonzero[paramInt1] = i1;
    } else {
      nonzero[paramInt1] = 576;
    }
    if (i1 < 0) {}
    for (i1 = 0; i1 < 576; i1++) {
      is_1d[i1] = 0;
    }
  }
  
  private void i_stereo_k_values(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 == 0)
    {
      k[0][paramInt3] = 1.0F;
      k[1][paramInt3] = 1.0F;
    }
    else if ((paramInt1 & 0x1) != 0)
    {
      k[0][paramInt3] = io[paramInt2][(paramInt1 + 1 >>> 1)];
      k[1][paramInt3] = 1.0F;
    }
    else
    {
      k[0][paramInt3] = 1.0F;
      k[1][paramInt3] = io[paramInt2][(paramInt1 >>> 1)];
    }
  }
  
  private void dequantize_sample(float[][] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    int i = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    float[][] arrayOfFloat = paramArrayOfFloat;
    int j;
    if ((window_switching_flag != 0) && (block_type == 2))
    {
      if (mixed_block_flag != 0)
      {
        j = sfBandIndex[sfreq].l[1];
      }
      else
      {
        n = sfBandIndex[sfreq].s[1];
        j = (n << 2) - n;
        m = 0;
      }
    }
    else {
      j = sfBandIndex[sfreq].l[1];
    }
    float f = (float)Math.pow(2.0D, 0.25D * (global_gain - 210.0D));
    int i4;
    int i5;
    int i6;
    for (int i3 = 0; i3 < nonzero[paramInt1]; i3++)
    {
      i4 = i3 % 18;
      i5 = (i3 - i4) / 18;
      if (is_1d[i3] == 0)
      {
        arrayOfFloat[i5][i4] = 0.0F;
      }
      else
      {
        i6 = is_1d[i3];
        if (i6 < t_43.length)
        {
          if (is_1d[i3] > 0) {
            arrayOfFloat[i5][i4] = (f * t_43[i6]);
          } else if (-i6 < t_43.length) {
            arrayOfFloat[i5][i4] = (-f * t_43[(-i6)]);
          } else {
            arrayOfFloat[i5][i4] = (-f * (float)Math.pow(-i6, 1.3333333333333333D));
          }
        }
        else if (is_1d[i3] > 0) {
          arrayOfFloat[i5][i4] = (f * (float)Math.pow(i6, 1.3333333333333333D));
        } else {
          arrayOfFloat[i5][i4] = (-f * (float)Math.pow(-i6, 1.3333333333333333D));
        }
      }
    }
    for (i3 = 0; i3 < nonzero[paramInt1]; i3++)
    {
      i4 = i3 % 18;
      i5 = (i3 - i4) / 18;
      if (i1 == j) {
        if ((window_switching_flag != 0) && (block_type == 2))
        {
          if (mixed_block_flag != 0)
          {
            if (i1 == sfBandIndex[sfreq].l[8])
            {
              j = sfBandIndex[sfreq].s[4];
              j = (j << 2) - j;
              i = 3;
              n = sfBandIndex[sfreq].s[4] - sfBandIndex[sfreq].s[3];
              m = sfBandIndex[sfreq].s[3];
              m = (m << 2) - m;
            }
            else if (i1 < sfBandIndex[sfreq].l[8])
            {
              j = sfBandIndex[sfreq].l[(++i + 1)];
            }
            else
            {
              j = sfBandIndex[sfreq].s[(++i + 1)];
              j = (j << 2) - j;
              m = sfBandIndex[sfreq].s[i];
              n = sfBandIndex[sfreq].s[(i + 1)] - m;
              m = (m << 2) - m;
            }
          }
          else
          {
            j = sfBandIndex[sfreq].s[(++i + 1)];
            j = (j << 2) - j;
            m = sfBandIndex[sfreq].s[i];
            n = sfBandIndex[sfreq].s[(i + 1)] - m;
            m = (m << 2) - m;
          }
        }
        else {
          j = sfBandIndex[sfreq].l[(++i + 1)];
        }
      }
      if ((window_switching_flag != 0) && (((block_type == 2) && (mixed_block_flag == 0)) || ((block_type == 2) && (mixed_block_flag != 0) && (i3 >= 36))))
      {
        int i2 = (i1 - m) / n;
        i6 = scalefac[paramInt1].s[i2][i] << scalefac_scale;
        i6 += (subblock_gain[i2] << 2);
        arrayOfFloat[i5][i4] *= two_to_negative_half_pow[i6];
      }
      else
      {
        i6 = scalefac[paramInt1].l[i];
        if (preflag != 0) {
          i6 += pretab[i];
        }
        i6 <<= scalefac_scale;
        arrayOfFloat[i5][i4] *= two_to_negative_half_pow[i6];
      }
      i1++;
    }
    for (i3 = nonzero[paramInt1]; i3 < 576; i3++)
    {
      i4 = i3 % 18;
      i5 = (i3 - i4) / 18;
      if (i4 < 0) {
        i4 = 0;
      }
      if (i5 < 0) {
        i5 = 0;
      }
      arrayOfFloat[i5][i4] = 0.0F;
    }
  }
  
  private void reorder(float[][] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    float[][] arrayOfFloat = paramArrayOfFloat;
    int m;
    int i5;
    int i6;
    int i7;
    if ((window_switching_flag != 0) && (block_type == 2))
    {
      for (m = 0; m < 576; m++) {
        out_1d[m] = 0.0F;
      }
      if (mixed_block_flag != 0)
      {
        for (m = 0; m < 36; m++)
        {
          i5 = m % 18;
          i6 = (m - i5) / 18;
          out_1d[m] = arrayOfFloat[i6][i5];
        }
        for (int n = 3; n < 13; n++)
        {
          int i1 = sfBandIndex[sfreq].s[n];
          int i2 = sfBandIndex[sfreq].s[(n + 1)] - i1;
          i5 = (i1 << 2) - i1;
          int i = 0;
          for (int j = 0; i < i2; j += 3)
          {
            int i3 = i5 + i;
            int i4 = i5 + j;
            i6 = i3 % 18;
            i7 = (i3 - i6) / 18;
            out_1d[i4] = arrayOfFloat[i7][i6];
            i3 += i2;
            i4++;
            i6 = i3 % 18;
            i7 = (i3 - i6) / 18;
            out_1d[i4] = arrayOfFloat[i7][i6];
            i3 += i2;
            i4++;
            i6 = i3 % 18;
            i7 = (i3 - i6) / 18;
            out_1d[i4] = arrayOfFloat[i7][i6];
            i++;
          }
        }
      }
      m = 0;
    }
    while (m < 576)
    {
      i5 = reorder_table[sfreq][m];
      i6 = i5 % 18;
      i7 = (i5 - i6) / 18;
      out_1d[m] = arrayOfFloat[i7][i6];
      m++;
      continue;
      for (m = 0; m < 576; m++)
      {
        i5 = m % 18;
        i6 = (m - i5) / 18;
        out_1d[m] = arrayOfFloat[i6][i5];
      }
    }
  }
  
  private void stereo(int paramInt)
  {
    int j;
    if (channels == 1) {
      for (i = 0; i < 32; i++) {
        for (j = 0; j < 18; j += 3)
        {
          lr[0][i][j] = ro[0][i][j];
          lr[0][i][(j + 1)] = ro[0][i][(j + 1)];
          lr[0][i][(j + 2)] = ro[0][i][(j + 2)];
        }
      }
    }
    gr_info_s localGr_info_s = si.ch[0].gr[paramInt];
    int m = header.mode_extension();
    int i5 = (header.mode() == 1) && ((m & 0x2) != 0) ? 1 : 0;
    int i6 = (header.mode() == 1) && ((m & 0x1) != 0) ? 1 : 0;
    int i7 = (header.version() == 0) || (header.version() == 2) ? 1 : 0;
    int i8 = scalefac_compress & 0x1;
    for (int i1 = 0; i1 < 576; i1++)
    {
      is_pos[i1] = 7;
      is_ratio[i1] = 0.0F;
    }
    if (i6 != 0)
    {
      int i9;
      int i10;
      int n;
      int i2;
      int i3;
      if ((window_switching_flag != 0) && (block_type == 2))
      {
        if (mixed_block_flag != 0)
        {
          i9 = 0;
          for (i10 = 0; i10 < 3; i10++)
          {
            int i11 = 2;
            for (n = 12; n >= 3; n--)
            {
              i1 = sfBandIndex[sfreq].s[n];
              i2 = sfBandIndex[sfreq].s[(n + 1)] - i1;
              for (i1 = (i1 << 2) - i1 + (i10 + 1) * i2 - 1; i2 > 0; i1--)
              {
                if (ro[1][(i1 / 18)][(i1 % 18)] != 0.0F)
                {
                  i11 = n;
                  n = -10;
                  i2 = -10;
                }
                i2--;
              }
            }
            n = i11 + 1;
            if (n > i9) {
              i9 = n;
            }
            while (n < 12)
            {
              i3 = sfBandIndex[sfreq].s[n];
              i = sfBandIndex[sfreq].s[(n + 1)] - i3;
              i1 = (i3 << 2) - i3 + i10 * i;
              while (i > 0)
              {
                is_pos[i1] = scalefac[1].s[i10][n];
                if (is_pos[i1] != 7) {
                  if (i7 != 0) {
                    i_stereo_k_values(is_pos[i1], i8, i1);
                  } else {
                    is_ratio[i1] = TAN12[is_pos[i1]];
                  }
                }
                i1++;
                i--;
              }
              n++;
            }
            n = sfBandIndex[sfreq].s[10];
            i = sfBandIndex[sfreq].s[11] - n;
            n = (n << 2) - n + i10 * i;
            i3 = sfBandIndex[sfreq].s[11];
            i = sfBandIndex[sfreq].s[12] - i3;
            i1 = (i3 << 2) - i3 + i10 * i;
            while (i > 0)
            {
              is_pos[i1] = is_pos[n];
              if (i7 != 0)
              {
                k[0][i1] = k[0][n];
                k[1][i1] = k[1][n];
              }
              else
              {
                is_ratio[i1] = is_ratio[n];
              }
              i1++;
              i--;
            }
          }
          if (i9 <= 3)
          {
            i1 = 2;
            j = 17;
            i = -1;
            while (i1 >= 0) {
              if (ro[1][i1][j] != 0.0F)
              {
                i = (i1 << 4) + (i1 << 1) + j;
                i1 = -1;
              }
              else
              {
                j--;
                if (j < 0)
                {
                  i1--;
                  j = 17;
                }
              }
            }
            for (i1 = 0; sfBandIndex[sfreq].l[i1] <= i; i1++) {}
            n = i1;
            i1 = sfBandIndex[sfreq].l[i1];
            while (n < 8)
            {
              for (i = sfBandIndex[sfreq].l[(n + 1)] - sfBandIndex[sfreq].l[n]; i > 0; i--)
              {
                is_pos[i1] = scalefac[1].l[n];
                if (is_pos[i1] != 7) {
                  if (i7 != 0) {
                    i_stereo_k_values(is_pos[i1], i8, i1);
                  } else {
                    is_ratio[i1] = TAN12[is_pos[i1]];
                  }
                }
                i1++;
              }
              n++;
            }
          }
        }
        else
        {
          i9 = 0;
        }
      }
      else {
        while (i9 < 3)
        {
          i10 = -1;
          for (n = 12; n >= 0; n--)
          {
            i3 = sfBandIndex[sfreq].s[n];
            i2 = sfBandIndex[sfreq].s[(n + 1)] - i3;
            for (i1 = (i3 << 2) - i3 + (i9 + 1) * i2 - 1; i2 > 0; i1--)
            {
              if (ro[1][(i1 / 18)][(i1 % 18)] != 0.0F)
              {
                i10 = n;
                n = -10;
                i2 = -10;
              }
              i2--;
            }
          }
          for (n = i10 + 1; n < 12; n++)
          {
            i3 = sfBandIndex[sfreq].s[n];
            i = sfBandIndex[sfreq].s[(n + 1)] - i3;
            i1 = (i3 << 2) - i3 + i9 * i;
            while (i > 0)
            {
              is_pos[i1] = scalefac[1].s[i9][n];
              if (is_pos[i1] != 7) {
                if (i7 != 0) {
                  i_stereo_k_values(is_pos[i1], i8, i1);
                } else {
                  is_ratio[i1] = TAN12[is_pos[i1]];
                }
              }
              i1++;
              i--;
            }
          }
          i3 = sfBandIndex[sfreq].s[10];
          int i4 = sfBandIndex[sfreq].s[11];
          i = i4 - i3;
          n = (i3 << 2) - i3 + i9 * i;
          i = sfBandIndex[sfreq].s[12] - i4;
          i1 = (i4 << 2) - i4 + i9 * i;
          while (i > 0)
          {
            is_pos[i1] = is_pos[n];
            if (i7 != 0)
            {
              k[0][i1] = k[0][n];
              k[1][i1] = k[1][n];
            }
            else
            {
              is_ratio[i1] = is_ratio[n];
            }
            i1++;
            i--;
          }
          i9++;
          continue;
          i1 = 31;
          j = 17;
          i = 0;
          while (i1 >= 0) {
            if (ro[1][i1][j] != 0.0F)
            {
              i = (i1 << 4) + (i1 << 1) + j;
              i1 = -1;
            }
            else
            {
              j--;
              if (j < 0)
              {
                i1--;
                j = 17;
              }
            }
          }
          for (i1 = 0; sfBandIndex[sfreq].l[i1] <= i; i1++) {}
          n = i1;
          i1 = sfBandIndex[sfreq].l[i1];
          while (n < 21)
          {
            for (i = sfBandIndex[sfreq].l[(n + 1)] - sfBandIndex[sfreq].l[n]; i > 0; i--)
            {
              is_pos[i1] = scalefac[1].l[n];
              if (is_pos[i1] != 7) {
                if (i7 != 0) {
                  i_stereo_k_values(is_pos[i1], i8, i1);
                } else {
                  is_ratio[i1] = TAN12[is_pos[i1]];
                }
              }
              i1++;
            }
            n++;
          }
          n = sfBandIndex[sfreq].l[20];
          for (i = 576 - sfBandIndex[sfreq].l[21]; (i > 0) && (i1 < 576); i--)
          {
            is_pos[i1] = is_pos[n];
            if (i7 != 0)
            {
              k[0][i1] = k[0][n];
              k[1][i1] = k[1][n];
            }
            else
            {
              is_ratio[i1] = is_ratio[n];
            }
            i1++;
          }
        }
      }
    }
    i1 = 0;
    for (int i = 0; i < 32; i++) {
      for (j = 0; j < 18; j++)
      {
        if (is_pos[i1] == 7)
        {
          if (i5 != 0)
          {
            lr[0][i][j] = ((ro[0][i][j] + ro[1][i][j]) * 0.70710677F);
            lr[1][i][j] = ((ro[0][i][j] - ro[1][i][j]) * 0.70710677F);
          }
          else
          {
            lr[0][i][j] = ro[0][i][j];
            lr[1][i][j] = ro[1][i][j];
          }
        }
        else if (i6 != 0) {
          if (i7 != 0)
          {
            lr[0][i][j] = (ro[0][i][j] * k[0][i1]);
            lr[1][i][j] = (ro[0][i][j] * k[1][i1]);
          }
          else
          {
            lr[1][i][j] = (ro[0][i][j] / (1.0F + is_ratio[i1]));
            lr[0][i][j] = (lr[1][i][j] * is_ratio[i1]);
          }
        }
        i1++;
      }
    }
  }
  
  private void antialias(int paramInt1, int paramInt2)
  {
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    if ((window_switching_flag != 0) && (block_type == 2) && (mixed_block_flag == 0)) {
      return;
    }
    int m;
    if ((window_switching_flag != 0) && (mixed_block_flag != 0) && (block_type == 2)) {
      m = 18;
    } else {
      m = 558;
    }
    for (int i = 0; i < m; i += 18) {
      for (int j = 0; j < 8; j++)
      {
        int n = i + 17 - j;
        int i1 = i + 18 + j;
        float f1 = out_1d[n];
        float f2 = out_1d[i1];
        out_1d[n] = (f1 * cs[j] - f2 * ca[j]);
        out_1d[i1] = (f2 * cs[j] + f1 * ca[j]);
      }
    }
  }
  
  private void hybrid(int paramInt1, int paramInt2)
  {
    gr_info_s localGr_info_s = si.ch[paramInt1].gr[paramInt2];
    for (int j = 0; j < 576; j += 18)
    {
      int i = (window_switching_flag != 0) && (mixed_block_flag != 0) && (j < 36) ? 0 : block_type;
      float[] arrayOfFloat = out_1d;
      for (int m = 0; m < 18; m++) {
        tsOutCopy[m] = arrayOfFloat[(m + j)];
      }
      inv_mdct(tsOutCopy, rawout, i);
      for (m = 0; m < 18; m++) {
        arrayOfFloat[(m + j)] = tsOutCopy[m];
      }
      float[][] arrayOfFloat1 = prevblck;
      arrayOfFloat[(0 + j)] = (rawout[0] + arrayOfFloat1[paramInt1][(j + 0)]);
      arrayOfFloat1[paramInt1][(j + 0)] = rawout[18];
      arrayOfFloat[(1 + j)] = (rawout[1] + arrayOfFloat1[paramInt1][(j + 1)]);
      arrayOfFloat1[paramInt1][(j + 1)] = rawout[19];
      arrayOfFloat[(2 + j)] = (rawout[2] + arrayOfFloat1[paramInt1][(j + 2)]);
      arrayOfFloat1[paramInt1][(j + 2)] = rawout[20];
      arrayOfFloat[(3 + j)] = (rawout[3] + arrayOfFloat1[paramInt1][(j + 3)]);
      arrayOfFloat1[paramInt1][(j + 3)] = rawout[21];
      arrayOfFloat[(4 + j)] = (rawout[4] + arrayOfFloat1[paramInt1][(j + 4)]);
      arrayOfFloat1[paramInt1][(j + 4)] = rawout[22];
      arrayOfFloat[(5 + j)] = (rawout[5] + arrayOfFloat1[paramInt1][(j + 5)]);
      arrayOfFloat1[paramInt1][(j + 5)] = rawout[23];
      arrayOfFloat[(6 + j)] = (rawout[6] + arrayOfFloat1[paramInt1][(j + 6)]);
      arrayOfFloat1[paramInt1][(j + 6)] = rawout[24];
      arrayOfFloat[(7 + j)] = (rawout[7] + arrayOfFloat1[paramInt1][(j + 7)]);
      arrayOfFloat1[paramInt1][(j + 7)] = rawout[25];
      arrayOfFloat[(8 + j)] = (rawout[8] + arrayOfFloat1[paramInt1][(j + 8)]);
      arrayOfFloat1[paramInt1][(j + 8)] = rawout[26];
      arrayOfFloat[(9 + j)] = (rawout[9] + arrayOfFloat1[paramInt1][(j + 9)]);
      arrayOfFloat1[paramInt1][(j + 9)] = rawout[27];
      arrayOfFloat[(10 + j)] = (rawout[10] + arrayOfFloat1[paramInt1][(j + 10)]);
      arrayOfFloat1[paramInt1][(j + 10)] = rawout[28];
      arrayOfFloat[(11 + j)] = (rawout[11] + arrayOfFloat1[paramInt1][(j + 11)]);
      arrayOfFloat1[paramInt1][(j + 11)] = rawout[29];
      arrayOfFloat[(12 + j)] = (rawout[12] + arrayOfFloat1[paramInt1][(j + 12)]);
      arrayOfFloat1[paramInt1][(j + 12)] = rawout[30];
      arrayOfFloat[(13 + j)] = (rawout[13] + arrayOfFloat1[paramInt1][(j + 13)]);
      arrayOfFloat1[paramInt1][(j + 13)] = rawout[31];
      arrayOfFloat[(14 + j)] = (rawout[14] + arrayOfFloat1[paramInt1][(j + 14)]);
      arrayOfFloat1[paramInt1][(j + 14)] = rawout[32];
      arrayOfFloat[(15 + j)] = (rawout[15] + arrayOfFloat1[paramInt1][(j + 15)]);
      arrayOfFloat1[paramInt1][(j + 15)] = rawout[33];
      arrayOfFloat[(16 + j)] = (rawout[16] + arrayOfFloat1[paramInt1][(j + 16)]);
      arrayOfFloat1[paramInt1][(j + 16)] = rawout[34];
      arrayOfFloat[(17 + j)] = (rawout[17] + arrayOfFloat1[paramInt1][(j + 17)]);
      arrayOfFloat1[paramInt1][(j + 17)] = rawout[35];
    }
  }
  
  private void do_downmix()
  {
    for (int i = 0; i < 18; i++) {
      for (int j = 0; j < 18; j += 3)
      {
        lr[0][i][j] = ((lr[0][i][j] + lr[1][i][j]) * 0.5F);
        lr[0][i][(j + 1)] = ((lr[0][i][(j + 1)] + lr[1][i][(j + 1)]) * 0.5F);
        lr[0][i][(j + 2)] = ((lr[0][i][(j + 2)] + lr[1][i][(j + 2)]) * 0.5F);
      }
    }
  }
  
  public void inv_mdct(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt)
  {
    float f1 = f2 = f3 = f4 = f5 = f6 = f7 = f8 = f9 = f10 = f11 = f12 = f13 = f14 = f15 = f16 = f17 = f18 = 0.0F;
    if (paramInt == 2)
    {
      paramArrayOfFloat2[0] = 0.0F;
      paramArrayOfFloat2[1] = 0.0F;
      paramArrayOfFloat2[2] = 0.0F;
      paramArrayOfFloat2[3] = 0.0F;
      paramArrayOfFloat2[4] = 0.0F;
      paramArrayOfFloat2[5] = 0.0F;
      paramArrayOfFloat2[6] = 0.0F;
      paramArrayOfFloat2[7] = 0.0F;
      paramArrayOfFloat2[8] = 0.0F;
      paramArrayOfFloat2[9] = 0.0F;
      paramArrayOfFloat2[10] = 0.0F;
      paramArrayOfFloat2[11] = 0.0F;
      paramArrayOfFloat2[12] = 0.0F;
      paramArrayOfFloat2[13] = 0.0F;
      paramArrayOfFloat2[14] = 0.0F;
      paramArrayOfFloat2[15] = 0.0F;
      paramArrayOfFloat2[16] = 0.0F;
      paramArrayOfFloat2[17] = 0.0F;
      paramArrayOfFloat2[18] = 0.0F;
      paramArrayOfFloat2[19] = 0.0F;
      paramArrayOfFloat2[20] = 0.0F;
      paramArrayOfFloat2[21] = 0.0F;
      paramArrayOfFloat2[22] = 0.0F;
      paramArrayOfFloat2[23] = 0.0F;
      paramArrayOfFloat2[24] = 0.0F;
      paramArrayOfFloat2[25] = 0.0F;
      paramArrayOfFloat2[26] = 0.0F;
      paramArrayOfFloat2[27] = 0.0F;
      paramArrayOfFloat2[28] = 0.0F;
      paramArrayOfFloat2[29] = 0.0F;
      paramArrayOfFloat2[30] = 0.0F;
      paramArrayOfFloat2[31] = 0.0F;
      paramArrayOfFloat2[32] = 0.0F;
      paramArrayOfFloat2[33] = 0.0F;
      paramArrayOfFloat2[34] = 0.0F;
      paramArrayOfFloat2[35] = 0.0F;
      int j = 0;
      for (int i = 0; i < 3; i++)
      {
        paramArrayOfFloat1[(15 + i)] += paramArrayOfFloat1[(12 + i)];
        paramArrayOfFloat1[(12 + i)] += paramArrayOfFloat1[(9 + i)];
        paramArrayOfFloat1[(9 + i)] += paramArrayOfFloat1[(6 + i)];
        paramArrayOfFloat1[(6 + i)] += paramArrayOfFloat1[(3 + i)];
        paramArrayOfFloat1[(3 + i)] += paramArrayOfFloat1[(0 + i)];
        paramArrayOfFloat1[(15 + i)] += paramArrayOfFloat1[(9 + i)];
        paramArrayOfFloat1[(9 + i)] += paramArrayOfFloat1[(3 + i)];
        f21 = paramArrayOfFloat1[(12 + i)] * 0.5F;
        f20 = paramArrayOfFloat1[(6 + i)] * 0.8660254F;
        f22 = paramArrayOfFloat1[(0 + i)] + f21;
        f2 = paramArrayOfFloat1[(0 + i)] - paramArrayOfFloat1[(12 + i)];
        f1 = f22 + f20;
        f3 = f22 - f20;
        f21 = paramArrayOfFloat1[(15 + i)] * 0.5F;
        f20 = paramArrayOfFloat1[(9 + i)] * 0.8660254F;
        f22 = paramArrayOfFloat1[(3 + i)] + f21;
        f5 = paramArrayOfFloat1[(3 + i)] - paramArrayOfFloat1[(15 + i)];
        f6 = f22 + f20;
        f4 = f22 - f20;
        f4 *= 1.9318516F;
        f5 *= 0.70710677F;
        f6 *= 0.5176381F;
        f23 = f1;
        f1 += f6;
        f6 = f23 - f6;
        f23 = f2;
        f2 += f5;
        f5 = f23 - f5;
        f23 = f3;
        f3 += f4;
        f4 = f23 - f4;
        f1 *= 0.5043145F;
        f2 *= 0.5411961F;
        f3 *= 0.6302362F;
        f4 *= 0.8213398F;
        f5 *= 1.306563F;
        f6 *= 3.830649F;
        f9 = -f1 * 0.7933533F;
        f10 = -f1 * 0.6087614F;
        f8 = -f2 * 0.9238795F;
        f11 = -f2 * 0.38268343F;
        f7 = -f3 * 0.9914449F;
        f12 = -f3 * 0.13052619F;
        f1 = f4;
        f2 = f5 * 0.38268343F;
        f3 = f6 * 0.6087614F;
        f4 = -f6 * 0.7933533F;
        f5 = -f5 * 0.9238795F;
        f6 = -f1 * 0.9914449F;
        f1 *= 0.13052619F;
        paramArrayOfFloat2[(j + 6)] += f1;
        paramArrayOfFloat2[(j + 7)] += f2;
        paramArrayOfFloat2[(j + 8)] += f3;
        paramArrayOfFloat2[(j + 9)] += f4;
        paramArrayOfFloat2[(j + 10)] += f5;
        paramArrayOfFloat2[(j + 11)] += f6;
        paramArrayOfFloat2[(j + 12)] += f7;
        paramArrayOfFloat2[(j + 13)] += f8;
        paramArrayOfFloat2[(j + 14)] += f9;
        paramArrayOfFloat2[(j + 15)] += f10;
        paramArrayOfFloat2[(j + 16)] += f11;
        paramArrayOfFloat2[(j + 17)] += f12;
        j += 6;
      }
    }
    paramArrayOfFloat1[17] += paramArrayOfFloat1[16];
    paramArrayOfFloat1[16] += paramArrayOfFloat1[15];
    paramArrayOfFloat1[15] += paramArrayOfFloat1[14];
    paramArrayOfFloat1[14] += paramArrayOfFloat1[13];
    paramArrayOfFloat1[13] += paramArrayOfFloat1[12];
    paramArrayOfFloat1[12] += paramArrayOfFloat1[11];
    paramArrayOfFloat1[11] += paramArrayOfFloat1[10];
    paramArrayOfFloat1[10] += paramArrayOfFloat1[9];
    paramArrayOfFloat1[9] += paramArrayOfFloat1[8];
    paramArrayOfFloat1[8] += paramArrayOfFloat1[7];
    paramArrayOfFloat1[7] += paramArrayOfFloat1[6];
    paramArrayOfFloat1[6] += paramArrayOfFloat1[5];
    paramArrayOfFloat1[5] += paramArrayOfFloat1[4];
    paramArrayOfFloat1[4] += paramArrayOfFloat1[3];
    paramArrayOfFloat1[3] += paramArrayOfFloat1[2];
    paramArrayOfFloat1[2] += paramArrayOfFloat1[1];
    paramArrayOfFloat1[1] += paramArrayOfFloat1[0];
    paramArrayOfFloat1[17] += paramArrayOfFloat1[15];
    paramArrayOfFloat1[15] += paramArrayOfFloat1[13];
    paramArrayOfFloat1[13] += paramArrayOfFloat1[11];
    paramArrayOfFloat1[11] += paramArrayOfFloat1[9];
    paramArrayOfFloat1[9] += paramArrayOfFloat1[7];
    paramArrayOfFloat1[7] += paramArrayOfFloat1[5];
    paramArrayOfFloat1[5] += paramArrayOfFloat1[3];
    paramArrayOfFloat1[3] += paramArrayOfFloat1[1];
    float f37 = paramArrayOfFloat1[0] + paramArrayOfFloat1[0];
    float f38 = f37 + paramArrayOfFloat1[12];
    float f19 = f38 + paramArrayOfFloat1[4] * 1.8793852F + paramArrayOfFloat1[8] * 1.5320889F + paramArrayOfFloat1[16] * 0.34729636F;
    float f20 = f37 + paramArrayOfFloat1[4] - paramArrayOfFloat1[8] - paramArrayOfFloat1[12] - paramArrayOfFloat1[12] - paramArrayOfFloat1[16];
    float f21 = f38 - paramArrayOfFloat1[4] * 0.34729636F - paramArrayOfFloat1[8] * 1.8793852F + paramArrayOfFloat1[16] * 1.5320889F;
    float f22 = f38 - paramArrayOfFloat1[4] * 1.5320889F + paramArrayOfFloat1[8] * 0.34729636F - paramArrayOfFloat1[16] * 1.8793852F;
    float f23 = paramArrayOfFloat1[0] - paramArrayOfFloat1[4] + paramArrayOfFloat1[8] - paramArrayOfFloat1[12] + paramArrayOfFloat1[16];
    float f39 = paramArrayOfFloat1[6] * 1.7320508F;
    float f24 = paramArrayOfFloat1[2] * 1.9696155F + f39 + paramArrayOfFloat1[10] * 1.2855753F + paramArrayOfFloat1[14] * 0.6840403F;
    float f25 = (paramArrayOfFloat1[2] - paramArrayOfFloat1[10] - paramArrayOfFloat1[14]) * 1.7320508F;
    float f26 = paramArrayOfFloat1[2] * 1.2855753F - f39 - paramArrayOfFloat1[10] * 0.6840403F + paramArrayOfFloat1[14] * 1.9696155F;
    float f27 = paramArrayOfFloat1[2] * 0.6840403F - f39 + paramArrayOfFloat1[10] * 1.9696155F - paramArrayOfFloat1[14] * 1.2855753F;
    float f40 = paramArrayOfFloat1[1] + paramArrayOfFloat1[1];
    float f41 = f40 + paramArrayOfFloat1[13];
    float f28 = f41 + paramArrayOfFloat1[5] * 1.8793852F + paramArrayOfFloat1[9] * 1.5320889F + paramArrayOfFloat1[17] * 0.34729636F;
    float f29 = f40 + paramArrayOfFloat1[5] - paramArrayOfFloat1[9] - paramArrayOfFloat1[13] - paramArrayOfFloat1[13] - paramArrayOfFloat1[17];
    float f30 = f41 - paramArrayOfFloat1[5] * 0.34729636F - paramArrayOfFloat1[9] * 1.8793852F + paramArrayOfFloat1[17] * 1.5320889F;
    float f31 = f41 - paramArrayOfFloat1[5] * 1.5320889F + paramArrayOfFloat1[9] * 0.34729636F - paramArrayOfFloat1[17] * 1.8793852F;
    float f32 = (paramArrayOfFloat1[1] - paramArrayOfFloat1[5] + paramArrayOfFloat1[9] - paramArrayOfFloat1[13] + paramArrayOfFloat1[17]) * 0.70710677F;
    float f42 = paramArrayOfFloat1[7] * 1.7320508F;
    float f33 = paramArrayOfFloat1[3] * 1.9696155F + f42 + paramArrayOfFloat1[11] * 1.2855753F + paramArrayOfFloat1[15] * 0.6840403F;
    float f34 = (paramArrayOfFloat1[3] - paramArrayOfFloat1[11] - paramArrayOfFloat1[15]) * 1.7320508F;
    float f35 = paramArrayOfFloat1[3] * 1.2855753F - f42 - paramArrayOfFloat1[11] * 0.6840403F + paramArrayOfFloat1[15] * 1.9696155F;
    float f36 = paramArrayOfFloat1[3] * 0.6840403F - f42 + paramArrayOfFloat1[11] * 1.9696155F - paramArrayOfFloat1[15] * 1.2855753F;
    float f43 = f19 + f24;
    float f44 = (f28 + f33) * 0.5019099F;
    f1 = f43 + f44;
    float f18 = f43 - f44;
    f43 = f20 + f25;
    f44 = (f29 + f34) * 0.5176381F;
    float f2 = f43 + f44;
    float f17 = f43 - f44;
    f43 = f21 + f26;
    f44 = (f30 + f35) * 0.55168897F;
    float f3 = f43 + f44;
    float f16 = f43 - f44;
    f43 = f22 + f27;
    f44 = (f31 + f36) * 0.61038727F;
    float f4 = f43 + f44;
    float f15 = f43 - f44;
    float f5 = f23 + f32;
    float f14 = f23 - f32;
    f43 = f22 - f27;
    f44 = (f31 - f36) * 0.8717234F;
    float f6 = f43 + f44;
    float f13 = f43 - f44;
    f43 = f21 - f26;
    f44 = (f30 - f35) * 1.1831008F;
    float f7 = f43 + f44;
    float f12 = f43 - f44;
    f43 = f20 - f25;
    f44 = (f29 - f34) * 1.9318516F;
    float f8 = f43 + f44;
    float f11 = f43 - f44;
    f43 = f19 - f24;
    f44 = (f28 - f33) * 5.7368565F;
    float f9 = f43 + f44;
    float f10 = f43 - f44;
    float[] arrayOfFloat = win[paramInt];
    paramArrayOfFloat2[0] = (-f10 * arrayOfFloat[0]);
    paramArrayOfFloat2[1] = (-f11 * arrayOfFloat[1]);
    paramArrayOfFloat2[2] = (-f12 * arrayOfFloat[2]);
    paramArrayOfFloat2[3] = (-f13 * arrayOfFloat[3]);
    paramArrayOfFloat2[4] = (-f14 * arrayOfFloat[4]);
    paramArrayOfFloat2[5] = (-f15 * arrayOfFloat[5]);
    paramArrayOfFloat2[6] = (-f16 * arrayOfFloat[6]);
    paramArrayOfFloat2[7] = (-f17 * arrayOfFloat[7]);
    paramArrayOfFloat2[8] = (-f18 * arrayOfFloat[8]);
    paramArrayOfFloat2[9] = (f18 * arrayOfFloat[9]);
    paramArrayOfFloat2[10] = (f17 * arrayOfFloat[10]);
    paramArrayOfFloat2[11] = (f16 * arrayOfFloat[11]);
    paramArrayOfFloat2[12] = (f15 * arrayOfFloat[12]);
    paramArrayOfFloat2[13] = (f14 * arrayOfFloat[13]);
    paramArrayOfFloat2[14] = (f13 * arrayOfFloat[14]);
    paramArrayOfFloat2[15] = (f12 * arrayOfFloat[15]);
    paramArrayOfFloat2[16] = (f11 * arrayOfFloat[16]);
    paramArrayOfFloat2[17] = (f10 * arrayOfFloat[17]);
    paramArrayOfFloat2[18] = (f9 * arrayOfFloat[18]);
    paramArrayOfFloat2[19] = (f8 * arrayOfFloat[19]);
    paramArrayOfFloat2[20] = (f7 * arrayOfFloat[20]);
    paramArrayOfFloat2[21] = (f6 * arrayOfFloat[21]);
    paramArrayOfFloat2[22] = (f5 * arrayOfFloat[22]);
    paramArrayOfFloat2[23] = (f4 * arrayOfFloat[23]);
    paramArrayOfFloat2[24] = (f3 * arrayOfFloat[24]);
    paramArrayOfFloat2[25] = (f2 * arrayOfFloat[25]);
    paramArrayOfFloat2[26] = (f1 * arrayOfFloat[26]);
    paramArrayOfFloat2[27] = (f1 * arrayOfFloat[27]);
    paramArrayOfFloat2[28] = (f2 * arrayOfFloat[28]);
    paramArrayOfFloat2[29] = (f3 * arrayOfFloat[29]);
    paramArrayOfFloat2[30] = (f4 * arrayOfFloat[30]);
    paramArrayOfFloat2[31] = (f5 * arrayOfFloat[31]);
    paramArrayOfFloat2[32] = (f6 * arrayOfFloat[32]);
    paramArrayOfFloat2[33] = (f7 * arrayOfFloat[33]);
    paramArrayOfFloat2[34] = (f8 * arrayOfFloat[34]);
    paramArrayOfFloat2[35] = (f9 * arrayOfFloat[35]);
  }
  
  private static float[] create_t_43()
  {
    float[] arrayOfFloat = new float[' '];
    for (int i = 0; i < 8192; i++) {
      arrayOfFloat[i] = ((float)Math.pow(i, 1.3333333333333333D));
    }
    return arrayOfFloat;
  }
  
  static int[] reorder(int[] paramArrayOfInt)
  {
    int i = 0;
    int[] arrayOfInt = new int['ɀ'];
    for (int j = 0; j < 13; j++)
    {
      int m = paramArrayOfInt[j];
      int n = paramArrayOfInt[(j + 1)];
      for (int i1 = 0; i1 < 3; i1++) {
        for (int i2 = m; i2 < n; i2++) {
          arrayOfInt[(3 * i2 + i1)] = (i++);
        }
      }
    }
    return arrayOfInt;
  }
  
  class Sftable
  {
    public int[] l;
    public int[] s;
    
    public Sftable()
    {
      l = new int[5];
      s = new int[3];
    }
    
    public Sftable(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      l = paramArrayOfInt1;
      s = paramArrayOfInt2;
    }
  }
  
  static class temporaire2
  {
    public int[] l = new int[23];
    public int[][] s = new int[3][13];
    
    public temporaire2() {}
  }
  
  static class III_side_info_t
  {
    public int main_data_begin = 0;
    public int private_bits = 0;
    public LayerIIIDecoder.temporaire[] ch = new LayerIIIDecoder.temporaire[2];
    
    public III_side_info_t()
    {
      ch[0] = new LayerIIIDecoder.temporaire();
      ch[1] = new LayerIIIDecoder.temporaire();
    }
  }
  
  static class temporaire
  {
    public int[] scfsi = new int[4];
    public LayerIIIDecoder.gr_info_s[] gr = new LayerIIIDecoder.gr_info_s[2];
    
    public temporaire()
    {
      gr[0] = new LayerIIIDecoder.gr_info_s();
      gr[1] = new LayerIIIDecoder.gr_info_s();
    }
  }
  
  static class gr_info_s
  {
    public int part2_3_length = 0;
    public int big_values = 0;
    public int global_gain = 0;
    public int scalefac_compress = 0;
    public int window_switching_flag = 0;
    public int block_type = 0;
    public int mixed_block_flag = 0;
    public int[] table_select = new int[3];
    public int[] subblock_gain = new int[3];
    public int region0_count = 0;
    public int region1_count = 0;
    public int preflag = 0;
    public int scalefac_scale = 0;
    public int count1table_select = 0;
    
    public gr_info_s() {}
  }
  
  static class SBI
  {
    public int[] l;
    public int[] s;
    
    public SBI()
    {
      l = new int[23];
      s = new int[14];
    }
    
    public SBI(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      l = paramArrayOfInt1;
      s = paramArrayOfInt2;
    }
  }
}
