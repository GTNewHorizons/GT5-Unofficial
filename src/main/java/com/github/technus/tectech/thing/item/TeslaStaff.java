package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.thing.CustomItemList.teslaStaff;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_Utility;

public final class TeslaStaff extends Item {

    public static TeslaStaff INSTANCE;

    private TeslaStaff() {
        setUnlocalizedName("tm.teslaStaff");
        setTextureName(MODID + ":itemTeslaStaff");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.tm.teslaStaff.desc"));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer aPlayer, Entity entity) {
        GT_Utility.sendChatToPlayer(aPlayer, "Zapperoni!");
        if (!(aPlayer instanceof EntityPlayerMP)) {
            double aX = aPlayer.posX;
            double aY = aPlayer.posY;
            double aZ = aPlayer.posZ;
            GT_Utility.doSoundAtClient(Reference.MODID + ":fx_scan", 1, 1.0F, aX, aY, aZ);
        }
        return false;
    }

    public static void run() {
        INSTANCE = new TeslaStaff();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        teslaStaff.set(INSTANCE);
    }
}
