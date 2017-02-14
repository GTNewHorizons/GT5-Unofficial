package gtPlusPlus.core.item.base.cell;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseItemPlasmaCell extends BaseItemComponent{

	private IIcon base;
	private IIcon overlay;
	ComponentTypes PlasmaCell = ComponentTypes.PLASMACELL;
	private int tickCounter = 0;
	private int tickCounterMax = 200;
	private short[] fluidColour;

	public BaseItemPlasmaCell(Material material) {
		super(material, ComponentTypes.PLASMACELL);	
		fluidColour = (short[]) ((material == null) ? extraData : material.getRGBA());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override 
	public void registerIcons(IIconRegister i) {
		this.base = i.registerIcon(CORE.MODID + ":" + "item"+PlasmaCell.getComponent());	
		this.overlay = i.registerIcon(CORE.MODID + ":" + "item"+PlasmaCell.getComponent()+"_Overlay");
		//this.overlay = cellMaterial.getFluid(1000).getFluid().get
	}


	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if (renderPass == 0){
			return Utils.rgbtoHexValue(230, 230, 230);			
		}	
		return componentColour;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		if(pass == 0) {
			return this.base;
		}
		return this.overlay;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack cell) {
		return materialName+" Plasma Cell";
	}

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		if (componentMaterial != null){
			if (!world.isRemote){
				if(tickCounter  < tickCounterMax ){
					tickCounter++;
				}	
				else if(tickCounter >= tickCounterMax){
					entityHolding.attackEntityFrom(DamageSource.onFire, 2);
					tickCounter = 0;
				}
			}
		}
		super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}

}
