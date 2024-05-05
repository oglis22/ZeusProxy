package de.code;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Blacklist {

    public Blacklist() {

    }

    private ArrayList<String> blacklist = new ArrayList<>();

    public boolean isIpOnBlacklist(String ip) {
        for (String s : blacklist) {
           if (s.equals(ip)) return true;
        }
        return false;
    }

    public ArrayList<String> getBlacklist() { return this.blacklist; }

    public void loadBlackList() {

        System.out.println("loading ip blacklist...");

        File file = new File("blacklist.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("blacklist.txt"));
            String line;
            while((line = br.readLine()) != null) {
                blacklist.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Blacklist loaded!");

    }

}
