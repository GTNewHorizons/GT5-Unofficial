package tectech.thing.casing;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GTCopiedBlockTexture;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.MaterialCasings;
import tectech.thing.CustomItemList;

/**
 * Created by danie_000 on 03.10.2016.
 */
// Mostly tesla coils, also 2 eye of harmony casings.
public class BlockGTCasingsBA0 extends BlockCasingsAbstract {

    private static final IIcon[] tM0 = new IIcon[2];
    private static final IIcon[] tM1 = new IIcon[2];
    private static final IIcon[] tM2 = new IIcon[2];
    private static final IIcon[] tM3 = new IIcon[2];
    private static final IIcon[] tM4 = new IIcon[2];
    private static final IIcon[] tM5 = new IIcon[2];
    private static final IIcon[] tM6 = new IIcon[2];
    private static IIcon tM7;
    private static final IIcon[] tM8 = new IIcon[2];
    private static final IIcon[] tM9 = new IIcon[2];

    private static IIcon EOH_INNER;
    private static IIcon EOH_OUTER;
    private static IIcon EOH_INFINITE;

    private static final byte START_INDEX = 16;

    public BlockGTCasingsBA0() {
        super(ItemCasingsBA0.class, "gt.blockcasingsBA0", MaterialCasings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][b
                + START_INDEX] = new GTCopiedBlockTexture(this, 6, b);
            /* IMPORTANT for block recoloring **/
        }

        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".0.name", "Redstone Alloy Primary Tesla Windings");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".1.name", "MV Superconductor Primary Tesla Windings");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".2.name", "HV Superconductor Primary Tesla Windings");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".3.name", "EV Superconductor Primary Tesla Windings");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".4.name", "IV Superconductor Primary Tesla Windings");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".5.name", "LuV Superconductor Primary Tesla Windings");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".9.name", "ZPM Superconductor Primary Tesla Windings");

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Tesla Base Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Tesla Toroid Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Tesla Secondary Windings");

        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".10.name", "Reinforced Temporal Structure Casing");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".11.name", "Reinforced Spatial Structure Casing");
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + ".12.name", "Infinite Spacetime Energy Boundary Casing");

        CustomItemList.tM_TeslaPrimary_0.set(new ItemStack(this, 1, 0));
        CustomItemList.tM_TeslaPrimary_1.set(new ItemStack(this, 1, 1));
        CustomItemList.tM_TeslaPrimary_2.set(new ItemStack(this, 1, 2));
        CustomItemList.tM_TeslaPrimary_3.set(new ItemStack(this, 1, 3));
        CustomItemList.tM_TeslaPrimary_4.set(new ItemStack(this, 1, 4));
        CustomItemList.tM_TeslaPrimary_5.set(new ItemStack(this, 1, 5));
        CustomItemList.tM_TeslaPrimary_6.set(new ItemStack(this, 1, 9));

        CustomItemList.tM_TeslaBase.set(new ItemStack(this, 1, 6));
        CustomItemList.tM_TeslaToroid.set(new ItemStack(this, 1, 7));
        CustomItemList.tM_TeslaSecondary.set(new ItemStack(this, 1, 8));

        CustomItemList.EOH_Reinforced_Temporal_Casing.set(new ItemStack(this, 1, 10));
        CustomItemList.EOH_Reinforced_Spatial_Casing.set(new ItemStack(this, 1, 11));
        CustomItemList.EOH_Infinite_Energy_Casing.set(new ItemStack(this, 1, 12));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        tM0[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_0");
        tM0[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_0");
        tM1[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_1");
        tM1[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_1");
        tM2[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_2");
        tM2[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_2");
        tM3[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_3");
        tM3[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_3");
        tM4[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_4");
        tM4[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_4");
        tM5[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_5");
        tM5[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_5");
        tM9[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_6");
        tM9[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_6");

        tM6[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_BASE_TOP_BOTTOM");
        tM6[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_BASE_SIDES");
        tM7 = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_TOROID");
        tM8[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_SECONDARY_TOP_BOTTOM");
        tM8[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_SECONDARY_SIDES");

        EOH_INNER = aIconRegister.registerIcon("gregtech:iconsets/EM_INNER_SPACETIME_REINFORCED_EOH_CASING");
        EOH_OUTER = aIconRegister.registerIcon("gregtech:iconsets/EM_OUTER_SPACETIME_REINFORCED_EOH_CASING");
        EOH_INFINITE = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER_INFINITE");
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        switch (aMeta) {
            case 0:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM0[0];
                    default:
                        return tM0[1];
                }
            case 1:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM1[0];
                    default:
                        return tM1[1];
                }
            case 2:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM2[0];
                    default:
                        return tM2[1];
                }
            case 3:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM3[0];
                    default:
                        return tM3[1];
                }
            case 4:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM4[0];
                    default:
                        return tM4[1];
                }
            case 5:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM5[0];
                    default:
                        return tM5[1];
                }
            case 6:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM6[0];
                    default:
                        return tM6[1];
                }
            case 7:
                return tM7;
            case 8:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM8[0];
                    default:
                        return tM8[1];
                }
            case 9:
                switch (ordinalSide) {
                    case 0:
                    case 1:
                        return tM9[0];
                    default:
                        return tM9[1];
                }
            case 10:
                return EOH_INNER;
            case 11:
                return EOH_OUTER;
            case 12:
                return EOH_INFINITE;
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int ordinalSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(ordinalSide, tMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        for (int i = 0; i <= 12; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
