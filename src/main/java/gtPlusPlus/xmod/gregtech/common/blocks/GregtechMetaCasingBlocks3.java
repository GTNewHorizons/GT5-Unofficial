package gtPlusPlus.xmod.gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.TAE;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler3;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.MTEPowerSubStation;

public class GregtechMetaCasingBlocks3 extends GregtechMetaCasingBlocksAbstract {

    public static boolean mConnectedMachineTextures = false;

    public static class GregtechMetaCasingItemBlocks3 extends GregtechMetaCasingItems {

        public GregtechMetaCasingItemBlocks3(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int meta = aStack.getItemDamage();
            int tier = MTEPowerSubStation.getCellTier(field_150939_a, meta);
            if (tier > 0) {
                long capacity = MTEPowerSubStation.getCapacityFromCellTier(tier);
                aList.add(
                    StatCollector
                        .translateToLocalFormatted("gtpp.tooltip.meta_casing.energy_storage", formatNumber(capacity)));
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaCasingBlocks3() {
        super(GregtechMetaCasingItemBlocks3.class, "gtplusplus.blockcasings.3", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            // Free up Redox casing in TAE
            if (i >= 4 && i <= 8) {
                continue;
            }
            TAE.registerTexture(2, i, TextureFactory.of(this, i));
        }
        // GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "Unnamed"); // Can Use, don't
        // change texture
        // (Used for Fusion
        // MK4)
        GregtechItemList.Casing_FishPond.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Extruder.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Multi_Use.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_BedrockMiner.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Vanadium_Redox_IV.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Vanadium_Redox_LuV.set(new ItemStack(this, 1, 5));
        GregtechItemList.Casing_Vanadium_Redox_ZPM.set(new ItemStack(this, 1, 6));
        GregtechItemList.Casing_Vanadium_Redox_UV.set(new ItemStack(this, 1, 7));
        GregtechItemList.Casing_Vanadium_Redox_MAX.set(new ItemStack(this, 1, 8));
        GregtechItemList.Casing_AmazonWarehouse.set(new ItemStack(this, 1, 9));
        GregtechItemList.Casing_AdvancedVacuum.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_Adv_BlastFurnace.set(new ItemStack(this, 1, 11));
        GregtechItemList.Casing_Fusion_External.set(new ItemStack(this, 1, 12));
        GregtechItemList.Casing_Fusion_Internal.set(new ItemStack(this, 1, 13));
        GregtechItemList.Casing_Containment.set(new ItemStack(this, 1, 15));

        for (int i = 4; i < 9; i++) {
            GTStructureChannels.PSS_CELL.registerAsIndicator(new ItemStack(this, 1, i), i - 2);
        }

        GTMod.proxy.mCTMBlockCache.put(this, (byte) 12, true);
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return CasingTextureHandler3.getIcon(ordinalSide, aMeta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final int ordinalSide) {
        final Block thisBlock = aWorld.getBlock(xCoord, yCoord, zCoord);
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        if ((tMeta != 12) || !GregtechMetaCasingBlocks3.mConnectedMachineTextures) {
            return getIcon(ordinalSide, tMeta);
        }
        final int tStartIndex = 0;
        final boolean[] tConnectedSides = {
            aWorld.getBlock(xCoord, yCoord - 1, zCoord) == thisBlock
                && aWorld.getBlockMetadata(xCoord, yCoord - 1, zCoord) == tMeta,
            aWorld.getBlock(xCoord, yCoord + 1, zCoord) == thisBlock
                && aWorld.getBlockMetadata(xCoord, yCoord + 1, zCoord) == tMeta,
            aWorld.getBlock(xCoord + 1, yCoord, zCoord) == thisBlock
                && aWorld.getBlockMetadata(xCoord + 1, yCoord, zCoord) == tMeta,
            aWorld.getBlock(xCoord, yCoord, zCoord + 1) == thisBlock
                && aWorld.getBlockMetadata(xCoord, yCoord, zCoord + 1) == tMeta,
            aWorld.getBlock(xCoord - 1, yCoord, zCoord) == thisBlock
                && aWorld.getBlockMetadata(xCoord - 1, yCoord, zCoord) == tMeta,
            aWorld.getBlock(xCoord, yCoord, zCoord - 1) == thisBlock
                && aWorld.getBlockMetadata(xCoord, yCoord, zCoord - 1) == tMeta };
        switch (ordinalSide) {
            case 0: {
                if (tConnectedSides[0]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
                }
                if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
                }
                if (tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
                }
                if (!tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
                }
                if (tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
                }
                if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
                }
                if (!tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (!tConnectedSides[4] && !tConnectedSides[2]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
                }
                if (!tConnectedSides[5] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
                }
            }
            case 1: {
                if (tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
                }
                if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
                }
                if (tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
                }
                if (!tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
                }
                if (tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
                }
                if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
                }
                if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
                }
                if (!tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[4]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
                }
                if (!tConnectedSides[3] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
                }
            }
            case 2: {
                if (tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
                }
                if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
                }
                if (tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
                }
                if (tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
                }
                if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[4]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
                }
            }
            case 3: {
                if (tConnectedSides[3]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
                }
                if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
                }
                if (tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
                }
                if (tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
                }
                if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
                }
                if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (!tConnectedSides[2] && !tConnectedSides[4]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
                }
            }
            case 4: {
                if (tConnectedSides[4]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
                }
                if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
                }
                if (tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
                }
                if (tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
                }
                if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
                }
                if (!tConnectedSides[3] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
                }
            }
            case 5: {
                if (tConnectedSides[2]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
                }
                if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
                }
                if (tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
                }
                if (tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
                }
                if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
                }
                if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
                }
                if (!tConnectedSides[0] && !tConnectedSides[1]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
                }
                if (!tConnectedSides[3] && !tConnectedSides[5]) {
                    return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
                }
                break;
            }
        }
        return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
    }
}
