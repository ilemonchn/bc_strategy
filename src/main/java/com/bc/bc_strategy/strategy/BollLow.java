package com.bc.bc_strategy.strategy;

import com.bc.bc_strategy.api.Klines;
import com.bc.bc_strategy.data.Datas;
import com.bc.bc_strategy.indicator.Boll;
import java.util.List;

public class BollLow {

    public static void main(String[] args) throws Exception {
        String data = Datas.readFile();
        List<Klines> klines = Klines.parseData(data);





    }
}
