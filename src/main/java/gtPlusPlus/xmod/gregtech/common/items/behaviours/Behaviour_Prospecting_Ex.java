package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockOres;
import gregtech.common.blocks.TileEntityOres;
import gregtech.common.items.behaviors.BehaviourProspecting;

public class Behaviour_Prospecting_Ex extends BehaviourProspecting {

    private final int mVanillaCosts;
    private final int mEUCosts;

    public Behaviour_Prospecting_Ex(final int aVanillaCosts, final int aEUCosts) {
        super(aVanillaCosts, aEUCosts);
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
    }

    @Override
    public boolean onItemUseFirst(final MetaBaseItem aItem, final ItemStack aStack, final EntityPlayer aPlayer,
        final World aWorld, final int aX, final int aY, final int aZ, final ForgeDirection side, final float hitX,
        final float hitY, final float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        final Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock == null) {
            return false;
        }
        final byte aMeta = (byte) aWorld.getBlockMetadata(aX, aY, aZ);

        ItemData tAssotiation = GTOreDictUnificator.getAssociation(new ItemStack(aBlock, 1, aMeta));
        if ((tAssotiation != null) && (tAssotiation.mPrefix.toString()
            .startsWith("ore"))) {
            GTUtility
                .sendChatToPlayer(aPlayer, "This is " + tAssotiation.mMaterial.mMaterial.mDefaultLocalName + " Ore.");
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
            return true;
        }
        if ((aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.stone))
            || (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTechAPI.sBlockGranites))
            || (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.netherrack))
            || (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.end_stone))
            || (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.dirt))
            || (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.grass))) {
            if (GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
                int tX = aX;
                int tY = aY;
                int tZ = aZ;
                int tMetaID = 0;
                final int tQuality = ((aItem instanceof MetaGeneratedTool)
                    ? ((MetaGeneratedTool) aItem).getHarvestLevel(aStack, "")
                    : 0) * 3;

                int i = 0;
                for (final int j = 6 + tQuality; i < j; i++) {
                    tX -= side.offsetX;
                    tY -= side.offsetY;
                    tZ -= side.offsetZ;

                    final Block tBlock = aWorld.getBlock(tX, tY, tZ);
                    if ((tBlock == Blocks.lava) || (tBlock == Blocks.flowing_lava)) {
                        GTUtility.sendChatToPlayer(aPlayer, "There is Lava behind this Block.");
                        break;
                    }
                    if ((tBlock == Blocks.water) || (tBlock == Blocks.flowing_water)
                        || ((tBlock instanceof IFluidBlock))) {
                        GTUtility.sendChatToPlayer(aPlayer, "There is a Liquid behind this Block.");
                        break;
                    }
                    if ((tBlock == Blocks.monster_egg) || (!GTUtility.hasBlockHitBox(aWorld, tX, tY, tZ))) {
                        GTUtility.sendChatToPlayer(aPlayer, "There is an Air Pocket behind this Block.");
                        break;
                    }
                    if (tBlock != aBlock) {
                        if (i >= 4) {
                            break;
                        }
                        GTUtility.sendChatToPlayer(aPlayer, "Material is changing behind this Block.");
                        break;
                    }
                }
                final Random tRandom = new Random(aX ^ aY ^ aZ ^ side.ordinal());
                i = 0;
                for (final int j = 9 + (2 * tQuality); i < j; i++) {
                    tX = (aX - 4 - tQuality) + tRandom.nextInt(j);
                    tY = (aY - 4 - tQuality) + tRandom.nextInt(j);
                    tZ = (aZ - 4 - tQuality) + tRandom.nextInt(j);
                    final Block tBlock = aWorld.getBlock(tX, tY, tZ);
                    if ((tBlock instanceof BlockOres)) {
                        final TileEntity tTileEntity = aWorld.getTileEntity(tX, tY, tZ);
                        if ((tTileEntity instanceof TileEntityOres)) {
                            final Materials tMaterial = GregTechAPI.sGeneratedMaterials[(((TileEntityOres) tTileEntity).mMetaData
                                % 1000)];
                            if ((tMaterial != null) && (tMaterial != Materials._NULL)) {
                                GTUtility.sendChatToPlayer(
                                    aPlayer,
                                    "Found traces of " + tMaterial.mDefaultLocalName + " Ore.");
                                return true;
                            }
                        }
                    } else {
                        tMetaID = aWorld.getBlockMetadata(tX, tY, tZ);
                        tAssotiation = GTOreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                        if ((tAssotiation != null) && (tAssotiation.mPrefix.toString()
                            .startsWith("ore"))) {
                            GTUtility.sendChatToPlayer(
                                aPlayer,
                                "Found traces of " + tAssotiation.mMaterial.mMaterial.mDefaultLocalName + " Ore.");
                            return true;
                        }
                    }
                }
                GTUtility.sendChatToPlayer(aPlayer, "No Ores found.");
            }
            return true;
        }
        return false;
    }
}
