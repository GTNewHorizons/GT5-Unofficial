package gregtech.common.items.behaviors;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
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
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;

public class BehaviourProspecting extends BehaviourNone {

    private final int mVanillaCosts;
    private final int mEUCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.prospecting", "Usable for Prospecting");

    public BehaviourProspecting(int aVanillaCosts, int aEUCosts) {
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

        ItemData tAssociation = GTOreDictUnificator.getAssociation(new ItemStack(aBlock, 1, aMeta));
        if (tAssociation != null && tAssociation.mPrefix != null
            && tAssociation.mMaterial != null
            && tAssociation.mPrefix.toString()
                .startsWith("ore")) {
            GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("100", "This is ") + tAssociation.mMaterial.mMaterial.mDefaultLocalName
                    + GTUtility.trans("101", " Ore."));
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
            return true;
        }

        if (aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.stone)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.netherrack)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.end_stone)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTechAPI.sBlockStones)
            || aBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTechAPI.sBlockGranites)
            || aBlock == GregTechAPI.sBlockOresUb1
            || aBlock == GregTechAPI.sBlockOresUb2
            || aBlock == GregTechAPI.sBlockOresUb3
            || aBlock == GregTechAPI.sBlockOres1) {
            if (GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_ANVIL_USE, 1.0F, -1.0F, aX, aY, aZ);
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
                        GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("102", "There is Lava behind this Rock."));
                        break;
                    }
                    if (tBlock instanceof BlockLiquid || tBlock instanceof IFluidBlock) {
                        GTUtility
                            .sendChatToPlayer(aPlayer, GTUtility.trans("103", "There is a Liquid behind this Rock."));
                        break;
                    }
                    if (tBlock == Blocks.monster_egg || !GTUtility.hasBlockHitBox(aWorld, tX, tY, tZ)) {
                        GTUtility.sendChatToPlayer(
                            aPlayer,
                            GTUtility.trans("104", "There is an Air Pocket behind this Rock."));
                        break;
                    }
                    if (tBlock != aBlock) {
                        if (i < 4) GTUtility.sendChatToPlayer(
                            aPlayer,
                            GTUtility.trans("105", "Material is changing behind this Rock."));
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
                            final Materials tMaterial = GregTechAPI.sGeneratedMaterials[((TileEntityOres) tTileEntity).mMetaData
                                % 1000];
                            if (tMaterial != null && tMaterial != Materials._NULL) {
                                GTUtility.sendChatToPlayer(
                                    aPlayer,
                                    GTUtility.trans("106", "Found traces of ") + tMaterial.mDefaultLocalName
                                        + GTUtility.trans("101", " Ore."));
                                return true;
                            }
                        }
                    } else {
                        tMetaID = aWorld.getBlockMetadata(tX, tY, tZ);
                        tAssociation = GTOreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                        if (tAssociation != null && tAssociation.mPrefix != null
                            && tAssociation.mMaterial != null
                            && tAssociation.mPrefix.toString()
                                .startsWith("ore")) {
                            GTUtility.sendChatToPlayer(
                                aPlayer,
                                GTUtility.trans("106", "Found traces of ")
                                    + tAssociation.mMaterial.mMaterial.mDefaultLocalName
                                    + GTUtility.trans("101", " Ore."));
                            return true;
                        }
                    }
                }
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("107", "No Ores found."));
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
