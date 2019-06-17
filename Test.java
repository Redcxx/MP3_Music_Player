import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.Enumeration;

public class Test {
  // public static void main(String[] args) {
  //   // String str = "abcd | hjkl | zxcv"; //need use \\| instead
  //   // String str = "\t\t\tabcd\t0";
  //   // for(String s : str.split("\t"))
  //   //   System.out.println("str: " + s);
  //
  //   // magic
  //   //\u000d
       //System.out.println(new GregorianCalendar().getTime());
  // }

  public static void main(String[] args) {
            UIDefaults defaults = UIManager.getDefaults();
            System.out.println(defaults.size()+ " properties defined !");
            String[ ] colName = {"Key", "Value"};
            String[ ][ ] rowData = new String[ defaults.size() ][ 2 ];
            int i = 0;
            for(Enumeration e = defaults.keys(); e.hasMoreElements();){
                Object key = e.nextElement();
                if(key.toString().indexOf("OptionPane") >= 0) {
                  rowData[ i ] [ 0 ] = key.toString();
                  rowData[ i ] [ 1 ] = ""+defaults.get(key);
                  System.out.println(rowData[i][0]+" ,, "+rowData[i][1]);
                  i++;
                }
            }
            JFrame f = new JFrame("UIManager properties default values");
            JTable t = new JTable(rowData, colName);
            f.setContentPane(new JScrollPane(t));
            //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setVisible(true);
        }
}
