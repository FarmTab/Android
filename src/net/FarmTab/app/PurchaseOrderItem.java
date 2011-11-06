package net.FarmTab.app;

public class PurchaseOrderItem {
	
	public InventoryItem item;
	public double quantity;
	public double price;

	public PurchaseOrderItem(InventoryItem item, double quantity, double price) {
		this.item = item;
		this.quantity = quantity;
		this.price = price;
	}
	
	public String toString() {
		return item.name + "\n" + this.quantity + " " + 
			(item.unit.equals("") ? "" : item.unit + " ") +
			"for " + "$" + this.price;
	}
}
