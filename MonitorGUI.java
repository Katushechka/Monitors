package Modul4;

import javax.swing.*;

import Modul3.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MonitorGUI extends Thread implements ActionListener 
{
       
        private JFrame frame;				// The Main window
        private JMenu fileMenu;				// The menu
        private JMenuItem openItem;			// File - open
        private JMenuItem saveItem;			// File - save as
        private JMenuItem exitItem;			// File - exit
        private JTextField txtFind;			// Input string to find
        private JTextField txtReplace; 		// Input string to replace
        private JCheckBox chkNotify;		// User notification choise
        private JLabel lblInfo;				// Hidden after file selected
        private JButton btnCreate;			// Start copying
        private JButton btnClear;			// Removes dest. file and removes marks
        private JButton btnReadFromFile;
        private JLabel lblChanges;			// Label telling number of replacements
        private ArrayList<String> words;
        private JScrollPane scrollSource;
        private JScrollPane scrollDest;
        private JTextArea textSource;
        private JTextArea textDestination;
        
        private Writer writer;
        private BoundedBuffer boundedBuffer;
        private String stringToFind;
        private String stringToReplace;
        private Thread threadWriter;
        private Thread threadReader;
        private Thread threadModifier;

        /**
         * Constructor
         */
        public MonitorGUI()
        {
        }

        /**
         * Starts the application
         */
        public void Start()
        {
            frame = new JFrame();
            frame.setBounds(0, 0, 714,600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);
            frame.setTitle("Text File Copier - with Find and Replace");
            InitializeGUI();					// Fill in components
            frame.setVisible(true);
            frame.setResizable(false);			// Prevent user from change size
            frame.setLocationRelativeTo(null);	// Start middle screen
        }

        /**
         * Sets up the GUI with components
         */
        private void InitializeGUI()
        {
            fileMenu = new JMenu("File");
            openItem = new JMenuItem("Open Source File");
            openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
            saveItem = new JMenuItem("Save Destination File As");
            saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
            saveItem.setEnabled(false);
            exitItem = new JMenuItem("Exit");
            exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.addSeparator();
            fileMenu.add(exitItem);
            JMenuBar  bar = new JMenuBar();
            frame.setJMenuBar(bar);
            bar.add(fileMenu);

            JPanel pnlFind = new JPanel();
            pnlFind.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Find and Replace"));
            pnlFind.setBounds(12, 32, 436, 122);
            pnlFind.setLayout(null);
            frame.add(pnlFind);
            JLabel lab1 = new JLabel("Find:");
            lab1.setBounds(7, 30, 80, 13);
            pnlFind.add(lab1);
            JLabel lab2 = new JLabel("Replace with:");
            lab2.setBounds(7, 63, 80, 13);
            pnlFind.add(lab2);

            txtFind = new JTextField();
            txtFind.setBounds(88, 23, 327, 20);
            pnlFind.add(txtFind);
            txtReplace = new JTextField();
            txtReplace.setBounds(88, 60, 327, 20);
            pnlFind.add(txtReplace);
            chkNotify = new JCheckBox("Notify user on every match");
            chkNotify.setBounds(88, 87, 180, 17);
            pnlFind.add(chkNotify);

            lblInfo = new JLabel("Select Source File..");
            lblInfo.setBounds(485, 42, 120, 13);
            frame.add(lblInfo);

            btnReadFromFile = new JButton("Read");
            btnReadFromFile.setBounds(465, 87, 230, 23);  //(x,y,bredd,höjd)
            frame.add(btnReadFromFile);
            btnCreate = new JButton("Create the destination file");
            btnCreate.setBounds(465, 119, 230, 23);
            frame.add(btnCreate);
            btnClear = new JButton("Clear Dest. file and remove marks");
            btnClear.setBounds(465, 151, 230, 23);
            frame.add(btnClear);
            

            lblChanges = new JLabel("No. of Replacements:");
            lblChanges.setBounds(279, 161, 200, 13);
            frame.add(lblChanges);

            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            tabbedPane.setBounds(12, 170, 653, 359);
            frame.add(tabbedPane);
            
            textSource = new JTextArea();
            scrollSource = new JScrollPane(textSource); 
            tabbedPane.addTab("Source", null, scrollSource, null);
            
            textDestination = new JTextArea();   
            scrollDest = new JScrollPane(textDestination);
            tabbedPane.addTab("Destination", null, scrollDest, null);
            
            btnReadFromFile.addActionListener(this);
            btnCreate.addActionListener(this);
            btnClear.addActionListener(this);
        }

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnReadFromFile) {
				words = new ArrayList<String>();
				BufferedReader r;
				try {
					r = new BufferedReader(new InputStreamReader(new FileInputStream("files/words-13-data.txt")));
					while (true) {
					    String word;
						try {
							word = r.readLine();							 
				 			    if (word == null) { 
							    	break; 
							    }
				 			   
				 			   words.add(word);
				 			   textSource.append(word + "\n");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 			   
					}
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}					
			}
			
			
			if(e.getSource() == btnCreate) {
				stringToFind = txtFind.getText();
				stringToReplace = txtReplace.getText();
				
					boundedBuffer = new BoundedBuffer(10,  stringToFind, stringToReplace);
					threadWriter = new Writer (boundedBuffer, words);
					threadWriter.start();
					threadModifier = new Modifier (boundedBuffer, words.size());
					threadModifier.start();
					threadReader = new Reader (this, boundedBuffer, words.size());
					threadReader.start();
				
			}
			
			if(e.getSource() == btnClear) {
				textSource.setText(null);
				textDestination.setText(null);
				threadWriter.interrupt();
				threadModifier.interrupt();
				threadReader.interrupt();
				lblChanges.setText("No. of Replacements: ");
			
			}
			
		}
		
		public void setTextInDestination (String text) {
			textDestination.append(text + "\n");
		}
		
		public void setNumberOfReplacements(int numOfReplacements) {
			lblChanges.setText("No. of Replacements: " + numOfReplacements);
		}

    }

