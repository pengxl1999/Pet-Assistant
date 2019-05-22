package com.pengxl.petassistant;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static com.pengxl.petassistant.StaticMember.account;
import static com.pengxl.petassistant.StaticMember.step;

public class ReceiveThread extends Thread {

    private Socket socket;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private String msg;
    private Context context;

    /*
        Previous version:
//        public static double latitude = -1;
//        public static double longitude = -1;
        Use StaticMember.xxx instead.
     */

    private static int i = 0;

    /*
        Previous version(UDP):
            @Override
            public void run(){
                while(!Thread.currentThread().isInterrupted()){
                i++;
                byte[] buffer = new byte[10240];
                DatagramPacket dgp = new DatagramPacket(buffer, buffer.length);
                try {
                    rsSocket.receive(dgp);
                    msg = new String(dgp.getData(), 0, dgp.getLength());
                    //Log.e("MESSAGE","" + msg);
                    Thread.yield();
                    getLatlng();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        TCP instead.
     */

    public ReceiveThread(Context context) {
        this.context = context;
    }

    @Override
    synchronized public void run() {
        Log.i("pengxl1999", "尝试连接");
        try {
            socket = new Socket("39.106.219.88", 8088);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("pengxl1999", "连接成功");

        try {
            if(bufferedReader == null) {
                return;
            }
            //发送消息确认身份
            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);
            printWriter.println("2 " + account);
            printWriter.flush();
            //printWriter.println("Hello Server!");
            while(!currentThread().isInterrupted()) {
                if((msg = bufferedReader.readLine()) != null) {
                    //Log.i("pengxl1999", "Server Message: " + msg);
                    String[] message = msg.split(" ");
                    if(message.length == 4 && message[0].equals("pxl")) {
                        double petLatitude = Double.parseDouble(message[1]);
                        double petLongitude = Double.parseDouble(message[2]);
                        step = Integer.parseInt(message[3]);
                        Log.i("pengxl1999", "Pet Position:" + petLatitude + " " + petLongitude + " " + step);

                        //坐标转换并记录宠物位置
                        LatLng latLng = new LatLng(petLatitude, petLongitude);
                        CoordinateConverter converter = new CoordinateConverter(context);
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(latLng);
                        LatLng result = converter.convert();
                        StaticMember.petPosition = result;
                        Log.i("pengxl1999",result.latitude + " " +  result.longitude);
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
                if(inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*处理从硬件获得的信息*/

    /*private void getLatlng() {
        StringBuffer lat = new StringBuffer();
        StringBuffer lon = new StringBuffer();
        StringBuffer s = new StringBuffer();
        if (msg == null) return;
        int k = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                if (k == 0) {
                    for (int j = k; j < i; j++) {
                        lon.append(msg.charAt(j));
                    }
                    k = i + 1;
                } else {
                    for (int j = k; j < i; j++) {
                        lat.append(msg.charAt(j));
                    }
                    k = i + 1;
                }
            }
        }
        for(int i = k;i < msg.length(); i++) {
            s.append(msg.charAt(i));
        }
        if(!lat.toString().equals(""))      StaticMember.petLatitude = Double.parseDouble(lat.toString());
        if(!lon.toString().equals(""))      StaticMember.petLongitude = Double.parseDouble(lon.toString());
        if(!s.toString().equals(""))        step = Integer.parseInt(s.toString());

        /*坐标转换，把GPS获取的真实坐标值转换为高德地图坐标*/

        /*LatLng latLng = new LatLng(StaticMember.petLatitude, StaticMember.petLongitude);
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(latLng);
        LatLng result = converter.convert();

        StaticMember.petLatitude = result.latitude;
        StaticMember.petLongitude = result.longitude;

    }*/
}
