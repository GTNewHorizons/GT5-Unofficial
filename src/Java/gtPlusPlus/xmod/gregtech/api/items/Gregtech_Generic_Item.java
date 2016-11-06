package gtPlusPlus.xmod.gregtech.api.items;

import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IProjectileItem;
import gregtech.api.util.*;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

import java.util.List;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Extended by most Items, also used as a fallback Item, to prevent the accidental deletion when Errors occur.
 */
public class Gregtech_Generic_Item extends Item implements IProjectileItem {
    private final String mName, mTooltip;
    protected IIcon mIcon;

    public Gregtech_Generic_Item(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        this(aUnlocalized, aEnglish, aEnglishTooltip, true);
    }

    public Gregtech_Generic_Item(String aUnlocalized, String aEnglish, String aEnglishTooltip, boolean aWriteToolTipIntoLangFile) {
        super();
        mName = aUnlocalized;
        GT_LanguageManager.addStringLocalization(mName + ".name", aEnglish);
        if (GT_Utility.isStringValid(aEnglishTooltip))
            GT_LanguageManager.addStringLocalization(mTooltip = mName + ".tooltip_main", aEnglishTooltip, aWriteToolTipIntoLangFile);
        else mTooltip = null;
        setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerItem(this, mName, CORE.MODID);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new GT_Item_Dispense());
    }

    @Override
    public final Item setUnlocalizedName(String aName) {
        return this;
    }

    @Override
    public final String getUnlocalizedName() {
        return mName;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getHasSubtypes() ? mName + "." + getDamage(aStack) : mName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        mIcon = aIconRegister.registerIcon(CORE.MODID+":"+mName);
    }

    @Override
    public boolean doesSneakBypassUse(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        return mIcon;
    }

    public int getTier(ItemStack aStack) {
        return 0;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if (getMaxDamage() > 0 && !getHasSubtypes())
            aList.add((aStack.getMaxDamage() - getDamage(aStack)) + " / " + aStack.getMaxDamage());
        if (mTooltip != null) aList.add(GT_LanguageManager.getTranslation(mTooltip));
        if (GT_ModHandler.isElectricItem(aStack)) aList.add("Tier: " + getTier(aStack));
        addAdditionalToolTips(aList, aStack);
    }

    protected void addAdditionalToolTips(List aList, ItemStack aStack) {
        //
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        isItemStackUsable(aStack);
    }

    public boolean isItemStackUsable(ItemStack aStack) {
        return true;
    }

    public ItemStack onDispense(IBlockSource aSource, ItemStack aStack) {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
        IPosition iposition = BlockDispenser.func_149939_a(aSource);
        ItemStack itemstack1 = aStack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
        return aStack;
    }

    @Override
    public EntityArrow getProjectile(SubTag aProjectileType, ItemStack aStack, World aWorld, double aX, double aY, double aZ) {
        return null;
    }

    @Override
    public EntityArrow getProjectile(SubTag aProjectileType, ItemStack aStack, World aWorld, EntityLivingBase aEntity, float aSpeed) {
        return null;
    }

    @Override
    public boolean hasProjectile(SubTag aProjectileType, ItemStack aStack) {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        return null;
    }

    @Override
    public boolean hasContainerItem(ItemStack aStack) {
        return getContainerItem(aStack) != null;
    }

    public static class GT_Item_Dispense extends BehaviorProjectileDispense {
        @Override
        public ItemStack dispenseStack(IBlockSource aSource, ItemStack aStack) {
            return ((Gregtech_Generic_Item) aStack.getItem()).onDispense(aSource, aStack);
        }

        @Override
        protected IProjectile getProjectileEntity(World aWorld, IPosition aPosition) {
            return null;
        }
    }
    
    @Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {	
    	if (stack.getDisplayName().contains("LuV")){
    		HEX_OxFFFFFF = 0xffffcc;
    	}
		else if (stack.getDisplayName().contains("ZPM")){
			HEX_OxFFFFFF = 0xace600;
		}
		else if (stack.getDisplayName().contains("UV")){
			HEX_OxFFFFFF = 0xffff00;
		}
		else if (stack.getDisplayName().contains("MAX")){
			HEX_OxFFFFFF = 0xff0000;
		}
		else if (stack.getDisplayName().contains("Sodium")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(0, 0, 150);
		}
		else if (stack.getDisplayName().contains("Cadmium")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(50, 50, 60);
		}
		else if (stack.getDisplayName().contains("Lithium")){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(225, 220, 255);
		}
		else {
			HEX_OxFFFFFF = 0xffffff;
		}		
		return HEX_OxFFFFFF;
	}
}