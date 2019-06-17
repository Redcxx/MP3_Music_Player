import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Container;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class OptionsGui extends JFrame {
  private final JLabel filter, sort;
  private final JButton apply, cancel;
  private final JComboBox<Song.DATA_TYPE> filterBox, sortBox;
  private final JTextField filterText;

  private final SDBGUI parent;

  private final GridBagLayout gridBag;
  private final GridBagConstraints settings;
  private final Container contents;

  private final String filterPlaceHolder="Enter filter text here...";

  public OptionsGui(SDBGUI parent) {
    this.parent = parent;

    setTitle("Options");
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    sort = new JLabel("Sort");
    apply = new JButton("Apply");
    cancel = new JButton("Cancel");
    filter = new JLabel("Filter");
    settings = new GridBagConstraints();
    filterText = new JTextField(filterPlaceHolder);
    filterBox = new JComboBox<Song.DATA_TYPE>();
    sortBox = new JComboBox<Song.DATA_TYPE>();
    gridBag = new GridBagLayout();
    contents = getContentPane();

    init();

    setInteraction();

    pack();
    setLocationRelativeTo(null);
  }//constructor

  private void init() {
    //add items to comboboxes
    for(Song.DATA_TYPE type : Song.DATA_TYPE.values()) {
      if(!type.toString().startsWith("Exact")) {
        sortBox.addItem(type);
      }//if
      filterBox.addItem(type);
    }//for

    setLayout(gridBag);
    settings.fill = GridBagConstraints.HORIZONTAL;
    settings.insets = new Insets(5,5,5,5);
    settings.ipady = 5;
    settings.ipadx = 10;
    settings.weightx = 1.0;
    settings.weighty = 1.0;
    settings.gridx = 0;
    settings.gridy = 0;
    settings.gridwidth = 1;
    contents.add(filter, settings);
    settings.gridx = 1;
    settings.gridwidth = 2;
    contents.add(filterBox, settings);
    settings.gridx = 3;
    contents.add(filterText, settings);
    settings.gridx = 0;
    settings.gridy = 1;
    settings.gridwidth = 1;
    contents.add(sort, settings);
    settings.gridx = 1;
    settings.gridwidth = 2;
    contents.add(sortBox, settings);
    settings.gridx = 0;
    settings.gridy = 2;
    settings.gridwidth = 3;
    contents.add(apply, settings);
    settings.gridx = 4;
    contents.add(cancel, settings);

    Utilities.paintGui(OptionsGui.this);
  }//init

  private void setInteraction() {
    filterText.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent fe) {
        if(filterText.getText().equals(filterPlaceHolder)) {
          filterText.setText("");
        }
      }//gainedFocus
      @Override
      public void focusLost(FocusEvent fe) {
        if(filterText.getText().equals("")) {
          filterText.setText(filterPlaceHolder);
        }//if
      }//focusLost
    });
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        dispose();
      }//actionPerformed
    });
    apply.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Song.DATA_TYPE filterType= (Song.DATA_TYPE) filterBox.getSelectedItem();
        Song.DATA_TYPE sortType = (Song.DATA_TYPE) sortBox.getSelectedItem();
        String filterString =
                            filterText.getText().replace(filterPlaceHolder, "");
        parent.filterAndSort(filterType, filterString, sortType);
        dispose();
      }//actionPerformed
    });
  }
}//class OptionsGui
