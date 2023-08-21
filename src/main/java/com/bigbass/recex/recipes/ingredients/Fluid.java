package com.bigbass.recex.recipes.ingredients;

public class Fluid {
	
	/** amount */
	public int a;
	
	/** unlocalizedName */
	public String uN;
	
	/** localizedName */
	public String lN;
	
	/** Image Path */
	public String iP;
	
	public Fluid(){
		
	}
	
	public Fluid(int amount, String unlocalizedName, String fluidName){
		this.a = amount;
		this.uN = unlocalizedName;
		this.lN = fluidName;
	}
	
	public Fluid(int amount, String unlocalizedName, String displayName, String imagePath){
		this.a = amount;
		this.uN = unlocalizedName;
		this.lN = displayName;
		this.iP = imagePath;
	}
}
