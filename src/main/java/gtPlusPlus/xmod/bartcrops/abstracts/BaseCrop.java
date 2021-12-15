package gtPlusPlus.xmod.bartcrops.abstracts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import speiger.src.crops.api.ICropCardInfo;

public abstract class BaseCrop extends CropCard implements ICropCardInfo {
	@SideOnly(Side.CLIENT)
	public void registerSprites(IIconRegister iconRegister) {
		this.textures = new IIcon[this.maxSize()];

		for (int i = 1; i <= this.textures.length; ++i) {
			this.textures[i - 1] = iconRegister.registerIcon(CORE.MODID+":crop/blockCrop." + this.name() + "." + i);
		}

	}

	public float dropGainChance() {
		return (float) (Math.pow(0.95D, (double) ((float) this.tier())) * (double) 1f);
	}

	public boolean canCross(ICropTile crop) {
		return crop.getSize() == this.maxSize();
	}

	public int getrootslength(ICropTile crop) {
		return 3;
	}

	public String discoveredBy() {
		return "Alkalus";
	}

	public String owner() {
		return "Gtplusplus";
	}

	public List<String> getCropInformation() {
		List<String> ret = new ArrayList<String>();
		ret.add(this.attributes().toString());
		return ret;
	}

	public ItemStack getDisplayItem(CropCard card) {
		return ItemUtils.getItemStackOfAmountFromOreDict("crop" + this.name(), 0);
	}
}