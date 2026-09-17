package gregtech.common.items.tools;

import java.util.Random;
import java.util.function.BooleanSupplier;

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
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreManager;

/**
 * The "right click stone to survey for ore" half of a hard hammer, moved out of {@code BehaviourProspecting}.
 */
public final class ProspectingActions {

    private ProspectingActions() {}

    private static Materials getOreMaterial(Block block, int meta) {
        ItemData association = GTOreDictUnificator.getAssociation(new ItemStack(block, 1, meta));
        if (association == null) return null;
        if (association.mPrefix == null) return null;
        if (association.mMaterial == null) return null;
        if (!association.mPrefix.toString()
            .startsWith("ore")) return null;

        return association.mMaterial.mMaterial;
    }

    /**
     * Surveys the block the player clicked and its surroundings for ore.
     *
     * @param toolQuality the tool's harvest level; a better tool sees further and scans a wider box.
     * @param pay         charges the tool for one survey, and reports whether it could be paid for.
     * @return whether the click was consumed.
     */
    public static boolean prospect(int toolQuality, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        ForgeDirection side, float hitX, float hitY, float hitZ, BooleanSupplier pay) {
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
            GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.ore", mat.getLocalizedName());
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            return true;
        }

        Materials oreMat = getOreMaterial(aBlock, aMeta);

        if (oreMat != null) {
            GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.ore", oreMat.getLocalizedName());
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            return true;
        }

        if (aBlock.getMaterial() == Material.rock || aBlock.getMaterial() == Material.ground
            || GTUtility.isOre(aBlock, aMeta)) {
            if (!pay.getAsBoolean()) return false;

            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, hitX, hitY, hitZ);
            int tX = aX, tY = aY, tZ = aZ;
            for (int i = 0, j = 6 + toolQuality; i < j; i++) {
                tX -= side.offsetX;
                tY -= side.offsetY;
                tZ -= side.offsetZ;

                Block tBlock = aWorld.getBlock(tX, tY, tZ);

                if (tBlock == Blocks.lava || tBlock == Blocks.flowing_lava) {
                    GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.lava");
                    break;
                }
                if (tBlock instanceof BlockLiquid || tBlock instanceof IFluidBlock) {
                    GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.liquid");
                    break;
                }
                if (tBlock == Blocks.monster_egg || !GTUtility.hasBlockHitBox(aWorld, tX, tY, tZ)) {
                    GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.air");
                    break;
                }
                if (tBlock != aBlock) {
                    if (i < 4) GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.changing");
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
                    GTUtility
                        .sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.traces_of", mat.getLocalizedName());
                    return true;
                }

                oreMat = getOreMaterial(tBlock, tMeta);

                if (oreMat != null) {
                    GTUtility
                        .sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.traces_of", oreMat.getLocalizedName());
                    return true;
                }
            }

            GTUtility.sendChatTrans(aPlayer, "GT5U.chat.behaviour.prospecting.no_ores");
            return true;
        }

        return false;
    }
}
