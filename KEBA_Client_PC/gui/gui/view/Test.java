package gui.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Test extends JPanel{
	public Test() {
		setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane);
		
		JLabel lblNewLabel = new JLabel("New label");
		splitPane.setLeftComponent(lblNewLabel);
		
		JButton btnNewButton = new JButton("New button");
		splitPane.setRightComponent(btnNewButton);
	}
	
}
