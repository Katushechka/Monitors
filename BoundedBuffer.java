package Modul4;

import java.util.ArrayList;

import javax.net.ssl.SSLEngineResult.Status;
/**
* The class BoundedBuffer represents a buffer.
* @version 1.0
* @author Ekaterina Korotetskaya
*/


public class BoundedBuffer {
	
	private String [] stringArray; //The actual string buffer array
//	private BufferStatus[] status; //An array of BuffreStatus objects, one for each element in buffer
	private int max;				//Elements in buffer
	private int writePos = 0;			//The position pointers for each thread
	private int readPos = 0;			
	private int findPos = 0;;
	private String findString;		//The string to search for
	private String replaceString;	//The replace string
	private int start;				//The start position in textbox for marking
	private int nbrReplacement; 	//Replacement counter
	private Object lockObject;		//For mutual exclusion
	private String wordToWrite;
	private String [] buffer = new String [10];
	private State [] wordsState = new State[10];
	private String currentState;
	private Boolean done = false;
	private volatile int count = 0;
	private String printUI;
	private int numberOfReplacements = 0;
	
	
	public enum State {
		EMPTY("EMPTY"), NEW("NEW"), CHECKED("CHECKED");
		
		public final String state;
		 
		State (String state) {
			this.state =state;
		} 
		
		public String getState() { 
			return state;
		}
	}
	
	/**
	* Constructor
	* @param elements: size of the buffer
	* @param find: String to be replaced
	* @param replace: String to replace
	*/
	public  BoundedBuffer (int elements, String find, String replace) {
		this.max=elements;
		this.findString=find;
		this.replaceString=replace;		
		
		/**
		* Set status EMPTY for all positions in the buffer 
		*/
		for ( int i = 0; i < buffer.length; i++) {
			wordsState[i]=State.EMPTY;
		}
	}
	
	
	/**
	* Writes to a location which is marked with the status value Empty
	* @param wordToWrite: String to write in the buffer
	*/
	public synchronized void writeData(String wordToWrite) {
		
		while(wordsState[writePos] != State.EMPTY) { 
			try { wait(); 
			} catch (Exception e) { 
				e.printStackTrace(); } 
		}
				buffer[writePos] = wordToWrite;
				System.out.println("WRITER: " + wordToWrite + " " + writePos );
				wordsState[writePos] = State.NEW;
				writePos = (writePos + 1) % buffer.length;
			
			notify();		
	}
	
	
	/**
	* The method "modifyData"
	* checks the written string, if the string equals with "find" makes the modification at that position
	* and change the status of this position to Checked
	*/	
	public synchronized void modifyData() {
		while(wordsState[findPos] != State.NEW) {
			try { wait(); 
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
		}
		
		if (buffer[findPos].toString().equals(findString)) {
			buffer[findPos] = replaceString;
			System.out.println("MODIFIER: " + findString + " " + replaceString + " " +findPos );
			wordsState[findPos] = State.CHECKED;
			findPos = (findPos + 1) % buffer.length; 
			numberOfReplacements ++;
			notify();
		} else {
			wordsState[findPos] = State.CHECKED;
			findPos = (findPos + 1) % buffer.length; 
			notify();
		}		
	}
	
	
	/**
	* The method "readData"
	* The reader reads a position having the status Checked and change the status of this position to EMPTY
	* @return printUI: String to set in UI
	*/	
	public synchronized String readData() {
		
		while (wordsState[readPos] != State.CHECKED) {
			try { wait(); 
			} catch (Exception e) { 
				e.printStackTrace(); } 
		}
		printUI = buffer[readPos].toString();
		System.out.println("READER: " + printUI + " " + readPos );
		wordsState[readPos] = State.EMPTY;
		readPos = (readPos + 1) % buffer.length; 
		notify();

		return printUI;  				
	}
	
	
	/**
	* The method "numberOfReplacements"
	* @return numberOfReplacements: returns number of strings that were replaced
	*/	
	public int numberOfReplacements() {
		return numberOfReplacements;
	}
}

