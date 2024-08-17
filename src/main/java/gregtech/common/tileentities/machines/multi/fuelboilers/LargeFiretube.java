package gregtech.common.tileentities.machines.multi.fuelboilers;

import static gregtech.api.GregTech_API.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorOmni;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GT_StructureUtility.*;

import appeng.core.Api;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;

/**
 * TODO: add throttling.
 */
public class LargeFiretube extends FueledBoiler<LargeFiretube> implements ISurvivalConstructable {

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
                .atLeast(
                    InputHatch.withAdder(
                        (thiz, gtTE, baseCasingIndex) -> thiz
                            .addHatchWithRecipeMap(gtTE, baseCasingIndex, thiz.getRecipeMap())))
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Water in
        .addElement(
            'W',
            GT_StructureUtility.<LargeFiretube>buildHatchAdder()
                .atLeast(
                    InputHatch.withAdder(
                        (thiz, gtTE, baseCasingIndex) -> thiz
                            .addHatchWithRecipeMap(gtTE, baseCasingIndex, RecipeMaps.waterOnly)))
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
            StructureUtility.ofChain(
                StructureUtility.ofBlocksTiered(
                    FueledBoiler::getTierCasing,
                    ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                    0,
                    (t, m) -> t.tier = m,
                    t -> t.tier),
                // I have one question for AE2.... W H Y ? ! ?
                StructureUtility.ofBlock(Api.INSTANCE.definitions().blocks().quartzGlass().maybeBlock().or(Blocks.glass), 0))
            )
        // Pipe casing
        .addElement(
            'P',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierPipe,
                ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                0,
                (t, m) -> t.tier = m,
                t -> t.tier))
        // Hotplate - every other tier
        .addElement(
            'H',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierHotplate,
                ImmutableList.of(Pair.of(sBlockMetal1, 3)),
                0,
                (t, m) -> t.tier = m,
                t -> t.tier))
        // Firebox
        .addElement(
            'T',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierFirebox,
                ImmutableList.of(Pair.of(sBlockCasings3, 13), Pair.of(sBlockCasings3, 14)),
                0,
                (t, m) -> t.tier = m,
                t -> t.tier))
        .build();

    public LargeFiretube(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }

    protected LargeFiretube(String name) {
        super(name);
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

    /**
     * 1024/s, doubling every tier.
     */
    @Override
    public int getPollutionPerSecond(ItemStack stack) {
        if (isBurning) {
            return 1 << (9 + tier);
        }
        return 0;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeFiretube(this.mName);
    }

    @Override
    protected int getEut(int eul) {
        // EU/t is (heatBoost / 25) * 32 EU/t * 2^tier
        return getHeatBoost(eul) / 25 * (1 << (5 + tier));
    }
}
