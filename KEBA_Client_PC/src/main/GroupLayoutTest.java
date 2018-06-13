package main;
import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;

public class GroupLayoutTest {
    public static void main(String[] args) {
        int n = 0;
        showFrame(setSingleGroupLayout(new JFrame("Group Layout Test 1")), n++);
        showFrame(setSingleGroupLayoutButtonsSeparated(new JFrame("Group Layout Test 2")), n++);
        showFrame(setMixedLayout(new JFrame("Group Layout Test 3")), n++);
    }

    private static void showFrame(JFrame jFrame, int n) {
        jFrame.pack();
        jFrame.setLocation(n * 200, n * 150);
        jFrame.setVisible(true);
    }

    private static JFrame setSingleGroupLayout(JFrame jFrame) {
        JPanel jPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(jPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        jPanel.setLayout(groupLayout);

        JLabel nameLable = new JLabel("Name:");
        JTextField nameInput = new JTextField(30);
        JLabel passLable = new JLabel("Passwort:");
        JTextField pasInput = new JPasswordField(20);
        JButton loginButton = new JButton("login");
        JButton cancelButton = new JButton("cancel");
        
        // added
        ParallelGroup horizontalGroup=groupLayout.createParallelGroup();
 
        groupLayout.setHorizontalGroup(groupLayout
                .createSequentialGroup()
                .addGroup(
                        groupLayout.createParallelGroup().addComponent(nameLable).addComponent(passLable)
                                .addComponent(loginButton))
                .addGroup(
                        groupLayout.createParallelGroup().addComponent(nameInput).addComponent(pasInput)
                                .addComponent(cancelButton)));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(nameLable).addComponent(nameInput))
                .addGroup(groupLayout.createParallelGroup().addComponent(passLable).addComponent(pasInput))
                .addGroup(groupLayout.createParallelGroup().addComponent(loginButton).addComponent(cancelButton)));

        jFrame.add(jPanel);
        return jFrame;
    }

    private static JFrame setSingleGroupLayoutButtonsSeparated(JFrame jFrame) {
        JPanel jPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(jPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        jPanel.setLayout(groupLayout);

        JLabel nameLable = new JLabel("Name:");
        JTextField nameInput = new JTextField(30);
        JLabel passLable = new JLabel("Passwort:");
        JTextField pasInput = new JPasswordField(20);
        JButton loginButton = new JButton("login");
        JButton cancelButton = new JButton("cancel");

        JPanel buttonGroup = new JPanel(new GridLayout(1, 0, 5, 5));
        buttonGroup.add(loginButton);
        buttonGroup.add(cancelButton);

        constructGroupLayout(groupLayout, nameLable, nameInput, passLable, pasInput, buttonGroup);

        jFrame.add(jPanel);
        return jFrame;
    }

    private static void constructGroupLayout(GroupLayout groupLayout, JLabel nameLable, JTextField nameInput,
            JLabel passLable, JTextField pasInput, JPanel buttonGroup) {
        groupLayout.setHorizontalGroup(groupLayout
                .createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(nameLable).addComponent(passLable).addGap(0))
                .addGroup(
                        groupLayout.createParallelGroup().addComponent(nameInput).addComponent(pasInput)
                                .addComponent(buttonGroup)));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup().addComponent(nameLable).addComponent(nameInput))
                .addGroup(groupLayout.createParallelGroup().addComponent(passLable).addComponent(pasInput))
                .addGroup(groupLayout.createParallelGroup().addGap(0).addComponent(buttonGroup)));
    }

    private static JFrame setMixedLayout(JFrame jFrame) {
        JPanel jPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(jPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        jPanel.setLayout(groupLayout);

        JLabel nameLable = new JLabel("Name:");
        JTextField nameInput = new JTextField(30);
        JLabel passLable = new JLabel("Passwort:");
        JTextField pasInput = new JPasswordField(20);
        JButton loginButton = new JButton("login");
        JButton cancelButton = new JButton("cancel");

        JPanel buttonGroup = new JPanel(new GridLayout(1, 0, 5, 5));
        buttonGroup.add(loginButton);
        buttonGroup.add(cancelButton);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonGroup, BorderLayout.EAST);
        jFrame.add(bottomPanel, BorderLayout.SOUTH);

        constructGroupLayout(groupLayout, nameLable, nameInput, passLable, pasInput, bottomPanel);

        jFrame.add(jPanel);
        return jFrame;
    }
}
