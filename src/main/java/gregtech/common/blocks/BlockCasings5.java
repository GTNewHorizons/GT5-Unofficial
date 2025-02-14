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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.config.Client;
import gregtech.common.render.GTRendererBlock;
import net.coderbot.iris.api.IIrisAware;
import net.coderbot.iris.api.IIrisRenderBlocks;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings5 extends BlockCasingsAbstract implements IHeatingCoil, IBlockWithTextures, IIrisAware {

    public static final int ACTIVE_OFFSET = 16;

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
    public int damageDropped(int metadata) {
        return super.damageDropped(metadata) % ACTIVE_OFFSET;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return super.getDamageValue(aWorld, aX, aY, aZ) % ACTIVE_OFFSET;
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | (aMeta % ACTIVE_OFFSET);
    }

    @Override
    public int getSubpassCount(int meta) {
        if (Client.render.useOldCoils) {
            return 1;
        } else {
            return switch (meta % ACTIVE_OFFSET) {
                case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 -> 2;
                default -> 1;
            };
        }
    }

    @Override
    public @Nullable ITexture[][] getTextures(int metadata) {
        // can be stubbed because the other getTextures delegates to this method
        return new ITexture[0][];
    }

    @Override
    public @Nullable ITexture[][] getTextures(RenderBlocks rb, int metadata) {
        List<ITexture> textures = new ArrayList<>();

        if (Client.render.useOldCoils) {
            IIconContainer icon = switch (metadata % ACTIVE_OFFSET) {
                case 0 -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL;
                case 1 -> Textures.BlockIcons.MACHINE_COIL_KANTHAL;
                case 2 -> Textures.BlockIcons.MACHINE_COIL_NICHROME;
                case 3 -> Textures.BlockIcons.MACHINE_COIL_TUNGSTENSTEEL;
                case 4 -> Textures.BlockIcons.MACHINE_COIL_HSSG;
                case 5 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAH;
                case 6 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAHALLOY;
                case 7 -> Textures.BlockIcons.MACHINE_COIL_ELECTRUMFLUX;
                case 8 -> Textures.BlockIcons.MACHINE_COIL_AWAKENEDDRACONIUM;
                case 9 -> Textures.BlockIcons.MACHINE_COIL_HSSS;
                case 10 -> Textures.BlockIcons.MACHINE_COIL_TRINIUM;
                case 11 -> Textures.BlockIcons.MACHINE_COIL_INFINITY;
                case 12 -> Textures.BlockIcons.MACHINE_COIL_HYPOGEN;
                case 13 -> Textures.BlockIcons.MACHINE_COIL_ETERNAL;
                case 14 -> Textures.BlockIcons.MACHINE_CASING_SHIELDED_ACCELERATOR;
                default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL;
            };

            textures.add(TextureFactory.of(icon));
        } else {
            int subpass = -1;

            if (rb instanceof IIrisRenderBlocks renderBlocks) {
                subpass = renderBlocks.getCurrentSubpass();
                switch (subpass) {
                    case 0 -> renderBlocks.setShaderMaterialId(this, metadata % ACTIVE_OFFSET);
                    case 1 -> renderBlocks.setShaderMaterialId(this, metadata);
                }
            }

            if (subpass == -1 || subpass == 0) {
                IIconContainer background = switch (metadata % ACTIVE_OFFSET) {
                    case 0 -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_BACKGROUND;
                    case 1 -> Textures.BlockIcons.MACHINE_COIL_KANTHAL_BACKGROUND;
                    case 2 -> Textures.BlockIcons.MACHINE_COIL_NICHROME_BACKGROUND;
                    case 3 -> Textures.BlockIcons.MACHINE_COIL_TUNGSTENSTEEL_BACKGROUND;
                    case 4 -> Textures.BlockIcons.MACHINE_COIL_HSSG_BACKGROUND;
                    case 5 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAH_BACKGROUND;
                    case 6 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAHALLOY_BACKGROUND;
                    case 7 -> Textures.BlockIcons.MACHINE_COIL_ELECTRUMFLUX_BACKGROUND;
                    case 8 -> Textures.BlockIcons.MACHINE_COIL_AWAKENEDDRACONIUM_BACKGROUND;
                    case 9 -> Textures.BlockIcons.MACHINE_COIL_HSSS_BACKGROUND;
                    case 10 -> Textures.BlockIcons.MACHINE_COIL_TRINIUM_BACKGROUND;
                    case 11 -> Textures.BlockIcons.MACHINE_COIL_INFINITY_BACKGROUND;
                    case 12 -> Textures.BlockIcons.MACHINE_COIL_HYPOGEN_BACKGROUND;
                    case 13 -> Textures.BlockIcons.MACHINE_COIL_ETERNAL_BACKGROUND;
                    case 14 -> Textures.BlockIcons.MACHINE_CASING_SHIELDED_ACCELERATOR;
                    default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_BACKGROUND;
                };

                textures.add(TextureFactory.of(background));
            }

            if (metadata >= ACTIVE_OFFSET && (subpass == -1 || subpass == 1)) {
                IIconContainer foreground = switch (metadata % ACTIVE_OFFSET) {
                    case 0 -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_FOREGROUND;
                    case 1 -> Textures.BlockIcons.MACHINE_COIL_KANTHAL_FOREGROUND;
                    case 2 -> Textures.BlockIcons.MACHINE_COIL_NICHROME_FOREGROUND;
                    case 3 -> Textures.BlockIcons.MACHINE_COIL_TUNGSTENSTEEL_FOREGROUND;
                    case 4 -> Textures.BlockIcons.MACHINE_COIL_HSSG_FOREGROUND;
                    case 5 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAH_FOREGROUND;
                    case 6 -> Textures.BlockIcons.MACHINE_COIL_NAQUADAHALLOY_FOREGROUND;
                    case 7 -> Textures.BlockIcons.MACHINE_COIL_ELECTRUMFLUX_FOREGROUND;
                    case 8 -> Textures.BlockIcons.MACHINE_COIL_AWAKENEDDRACONIUM_FOREGROUND;
                    case 9 -> Textures.BlockIcons.MACHINE_COIL_HSSS_FOREGROUND;
                    case 10 -> Textures.BlockIcons.MACHINE_COIL_TRINIUM_FOREGROUND;
                    case 11 -> Textures.BlockIcons.MACHINE_COIL_INFINITY_FOREGROUND;
                    case 12 -> Textures.BlockIcons.MACHINE_COIL_HYPOGEN_FOREGROUND;
                    case 13 -> Textures.BlockIcons.MACHINE_COIL_ETERNAL_FOREGROUND;
                    default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_FOREGROUND;
                };

                textures.add(TextureFactory.builder().addIcon(foreground).glow().build());
            }
        }

        ITexture[] layers = textures.toArray(new ITexture[0]);

        return new ITexture[][] { layers, layers, layers, layers, layers, layers };
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.mRenderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /*--------------- COIL CHECK IMPL. ------------*/

    public static HeatingCoilLevel getCoilHeatFromDamage(int meta) {
        return switch (meta % ACTIVE_OFFSET) {
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
        return getCoilHeatFromDamage(meta % ACTIVE_OFFSET);
    }
}
