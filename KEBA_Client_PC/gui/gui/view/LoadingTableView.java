package gui.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dbObjects.LoadingData;

public class LoadingTableView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private DefaultTableModel tableModel;
    private JTable table;
    
    public LoadingTableView(){
    	initForm();
    }
    
    private void initForm(){
    	String[] columns = new String[] {
                "Load ID", "Session ID", "Start", "End", "Time [s]",
                "Energy", "Usable", "Complete", "Load file", "State file"
            };
            
            tableModel = new DefaultTableModel(columns, 0);
            
            table = new JTable(tableModel);
            
            JScrollPane scrollFrame = new JScrollPane(table);
            table.setAutoscrolls(true);
            //scrollFrame.setPreferredSize(new Dimension( 800,300));
            this.setLayout(new GridLayout(0,1));
            this.add(scrollFrame);
    }
    
    public void add(ArrayList<LoadingData> loadings){
    	
    	for (int i = 0; i < loadings.size(); i++){
    		   int loadiID = loadings.get(i).getLoadID();
    		   int sessionID = loadings.get(i).getSessionID();
    		   String start = loadings.get(i).getStartDateTime().toString();
    		   String end = "";
    		   if (loadings.get(i).getEndDateTime()!= null){
    			   end = loadings.get(i).getEndDateTime().toString();
    		   }
    		   int time = loadings.get(i).getLoadTime();
    		   int energy = loadings.get(i).getLoadedEnergy();
    		   String usable;
    		   if (loadings.get(i).isUsable()){
    			   usable= "Yes";
    		   }
    		   else {
    			   usable= "NO";
    		   }
    		   String complete;
    		   if (loadings.get(i).isComplete()){
    			   complete= "Yes";
    		   }
    		   else {
    			   complete= "NO";
    		   }
    		   
    		   String loadFilePath;
    		   if (loadings.get(i).isLoadFileAvailable()){
    			   loadFilePath= loadings.get(i).getLoadFilePath();
    		   }
    		   else {
    			   loadFilePath= "N/A";
    		   }
    		   
    		   String stateFilePath;
    		   if (loadings.get(i).isStateFileAvailable()){
    			   stateFilePath= loadings.get(i).getStateFilePath();
    		   }
    		   else {
    			   stateFilePath= "N/A";
    		   }
    		   
    		   Object[] data = {loadiID, sessionID, start, end, time, energy, 
    		                               usable, complete, loadFilePath, stateFilePath};

    		   tableModel.addRow(data);

    		}
    }
    
    public void add(LoadingData loading){
    	
    	int loadiID = loading.getLoadID();
    	int sessionID = loading.getSessionID();
    	String start = loading.getStartDateTime().toString();
    	String end = loading.getEndDateTime().toString();
    	int time = loading.getLoadTime();
    	int energy = loading.getLoadedEnergy();
    	String usable;
    	if (loading.isUsable()){
    		usable= "Yes";
    	
    	}
    	else {
    		usable= "NO";
    	}
    	String complete;
    	if (loading.isComplete()){
    		complete= "Yes";
    	}
    	else {
    		complete= "NO";
    	}
    	
    	String loadFilePath;
    	if (loading.isLoadFileAvailable()){
    		loadFilePath= loading.getLoadFilePath();
    	}
    	else {
    		loadFilePath= "N/A";
    	}
    	
    	String stateFilePath;
    	if (loading.isStateFileAvailable()){
    		stateFilePath= loading.getStateFilePath();
    	}
    	else {
    		stateFilePath= "N/A";
    	}
    	
    	Object[] data = {loadiID, sessionID, start, end, time, energy, 
    			usable, complete, loadFilePath, stateFilePath};
    	
    	tableModel.addRow(data);
    	
    }
    
    
}
