package edu.berkeley.icsi.netalyzr.tests;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;




/**
 * This class implements stats-keeping and UDP I/O management
 * for the latency and buffer measurement tests.
 * 
 * Each object has a start time C_t_0, the time at which the
 * object was created. During the test, the current time
 * C_t_cur refers to the time at which the next UDP message is
 * assembled. Both are in millisecond granularity. C_t_cur -
 * C_t_0 serves as a strictly increasing packet identifier.
 * 
 * The object maintains a counter of the number of datagrams
 * sent in the test, C_num_tx, and is configured with the
 * desired size C_rcv_len, in bytes, of datagrams sent in
 * response to the datagram sent by us. Transmitted datagrams
 * are length-padded (with a sequence of dots) to desired
 * length. Various additional arguments configure the sending
 * rate, number of datagrams, etc.
 * 
 * The UDP datagram content looks as follows (gaps indicate a
 * single space character):
 * 
 * <C_t_cur - C_t_0> <C_num_tx> <C_rcv_len> <padding>
 * 
 * Datagrams are sent from an ephemeral port, to the buffer
 * server's well-known port. The I/O routine processes up to a
 * single response packet after every transmitted one, but
 * times out immediately if no packets have arrived.
 * 
 * The server's protocol operates as follows. For each client
 * session (identified by source address & port), the time of
 * last received packet as well as the number of packets
 * S_num_rx received at that time is stored. Similarly to the
 * client, the server maintains times S_t_0 and S_t_cur. For
 * every received packet, the server immediately sends a
 * response of the form
 * 
 * <C_t_cur - C_t_0> <S_t_cur - S_t_0> <C_num_tx> <C_rcv_len> <S_num_rx>
 *  <padding>
 *  
 *   Problems:
 *    - server does not expire state
 *    - server-side duplicate tracking will break after 30 seconds.
 *    - If there's an acceleration proxy with compression capabilities,
 *    the results may be biased
 *    
 * @author Netalyzr
 *
 */
public class NetProbeStats {

    private static final String TAG = "NETALYZR";
    

    public float avgRTT;
    public float sustainedPPS;
    public float sustainedRTT;

    public int sendPacketSize;
    public int recvPacketSize;

    public long sendCount;
    public long recvCount;
    public long serverRecvCount;

    public long reorderCount;
    private int reorderIndex;

    public long lossBurstCount;
    public long lossBurstLength;

    public long dupCount;
    
    private boolean [] dupData;

    private int dupRange;

    public int status;

    private String server;
    private InetAddress serverIP;
    private int port;
    private int sendRate;
    private int sendTime;
    private int sendSize;
    private int recvSize;
    private String sendSlug;

    private boolean isPing;
    private int maxSend;

    private boolean stopAtPing;

    // Alternate constructor for just pinging
    // an X number of packets.
    public NetProbeStats(String serverIn,
                 int portIn,
                 int sendRateIn, // in ms between packets
                 int maxSendIn){
        isPing = true;
        server = serverIn;

        dupRange = 30000;
        dupData = new boolean[dupRange];
        dupCount = 0;
        reorderCount = 0;
        reorderIndex = -1;
        lossBurstCount = 0;
        lossBurstLength = 0;

        try {
        serverIP = InetAddress.getByName(server);
        } catch (UnknownHostException e){
        //status = Test.TEST_ERROR;
        Log.d(TAG, "Failed to initialize properly");
        return;
        }
        maxSend = maxSendIn;
        port = portIn;
        sendRate = sendRateIn;
        sendTime = 0;
        sendSize = 0;
        recvSize = 0;
        sendSlug = "";
        serverRecvCount = 0;
        for(int i = 0; i < sendSize; ++i){
        sendSlug += ".";
        }
        stopAtPing = false;
    }


    // Alternate constructor for just pinging
    // until the ping test starts
    public NetProbeStats(String serverIn,
                 int portIn,
                 int sendRateIn){ // in ms between packets
        isPing = true;
        server = serverIn;
        stopAtPing = true;

        lossBurstCount = 0;
        lossBurstLength = 0;

        dupRange = 30000;
        dupData = new boolean[dupRange];
        dupCount = 0;
        reorderCount = 0;
        reorderIndex = -1;


        try {
        serverIP = InetAddress.getByName(server);
        } catch (UnknownHostException e){
        //status = Test.TEST_ERROR;
        Log.d(TAG, "Failed to initialize properly");
        return;
        }
        /* Just Send a "Metric S-load", so set max very high */
        maxSend = 10000;
        port = portIn;
        sendRate = sendRateIn;
        sendTime = 0;
        sendSize = 0;
        recvSize = 0;
        sendSlug = "";
        serverRecvCount = 0;
        for(int i = 0; i < sendSize; ++i){
        sendSlug += ".";
        }
    }



    public NetProbeStats(String serverIn, 
                 int portIn,
                 int sendRateIn, // In ms between packets. 
                               // 0 = no delay
                 int sendTimeIn, // In seconds
                 int sendSizeIn, // Any additional padding on sent
                 int recvSizeIn  // and on received
                 ){
        isPing = false;
        dupRange = 30000;
        dupData = new boolean[dupRange];
        dupCount = 0;
        reorderCount = 0;
        reorderIndex = -1;

        lossBurstCount = 0;
        lossBurstLength = 0;

        stopAtPing = false;

        serverRecvCount = 0;
        server = serverIn;
        try {
        serverIP = InetAddress.getByName(server);
        } catch (UnknownHostException e){
        //status = Test.TEST_ERROR;
        Log.d(TAG, "Failed to initialize properly");
        return;
        }
        port = portIn;
        sendRate = sendRateIn;
        sendTime = sendTimeIn;
        sendSize = sendSizeIn;
        recvSize = recvSizeIn;
        sendSlug = "";
        for(int i = 0; i < sendSize; ++i){
        sendSlug += ".";
        }
    }

    public void run(){
        sendCount = 0;
        recvCount = 0;

        //status = Test.TEST_ERROR;

        long rttCount = 0;
        long sustainedRttCount = 0;
        long ppsCount = 0;

        long startTime = (new Date()).getTime();
        long currentTime = (new Date()).getTime();      

        Log.d(TAG,"Start time is " + startTime);
        Log.d(TAG,"Remote server is " + server);
        Log.d(TAG,"Remote port is " + port);
        
        byte [] bufIn = new byte[2048];
        DatagramSocket socket;
        try{
            socket = new DatagramSocket();
            socket.setSoTimeout(1);
        } catch (SocketException e){
            //status = Test.TEST_ERROR;
            Log.d(TAG,"Test aborted due to socket exception");
            return;
        }

        long lastSend = 0;
        recvPacketSize = 0;

        try {
        while((stopAtPing //&& !TestState.startedPingTest
               && sendCount < maxSend) ||
              (isPing && sendCount < maxSend && !stopAtPing) ||
              (!isPing && !stopAtPing && 
               (currentTime - startTime) < (sendTime * 1000))){
            currentTime = (new Date()).getTime();
            if((sendRate == 0) ||
               ((currentTime - lastSend) > sendRate)){
            String message = (currentTime - startTime) + " " + 
                sendCount +
                " " + recvSize + " " + sendSlug;
        
            try {
                socket.send(new DatagramPacket(message.getBytes(),
                               message.length(),
                               serverIP,
                               port));
            }  catch(IOException e){
                if(isPing){
                Log.d(TAG,"Probing process caught IOException, just treating as a loss event.");
                }
            }
            sendCount++;
            sendPacketSize = message.length();
            lastSend = currentTime;
            }
            try {
            DatagramPacket d = new DatagramPacket(bufIn,
                                  2048);
            socket.receive(d);
            currentTime = (new Date()).getTime();
            long deltaTime = currentTime - startTime;
            String s = new String(bufIn);
            String []t = s.split(" ");
            int sentTime = Utils.parseInt(t[0]);
            int serverCount = Utils.parseInt(t[4]);

            int packetID = Utils.parseInt(t[2]);
            if(packetID < reorderIndex){
                reorderCount += 1;
                Log.d(TAG,"Packet reordering observed");
            }
            reorderIndex = packetID;
            if(packetID < dupRange &&
               dupData[packetID]){
                Log.d(TAG,"Duplicate packet received");
                dupCount += 1;
            }
            if(packetID < dupRange){
                dupData[packetID] = true;
            }


            if(serverCount > this.serverRecvCount){
                this.serverRecvCount = serverCount;
            }
            if (sentTime < 0)
                return;
            rttCount += (deltaTime - sentTime);


            if(d.getLength() != 0){
                recvPacketSize = d.getLength();
            }

            if(deltaTime >= (sendTime * 500)){
                ppsCount += 1;
                sustainedRttCount += (deltaTime - sentTime);
            }
            recvCount++;
            
            } catch(SocketTimeoutException e){
            
            }
            currentTime = (new Date()).getTime();
        }

        long loopTime = (new Date()).getTime();
        Log.d(TAG,"All packets sent, waiting for the last responses");
        // Keep receiving for 50% seconds afterwards
        // 
        // Or 2 seconds afterwards for pings
        while( (!isPing && ((currentTime - startTime) < ((sendTime) * 1500)))
               ||
               (isPing && (loopTime > (currentTime - 3000)))){
            try{
            DatagramPacket d = new DatagramPacket(bufIn, 2048);
            socket.receive(d);

            if(d.getLength() != 0){
                recvPacketSize = d.getLength();
            }

            currentTime = (new Date()).getTime();
            long deltaTime = currentTime - startTime;
            String s = new String(bufIn);
            String []t = s.split(" ");
            int sentTime = Utils.parseInt(t[0]);
            int serverCount = Utils.parseInt(t[4]);

            int packetID = Utils.parseInt(t[2]);
            if(packetID < reorderIndex){
                reorderCount += 1;
                Log.d(TAG,"Packet reordering observed");
            }
            reorderIndex = packetID;
            if(packetID < dupRange &&
               dupData[packetID]){
                Log.d(TAG,"Duplicate packet received");
                dupCount += 1;
            }
            if(packetID < dupRange){
                dupData[packetID] = true;
            }

            if(serverCount > this.serverRecvCount){
                this.serverRecvCount = serverCount;
            }
            if (sentTime < 0)
                return;
            rttCount += (deltaTime - sentTime);
            recvCount++;
            } catch(SocketTimeoutException e){
            
            }
            currentTime = (new Date()).getTime();
        }
        // A burst is a minimum of 3 packets in a row lost
        Log.d(TAG,"Now counting up bursts on loss");
        boolean inBurst = false;
        long currentBurst = 0;
        for(int i = 2; i < sendCount && i < dupRange; ++i){
            if(dupData[i]){
            inBurst = false;
            }
            else if (!dupData[i] &&
                 !dupData[i-1] &&
                 !dupData[i-2]){
            if(inBurst){
                currentBurst += 1;
                if(currentBurst > lossBurstLength){
                lossBurstLength = currentBurst;
                }
            } else {
                inBurst = true;
                currentBurst = 3;
                lossBurstCount += 1;
                if(lossBurstLength < 3){
                lossBurstLength = 3;
                }
            }

            }
        }
        Log.d(TAG,"Probing done");
        } catch(IOException e){
        Log.d(TAG,"Probing process caught IOException!");
        //status = Test.TEST_ERROR;
        return;
        }
        avgRTT = ((float) rttCount) / ((float) recvCount);
        sustainedPPS = ((float) ppsCount) / ((float) (sendTime * 0.5));
        sustainedRTT = ((float) sustainedRttCount) / ((float) ppsCount);

        Log.d(TAG,"Sent " + sendCount + " packets");
        Log.d(TAG,"Received " + recvCount + " packets");
        Log.d(TAG,"Average RTT " + avgRTT);
        Log.d(TAG,"Sustained RTT " + sustainedRTT);
        Log.d(TAG,"Server received " + serverRecvCount);
        
        Log.d(TAG,"Packets reordered " + reorderCount);
        Log.d(TAG,"Packets duplicated " + dupCount);

        Log.d(TAG,"Loss bursts observed " + lossBurstCount);

        if (!isPing){
        Log.d(TAG,"Sustained PPS " + sustainedPPS);
        Log.d(TAG,"Send packet bandwidth " +
                (sendPacketSize * 8 * sustainedPPS));
        Log.d(TAG,"Received packet bandwidth " +
                (recvPacketSize * 8 * sustainedPPS));
        }
        Log.d(TAG,"Send packet size " + sendPacketSize);
        Log.d(TAG, "Received packet size " + recvPacketSize);

        socket.close();
        //status = Test.TEST_SUCCESS;
    }
    
}