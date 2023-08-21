package com.bigbass.recex.recipes;

import com.bigbass.recex.render.Renderer;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public final class FilePathUtil
{
	private FilePathUtil() {}
	
	/** This string needs to be URL parameter-safe, as well as file system-safe. */
	public static final String ID_SEPARATOR = "~";
	
	private static final HashMap<Item, String> badItemLookup = new HashMap<>();
	
	public static String itemId(ItemStack itemStack) {
		if (itemStack == null) { throw new IllegalArgumentException("itemStack must not be null"); }
		String id = itemId(itemStack.getItem());
		id += ID_SEPARATOR + itemStack.getItemDamage();
		
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (nbt != null) {
			id += ID_SEPARATOR + nbtToEncodedString(nbt);
		}
		
		return id;
	}
	
	public static String itemId(Item item) {
		String uniqueId;
		try
		{
			GameRegistry.UniqueIdentifier uniqueIdObj = GameRegistry.findUniqueIdentifierFor(item);
			uniqueId = uniqueIdObj.modId + ID_SEPARATOR + uniqueIdObj.name;
		} catch (Exception ignored) {
			uniqueId = badItemLookup.get(item);
			if (uniqueId == null) {
				uniqueId = UUID.randomUUID() + ID_SEPARATOR + UUID.randomUUID();
				badItemLookup.put(item, uniqueId);
			}
		}
		return sanitize(uniqueId);
	}
	
	public static String imageFilePath(ItemStack itemStack) {
		// Replace the first occurrence of ID_SEPARATOR to get the mod name as its own separate
		// folder.
		String itemId = itemId(itemStack);
		int firstIndex = itemId.indexOf(ID_SEPARATOR);
		return "item" + File.separator + itemId.substring(0, firstIndex) + File.separator
			+ itemId.substring(firstIndex + ID_SEPARATOR.length())
			+ Renderer.IMAGE_FILE_EXTENSION;
	}
	
	public static String fluidId(FluidStack fluidStack) {
		if (fluidStack == null) { throw new IllegalArgumentException("fluidStack must not be null"); }
		String id = fluidId(fluidStack.getFluid());
		
		NBTTagCompound nbt = fluidStack.tag;
		if (nbt != null) {
			id += ID_SEPARATOR + nbtToEncodedString(nbt);
		}
		
		return id;
	}
	
	public static String fluidId(Fluid fluid) {
		String uniqueName = FluidRegistry.getDefaultFluidName(fluid);
		int separator = uniqueName.indexOf(':');
		return sanitize(
			uniqueName.substring(0, separator)
				+ ID_SEPARATOR + uniqueName.substring(separator + 1));
	}
	
	public static String imageFilePath(FluidStack fluidStack) {
		// Replace the first occurrence of ID_SEPARATOR to get the mod name as its own separate
		// folder.
		String fluidId = fluidId(fluidStack.getFluid());
		int firstIndex = fluidId.indexOf(ID_SEPARATOR);
		return "fluid" + File.separator + fluidId.substring(0, firstIndex) + File.separator
			+ fluidId.substring(firstIndex + ID_SEPARATOR.length())
			+ Renderer.IMAGE_FILE_EXTENSION;
	}
	
	/**
	 * Strips out URL- and file system-unsafe characters.
	 *
	 * <p>Windows in particular is a bit finicky. We may need to expand this method in the future.
	 * See <a href="https://stackoverflow.com/a/48962674">here</a>.
	 */
	private static String sanitize(String string) {
		// Note: four backslashes are needed to escape to a single backslash in the target string.
		return string.replaceAll("[<>:\"/\\\\|?*]", "");
	}
	
	private static String nbtToEncodedString(NBTTagCompound nbt) {
		UUID uuid = UUID.nameUUIDFromBytes(nbt.toString().getBytes(StandardCharsets.UTF_8));
		byte[] upper = Longs.toByteArray(uuid.getMostSignificantBits());
		byte[] lower = Longs.toByteArray(uuid.getLeastSignificantBits());
		return Base64.getUrlEncoder().encodeToString(Bytes.concat(upper, lower));
	}
}
