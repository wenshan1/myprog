package me.wenshan.stock.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StockIndex_PK implements Serializable {
    private static final long serialVersionUID = -6282576227805319314L;

    @Column(name = "name")
    private String name; /* 股票代码 */

    @Column(name = "riqi")
    private Date riqi; /* 日期 */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRiqi() {
        return riqi;
    }

    public void setRiqi(Date riqi) {
        this.riqi = riqi;
    }

    public int hashCode() {
        return name.hashCode() + riqi.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        StockIndex_PK pk = (StockIndex_PK) obj;
        return pk.getRiqi().compareTo(riqi) == 0 && pk.name.compareTo(name) == 0;
    }
}
