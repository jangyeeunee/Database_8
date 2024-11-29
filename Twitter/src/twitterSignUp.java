import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class twitterSignUp extends JFrame {

    public twitterSignUp() throws ParseException {
        Map<String,String> data = new HashMap<>();
        // Set up the frame
        setTitle("Sign up to Twitter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Twitter logo
        JLabel logoLabel = new JLabel(new ImageIcon("twitter_logo.png")); // Add a Twitter logo here if available
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);

        // Title label
        JLabel titleLabel = new JLabel("Sign up to Twitter");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username field
        JTextField IdField = new JTextField(15);
        IdField.setMaximumSize(new Dimension(300, 30));
        IdField.setFont(new Font("Arial", Font.PLAIN, 14));
        IdField.setBorder(BorderFactory.createTitledBorder("Id"));
        panel.add(IdField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password field
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passwordField);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        //first name filed
        JTextField firstNameFiled = new JTextField(15);
        firstNameFiled.setMaximumSize(new Dimension(300, 30));
        firstNameFiled.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameFiled.setBorder(BorderFactory.createTitledBorder("First Name"));
        panel.add(firstNameFiled);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        //second name filed
        JTextField lastNameFiled = new JTextField(15);
        lastNameFiled.setMaximumSize(new Dimension(300, 30));
        lastNameFiled.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameFiled.setBorder(BorderFactory.createTitledBorder("Second Name"));
        panel.add(lastNameFiled);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        //email filed
        JTextField emailFiled = new JTextField(15);
        emailFiled.setMaximumSize(new Dimension(300, 30));
        emailFiled.setFont(new Font("Arial", Font.PLAIN, 14));
        emailFiled.setBorder(BorderFactory.createTitledBorder("Email"));
        panel.add(emailFiled);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add Spinner for date
        JLabel dateLabel = new JLabel("Date of Birth:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setMaximumSize(new Dimension(300, 30));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create a sub-panel for the spinner to control its size
        JPanel spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.setBackground(Color.WHITE);

        SpinnerDateModel birthModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(birthModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        // Set preferred size for the spinner
        dateSpinner.setPreferredSize(new Dimension(300, 30)); // Adjust width and height
        spinnerPanel.add(dateSpinner);
        panel.add(spinnerPanel);

        //get date to string
        Date selectedDate = (Date) dateSpinner.getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel phoneNumLabel = new JLabel("Phone number:");
        phoneNumLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneNumLabel.setMaximumSize(new Dimension(300, 30));
        phoneNumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(phoneNumLabel);



        //phone number filed
        MaskFormatter phoneFormatter = new MaskFormatter("###-####-####");
        phoneFormatter.setPlaceholderCharacter('_');
        JFormattedTextField phoneField = new JFormattedTextField(phoneFormatter);
        phoneField.setColumns(11);
        phoneField.setMaximumSize(new Dimension(300, 30));
        panel.add(phoneField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel genderLabel = new JLabel("Select Gender:");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        genderLabel.setMaximumSize(new Dimension(300, 30));
        genderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(genderLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));



        // Add radio buttons to a sub-panel
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        genderPanel.setBackground(Color.WHITE);

        // Radio buttons for gender
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        String Gender;
        if (maleButton.isSelected()) {
            Gender = "M";
        } else if (femaleButton.isSelected()) {
            Gender = "F";
        } else {
            Gender = "N";
        }

        panel.add(genderPanel);


        // Sign up button
        JButton loginButton = new JButton("Sign up");
        loginButton.setBackground(new Color(29, 161, 242));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loginButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        loginButton.addActionListener(e -> {
            data.put("id",IdField.getText());
            data.put("password",new String(passwordField.getPassword()));
            data.put("first_name",firstNameFiled.getText());
            data.put("last_name",lastNameFiled.getText());
            data.put("email",emailFiled.getText());
            data.put("phone_number",phoneField.getText());
            data.put("birth",dateFormat.format(selectedDate));
            data.put("gender",Gender);

            dbConnect db = dbConnect.getInstance();
            boolean loginSuccess = db.SignUpDB(data,this);
            System.out.println("로그인 실패");
            System.out.println(loginSuccess);
            if(loginSuccess) {
                try {
                    TwitterLogin twitterLogin = new TwitterLogin();
                    dispose();
                } catch (Exception e1) {
                }
            }
        });

        // Add the main panel to the frame
        add(panel);
        setVisible(true);
    }

}

