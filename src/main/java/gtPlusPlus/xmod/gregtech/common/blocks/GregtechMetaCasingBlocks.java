package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler;

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
        return CasingTextureHandler.getIcon(ordinalSide, aMeta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final int ordinalSide) {
        final GregtechMetaCasingBlocks i = this;
        return CasingTextureHandler
            .handleCasingsGT(aWorld, xCoord, yCoord, zCoord, ForgeDirection.getOrientation(ordinalSide), i);
    }
}
