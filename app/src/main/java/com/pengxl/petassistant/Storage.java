package com.pengxl.petassistant;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Storage {

    private int stringNumber;
    private Context context;

    public Storage(Context context) {
        this.stringNumber = 0;
        this.context = context;
    }

    public boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "不适用";
        }
        return sdpath;
    }

    public String getDefaultFilePath() {
        String filepath = "";
        File file = new File(Environment.getExternalStorageDirectory(),
                "abc.txt");
        if (file.exists()) {
            filepath = file.getAbsolutePath();
        } else {
            filepath = "不适用";
        }
        return filepath;
    }

    public boolean readUserFile() {
        try {
            File parent = new File(Environment.getExternalStorageDirectory().getPath() + "/PetAssistantData");
            if(!parent.mkdirs()) {}
            File file = new File(parent, "UserData.txt");
            if(!file.exists()) {
                return false;
            }
            if(file.length() == 0) {
                return false;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";
            while ((readline = br.readLine()) != null) {
                String[] s = readline.split(" ");
                StaticMember.account = s[0];
                StaticMember.password = s[1];
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void writeUserFile() {
        try {
            File parent = new File(Environment.getExternalStorageDirectory().getPath() + "/PetAssistantData");
            if(!parent.mkdirs()) {}
            File file = new File(parent, "UserData.txt");
            if(!file.createNewFile()) {}
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            String info = StaticMember.account + " " + StaticMember.password;
            bw.write(info);
            bw.flush();
            //Toast.makeText(context, "写入成功！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void writePetFile(String message) {
        try {
            File parent = new File(Environment.getExternalStorageDirectory().getPath() + "/PetAssistantData");
            if(!parent.mkdirs()) {}
            File file = new File(parent, StaticMember.account + ".txt");
            if(!file.createNewFile()) {}
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            bw.write(message);
            bw.flush();
            //Toast.makeText(context, "写入成功！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String readPetFile() {
        try {
            File parent = new File(Environment.getExternalStorageDirectory().getPath() + "/PetAssistantData");
            if(!parent.mkdirs()) {}
            File file = new File(parent, StaticMember.account + ".txt");
            if(!file.exists()) {
                return "";
            }
            if(file.length() == 0) {
                return "";
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";
            if ((readline = br.readLine()) != null) {
                return readline;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
