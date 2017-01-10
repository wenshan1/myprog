package me.wenshan.stockmodel.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StockModelData_Pk implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column (name="name", nullable = false)
    private String name; /* 模型名字 */
    
    @Column (name="riqi", nullable = false)
    private Date riqi; /* 日期 */
    
    public StockModelData_Pk () {
        
    }
    public StockModelData_Pk (Date riqi, String name) {
        this.riqi = riqi;
        this.name = name;
        
    }
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
    
    public int hashCode(){
        return name.hashCode() + riqi.hashCode();
    }
    
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        StockModelData_Pk pk = (StockModelData_Pk) obj;
        return pk.getRiqi().compareTo(riqi) == 0 && pk.name.compareTo(name) == 0;
    }
}
