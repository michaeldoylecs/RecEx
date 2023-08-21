package com.bigbass.recex.recipes;

import com.bigbass.recex.recipes.ingredients.Item;

public class ItemBreakthroughCircuit extends Item {
	
	/** circuit config */
	public int cfg;
	
	public ItemBreakthroughCircuit(){
	
	}
	
	public ItemBreakthroughCircuit(Item item, int cfg){
		super(item.a, item.uN, item.lN);
		
		this.cfg = cfg;
	}
}
