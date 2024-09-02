package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import cpw.mods.fml.common.eventhandler.Event;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder;

public class BehaviourHoe extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager.addStringLocalization("gt.behaviour.hoe", "Can till Dirt");

    public BehaviourHoe(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        if (!aPlayer.canPlayerEdit(aX, aY, aZ, ordinalSide, aStack)) {
            return false;
        }
        UseHoeEvent event = new UseHoeEvent(aPlayer, aStack, aWorld, aX, aY, aZ);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        if (event.getResult() == Event.Result.ALLOW) {
            if (!aPlayer.capabilities.isCreativeMode) {
                ((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts);
            }
            return true;
        }
        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if ((ordinalSide != 0) && (GTUtility.isBlockAir(aWorld, aX, aY + 1, aZ))
            && ((aBlock == Blocks.grass) || (aBlock == Blocks.dirt))) {
            new WorldSpawnedEventBuilder.SoundEventBuilder()
                .setVolume((Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F)
                .setPitch(Blocks.farmland.stepSound.getPitch() * 0.8F)
                .setIdentifier(Blocks.farmland.stepSound.getStepResourcePath())
                .setPosition(aX + 0.5F, aY + 0.5F, aZ + 0.5F)
                .setWorld(aWorld)
                .run();
            if (aWorld.isRemote) {
                return true;
            }
            aWorld.setBlock(aX, aY, aZ, Blocks.farmland);
            if (!aPlayer.capabilities.isCreativeMode) {
                ((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts);
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
