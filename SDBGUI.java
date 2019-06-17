import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Color;
import java.awt.Container;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.ListModel;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.AbstractButton;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;


public class SDBGUI extends JFrame {
  private MP3Player player;
  private SongDatabase database;

  private final String defaultFilterString = "";
  private final Song.DATA_TYPE defaultFilter = Song.DATA_TYPE.TITLE;
  private final Song.DATA_TYPE defaultSortMode = Song.DATA_TYPE.TITLE;

  private String currFilterString;
  private Song.DATA_TYPE currFilter;
  private Song.DATA_TYPE currSortMode;

  private final Song.DATA_TYPE[] filterOptions;
  private final Song.DATA_TYPE[] sortOptions;

  private final JList<Song> songlist;
  private final JLabel currentSongInfo;
  private final JButton options, play, edit, close;
  private final JMenuItem add, save, load;
  private final JMenuBar menubar;
  private final JMenu file;

  private final Container contents;
  private final GridBagLayout gridBag;
  private final GridBagConstraints settings;

  private final String default_current_song_info_string = "------------------";

  public SDBGUI(String title) throws MP3PlayerException {
    if(title == null || title == "")
      throw new MP3PlayerException("title cant be null/empty");
    player = new MP3Player();
    try {
      System.out.println("Using sample database");
      database = new SongDatabase();//sample database
    } catch(Exception e) {
      Utilities.log(e);
      throw new IllegalArgumentException(e);
    }//try catch

    filterOptions = Song.DATA_TYPE.values();
    List<Song.DATA_TYPE> allSortOptions = new ArrayList<Song.DATA_TYPE>();
    for(Song.DATA_TYPE type : Song.DATA_TYPE.values()) {
      if(!type.toString().startsWith("Exact")) {
        allSortOptions.add(type);
      }//if
    }//for
    sortOptions = allSortOptions.toArray(
                                    new Song.DATA_TYPE[allSortOptions.size()]);

    //general
    contents = getContentPane();
    gridBag = new GridBagLayout();
    settings = new GridBagConstraints();

    //menu
    menubar = new JMenuBar();
    file = new JMenu("File") {
      private Border emptyBorder = BorderFactory.createEmptyBorder();
      @Override
      public JPopupMenu getPopupMenu() {
        JPopupMenu menu = super.getPopupMenu();
        menu.setBorder(emptyBorder);
        return menu;
      }//getPopupMenu
    };
    save = new JMenuItem("Save");
    load = new JMenuItem("Load");
    add = new JMenuItem("Add");

    //currentSongInfo
    currentSongInfo = new JLabel(default_current_song_info_string);
    currentSongInfo.setHorizontalAlignment(SwingConstants.CENTER);

    //functions
    options = new JButton("Options");
    play = new JButton("Play");
    edit = new JButton("Edit");
    close = new JButton("Close");

    //song list
    DefaultListModel<Song> model = new DefaultListModel<Song>();
    songlist = new JList<Song>();
    filterAndSort(defaultFilter, defaultFilterString, defaultSortMode);
    songlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    setTitle(title);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setSize(WIDTH, HEIGHT);
    setLayout(gridBag);
    init();
    setColors();
    setInteraction();
    pack();
    setLocationRelativeTo(null);
  }//constructor

  private void setColors() {

    Utilities.paintGui(SDBGUI.this);

    file.setBorderPainted(false);
    menubar.setBorderPainted(false);
    currentSongInfo.setForeground(Utilities.huicolor);
    songlist.setForeground(Utilities.taocolor);
    close.setBorder(BorderFactory.createEmptyBorder());
  }//setColors

  private void rePaintSonglist(List<Song> songs) {
    DefaultListModel<Song> model = new DefaultListModel<Song>();
    for(Song s : songs)
      model.addElement(s);
    songlist.setModel(model);
  }

  public void filterAndSort(Song.DATA_TYPE filter, String filterString,
                                        Song.DATA_TYPE sortMode) {
    System.out.println("filter and sort: " + filter + " "
                      + (filterString.equals("") ? "<EMPTY STRING>"
                      : filterString) + " " + sortMode);
    currFilter = filter;
    currFilterString = filterString;
    currSortMode = sortMode;
    List<Song> songs = database.filter(filter, filterString);
    Song.setSortMode(sortMode);
    Collections.sort(songs);
    rePaintSonglist(songs);
  }//filterAndSort

  public void filterAndSort() {
    filterAndSort(currFilter, currFilterString, currSortMode);
  }//filterAndSort

  public boolean removeSong(Song song) {
    boolean res = database.removeSong(song);
    filterAndSort();
    return res;
  }//removeSong

  public boolean addSong(Song song) {
    boolean res = database.addSong(song);
    filterAndSort();
    return res;
  }//addSong

  public boolean addSongForce(Song song) {
    boolean res = database.addSongForce(song);
    filterAndSort();
    return res;
  }//addSong

  private boolean containsSong(Song song) {
    return database.containsSong(song);
  }//containsSong

  private void replace(SongDatabase other) {
    database.replace(other);//replace song only
    filterAndSort();
  }//replace

  private Song getSelectedSong() {
    Song selectedSong = songlist.getSelectedValue();
    if(selectedSong == null) {
      Utilities.showMsgDialog(SDBGUI.this, "./scary_icon.png",
                              "You did not select any song...",
                              "A message from a deadly loli");
      return null;
    } else {
      return selectedSong;
    }// if else
  }//getSelectedSong

  private void init() {
    settings.fill = GridBagConstraints.HORIZONTAL;
    settings.anchor = GridBagConstraints.CENTER;
    settings.gridx = 0;
    settings.gridy = 0;
    settings.ipady = 0;
    settings.ipadx = 0;
    settings.weightx = 1.0;
    settings.weighty = 0;

    //menu
    file.add(add);
    file.add(save);
    file.add(load);
    menubar.add(file);
    settings.gridwidth = 4;
    settings.gridheight = 1;
    contents.add(menubar, settings);

    //current song info display
    settings.gridy++;
    settings.ipady = 15;
    contents.add(currentSongInfo, settings);

    //function buttons
    settings.ipady = 7;
    settings.ipadx = 10;
    settings.gridwidth = 1;
    settings.gridx = 0;
    settings.gridy++;
    contents.add(edit, settings);
    settings.gridx--;
    contents.add(play, settings);
    settings.gridx--;
    contents.add(options, settings);

    //jlist for songs
    // settings.ipady = 0;
    // settings.ipadx = 0;
    settings.gridy++;
    settings.gridx = 0;
    settings.gridwidth = 4;
    contents.add(songlist, settings);

    //close button
    settings.ipady = 10;
    settings.ipadx = 10;
    settings.gridy++;
    settings.gridx++;
    settings.gridwidth = 1;
    contents.add(close, settings);

  }//init

  private void setInteraction() {
    edit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Song song = getSelectedSong();
        if(song != null) {
          new SongEditGui(SDBGUI.this, song).setVisible(true);
        }//ifelse
      }//actionPerformed
    });
    play.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        if(play.getText() == "Play") {
          Song selectedSong = getSelectedSong();
          if(selectedSong != null) {
            try {
              player.play(selectedSong.getURL());
              play.setText("Stop");
              currentSongInfo.setText(selectedSong.shortString());
            } catch(IOException | NullPointerException ioe) {
              // ioe.printStackTrace();
              // player = null;
              // player = new MP3Player();
              Utilities.log(ioe);
              play.setText("Play");
              Utilities.showMsgDialog(SDBGUI.this, "./cute_icon.png",
                              "This song can't be played for unknown reason...",
                                      "a curious and sorry message");
            }
          }//if
        } else if(play.getText() == "Stop") {
          player.stop();
          play.setText("Play");
          currentSongInfo.setText(default_current_song_info_string);
        }
      }//actionPerformed
    });
    close.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent ae) {
         String[] options = { "let me go!", "stay a while.." };
         int reply = Utilities.showQuitDialog( SDBGUI.this, options,
                                               "./cute_icon.png",
                                               "Are you sure you want to quit?",
                                               "wait a moment...");
         if(reply == 0) {
           System.out.println("Seeya");
           System.exit(0);
         } else {
           System.out.println("Weebcome baaak");
         }//ifelse
       }//actionPerformed
     });
    options.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        new OptionsGui(SDBGUI.this).setVisible(true);
      }//actionPerformed
    });
    add.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        new AddSongGui(SDBGUI.this).setVisible(true);
      }//actionPerformed
    });
    save.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        File saveFile = null;//will be override
        String[] opts = {"save to existing file",
                          "save to new file", "misclicked.."};
        int reply = Utilities.showQuitDialog(SDBGUI.this, opts, "cute_icon.png",
                                            "which way would you like to save?",
                                            "save options");
        if(reply == 0) {
          saveFile = Utilities.getFileFromUser(SDBGUI.this, "data");
        } else if(reply == 1) {
          boolean filexists = true;
          String title = database.getTitle();
          saveFile = new File(title + ".data");
          int count = 1;
          while(saveFile.exists()) {
            saveFile = new File(title + count++ + ".data");
          }//while
        } else {
          dispose();//do nothing if misclicked option
        }//if else if else

        if(saveFile == null) dispose();//user didnot select any file

        ObjectOutputStream writer = null;//will be override
        try {
          writer = new ObjectOutputStream(new FileOutputStream(saveFile));
          writer.writeObject(database);
          System.out.println("successfully saved to: " + saveFile.getName());
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                  "database saved successfully to: \""
                                  + saveFile.getName() + "\"",
                                  "a delightful message");
        } catch (IOException ioe) {
          Utilities.log(ioe);
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                  "error encountered while saving...",
                                  "a sorry message");
        } catch(Exception e) {
          Utilities.log(e);
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                 "unexpected error encountered while saving...",
                                  "a sorry message");
        } finally {
          try {
            if(writer != null) {
              writer.close();
            }//if
          } catch(Exception e) {
            Utilities.log(e);
            Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                    "error encountered while closing file...",
                                    "a sorry message");
          }//try catch
        }//try catch finally
      }//actionPerformed
    });
    load.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        ObjectInputStream reader = null;
        File loadFile = Utilities.getFileFromUser(SDBGUI.this, "data");
        if(loadFile == null)
          return;
        if(!loadFile.getName().endsWith(".data")) {
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                  "error encountered while reading file...",
                                  "a sorry message");
          return;
        }//if

        try {
          reader = new ObjectInputStream(new FileInputStream(loadFile));
          SongDatabase newDatabase = (SongDatabase) reader.readObject();
          replace(newDatabase);
          pack();
          System.out.println("read successfully from: " + loadFile.getName());
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                  "database loaded successfully to: \""
                                  + loadFile.getName() + "\"",
                                  "a delightful message");
        } catch (IOException ioe) {
          Utilities.log(ioe);
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                  "error encountered while reading file...",
                                  "a sorry message");
        } catch(ClassCastException cce) {
          Utilities.log(cce);
          Utilities.showMsgDialog(SDBGUI.this, "scary_icon.png",
                                  "file has been corrputed...",
                                  "a curious message");
        } catch(ClassNotFoundException cfe) {
          Utilities.log(cfe);
          Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                  "Something unexpected happened...",
                                  "a curious and sorry message");
        } finally {
          try { reader.close(); }
          catch(Exception e) {
            Utilities.log(e);
            Utilities.showMsgDialog(SDBGUI.this, "cute_icon.png",
                                    "error encountered while closing file...",
                                    "a sorry message");
          }//try catch closse reader
        }//try catches finally

      }//actionPerformed
    });

    player.setPlaybackListener(new PlayFinishedListener() {
      @Override
      public void playFinished() {
        Song nextSong = getNextSong();
        if(nextSong != null) {
          try {
            player.play(nextSong.getURL());
            currentSongInfo.setText(nextSong.shortString());
          } catch(IOException e) {
            Utilities.log(e);
            System.out.println("Error while trying to play song");
            e.printStackTrace();
          }
        } else {
          play.setText("Play");
          currentSongInfo.setText(default_current_song_info_string);
        }
      }//playFinished
    });
  }//setInteraction

  //NOTE: UPDATE THIS WHEN MAIN FEATURE FINISHED
  private Song getNextSong() {
    return null;
  }

}//class
