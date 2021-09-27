package Modul4;

/**
* The class Modifier
* @version 1.0
* @author Ekaterina Korotetskaya
*/
public class Modifier extends Thread{

    private BoundedBuffer buffer;
    private int arrayLength;

    
    /**
	* Constructor
	* @param buffer: buffer for strings 
	* @param arrayLength: number of strings in the buffer
	*/
    public Modifier(BoundedBuffer buffer, int arrayLength){
        this.buffer = buffer;
        this.arrayLength = arrayLength;
    }

    public void run() {
    	modify();
    }

    /**
     * Modify data in the buffer. Performed for each word in the source text.
     */
    public void modify(){
        for(int i = 0; i < arrayLength; i++) {
            buffer.modifyData();
        }
    }
}

