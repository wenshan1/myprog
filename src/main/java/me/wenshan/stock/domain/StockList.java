package me.wenshan.stock.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stocklist")
public class StockList {
    public StockList(String str) {
        this.stockName = str;
    }

    public StockList(){
        
    }
    
    @Id
    @Column(name="stockname")
    private String stockName;
    
    public String getStockName ()
    {
        return this.stockName;
    }
}
