import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

//this class represent a collection of song as a song database
//and include some manipulation of the database

public class SongDatabase implements Iterable<Song>, Serializable {
  private Set<Song> songs;
  private final String title;
  private File source;
  private static String sls = System.getProperty("line.separator");
  private Song.DATA_TYPE filterMode;

  public SongDatabase(File source) throws CorruptedFileException, IOException {
    this.source = source;
    BufferedReader reader = new BufferedReader(new FileReader(source));
    try {
      //this does not check whether the word before "=" is "title"
      this.title = reader.readLine().split("=")[1];//format: title=<name>
    } catch(IOException e) {
      Utilities.log(e);
      throw new IOException("Title missing for database", e);
    }
    songs = new HashSet<Song>();
    Song currSong;
    int duplicates = 0, invalids = 0;
    String currline;
    //read all songs
    try {
      while((currline = reader.readLine()) != null) {
        try {
          currSong = new Song(currline);
          if(!songs.add(currSong)) {
            duplicates++;
            System.err.println("duplicate song found: " + currSong);
          }//if else
        } catch(Exception e) {
          Utilities.log(e);
          System.out.println("Invalid song found: " + currline);
          invalids++;
        }
      }//while
    } catch(Exception e) {
      Utilities.log(e);
      throw new CorruptedFileException("Error while reading file", e);
    } finally {
      try { reader.close(); }
      catch(Exception e) {
        Utilities.log(e);
        System.out.println("Error while closing file");
      }
    }//try catch ifnally

    //report duplicates and invalid songs
    System.out.println("There are " + duplicates + " duplicates in " + source);
    System.out.println("There are " + invalids+ " invalid songs in " + source);

  }//SongDatabase

  public SongDatabase() throws CorruptedFileException, IOException {
      this(new File("sampleSongDatabase.mdb"));
  }

  public List<Song> filter(Song.DATA_TYPE type, String matchString) {
    List<Song> filteredSongs = new ArrayList<Song>();
    for(Song song : this.songs) {
      try {
        if(song.matches(type, matchString)) {
          filteredSongs.add(song);
        }//if
      } catch(Exception e) {
        System.out.println("Error while filtering: " + song);
      }//try catch
    }//for
    return filteredSongs;
  }

  public boolean addSong(String songstr) {
    try {
      Song song = new Song(songstr);
      return songs.add(song);
    } catch(Exception e) {
      return false;
    }//try catch
  }//addSong

  public boolean addSong(Song song) {
    System.out.println("adding: " + song);
    return songs.add(song);
  }//addSong

  public boolean addSongForce(Song song) {
    //remove before adding basically
    System.out.println("forcely adding: " + song);
    songs.remove(song);
    return songs.add(song);
  }//addSong

  public boolean removeSong(Song song) {
    System.out.println("deleting: " + song);
    return songs.remove(song);
  }

  public boolean containsSong(Song song) {
    for(Song s : songs) {
      if(s.equals(song)) {
        return true;
      }//if
    }//for
    return false;
  }//containsSong

  public Iterator<Song> iterator() {
    return songs.iterator();
  }//iterator

  public void replace(SongDatabase other) {
    this.songs = other.songs;//replace songs only
  }//replace

  @Override
  public String toString() {
    String res =  "Database Title: " + title + sls
    + "Songs: ";
    for(Song s : this)
    res += sls + s;
    return res;
  }

  public String getTitle() {
    return this.title;
  }

  //this main method is just for testing
  public static void main(String[] args) {
    if(args.length != 1)
    throw new IllegalArgumentException("please provide a file");
    File file = new File(args[0]);
    try {
      SongDatabase sd = new SongDatabase(file);
      System.out.println("currenly in database: ");
      System.out.println(sd);
      System.out.println();

      System.out.println("Testing remove song...");
      List<Song> removed = new ArrayList<Song>();
      for(Iterator<Song> iter = sd.iterator(); iter.hasNext();) {
        removed.add(iter.next());
        iter.remove();
        System.out.println("removed one song: " + sls + sd);
      }//for
      System.out.println();

      System.out.println("Tesing add song...");
      for(Song s : removed) {
        sd.addSong(s);
        System.out.println("added one song:");
        System.out.println(sd);
      }//for
      System.out.println();

      System.out.println("Testing filter...");
      System.out.println("filtered title contains Ma:");
      for(Song s : sd.filter(Song.DATA_TYPE.TITLE, "Ma")) {
        System.out.println(s);
      }
      System.out.println("filtered artist contains Me:");
      for(Song s : sd.filter(Song.DATA_TYPE.ARTIST, "Me")) {
        System.out.println(s);
      }
      System.out.println("filtered genre == Easy Listening:");
      for(Song s : sd.filter(Song.DATA_TYPE.GENRE, "Easy Listening")) {
        System.out.println(s);
      }
      System.out.println("filtered play count == 4:");
      for(Song s : sd.filter(Song.DATA_TYPE.PLAY_COUNT, "4")) {
        System.out.println(s);
      }
      System.out.println("filtered title == Mara:");
      for(Song s : sd.filter(Song.DATA_TYPE.EXACT_TITLE, "Mara")) {
        System.out.println(s);
      }
      System.out.println("filtered artist == Meredith Ryan Packer:");
      for(Song s : sd.filter(Song.DATA_TYPE.EXACT_ARTIST,
      "Meredith Ryan Packer")) {
        System.out.println(s);
      }
      System.out.println();

      System.out.println("Tesing completed");
    } catch(Exception e) {
      Utilities.log(e);
      System.out.println("### Error while testing ###");
      e.printStackTrace();
    }//try catch

  }//main

}//SongDatabase
