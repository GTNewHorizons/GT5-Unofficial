package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTEFuelRefineFactory extends MTETooltipMultiBlockBaseEM implements IConstructable, ISurvivalConstructable {

    private IStructureDefinition<MTEFuelRefineFactory> multiDefinition = null;
    private int Tier = -1;
    private int[] cnt = new int[] { 0, 0, 0, 0 };
    private static final Block[] coils = new Block[] { Loaders.FRF_Coil_1, Loaders.FRF_Coil_2, Loaders.FRF_Coil_3,
        Loaders.FRF_Coil_4 };

    public MTEFuelRefineFactory(String name) {
        super(name);
        useLongPower = true;
    }

    public MTEFuelRefineFactory(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        useLongPower = true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 7, 12, 1, itemStack, hintsOnly);
    }

    @Override
    public IStructureDefinition<MTEFuelRefineFactory> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEFuelRefineFactory>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "               ", "      CCC      ", "               " },
                            { "      XGX      ", "    CCFFFCC    ", "      XGX      " },
                            { "    CC   CC    ", "   CFFCCCFFC   ", "    CC   CC    " },
                            { "   C       C   ", "  CFCC   CCFC  ", "   C       C   " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { " X           X ", "CFC         CFC", " X           X " },
                            { " G           G ", "CFC         CFC", " G           G " },
                            { " X           X ", "CFC         CFC", " X           X " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { "   C       C   ", "  CFCC   CCFC  ", "   C       C   " },
                            { "    CC   CC    ", "   CFFC~CFFC   ", "    CC   CC    " },
                            { "      XGX      ", "    CCFFFCC    ", "      XGX      " },
                            { "               ", "      CCC      ", "               " } }))
                .addElement(
                    'X',
                    buildHatchAdder(MTEFuelRefineFactory.class)
                        .atLeast(
                            gregtech.api.enums.HatchElement.Maintenance,
                            gregtech.api.enums.HatchElement.InputHatch,
                            gregtech.api.enums.HatchElement.InputBus,
                            gregtech.api.enums.HatchElement.OutputHatch,
                            tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti
                                .or(gregtech.api.enums.HatchElement.Energy))
                        .casingIndex(179)
                        .dot(1)
                        .buildAndChain(ofBlock(Loaders.FRF_Casings, 0)))
                .addElement('C', ofBlock(Loaders.FRF_Casings, 0))
                .addElement('G', ofBlock(Loaders.fieldRestrictingGlass, 0))
                .addElement(
                    'F',
                    ofChain(
                        onElementPass(x -> ++x.cnt[0], ofFieldCoil(0)),
                        onElementPass(x -> ++x.cnt[1], ofFieldCoil(1)),
                        onElementPass(x -> ++x.cnt[2], ofFieldCoil(2)),
                        onElementPass(x -> ++x.cnt[3], ofFieldCoil(3))))
                .build();
        }
        return multiDefinition;
    }

    public static <T> IStructureElement<T> ofFieldCoil(int aIndex) {
        return new IStructureElement<T>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                return block.equals(coils[aIndex]);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, coils[getIndex(trigger)], 0);
                return true;
            }

            private int getIndex(ItemStack trigger) {
                int s = trigger.stackSize;
                if (s > 4 || s <= 0) s = 4;
                return s - 1;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, coils[getIndex(trigger)], 0, 3);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(coils[getIndex(trigger)], 0);
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (check(t, world, x, y, z)) return PlaceResult.SKIP;
                return StructureUtility.survivalPlaceBlock(
                    coils[getIndex(trigger)],
                    0,
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Naquadah Fuel Refinery")
            .addInfo("Controller block for the Naquadah Fuel Refinery")
            .addInfo("But at what cost?")
            .addInfo("Produces naquadah fuels.")
            .addInfo("Needs field restriction coils to control the fatal radiation.")
            .addInfo("Use higher tier coils to unlock more fuel types and reduce the processing times.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(3, 15, 15, false)
            .addInputHatch("The casings adjacent to field restriction glass.")
            .addInputBus("The casings adjacent to field restriction glass.", 1)
            .addOutputHatch("The casings adjacent to field restriction glass.", 1)
            .addEnergyHatch("The casings adjacent to field restriction glass.", 1)
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.Tier = aNBT.getInteger("mTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTier", this.Tier);
        super.saveNBTData(aNBT);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("FuelRefineFactory.hint", 8);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        cnt[0] = 0;
        cnt[1] = 0;
        cnt[2] = 0;
        cnt[3] = 0;
        return structureCheck_EM(mName, 7, 12, 1) && getTier() != -1;
    }

    public int getTier() {
        for (int i = 0; i < 4; i++) {
            if (cnt[i] == 32) {
                Tier = i + 1;
                return i;
            }
        }
        Tier = -1;
        return -1;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > Tier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                int overclockAmount = Tier - recipe.mSpecialValue;
                return super.createOverclockCalculator(recipe).limitOverclockCount(overclockAmount);
            }
        }.setOverclock(2.0, 2.0); // Set Overclock to be 2/2
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
    }

    public final boolean addToFRFList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof MTEHatch) {
                    ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                }
                if (aMetaTileEntity instanceof MTEHatchInput) {
                    return this.mInputHatches.add((MTEHatchInput) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof MTEHatchOutput) {
                    return this.mOutputHatches.add((MTEHatchOutput) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof MTEHatchInputBus) {
                    return this.mInputBusses.add((MTEHatchInputBus) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof MTEHatchEnergy) {
                    return this.mEnergyHatches.add((MTEHatchEnergy) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof MTEHatchEnergyMulti) {
                    return this.eEnergyMulti.add((MTEHatchEnergyMulti) aMetaTileEntity);
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFuelRefineFactory(this.mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] infoData = new String[super.getInfoData().length + 1];
        System.arraycopy(super.getInfoData(), 0, infoData, 0, super.getInfoData().length);
        infoData[super.getInfoData().length] = StatCollector.translateToLocal("scanner.info.FRF") + " " + this.Tier;
        return infoData;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179),
                new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE), TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179),
                new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE), TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 7, 12, 1, elementBudget, env, false, true);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
