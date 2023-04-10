package gregtech.common.items.behaviors;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityItemPipe;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class Behaviour_Plunger_Item extends Behaviour_None {

    private final int mCosts;
    private final String mTooltip = GT_LanguageManager
        .addStringLocalization("gt.behaviour.plunger.item", "Clears Items from Pipes");

    public Behaviour_Plunger_Item(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((aTileEntity instanceof IGregTechTileEntity)) {
            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aTileEntity).getMetaTileEntity();
            if ((tMetaTileEntity instanceof IMetaTileEntityItemPipe)) {
                for (IMetaTileEntityItemPipe tTileEntity : GT_Utility
                    .sortMapByValuesAcending(
                        IMetaTileEntityItemPipe.Util
                            .scanPipes((IMetaTileEntityItemPipe) tMetaTileEntity, new HashMap<>(), 0L, false, true))
                    .keySet()) {
                    int i = 0;
                    for (int j = tTileEntity.getSizeInventory(); i < j; i++) {
                        if (tTileEntity.isValidSlot(i)) {
                            if ((tTileEntity.getStackInSlot(i) != null) && ((aPlayer.capabilities.isCreativeMode)
                                || (((GT_MetaGenerated_Tool) aItem).doDamage(aStack, this.mCosts)))) {
                                ItemStack tStack = tTileEntity.decrStackSize(i, 64);
                                if (tStack != null) {
                                    EntityItem tEntity = new EntityItem(
                                        aWorld,
                                        ((IGregTechTileEntity) aTileEntity).getOffsetX((byte) aSide, 1) + 0.5D,
                                        ((IGregTechTileEntity) aTileEntity).getOffsetY((byte) aSide, 1) + 0.5D,
                                        ((IGregTechTileEntity) aTileEntity).getOffsetZ((byte) aSide, 1) + 0.5D,
                                        tStack);
                                    tEntity.motionX = 0.0D;
                                    tEntity.motionY = 0.0D;
                                    tEntity.motionZ = 0.0D;
                                    aWorld.spawnEntityInWorld(tEntity);
                                    GT_Utility.sendSoundToPlayers(
                                        aWorld,
                                        SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE,
                                        1.0F,
                                        -1.0F,
                                        aX,
                                        aY,
                                        aZ);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
