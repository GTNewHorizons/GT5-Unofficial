package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchBooster;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings10;
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
                "BDB",
                "BBB",
                "B~B",
                "BBB",
                "C C"
            }, {
                "BBB",
                "A A",
                "A A",
                "BBB",
                "   "
            }, {
                "BBB",
                "BAB",
                "BAB",
                "BBB",
                "C C"
            }})
        //spotless:on
        .addElement(
            'B',
            buildHatchAdder(MTESuperConductorProcessor.class)
                .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy.or(MultiAmpEnergy))
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(15))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTESuperConductorProcessor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 15))))
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
        .addElement(
            'C',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTESuperConductorProcessor::setSolenoidLevel,
                    MTESuperConductorProcessor::getSolenoidLevel)))
        .addElement(
            'D',
            buildHatchAdder(MTESuperConductorProcessor.class).adder(MTESuperConductorProcessor::addboosterHatch)
                .hatchClass(MTEHatchBooster.class)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(0))
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Brewery")
            .addBulkMachineInfo(4, 1.5F, 1F)
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
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
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0) && casingAmount >= 1;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result != CheckRecipeResultRegistry.NO_RECIPE && boosterHatch != null && mOutputItems != null) {
            for (int i = 0; i < mOutputItems.length; i++) {
                for (int j = 0; j < boosterHatch.getInventoryStackLimit(); j++) {
                    int sconID = getSconID(mOutputItems[i]);
                    int boosterID = boosterHatch.getBoosterIDInSlot(j);
                    if (sconID != -1 && boosterID != -1 && sconID == boosterID) {
                        mOutputItems[i].stackSize = (int) (mOutputItems[i].stackSize * 1.15);
                    }
                }
            }
        }
        return result;
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
