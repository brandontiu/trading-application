package AdminGUI;

import Admins.AdminMenuController;
import Popups.PopUpWindow;
import Presenters.AdminMenuPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAdminUserWindow {
    private final AdminMenuController amc;
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    private JPanel mainPanel = new JPanel();
    private JLabel username = new JLabel();
    private JLabel password = new JLabel();
    private JButton createAccountButton = new JButton();
    private JTextField usernameTextField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);

    public AddAdminUserWindow(AdminMenuController amc) {
        this.amc = amc;
    }
    public void display(){
        JFrame frame = new JFrame(amp.createNewAdmin);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // set the frame's size and centre it
        frame.setSize(new Dimension(500, 350));
        frame.setLocationRelativeTo(null);
        // display window
        frame.setVisible(true);

        frame.add(mainPanel);
        placeComponents(mainPanel);


        createAccountButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String passText = new String(passwordField.getPassword());
                String nameText = usernameTextField.getText();
                if (nameText.isEmpty()){
                    new PopUpWindow(amp.enter("username")).display();
                }
                if (passText.isEmpty()){
                    new PopUpWindow(amp.enter("password")).display();
                }else{
                    if(amc.createAdmin(nameText,passText)){//new admin account successfully added
                        new PopUpWindow(amp.successfully("Admin created")).display();
                    }else{//new admin username is taken
                        new PopUpWindow(amp.usernameTaken).display();
                    }
                }
            }
        });
    }
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);
        username.setText(amp.username);
        username.setBounds(50, 80, 150, 25);
        panel.add(username);

        password.setText(amp.password);
        password.setBounds(50, 130, 150, 25);
        panel.add(password);

        usernameTextField.setBounds(220, 80, 165, 25);
        panel.add(usernameTextField);

        passwordField.setBounds(220, 130, 165, 25);
        panel.add(passwordField);

        createAccountButton.setText(amp.createAccount);
        createAccountButton.setBounds(150, 200, 160, 25);
        panel.add(createAccountButton);
        }


    }
