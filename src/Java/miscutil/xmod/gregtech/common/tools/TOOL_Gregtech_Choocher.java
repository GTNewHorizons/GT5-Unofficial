package miscutil.xmod.gregtech.common.tools;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.Arrays;
import java.util.List;

import miscutil.xmod.gregtech.api.items.Gregtech_MetaTool;
import miscutil.xmod.gregtech.api.items.types.ToolType_HardHammer;
import miscutil.xmod.gregtech.api.items.types.ToolType_Wrench;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent;

public class TOOL_Gregtech_Choocher
        extends TOOL_Gregtech_Base {
	
	public static final List<String> mEffectiveList = Arrays.asList(new String[]{EntityIronGolem.class.getName(), "EntityTowerGuardian"});
	
	
	@Override
	public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer) {
		String tName = aEntity.getClass().getName();
		tName = tName.substring(tName.lastIndexOf(".") + 1);
		return (mEffectiveList.contains(tName)) || (tName.contains("Golem")) ? aOriginalDamage * 2.0F : aOriginalDamage;
	}	
	
    @Override
	public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
	public int getToolDamagePerDropConversion() {
        return 100;
    }

    @Override
	public int getToolDamagePerContainerCraft() {
        return 400;
    }

    @Override
	public int getToolDamagePerEntityAttack() {
        return 100;
    }

    @Override
	public int getBaseQuality() {
        return 0;
    }

    @Override
	public float getBaseDamage() {
        return 4.0F;
    }

    @Override
	public float getSpeedMultiplier() {
        return 0.85F;
    }

    @Override
	public float getMaxDurabilityMultiplier() {
        return 1.2F;
    }

    @Override
	public String getCraftingSound() {
        return null;
    }

    @Override
	public String getEntityHitSound() {
        return null;
    }

    @Override
	public String getBreakingSound() {
        return (String) GregTech_API.sSoundList.get(Integer.valueOf(0));
    }

    @Override
	public String getMiningSound() {
        return null;
    }

    @Override
	public boolean canBlock() {
        return true;
    }
    
    @Override
	public boolean isWrench(){
    	return true;
    }

    @Override
	public boolean isCrowbar() {
        return false;
    }

    @Override
	public boolean isWeapon() {
        return true;
    }

    @Override
	public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        String tTool = aBlock.getHarvestTool(aMetaData);
        return ((tTool != null) && ((tTool.equals("sword")) || (tTool.equals("wrench")) || (tTool.equals("hammer")) || (tTool.equals("pickaxe")))) || (aBlock.getMaterial() == Material.rock) || (aBlock.getMaterial() == Material.glass) || (aBlock.getMaterial() == Material.piston) || (aBlock == Blocks.hopper) || (aBlock == Blocks.dispenser) || (aBlock == Blocks.dropper) || (aBlock.getMaterial() == Material.ice) || (aBlock.getMaterial() == Material.packedIce) || (GT_Recipe.GT_Recipe_Map.sHammerRecipes.containsInput(new ItemStack(aBlock, 1, aMetaData))) || (aBlock.getMaterial() == Material.sand) || (aBlock.getMaterial() == Material.grass) || (aBlock.getMaterial() == Material.ground) || (aBlock.getMaterial() == Material.snow) || (aBlock.getMaterial() == Material.clay) || (aBlock.getMaterial() == Material.leaves) || (aBlock.getMaterial() == Material.vine) || (aBlock.getMaterial() == Material.wood) || (aBlock.getMaterial() == Material.cactus) || (aBlock.getMaterial() == Material.circuits) || (aBlock.getMaterial() == Material.gourd) || (aBlock.getMaterial() == Material.web) || (aBlock.getMaterial() == Material.cloth) || (aBlock.getMaterial() == Material.carpet) || (aBlock.getMaterial() == Material.plants) || (aBlock.getMaterial() == Material.cake) || (aBlock.getMaterial() == Material.tnt) || (aBlock.getMaterial() == Material.sponge);
    }
    
    @Override
	public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        int rConversions = 0;
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sHammerRecipes.findRecipe(null, true, 2147483647L, null, new ItemStack[]{new ItemStack(aBlock, 1, aMetaData)});
        if ((tRecipe == null) || (aBlock.hasTileEntity(aMetaData))) {
            for (ItemStack tDrop : aDrops) {
                tRecipe = GT_Recipe.GT_Recipe_Map.sHammerRecipes.findRecipe(null, true, 2147483647L, null, new ItemStack[]{GT_Utility.copyAmount(1L, new Object[]{tDrop})});
                if (tRecipe != null) {
                    ItemStack tHammeringOutput = tRecipe.getOutput(0);
                    if (tHammeringOutput != null) {
                        rConversions += tDrop.stackSize;
                        tDrop.stackSize *= tHammeringOutput.stackSize;
                        tHammeringOutput.stackSize = tDrop.stackSize;
                        GT_Utility.setStack(tDrop, tHammeringOutput);
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
	public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
	public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
		return aIsToolHead ? Textures.ItemIcons.WIRE_CUTTER : Textures.ItemIcons.VOID;
	}

    
/*    @Override
	public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
		return aIsToolHead ? Textures.class..ItemIcons.SKOOKUMCHOOCHER : null;
	}
*/
    //
    
    @Override
	public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
		return aIsToolHead ? Gregtech_MetaTool.getPrimaryMaterial(aStack).mRGBa : null;
	}

    @Override
	public void onStatsAddedToTool(Gregtech_MetaTool aItem, int aID) {
        aItem.addItemBehavior(aID, new ToolType_Wrench(150));
        aItem.addItemBehavior(aID, new ToolType_HardHammer(1, 1250));
    }

    @Override
	public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        aPlayer.triggerAchievement(AchievementList.buildSword);
        try {
        	GT_Mod.instance.achievements.issueAchievement(aPlayer, "tools");
            GT_Mod.instance.achievements.issueAchievement(aPlayer, "unitool");
        } catch (Exception e) {
        }
    }

    @Override
	public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " has been Choochered by " + EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE);
    }
}
