package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.constructableTT;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

import static com.github.technus.tectech.auxiliary.Reference.MODID;

/**
 * Created by Tec on 15.03.2017.
 */
public class DebugBuilder extends Item {
    public static DebugBuilder INSTANCE;

    DebugBuilder() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("em.debugBuilder");
        setTextureName(MODID + ":itemDebugBuilder");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            if (tTileEntity != null && tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE != null && metaTE instanceof constructableTT) {
                    ((constructableTT) metaTE).construct(aStack.stackSize);
                    return true;
                }
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.tecMark);
        aList.add("Constructs Multiblocks");
        aList.add(EnumChatFormatting.BLUE + "Quantity Matters");
    }

    public static void run() {
        INSTANCE = new DebugBuilder();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
