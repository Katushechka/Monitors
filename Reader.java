package Modul4;

import java.util.ArrayList;

/**
* The class Reader represents a reader that reads from the buffer.
* @version 1.0
* @author Ekaterina Korotetskaya
*/
public class Reader extends Thread {
	private MonitorGUI ui;
	private BoundedBuffer buffer;
	private int nrOfStrings;
	private ArrayList<String> text;
	private String wordToSetInUI;
	private int numOfReplacements;
	
	
	/**
	* Constructor
	* @param ui: object of the class MonitorGUI 
	* @param buffer: buffer with strings
	* @param nrOfStrings: number of strings to read from the buffer
	*/
	public Reader (MonitorGUI ui, BoundedBuffer buffer, int nrOfStrings) {
		this.ui=ui;
		this.buffer=buffer;
		this.nrOfStrings=nrOfStrings;
	}
	
	
	/**
	* run method
	* calls the read method of the bounded buffer and sets saved strings in the UI
	*/
	public void run() {
		for ( int i = 0; i < nrOfStrings; i ++) {
			 wordToSetInUI = buffer.readData();
			 if (wordToSetInUI != null) {
				 ui.setTextInDestination(wordToSetInUI);
				 numOfReplacements = buffer.numberOfReplacements();
				 ui.setNumberOfReplacements(numOfReplacements);
			 }			 
		}
	}
}

