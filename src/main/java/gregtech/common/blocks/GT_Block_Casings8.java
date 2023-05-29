package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_RenderingWorld;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class GT_Block_Casings8 extends GT_Block_Casings_Abstract {

    public static boolean mConnectedMachineTextures = true;

    // WATCH OUT FOR TEXTURE ID's
    public GT_Block_Casings8() {
        super(GT_Item_Casings8.class, "gt.blockcasings8", GT_Material_Casings.INSTANCE, 15);
        /*
         * DO NOT USE INDEX 15 ! USED HERE: https://github.com/GTNewHorizons/Electro-Magic-Tools/pull/17
         */

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Chemically Inert Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "PTFE Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Mining Neutronium Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Mining Black Plutonium Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Extreme Engine Intake Casing");
        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName() + ".5.name",
            "Europium Reinforced Radiation Proof Machine Casing");
        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName() + ".6.name",
            "Advanced Rhodium Plated Palladium Machine Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".7.name", "Advanced Iridium Plated Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Magical Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "HSS-S Turbine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Radiant Naquadah Alloy Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".11.name", "Basic Photolithographic Framework Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".12.name", "Reinforced Photolithographic Framework Casing");
        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName() + ".13.name",
            "Radiation Proof Photolithographic Framework Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Infinity Cooled Casing");

        ItemList.Casing_Chemically_Inert.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Pipe_Polytetrafluoroethylene.set(new ItemStack(this, 1, 1));
        ItemList.Casing_MiningNeutronium.set(new ItemStack(this, 1, 2));
        ItemList.Casing_MiningBlackPlutonium.set(new ItemStack(this, 1, 3));
        ItemList.Casing_ExtremeEngineIntake.set(new ItemStack(this, 1, 4));
        ItemList.Casing_AdvancedRadiationProof.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Advanced_Rhodium_Palladium.set(new ItemStack(this, 1, 6));
        ItemList.Casing_Advanced_Iridium.set(new ItemStack(this, 1, 7));
        ItemList.Casing_Magical.set(new ItemStack(this, 1, 8));
        ItemList.Casing_TurbineGasAdvanced.set(new ItemStack(this, 1, 9));
        ItemList.RadiantNaquadahAlloyCasing.set(new ItemStack(this, 1, 10));
        ItemList.BasicPhotolithographicFrameworkCasing.set(new ItemStack(this, 1, 11));
        ItemList.ReinforcedPhotolithographicFrameworkCasing.set(new ItemStack(this, 1, 12));
        ItemList.RadiationProofPhotolithographicFrameworkCasing.set(new ItemStack(this, 1, 13));
        ItemList.InfinityCooledCasing.set(new ItemStack(this, 1, 14));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | (aMeta + 48);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_CHEMICALLY_INERT.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_MINING_NEUTRONIUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_MINING_BLACKPLUTONIUM.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_ENGINE_INTAKE.getIcon(); // changed color in a
            // terrible way
            case 5 -> Textures.BlockIcons.MACHINE_CASING_ADVANCEDRADIATIONPROOF.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_RHODIUM_PALLADIUM.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_IRIDIUM.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_MAGICAL.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_ADVANCEDGAS.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_RADIANT_NAQUADAH_ALLOY.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_1.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_2.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_3.getIcon();
            case 14 -> Textures.BlockIcons.INFINITY_COOLED_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }

    @Deprecated
    public IIcon getTurbineCasing(int meta, int iconIndex, boolean active) {
        return switch (meta) {
            case 9 -> active ? Textures.BlockIcons.TURBINE_ADVGASACTIVE[iconIndex].getIcon()
                : Textures.BlockIcons.TURBINEADVGAS[iconIndex].getIcon();
            default -> active ? Textures.BlockIcons.TURBINE_ACTIVE[iconIndex].getIcon()
                : Textures.BlockIcons.TURBINE[iconIndex].getIcon();
        };
    }

    public IIcon getTurbineCasing(int meta, int iconIndex, boolean active, boolean hasTurbine) {
        return switch (meta) {
            case 9 -> active ? Textures.BlockIcons.TURBINE_ADVGASACTIVE[iconIndex].getIcon()
                : hasTurbine ? Textures.BlockIcons.TURBINEADVGAS[iconIndex].getIcon()
                    : Textures.BlockIcons.TURBINE_ADVGASEMPTY[iconIndex].getIcon();
            default -> active ? Textures.BlockIcons.TURBINE_ACTIVE[iconIndex].getIcon()
                : hasTurbine ? Textures.BlockIcons.TURBINE[iconIndex].getIcon()
                    : Textures.BlockIcons.TURBINE_EMPTY[iconIndex].getIcon();
        };
    }

    private static int isTurbineControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof GT_MetaTileEntity_LargeTurbine turbine && tTile.getFrontFacing()
            .ordinal() == ordinalSide) {
            if (turbine.isNewStyleRendering()) return 0;
            if (tTile.isActive()) return 1;
            return turbine.hasTurbine() ? 2 : 3;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int ordinalSide) {
        aWorld = GT_RenderingWorld.getInstance(aWorld);
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);

        if (tMeta != 9 || !mConnectedMachineTextures) {
            return getIcon(ordinalSide, tMeta);
        }

        int tInvertLeftRightMod = ordinalSide % 2 * 2 - 1;

        switch (ordinalSide / 2) {
            case 0 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, ordinalSide))
                            != 0) {
                            return getTurbineCasing(tMeta, 4 - i * 3 - j, tState == 1, tState == 2);
                        }
                    }
                }
            }
            case 1 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, ordinalSide))
                            != 0) {
                            return getTurbineCasing(
                                tMeta,
                                4 + i * 3 - j * tInvertLeftRightMod,
                                tState == 1,
                                tState == 2);
                        }
                    }
                }
            }
            case 2 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, ordinalSide))
                            != 0) {
                            return getTurbineCasing(
                                tMeta,
                                4 + i * 3 + j * tInvertLeftRightMod,
                                tState == 1,
                                tState == 2);
                        }
                    }
                }
            }
        }

        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }
}
