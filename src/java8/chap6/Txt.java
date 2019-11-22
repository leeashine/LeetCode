package java8.chap6;

import java.math.BigDecimal;
import java.util.Date;

public class Txt {
	private Date date;
	private String name;
	private String shopName;
	private BigDecimal money;
	private Double charge;
	private Double trueMoney;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Double getCharge() {
		return charge;
	}
	public void setCharge(Double charge) {
		this.charge = charge;
	}
	public Double getTrueMoney() {
		return trueMoney;
	}
	public void setTrueMoney(Double trueMoney) {
		this.trueMoney = trueMoney;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	
}
