package kekztech.common.tileentities;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gregtech.api.util.tooltip.TooltipHelper.anyCasingText;

import java.util.Map;

import gregtech.common.misc.GTStructureChannels;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import kekztech.common.Blocks;

@IMetaTileEntity.SkipGenerateDescription
public class MTESOFuelCellMK1 extends MTESOFuelCellBase<MTESOFuelCellMK1> {

    private static final int OXYGEN_PER_SEC = 100;
    private static final int EU_PER_TICK = 2048;
    private static final int STEAM_PER_SEC = 20000;
    private static final int STEAM_PER_TICK = STEAM_PER_SEC / 20;

    public MTESOFuelCellMK1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, STEAM_PER_TICK, OXYGEN_PER_SEC, EU_PER_TICK);
    }

    public MTESOFuelCellMK1(String aName) {
        super(aName, STEAM_PER_TICK, OXYGEN_PER_SEC, EU_PER_TICK);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new MTESOFuelCellMK1(super.mName);
    }

    private static final int CASING_TEXTURE_ID = 49;
    private static final IStructureDefinition<MTESOFuelCellMK1> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTESOFuelCellMK1>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccc", "sss", "sss", "sss", "ccc" }, { "c~c", "geg", "geg", "geg", "cdc" },
                    { "ccc", "sss", "sss", "sss", "ccc" } }))
        .addElement(
            'c',
            ofChain(
                buildHatchAdder(MTESOFuelCellMK1.class).atLeast(InputHatch, InputHatch, OutputHatch, Maintenance)
                    .hint(1)
                    .casingIndex(CASING_TEXTURE_ID)
                    .build(),
                onElementPass(te -> te.mCasing++, ofBlock(GregTechAPI.sBlockCasings4, 1))))
        .addElement('d', Dynamo.newAny(CASING_TEXTURE_ID, 2))
        .addElement('e', ofBlockAnyMeta(Blocks.yszUnit))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.mGlassTier = t, te -> te.mGlassTier))
        .addElement('s', ofSheetMetal(Materials.StainlessSteel))
        .build();

    @Override
    public IStructureDefinition<MTESOFuelCellMK1> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final Map<String, Object> vars = Map.of(
            "euPerTickX20",
            formatNumber(EU_PER_TICK * 20),
            "euPerTick",
            formatNumber(EU_PER_TICK),
            "steamPerTick",
            formatNumber(STEAM_PER_SEC),
            "oxygenPerSec",
            formatNumber(OXYGEN_PER_SEC));
        tt.addMachineType(StatCollector.translateToLocal("kekztech.multiblock.SOFuelCell.machine_type"))
            .addMarkdown(new ResourceLocation("gregtech", "so-fuel-cell-mk1"), vars)
            .beginStructureBlock(3, 3, 5, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("12-13", Casings.CleanStainlessSteelMachineCasing.getLocalizedName(), false)
            .addCasing("18", OrePrefixes.sheetmetal.getLocalizedNameForItem(Materials.StainlessSteel), false)
            .addCasing(
                "6",
                StatCollector
                    .translateToLocalFormatted("gt.mbtt.structure.min_tiered_glass", GTValues.VN[VoltageIndex.EV]),
                false)
            .addCasing("3", StatCollector.translateToLocal("kekztech.multiblock.SOFuelCellMK1.electrolyte_unit"), false)
            .addDynamoHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.back_center_casing"), 2)
            .addMaintenanceHatch("1", anyCasingText(), 1)
            .addInputHatch("1+", anyCasingText(), 1)
            .addOutputHatch("1+", anyCasingText(), 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DynamoLimit"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID);
    }

    @Override
    public float getFuelEffiencyMultiplier(GTRecipe aFuel) {
        return 1.0f;
    }

    @Override
    public FluidStack getSteamOutput(long aStackSize) {
        return Materials.Steam.getGas(aStackSize);
    }
}
