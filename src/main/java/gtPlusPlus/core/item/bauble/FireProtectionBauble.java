package gtPlusPlus.core.item.bauble;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.mixin.interfaces.accessors.EntityAccessor;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class FireProtectionBauble extends BaseBauble {

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
    public void onUnequipped(final ItemStack arg0, final EntityLivingBase entity) {
        if (!entity.worldObj.isRemote) {
            if (entity instanceof EntityPlayer player) {
                ((EntityAccessor) player).gt5u$setImmuneToFire(false);
            }
        }
    }

    @Override
    public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase entity) {
        if (!entity.worldObj.isRemote) {
            if (entity instanceof EntityPlayer player) {
                ((EntityAccessor) player).gt5u$setImmuneToFire(true);
            }
        }
    }

    public String getTextureNameForBauble() {
        return "baubles/itemFireProtectGlovesBetter";
    }
}
