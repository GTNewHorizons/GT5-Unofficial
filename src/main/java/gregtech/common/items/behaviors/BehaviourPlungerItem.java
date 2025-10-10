package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
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
    public boolean onItemUseFirst(MetaBaseItem item, ItemStack stack, EntityPlayer player, World world, int x, int y,
        int z, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return false;
        }

        if (world.getTileEntity(x, y, z) instanceof IGregTechTileEntity igte) {
            if (igte.getMetaTileEntity() instanceof MTEItemPipe itemPipe) {
                ((MetaGeneratedTool) item).doDamage(stack, this.mCosts);
                GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_PLUNGER, 1.0F, -1.0F, x, y, z);

                itemPipe.onPlungerRightClick(side, player);
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
