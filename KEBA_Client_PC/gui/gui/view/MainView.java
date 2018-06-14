package gui.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

public class MainView extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Container container;
	
	private JTabbedPane cards;
	private JPanel actualCard;
	private JPanel loadingCard;
	private JPanel infoCard;
	private JPanel settingsCard;
	
	private JPanel bottomPanel;
	private JPanel serverConnectionView;
	private JPanel serverStateView;
	
	private JPanel noConnectionPanel;
	private JLabel noConnectionLabel;
	
	final String currentPanel = "Current Loading";
	final String loadingPanel = "All Loadings";
	final String infoPanel = "Info";
	final String settingPanel = "Settings";
	

	public MainView(JPanel actualCard, JPanel loadingCard, JPanel infoCard, JPanel settingsCard, 
			JPanel serverConnectionView, JPanel serverStateView){
		super("KEBA Client");
		container = this.getContentPane();
		container.setLayout(new BorderLayout());

		this.actualCard= actualCard;
		this.loadingCard= loadingCard;
		this.infoCard= infoCard;
		this.settingsCard= settingsCard;
		
		
		this.bottomPanel= new JPanel(new GridLayout(1, 2));
		this.serverConnectionView= serverConnectionView;
		this.serverStateView= serverStateView;
		
		Boolean isConnected= (actualCard!= null);

		initForm(isConnected);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	public void setActualCard(JPanel actualCard){
		this.actualCard= actualCard;
	}
	
	public void setLoadingCard(JPanel loadingCard){
		this.loadingCard= loadingCard;
	}
	
	public void setInfoCard(JPanel infoCard){
		this.infoCard= infoCard;
	}
	
	public void setSettingsCard(JPanel settingsCard){
		this.settingsCard= settingsCard;
	}
	
	public void changeLayoutConnection(Boolean isConnected){
		if (isConnected) {
			this.cards.remove(noConnectionPanel);
			this.cards.add(this.actualCard, currentPanel);
			this.cards.add(this.loadingCard, loadingPanel);
			this.cards.add(this.infoCard, infoPanel);
			this.cards.add(this.settingsCard, settingPanel);
		}
		else {
			this.cards.remove(actualCard);
			this.cards.remove(loadingCard);
			this.cards.remove(infoCard);
			this.cards.remove(settingsCard);
			this.cards.add(noConnectionPanel);
		}
		this.container.revalidate();
		this.container.repaint();
	}
	
	private void initForm(Boolean isConnected){
		this.cards = new JTabbedPane(JTabbedPane.TOP);
		
		if (isConnected){
			//Create the panel that contains the "cards".
			this.cards.add(this.actualCard, currentPanel);
			this.cards.add(this.loadingCard, loadingPanel);
			this.cards.add(this.infoCard, infoPanel);
			this.cards.add(this.settingsCard, settingPanel);
		}
		else{
			//Create the panel that contains the "cards".
			this.noConnectionPanel= new JPanel(new BorderLayout());
			this.noConnectionLabel= new JLabel("No connection to KEBA Server");
			this.noConnectionPanel.add(noConnectionLabel, BorderLayout.CENTER);
			this.cards.add(noConnectionPanel, "No connection");
		}
		
		//Create bottom panel
		this.bottomPanel.add(serverConnectionView);
		this.bottomPanel.add(serverStateView);
		
		this.container.add(cards, BorderLayout.CENTER);
		this.container.add(bottomPanel, BorderLayout.PAGE_END);
		//cardLayout.show(cardPanelMain, "Summary");
		
		this.pack();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	}
	
	public void setWinListener(WindowListener l){
        this.addWindowListener(l);
    }
}
