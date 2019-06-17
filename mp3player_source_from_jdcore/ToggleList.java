import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListModel;









public class ToggleList
  extends JList
{
  public ToggleList()
  {
    setSelectionModel(new ToggleList.ToggleSelectionModel(null));
  }
  


  public void removeSelection() { removeSelectionInterval(0, getModel().getSize() - 1); }
  
  private class ToggleSelectionModel extends DefaultListSelectionModel {
    private ToggleSelectionModel() {}
    
    boolean gestureStarted = false;
    
    public void setSelectionInterval(int paramInt1, int paramInt2) {
      if ((isSelectedIndex(paramInt1)) && (!gestureStarted)) {
        super.removeSelectionInterval(paramInt1, paramInt2);
      }
      else {
        super.setSelectionInterval(paramInt1, paramInt2);
      }
      gestureStarted = true;
    }
    
    public void setValueIsAdjusting(boolean paramBoolean) {
      if (!paramBoolean) {
        gestureStarted = false;
      }
    }
  }
}
