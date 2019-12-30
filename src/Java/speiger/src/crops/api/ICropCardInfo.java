package speiger.src.crops.api;

import java.util.List;
import net.minecraft.item.ItemStack;

public interface ICropCardInfo {
	
	List<String> getCropInformation();

	ItemStack getDisplayItem();
	
}