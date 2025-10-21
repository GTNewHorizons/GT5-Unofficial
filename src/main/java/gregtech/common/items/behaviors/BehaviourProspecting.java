package gregtech.common.items.behaviors;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreManager;

public class BehaviourProspecting extends BehaviourNone {

    private final int mVanillaCosts;
    private final int mEUCosts;
    private final String mTooltip = GTUtility.translate("gt.tooltip.behaviour.prospecting");

    public BehaviourProspecting(int aVanillaCosts, int aEUCosts) {
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
    }

    private static Materials getOreMaterial(Block block, int meta) {
        ItemData association = GTOreDictUnificator.getAssociation(new ItemStack(block, 1, meta));
        if (association == null) return null;
        if (association.mPrefix == null) return null;
        if (association.mMaterial == null) return null;
        if (!association.mPrefix.toString()
            .startsWith("ore")) return null;

        return association.mMaterial.mMaterial;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }

        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock.isAir(aWorld, aX, aY, aZ)) {
            return false;
        }

        int aMeta = aWorld.getBlockMetadata(aX, aY, aZ);

        IOreMaterial mat = OreManager.getMaterial(aBlock, aMeta);
        if (mat != null) {
            GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.translate("gt.chat.interact.desc.this_is") + mat.getLocalizedName()
                    + GTUtility.translate("gt.chat.interact.desc.ore"));
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            return true;
        }

        Materials oreMat = getOreMaterial(aBlock, aMeta);

        if (oreMat != null) {
            GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.translate("gt.chat.interact.desc.this_is") + oreMat.mDefaultLocalName
                    + GTUtility.translate("gt.chat.interact.desc.ore"));
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            return true;
        }

        if (aBlock.getMaterial() == Material.rock || aBlock.getMaterial() == Material.ground
            || GTUtility.isOre(aBlock, aMeta)) {
            if (!GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) return false;

            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
            int toolQuality = aItem instanceof MetaGeneratedTool ? aItem.getHarvestLevel(aStack, "") : 0;
            int tX = aX, tY = aY, tZ = aZ;
            for (int i = 0, j = 6 + toolQuality; i < j; i++) {
                tX -= side.offsetX;
                tY -= side.offsetY;
                tZ -= side.offsetZ;

                Block tBlock = aWorld.getBlock(tX, tY, tZ);

                if (tBlock == Blocks.lava || tBlock == Blocks.flowing_lava) {
                    GTUtility.sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.lava_behind_rock"));
                    break;
                }
                if (tBlock instanceof BlockLiquid || tBlock instanceof IFluidBlock) {
                    GTUtility
                        .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.liquid_behind_rock"));
                    break;
                }
                if (tBlock == Blocks.monster_egg || !GTUtility.hasBlockHitBox(aWorld, tX, tY, tZ)) {
                    GTUtility.sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.air_behind_rock"));
                    break;
                }
                if (tBlock != aBlock) {
                    if (i < 4) GTUtility
                        .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.material_behind_rock"));
                    break;
                }
            }

            final Random tRandom = new XSTR(aX ^ aY ^ aZ ^ side.ordinal());
            for (int i = 0, j = 9 + 2 * toolQuality; i < j; i++) {
                tX = aX - 4 - toolQuality + tRandom.nextInt(j);
                tY = aY - 4 - toolQuality + tRandom.nextInt(j);
                tZ = aZ - 4 - toolQuality + tRandom.nextInt(j);

                Block tBlock = aWorld.getBlock(tX, tY, tZ);
                int tMeta = aWorld.getBlockMetadata(tX, tY, tZ);

                mat = OreManager.getMaterial(tBlock, tMeta);
                if (mat != null) {
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        GTUtility.translate("gt.chat.interact.desc.found_traces") + mat.getLocalizedName()
                            + GTUtility.translate("gt.chat.interact.desc.ore"));
                    return true;
                }

                oreMat = getOreMaterial(tBlock, tMeta);

                if (oreMat != null) {
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        GTUtility.translate("gt.chat.interact.desc.found_traces") + oreMat.mDefaultLocalName
                            + GTUtility.translate("gt.chat.interact.desc.ore"));
                    return true;
                }
            }

            GTUtility.sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.ore_not_found"));
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
