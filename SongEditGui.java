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


public class SongEditGui extends JFrame {
  private final JLabel title, genre, artist, url;
  private final JTextField titleText, genreText, artistText, urlText;
  private final JButton apply, delete, close;
  private final JPanel pane;

  private final Container contents;
  private final GridBagConstraints settings;

  private final SDBGUI parent;

  public SongEditGui(SDBGUI parent, Song song) {
    super();
    this.parent = parent;

    String titleStr = "edit: " + song.getTitle() + " - " + song.getArtist();
    setTitle(titleStr);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo(null);

    url  = new JLabel("URL: ");
    genre = new JLabel("Genre: ");
    title  = new JLabel("Title: ");
    artist  = new JLabel("Artist: ");
    urlText  = new JTextField(song.getURL());
    genreText = new JTextField(song.getGenre());
    artistText = new JTextField(song.getArtist());
    titleText = new JTextField(song.getTitle());
    delete  = new JButton("Delete");
    apply  = new JButton("Apply");
    close = new JButton("Close");
    pane = new JPanel();

    contents = getContentPane();
    settings = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(10,10,10,10), 0, 0);

    init();

    setInteraction(song);

    pack();
    setLocationRelativeTo(null);
  }//SongEditGui

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
    pane.add(delete, settings);
    settings.gridx++;
    pane.add(apply, settings);
    settings.gridx++;
    pane.add(close, settings);

    contents.add(BorderLayout.CENTER, pane);

    Utilities.paintGui(SongEditGui.this);

    delete.setBackground(Color.RED.darker().darker());

  }//init

  private void setInteraction(final Song song) {

    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        String[] options = { "close this edit!", "actually no" };
        int reply = Utilities.showQuitDialog( SongEditGui.this, options,
                                              "./cute_icon.png",
                                              "Are you sure you want to quit ?",
                                              "wait a moment...");
        if(reply == 0) dispose();
        else { System.out.println("Weebcome baaak"); }//ifelse
      }//actionPerformed
    });//anonymous listener
    delete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        String[] options = { "I'm 100% sure!", "actually no" };
        int reply = Utilities.showQuitDialog( SongEditGui.this, options,
                                      "./cute_icon.png",
                                      "Are you sure to delete this song ?\n"
                                   + "NOTE: This can't be recovered internally",
                                      "wait a moment...");
        if(reply == 0) {
            if (parent.removeSong(song)) {
              System.out.println("remove succeed");
            } else {
              Utilities.showMsgDialog(SongEditGui.this, "./cute_icon.png",
                                      "a unknown problem is encountered "
                                       +"\nand song can't be deleted...",
                                      "a sorry message");
              System.out.println("remove failed");
            }//ifelse
            dispose();
        } else {
          System.out.println("you love ur song");
        }//ifelse
      }//actionPerformed
    });
    apply.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        String[] options = { "just do it!", "let me reconsider.." };
        int reply = Utilities.showQuitDialog( SongEditGui.this, options,
                                              "./cute_icon.png",
                                   "This will reset song play count, continue?",
                                              "hold a sec...");
        if(reply == 0) {
          if(parent.removeSong(song)) {
            String input = "";
            input += titleText.getText() + "\t";
            input += artistText.getText() + "\t";
            input += genreText.getText() + "\t";
            input += urlText.getText() + "\t";
            input += song.getTimesPlayed();
            try {
              parent.addSong(new Song(input));
              dispose();
            } catch(Exception e) {
              Utilities.log(e);
              parent.addSong(song);
              Utilities.showMsgDialog(SongEditGui.this, "scary_icon.png",
                            "Url given is invalid", "a unhappy message");
            }//try catch
          }//if
        } else {
          System.out.println("Weebcome baaak");
        }//ifelse
      }//actionPerformed
    });
  }//setInteraction

}//class SongEditGui
