package com.bigbass.recex.recipes;

import com.bigbass.recex.recipes.ingredients.Item;

public class ItemBioCircuit extends Item {
	
	/** circuit config */
	public int cfg;
	
	public ItemBioCircuit(){
	
	}
	
	public ItemBioCircuit(Item item, int cfg){
		super(item.a, item.uN, item.lN);
		
		this.cfg = cfg;
	}
}
