package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.DARK_GRAY;
import static net.minecraft.util.EnumChatFormatting.DARK_GREEN;
import static net.minecraft.util.EnumChatFormatting.DARK_RED;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.WHITE;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchBooster;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.misc.GTStructureChannels;

public class MTESuperConductorProcessor extends MTEExtendedPowerMultiBlockBase<MTESuperConductorProcessor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private int casingTier = -1;
    private Byte solenoidLevel = null;
    private int bonusParallel = 1;

    private static final IStructureDefinition<MTESuperConductorProcessor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESuperConductorProcessor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "                       ",
                "          EEE          ",
                "         EDDDE         ",
                "         ED~DE         ",
                "         EDDDE         ",
                "          EEE          "
            },{
                "                       ",
                "          EEE          ",
                "         EEEEE         ",
                "    EE EECCCCCEE EE    ",
                "    F    EEEEE    F    ",
                "    F     EEE     F    "
            },{
                "                       ",
                "                       ",
                "       DDGGGGGDD       ",
                "   EEEECCBBBBBCCEEEE   ",
                "       DDAGGGADD       ",
                "                       "
            },{
                "                       ",
                "                       ",
                "      DGGDDDDDGGD      ",
                "  EEEECBBCCCCCBBCEEEE  ",
                "      DAADDDDDAAD      ",
                "                       "
            },{
                "                       ",
                "                       ",
                "     DGDD     DDGD     ",
                " EEEECBCCEEHEECCBCEEEE ",
                " F   DADD     DDAD   F ",
                " F                   F "
            },{
                "                       ",
                "                       ",
                "    DGD         DGD    ",
                " EEECBCEE     EECBCEEE ",
                "    DAD         DAD    ",
                "                       "
            },{
                "                       ",
                "                       ",
                "   DGD           DGD   ",
                "  ECBCE         ECBCE  ",
                "   DAD           DAD   ",
                "                       "
            },{
                "                       ",
                "                       ",
                "  DGD             DGD  ",
                " ECBCE D       D ECBCE ",
                "  DAD             DAD  ",
                "                       "
            },{
                "          D D          ",
                "                       ",
                "  DGD   D     D   DGD  ",
                " ECBCE           ECBCE ",
                "  DAD             DAD  ",
                "                       "
            },{
                "          D D          ",
                "         D   D         ",
                " DGD               DGD ",
                "ECBCE             ECBCE",
                " DAD               DAD ",
                "                       "
            },{
                "        DDD DDD        ",
                "                       ",
                " DGD               DGD ",
                "ECBCE             ECBCE",
                " DGD               DGD ",
                "                       "
            },{
                "                       ",
                " EEE               EEE ",
                "EEGEE             EDGDE",
                "ECBCE             ECBCE",
                "EEGEE             EDGDE",
                " EEE               EEE "
            },{
                "                       ",
                "                       ",
                " DGD               DGD ",
                "ECBCE             ECBCE",
                " DGD               DGD ",
                "                       "
            },{
                "           D           ",
                "                       ",
                " DGD               DGD ",
                "ECBCE             ECBCE",
                " DAD               DAD ",
                "                       "
            },{
                "          D D          ",
                "           D           ",
                "  DGD             DGD  ",
                " ECBCE           ECBCE ",
                "  DAD             DAD  ",
                "                       "
            },{
                "         D   D         ",
                "                       ",
                "  DGD      D      DGD  ",
                " ECBCE           ECBCE ",
                "  DAD             DAD  ",
                "                       "
            },{
                "                       ",
                "                       ",
                "   DGD           DGD   ",
                "  ECBCE    D    ECBCE  ",
                "   DAD           DAD   ",
                "                       "
            },{
                "                       ",
                "                       ",
                "    DGD         DGD    ",
                "   ECBCEE  D  EECBCE   ",
                "    DAD         DAD    ",
                "                       "
            },{
                "                       ",
                "                       ",
                "     DGDD  E  DDGD     ",
                "    ECBCCEEEEECCBCE    ",
                "     DADD  E  DDAD     ",
                "                       "
            },{
                "                       ",
                "           E           ",
                "      DGGDDDDDGGD      ",
                "     ECBBCCCCCBBCE     ",
                "      DAADDDDDAAD      ",
                "           E           "
            },{
                "                       ",
                "           E           ",
                "       DDGGGGGDD       ",
                "      ECCBBBBBCCE      ",
                "       DDAGGGADD       ",
                "           E           "
            },{
                "                       ",
                "           E           ",
                "         DDDDD         ",
                "       EECCCCCEE       ",
                "         DDDDD         ",
                "           E           "
            },{
                "                       ",
                "                       ",
                "           E           ",
                "         EEEEE         ",
                "           E           ",
                "                       "
            },{
                "                       ",
                "                       ",
                "                       ",
                "         EEEEE         ",
                "                       ",
                "                       "
            },{
                "                       ",
                "                       ",
                "                       ",
                "          EEE          ",
                "          F F          ",
                "          F F          "
            }})       //spotless:on
        // coal casings
        .addElement(
            'A',
            lazy(
                () -> ofBlocksTiered(
                    (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta : null,
                    IntStream.range(0, 14)
                        .mapToObj(i -> Pair.of(Loaders.componentAssemblylineCasing, i))
                        .collect(Collectors.toList()),
                    -1,
                    MTESuperConductorProcessor::setCasingTier,
                    MTESuperConductorProcessor::getCasingTier)))

        // solenoids
        .addElement(
            'B',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTESuperConductorProcessor::setSolenoidLevel,
                    MTESuperConductorProcessor::getSolenoidLevel)))

        // coolant duct
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 9))

        // quantum convection casing
        .addElement(
            'D',
            buildHatchAdder(MTESuperConductorProcessor.class)
                .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(8))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTESuperConductorProcessor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 8))))

        // naq casing
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 10))

        // radiation proof steel frame box
        .addElement('F', ofBlock(Loaders.radiationProtectionSteelFrame, 0))

        // quantum glass
        .addElement('G', lazy(() -> Casings.QuantumGlass.asElement()))
        // booster housing
        .addElement(
            'H',
            buildHatchAdder(MTESuperConductorProcessor.class).adder(MTESuperConductorProcessor::addboosterHatch)
                .hatchClass(MTEHatchBooster.class)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .dot(2)
                .build())
        .build();

    private MTEHatchBooster boosterHatch = null;

    public MTESuperConductorProcessor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESuperConductorProcessor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTESuperConductorProcessor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperConductorProcessor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 8)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Bulk Superconductor Assembler, SCP")
            .addInfo(DARK_GRAY + "" + EnumChatFormatting.ITALIC + "Secure. Contain. Produce.")
            .addInfo(
                "The" + TooltipHelper.coloredText(" Component Assembly Line Casing ", YELLOW)
                    + "tier increases the parallels of the machine")
            .addInfo("The parallel multiplier is" + TooltipHelper.italicText(" 0.95 * (1.32 ^ CoAL Casing Tier)"))
            .addInfo("Recipes are completed in batches of 64, at 75% of the original recipe's time")
            .addInfo(
                "Every" + TooltipHelper.coloredText(" Superconducting Solenoid Coil ", WHITE)
                    + "tier above the recipe gives a "
                    + TooltipHelper.coloredText("10%", AQUA)
                    + " EU discount (multiplicative)")
            .addInfo(TooltipHelper.coloredText("Supports TecTech Multi-Amp and Laser Hatches!", GREEN))
            .addSeparator()
            .addInfo("Superconductor Boosters are available for every tier of superconductor wire")
            .addInfo("Up to 3 unique boosters can be inserted into the Booster Housing")
            .addInfo(
                "Each one will boost the output of its respective tier's superconductor, receiving"
                    + TooltipHelper.parallelText(" 2x ")
                    + "parallels and"
                    + TooltipHelper.coloredText(" 15% ", DARK_GREEN)
                    + "additional output")
            .addInfo("Each booster installed will incur a flat cost of coolants, regardless of machine status")
            .addInfo(
                "Failure to supply the correct amount of fluids will result in the machine instantly"
                    + TooltipHelper.coloredText(" shutting down & voiding ", DARK_RED)
                    + "its current recipe")
            .addInfo("Higher tiers of boosters will require the coolants of all lower tiers")
            .addInfo(
                TooltipHelper.coloredText("33,333 L/s", GREEN) + " : "
                    + TooltipHelper.coloredText("MV-ZPM", GOLD)
                    + " : "
                    + TooltipHelper.coloredText("Liquid Helium", LIGHT_PURPLE))
            .addInfo(
                TooltipHelper.coloredText("3,333 L/s", GREEN) + " : "
                    + TooltipHelper.coloredText("UV-UHV", GOLD)
                    + " : "
                    + TooltipHelper.coloredText("Superfluid Helium", LIGHT_PURPLE))
            .addInfo(
                TooltipHelper.coloredText("333 L/s", GREEN) + " : "
                    + TooltipHelper.coloredText("UEV-UMV", GOLD)
                    + " : "
                    + TooltipHelper.coloredText("SpaceTime", LIGHT_PURPLE))
            .addSeparator()
            .addInfo(RED + "Do I look dumb..?")

            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Quantum Convection Casing", 200, false)
            .addCasingInfoExactly("Radiant Naquadah Alloy Casing", 186, false)
            .addCasingInfoExactly("Coolant Duct", 96, false)
            .addCasingInfoExactly("Quantum Glass", 60, false)
            .addCasingInfoExactly("Solenoid Superconducting Coil", 48, true)
            .addCasingInfoExactly("Component Assembly Line Casing", 36, true)
            .addCasingInfoExactly("Radiation Proof Steel Frame Box", 12, false)
            .addInputBus("Any Quantum Convection Casing", 1)
            .addOutputBus("Any Quantum Convection Casing", 1)
            .addInputHatch("Any Quantum Convection Casing", 1)
            .addEnergyHatch("Any Quantum Convection Casing", 1)
            .addMaintenanceHatch("Any Quantum Convection Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 11, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 11, 3, 0, elementBudget, env, false, true);
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        solenoidLevel = null;
        boosterHatch = null;
        this.casingTier = -1;
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 11, 3, 0) && casingAmount >= 200;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result != CheckRecipeResultRegistry.NO_RECIPE && boosterHatch != null && mOutputItems != null) {
            for (int i = 0; i < mOutputItems.length; i++) {
                for (int j = 0; j < boosterHatch.getInventoryStackLimit(); j++) {
                    int sconID = getSconID(mOutputItems[i]);
                    int boosterID = boosterHatch.getBoosterIDInSlot(j);
                    if (boosterID != -1 && sconID == boosterID) {
                        mOutputItems[i].stackSize = (int) (mOutputItems[i].stackSize * 1.15);
                    }
                }
            }
        }
        return result;
    }

    /*
     * @Override
     * public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
     * if (boosterHatch != null) {
     * for (int k = 0; k < 3; k++) {
     * int boosterID = boosterHatch.getBoosterIDInSlot(k);
     * if (boosterID >= 2 && boosterID <= 8) {
     * for (MTEHatchInput hatch : mInputHatches) {
     * FluidStack fluid = WerkstoffLoader.LiquidHelium.getFluidOrGas(1666);
     * if (drain(hatch, fluid, false)) {
     * drain(hatch, fluid, true);
     * break;
     * }
     * // stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
     * }
     * }
     * }
     * }
     * super.onPostTick(aBaseMetaTileEntity, aTick);
     * }
     */

    List<FluidStack> fluids = new ArrayList<>();

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (boosterHatch == null) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            return;
        }
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == 0) {
                fluids.clear();
                int Tier1Multiplier = 0;
                int Tier2Multiplier = 0;
                int Tier3Multiplier = 0;

                for (int k = 0; k < 3; k++) {
                    int boosterID = boosterHatch.getBoosterIDInSlot(k);
                    if (boosterID < 2) break;

                    if (boosterID >= 2) {
                        Tier1Multiplier += 1;
                    }
                    if (boosterID >= 8) {
                        Tier2Multiplier += 1;
                    }
                    if (boosterID >= 10) {
                        Tier3Multiplier += 1;
                    }
                }
                if (Tier1Multiplier > 0) {
                    fluids.add(WerkstoffLoader.LiquidHelium.getFluidOrGas(33333 * Tier1Multiplier));
                }

                if (Tier2Multiplier > 0) {
                    fluids.add(Materials.LiquidNitrogen.getGas(3333 * Tier2Multiplier));
                }

                if (Tier3Multiplier > 0) {
                    fluids.add(Materials.SpaceTime.getMolten(333 * Tier3Multiplier));
                }

                for (FluidStack fluid : fluids) {
                    boolean foundFluid = false;
                    for (MTEHatchInput hatch : mInputHatches) {
                        if (drain(hatch, fluid, true)) {
                            foundFluid = true;
                            break;
                        }
                    }
                    if (!foundFluid) this.stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    private int getSconID(ItemStack scon) {
        if (scon != null) {
            ItemStack sconMV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1);
            if (scon.isItemEqual(sconMV)) return 2;
            ItemStack sconHV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1);
            if (scon.isItemEqual(sconHV)) return 3;
            ItemStack sconEV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1);
            if (scon.isItemEqual(sconEV)) return 4;
            ItemStack sconIV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconIV)) return 5;
            ItemStack sconLuV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconLuV)) return 6;
            ItemStack sconZPM = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1);
            if (ItemStack.areItemStacksEqual(scon, sconZPM)) return 7;
            ItemStack sconUV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconUV)) return 8;
            ItemStack sconUHV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconUHV)) return 9;
            ItemStack sconUEV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconUEV)) return 10;
            ItemStack sconUIV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconUIV)) return 11;
            ItemStack sconUMV = GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1);
            if (ItemStack.areItemStacksEqual(scon, sconUMV)) return 12;
        } ;
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            public ProcessingLogic setMaxParallel(int maxParallel) {
                return super.setMaxParallel(maxParallel);
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (solenoidLevel - GTUtility.getTier(recipe.mEUt) < 0) return CheckRecipeResultRegistry.NO_RECIPE;
                bonusParallel = 1;
                if (boosterHatch != null) {
                    for (int j = 0; j < boosterHatch.getInventoryStackLimit(); j++) {
                        int boosterID = boosterHatch.getBoosterIDInSlot(j);
                        if (GTUtility.getTier(recipe.mEUt) != -1 && boosterID != -1
                            && GTUtility.getTier(recipe.mEUt) == boosterID) {
                            bonusParallel = 2;
                        }
                    }
                }
                maxParallel = Math.max(1, bonusParallel * (int) Math.round((0.95 * Math.pow(1.32, casingTier + 1))));
                return super.validateRecipe(recipe);
            }
        }.noRecipeCaching();
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    public static boolean isValidBooster(ItemStack aBooster) {
        return aBooster != null && aBooster.getItem() instanceof MetaGeneratedItem01
            && aBooster.getItemDamage() >= 32150
            && aBooster.getItemDamage() <= 32160;
    }

    private boolean addboosterHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchBooster booster) {
                booster.updateTexture(aBaseCasingIndex);
                if (boosterHatch == null) {
                    boosterHatch = booster;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.scpRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    private void setCasingTier(int tier) {
        this.casingTier = tier;
    }

    private int getCasingTier() {
        return this.casingTier;
    }

    private Byte getSolenoidLevel() {
        return solenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        solenoidLevel = level;
    }

    private double calculateEuDiscount(int recipeVoltage) {
        final int recipeTier = GTUtility.getTier(recipeVoltage);
        final int exponent = Math.max(0, solenoidLevel - recipeTier);
        return Math.pow(0.9, exponent);
    }
}
