package gregtech.common.tileentities.machines.multi.fuelboilers;

import static gregtech.api.GregTech_API.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorOmni;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GT_StructureUtility.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.CrossAxisAlignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.IWindowCreator;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.*;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;

public class LargeFiretube extends FueledBoiler<LargeFiretube> implements ISurvivalConstructable {

    private int tierWall = -1;
    private int tierPipe = -1;
    private int tierHotplate = -1;
    private int tierFirebox = -1;
    private int water, heat, steam = 0;
    private float waterMax = 10000;
    private float heatMax = 10000;
    private float steamMax = waterMax + heatMax;

    // There's only one piece to this structure... for now >:)
    // TODO: multiple boiler chambers + superheater
    private static final String MAIN_PIECE_NAME = "main";
    private static final int CASING_TEXTURE_INDEX = ((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings2)
        .getTextureIndex(10);

    private static final int X_OFFSET = 1;
    private static final int Y_OFFSET = 5;
    private static final int Z_OFFSET = 2;
    private static final String[][] structure =
        // spotless:off
        new String[][] {
            { "             ", "             ", "     BBBBB   ", "     BBBBB   ", "     BBBBB   ", "     F   F   " , "     F   F   " },
            { "             ", "    BBBBBBB  ", "    BH----B  ", "    BH----B  ", "    BH----B  ", "    BBBBBBB  " , "             " },
            { "     BBBBB   ", "    BH----B  ", "   T-PPPPPBBB", "   T-H----BBB", "   T-PPPPPBBB", " ~  BH----B  " , "FZF  BBBBB   " },
            { "     BBSBB   ", "    BH----B  ", "   T-H----BBM", " PPP-H----BBW", " P T-H----BBB", "EP  BH----B  " , "FFF  BBBBB   " },
            { "     BBBBB   ", "    BH----B  ", "   T-PPPPPBBB", "   T-H----BBB", "   T-PPPPPBBB", "    BH----B  " , " F   BBBBB   " },
            { "             ", "    BBBBBBB  ", "    BH----B  ", "    BH----B  ", "    BH----B  ", "    BBBBBBB  " , "             " },
            { "             ", "             ", "     BBBBB   ", "     BBBBB   ", "     BBBBB   ", "     F   F   " , "     F   F   " } };
    // spotless:on

    private static final IStructureDefinition<LargeFiretube> STRUCTURE_DEFINITION = StructureDefinition
        .<LargeFiretube>builder()
        .addShape(MAIN_PIECE_NAME, structure)
        // IO
        // Fuel in
        .addElement(
            'E',
            GT_StructureUtility.<LargeFiretube>buildHatchAdder()
                .atLeast(InputHatch)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Water in
        .addElement(
            'W',
            GT_StructureUtility.<LargeFiretube>buildHatchAdder()
                .atLeast(InputHatch)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Pollution out
        .addElement(
            'M',
            GT_StructureUtility.<LargeFiretube>buildHatchAdder()
                .atLeast(Muffler)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Steam out
        .addElement(
            'S',
            GT_StructureUtility.<LargeFiretube>buildHatchAdder()
                .atLeast(OutputHatch)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Tools in
        .addElement(
            'Z',
            GT_StructureUtility.<LargeFiretube>buildHatchAdder()
                .atLeast(Maintenance)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Building blocks
        // Invar frame
        .addElement('F', ofFrame(Materials.Invar))
        // Bronze plated bricks
        // TODO: OR glass
        .addElement(
            'B',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierCasing,
                ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                -1,
                (t, m) -> t.tierWall = m,
                t -> t.tierWall))
        // Pipe casing
        .addElement(
            'P',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierPipe,
                ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                -1,
                (t, m) -> t.tierPipe = m,
                t -> t.tierPipe))
        // Hotplate - every other tier
        .addElement(
            'H',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierHotplate,
                ImmutableList.of(Pair.of(sBlockMetal1, 3)),
                -1,
                (t, m) -> t.tierHotplate = m,
                t -> t.tierHotplate))
        // Firebox
        .addElement(
            'T',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierFirebox,
                ImmutableList.of(Pair.of(sBlockCasings3, 13), Pair.of(sBlockCasings3, 14)),
                -1,
                (t, m) -> t.tierFirebox = m,
                t -> t.tierFirebox))
        .build();

    public LargeFiretube(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }

    protected LargeFiretube(String name) {
        super(name);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Validate structure
        return checkPiece(MAIN_PIECE_NAME, X_OFFSET, Y_OFFSET, Z_OFFSET);

        // TODO: check for glass amount
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(MAIN_PIECE_NAME, stackSize, hintsOnly, X_OFFSET, Y_OFFSET, Z_OFFSET);
    }

    @Override
    public IStructureDefinition<LargeFiretube> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Boiler")
            .addInfo("Controller block for the Large Firetube Boiler")
            .addInfo("Burns fuels to generate steam efficiently")
            .addInfo("Each tier allows higher heat and 4X throughput")
            .beginStructureBlock(13, 7, 7, false)
            .addInfo(AuthorOmni.get())
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeFiretube(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_INDEX) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity thiz, long tick) {
        super.onPostTick(thiz, tick);
        water = (int) ((Math.sin(tick / 20D) + 1) * waterMax / 2);
        heat = (int) ((Math.cos(tick / 20D) + 1) * heatMax / 2);
        steam = water + heat;
    }

    /**
     * The GT large boilers produced 1000 pollution/s/tier. This one will produce 1 pollution per 1L of wasted fuel,
     */
    @Override
    public int getPollutionPerSecond(ItemStack stack) {
        return 0;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(10, this.createMonitorWindow);
        builder.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(10);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    // TODO: some flame texture? or steam?
                    ret.add(GT_UITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip("Boiler Monitor")
                .setPos(174, 130));
    }

    private final IWindowCreator createMonitorWindow = (EntityPlayer ignored) -> {
        final int pBarOffset = 30;
        final int pBarPad = 10;
        final ModularWindow.Builder b = ModularWindow.builder(190, 50);
        b.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        b.setGuiTint(getGUIColorization());

        b.widget(new TextWidget("Boiler Monitor").setPos(5, 5));
        b.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(185, 3));
        b.widget(
            new DynamicPositionedColumn().setSynced(false)
                .widget(
                    new MultiChildWidget().addChild(new TextWidget("Water"))
                        .addChild(
                            new ProgressBar().setProgress(() -> water / waterMax)
                                .setDirection(ProgressBar.Direction.RIGHT)
                                .setTexture(
                                    GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM_R90
                                        .get(tierPipe == 1 ? SteamVariant.BRONZE : SteamVariant.STEEL),
                                    GT_UITextures.PROGRESSBAR_BOILER_WATER_R90,
                                    54)
                                .setPos(pBarOffset, 0)
                                .setSizeProvider(
                                    (screenSize, window,
                                        parent) -> new Size(window.getSize().width - pBarOffset - pBarPad, 10))))
                .widget(
                    new MultiChildWidget().addChild(new TextWidget("Heat"))
                        .addChild(
                            new ProgressBar().setProgress(() -> heat / heatMax)
                                .setDirection(ProgressBar.Direction.RIGHT)
                                .setTexture(
                                    GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM_R90
                                        .get(tierPipe == 1 ? SteamVariant.BRONZE : SteamVariant.STEEL),
                                    GT_UITextures.PROGRESSBAR_BOILER_HEAT_R90,
                                    54)
                                .setPos(pBarOffset, 0)
                                .setSizeProvider(
                                    (screenSize, window,
                                        parent) -> new Size(window.getSize().width - pBarOffset - pBarPad, 10))))
                .widget(
                    new MultiChildWidget().addChild(new TextWidget("Steam"))
                        .addChild(
                            new ProgressBar().setProgress(() -> steam / steamMax)
                                .setDirection(ProgressBar.Direction.RIGHT)
                                .setTexture(
                                    GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM_R90
                                        .get(tierPipe == 1 ? SteamVariant.BRONZE : SteamVariant.STEEL),
                                    GT_UITextures.PROGRESSBAR_BOILER_STEAM_R90,
                                    54)
                                .setPos(pBarOffset, 0)
                                .setSizeProvider(
                                    (screenSize, window,
                                        parent) -> new Size(window.getSize().width - pBarOffset - pBarPad, 10))))
                .setAlignment(CrossAxisAlignment.END)
                .setPos(5, 15));

        return b.build();
    };
}
