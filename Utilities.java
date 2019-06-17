import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.AbstractButton;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class Utilities {
  private Utilities() {}

  public static Color smokered = new Color(137, 78, 84);//smoken red
  public static Color lightred = new Color(227, 180, 184);//mouse nose red
  public static Color taocolor = new Color(245, 150, 170);//tao
  public static Color yingcolor = new Color(254, 223, 225);//ying hua
  public static Color huicolor = new Color(215, 196, 187);//hui ying
  public static Color transparentColor = new Color(0,0,0,0);

  public static int showQuitDialog(Component parent, String[] opts,
                                  String iconPath, String msg, String heading) {
    Icon cuteIcon = getIcon(iconPath);
    JButton[] options = getOptions(opts);

    JOptionPane pane = new JOptionPane();
    pane.setIcon(cuteIcon);
    pane.setMessage(msg);
    pane.setOptions(options);
    pane.setInitialSelectionValue(options[0]);
    paintAllComponents(pane);
    //to override paintAllComponents button:
    paintMarginedButtons(options);
    JDialog dialog = pane.createDialog(parent, heading);
    dialog.setVisible(true);
    dialog.dispose();

    //from java src cde#JOptionPane#showOptionDialog
    Object selectedValue = pane.getValue();
    if(selectedValue == null)
      return JOptionPane.CLOSED_OPTION;
    if(options == null) {
      if(selectedValue instanceof Integer)
        return ((Integer)selectedValue).intValue();
      return JOptionPane.CLOSED_OPTION;
    }
    for(int counter = 0, maxCounter = options.length;
      counter < maxCounter; counter++) {
      if(options[counter].equals(selectedValue))
        return counter;
    }
    return JOptionPane.CLOSED_OPTION;
  }//showQuitDialog

  public static void showMsgDialog(Component parent, String iconPath,
                                          String msg, String heading) {
    String[] opts = {"OK Got That"};
    showQuitDialog(parent, opts, iconPath, msg, heading);
  }//showMsgDialog

  @SuppressWarnings("unchecked")
  private static void paintAllComponents(Container parent) {
    for(Component c : parent.getComponents()) {
      if(c instanceof JButton) {
        paintBtn((AbstractButton) c);

      } else if(c instanceof JComboBox) {
        ((JComboBox<Song>) c).setRenderer(new MP3PlayerListCellRenderer());
        paintbackfore(c);

      } else if(c instanceof JList) {
        ((JList<Song>) c).setCellRenderer(new MP3PlayerListCellRenderer());
        paintbackfore(c);

      } else if(c instanceof Container) {
        paintAllComponents((Container) c);
      }//if else if else if else if 
    }//for
    paintbackfore(parent);
  }//paintAllComponents

  private static JButton[] getOptions(String[] strs) {
    final JButton[] btns = new JButton[strs.length];
    // final Insets margin = new Insets(10,10,10,10);
    //does not work due to custom border
    for(int i = 0; i < strs.length; i++) {
      final JButton btn = new JButton(strs[i]);
      btns[i] = btn;
      btn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
          JOptionPane pane = getOptionPane((JComponent) ae.getSource());
          pane.setValue(btn);
        }//actionPerformed
      });
    }//for
    return btns;
  }//getOptions

  public static Border taoBorder =
  BorderFactory.createLineBorder(taocolor, 1, true);
  public static Border emptyBorder = BorderFactory.createEmptyBorder();
  public static Border margin =
                            BorderFactory.createLineBorder(transparentColor, 4);
  public static CompoundBorder marginBorder =
                          BorderFactory.createCompoundBorder(taoBorder, margin);
  //for list cell selection and no selection
  public static Border selectedBorder =
                                    BorderFactory.createLineBorder(lightred, 1);
  public static Border normalBorder =
                            BorderFactory.createLineBorder(transparentColor, 1);

  private static void paintMarginedButtons(AbstractButton[] btns) {
    for(AbstractButton btn : btns) {
      btn.setBorder(marginBorder);
    }//for
  }//paintMarginedButtons

  private static JOptionPane getOptionPane(JComponent parent) {
    //from java src cde
    JOptionPane pane = null;
    if (!(parent instanceof JOptionPane)) {
      pane = getOptionPane((JComponent)parent.getParent());
    } else {
      pane = (JOptionPane) parent;
    }
    return pane;
  }

  private static JPanel getPanel(String iconPath, String msg) {
      JPanel panel = new JPanel();
      JLabel label = new JLabel(msg);

      label.setIcon(getIcon(iconPath));
      panel.add(label);
      panel.setBackground(getBackgroundColor());

      return panel;
  }

  private static Icon getIcon(String path) {
      //trim icon
      ImageIcon cuteImageIcon = new ImageIcon(path);
      Image cuteImage =
      cuteImageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
      return new ImageIcon(cuteImage);
  }//getIcon

  public static File getFileFromUser(JFrame parent, String extension) {
    String userdir = System.getProperty("user.dir");
    JFileChooser fileChooser = new JFileChooser(userdir);
    FileNameExtensionFilter filter =
                      new FileNameExtensionFilter("*." + extension, extension);
    fileChooser.setFileFilter(filter);
    int buttonPressed = fileChooser.showOpenDialog(parent);

    if(buttonPressed == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    } else {
      //user cancelled file chooser
      return null;
    }//if else
  }//getFileFromUser

  public static void paintBtn(AbstractButton c) {
    paintbackfore(c);
    c.setBorder(taoBorder);
    c.setFocusPainted(false);
    c.setBorder(marginBorder);
  }//paintBtn

  public static void paintBtn(AbstractButton[] cs) {
    for(AbstractButton c : cs) {
      paintBtn(c);
    }//for
  }//paintBtn

  public static void paintbackfore(Component c) {
    c.setBackground(getBackgroundColor());
    c.setForeground(getForegroundColor());
  }//paintbackfore

  public static void paintbackfore(Component[] cs) {
    for(Component c : cs) {
      paintbackfore(c);
    }//for
  }//paintbackfore

  public static Color getBackgroundColor() {
    return smokered;
  }

  public static Color getForegroundColor() {
    return yingcolor;
  }

  public static Border getSelectedBorder() {
    return selectedBorder;
  }//getSelectedBorder

  public static Border getNormalBorder() {
    return normalBorder;
  }//getNormalBorder

  public static void paintGui(Container c) {
    paintAllComponents(c);
  }

  //log part///////////////////

  private static PrintWriter writer = null;
  private static File log = new File("log.txt");
  // static {
  //   File log = new File("log.txt");
  //   if(!log.exists()) {
  //     try {
  //       log.createNewFile();
  //     } catch(IOException e) {
  //       e.printStackTrace();
  //       System.out.println("error while creating log file");
  //     }
  //   }
  // }//static
  public static void log(Throwable e) {
    try {
      String sls = System.getProperty("line.separator");
      String time = new GregorianCalendar().getTime().toString() + sls;
      writer = new PrintWriter(new FileWriter(log, true));

      writer.write(time);
      e.printStackTrace(writer);
      writer.write(sls);
    } catch(IOException ioe) {
      ioe.printStackTrace();
      System.err.println("error while logging exception");
    } finally {
      writer.close();
      if(writer != null && writer.checkError()) {
        System.err.println("error while checking error");
      }//if
    }//try catch finally
  }//log
}//Utilities
