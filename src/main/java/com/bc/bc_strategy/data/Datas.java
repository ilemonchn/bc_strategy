package com.bc.bc_strategy.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Datas {

    private static final String FILE_PATH = "src/main/java/com/bc/bc_strategy/data/klines.txt";

    public static String readFile() throws Exception {
        FileReader fr = new FileReader(FILE_PATH);
        BufferedReader br = new BufferedReader(fr);
        String data = br.readLine();
        br.close();
        return data;
    }

    public static void writeFile(String data) throws Exception {
        FileWriter fw = new FileWriter(FILE_PATH);
        fw.write(data);
        fw.close();
    }
}
