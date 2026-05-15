package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class CustomCollectors {
    public static class AuditAccumulator{
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryMap = new HashMap<>();
        private final Map<String, Transaction> countryMaxMap = new HashMap<>();
        List<Transaction> highValueList = new ArrayList<>();
        int count = 0;
        void accumulate(Transaction tx){
            total = total.add(tx.getAmount());
            categoryMap.merge(tx.getCategory(), tx.getAmount(), BigDecimal::add);
            countryMaxMap.merge(tx.getCountry(), tx, (existing, replacement) ->
                    replacement.getAmount().compareTo(existing.getAmount()) > 0 ? replacement : existing);
            if(tx.getAmount().compareTo(new BigDecimal("1000"))> 0){
                highValueList.add(tx);
            }
            count++;
        }
        AuditAccumulator combine(AuditAccumulator other){
            this.total = this.total.add(other.total);
            other.categoryMap.forEach((k,v) -> this.categoryMap.merge(k, v, BigDecimal::add));
            other.countryMaxMap.forEach((k, v) -> this.countryMaxMap.merge(k, v, (e, r) ->
                    r.getAmount().compareTo(e.getAmount()) > 0 ? r : e));
            this.highValueList.addAll(other.highValueList);
            this.count += other.count;
            return this;
        }
        AuditSnapshot finish(){
            double avg = (count == 0) ? 0 : total.doubleValue()/count;
            return new AuditSnapshot(total, categoryMap, countryMaxMap, highValueList, avg);
        }
    }
    public static Collector<Transaction, ?, AuditSnapshot> toAuditSnapshot(){
        return Collector.of(
                AuditAccumulator::new,
                AuditAccumulator::accumulate,
                AuditAccumulator::combine,
                AuditAccumulator::finish
        );
    }
}
