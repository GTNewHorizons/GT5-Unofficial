package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.IFrontRotation;
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
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

import static com.github.technus.tectech.Reference.MODID;

/**
 * Created by Tec on 15.03.2017.
 */
public final class FrontRotationTriggerItem extends Item {
    public static FrontRotationTriggerItem INSTANCE;

    private FrontRotationTriggerItem() {
        setUnlocalizedName("em.frontRotate");
        setTextureName(MODID + ":itemFrontRotate");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if(tTileEntity==null || aPlayer instanceof FakePlayer) {
            return aPlayer instanceof EntityPlayerMP;
        }
        if (aPlayer instanceof EntityPlayerMP) {
            if (tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE instanceof IFrontRotation) {
                    ((IFrontRotation) metaTE).rotateAroundFrontPlane(!aPlayer.isSneaking());
                    return true;
                }
            } else if (tTileEntity instanceof IFrontRotation) {
                ((IFrontRotation) tTileEntity).rotateAroundFrontPlane(!aPlayer.isSneaking());
                return true;
            }
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_GENERAL);
        aList.add("Triggers Front Rotation Interface");
        aList.add(EnumChatFormatting.BLUE + "Rotates only the front panel,");
        aList.add(EnumChatFormatting.BLUE + "which allows structure rotation.");
    }

    public static void run() {
        INSTANCE = new FrontRotationTriggerItem();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
