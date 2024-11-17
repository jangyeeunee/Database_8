import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class buttonAction implements ActionListener {
    private String actionType;
    private String id;
    private String password;

    public buttonAction(String actionType, String id, String password) {
        this.actionType = actionType;
        this.id = id;
        this.password = password;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dbConnect db = new dbConnect(actionType, id, password);
        db.getActionDb();
    }
}
