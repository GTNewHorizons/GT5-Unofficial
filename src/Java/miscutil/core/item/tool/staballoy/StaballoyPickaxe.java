package miscutil.core.item.tool.staballoy;

import miscutil.core.lib.Strings;
import net.minecraft.item.ItemPickaxe;

public class StaballoyPickaxe extends ItemPickaxe{

	public StaballoyPickaxe(String unlocalizedName, ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(Strings.MODID + ":" + unlocalizedName);
	}

}
