package gregtech.common.tileentities.machines.multi;

package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilde
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTUtility.validMTEList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import ic2.api.recipe.ICannerEnrichRecipeManager.Input;

public class MTEExtremeCombustionEngine extends MTEExtendedPowerMultiBlockBase<MTEExtremeCombustionEngine>
    implements ISurvivalConstructable {

    protected int casingAmount;
    protected int turbineCasingAmount;
    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 1;
    private static final int OFFSET_Z = 1;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    protected int fuelConsumption = 0;
    protected int fuelValue = 0;
    protected int fuelRemaining = 0;
    protected boolean boostEu = false;

    public MTEExtremeCombustionEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExtremeCombustionEngine(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Combustion Generator, ECE")
            .addInfo("Supply high rating fuel and 8000L of Lubricant per hour to run")
            .addInfo("Supply 40L/s of Liquid Oxygen to boost output (optional)")
            .addInfo("Default: Produces 10900EU/t at 100% fuel efficiency")
            .addInfo("Boosted: Produces 32700EU/t at 150% fuel efficiency")
            .addInfo("You need to wait for it to reach 300% to output full power")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 4, false)
            .addController("Front center")
            .addCasingInfoRange("Robust Tungstensteel Machine Casing", 16, 22, false)
            .addOtherStructurePart("Titanium Gear Box Machine Casing", "Inner 2 blocks")
            .addOtherStructurePart("Extreme Engine Intake Machine Casing", "8x, ring around controller")
            .addStructureInfo("Extreme Engine Intake Casings must not be obstructed in front (only air blocks)")
            .addDynamoHatch("Back center", 2)
            .addMaintenanceHatch("One of the casings next to a Gear Box", 1)
            .addMufflerHatch("Top middle back, above the rear Gear Box", 1)
            .addInputHatch("HOG, next to a Gear Box", 1)
            .addInputHatch("Lubricant, next to a Gear Box", 1)
            .addInputHatch("Liquid Oxygen, optional, next to a Gear Box", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEExtremeCombustionEngine> getStructureDefinition() {
        return StructureDefinition.<MTEExtremeCombustionEngine>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                new String[][]{{
    "          ",
    "    H    H",
    "H H H    H",
    "H H H    H",
    "FEFEFEEEEF"
},{
    "    H----H",
    "H H CGGGGC",
    "CCC CDDDDC",
    "C~C CBBBBC",
    "FEFEFEEEEF"
},{
    "    H----H",
    "H H CGGGGC",
    "CCCAAAAAAI",
    "CCC CBBBBC",
    "FEFEFEEEEF"
},{
    "    H----H",
    "H H CGGGGC",
    "CCC CDDDDC",
    "CCC CBBBBC",
    "FEFEFEEEEF"
},{
    "          ",
    "    H    H",
    "H H H    H",
    "H H H    H",
    "FEFEFEEEEF"
}})
            .addElement('A', Casings.SteelGearBoxCasing.asElement())
            .addElement('B', Casings.TungstensteelFireboxCasing.asElement())
            .addElement('C', buildHatchAdder(MTEExtremeCombustionEngine.class).atLeast(Muffler, Maintenance)
                    .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                    .hint(1)
                    .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.RobustTungstenSteelMachineCasing.asElement())))
            .addElement('D', buildHatchAdder(MTEExtremeCombustionEngine.class).atLeast(InputHatch, InputHatch, InputHatch)
                    .casingIndex(Casings.TungstensteelTurbineCasing.textureId)
                    .hint(2)
                    .buildAndChain(onElementPass(x -> ++x.turbineCasingAmount ,Casings.TungstensteelTurbineCasing.asElement())))
            .addElement('E', Casings.ChemicallyInertMachineCasing.asElement())
            .addElement('F', Casings.PTFEPipeCasing.asElement())
            .addElement('G', Casings.ExtremeEngineIntakeCasing.asElement())
            .addElement('H', ofFrame(Materials.Polytetrafluoroethylene))
            .addElement(
                'I',
                buildHatchAdder(MTEExtremeCombustionEngine.class).atLeast(Dynamo.or(ExoticDynamo))
                    .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                    .hint(3)
                    .buildAndChain(Casings.RobustTungstenSteelMachineCasing.asElement()))
            .build();
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return RecipeMaps.extremeDieselFuels;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][60], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][60], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][60] };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExtremeCombustionEngine(this.mName);
    }

    @Override
    protected int getNominalOutput() {
        return 10900;
    }

    @Override
    protected Materials getBooster() {
        return Materials.LiquidOxygen;
    }

    @Override
    protected int getEfficiencyIncrease() {
        return 20;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionExtremeCombustionEnginePerSecond;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.extreme_diesel_engine")
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            getIdealStatus() == getRepairStatus()
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                    + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                    + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.engine.output") + ": "
                + EnumChatFormatting.RED
                + formatNumber((long) -mEUt * mEfficiency / 10000)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.engine.consumption") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelConsumption)
                + EnumChatFormatting.RESET
                + " L/t",
            StatCollector.translateToLocal("GT5U.engine.value") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelValue)
                + EnumChatFormatting.RESET
                + " EU/L",
            StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                + EnumChatFormatting.GOLD
                + formatNumber(fuelRemaining)
                + EnumChatFormatting.RESET
                + " L",
            StatCollector.translateToLocal("GT5U.engine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.YELLOW
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

     @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && !mMufflerHatches.isEmpty()
            && casingAmount >= 10;
    }

    
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }
}
