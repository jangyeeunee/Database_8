import java.awt.event.*;

public class buttonAction implements ActionListener {

    private String actionType;

    public buttonAction(String getActionType) {

        this.actionType = getActionType;
    }

    public void actionPerformed(ActionEvent e) {
        dbConnect connect = new dbConnect(actionType);
    }
}