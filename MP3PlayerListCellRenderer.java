import javax.swing.DefaultListCellRenderer;
import javax.swing.border.Border;
import javax.swing.Icon;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import sun.swing.DefaultLookup;


public class MP3PlayerListCellRenderer extends DefaultListCellRenderer {
  public MP3PlayerListCellRenderer() { super(); }

  //from java src cde#DefaultListCellRenderer#getListCellRendererComponent
  @Override
  public Component getListCellRendererComponent(  JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
    super.getListCellRendererComponent(list, value, index,
                                       isSelected, cellHasFocus);
    //color
    if (isSelected) {
        super.setBackground(new Color(183, 84, 96));
        super.setForeground(Color.WHITE);
    } else {
        super.setBackground(Utilities.getBackgroundColor());
        super.setForeground(Utilities.getForegroundColor());
    }//if else

    //string to display
    if (value instanceof Icon) {
      setIcon((Icon)value);
      setText("");
    } else {
      String str;
      if(value instanceof Song) {
        str = ((Song) value).shortString();
      } else {
        str = value.toString();
      }//if else
      setIcon(null);
      setText(str);
    }//if else

    //border
    Border border = null;
    if (cellHasFocus) {
      if (isSelected) {
        border = Utilities.getSelectedBorder();
      }//if
    } else {
      border = Utilities.getNormalBorder();
    }//if else
    setBorder(border);

    return this;
  }//getListCellRendererComponent

}//class
