package gregtech.common.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.GTMod;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.api.util.GTUtility;

public class ToolJackHammer extends ToolDrillLV {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 400;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 400;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 3200;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 800;
    }

    @Override
    public int getBaseQuality() {
        return 1;
    }

    @Override
    public float getBaseDamage() {
        return 3.0F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 12.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 2.0F;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "pickaxe") //
            || GTToolHarvestHelper.isAppropriateMaterial(
                aBlock, //
                Material.rock, //
                Material.glass, //
                Material.ice, //
                Material.packedIce //
            );
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
        int aY, int aZ, int aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        int rConversions = 0;
        GTRecipe tRecipe = RecipeMaps.hammerRecipes.findRecipeQuery()
            .items(new ItemStack(aBlock, 1, aMetaData))
            .notUnificated(true)
            .find();
        if ((tRecipe == null) || (aBlock.hasTileEntity(aMetaData))) {
            for (ItemStack tDrop : aDrops) {
                tRecipe = RecipeMaps.hammerRecipes.findRecipeQuery()
                    .items(GTUtility.copyAmount(1, tDrop))
                    .notUnificated(true)
                    .find();
                if (tRecipe != null) {
                    ItemStack tHammeringOutput = tRecipe.getOutput(0);
                    if (tHammeringOutput != null) {
                        rConversions += tDrop.stackSize;
                        tDrop.stackSize *= tHammeringOutput.stackSize;
                        tHammeringOutput.stackSize = tDrop.stackSize;
                        GTUtility.setStack(tDrop, tHammeringOutput);
                    }
                }
            }
        } else {
            aDrops.clear();
            aDrops.add(tRecipe.getOutput(0));
            rConversions++;
        }
        return rConversions;
    }

    @Override
    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        try {
            GTMod.achievements.issueAchievement(aPlayer, "hammertime");
        } catch (Exception ignored) {}
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.JACKHAMMER : null;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " has been jackhammered into pieces by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
