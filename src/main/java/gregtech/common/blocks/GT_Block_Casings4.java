package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_RenderingWorld;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;

public class GT_Block_Casings4 extends GT_Block_Casings_Abstract {

    /**
     * This mapping is used to look up which texture should be used to render the connected texture for fusion casings.
     *
     * This mapping is computed from that giant if ladder from #getIcon in commit
     * da3421547afadc49938b5b6a7f9a9679afa1d570 The exact meaning of these numbers are like black magic. Read the
     * original getIcon implementation to understand why it is 0, 1, etc, if that if ladder is even intelligible.
     */
    private static final int[][] mapping = new int[][] {
            { 7, 7, 7, 7, 0, 7, 0, 7, 1, 7, 1, 7, 8, 7, 8, 7, 0, 7, 0, 7, 0, 7, 0, 7, 9, 7, 9, 7, 3, 7, 3, 7, 1, 7, 1,
                    7, 11, 7, 11, 7, 1, 7, 1, 7, 2, 7, 2, 7, 10, 7, 10, 7, 5, 7, 5, 7, 4, 7, 4, 7, 6, 7, 6, 7 },
            { 7, 7, 7, 7, 0, 0, 7, 7, 1, 1, 7, 7, 8, 8, 7, 7, 0, 0, 7, 7, 0, 0, 7, 7, 9, 9, 7, 7, 3, 3, 7, 7, 1, 1, 7,
                    7, 11, 11, 7, 7, 1, 1, 7, 7, 2, 2, 7, 7, 10, 10, 7, 7, 5, 5, 7, 7, 4, 4, 7, 7, 6, 6, 7, 7 },
            { 7, 1, 1, 1, 0, 9, 10, 4, 7, 1, 1, 1, 0, 9, 10, 4, 0, 8, 11, 2, 0, 3, 5, 6, 0, 8, 11, 2, 0, 3, 5, 6, 7, 7,
                    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
            { 7, 1, 1, 1, 0, 8, 11, 2, 7, 7, 7, 7, 7, 7, 7, 7, 0, 9, 10, 4, 0, 3, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 1, 1,
                    1, 0, 8, 11, 2, 7, 7, 7, 7, 7, 7, 7, 7, 0, 9, 10, 4, 0, 3, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7 },
            { 7, 1, 1, 1, 7, 1, 1, 1, 0, 8, 11, 2, 0, 8, 11, 2, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 0, 9,
                    10, 4, 0, 9, 10, 4, 0, 3, 5, 6, 0, 3, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
            { 7, 1, 1, 1, 7, 7, 7, 7, 0, 9, 10, 4, 7, 7, 7, 7, 7, 1, 1, 1, 7, 7, 7, 7, 0, 9, 10, 4, 7, 7, 7, 7, 0, 8,
                    11, 2, 7, 7, 7, 7, 0, 3, 5, 6, 7, 7, 7, 7, 0, 8, 11, 2, 7, 7, 7, 7, 0, 3, 5, 6, 7, 7, 7, 7 }, };

    public static boolean mConnectedMachineTextures = true;

    public GT_Block_Casings4() {
        super(GT_Item_Casings4.class, "gt.blockcasings4", GT_Material_Casings.INSTANCE, 16);
        GT_LanguageManager.addStringLocalization(
                getUnlocalizedName() + ".0.name",
                "Robust Tungstensteel Machine Casing");
        GT_LanguageManager.addStringLocalization(
                getUnlocalizedName() + ".1.name",
                "Clean Stainless Steel Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Stable Titanium Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Titanium Firebox Casing");
        // GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Fusion Casing");
        // GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Fusion Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Fusion Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Fusion Machine Casing MK II");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Turbine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Stainless Steel Turbine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Titanium Turbine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Tungstensteel Turbine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Engine Intake Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Mining Osmiridium Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Firebricks");

        ItemList.Casing_RobustTungstenSteel.set(new ItemStack(this, 1, 0));
        ItemList.Casing_CleanStainlessSteel.set(new ItemStack(this, 1, 1));
        ItemList.Casing_StableTitanium.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Firebox_Titanium.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Fusion.set(new ItemStack(this, 1, 6));
        ItemList.Casing_Fusion_Coil.set(new ItemStack(this, 1, 7));
        ItemList.Casing_Fusion2.set(new ItemStack(this, 1, 8));
        ItemList.Casing_Turbine.set(new ItemStack(this, 1, 9));
        ItemList.Casing_Turbine1.set(new ItemStack(this, 1, 10));
        ItemList.Casing_Turbine2.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Turbine3.set(new ItemStack(this, 1, 12));
        ItemList.Casing_EngineIntake.set(new ItemStack(this, 1, 13));
        ItemList.Casing_MiningOsmiridium.set(new ItemStack(this, 1, 14));
        ItemList.Casing_Firebricks.set(new ItemStack(this, 1, 15));

        GT_Mod.gregtechproxy.mCTMBlockCache.put(this, (byte) 6, true);
        GT_Mod.gregtechproxy.mCTMBlockCache.put(this, (byte) 8, true);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta + 48;
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
            case 12:
                return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            case 1:
            case 10:
                return Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
            case 2:
            case 11:
                return Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 3:
                return aSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_TITANIUM.getIcon()
                        : Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 4:
                // Do not overwrite!
                return Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW.getIcon();
            case 5:
                // Do not overwrite!
                return Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS.getIcon();
            case 6:
                return Textures.BlockIcons.MACHINE_CASING_FUSION.getIcon();
            case 7:
                return Textures.BlockIcons.MACHINE_CASING_FUSION_COIL.getIcon();
            case 8:
                return Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
            case 9:
                return Textures.BlockIcons.MACHINE_CASING_TURBINE.getIcon();
            case 13:
                return Textures.BlockIcons.MACHINE_CASING_ENGINE_INTAKE.getIcon();
            case 14:
                return Textures.BlockIcons.MACHINE_CASING_MINING_OSMIRIDIUM.getIcon();
            case 15:
                return Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS.getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    @Deprecated
    public IIcon getTurbineCasing(int meta, int iconIndex, boolean active) {
        switch (meta) {
            case 10:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE1[iconIndex].getIcon()
                        : Textures.BlockIcons.TURBINE1[iconIndex].getIcon();
            case 11:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE2[iconIndex].getIcon()
                        : Textures.BlockIcons.TURBINE2[iconIndex].getIcon();
            case 12:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE3[iconIndex].getIcon()
                        : Textures.BlockIcons.TURBINE3[iconIndex].getIcon();
            default:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE[iconIndex].getIcon()
                        : Textures.BlockIcons.TURBINE[iconIndex].getIcon();
        }
    }

    @Deprecated
    public IIcon getTurbineCasing(int meta, int iconIndex, boolean active, boolean hasTurbine) {
        switch (meta) {
            case 10:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE1[iconIndex].getIcon()
                        : hasTurbine ? Textures.BlockIcons.TURBINE1[iconIndex].getIcon()
                                : Textures.BlockIcons.TURBINE_EMPTY1[iconIndex].getIcon();
            case 11:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE2[iconIndex].getIcon()
                        : hasTurbine ? Textures.BlockIcons.TURBINE2[iconIndex].getIcon()
                                : Textures.BlockIcons.TURBINE_EMPTY2[iconIndex].getIcon();
            case 12:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE3[iconIndex].getIcon()
                        : hasTurbine ? Textures.BlockIcons.TURBINE3[iconIndex].getIcon()
                                : Textures.BlockIcons.TURBINE_EMPTY3[iconIndex].getIcon();
            default:
                return active ? Textures.BlockIcons.TURBINE_ACTIVE[iconIndex].getIcon()
                        : hasTurbine ? Textures.BlockIcons.TURBINE[iconIndex].getIcon()
                                : Textures.BlockIcons.TURBINE_EMPTY[iconIndex].getIcon();
        }
    }

    private static int isTurbineControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof GT_MetaTileEntity_LargeTurbine turbine && tTile.getFrontFacing() == aSide) {
            if (turbine.isNewStyleRendering()) return 0;
            if (tTile.isActive()) return 1;
            return turbine.hasTurbine() ? 2 : 3;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        aWorld = GT_RenderingWorld.getInstance(aWorld);
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        if (tMeta != 6 && tMeta != 8 && tMeta != 9 && tMeta != 10 && tMeta != 11 && tMeta != 12
                || !mConnectedMachineTextures) {
            return getIcon(aSide, tMeta);
        }
        if (tMeta > 8 && tMeta < 13) {
            int tInvertLeftRightMod = aSide % 2 * 2 - 1;
            switch (aSide / 2) {
                case 0:
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if (i == 0 && j == 0) continue;
                            int tState;
                            if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, aSide))
                                    != 0) {
                                return getTurbineCasing(tMeta, 4 - i * 3 - j, tState == 1, tState == 2);
                            }
                        }
                    }
                    break;
                case 1:
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if (i == 0 && j == 0) continue;
                            int tState;
                            if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, aSide))
                                    != 0) {
                                return getTurbineCasing(
                                        tMeta,
                                        4 + i * 3 - j * tInvertLeftRightMod,
                                        tState == 1,
                                        tState == 2);
                            }
                        }
                    }
                    break;
                case 2:
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if (i == 0 && j == 0) continue;
                            int tState;
                            if ((tState = isTurbineControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, aSide))
                                    != 0) {
                                return getTurbineCasing(
                                        tMeta,
                                        4 + i * 3 + j * tInvertLeftRightMod,
                                        tState == 1,
                                        tState == 2);
                            }
                        }
                    }
                    break;
            }
            switch (tMeta) {
                case 10:
                    return Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
                case 11:
                    return Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
                case 12:
                    return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
                default:
                    return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
            }
        }
        int tStartIndex = tMeta == 6 ? 1 : 13;
        int tIndexIntoMapping = 0;
        if (isSameBlock(aWorld, xCoord, yCoord - 1, zCoord, tMeta)) tIndexIntoMapping |= 1;
        if (isSameBlock(aWorld, xCoord, yCoord + 1, zCoord, tMeta)) tIndexIntoMapping |= 1 << 1;
        if (isSameBlock(aWorld, xCoord + 1, yCoord, zCoord, tMeta)) tIndexIntoMapping |= 1 << 2;
        if (isSameBlock(aWorld, xCoord, yCoord, zCoord + 1, tMeta)) tIndexIntoMapping |= 1 << 3;
        if (isSameBlock(aWorld, xCoord - 1, yCoord, zCoord, tMeta)) tIndexIntoMapping |= 1 << 4;
        if (isSameBlock(aWorld, xCoord, yCoord, zCoord - 1, tMeta)) tIndexIntoMapping |= 1 << 5;
        return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + mapping[aSide][tIndexIntoMapping]].getIcon();
    }

    private boolean isSameBlock(IBlockAccess aWorld, int aX, int aY, int aZ, int aMeta) {
        return aWorld.getBlock(aX, aY, aZ) == this && aWorld.getBlockMetadata(aX, aY, aZ) == aMeta;
    }
}
