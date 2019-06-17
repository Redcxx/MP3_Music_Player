import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;

public class AddSongGui extends JFrame{
  private final JLabel title, genre, artist, url;
  private final JTextField titleText, genreText, artistText, urlText;
  private final JButton add, browse, cancel;
  private final JPanel pane;

  private final Container contents;
  private final GridBagConstraints settings;

  private final SDBGUI parent;

  public AddSongGui(SDBGUI parent) {
    this.parent = parent;
    setTitle("add");
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    url  = new JLabel("URL: ");
    genre = new JLabel("Genre: ");
    title  = new JLabel("Title: ");
    artist  = new JLabel("Artist: ");
    urlText  = new JTextField("");
    genreText = new JTextField("");
    artistText = new JTextField("");
    titleText = new JTextField("");
    add  = new JButton("Add");
    cancel = new JButton("Cancel");
    browse  = new JButton("Browse");
    pane = new JPanel();

    contents = getContentPane();
    settings = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(10,10,10,10), 0, 0);
    init();

    setInteraction();

    pack();
    setLocationRelativeTo(null);
  }//constructor

  private void init() {

    pane.setLayout(new GridBagLayout());

    pane.add(title, settings);
    settings.gridx++;
    settings.gridwidth = 2;
    pane.add(titleText, settings);
    settings.gridy++;
    pane.add(genreText, settings);
    settings.gridx--;
    pane.add(genre, settings);
    settings.gridy++;
    pane.add(artist, settings);
    settings.gridx++;
    settings.gridwidth = 2;
    pane.add(artistText, settings);
    settings.gridy++;
    pane.add(urlText, settings);
    settings.gridx--;
    pane.add(url, settings);
    settings.gridwidth = 1;
    settings.gridy++;
    pane.add(cancel, settings);
    settings.gridx++;
    pane.add(browse, settings);
    settings.gridx++;
    pane.add(add, settings);

    contents.add(BorderLayout.CENTER, pane);

    Utilities.paintGui(AddSongGui.this);

    // cancel.setBackground(Color.RED.darker().darker());

  }//init

  private void setInteraction() {
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        String[] options = { "just close it!", "actually no" };
        int reply = Utilities.showQuitDialog( AddSongGui.this, options,
                                              "./cute_icon.png",
                                              "are you sure you want to quit?"
                                              + "\nchanges will be lost!",
                                              "wait a moment...");
        if(reply == 0)
          dispose();
        else {
          System.out.println("Weebcome baaak");
        }//ifelse
      }//actionPerformed
    });//anonymous listener
    browse.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        File file = Utilities.getFileFromUser(AddSongGui.this, "mp3");
        if(file != null) {
          if(file.getName().endsWith(".mp3")) {
            try {
              String songUrl = file.toURI().toURL().toString();
              String songString = "\t\t\t" + songUrl + "\t0";
              Song song = new Song(songString);
              urlText.setText(songUrl);
            } catch(MalformedURLException me) {
              Utilities.log(me);
              Utilities.showMsgDialog(AddSongGui.this, "./cute_icon.png",
                              "This file can't be converted into valid url...",
                                      "a sorry message");
            } catch(CorruptedSongException ce) {
              Utilities.log(ce);
              Utilities.showMsgDialog(AddSongGui.this, "./cute_icon.png",
                              "This file is not successfully converted...",
                                      "a sorry message");
            }//try catch
          } else {// not mp3 file
            Utilities.showMsgDialog(AddSongGui.this, "./scary_icon.png",
                                    "Please provide a valid \".mp3\" file",
                                    "a message meant to scare you");
          }//if else, check if its mp3
        } else {
          System.out.println("User did not select any file");
        }//if else`
      }//actionPerformed
    });
    add.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
          String input = "";
          input += titleText.getText() + "\t";
          input += artistText.getText() + "\t";
          input += genreText.getText() + "\t";
          input += urlText.getText() + "\t";
          input += 0;
          try {
            Song newSong = new Song(input);
            if(parent.addSong(newSong)) {
              Utilities.showMsgDialog(AddSongGui.this, "cute_icon.png",
                            "Song successfully added", "a cheerful message");
              parent.pack();
              dispose();
            } else {
              String[] opts = { "Just override it!",
                                "wait, it's a mistakaaa" };
              int reply = Utilities.showQuitDialog(AddSongGui.this, opts,
                                      "./cute_icon.png",
                                      "Song with the same title, artist and"
                                      + "\ngenre already exists in database"
                                      + "\ndo you wish to override it?",
                                      "heeey just to confirm..");
              if(reply == 0) {
                parent.addSongForce(newSong);
                dispose();
              } else {
                System.out.println("good decision!");
              }//if else
            }//if else
          } catch(Exception e) {
            Utilities.log(e);
            Utilities.showMsgDialog(AddSongGui.this, "scary_icon.png",
                          "Url given is invalid", "a unhappy message");
          }//try catch
      }//actionPerformed
    });
  }//setInteraction

}//class
