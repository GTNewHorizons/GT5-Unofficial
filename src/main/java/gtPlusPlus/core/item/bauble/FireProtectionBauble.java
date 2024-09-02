package gtPlusPlus.core.item.bauble;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.PreloaderCore;

public class FireProtectionBauble extends BaseBauble {

    private static Field isImmuneToFire;

    static {
        isImmuneToFire = ReflectionUtils
            .getField(Entity.class, !PreloaderCore.DEV_ENVIRONMENT ? "func_70045_F" : "isImmuneToFire");
    }

    public static boolean fireImmune(Entity aEntity) {
        return aEntity.isImmuneToFire();
    }

    public static boolean setEntityImmuneToFire(Entity aEntity, boolean aImmune) {
        try {
            return ReflectionUtils.setField(aEntity, isImmuneToFire, aImmune);
        } catch (Throwable t) {}
        return false;
    }

    public FireProtectionBauble() {
        super(BaubleType.RING);
        String aUnlocalName = "GTPP.bauble.fireprotection.0" + ".name";
        this.setUnlocalizedName(aUnlocalName);
        this.setTextureName(GTPlusPlus.ID + ":" + getTextureNameForBauble());
        this.setMaxDamage(100);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        if (GameRegistry.findItem(GTPlusPlus.ID, aUnlocalName) == null) {
            GameRegistry.registerItem(this, aUnlocalName);
        }
    }

    @Override
    public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_,
        final boolean p_77663_5_) {
        super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack p_77653_1_) {
        return (EnumChatFormatting.DARK_RED + super.getItemStackDisplayName(p_77653_1_) + EnumChatFormatting.GRAY);
    }

    @Override
    public boolean showDurabilityBar(final ItemStack stack) {
        return false;
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        super.addInformation(stack, aPlayer, list, bool);
    }

    @Override
    public boolean canEquip(final ItemStack arg0, final EntityLivingBase arg1) {
        return true;
    }

    @Override
    public boolean canUnequip(final ItemStack arg0, final EntityLivingBase arg1) {
        return true;
    }

    @Override
    public void onEquipped(final ItemStack arg0, final EntityLivingBase aPlayer) {}

    @Override
    public void onUnequipped(final ItemStack arg0, final EntityLivingBase aPlayer) {
        if (!aPlayer.worldObj.isRemote) {
            if (aPlayer instanceof EntityPlayer bPlayer) {
                if (bPlayer.isPotionActive(Potion.fireResistance)) {
                    bPlayer.removePotionEffect(Potion.fireResistance.id);
                }
                setEntityImmuneToFire(bPlayer, false);
            }
        }
    }

    @Override
    public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase aPlayer) {
        if (!aPlayer.worldObj.isRemote) {
            if (aPlayer instanceof EntityPlayer bPlayer) {
                if (!fireImmune(bPlayer)) {
                    setEntityImmuneToFire(bPlayer, true);
                }
                if (!bPlayer.isPotionActive(Potion.fireResistance)) {
                    bPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 100, 4));
                }
            }
        }
    }

    public String getTextureNameForBauble() {
        return "baubles/itemFireProtectGlovesBetter";
    }
}
