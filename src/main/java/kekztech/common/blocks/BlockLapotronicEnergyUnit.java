package kekztech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import kekztech.common.itemBlocks.ItemBlockLapotronicEnergyUnit;

public class BlockLapotronicEnergyUnit extends BaseGTUpdateableBlock {

    private static final BlockLapotronicEnergyUnit INSTANCE = new BlockLapotronicEnergyUnit();

    public enum IconBaseSide implements IIconContainer {

        INSTANCE;

        @Override
        public IIcon getIcon() {
            return BlockLapotronicEnergyUnit.INSTANCE.iconBaseSide;
        }

        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    static {
        // technically, this page is owned by me, glee8e, however, I'm kind enough to spare 1 for kekztech since
        // this is basically abandon ware by now.
        GTUtility.addTexturePage((byte) 42);
        Textures.BlockIcons.setCasingTexture((byte) 42, (byte) 127, TextureFactory.of(IconBaseSide.INSTANCE));
    }

    private IIcon iconBaseSide;
    private IIcon iconBaseTop;

    private IIcon iconLapoEmptySide;
    private IIcon iconLapoEmptyTop;
    private IIcon iconLapoEVSide;
    private IIcon iconLapoEVTop;
    private IIcon iconLapoIVSide;
    private IIcon iconLapoIVTop;
    private IIcon iconLapoLuVSide;
    private IIcon iconLapoLuVTop;
    private IIcon iconLapoZPMSide;
    private IIcon iconLapoZPMTop;
    private IIcon iconLapoUVSide;
    private IIcon iconLapoUVTop;
    private IIcon iconUltimateSide;
    private IIcon iconUltimateTop;
    private IIcon iconUltimateExtremeSide;
    private IIcon iconUltimateExtremeTop;
    private IIcon iconUltimateInsaneSide;
    private IIcon iconUltimateInsaneTop;
    private IIcon iconUltimateMegaSide;
    private IIcon iconUltimateMegaTop;

    private BlockLapotronicEnergyUnit() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_lapotronicenergyunit_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(5.0f);
        INSTANCE.setResistance(6.0f);
        GameRegistry.registerBlock(INSTANCE, ItemBlockLapotronicEnergyUnit.class, blockName);

        return INSTANCE;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        iconBaseSide = ir.registerIcon("kekztech:LSCBase_side");
        iconBaseTop = ir.registerIcon("kekztech:LSCBase_top");

        iconLapoEmptySide = ir.registerIcon("kekztech:LapotronicEnergyUnit6_side");
        iconLapoEmptyTop = ir.registerIcon("kekztech:LapotronicEnergyUnit6_top");
        iconLapoEVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit7_side");
        iconLapoEVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit7_top");
        iconLapoIVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit1_side");
        iconLapoIVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit1_top");
        iconLapoLuVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit2_side");
        iconLapoLuVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit2_top");
        iconLapoZPMSide = ir.registerIcon("kekztech:LapotronicEnergyUnit3_side");
        iconLapoZPMTop = ir.registerIcon("kekztech:LapotronicEnergyUnit3_top");
        iconLapoUVSide = ir.registerIcon("kekztech:LapotronicEnergyUnit4_side");
        iconLapoUVTop = ir.registerIcon("kekztech:LapotronicEnergyUnit4_top");

        iconUltimateSide = ir.registerIcon("kekztech:UltimateEnergyUnit_side");
        iconUltimateTop = ir.registerIcon("kekztech:UltimateEnergyUnit_top");
        iconUltimateExtremeSide = ir.registerIcon("kekztech:ReallyUltimateEnergyUnit_side");
        iconUltimateExtremeTop = ir.registerIcon("kekztech:ReallyUltimateEnergyUnit_top");
        iconUltimateInsaneSide = ir.registerIcon("kekztech:InsanelyUltimateEnergyUnit_side");
        iconUltimateInsaneTop = ir.registerIcon("kekztech:InsanelyUltimateEnergyUnit_top");
        iconUltimateMegaSide = ir.registerIcon("kekztech:MegaUltimateEnergyUnit_side");
        iconUltimateMegaTop = ir.registerIcon("kekztech:MegaUltimateEnergyUnit_top");
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        // Multi casing
        par3List.add(new ItemStack(par1, 1, 0));
        // Empty capacitor
        par3List.add(new ItemStack(par1, 1, 6));
        // Lapo capacitors EV - UV
        par3List.add(new ItemStack(par1, 1, 7));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        // Ultimate battery
        par3List.add(new ItemStack(par1, 1, 5));
        // UEV Cap
        par3List.add(new ItemStack(par1, 1, 8));
        // UIV Cap
        par3List.add(new ItemStack(par1, 1, 9));
        // UMV Cap
        par3List.add(new ItemStack(par1, 1, 10));
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        switch (meta) {
            case 0:
                return (side < 2) ? iconBaseTop : iconBaseSide;
            case 1:
                return (side < 2) ? iconLapoIVTop : iconLapoIVSide;
            case 2:
                return (side < 2) ? iconLapoLuVTop : iconLapoLuVSide;
            case 3:
                return (side < 2) ? iconLapoZPMTop : iconLapoZPMSide;
            case 4:
                return (side < 2) ? iconLapoUVTop : iconLapoUVSide;
            case 5:
                return (side < 2) ? iconUltimateTop : iconUltimateSide;
            case 6:
                return (side < 2) ? iconLapoEmptyTop : iconLapoEmptySide;
            case 7:
                return (side < 2) ? iconLapoEVTop : iconLapoEVSide;
            case 8:
                return (side < 2) ? iconUltimateExtremeTop : iconUltimateExtremeSide;
            case 9:
                return (side < 2) ? iconUltimateInsaneTop : iconUltimateInsaneSide;
            case 10:
                return (side < 2) ? iconUltimateMegaTop : iconUltimateMegaSide;
            default:
                return iconUltimateTop;
        }
    }
}
