package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.function.Consumer;

import static gregtech.api.enums.HeatingCoilLevel.*;

public class GT_Block_Casings5 extends GT_Block_Casings_Abstract implements IHeatingCoil {

    public GT_Block_Casings5() {
        super(GT_Item_Casings5.class, "gt.blockcasings5", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Textures.BlockIcons.casingTexturePages[1][i] = TextureFactory.of(this, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Cupronickel Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Kanthal Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Nichrome Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Tungstensteel Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "HSS-G Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Naquadah Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Naquadah Alloy Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Electrum Flux Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Awakened Draconium Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "HSS-S Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Trinium Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Infinity Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Hypogen Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Eternal Coil Block");

        
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
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
            case 1:
                return Textures.BlockIcons.MACHINE_COIL_KANTHAL.getIcon();
            case 2:
                return Textures.BlockIcons.MACHINE_COIL_NICHROME.getIcon();
            case 3:
                return Textures.BlockIcons.MACHINE_COIL_TUNGSTENSTEEL.getIcon();
            case 4:
                return Textures.BlockIcons.MACHINE_COIL_HSSG.getIcon();
            case 5:
                return Textures.BlockIcons.MACHINE_COIL_NAQUADAH.getIcon();
            case 6:
                return Textures.BlockIcons.MACHINE_COIL_NAQUADAHALLOY.getIcon();
            case 7:
                return Textures.BlockIcons.MACHINE_COIL_ELECTRUMFLUX.getIcon();
            case 8:
                return Textures.BlockIcons.MACHINE_COIL_AWAKENEDDRACONIUM.getIcon();
            case 9:
                return Textures.BlockIcons.MACHINE_COIL_HSSS.getIcon();
            case 10:
                return Textures.BlockIcons.MACHINE_COIL_TRINIUM.getIcon();
            case 11:
                return Textures.BlockIcons.MACHINE_COIL_INFINITY.getIcon();
            case 12:
                return Textures.BlockIcons.MACHINE_COIL_HYPOGEN.getIcon();
            case 13:
                return Textures.BlockIcons.MACHINE_COIL_ETERNAL.getIcon();
        }
        return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
    }

    /*--------------- COIL CHECK IMPL. ------------*/

    public static HeatingCoilLevel getCoilHeatFromDamage(int meta) {
        switch (meta) {
            case 0:
                return LV;
            case 1:
                return MV;
            case 2:
                return HV;
            case 3:
                return EV;
            case 4:
                return IV;
            case 5:
                return ZPM;
            case 6:
                return UV;
            case 7:
                return UEV;
            case 8:
                return UIV;
            case 9:
                return LuV;
            case 10:
                return UHV;
            case 11:
                return UMV;
            case 12:
                return UXV;
            case 13:
                return MAX;
            default:
                return None;
        }
    }

    public static int getMetaFromCoilHeat(HeatingCoilLevel level) {
        switch (level) {
            case LV:
                return 0;
            case MV:
                return 1;
            case HV:
                return 2;
            case EV:
                return 3;
            case IV:
                return 4;
            case ZPM:
                return 5;
            case UV:
                return 6;
            case UEV:
                return 7;
            case UIV:
                return 8;
            case LuV:
                return 9;
            case UHV:
                return 10;
            case UMV:
                return 11;
            case UXV:
                return 12;
            case MAX:
                return 13;
            default:
                return 0;
        }
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
