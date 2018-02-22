package gtPlusPlus.core.item.base.plates;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseItemPlate_OLD extends Item{

	protected final int colour;
	protected final int sRadiation;
	protected final String materialName;
	protected final String unlocalName;
	protected final String chemicalNotation;

	public BaseItemPlate_OLD(final String unlocalizedName, final String materialName, final int colour, final int sRadioactivity) {
		this(unlocalizedName, materialName, "NullFormula", colour, sRadioactivity);
	}
	
	public BaseItemPlate_OLD(final String unlocalizedName, final String materialName, final String mChemicalFormula, final int colour, final int sRadioactivity) {
		this.setUnlocalizedName("itemPlate"+unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.unlocalName = "itemPlate"+unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemPlate");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
		if (mChemicalFormula.equals("") || mChemicalFormula.equals("NullFormula")){
			this.chemicalNotation = StringUtils.subscript(materialName);			
		}
		else {
			this.chemicalNotation = StringUtils.subscript(mChemicalFormula);			
		}
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, "itemPlate"+unlocalizedName);
		String temp;
		if (this.unlocalName.toLowerCase().contains("itemplate")){
			temp = this.unlocalName.replace("itemP", "p");
			if ((temp != null) && !temp.equals("")){
				GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
			}
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return (this.materialName+ " plate");
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}
	
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (StringUtils.containsSuperOrSubScript(this.chemicalNotation)){
			list.add(this.chemicalNotation);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}
}
