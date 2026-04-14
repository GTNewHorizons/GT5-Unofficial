package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocks extends GregtechMetaCasingBlocksAbstract {

    public GregtechMetaCasingBlocks() {
        super(GregtechMetaCasingItems.class, "miscutils.blockcasings", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            if (i == 2 || i == 3 || i == 4) {
                continue;
            }
            TAE.registerTexture(0, i, TextureFactory.of(this, i));
        }
        GregtechItemList.Casing_Centrifuge1.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_CokeOven.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_CokeOven_Coil1.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_CokeOven_Coil2.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_MaterialPress.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Electrolyzer.set(new ItemStack(this, 1, 5));
        GregtechItemList.Casing_WireFactory.set(new ItemStack(this, 1, 6));
        GregtechItemList.Casing_MacerationStack.set(new ItemStack(this, 1, 7));
        GregtechItemList.Casing_MatterGen.set(new ItemStack(this, 1, 8));
        GregtechItemList.Casing_MatterFab.set(new ItemStack(this, 1, 9));
        GregtechItemList.Casing_IronPlatedBricks.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_MultitankExterior.set(new ItemStack(this, 1, 11));
        GregtechItemList.Casing_Reactor_I.set(new ItemStack(this, 1, 12));
        GregtechItemList.Casing_Reactor_II.set(new ItemStack(this, 1, 13));
        GregtechItemList.Casing_Coil_BlastSmelter.set(new ItemStack(this, 1, 14));
        GregtechItemList.Casing_BlastSmelter.set(new ItemStack(this, 1, 15));
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) { // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                // Centrifuge
                case 0 -> TexturesGtBlock.Casing_Material_Centrifuge.getIcon();
                // Coke Oven Frame
                case 1 -> TexturesGtBlock.Casing_Material_Tantalloy61.getIcon();
                // Coke Oven Casing Tier 1
                case 2 -> Textures.BlockIcons.MACHINE_CASING_FIREBOX_BRONZE.getIcon();
                // Coke Oven Casing Tier 2
                case 3 -> Textures.BlockIcons.MACHINE_CASING_FIREBOX_STEEL.getIcon();
                // Material Press Casings
                case 4 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
                // Electrolyzer Casings
                case 5 -> TexturesGtBlock.Casing_Material_Potin.getIcon();
                // Broken Blue Fusion Casings
                case 6 -> TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
                // Maceration Stack Casings
                case 7 -> TexturesGtBlock.Casing_Material_Tumbaga.getIcon();
                // Broken Pink Fusion Casings
                case 8 -> Textures.BlockIcons.MATTER_GENERATION_COIL.getIcon();
                // Matter Fabricator Casings
                case 9 -> Textures.BlockIcons.MATTER_FABRICATOR_CASING.getIcon();
                // Iron Blast Fuance Textures
                case 10 -> TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
                // Multitank Exterior Casing
                case 11 -> TexturesGtBlock.Casing_Material_Grisium.getIcon();
                // Reactor Casing I
                case 12 -> TexturesGtBlock.Casing_Material_Stellite.getIcon();
                // Reactor Casing II
                case 13 -> TexturesGtBlock.Casing_Material_Zeron100.getIcon();
                case 14 -> TexturesGtBlock.Casing_Staballoy_Firebox.getIcon();
                case 15 -> TexturesGtBlock.Casing_Material_ZirconiumCarbide.getIcon();
                default -> Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();
            };
        }
        return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta != 0) {
            return getIcon(side, meta);
        }
        return TexturesGtBlock.Casing_Material_Centrifuge.getIcon();
    }
}
