package gui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChartView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JPanel summaryPanel;
	private CardLayout cardLayout;
	
	private JPanel energyGraphTile;
	private JPanel voltageGraphTile;
	private JPanel currentGraphTile;
	private LoadingInfoView infoViewTile;
	
	private JPanel energyGraph;
	private JPanel voltageGraph;
	private JPanel currentGraph;
	private JPanel noConnectionPanel;
	private LoadingInfoView infoView;
	
	public ChartView(JPanel sumEnergyGraph, JPanel sumVoltageGraph, JPanel sumCurrentGraph, LoadingInfoView sumInfoView,
			JPanel energyGraph, JPanel voltageGraph, JPanel currentGraph, LoadingInfoView infoView){
		cardLayout= new CardLayout();
		this.setLayout(cardLayout);
		
		summaryPanel= new JPanel();
		summaryPanel.setLayout(new GridLayout(2, 2));
		
		this.energyGraphTile= sumEnergyGraph;
		this.voltageGraphTile= sumVoltageGraph;
		this.currentGraphTile= sumCurrentGraph;
		this.infoViewTile= sumInfoView;
		
		this.energyGraph= energyGraph;
		this.voltageGraph= voltageGraph;
		this.currentGraph= currentGraph;
		this.infoView= infoView;
		this.noConnectionPanel= new JPanel(new BorderLayout());
		
		JLabel noConnectionLabel= new JLabel("No connection to Server");
		this.noConnectionPanel.add(noConnectionLabel, BorderLayout.CENTER);
		
		summaryPanel.add(this.energyGraphTile);
		summaryPanel.add(this.infoViewTile);
		summaryPanel.add(this.voltageGraphTile);
		summaryPanel.add(this.currentGraphTile);
		
		this.add(summaryPanel, "Summary");
		this.add(this.energyGraph, "Energygraph");
		this.add(this.voltageGraph, "Voltagegraph");
		this.add(this.currentGraph, "Currentgraph");
		this.add(this.infoView, "loadinginfo");
		this.add(this.noConnectionPanel, "Serverconnection");
	}

	public JPanel getEnergyGraphTile() {
		return energyGraphTile;
	}

	public JPanel getVoltageGraphTile() {
		return voltageGraphTile;
	}

	public LoadingInfoView getInfoViewTile() {
		return infoViewTile;
	}
	
	public JPanel getCurrentGraphTile() {
		return currentGraphTile;
	}

	public void setEnergyGraphTile(JPanel view1) {
		this.energyGraphTile = view1;
	}

	public void setVoltageGraphTile(JPanel view2) {
		this.voltageGraphTile = view2;
	}

	public void setInfoViewTile(LoadingInfoView view3) {
		this.infoViewTile = view3;
	}

	public void setCurrentGraphTile(JPanel view4) {
		this.currentGraphTile = view4;
	}
	
	public JPanel getEnergyGraph() {
		return energyGraph;
	}

	public JPanel getVoltageGraph() {
		return voltageGraph;
	}

	public LoadingInfoView getInfoView() {
		return infoView;
	}
	
	public JPanel getCurrentGraph() {
		return currentGraph;
	}

	public void setEnergyGraph(JPanel view1) {
		this.energyGraph = view1;
	}

	public void setVoltageGraph(JPanel view2) {
		this.voltageGraph = view2;
	}

	public void setInfoView(LoadingInfoView view3) {
		this.infoView = view3;
	}

	public void setCurrentGraph(JPanel view4) {
		this.currentGraph = view4;
	}

	public void changeCurrentCard(String name) {
		this.cardLayout.show(this, name);
	}
	
	public void showConnectionState(Boolean isConnected){
		if (isConnected) this.cardLayout.show(this, "Summary");
		else this.cardLayout.show(this, "Serverconnection");
	}
	
	public void addNewGraphs(JPanel sumEnergyGraph, JPanel sumVoltageGraph, JPanel sumCurrentGraph, 
			JPanel energyGraph, JPanel voltageGraph, JPanel currentGraph){
		
		summaryPanel.remove(this.energyGraphTile);
		summaryPanel.remove(this.voltageGraphTile);
		summaryPanel.remove(this.currentGraphTile);
		
		this.energyGraphTile= sumEnergyGraph;
		this.voltageGraphTile= sumVoltageGraph;
		this.currentGraphTile= sumCurrentGraph;
		
		summaryPanel.add(this.energyGraphTile);
		summaryPanel.add(this.voltageGraphTile);
		summaryPanel.add(this.currentGraphTile);
		
		summaryPanel.revalidate();
		summaryPanel.repaint();
		
		this.remove(this.energyGraph);
		this.remove(this.voltageGraph);
		this.remove(this.currentGraph);
		
		this.energyGraph= energyGraph;
		this.voltageGraph= voltageGraph;
		this.currentGraph= currentGraph;

		this.add(this.energyGraph, "Energygraph");
		this.add(this.voltageGraph, "Voltagegraph");
		this.add(this.currentGraph, "Currentgraph");
		
		this.revalidate();
		this.repaint();
	}
}
