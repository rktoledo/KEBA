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

public class ServerStateView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel lbl1;
	private JLabel lbl2;
	private JButton butServState;
	
	private TitledBorder border;
	
	private Boolean isConnected;
    
    public ServerStateView(Boolean isConnected){
    	this.isConnected= isConnected;
    	initForm();
    }

    private void initForm(){
    	this.setLayout(new GridLayout(1,3));
    	setBorder();
	    
    	lbl1 = new JLabel("Server state: ", SwingConstants.RIGHT);
    	lbl2 = new JLabel("Stopped");
    	butServState = new JButton("Start Server");
    	
    	this.applyComponentOrientation(
    		    ComponentOrientation.getOrientation(
    		        this.getLocale()));
        
        this.add(lbl1);
        this.add(lbl2);
        this.add(butServState);
        
        greyOut(isConnected);
    }
    
    public void greyOut(Boolean isConnected){
    	butServState.setEnabled(isConnected); 
    }
    
    private void setBorder(){
    	border= new TitledBorder("Server Information");
    	border.setTitleJustification(TitledBorder.CENTER);
	    border.setTitlePosition(TitledBorder.TOP);
	    border.setBorder(BorderFactory.createEtchedBorder());
	    this.setBorder(border);
    }

    public void setServerState(boolean isRunning){
    	if (isRunning){
    		lbl2.setText("Running");
    		butServState.setText("Stop Server");
    	}
    	else {
    		lbl2.setText("Stopped");
    		butServState.setText("Start Server");
    	}
    }
    
    public void setServerStateListener(ActionListener l){
        this.butServState.addActionListener(l);
    }
}
