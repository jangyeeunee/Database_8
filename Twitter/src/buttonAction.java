import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class buttonAction implements ActionListener {
    private String actionType;
    private String id;
    private String password;
    private JFrame parentFrame;

    public buttonAction(String actionType, String id, String password, JFrame parentFrame) {
        this.actionType = actionType;
        this.id = id;
        this.password = password;
        this.parentFrame = parentFrame;
    }
    public buttonAction(String actionType, String id){
        this.actionType = actionType;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dbConnect db = new dbConnect(actionType, id, password,parentFrame);
        db.getActionDb();
    }
}
