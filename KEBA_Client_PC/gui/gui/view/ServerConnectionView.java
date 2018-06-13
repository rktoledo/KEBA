package gui.view;

import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class ServerConnectionView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel lbl1;
	private JLabel lbl2;
	private JButton butConnState;
	
	private TitledBorder border;
    
    public ServerConnectionView(){
    	initForm();
    }

    private void initForm(){
    	this.setLayout(new GridLayout(1,3));
    	setBorder();
	    
    	lbl1 = new JLabel("Connection state: ", SwingConstants.RIGHT);
    	lbl2 = new JLabel("Not connected");
    	butConnState = new JButton("Connect");
    	
    	this.applyComponentOrientation(
    		    ComponentOrientation.getOrientation(
    		        this.getLocale()));
        
        this.add(lbl1);
        this.add(lbl2);
        this.add(butConnState);

    }
    
    private void setBorder(){
    	border= new TitledBorder("Server Connection");
    	border.setTitleJustification(TitledBorder.CENTER);
	    border.setTitlePosition(TitledBorder.TOP);
	    border.setBorder(BorderFactory.createEtchedBorder());
	    this.setBorder(border);
    }

    public void setConnectionState(boolean isConnected){
    	if (isConnected){
    		lbl2.setText("Connected");
    		butConnState.setText("Disconnect");
    	}
    	else {
    		lbl2.setText("Not connected");
    		butConnState.setText("Connect");
    	}
    }
    
    public void setServerConnectionListener(ActionListener l){
        this.butConnState.addActionListener(l);
    }
}

