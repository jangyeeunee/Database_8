//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class buttonAction implements ActionListener {
//    private String actionType;
//    private String id;
//    private String password;
//    private JFrame parentFrame;
//
//    public buttonAction(String actionType, String id, String password, JFrame parentFrame) {
//        this.actionType = actionType;
//        this.id = id;
//        this.password = password;
//        this.parentFrame = parentFrame;
//    }
//
//    public buttonAction(String actionType, String id){
//        this.actionType = actionType;
//        this.id = id;
//    }
//    public buttonAction(){}
//
//    public void setActionType(String actionType) {
//        this.actionType = actionType;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setLogin(String actionType, String id, String password,JFrame parentFrame) {
//        this.actionType = actionType;
//        this.id = id;
//        this.password = password;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        dbConnect db = new dbConnect(actionType, id, password,parentFrame);
//        db.connectDB();
//        db.getActionDb();
//    }
//}
