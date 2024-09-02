package gregtech.common.blocks;

import static gregtech.api.enums.HeatingCoilLevel.EV;
import static gregtech.api.enums.HeatingCoilLevel.HV;
import static gregtech.api.enums.HeatingCoilLevel.IV;
import static gregtech.api.enums.HeatingCoilLevel.LV;
import static gregtech.api.enums.HeatingCoilLevel.LuV;
import static gregtech.api.enums.HeatingCoilLevel.MAX;
import static gregtech.api.enums.HeatingCoilLevel.MV;
import static gregtech.api.enums.HeatingCoilLevel.None;
import static gregtech.api.enums.HeatingCoilLevel.UEV;
import static gregtech.api.enums.HeatingCoilLevel.UHV;
import static gregtech.api.enums.HeatingCoilLevel.UIV;
import static gregtech.api.enums.HeatingCoilLevel.UMV;
import static gregtech.api.enums.HeatingCoilLevel.UV;
import static gregtech.api.enums.HeatingCoilLevel.UXV;
import static gregtech.api.enums.HeatingCoilLevel.ZPM;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.util.GTLanguageManager;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings5 extends BlockCasingsAbstract implements IHeatingCoil {

    public BlockCasings5() {
        super(ItemCasings5.class, "gt.blockcasings5", MaterialCasings.INSTANCE, 16);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Cupronickel Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Kanthal Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Nichrome Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "TPV-Alloy Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "HSS-G Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Naquadah Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Naquadah Alloy Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Electrum Flux Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Awakened Draconium Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "HSS-S Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Trinium Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Infinity Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Hypogen Coil Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Eternal Coil Block");

        ItemList.Casing_Coil_Cupronickel.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Coil_Kanthal.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Coil_Nichrome.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Coil_TungstenSteel.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Coil_HSSG.set(new ItemStack(this, 1, 4));
        ItemList.Casing_Coil_Naquadah.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Coil_NaquadahAlloy.set(new ItemStack(this, 1, 6));
        ItemList.Casing_Coil_ElectrumFlux.set(new ItemStack(this, 1, 7));
        ItemList.Casing_Coil_AwakenedDraconium.set(new ItemStack(this, 1, 8));
        ItemList.Casing_Coil_HSSS.set(new ItemStack(this, 1, 9));
        ItemList.Casing_Coil_Trinium.set(new ItemStack(this, 1, 10));
        ItemList.Casing_Coil_Infinity.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Coil_Hypogen.set(new ItemStack(this, 1, 12));
        ItemList.Casing_Coil_Eternal.set(new ItemStack(this, 1, 13));
        ItemList.Casing_Shielded_Accelerator.set(new ItemStack(this, 1, 14));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | aMeta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_COIL_KANTHAL.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_COIL_NICHROME.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_COIL_TUNGSTENSTEEL.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_COIL_HSSG.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAH.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAHALLOY.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_COIL_ELECTRUMFLUX.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_COIL_AWAKENEDDRACONIUM.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_COIL_HSSS.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_COIL_TRINIUM.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_COIL_INFINITY.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_COIL_HYPOGEN.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_COIL_ETERNAL.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_SHIELDED_ACCELERATOR.getIcon();
            default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
        };
    }

    /*--------------- COIL CHECK IMPL. ------------*/

    public static HeatingCoilLevel getCoilHeatFromDamage(int meta) {
        return switch (meta) {
            case 0 -> LV;
            case 1 -> MV;
            case 2 -> HV;
            case 3 -> EV;
            case 4 -> IV;
            case 5 -> ZPM;
            case 6 -> UV;
            case 7 -> UEV;
            case 8 -> UIV;
            case 9 -> LuV;
            case 10 -> UHV;
            case 11 -> UMV;
            case 12 -> UXV;
            case 13 -> MAX;
            default -> None;
        };
    }

    public static int getMetaFromCoilHeat(HeatingCoilLevel level) {
        return switch (level) {
            case LV -> 0;
            case MV -> 1;
            case HV -> 2;
            case EV -> 3;
            case IV -> 4;
            case ZPM -> 5;
            case UV -> 6;
            case UEV -> 7;
            case UIV -> 8;
            case LuV -> 9;
            case UHV -> 10;
            case UMV -> 11;
            case UXV -> 12;
            case MAX -> 13;
            default -> 0;
        };
    }

    @Override
    public HeatingCoilLevel getCoilHeat(int meta) {
        getOnCoilCheck().accept(this);
        return getCoilHeatFromDamage(meta);
    }

    /*--------------- CALLBACK ------------*/

    private Consumer<IHeatingCoil> callback = coil -> {};

    @Override
    public void setOnCoilCheck(Consumer<IHeatingCoil> callback) {
        this.callback = callback;
    }

    @Override
    public Consumer<IHeatingCoil> getOnCoilCheck() {
        return this.callback;
    }
}
