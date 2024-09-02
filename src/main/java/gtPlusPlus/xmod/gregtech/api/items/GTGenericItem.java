package gtPlusPlus.xmod.gregtech.api.items;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
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
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IProjectileItem;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;

/**
 * Extended by most Items, also used as a fallback Item, to prevent the accidental deletion when Errors occur.
 */
public class GTGenericItem extends Item implements IProjectileItem {

    private final String mName, mTooltip;
    protected IIcon mIcon;

    public GTGenericItem(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        this(aUnlocalized, aEnglish, aEnglishTooltip, true);
    }

    public GTGenericItem(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip,
        final boolean aWriteToolTipIntoLangFile) {
        super();
        this.mName = aUnlocalized;
        GTLanguageManager.addStringLocalization(this.mName + ".name", aEnglish);
        if (GTUtility.isStringValid(aEnglishTooltip)) {
            GTLanguageManager.addStringLocalization(
                this.mTooltip = this.mName + ".tooltip_main",
                aEnglishTooltip,
                aWriteToolTipIntoLangFile);
        } else {
            this.mTooltip = null;
        }
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerItem(this, this.mName, GTPlusPlus.ID);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new GT_Item_Dispense());
    }

    @Override
    public final Item setUnlocalizedName(final String aName) {
        return this;
    }

    @Override
    public final String getUnlocalizedName() {
        return this.mName;
    }

    @Override
    public String getUnlocalizedName(final ItemStack aStack) {
        return this.getHasSubtypes() ? this.mName + "." + this.getDamage(aStack) : this.mName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister aIconRegister) {
        this.mIcon = aIconRegister.registerIcon(GTPlusPlus.ID + ":" + this.mName);
    }

    @Override
    public boolean doesSneakBypassUse(final World aWorld, final int aX, final int aY, final int aZ,
        final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public IIcon getIconFromDamage(final int par1) {
        return this.mIcon;
    }

    public int getTier(final ItemStack aStack) {
        return 0;
    }

    @Override
    public void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, final List aList,
        final boolean aF3_H) {
        if ((this.getMaxDamage() > 0) && !this.getHasSubtypes()) {
            aList.add((aStack.getMaxDamage() - this.getDamage(aStack)) + " / " + aStack.getMaxDamage());
        }
        if (this.mTooltip != null) {
            aList.add(GTLanguageManager.getTranslation(this.mTooltip));
        }
        if (GTModHandler.isElectricItem(aStack)) {
            aList.add("Tier: " + this.getTier(aStack));
        }
        this.addAdditionalToolTips(aList, aStack);
    }

    protected void addAdditionalToolTips(final List aList, final ItemStack aStack) {
        //
    }

    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        this.isItemStackUsable(aStack);
    }

    public boolean isItemStackUsable(final ItemStack aStack) {
        return true;
    }

    public ItemStack onDispense(final IBlockSource aSource, final ItemStack aStack) {
        final EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
        final IPosition iposition = BlockDispenser.func_149939_a(aSource);
        final ItemStack itemstack1 = aStack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
        return aStack;
    }

    @Override
    public EntityArrow getProjectile(final SubTag aProjectileType, final ItemStack aStack, final World aWorld,
        final double aX, final double aY, final double aZ) {
        return null;
    }

    @Override
    public EntityArrow getProjectile(final SubTag aProjectileType, final ItemStack aStack, final World aWorld,
        final EntityLivingBase aEntity, final float aSpeed) {
        return null;
    }

    @Override
    public boolean hasProjectile(final SubTag aProjectileType, final ItemStack aStack) {
        return false;
    }

    @Override
    public ItemStack getContainerItem(final ItemStack aStack) {
        return null;
    }

    @Override
    public boolean hasContainerItem(final ItemStack aStack) {
        return this.getContainerItem(aStack) != null;
    }

    public static class GT_Item_Dispense extends BehaviorProjectileDispense {

        @Override
        public ItemStack dispenseStack(final IBlockSource aSource, final ItemStack aStack) {
            return ((GTGenericItem) aStack.getItem()).onDispense(aSource, aStack);
        }

        @Override
        protected IProjectile getProjectileEntity(final World aWorld, final IPosition aPosition) {
            return null;
        }
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
        if (stack.getDisplayName()
            .contains("LuV")) {
            HEX_OxFFFFFF = 0xffffcc;
        } else if (stack.getDisplayName()
            .contains("ZPM")) {
                HEX_OxFFFFFF = 0xace600;
            } else if (stack.getDisplayName()
                .contains("UV")) {
                    HEX_OxFFFFFF = 0xffff00;
                } else if (stack.getDisplayName()
                    .contains("MAX")) {
                        HEX_OxFFFFFF = 0xff0000;
                    } else if (stack.getDisplayName()
                        .contains("Sodium")) {
                            HEX_OxFFFFFF = Utils.rgbtoHexValue(0, 0, 150);
                        } else if (stack.getDisplayName()
                            .contains("Cadmium")) {
                                HEX_OxFFFFFF = Utils.rgbtoHexValue(50, 50, 60);
                            } else if (stack.getDisplayName()
                                .contains("Lithium")) {
                                    HEX_OxFFFFFF = Utils.rgbtoHexValue(225, 220, 255);
                                } else {
                                    HEX_OxFFFFFF = 0xffffff;
                                }
        return HEX_OxFFFFFF;
    }
}
