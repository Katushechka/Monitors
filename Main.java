package Modul4;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main (String [] args){
        MonitorGUI gui = new MonitorGUI();
        gui.Start();

        try {
            Scanner sc = new Scanner(System.in);
            String str =sc.nextLine();
        }
        catch (Exception e) {
        }
    }

}

