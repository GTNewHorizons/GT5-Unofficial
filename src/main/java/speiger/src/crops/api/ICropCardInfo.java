package speiger.src.crops.api;

import java.util.List;

import net.minecraft.item.ItemStack;

import ic2.api.crops.CropCard;

/**
 * Adds information from CropCards. This class has priority over ICropInfo.
 * 
 * @requirement: the class that implements this interface needs to extend {@link CropCard}
 */
public interface ICropCardInfo {

    public List<String> getCropInformation();

    public ItemStack getDisplayItem();
}
