package kekztech.common.items;

import java.util.Collections;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTUtility;
import kekztech.KekzCore;

public class ErrorItem extends Item {

    private static final ErrorItem INSTANCE = new ErrorItem();

    private ErrorItem() {}

    public static ErrorItem getInstance() {
        return INSTANCE;
    }

    public void registerItem() {
        super.setHasSubtypes(false);
        final String unlocalizedName = "kekztech_error_item";
        super.setUnlocalizedName(unlocalizedName);
        super.setCreativeTab(CreativeTabs.tabMisc);
        super.setMaxStackSize(64);
        super.setTextureName(KekzCore.MODID + ":" + "Error");
        GameRegistry.registerItem(getInstance(), unlocalizedName);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        Collections.addAll(list, GTUtility.breakLines(StatCollector.translateToLocal("tooltip.kekztech.wrong")));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        player.swingItem();
        return item;
    }
}
