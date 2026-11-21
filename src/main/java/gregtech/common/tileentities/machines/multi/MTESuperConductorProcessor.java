package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTORPROCESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.DARK_AQUA;
import static net.minecraft.util.EnumChatFormatting.DARK_GRAY;
import static net.minecraft.util.EnumChatFormatting.DARK_GREEN;
import static net.minecraft.util.EnumChatFormatting.DARK_RED;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.WHITE;

import java.util.ArrayList;
import java.util.Arrays;
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
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.register.LanthItemList;

public class MTESuperConductorProcessor extends MTEExtendedPowerMultiBlockBase<MTESuperConductorProcessor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    public static final double BOOSTER_OUTPUT_MULTIPLIER = 1.15;
    private static final Materials[] SUPERCONDUCTORS = { Materials.SuperconductorMV, Materials.SuperconductorHV,
        Materials.SuperconductorEV, Materials.SuperconductorIV, Materials.SuperconductorLuV,
        Materials.SuperconductorZPM, Materials.SuperconductorUV, Materials.SuperconductorUHV,
        Materials.SuperconductorUEV, Materials.SuperconductorUIV, Materials.SuperconductorUMV };
    private int casingTier = -1;
    private Byte solenoidLevel = null;

    private static final IStructureDefinition<MTESuperConductorProcessor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESuperConductorProcessor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{
                {"       FFF       ",
                "    I FFFFF I    ",
                "      FF~FF      ",
                "    I FFFFF I    ",
                "       FFF       "
            },{
                "    I  FFF  I    ",
                "   II FFAFF II   ",
                "    HGDDDDDGH    ",
                "   II FFAFF II   ",
                "    I  FFF  I    "
            },{
                "   I   FFF   I   ",
                "  IIFFNNANNFFII  ",
                "   HDDCCCCCDDH   ",
                "  IIFFNNANNFFII  ",
                "   IG  FFF  GI   "
            },{
                "  I    FFF    I  ",
                " IIFNNFFAFFNNFII ",
                "  HDCCDDDDDCCDH  ",
                " IIFNNFFAFFNNFII ",
                "  IGG LLLLL GGI  "
            },{
                " I      G      I ",
                "IIFNNFJJGJJFNNFII",
                " HDCCDGGGGGDCCDH ",
                "IIFNNFJJGJJFNNFII",
                " IGGGLLLLLLLGGGI "
            },{
                "        B        ",
                "  FNFJ     JFNF  ",
                " GDCDG  E  GDCDG ",
                "  FNFJ     JFNF  ",
                "    LGLGLGLGL    "
            },{
                "        B        ",
                " FNFJ       JFNF ",
                "GDCDG  KEK  GDCDG",
                " FNFJ       JFNF ",
                "   LLLGGLGGLLL   "
            },{
                " GGG    B    GGG ",
                "JFEFJ       JFEFJ",
                "GDCDG KKEKK GDCDG",
                "JFNFJ       JFNFJ",
                " LLLLGGGLGGGLLLL "
            },{
                " GBBBBBBGBBBBBBG ",
                "JFEFG   G   GFEFJ",
                "GDCDGEEEGEEEGDCDG",
                "JFNFG   G   GFNFJ",
                " LLLLLLLLLLLLLLL "
            },{
                " GGG    B    GGG ",
                "JFEFJ       JFEFJ",
                "GDCDG KKEKK GDCDG",
                "JFNFJ       JFNFJ",
                " LLLLLLLGLLLLLLL "
            },{
                "        B        ",
                " FNFJ       JFNF ",
                "GDCDG  KEK  GDCDG",
                " FNFJ       JFNF ",
                "   LLLLGGGLLLL   "
            },{
                "        B        ",
                "  FNFJ     JFNF  ",
                " GDCDG  E  GDCDG ",
                "  FNFJ     JFNF  ",
                "    LLGLGLGLL    "
            },{
                " I      G      I ",
                "IIFNNFJJGJJFNNFII",
                " HDCCDGGGGGDCCDH ",
                "IIFNNFJJGJJFNNFII",
                " IGGGLLLGLLLGGGI "
            },{
                "  I     G     I  ",
                " IIFNNFFFFFNNFII ",
                "  HDCCDDDDDCCDH  ",
                " IIFNNFFFFFNNFII ",
                "  IGG LLGLL GGI  "
            },{
                "   I   GGG   I   ",
                "  IIFFNNNNNFFII  ",
                "   HDDCCCCCDDH   ",
                "  IIFFNNNNNFFII  ",
                "   IG  GGG  GI   "
            },{
                "    I  GGG  I    ",
                "   II FFFFF II   ",
                "    HGDDDDDGH    ",
                "   II FFFFF II   ",
                "    I  GGG  I    "
            },{
                "       GGG       ",
                "    I  GGG  I    ",
                "      GGOGG      ",
                "    I  GGG  I    ",
                "       GGG       "
            }})       //spotless:on
        // coolant delivery casing
        .addElement('A', ofBlock(LanthItemList.COOLANT_DELIVERY_CASING, 0))
        // component assembly line casing
        .addElement(
            'B',
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
            'C',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTESuperConductorProcessor::setSolenoidLevel,
                    MTESuperConductorProcessor::getSolenoidLevel)))
        // coolant duct
        .addElement('D', Casings.CoolantDuct.asElement())
        // black plutonium item pipe casing
        .addElement('E', Casings.BlackPlutoniumItemPipeCasing.asElement())
        // quantum convection casing
        .addElement(
            'F',
            buildHatchAdder(MTESuperConductorProcessor.class)
                .atLeast(InputBus, InputHatch, OutputBus, Energy.or(ExoticEnergy))
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(8))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTESuperConductorProcessor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 8))))
        // advanced iridium plated machine casing
        .addElement('G', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
        // advanced filter casing
        .addElement('H', Casings.AdvancedFilterCasing.asElement())
        // stabilized naquadah water plant casing
        .addElement('I', Casings.StabilizedNaquadahWaterPlantCasing.asElement())
        // neutronium frame box
        .addElement('J', ofFrame(Materials.Neutronium))
        // naquadah alloy frame box
        .addElement('K', ofFrame(Materials.NaquadahAlloy))
        // wire factory casing
        .addElement('L', Casings.WireFactoryCasing.asElement())
        // quantum glass
        .addElement('N', Casings.QuantumGlass.asElement())
        // booster hatch
        .addElement(
            'O',
            buildHatchAdder(MTESuperConductorProcessor.class).adder(MTESuperConductorProcessor::addboosterHatch)
                .hatchClass(MTEHatchBooster.class)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .hint(2)
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
            .addInfo("Assembles superconductor wires in large batches")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo(
                TooltipHelper.coloredText("Parallels ", GOLD) + "increase with higher tiers of "
                    + TooltipTier.COMPONENT_ASSEMBLY_LINE_CASING.getValue())
            .addInfo(
                TooltipHelper.coloredText("Parallel Count", GOLD)
                    + TooltipHelper.coloredText(" = 1.85 * (1.32 ^ CoAL Casing Tier)", WHITE))
            .addInfo(
                "Machine speed is" + TooltipHelper.speedText(" doubled ")
                    + "for each "
                    + TooltipTier.COMPONENT_ASSEMBLY_LINE_CASING.getValue()
                    + " tier above the recipe's superconductor tier")
            .addInfo("Can only process recipes at or below the " + TooltipTier.SOLENOID.getValue() + " tier")
            .addInfo(
                "Gains a" + TooltipHelper.coloredText(" 10% ", AQUA)
                    + "EU discount (multiplicative) for each "
                    + TooltipTier.SOLENOID.getValue()
                    + " tier above the recipe")
            .addSeparator()
            .addInfo(
                TooltipHelper.coloredText("Superconductor boosters ", DARK_AQUA)
                    + "are available for every tier of superconductor wire")
            .addInfo(
                "Up to 3 unique" + TooltipHelper.coloredText(" boosters ", DARK_AQUA)
                    + "can be inserted into the"
                    + TooltipHelper.coloredText(" Booster Housing", DARK_AQUA))
            .addInfo(
                "The" + TooltipHelper.coloredText(" booster's ", DARK_AQUA)
                    + "corresponding superconductor will receive"
                    + TooltipHelper.speedText(" 2x ")
                    + "speed and"
                    + TooltipHelper.coloredText(" 15% ", DARK_GREEN)
                    + "additional output")
            .addInfo(
                "Each" + TooltipHelper.coloredText(" booster ", DARK_AQUA)
                    + "installed will incur a flat cost of"
                    + TooltipHelper.coloredText(" coolants", LIGHT_PURPLE)
                    + ", regardless of machine status")
            .addInfo(
                "Failure to supply the correct amount of" + TooltipHelper.coloredText(" coolants ", LIGHT_PURPLE)
                    + "will result in the machine"
                    + TooltipHelper.coloredText(" shutting down & voiding ", DARK_RED)
                    + "its current recipe")
            .addInfo(
                "Higher tiers of" + TooltipHelper.coloredText(" boosters ", DARK_AQUA)
                    + "will require the"
                    + TooltipHelper.coloredText(" coolants ", LIGHT_PURPLE)
                    + "of all lower tiers")
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
                TooltipHelper.coloredText("33 L/s", GREEN) + " : "
                    + TooltipHelper.coloredText("UEV-UMV", GOLD)
                    + " : "
                    + TooltipHelper.coloredText("SpaceTime", LIGHT_PURPLE))
            .addSeparator()
            .addInfo(RED + "Do I look dumb...?")

            .beginStructureBlock(17, 5, 17, true)
            .addController("Front Center")
            .addCasingInfoMin("Quantum Convection Casing", 156, false)
            .addCasingInfoExactly(Casings.CoolantDuct.getLocalizedName(), 68, false)
            .addCasingInfoExactly(Casings.BlackPlutoniumItemPipeCasing.getLocalizedName(), 18, false)
            .addCasingInfoExactly(Casings.AdvancedIridiumPlatedMachineCasing.getLocalizedName(), 146, false)
            .addCasingInfoExactly(Casings.AdvancedFilterCasing.getLocalizedName(), 16, false)
            .addCasingInfoExactly(Casings.StabilizedNaquadahWaterPlantCasing.getLocalizedName(), 104, false)
            .addCasingInfoExactly(OrePrefixes.frameGt.getLocalizedNameForItem(Materials.Neutronium), 52, false)
            .addCasingInfoExactly(OrePrefixes.frameGt.getLocalizedNameForItem(Materials.NaquadahAlloy), 12, false)
            .addCasingInfoExactly(Casings.WireFactoryCasing.getLocalizedName(), 86, false)
            .addCasingInfoExactly(Casings.QuantumGlass.getLocalizedName(), 72, false)
            .addCasingInfoExactly(LanthItemList.COOLANT_DELIVERY_CASING.getLocalizedName(), 6, false)
            .addCasingInfoExactly("Solenoid Superconducting Coil", 40, true)
            .addCasingInfoExactly("Component Assembly Line Casing", 18, true)
            .addInputBus("Any Quantum Convection Casing", 1)
            .addOutputBus("Any Quantum Convection Casing", 1)
            .addInputHatch("Any Quantum Convection Casing", 1)
            .addEnergyHatch("Any Quantum Convection Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 8, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 8, 2, 0, elementBudget, env, false, true);
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
        return checkPiece(STRUCTURE_PIECE_MAIN, 8, 2, 0) && casingAmount >= 156;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful() && boosterHatch != null && mOutputItems != null) {
            for (int i = 0; i < mOutputItems.length; i++) {
                int sconID = getSconID(mOutputItems[i]);
                if (sconID == -1) continue;
                for (int j = 0; j < boosterHatch.getInventoryStackLimit(); j++) {
                    if (boosterHatch.getBoosterIDInSlot(j) == sconID) {
                        mOutputItems[i].stackSize = (int) (mOutputItems[i].stackSize * BOOSTER_OUTPUT_MULTIPLIER);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    List<FluidStack> fluids = new ArrayList<>();

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && boosterHatch != null) {
            if (aTick % 20 == 0) {
                fluids.clear();
                int Tier1Multiplier = 0;
                int Tier2Multiplier = 0;
                int Tier3Multiplier = 0;

                for (int k = 0; k < 3; k++) {
                    int boosterID = boosterHatch.getBoosterIDInSlot(k);

                    if (boosterID >= 2) Tier1Multiplier++;

                    if (boosterID >= 8) Tier2Multiplier++;

                    if (boosterID >= 10) Tier3Multiplier++;

                }
                if (Tier1Multiplier > 0) {
                    fluids.add(WerkstoffLoader.LiquidHelium.getFluidOrGas(33333 * Tier1Multiplier));
                }

                if (Tier2Multiplier > 0) {
                    fluids.add(Materials.SuperfluidHelium.getFluid(3333L * Tier2Multiplier));
                }

                if (Tier3Multiplier > 0) {
                    fluids.add(Materials.SpaceTime.getMolten(33L * Tier3Multiplier));
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

    private final int[] lastBoosterIDs = new int[3];

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (boosterHatch == null) return super.onRunningTick(aStack);
        for (int j = 0; j < 3; j++) {
            int boosterID = boosterHatch.getBoosterIDInSlot(j);
            int lastID = lastBoosterIDs[j];

            if (lastID >= 2 && boosterID < 2) {
                Arrays.fill(lastBoosterIDs, -1);
                stopMachine(ShutDownReasonRegistry.NONE);
                return false;
            }
            if (boosterID >= 2 && lastID >= 2 && boosterID < lastID) {
                Arrays.fill(lastBoosterIDs, -1);
                stopMachine(ShutDownReasonRegistry.NONE);
                return false;
            }
            if (boosterID < 2 && lastID < 2) {
                continue;
            }
            lastBoosterIDs[j] = boosterID;
        }
        return super.onRunningTick(aStack);
    }

    private int getSconID(ItemStack scon) {
        if (scon == null) return -1;
        for (int i = 0; i < SUPERCONDUCTORS.length; i++) {
            ItemStack wire = GTOreDictUnificator.get(OrePrefixes.wireGt01, SUPERCONDUCTORS[i], 1);
            if (GTUtility.areStacksEqual(scon, wire)) return i + 2;
        }
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
                int recipeTier = GTUtility.getTier(recipe.mEUt);
                if (solenoidLevel - recipeTier < 0) return CheckRecipeResultRegistry.NO_RECIPE;
                int bonusTier = casingTier + 1 > recipeTier ? casingTier + 1 - recipeTier : 0;
                double speedMultiplier = Math.pow(2.0, bonusTier);
                if (boosterHatch != null) {
                    for (int j = 0; j < boosterHatch.getInventoryStackLimit(); j++) {
                        if (boosterHatch.getBoosterIDInSlot(j) == recipeTier) {
                            speedMultiplier *= 2.0;
                            break;
                        }
                    }
                }
                setSpeedBonus(1.0 / speedMultiplier);
                maxParallel = Math.max(1, (int) Math.round(1.85 * Math.pow(1.32, casingTier + 1)));
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
