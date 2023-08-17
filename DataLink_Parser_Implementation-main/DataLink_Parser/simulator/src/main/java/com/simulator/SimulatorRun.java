package com.simulator;

import java.io.IOException;
import java.util.List;

import com.simulator.exceptions.EmptyFileException;
import com.simulator.exceptions.NotSixtyFourBitDataException;
import com.simulator.exceptions.NullLineException;

public class SimulatorRun {
    public static int firstDelay=0;
    public static void main(String[] args) throws IOException, NullPointerException{
        
        /*
         * Read file names from the console
         */
        Client client = new Client();

        String fileName = "DataLink_Parser\\simulator\\src\\main\\java\\com\\simulator\\binary_data\\data_1.txt";
        
        
        try {
            
            FileReader file = new FileReader(fileName);
            List<String> ls = file.readData();

            for (int i = 0; i < ls.size(); i++) {
                client.startConnection("127.0.0.1", 5000);

                String msg = ls.get(i);
                int delayInSeconds = 5;
                if(getDelay()>0){
                    try {
                        Thread.sleep(delayInSeconds * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 
                }
                setDelay();
                
           
                client.sendMessage(msg);
                
                client.stopConnection();
            }

        }
        catch (NullLineException e){
            System.out.println("\nERROR: Empty line has been detected in the related file!\n");
        }
        catch (EmptyFileException e) {
            System.out.println("\nERROR: File is empty! Fill the file and try it again!\n");
        } 
        catch (NotSixtyFourBitDataException e) {
            System.out.println("\nERROR: File read contains data not 64-bit!\n");
        }
        catch (NullPointerException e) {
            // Not found file is trying to read!
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getDelay(){
        return firstDelay;
    }
    public static void setDelay(){
        firstDelay++;
    }
}
