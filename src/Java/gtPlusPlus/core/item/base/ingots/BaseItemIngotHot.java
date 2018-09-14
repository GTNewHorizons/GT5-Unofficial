package gtPlusPlus.core.item.base.ingots;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemIngotHot extends BaseItemIngot{

	private final ItemStack outputIngot;
	private int tickCounter = 0;
	private final int tickCounterMax = 200;
	private final int mTier;

	private IIcon base;
	private IIcon overlay;

	public BaseItemIngotHot(final Material material) {
		super(material, ComponentTypes.HOTINGOT);
		this.setTextureName(CORE.MODID + ":" + "itemIngotHot");
		this.outputIngot = material.getIngot(1);
		this.mTier = material.vTier;
		this.generateRecipe();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return super.getItemStackDisplayName(p_77653_1_);
		//return ("Hot "+this.materialName+ " Ingot");
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(225, 225, 225);
	}

	private void generateRecipe(){
		Logger.WARNING("Adding Vacuum Freezer recipe for a Hot Ingot of "+this.materialName+".");
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getSimpleStack(this), this.outputIngot.copy(), 60*this.mTier);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (!world.isRemote){
			if(this.tickCounter < this.tickCounterMax){
				this.tickCounter++;
			}
			else if(this.tickCounter == this.tickCounterMax){
				entityHolding.attackEntityFrom(DamageSource.onFire, 1);
				this.tickCounter = 0;
			}
		}
		super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		if (CORE.ConfigSwitches.useGregtechTextures){
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void registerIcons(final IIconRegister i) {

		if (CORE.ConfigSwitches.useGregtechTextures){
			this.base = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "ingotHot");
			this.overlay = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "ingotHot_OVERLAY");
		}
		else {
			this.base = i.registerIcon(CORE.MODID + ":" + "item"+BaseItemComponent.ComponentTypes.HOTINGOT.getComponent());
			//this.overlay = i.registerIcon(CORE.MODID + ":" + "item"+BaseItemComponent.ComponentTypes.HOTINGOT.getComponent()+"_Overlay");
		}
		//this.overlay = cellMaterial.getFluid(1000).getFluid().get
	}	

	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		if(pass == 0 && CORE.ConfigSwitches.useGregtechTextures) {
			return this.base;
		}
		else if(pass == 1 && CORE.ConfigSwitches.useGregtechTextures) {
			return this.overlay;
		}
		else {
			return this.overlay;
		}
	}


}
