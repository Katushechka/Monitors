# Monitors
The main goals of this project are to learn and implement the follow concepts of multithreading:

-Monitors as synchronization mechanisms.

-Bounded buffer as shared resource for communication between multiple threads.

-Producer/Consumer and Writer/Reader patterns





**Description:**

It is an application that reads the contents of a file from a source into the memory, let the user manipulate the text (search
and replace) and copy to a destination file. The acts of reading, modifying and writing to file must take place by threads at the same
time. Although it is possible to implement the Producer/Consumer model for the solution, a Writer/Reader scenario is recommended.





**User manual:**

Put the text file "words-13-data.txt" in the folder "files".

For reading from the text file press the button "Read".

In the window "Find " write a word that should be replaced. In the window "Replace" write a new word.

Press the button "Create the destination file" - replace operation will be performed. 
