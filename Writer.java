package Modul4;

import java.awt.List;

/**
* The class Writer represents a writer that writes in the buffer.
* @version 1.0
* @author Ekaterina Korotetskaya
*/
import java.util.ArrayList;

public class Writer extends Thread{
	private BoundedBuffer buffer;
	private ArrayList<String> textToWrite;
	private boolean runWriter = true;
	

	/**
	* Constructor
	* @param buffer: buffer with strings
	* @param textIn: a list with words to write in the buffer
	*/

	public Writer (BoundedBuffer buffer, ArrayList<String> textIn) {
		this.buffer=buffer;
		this.textToWrite=textIn;
	}
	
	
	/**
	* run method
	* The method loops through the strings and calls the write method of the bounded buffer
	*/

	public void run() {
		for (int i = 0; i < textToWrite.size(); i++) {
			buffer.writeData(textToWrite.get(i));
		}
	}
}

