package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import thaumcraft.api.aspects.IEssentiaTransport;
import gregtech.api.enums.SoundResource;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class Behaviour_Plunger_Essentia extends Behaviour_None {

    private final int mCosts;
    private final String mTooltip = GT_LanguageManager
        .addStringLocalization("gt.behaviour.plunger.essentia", "Clears Essentia from Containers and Tubes");

    public Behaviour_Plunger_Essentia(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (((aTileEntity instanceof IEssentiaTransport)) && ((aPlayer.capabilities.isCreativeMode)
            || (((GT_MetaGenerated_Tool) aItem).doDamage(aStack, this.mCosts)))) {
            GT_Utility.sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
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
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
