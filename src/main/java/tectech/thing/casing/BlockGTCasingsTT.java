package tectech.thing.casing;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.MaterialCasings;
import tectech.TecTech;
import tectech.thing.CustomItemList;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class BlockGTCasingsTT extends BlockCasingsAbstract {

    public static final byte texturePage = TecTech.tectechTexturePage1;
    public static final short textureOffset = texturePage << 7; // Start of PAGE 8 (which is the 9th page) (8*128)
    private static IIcon eM0, eM1, eM1s, eM2, eM2s, eM3, eM3s, eM4, eM5, eM6, eM7, eM7s, eM8, eM9, eM10, eM11, eM12,
        eM13, eM14;
    private static final IIcon[] debug = new IIcon[6];

    public BlockGTCasingsTT() {
        super(ItemCasingsTT.class, "gt.blockcasingsTT", MaterialCasings.INSTANCE);
        setCreativeTab(TecTech.creativeTabTecTech);

        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[texturePage][b] = TextureFactory.of(this, b);
            /* IMPORTANT for block recoloring **/
        }

        CustomItemList.eM_Power.set(new ItemStack(this, 1, 0));

        CustomItemList.eM_Computer_Casing.set(new ItemStack(this, 1, 1));
        CustomItemList.eM_Computer_Vent.set(new ItemStack(this, 1, 2));
        CustomItemList.eM_Computer_Bus.set(new ItemStack(this, 1, 3));

        CustomItemList.eM_Containment.set(new ItemStack(this, 1, 4));
        CustomItemList.eM_Containment_Advanced.set(new ItemStack(this, 1, 5));
        CustomItemList.eM_Containment_Field.set(new ItemStack(this, 1, 6));

        CustomItemList.eM_Coil.set(new ItemStack(this, 1, 7));
        CustomItemList.eM_Hollow.set(new ItemStack(this, 1, 8));
        CustomItemList.eM_Spacetime.set(new ItemStack(this, 1, 9));

        CustomItemList.eM_Teleportation.set(new ItemStack(this, 1, 10));
        CustomItemList.eM_Dimensional.set(new ItemStack(this, 1, 11));

        CustomItemList.eM_Ultimate_Containment.set(new ItemStack(this, 1, 12));
        CustomItemList.eM_Ultimate_Containment_Advanced.set(new ItemStack(this, 1, 13));
        CustomItemList.eM_Ultimate_Containment_Field.set(new ItemStack(this, 1, 14));

        CustomItemList.debugBlock.set(new ItemStack(this, 1, 15));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        // super.registerBlockIcons(aIconRegister);
        eM0 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");

        eM1 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_NONSIDE");
        eM1s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC");
        eM2 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_VENT_NONSIDE");
        eM2s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_VENT");
        eM3 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_ADV_NONSIDE");
        eM3s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_ADV");

        eM4 = aIconRegister.registerIcon("gregtech:iconsets/EM_CASING");
        eM5 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD_CASING");
        eM6 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD");

        eM7 = aIconRegister.registerIcon("gregtech:iconsets/EM_COIL_NONSIDE");
        eM7s = aIconRegister.registerIcon("gregtech:iconsets/EM_COIL");
        eM8 = aIconRegister.registerIcon("gregtech:iconsets/EM_HOLLOW");
        eM9 = aIconRegister.registerIcon("gregtech:iconsets/EM_TIMESPACE");

        eM10 = aIconRegister.registerIcon("gregtech:iconsets/EM_TELE");
        eM11 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM");

        eM12 = aIconRegister.registerIcon("gregtech:iconsets/EM_ULTIMATE_CASING");
        eM13 = aIconRegister.registerIcon("gregtech:iconsets/EM_ULTIMATE_FIELD_CASING");
        eM14 = aIconRegister.registerIcon("gregtech:iconsets/EM_ULTIMATE_FIELD");

        debug[0] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_0");
        debug[1] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_1");
        debug[2] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_2");
        debug[3] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_3");
        debug[4] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_4");
        debug[5] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_5");
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> eM0;
            case 1 -> ordinalSide < 2 ? eM1 : eM1s;
            case 2 -> ordinalSide < 2 ? eM2 : eM2s;
            case 3 -> ordinalSide < 2 ? eM3 : eM3s;
            case 4 -> eM4;
            case 5 -> eM5;
            case 6 -> eM6;
            case 7 -> ordinalSide < 2 ? eM7 : eM7s;
            case 8 -> eM8;
            case 9 -> eM9;
            case 10 -> eM10;
            case 11 -> eM11;
            case 12 -> eM12;
            case 13 -> eM13;
            case 14 -> eM14;
            case 15 -> debug[ordinalSide];
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        for (int i = 0; i <= 15; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
