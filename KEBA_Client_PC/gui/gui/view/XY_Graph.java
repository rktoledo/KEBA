package gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dbObjects.LoadingObject;


public class XY_Graph extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JFreeChart chart;
	private XYDataset ds;
	private LoadingObject loading;
	private ChartPanel chartPanel;
	private JPanel panel= new JPanel();
	
	private XYSeriesCollection dataset;

	private XYSeries series; 
	
	private TitledBorder border;
	
	private XYItemRenderer renderer; 
	
	private String type;
	
	public XY_Graph(){
	}
	
	public XY_Graph(LoadingObject loading, String type, String unit, Boolean isLarge){
		String name= type+"graph";
		if (isLarge) name= "Summary";
		this.setName(name); 
		this.setLayout(new BorderLayout());
		this.type= type;
		
		this.loading= loading;
		/*if (this.loading== null){
			JLabel noData= new JLabel("No Data");
			noData.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			panel= new JPanel();
			panel.setName(name);
			panel.setLayout(new GridLayout(1,1));
			panel.add(noData);
			this.add(panel, BorderLayout.CENTER);
		}
		else {*/
			dataset = new XYSeriesCollection();
			this.ds=  createDataset();
			this.chart= ChartFactory.createXYLineChart(null, "Time t [min]", unit, this.ds, PlotOrientation.VERTICAL, true, true, true);
			this.chartPanel= new ChartPanel(chart);
			this.chartPanel.setName(name);
			customizePlot();
			/*this.renderer = chart.getXYPlot().getRenderer();
			this.renderer.setSeriesPaint(1, Color.blue);*/
			this.add(chartPanel, BorderLayout.CENTER);
		//}
		
		border.setTitleJustification(TitledBorder.CENTER);
	    border.setTitlePosition(TitledBorder.TOP);
	    border.setBorder(BorderFactory.createEtchedBorder());
	    this.setBorder(border);
	}
	
	private void customizePlot(){
		this.renderer = chart.getXYPlot().getRenderer();
	    //renderer.setSeriesLinesVisible(2, false);
	    //renderer.setSeriesShapesVisible(1, false);
	    renderer.setSeriesPaint(0, Color.blue);
	    //renderer.setSeriesPaint(1, Color.red);
	    //renderer.setSeriesPaint(2, Color.blue);

	    XYPlot plot = chart.getXYPlot();
	    plot.setOutlinePaint(Color.BLUE);
	    plot.setRenderer(renderer);
	}
	
	private XYDataset createDataset(){	
	    if (this.loading!= null){
	    
	    	int[] x_values_help= this.loading.getTimeSecondsArray();
	    	float[] x_values= new float[x_values_help.length];
	    	for (int i= 0; i< x_values_help.length; i++){
    			x_values[i]= (float)x_values_help[i]/60;
    		}
	    	int[] y_values;

    		Float y_val;
		
	    	switch (this.type) {
	    	case "Energy":
	    		border= new TitledBorder("Energy");
	    		series = new XYSeries("Charged Energy: Charge no. " + loading.getSessionID());
	    		y_values= this.loading.getEnergyArray();
	    		for (int i= 0; i< x_values.length; i++){
	    			y_val= ((float) y_values[i])/10000;
	    			series.add(x_values[i], y_val);
	    		}
	    		break;
			
	    	case "Voltage":
	    		border= new TitledBorder("Voltage");
	    		series = new XYSeries("Voltage (Phase 1): Charge no. " + loading.getSessionID());
	    		y_values= this.loading.getVoltageArray();
	    		for (int i= 0; i< x_values.length; i++){
	    			series.add(x_values[i], y_values[i]);
	    		}
	    		break;
	    		
	    	case "Current":
	    		border= new TitledBorder("Current");
	    		series = new XYSeries("Current (Phase 1): Charge no. " + loading.getSessionID());
	    		y_values= this.loading.getCurrentArray();
	    		for (int i= 0; i< x_values.length; i++){
	    			y_val= ((float) y_values[i])/1000;
	    			series.add(x_values[i], y_val);
	    		}
	    		break;

	    	default:
	    		series = new XYSeries("No DATA");
	    		break;
	    	}
	    }
	    else {
	    	series = new XYSeries("No DATA");
	    }
	    	
	    dataset.addSeries(series);	
		return dataset;
	}
	
	public void newGraph(LoadingObject loading){
		this.loading= loading;
		dataset.removeAllSeries();
		createDataset();
	}
	
	public void updateGraph(final LoadingObject loading){
		SwingUtilities.invokeLater(new Runnable() {
	          public void run() {
	            updateGraphInternal(loading);
	          }
	        });
	}
	
	public JPanel getChart(){
		if (chartPanel== null) return this.panel;
		else return this.chartPanel;
	}
	
	private void updateGraphInternal(LoadingObject loading){
		//LoadingObject updateLoad= loading;
		
		if (loading!= null && 
				this.loading.getTimeSecondsArray().length <= loading.getTimeSecondsArray().length) {
			
			int[] x_values_old= this.loading.getTimeSecondsArray();
			////System.out.println("x_values_old.length= " + x_values_old.length);
			int[] x_values_new= loading.getTimeSecondsArray();
			////System.out.println("x_values_new.length= " + x_values_new.length);
		    
			float[] x_values= new float[x_values_new.length-x_values_old.length];
	    		 
	    	int[] y_values;
	    	
	    	for (int i= 0; i< x_values.length; i++){
	    		x_values[i]= (float)x_values_new[i+x_values_old.length]/60;
	    	}

    		Float y_val;
		
	    	switch (type) {
	    	case "Energy":
	    		////System.out.println("UPDATE Energy table add size= 	 " + x_values.length);
	    		y_values= loading.getEnergyArray();
	    		////System.out.println("UPDATE Energy table add yvalues size= 	 " + y_values.length);
	    		for (int i= 0; i< x_values.length; i++){
	    			y_val= ((float) y_values[i+x_values_old.length])/10000;
	    			series.add(x_values[i], y_val);
	    		}
	    		break;
			
	    	case "Voltage":
	    		////System.out.println("UPDATE Voltage table add size= 	 " + x_values.length);
	    		y_values= loading.getVoltageArray();
	    		////System.out.println("UPDATE Voltage table add yvalues size= 	 " + y_values.length);
	    		for (int i= 0; i< x_values.length; i++){
	    			y_val= (float) y_values[i+x_values_old.length];
	    			series.add(x_values[i], y_val);
	    		}
	    		break;
	    		
	    	case "Current":
	    		////System.out.println("UPDATE Current table add size= 	 " + x_values.length);
	    		y_values= loading.getCurrentArray();
	    		//System.out.println("UPDATE Current table add yvalues size= 	 " + y_values.length);
	    		for (int i= 0; i< x_values.length; i++){
	    			y_val= ((float) y_values[i+x_values_old.length])/1000;
	    			series.add(x_values[i], y_val);
	    		}
	    		break;

	    	default:
	    		//series = new XYSeries("No DATA");
	    		break;
	    	}
	    }
		else if (loading!= null && 
				this.loading.getTimeSecondsArray().length > loading.getTimeSecondsArray().length){
			// Do nothing. Should be a new Table!
		}
		
	}
}
