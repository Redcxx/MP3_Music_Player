//NOTE: THIS CLASS DOES NOT LOG EXCEPTION GENERATED

import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.IOException;
import java.io.Serializable;
/**
 * Partially completed class to represent a song for the Song Database.
 * <P>
 * This file should be copied as Song.java and then modified to provide
 * the implementation.
 */
public class Song implements Comparable<Song>, Serializable {

  public static enum DATA_TYPE {
    TITLE, ARTIST, GENRE, PLAY_COUNT, EXACT_TITLE, EXACT_ARTIST;

    @Override
    public String toString() {
      switch(this) {
        case TITLE: return "Title";
        case ARTIST: return "Artist";
        case GENRE: return "Genre";
        case PLAY_COUNT: return "Play Count";
        case EXACT_TITLE: return "Exact Title";
        case EXACT_ARTIST: return "Exact Artist";
        default://should not happened
          throw new IllegalArgumentException("Song.DATA_TYPE.toString error");
      }//switch
    }//toString
   };

  private static DATA_TYPE sortMode;
  private static MP3Player tester;
  private int playCount;
  private final int hashCode;
  private final String original;
  private final String title, artist, genre, url;

  private String SONG_FORMAT = "%-20s - %-15s | %-15s | %-5s | %-20s";
  //                            title  artist   genre  playcount url

  public Song(String tabSeparatedInfo) throws NumberFormatException,
              ArrayIndexOutOfBoundsException, CorruptedSongException {
    String[] songInfos = tabSeparatedInfo.split("\t");
    this.title = songInfos[0];
    this.artist = songInfos[1];
    this.genre = songInfos[2];
    this.url = songInfos[3];
    this.playCount = Integer.parseInt(songInfos[4]);
    this.original = tabSeparatedInfo;
    if(!urlIsValid(this.url)) {
      throw new CorruptedSongException("Invalid url");
    }//if
    this.hashCode =   3 * title.hashCode()
                    + 5 * artist.hashCode()
                    + 7 * genre.hashCode();
  }//Song

  static {
    sortMode = DATA_TYPE.TITLE;
    tester = new MP3Player();
  }//static

  public void incTimesPlayed() {
    playCount++;
  }//incTimesPlayed

  /** Matches this song against a given
   *  title, artist or genre,
   *  depending on the given match mode, which may be as a substring or exact.
   *  (note: there is no match for url/timesPlayed)
   *  @param matchMode match mode to use
   *  @param matchValue match value to use (either exact or substring, depending on
   *  matchMode)
   *  @return true if and only if song matches matchValue with respect to matchMode
   */
  public boolean matches(DATA_TYPE matchMode, String matchValue)
                throws NumberFormatException, MP3PlayerException {
     switch(matchMode) {
       case TITLE: return title.indexOf(matchValue) >= 0;
       case ARTIST: return artist.indexOf(matchValue) >= 0;
       case GENRE: return genre.indexOf(matchValue) >= 0;
       case PLAY_COUNT:
        int value = -1;

        //NOTE: THIS WILL RETURN TRUE IF THE STRING IS INVALID
        try { value = Integer.parseInt(matchValue); }
        catch(Exception e) { return true; }

        return playCount == value;
       case EXACT_TITLE: return title.equals(matchValue);
       case EXACT_ARTIST: return artist.equals(matchValue);
       //should not happen
       default: throw new MP3PlayerException("Invalid match type");
     }//switch
  }//matches

  /**
   * Sets the sort mode to use in future comparisons.
   * This should be over-ridden in an implementation.
   * @param requiredSortMode required sort mode
   */
  public static void setSortMode(DATA_TYPE requiredSortMode) {
    sortMode = requiredSortMode;
  }//setSortMode

  @Override
  public boolean equals(Object other) {
    try {
    Song otherSong = (Song) other;
    boolean equals = true;
      equals &= this.matches(DATA_TYPE.EXACT_TITLE, otherSong.title);
      equals &= this.matches(DATA_TYPE.EXACT_ARTIST, otherSong.artist);
      equals &= this.matches(DATA_TYPE.GENRE, otherSong.genre);
      return equals;
    } catch(Exception e) {
      return false;
    }//try catch
  }//equals

  @Override
  public int hashCode() {
    return this.hashCode;
  }//hashCode

  /**
   * Modify to Implement {@link Comparable Comparable} interface, to give a total
   * ordering on <CODE>SongTemplate</CODE> objects, but dependent upon the
   * current sort mode.
   * (note: consistent {@link #equals(Object) equals()}
   * and {@link #hashCode() hashCode()} methods should be implemented.
   * @param other the object to be compared
   * @return -ve(<), 0(=), +ve(>)
   */
  @Override
  public int compareTo(Song other) {
    switch(sortMode) {
      case TITLE: case EXACT_TITLE: return title.compareTo(other.title);
      case ARTIST: case EXACT_ARTIST: return artist.compareTo(other.artist);
      case GENRE: return genre.compareTo(other.genre);
      case PLAY_COUNT: return playCount - other.playCount;
      //should not happen
      default: System.err.println("Invalid sort type"); return -1;
    }//switch
  }//compareTo

  private boolean urlIsValid(String url) {
    tester = new MP3Player();
    if(!url.endsWith("mp3")) return false;
    try {
      tester.play(url);
      tester.stop();
      // tester.play(url);
      // tester.stop();
      // tester.play(url);
      // tester.stop();
    } catch(NullPointerException | IOException ne) {
      //null pointer is raised internally in player, i dk why
      return false;
    }//try catch
    System.out.println("url accepted: " + url);
    return true;
  }//urlIsValid

  @Override
  public String toString() {
    return String.format(SONG_FORMAT, getTitle(), getArtist(), getGenre(),
                                      getTimesPlayed(), getURL());
  }//toString

  public String shortString() {
    return getTitle() + " - " + getArtist();
  }

  /**
   * @return return title field
   */
  public String getTitle() {
    return title;
  }//getTitle

  /**
   * @return return artist field
   */
  public String getArtist() {
    return artist;
  }//getArtist


  /**
   * @return return genre field
   */
  public String getGenre() {
    return genre;
  }//getGenre


  /**
   * @return return URL field
   */
  public String getURL() {
    return url;
  }//getURL


  public int getTimesPlayed() {
    return playCount;
  }//getTimesPlayed

  //for testing
  public static void main(String[] args) {
    String[] songs = {"Mara	Brenda Kayne	Easy Listening"
                      + "	http://www.cs.man.ac.uk/~alanw/CS1092/Brenda_C."
                      + "_Kayne-Mara.mp3	2",
                      "Carol of the Three Kings	Meredith Ryan Packer	Soundtrack"+
                      "	http://www.cs.man.ac.uk/~alanw/CS1092/One_Christmas"
                      + "_Eve-Carol_of_the_Three_Kings.mp3	4",
                      "Non-existent Melody	The Ghostwriter	Easy Listening"+
                      "	http://www.cs.man.ac.uk/~alanw/aBrokenURL.mp3	0",
                      "Another Non-existent Melody	The Ghostwriter	Easy"
                      + " Listening		0",
                      "Morning Star/Three Jolly BlackSheepskins	Albireo	Folk"+
                      "	http://www.cs.man.ac.uk/~seanb/teaching/CS1092/"
                      + "mp3/morning.mp3	0",
                      "(Not) the Fisher's Hornpipe	Albireo	Folk"+
                      "	http://www.cs.man.ac.uk/~seanb/teaching/CS1092/"
                      + "mp3/fishers.mp3	1",
                      "Moonlight	Sean	Easy Listening		6",
                      "Fly Me to the Moon	Sean Sinatra	Easy Listening		5",
                      "Jupiter and Mars	Frank Sinatra	Easy Listening		4",
                      "Fly Me to the Moon	Frank Sinatra	Easy Listening		6",
                      "Moon River	Frank Sinatra	Easy Listening		3",
                      "Moon Unit	Frank Zappa	Easy Listening		2",
                      "Over the Moon	Alan	Easy Listening		3",
                      "Mars	Alan	Easy Listening	http://brokenURL.com	2",
                      "Venus	Sean	Rock	file:/nonExistentFile.mp3	0",
                      "Moonrock	Howard	Rock		5",
                      "Mars 	Howard	Classical		4",
                      "Mars	Sean	Classical		3",
                      "Mars Attacks	Alan	Classical		2",
                      "Earth	Alan	Classical		1",
                      "Mars	John	Folk		0",
                      "Earth Attacks	John	Classical		5",
                      "Earth	John	Rock		4",
                      "A Title	An Artist	Classical		3",
                      "A Title	An Artist	Classical		3",
                    };
    List<Song> songObjects = new ArrayList<Song>();
    int invalidcount = 0;
    for(int i = 0; i < songs.length; i++) {
      try {
        songObjects.add(new Song(songs[i]));
      } catch(Exception e) {
        System.out.println("Invalid song found: " + songs[i]);
        invalidcount++;
      }//try catch
    }//for

    System.out.println("There are " + invalidcount + " invalid songs");
    System.out.println();
    for(Song s : songObjects) {
      System.out.println(s);
    }//for

    String sls = "------------------------------------------------------------";


    Song.setSortMode(Song.DATA_TYPE.TITLE);
    System.out.println(sls);
    System.out.println("Sort mode set to title");
    Collections.sort(songObjects);
    for(Song s : songObjects) { System.out.println(s); }//for

    Song.setSortMode(Song.DATA_TYPE.ARTIST);
    System.out.println(sls);
    System.out.println("Sort mode set to artist");
    Collections.sort(songObjects);
    for(Song s : songObjects) { System.out.println(s); }//for

    Song.setSortMode(Song.DATA_TYPE.GENRE);
    System.out.println(sls);
    System.out.println("Sort mode set to genre");
    Collections.sort(songObjects);
    for(Song s : songObjects) { System.out.println(s); }//for

    Song.setSortMode(Song.DATA_TYPE.PLAY_COUNT);
    System.out.println(sls);
    System.out.println("Sort mode set to play count");
    Collections.sort(songObjects);
    for(Song s : songObjects) { System.out.println(s); }//for

    System.out.println("test completed");
  }//main

}//class
