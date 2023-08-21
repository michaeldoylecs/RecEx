package com.bigbass.recex.recipes.ingredients;

public class Item implements IItem {
	
	/** amount */
	public int a;
	
	/** unlocalizedName */
	public String uN;
	
	/** localizedName */
	public String lN;
	
	/** Image Path */
	public String iP;
	
	public Item(){
		
	}
	
	public Item(int amount, String unlocalizedName, String displayName){
		this.a = amount;
		this.uN = unlocalizedName;
		this.lN = displayName;
	}
	
	public Item(int amount, String unlocalizedName, String displayName, String imagePath){
		this.a = amount;
		this.uN = unlocalizedName;
		this.lN = displayName;
		this.iP = imagePath;
	}
}
