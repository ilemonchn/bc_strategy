package com.bc.bc_strategy.indicator;

import com.bc.bc_strategy.api.Klines;
import com.bc.bc_strategy.util.JsonUtil;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Boll {

    private double high;
    private double avg;
    private double low;

    /**
     *
     * @param klines 入参为个级别的20根k线。一般取最新的21根kline，去掉暂未形成的最后一个，计算剩余20个的布林线。
     * @return
     */
    public static Boll calcBoll(List<Klines> klines) {
        int size = klines.size();
        double sum = klines.stream().reduce(Klines.builder().build(), (a, b)-> {
            a.setClosePrice(a.getClosePrice() + b.getClosePrice());
            return a;
        }).getClosePrice();
        double avg = sum/size;
        // 方差
        double t = 0.0;
        for (int i = 0; i < klines.size(); i++) {
            double price = klines.get(i).getClosePrice();
            t += (price-avg) * (price-avg);
        }
        double sd = Math.sqrt(t/size);
        double high = avg + 2*sd;
        double low = avg - 2*sd;
        Boll b = Boll.builder().avg(avg).high(high).low(low).build();
        return b;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
