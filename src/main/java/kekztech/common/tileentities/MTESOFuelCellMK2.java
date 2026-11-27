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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
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
import gregtech.common.misc.GTStructureChannels;
import kekztech.common.Blocks;

@IMetaTileEntity.SkipGenerateDescription
public class MTESOFuelCellMK2 extends MTESOFuelCellBase<MTESOFuelCellMK2> {

    private static final int OXYGEN_PER_SEC = 2000;
    private static final int EU_PER_TICK = 24576; // 100% Efficiency, 3A IV
    private static final int STEAM_PER_SEC = 96000; // SH Steam (10,800EU/t @ 150% Efficiency)
    private static final int STEAM_PER_TICK = STEAM_PER_SEC / 20;
    private static final int GAS_FUEL_THRESHOLD = 1000;

    public MTESOFuelCellMK2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, STEAM_PER_TICK, OXYGEN_PER_SEC, EU_PER_TICK);
    }

    public MTESOFuelCellMK2(String aName) {
        super(aName, STEAM_PER_TICK, OXYGEN_PER_SEC, EU_PER_TICK);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new MTESOFuelCellMK2(super.mName);
    }

    private static final int CASING_TEXTURE_ID = 48;
    private static final IStructureDefinition<MTESOFuelCellMK2> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTESOFuelCellMK2>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccc", "sss", "sss", "sss", "ccc" }, { "c~c", "geg", "geg", "geg", "cdc" },
                    { "ccc", "sss", "sss", "sss", "ccc" } }))
        .addElement(
            'c',
            ofChain(
                buildHatchAdder(MTESOFuelCellMK2.class).atLeast(InputHatch, InputHatch, OutputHatch, Maintenance)
                    .hint(1)
                    .casingIndex(CASING_TEXTURE_ID)
                    .build(),
                onElementPass(te -> te.mCasing++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
        .addElement('d', Dynamo.newAny(CASING_TEXTURE_ID, 2))
        .addElement('e', ofBlockAnyMeta(Blocks.gdcUnit))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.mGlassTier = t, te -> te.mGlassTier))
        .addElement('s', ofSheetMetal(Materials.TungstenSteel))
        .build();

    @Override
    public IStructureDefinition<MTESOFuelCellMK2> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("euPerTickX20", formatNumber(EU_PER_TICK * 20));
        vars.put("euPerTick", formatNumber(EU_PER_TICK));
        vars.put("steamPerTick", formatNumber(STEAM_PER_TICK));
        vars.put("oxygenPerSec", formatNumber(OXYGEN_PER_SEC));
        vars.put("gasFuelThreshold", formatNumber(GAS_FUEL_THRESHOLD));
        tt.addMachineType(StatCollector.translateToLocal("kekztech.multiblock.SOFuelCell.machine_type"))
            .addMarkdown(new ResourceLocation("gregtech", "so-fuel-cell-mk2"), vars)
            .beginStructureBlock(3, 3, 5, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("12-13", Casings.RobustTungstenSteelMachineCasing.getLocalizedName(), false)
            .addCasing("18", OrePrefixes.sheetmetal.getLocalizedNameForItem(Materials.TungstenSteel), false)
            .addCasing(
                "6",
                StatCollector
                    .translateToLocalFormatted("gt.mbtt.structure.min_tiered_glass", GTValues.VN[VoltageIndex.EV]),
                false)
            .addCasing("3", StatCollector.translateToLocal("kekztech.multiblock.SOFuelCellMK2.electrolyte_unit"), false)
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
    public float getFuelEffiencyMultiplier(GTRecipe aFuel) {
        return Math.max(1, (float) aFuel.mSpecialValue / GAS_FUEL_THRESHOLD);
    }

    @Override
    public FluidStack getSteamOutput(long aStackSize) {
        return FluidRegistry.getFluidStack("ic2superheatedsteam", (int) aStackSize);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID);
    }
}
