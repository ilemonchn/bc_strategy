package com.bc.bc_strategy.api;

import com.bc.bc_strategy.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * //// Kline open time
 * //    "0.01634790",       // Open price
 * //            "0.80000000",       // High price
 * //            "0.01575800",       // Low price
 * //            "0.01577100",       // Close price
 * //            "148976.11427815",  // Volume
 * //            1499644799999,      // Kline Close time
 * //            "2434.19055334",    // Quote asset volume
 * //            308,                // Number of trades
 * //            "1756.87402397",    // Taker buy base asset volume
 * //            "28.46694368",      // Taker buy quote asset volume
 * //            "0"                 // Unused field, ignore.
 *
 * //    [
 * //            [
 * //            1499040000000,      // k线开盘时间
 * //            "0.01634790",       // 开盘价
 * //            "0.80000000",       // 最高价
 * //            "0.01575800",       // 最低价
 * //            "0.01577100",       // 收盘价(当前K线未结束的即为最新价)
 * //            "148976.11427815",  // 成交量
 * //            1499644799999,      // k线收盘时间
 * //            "2434.19055334",    // 成交额
 * //            308,                // 成交笔数
 * //            "1756.87402397",    // 主动买入成交量
 * //            "28.46694368",      // 主动买入成交额
 * //            "17928899.62484339" // 请忽略该参数
 * //            ]
 * //            ]
 */
@Getter
@Setter
@Builder
public class Klines {
    private long openTime; //0 开盘时间
    private double openPrice; //1 开盘价
    private double highPrice; //2 最高价
    private double lowPrice; //3 最低价
    private double closePrice; //4 收盘价(当前K线未结束的即为最新价)
    private double volume; //5 成交量 btc
    private long closeTime; //6 收盘时间
    private double amount; //7 成交额 usdt
    private double orderNum; //8 成交笔数
    private double takerVolume; //9 主动买入成交量 btc
    private double takerAmount; //10 主动买入成交额 usdt

    public static List<Klines> parseData(String linesData) {
        int start = linesData.indexOf("[");
        int end = linesData.lastIndexOf("]");
        String pure = linesData.substring(start+1, end).replaceAll(" ", "");
        String[] splits = pure.split("\\],\\[");
        List<Klines> klines = new ArrayList<>();
        for (String l : splits) {
            String data = l.replaceAll("\\[", "").replaceAll("\\]", "");
            Klines k = Klines.builder().build().parseSingle(data);
            klines.add(k);
        }
        return klines;
    }


    public Klines parseSingle(String line) {
        KlinesBuilder klb = Klines.builder();
        int fid = 0;
        for (String field : line.split(",")) {
            String f = field.replaceAll("\"", "");
            if (fid == 0) {
                long openTime = Long.parseLong(f);
                klb.openTime(openTime);
            } else if (fid == 1) {
                double openPrice = Double.parseDouble(f);
                klb.openPrice(openPrice);
            } else if (fid == 2) {
                double price = Double.parseDouble(f);
                klb.highPrice(price);
            } else if (fid == 3) {
                double price = Double.parseDouble(f);
                klb.lowPrice(price);
            } else if (fid == 4) {
                double price = Double.parseDouble(f);
                klb.closePrice(price);
            } else if (fid == 5) {
                double v = Double.parseDouble(f);
                klb.volume(v);
            } else if (fid == 6) {
                long v = Long.parseLong(f);
                klb.closeTime(v);
            } else if (fid == 7) {
                double v = Double.parseDouble(f);
                klb.amount(v);
            } else if (fid == 8) {
                double v = Double.parseDouble(f);
                klb.orderNum(v);
            } else if (fid == 9) {
                double v = Double.parseDouble(f);
                klb.takerVolume(v);
            } else if (fid == 10) {
                double v = Double.parseDouble(f);
                klb.takerAmount(v);
                break;
            }
            fid++;
        }
        return klb.build();
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

}
