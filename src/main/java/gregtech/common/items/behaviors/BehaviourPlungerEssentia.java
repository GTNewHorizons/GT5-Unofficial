package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import thaumcraft.api.aspects.IEssentiaTransport;

public class BehaviourPlungerEssentia extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.plunger.essentia", "Clears Essentia from Containers and Tubes");

    public BehaviourPlungerEssentia(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (((aTileEntity instanceof IEssentiaTransport))
            && ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts)))) {
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
            for (ForgeDirection tDirection : ForgeDirection.VALID_DIRECTIONS) {
                ((IEssentiaTransport) aTileEntity).takeEssentia(
                    ((IEssentiaTransport) aTileEntity).getEssentiaType(tDirection),
                    ((IEssentiaTransport) aTileEntity).getEssentiaAmount(tDirection),
                    tDirection);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
