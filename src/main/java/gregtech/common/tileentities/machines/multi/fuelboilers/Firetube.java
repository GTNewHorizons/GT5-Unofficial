package gregtech.common.tileentities.machines.multi.fuelboilers;

import static gregtech.api.GregTech_API.*;
import static gregtech.api.GregTech_API.sBlockCasings3;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_Values.AuthorOmni;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import appeng.core.Api;
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

public class Firetube extends FueledBoiler<Firetube> implements ISurvivalConstructable {

    // There's only one piece to this structure... for now >:)
    // TODO: multiple boiler chambers + superheater
    private static final String MAIN_PIECE_NAME = "main";
    private static final int CASING_TEXTURE_INDEX = ((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings2)
        .getTextureIndex(10);

    private static final int X_OFFSET = 2;
    private static final int Y_OFFSET = 6;
    private static final int Z_OFFSET = 0;
    private static final String[][] structure =
        // spotless:off
        new String[][] {
            { "     ", " BBB ", " BBB ", "FWWWF", "FF-FF", "F---F", "F ~ F" },
            { " OOO ", "B---B", "B---B", "W---W", "FbHbF", "-----", " ITI " },
            { " OMO ", "B-P-B", "B-P-B", "W-P-W", "-HPH-", "-----", "ITTTI" },
            { " OOO ", "B---B", "B---B", "W---W", "FbHbF", "-----", " ITI " },
            { "     ", " BBB ", " BBB ", "FWWWF", "FF-FF", "F---F", "F I F" } };
    // spotless:on

    private static final IStructureDefinition<Firetube> STRUCTURE_DEFINITION = StructureDefinition
        .<Firetube>builder()
        .addShape(MAIN_PIECE_NAME, structure)
        // IO
        // Fuel + maint
        .addElement(
            'I',
            GT_StructureUtility.<Firetube>buildHatchAdder()
                .atLeast(
                    InputHatch.withAdder(
                        (thiz, gtTE, baseCasingIndex) -> thiz
                            .addHatchWithRecipeMap(gtTE, baseCasingIndex, thiz.getRecipeMap())),
                    Maintenance)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .buildAndChain(
                    StructureUtility.ofBlocksTiered(
                        FueledBoiler::getTierCasing,
                        ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                        0,
                        (t, m) -> t.tier = m,
                        t -> t.tier)))
        // Water in
        .addElement(
            'W',
            GT_StructureUtility.<Firetube>buildHatchAdder()
                .atLeast(
                    InputHatch.withAdder(
                        (thiz, gtTE, baseCasingIndex) -> thiz
                            .addHatchWithRecipeMap(gtTE, baseCasingIndex, RecipeMaps.waterOnly)))
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .buildAndChain(
                    StructureUtility.ofChain(
                        StructureUtility.ofBlocksTiered(
                            FueledBoiler::getTierCasing,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            0,
                            (t, m) -> t.tier = m,
                            t -> t.tier),
                        StructureUtility.ofBlock(
                            Api.INSTANCE.definitions()
                                .blocks()
                                .quartzGlass()
                                .maybeBlock()
                                .or(Blocks.glass),
                            0))))
        // Pollution out
        .addElement(
            'M',
            GT_StructureUtility.<Firetube>buildHatchAdder()
                .atLeast(Muffler)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .build())
        // Steam out
        .addElement(
            'O',
            GT_StructureUtility.<Firetube>buildHatchAdder()
                .atLeast(OutputHatch)
                .casingIndex(CASING_TEXTURE_INDEX)
                .dot(1)
                .buildAndChain(
                    StructureUtility.ofChain(
                        StructureUtility.ofBlocksTiered(
                            FueledBoiler::getTierCasing,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            0,
                            (t, m) -> t.tier = m,
                            t -> t.tier),
                        StructureUtility.ofBlock(
                            Api.INSTANCE.definitions()
                                .blocks()
                                .quartzGlass()
                                .maybeBlock()
                                .or(Blocks.glass),
                            0))))
        // Building blocks
        // Invar frame
        .addElement('F', ofFrame(Materials.Invar))
        // Bronze plated bricks, or glass
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
                StructureUtility.ofBlock(
                    Api.INSTANCE.definitions()
                        .blocks()
                        .quartzGlass()
                        .maybeBlock()
                        .or(Blocks.glass),
                    0)))
        // Mandatory casings
        .addElement(
            'b',
            StructureUtility.ofBlocksTiered(
                FueledBoiler::getTierCasing,
                ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                0,
                (t, m) -> t.tier = m,
                t -> t.tier))
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

    public Firetube(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }

    protected Firetube(String name) {
        super(name);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Boiler")
            .addInfo("Controller block for the Firetube Boiler")
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
        if (!checkPiece(MAIN_PIECE_NAME, X_OFFSET, Y_OFFSET, Z_OFFSET)) return false;
        if (mOutputHatches.size() != 1) return false;
        if (mInputHatches.size() != 2) return false;
        // TODO: check for glass amount
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(MAIN_PIECE_NAME, stackSize, hintsOnly, X_OFFSET, Y_OFFSET, Z_OFFSET);
    }

    @Override
    public IStructureDefinition<Firetube> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Firetube(this.mName);
    }

    @Override
    protected int getEut(int eul) {
        // EU/t is (heatBoost / 25) * 32 EU/t * 2^tier
        return getHeatBoost(eul) / 25 * (1 << (5 + tier));
    }
}
