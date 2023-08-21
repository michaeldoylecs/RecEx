package com.bigbass.recex.recipes;

import codechicken.nei.ItemList;
import com.bigbass.recex.Logger;
import com.bigbass.recex.render.RenderDispatcher;
import com.bigbass.recex.render.RenderJob;
import com.bigbass.recex.render.Renderer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ImageExporter {
	private final HashSet<String> exportedItemIdStrings = new HashSet<>();
	private final HashSet<String> exportedFluidIdStrings = new HashSet<>();
	
	public ImageExporter(File imageDirectory) {
		if (!imageDirectory.isDirectory()) {
			throw new IllegalArgumentException("imageDirectory must be a valid directory!");
		}
		Renderer.INSTANCE.preinitialize(imageDirectory);
		RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.INITIALIZING);
	}
	
	/**
	 * Exports the image of an ItemStack if it hasn't yet been exported.
	 */
	public void exportItemImageIfUnique(ItemStack itemStack) {
		if (itemStack == null) { return; }
		ItemStack sanitizedItemStack = sanitizeItemStackForRendering(itemStack);
		String itemId = FilePathUtil.itemId(sanitizedItemStack);
		if (!exportedItemIdStrings.contains(itemId)) {
			Logger.intermittentLog(
				"exportItemImageIfUnique",
				"Enqueueing render of item: " + sanitizedItemStack);
			RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(sanitizedItemStack));
			exportedItemIdStrings.add(itemId);
		}
	}
	
	/**
	 * Exports the images of all ItemStacks if they haven't yet been exported.
	 */
	public void exportItemImagesIfUnique(List<ItemStack> itemStack) {
		for (ItemStack stack : itemStack) {
			exportItemImageIfUnique(stack);
		}
	}
	
	/**
	 * Exports the image of an ItemStack if it hasn't yet been exported.
	 */
	public void exportFluidImageIfUnique(FluidStack fluidStack) {
		if (fluidStack == null) { return; }
		if (fluidStack.getFluid().getIcon() == null) {
			Logger.error("Found fluid with null icon: " + fluidStack.getUnlocalizedName());
			return;
		}
		String fluidId = FilePathUtil.fluidId(fluidStack);
		if (!exportedFluidIdStrings.contains(fluidId)) {
			Logger.intermittentLog(
				"exportFluidImageIfUnique",
				"Enqueueing render of fluid: " + fluidStack.getUnlocalizedName());
			RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
			exportedFluidIdStrings.add(fluidId);
		}
	}
	
	public void sleepUntilJobsComplete() {
		try {
			RenderDispatcher.INSTANCE.waitUntilJobsComplete();
		} catch (InterruptedException wakeUp) {
			Logger.error(wakeUp.getMessage());
			Logger.error(Arrays.toString(wakeUp.getStackTrace()));
		}
		RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.DESTROYING);
	}
	
	private ItemStack sanitizeItemStackForRendering(ItemStack originalItemStack) {
		ItemStack itemStack = originalItemStack.copy();
		// We need to handle items whose damage is set to the wildcard, otherwise
		// the renderer will crash from misusing the damage value.
		if (hasWildcardItemDamage(originalItemStack)) {
			itemStack.setItemDamage(0);
		}
		return itemStack;
	}
	
	private boolean hasWildcardItemDamage(ItemStack itemStack) {
		return itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
	}
}
