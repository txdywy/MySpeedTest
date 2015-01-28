package com.num.myspeedtest.controller.utils;

import com.num.myspeedtest.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class DNSUtil {

    private static InetSocketAddress RadbAddr = null;

    public static String getAsnByIp(String ip) {
        Socket socket;
        String hostname = "";
        if(ip.equals("")) {
            return hostname;
        }
        if(RadbAddr == null) {
            RadbAddr = new InetSocketAddress(Constants.RADB_SERVER, Constants.RADB_PORT);
        }
        try {
            socket = new Socket();
            socket.setSoTimeout(Constants.SO_TIMEOUT);
            socket.connect(RadbAddr);
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(in);
            out.write(ip + "\r\n");
            out.flush();

            String response;
            while((response = reader.readLine())!=null) {
                if(response.contains("origin:")) {
                    hostname = response.replace("origin:","").trim();
                    in.close();
                    out.close();
                    socket.close();
                    return hostname;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hostname;
    }
}
