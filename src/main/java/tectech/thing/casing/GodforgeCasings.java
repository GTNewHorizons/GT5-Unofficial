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
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;
import tectech.thing.CustomItemList;

public class GodforgeCasings extends GT_Block_Casings_Abstract {

    private static IIcon GodforgeTrim;
    private static IIcon GodforgeInner;
    private static IIcon GodforgeSupport;
    private static IIcon GodforgeOuter;
    private static IIcon GodforgeEnergy;
    private static IIcon GravitonModulatorT1;
    private static IIcon GravitonModulatorT2;
    private static IIcon GravitonModulatorT3;
    private static IIcon PhononConduit;

    private static final byte START_INDEX = 64;

    public GodforgeCasings() {
        super(GT_Item_Casings_Godforge.class, "gt.godforgecasing", GT_Material_Casings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[7][b + START_INDEX] = new GT_CopiedBlockTexture(this, 6, b);
        }

        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".0.name", "Singularity Reinforced Stellar Shielding Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Celestial Matter Guidance Casing");
        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName() + ".2.name",
            "Boundless Gravitationally Severed Structure Casing");
        GT_LanguageManager.addStringLocalization(
            getUnlocalizedName() + ".3.name",
            "Transcendentally Amplified Magnetic Confinement Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Stellar Energy Siphon Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Remote Graviton Flow Modulator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Medial Graviton Flow Modulator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Central Graviton Flow Modulator");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".8.name", "Harmonic Phonon Transmission Conduit");

        CustomItemList.Godforge_SingularityShieldingCasing.set(new ItemStack(this, 1, 0));
        CustomItemList.Godforge_GuidanceCasing.set(new ItemStack(this, 1, 1));
        CustomItemList.Godforge_BoundlessStructureCasing.set(new ItemStack(this, 1, 2));
        CustomItemList.Godforge_MagneticConfinementCasing.set(new ItemStack(this, 1, 3));
        CustomItemList.Godforge_StellarEnergySiphonCasing.set(new ItemStack(this, 1, 4));
        CustomItemList.Godforge_GravitonFlowModulatorTier1.set(new ItemStack(this, 1, 5));
        CustomItemList.Godforge_GravitonFlowModulatorTier2.set(new ItemStack(this, 1, 6));
        CustomItemList.Godforge_GravitonFlowModulatorTier3.set(new ItemStack(this, 1, 7));
        CustomItemList.Godforge_HarmonicPhononTransmissionConduit.set(new ItemStack(this, 1, 8));
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
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide, tMeta);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 8; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
