package edu.berkeley.icsi.netalyzr.tests.connectivity;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BandwidthTestTCP extends Thread {
    private String server;
    private int PORT;
    private int N_STREAMS;
    private String test;
    private int PACKET_SIZE;
    private int MAXDUR;
    private int reportGran;
    private CountDownLatch bwlatch;

    private int NUMARR = 10;
    private int TIMEOUT = 10000;
    private String TAG = "NETALYZR_BWTCP";
    private final boolean debug = true;
    double[] aggrResults; 
    double [] aggrBySecResults; 
    double [][] bySecResults; 
    
    public BandwidthTestTCP (String server, 
                            String test,
                            int nStreams,
                            int sPort,
                            int pSize,
                            int maxDur,
                            int reportGran,
                            CountDownLatch bwlatch){

        if (debug) Log.i(TAG,"\n\nstarting tcp bandwidth test\n\n");
        this.server = server;
        this.test = test;
        this.N_STREAMS = nStreams;
        this.PORT = sPort;
        this.MAXDUR = maxDur;
        this.PACKET_SIZE= pSize;
        this.reportGran = reportGran;
        this.bwlatch = bwlatch;
        this.aggrResults = new double[N_STREAMS];
        this.aggrBySecResults = new double[MAXDUR/reportGran];
        this.bySecResults = new double[N_STREAMS][MAXDUR/reportGran];
        if (debug) Log.i(TAG,"init done");
    }
    
    public void run(){
        if (debug) Log.i(TAG,"\n\nin tcp bandwidth test thread\n\n");
        Socket[] socket = new Socket[N_STREAMS];
        CountDownLatch latch = new CountDownLatch(N_STREAMS);

        Arrays.fill(aggrResults, -1.0);
        
        try{
            for (int i=0; i<N_STREAMS; i++){
                socket[i] = new Socket(this.server,PORT);
                new Client(socket[i],i,latch,test,MAXDUR,PACKET_SIZE).start();
            }
        } catch(IOException e){
            if (debug) Log.i(TAG,"Could not connect to server");
            //return Test.TEST_ERROR;
        }
        
        try {
            latch.await(MAXDUR+5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.i(TAG,"exception "+e.getMessage(), e);
            //return Test.TEST_ERROR;
        }
        if (debug) Log.i(TAG,"Done threads.");
        

        bwlatch.countDown(); 
        //return Test.TEST_SUCCESS;
    }
    
    private class Client extends Thread{
        private Socket socket;
        private int clientid;
        private int MAXDUR;
        private int PACKET_SIZE;
        private CountDownLatch latch;
        private String test;

        public Client(Socket socket, 
                        int clientid, 
                        CountDownLatch latch, 
                        String test, 
                        int MAXDUR, 
                        int PACKET_SIZE){
            this.socket = socket;
            this.clientid = clientid;
            this.latch = latch;
            this.test = test;
            this.MAXDUR = MAXDUR;
            this.PACKET_SIZE = PACKET_SIZE;
        }
        
        public void run(){
            try {
                socket.setSoTimeout(TIMEOUT);
            } catch (SocketException e1) {
                aggrResults[clientid] = -1.0;
                latch.countDown();
                return;
            }
            BufferedReader In = null;
            DataOutputStream out = null;
            DataInputStream in = null;
            if (debug) Log.i(TAG, "In connection " + clientid);

            try {
                In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                String testParam = "test:" + test + " duration:" + MAXDUR  + " pktsize:" + PACKET_SIZE + " reportgran:" + reportGran + "\n";
                out.writeBytes(testParam);
                //testParamServer.split(" ");
            } catch (IOException e){
                aggrResults[clientid] = -1.0;
                latch.countDown();
                return;
                }
            
            if (debug) Log.i(TAG, "client in " + test + " mode");         
            if (test.equals("UPLINK")){
                sendData(In,out,clientid);
                if (debug) Log.i(TAG, test);
            }
            else if(test.equals("DOWNLINK")){
                recvData(in,out,clientid);
            }
            if (debug) Log.i(TAG, "Finished " + test + " " + clientid);         

            try{
                socket.close();
            } catch (IOException e){
                if (debug) Log.i(TAG, "Couldn't close socket");
            }
            latch.countDown();
        }
    }

    private void sendData( BufferedReader summaryIn, DataOutputStream out,int clientid){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e2) {
            aggrResults[clientid] = -1.0;
            //if (debug) Log.i(TAG,"sleep " + clientid, e2);
            return;
        }
        
        //char fillerStr = '1';
        byte[][] messageArr = new byte[NUMARR][PACKET_SIZE];
        for (int i=0;i<NUMARR;i++){
            new Random().nextBytes(messageArr[i]);
            for (int j=0;j<PACKET_SIZE;j++){
                if (messageArr[i][j] == '+' || messageArr[i][j] == '~'){
                    messageArr[i][j] = '0';
                }
            }
        }
        //new Random().nextBytes(messageArr);        
        long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        long totBytes = 0;
        //Log.i(TAG,"client " + clientid + " Starting send");
        try {
            out.writeBytes("~");
            out.flush();
        } catch (IOException e1){
            aggrResults[clientid] = -1.0;
            return;
            }
        //Log.i(TAG,"client " + clientid + " finished init");
        int cntMsg = 0;
        while (true){
            try {
                //Log.i(TAG, "client " + clientid + " writing");
                //Thread.sleep(10);
                out.write(messageArr[cntMsg%NUMARR]);
                totBytes += messageArr[0].length;
                cntMsg += 1;
            } catch (SocketException e){
                aggrResults[clientid] = -1.0;
                if (debug) Log.i(TAG,"write data SE " + clientid, e);
                return;
            }  catch (IOException e) {
                aggrResults[clientid] = -1.0;
                if (debug) Log.i(TAG,"write data IOE" + clientid, e);
                return;
                } catch (Exception e){
                    aggrResults[clientid] = -1.0;
                    if (debug) Log.i(TAG,"general xception " + clientid, e);
                    return;
                    
                }
            //Log.i(TAG, "client " + clientid + " writing post");
            currentTime = System.currentTimeMillis();
            if (currentTime - startTime > MAXDUR){
                break;
            }
        }
        //Log.i(TAG,"client " + clientid + " finished write");
        try {
            out.flush();
            out.writeBytes("+");
            out.flush();

        } catch (IOException e) {
            this.aggrResults[clientid] = -1.0;
            return;
            }
        if (debug) Log.i(TAG, "Sent " + totBytes + " bytes");
        String summary = "";
        try{
            summary = summaryIn.readLine();
            if (debug) Log.i(TAG, summary);
        }
        catch(SocketTimeoutException e){
            if (debug) Log.i(TAG, "Couldn't read summary: STE");
            aggrResults[clientid] = -1.0;
            return;
        } catch (IOException e) {
            if (debug) Log.i(TAG, "Couldn't read summary: IOE");
            aggrResults[clientid] = -1.0;
            return;
        }
    }
    
    public void recvData(DataInputStream in, DataOutputStream out, int clientid){
        int bytesRead = 0;
        long startTime = -1;
        //long slowstartTime = -1;
        //long currentTime = (new Date()).getTime();
        long endTime = -1;
        byte[] messageArr = new byte[PACKET_SIZE];
        long totalBytes = 0;
        //long aggrBytes = 0;
        boolean start = false;
        boolean end = false;
        int currSec = 0;
        long prevTotal = 0;
        long prevCheck = 0;
        double aggrTput = -1.0;
        
        try{
            while (!end){
                
                bytesRead = in.read(messageArr);
                if(bytesRead > 0){
                    if (!start && messageArr[0] == '~'){
                        startTime = System.currentTimeMillis();
                        start = true;
                        prevCheck = startTime;
                    }
                    if (messageArr[bytesRead-1] == '+'){
                        endTime = System.currentTimeMillis();;
                        end = true;
                    }
                }
                if (bytesRead == -1){
                    if (System.currentTimeMillis() - startTime > TIMEOUT){
                        Log.i(TAG,"End of Stream");
                        break;
                    }
                }
                if (start){
                    totalBytes += bytesRead;
                    long ct = System.currentTimeMillis();
                    //Log.i(TAG,"" + (ct - startTime)+ " " +(ct - startTime)/reportGran+" " +((ct - startTime)/reportGran)*reportGran+" "+currSec);
                    if ((((ct - startTime)/reportGran)*reportGran) > currSec){
                        if  (currSec < MAXDUR){
                            bySecResults[clientid][currSec/reportGran] = (totalBytes - prevTotal)*8.0/(ct - prevCheck);
                            //if (debug) Log.i(TAG,"bysec " + clientid + " " + currSec/reportGran + " " + bySecResults[clientid][currSec/reportGran]);
                            prevTotal = totalBytes;
                            prevCheck = ct;
                            currSec += reportGran;
                        }
                    }
                }
            }
            if (debug) Log.i(TAG, "End: Total received " + totalBytes + " in " + (endTime - startTime) + " msec");
        } catch(SocketTimeoutException e){
            if (debug) Log.i(TAG, "Couldn't read data");
            aggrResults[clientid] = -1.0;
            return;
        } catch (IOException e){
            if (debug) Log.i(TAG, "Couldn't read data");
            aggrResults[clientid] = -1.0;
            return;
        } finally{
            if (start && end){
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2); 
                    df.setGroupingUsed(false);
                    aggrTput = (double)(totalBytes)*8.0/(endTime - startTime);
                    String summary = "" + totalBytes + " " + (endTime - startTime)/1000.0 + " " + df.format(aggrTput) + " ";
                    for (int i=0;i<bySecResults[clientid].length;i++){
                        //if (debug) Log.i(TAG, "Bysec " + i + " " + bySecResults[clientid][i]);
                        summary = summary + i + ":" + df.format(bySecResults[clientid][i])  + ";";
                    }
                    try {
                        out.writeBytes(summary);
                    } catch (IOException e) {
                        if (debug) Log.i(TAG, "Couldn't send summary to server.");
                    }
                    aggrResults[clientid] = aggrTput;
                    if (debug) Log.i(TAG,summary);
            }
            else{
                if (debug) Log.i(TAG, "Invalid Test");
                aggrResults[clientid] = -1;
            }
        }
        return;
    }
}
