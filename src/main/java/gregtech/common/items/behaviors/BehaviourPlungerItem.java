package gregtech.common.items.behaviors;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityItemPipe;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class BehaviourPlungerItem extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.plunger.item", "Clears Items from Pipes");

    public BehaviourPlungerItem(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aTileEntity instanceof IGregTechTileEntity gtTE) {
            IMetaTileEntity tMetaTileEntity = gtTE.getMetaTileEntity();
            if ((tMetaTileEntity instanceof IMetaTileEntityItemPipe)) {
                for (IMetaTileEntityItemPipe tTileEntity : GTUtility
                    .sortMapByValuesAcending(
                        IMetaTileEntityItemPipe.Util
                            .scanPipes((IMetaTileEntityItemPipe) tMetaTileEntity, new HashMap<>(), 0L, false, true))
                    .keySet()) {
                    int i = 0;
                    for (int j = tTileEntity.getSizeInventory(); i < j; i++) {
                        if (tTileEntity.isValidSlot(i)) {
                            if ((tTileEntity.getStackInSlot(i) != null) && ((aPlayer.capabilities.isCreativeMode)
                                || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts)))) {
                                final ItemStack tStack = tTileEntity.decrStackSize(i, 64);
                                if (tStack != null) {
                                    final EntityItem tEntity = new EntityItem(
                                        aWorld,
                                        gtTE.getOffsetX(side, 1) + 0.5D,
                                        gtTE.getOffsetY(side, 1) + 0.5D,
                                        gtTE.getOffsetZ(side, 1) + 0.5D,
                                        tStack);
                                    tEntity.motionX = 0.0D;
                                    tEntity.motionY = 0.0D;
                                    tEntity.motionZ = 0.0D;
                                    aWorld.spawnEntityInWorld(tEntity);
                                    GTUtility.sendSoundToPlayers(
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
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
