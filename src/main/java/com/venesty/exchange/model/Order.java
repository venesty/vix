package com.venesty.exchange.model;

public class Order {
	
	public static enum Direction {
		BUY,
		SELL;
	}
	
	private String ric;
	
	private Integer quantity;
	
	private Double price;
	
	private Direction direction;
	
	private String user;
	
	private Double executionPrice;
	
	public Order(String ric, Integer quantity, Double price, Direction direction, String user) {
		this.ric = ric;
		this.quantity = quantity;
		this.price = price;
		this.direction = direction;
		this.user = user;
	}

	public String getRic() {
		return ric;
	}

	public Integer getQuantity() {
		return quantity;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setExecutionPrice(Double executionPrice) {
		this.executionPrice = executionPrice;
	}
	
	public Double getExecutionPrice() {
		return executionPrice;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public String getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((executionPrice == null) ? 0 : executionPrice.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((ric == null) ? 0 : ric.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (direction != other.direction)
			return false;
		if (executionPrice == null) {
			if (other.executionPrice != null)
				return false;
		} else if (!executionPrice.equals(other.executionPrice))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (ric == null) {
			if (other.ric != null)
				return false;
		} else if (!ric.equals(other.ric))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [ric=" + ric + ", quantity=" + quantity + ", price="
				+ price + ", direction=" + direction + ", user=" + user + "]";
	}
	
	

}