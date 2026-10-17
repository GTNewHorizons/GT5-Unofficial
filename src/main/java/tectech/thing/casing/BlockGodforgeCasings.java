package tectech.thing.casing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.MaterialCasings;
import tectech.thing.CustomItemList;

public class BlockGodforgeCasings extends BlockCasingsAbstract {

    private static IIcon GodforgeTrim;
    private static IIcon GodforgeInner;
    private static IIcon GodforgeSupport;
    private static IIcon GodforgeOuter;
    private static IIcon GodforgeEnergy;
    private static IIcon GravitonModulatorT1;
    private static IIcon GravitonModulatorT2;
    private static IIcon GravitonModulatorT3;
    private static IIcon PhononConduit;
    private static IIcon ProtoHalkoniteSiphon;
    private static IIcon ProtoHalkoniteSiphonCracked;

    private static final byte START_INDEX = 64;

    public static final int PROTO_HALKONITE_SIPHON_META = 9;
    /** Not part of any structure. */
    public static final int PROTO_HALKONITE_SIPHON_CRACKED_META = 10;

    public BlockGodforgeCasings() {
        super(ItemCasingsGodforge.class, "gt.godforgecasing", MaterialCasings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[7][b + START_INDEX] = TextureFactory.of(this, b);
        }

        CustomItemList.Godforge_SingularityShieldingCasing.set(new ItemStack(this, 1, 0));
        CustomItemList.Godforge_GuidanceCasing.set(new ItemStack(this, 1, 1));
        CustomItemList.Godforge_BoundlessStructureCasing.set(new ItemStack(this, 1, 2));
        CustomItemList.Godforge_MagneticConfinementCasing.set(new ItemStack(this, 1, 3));
        CustomItemList.Godforge_StellarEnergySiphonCasing.set(new ItemStack(this, 1, 4));
        CustomItemList.Godforge_GravitonFlowModulatorTier1.set(new ItemStack(this, 1, 5));
        CustomItemList.Godforge_GravitonFlowModulatorTier2.set(new ItemStack(this, 1, 6));
        CustomItemList.Godforge_GravitonFlowModulatorTier3.set(new ItemStack(this, 1, 7));
        CustomItemList.Godforge_HarmonicPhononTransmissionConduit.set(new ItemStack(this, 1, 8));
        CustomItemList.Godforge_ProtoHalkoniteSiphonCasing.set(new ItemStack(this, 1, PROTO_HALKONITE_SIPHON_META));
        CustomItemList.Godforge_ProtoHalkoniteSiphonCasingCracked
            .set(new ItemStack(this, 1, PROTO_HALKONITE_SIPHON_CRACKED_META));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        GodforgeTrim = aIconRegister.registerIcon("gregtech:iconsets/GODFORGE_TRIM");
        GodforgeInner = aIconRegister.registerIcon("gregtech:iconsets/GODFORGE_INNER");
        GodforgeSupport = aIconRegister.registerIcon("gregtech:iconsets/GODFORGE_SUPPORT");
        GodforgeOuter = aIconRegister.registerIcon("gregtech:iconsets/GRAVITON_TOP_BOTTOM");
        GodforgeEnergy = aIconRegister.registerIcon("gregtech:iconsets/GODFORGE_ENERGY");
        GravitonModulatorT1 = aIconRegister.registerIcon("gregtech:iconsets/GRAVITON_CASING_2");
        GravitonModulatorT2 = aIconRegister.registerIcon("gregtech:iconsets/GRAVITON_CASING_1");
        GravitonModulatorT3 = aIconRegister.registerIcon("gregtech:iconsets/GRAVITON_CASING_0");
        PhononConduit = aIconRegister.registerIcon("gregtech:iconsets/PHONON_CONDUIT");
        ProtoHalkoniteSiphon = aIconRegister.registerIcon("gregtech:iconsets/GODFORGE_PROTOHALKONITE_SIPHON");
        ProtoHalkoniteSiphonCracked = aIconRegister
            .registerIcon("gregtech:iconsets/GODFORGE_PROTOHALKONITE_SIPHON_CRACKED");
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return GodforgeTrim;
            case 1:
                return GodforgeInner;
            case 2:
                return GodforgeSupport;
            case 3:
                return GodforgeOuter;
            case 4:
                return GodforgeEnergy;
            case 5:
                if (aSide < 2) {
                    return GodforgeOuter;
                }
                return GravitonModulatorT1;
            case 6:
                if (aSide < 2) {
                    return GodforgeOuter;
                }
                return GravitonModulatorT2;
            case 7:
                if (aSide < 2) {
                    return GodforgeOuter;
                }
                return GravitonModulatorT3;
            case 8:
                return PhononConduit;
            case PROTO_HALKONITE_SIPHON_META:
                return ProtoHalkoniteSiphon;
            case PROTO_HALKONITE_SIPHON_CRACKED_META:
                return ProtoHalkoniteSiphonCracked;
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        if (metadata == PROTO_HALKONITE_SIPHON_CRACKED_META) {
            return new ArrayList<>();
        }
        return super.getDrops(world, x, y, z, metadata, fortune);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> aList) {
        for (int i = 0; i <= PROTO_HALKONITE_SIPHON_CRACKED_META; i++) {
            aList.add(new ItemStack(item, 1, i));
        }
    }
}
