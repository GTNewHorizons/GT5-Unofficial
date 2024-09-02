package gtPlusPlus.core.item.bauble;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.minecraft.NBTUtils;

@Optional.InterfaceList(
    value = { @Optional.Interface(iface = "baubles.api.IBauble", modid = Mods.Names.BAUBLES),
        @Optional.Interface(iface = "baubles.api.BaubleType", modid = Mods.Names.BAUBLES) })
public class BaseBauble extends Item implements IBauble {

    /**
     * Implementation suggestions taken from Botania.
     */
    private BaubleType mThisBauble;

    private List<String> damageNegations = new ArrayList<>();
    Multimap<String, AttributeModifier> attributes = HashMultimap.create();

    public BaseBauble(BaubleType type) {
        this.mThisBauble = type;
        this.setMaxStackSize(1);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
    }

    public List<String> getDamageNegations() {
        return damageNegations;
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        String key = "gtplusplus." + getUnlocalizedName() + ".name";
        if (key.equals(GTLanguageManager.getTranslation(key))) {
            return super.getItemStackDisplayName(tItem).replaceAll(".name", "");
        }
        return GTLanguageManager.getTranslation(key);
    }

    @Override
    public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
        return arg1 instanceof EntityPlayer;
    }

    @Override
    public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
        return arg1 instanceof EntityPlayer;
    }

    @Override
    public BaubleType getBaubleType(ItemStack arg0) {
        return mThisBauble;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            onEquippedOrLoadedIntoWorld(entity);
            setPlayerHashcode(stack, entity.hashCode());
        }
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (getPlayerHashcode(stack) != player.hashCode()) {
            onEquippedOrLoadedIntoWorld(player);
            setPlayerHashcode(stack, player.hashCode());
        }
    }

    public void onEquippedOrLoadedIntoWorld(EntityLivingBase player) {
        attributes.clear();
        player.getAttributeMap()
            .applyAttributeModifiers(attributes);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        attributes.clear();
        player.getAttributeMap()
            .removeAttributeModifiers(attributes);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    public static int getPlayerHashcode(ItemStack stack) {
        return NBTUtils.getInteger(stack, "mPlayerHashcode");
    }

    public static void setPlayerHashcode(ItemStack stack, int hash) {
        NBTUtils.setInteger(stack, "mPlayerHashcode", hash);
    }
}
