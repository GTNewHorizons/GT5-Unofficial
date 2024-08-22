package gregtech.common.items.behaviors;

import java.util.List;
import java.util.Random;

import gregtech.common.blocks.TileEntityOres;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;
import gregtech.common.blocks.BlockOresAbstract;

public class Behaviour_Prospecting extends Behaviour_None {

    private final int mVanillaCosts;
    private final int mEUCosts;
    private final String mTooltip = GT_LanguageManager
        .addStringLocalization("gt.behaviour.prospecting", "Usable for Prospecting");

    public Behaviour_Prospecting(int aVanillaCosts, int aEUCosts) {
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock == null) {
            return false;
        }
        byte aMeta = (byte) aWorld.getBlockMetadata(aX, aY, aZ);

        ItemData tAssociation = OreDictUnificator.getAssociation(new ItemStack(aBlock, 1, aMeta));
        if (tAssociation != null && tAssociation.mPrefix != null
            && tAssociation.mMaterial != null
            && tAssociation.mPrefix.toString()
                .startsWith("ore")) {
            GT_Utility.sendChatToPlayer(
                aPlayer,
                GT_Utility.trans("100", "This is ") + tAssociation.mMaterial.mMaterial.mDefaultLocalName
                    + GT_Utility.trans("101", " Ore."));
            GT_Utility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
            return true;
        }

        if (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.stone)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.netherrack)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.end_stone)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTech_API.sBlockStones)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTech_API.sBlockGranites)
            || aBlock == GregTech_API.sBlockOresUb1
            || aBlock == GregTech_API.sBlockOresUb2
            || aBlock == GregTech_API.sBlockOresUb3
            || aBlock == GregTech_API.sBlockOres1) {
            if (GT_ModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                GT_Utility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
                int tMetaID;
                int tQuality = aItem instanceof MetaGeneratedTool ? aItem.getHarvestLevel(aStack, "") : 0;
                int tX = aX, tY = aY, tZ = aZ;
                Block tBlock;
                for (int i = 0, j = 6 + tQuality; i < j; i++) {
                    tX -= side.offsetX;
                    tY -= side.offsetY;
                    tZ -= side.offsetZ;

                    tBlock = aWorld.getBlock(tX, tY, tZ);
                    if (tBlock == Blocks.lava || tBlock == Blocks.flowing_lava) {
                        GT_Utility
                            .sendChatToPlayer(aPlayer, GT_Utility.trans("102", "There is Lava behind this Rock."));
                        break;
                    }
                    if (tBlock instanceof BlockLiquid || tBlock instanceof IFluidBlock) {
                        GT_Utility
                            .sendChatToPlayer(aPlayer, GT_Utility.trans("103", "There is a Liquid behind this Rock."));
                        break;
                    }
                    if (tBlock == Blocks.monster_egg || !GT_Utility.hasBlockHitBox(aWorld, tX, tY, tZ)) {
                        GT_Utility.sendChatToPlayer(
                            aPlayer,
                            GT_Utility.trans("104", "There is an Air Pocket behind this Rock."));
                        break;
                    }
                    if (tBlock != aBlock) {
                        if (i < 4) GT_Utility.sendChatToPlayer(
                            aPlayer,
                            GT_Utility.trans("105", "Material is changing behind this Rock."));
                        break;
                    }
                }

                final Random tRandom = new XSTR(aX ^ aY ^ aZ ^ side.ordinal());
                for (int i = 0, j = 9 + 2 * tQuality; i < j; i++) {
                    tX = aX - 4 - tQuality + tRandom.nextInt(j);
                    tY = aY - 4 - tQuality + tRandom.nextInt(j);
                    tZ = aZ - 4 - tQuality + tRandom.nextInt(j);
                    tBlock = aWorld.getBlock(tX, tY, tZ);
                    if (tBlock instanceof BlockOresAbstract) {
                        final TileEntity tTileEntity = aWorld.getTileEntity(tX, tY, tZ);
                        if (tTileEntity instanceof TileEntityOres) {
                            final Materials tMaterial = GregTech_API.sGeneratedMaterials[((TileEntityOres) tTileEntity).mMetaData
                                % 1000];
                            if (tMaterial != null && tMaterial != Materials._NULL) {
                                GT_Utility.sendChatToPlayer(
                                    aPlayer,
                                    GT_Utility.trans("106", "Found traces of ") + tMaterial.mDefaultLocalName
                                        + GT_Utility.trans("101", " Ore."));
                                return true;
                            }
                        }
                    } else {
                        tMetaID = aWorld.getBlockMetadata(tX, tY, tZ);
                        tAssociation = OreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                        if (tAssociation != null && tAssociation.mPrefix != null
                            && tAssociation.mMaterial != null
                            && tAssociation.mPrefix.toString()
                                .startsWith("ore")) {
                            GT_Utility.sendChatToPlayer(
                                aPlayer,
                                GT_Utility.trans("106", "Found traces of ")
                                    + tAssociation.mMaterial.mMaterial.mDefaultLocalName
                                    + GT_Utility.trans("101", " Ore."));
                            return true;
                        }
                    }
                }
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("107", "No Ores found."));
                return true;
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
