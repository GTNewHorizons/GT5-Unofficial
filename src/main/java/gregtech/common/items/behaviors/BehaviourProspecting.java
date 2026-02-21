package gregtech.common.items.behaviors;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
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
                GTUtility.trans("100", "This is ") + mat.getLocalizedName() + GTUtility.trans("101", " Ore."));
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            return true;
        }

        Materials oreMat = getOreMaterial(aBlock, aMeta);

        if (oreMat != null) {
            GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("100", "This is ") + oreMat.mDefaultLocalName + GTUtility.trans("101", " Ore."));
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            return true;
        }

        if (aBlock.getMaterial() == Material.rock || aBlock.getMaterial() == Material.ground
            || GTUtility.isOre(aBlock, aMeta)) {
            if (!GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) return false;

            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            int toolQuality = aItem instanceof MetaGeneratedTool ? aItem.getHarvestLevel(aStack, "") : 0;
            int tX = aX, tY = aY, tZ = aZ;
            for (int i = 0, j = 6 + toolQuality; i < j; i++) {
                tX -= side.offsetX;
                tY -= side.offsetY;
                tZ -= side.offsetZ;

                Block tBlock = aWorld.getBlock(tX, tY, tZ);

                if (tBlock == Blocks.lava || tBlock == Blocks.flowing_lava) {
                    GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("102", "There is Lava behind this Rock."));
                    break;
                }
                if (tBlock instanceof BlockLiquid || tBlock instanceof IFluidBlock) {
                    GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("103", "There is a Liquid behind this Rock."));
                    break;
                }
                if (tBlock == Blocks.monster_egg || !GTUtility.hasBlockHitBox(aWorld, tX, tY, tZ)) {
                    GTUtility
                        .sendChatToPlayer(aPlayer, GTUtility.trans("104", "There is an Air Pocket behind this Rock."));
                    break;
                }
                if (tBlock != aBlock) {
                    if (i < 4) GTUtility
                        .sendChatToPlayer(aPlayer, GTUtility.trans("105", "Material is changing behind this Rock."));
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
                        GTUtility.trans("106", "Found traces of ") + mat.getLocalizedName()
                            + GTUtility.trans("101", " Ore."));
                    return true;
                }

                oreMat = getOreMaterial(tBlock, tMeta);

                if (oreMat != null) {
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        GTUtility.trans("106", "Found traces of ") + oreMat.mDefaultLocalName
                            + GTUtility.trans("101", " Ore."));
                    return true;
                }
            }

            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("107", "No Ores found."));
            return true;
        }

        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(StatCollector.translateToLocal("gt.behaviour.prospecting"));
        return aList;
    }
}
