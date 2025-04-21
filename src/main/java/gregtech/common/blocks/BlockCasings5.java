package gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.translatedText;
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
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
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
import gregtech.common.config.Client;
import gregtech.common.render.GTRendererBlock;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings5 extends BlockCasingsAbstract implements IHeatingCoil, IBlockWithTextures {

    public static final Supplier<String> COIL_HEAT_TOOLTIP = translatedText("gt.coilheattooltip");
    public static final Supplier<String> COIL_UNIT_TOOLTIP = translatedText("gt.coilunittooltip");

    public static final int ACTIVE_OFFSET = 16;

    public BlockCasings5() {
        super(ItemCasings.class, "gt.blockcasings5", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Coil_Cupronickel, "Cupronickel Coil Block");
        register(1, ItemList.Casing_Coil_Kanthal, "Kanthal Coil Block");
        register(2, ItemList.Casing_Coil_Nichrome, "Nichrome Coil Block");
        register(3, ItemList.Casing_Coil_TungstenSteel, "TPV-Alloy Coil Block");
        register(4, ItemList.Casing_Coil_HSSG, "HSS-G Coil Block");
        register(5, ItemList.Casing_Coil_Naquadah, "Naquadah Coil Block");
        register(6, ItemList.Casing_Coil_NaquadahAlloy, "Naquadah Alloy Coil Block");
        register(7, ItemList.Casing_Coil_ElectrumFlux, "Electrum Flux Coil Block");
        register(8, ItemList.Casing_Coil_AwakenedDraconium, "Awakened Draconium Coil Block");
        register(9, ItemList.Casing_Coil_HSSS, "HSS-S Coil Block");
        register(10, ItemList.Casing_Coil_Trinium, "Trinium Coil Block");
        register(11, ItemList.Casing_Coil_Infinity, "Infinity Coil Block");
        register(12, ItemList.Casing_Coil_Hypogen, "Hypogen Coil Block");
        register(13, ItemList.Casing_Coil_Eternal, "Eternal Coil Block");
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
    public @Nullable ITexture[][] getTextures(int metadata) {
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
                default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL;
            };

            textures.add(TextureFactory.of(icon));
        } else {
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
                default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_BACKGROUND;
            };

            textures.add(
                TextureFactory.builder()
                    .addIcon(background)
                    .material(Blocks.stone)
                    .build());

            if (metadata >= ACTIVE_OFFSET) {
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

                textures.add(
                    TextureFactory.builder()
                        .addIcon(foreground)
                        .glow()
                        .material(Blocks.glowstone)
                        .build());
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

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);

        int metadata = stack.getItemDamage();

        HeatingCoilLevel coilLevel = BlockCasings5.getCoilHeatFromDamage(metadata);
        tooltip.add(COIL_HEAT_TOOLTIP.get() + coilLevel.getHeat() + COIL_UNIT_TOOLTIP.get());

        tooltip.add(StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", metadata + 1, "coil"));
    }
}
