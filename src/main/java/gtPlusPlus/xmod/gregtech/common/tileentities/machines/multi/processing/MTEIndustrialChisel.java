package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStreamUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import team.chisel.carving.Carving;

public class MTEIndustrialChisel extends GTPPMultiBlockBase<MTEIndustrialChisel> implements ISurvivalConstructable {

    private int mCasing;

    private ItemStack target;
    private static IStructureDefinition<MTEIndustrialChisel> STRUCTURE_DEFINITION = null;
    private ItemStack mInputCache;
    private ItemStack mOutputCache;
    private GTRecipe mCachedRecipe;

    public MTEIndustrialChisel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialChisel(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialChisel(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Chisel";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addBulkMachineInfo(16, 3f, 0.75f)
            .addInfo("Factory Grade Auto Chisel")
            .addInfo("Target block goes in Controller slot for common Input Buses")
            .addInfo("You can also set a target block in each Chisel Bus and use them as an Input Bus")
            .addInfo("If no target is provided for common buses, the result of the first chisel is used")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoMin("Sturdy Printer Casing", 6, false)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addMufflerHatch("Any casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialChisel> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialChisel>builder()
                .addShape(
                    mName,
                    transpose(
                        // spotless:off
                                    new String[][] {
                                            { "CCC", "CCC", "CCC" },
                                            { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" },
                                    }))
                                    // spotless:on
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialChisel.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(90)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings5Misc, 5))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialChiselActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialChiselActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialChisel;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialChiselGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return 90;
    }

    private boolean hasValidCache(ItemStack aStack, ItemStack aSpecialSlot, boolean aClearOnFailure) {
        if (mInputCache != null && mOutputCache != null && mCachedRecipe != null) {
            if (GTUtility.areStacksEqual(aStack, mInputCache) && GTUtility.areStacksEqual(aSpecialSlot, mOutputCache)) {
                return true;
            }
        }
        // clear cache if it was invalid
        if (aClearOnFailure) {
            mInputCache = null;
            mOutputCache = null;
            mCachedRecipe = null;
        }
        return false;
    }

    private void cacheItem(ItemStack aInputItem, ItemStack aOutputItem, GTRecipe aRecipe) {
        mInputCache = aInputItem.copy();
        mOutputCache = aOutputItem.copy();
        mCachedRecipe = aRecipe;
    }

    // lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
    private static boolean canBeMadeFrom(ItemStack from, ItemStack to) {
        List<ItemStack> results = getItemsForChiseling(from);
        for (ItemStack s : results) {
            if (s.getItem() == to.getItem() && s.getItemDamage() == to.getItemDamage()) {
                return true;
            }
        }
        return false;
    }

    // lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
    private static boolean hasChiselResults(ItemStack from) {
        List<ItemStack> results = getItemsForChiseling(from);
        return !results.isEmpty();
    }

    private static List<ItemStack> getItemsForChiseling(ItemStack aStack) {
        return Carving.chisel.getItemsForChiseling(aStack);
    }

    private static ItemStack getChiselOutput(ItemStack aInput, ItemStack aTarget) {
        ItemStack tOutput;
        if (aTarget != null && canBeMadeFrom(aInput, aTarget)) {
            tOutput = aTarget;
        } else if (aTarget != null && !canBeMadeFrom(aInput, aTarget)) {
            tOutput = null;
        } else {
            tOutput = getItemsForChiseling(aInput).get(0);
        }
        return tOutput;
    }

    private GTRecipe generateChiselRecipe(ItemStack aInput) {
        boolean tIsCached = hasValidCache(aInput, this.target, true);
        if (tIsCached || aInput != null && hasChiselResults(aInput)) {
            ItemStack tOutput = tIsCached ? mOutputCache.copy() : getChiselOutput(aInput, this.target);
            if (tOutput != null) {
                if (mCachedRecipe != null && GTUtility.areStacksEqual(aInput, mInputCache)
                    && GTUtility.areStacksEqual(tOutput, mOutputCache)) {
                    return mCachedRecipe;
                }
                // We can chisel this
                GTRecipe aRecipe = new GTRecipe(
                    new ItemStack[] { GTUtility.copyAmount(1, aInput) },
                    new ItemStack[] { GTUtility.copyAmount(1, tOutput) },
                    null,
                    new int[] { 10000 },
                    GTValues.emptyFluidStackArray,
                    GTValues.emptyFluidStackArray,
                    20,
                    16,
                    0);

                // Cache it
                cacheItem(aInput, tOutput, aRecipe);
                return aRecipe;
            }
        }
        return null;
    }

    private GTRecipe getRecipe() {
        for (MTEHatchInputBus bus : this.mInputBusses) {
            if (bus instanceof MTEHatchChiselBus) { // Chisel buses
                if (bus.mInventory[bus.getSizeInventory() - 1] == null) continue;
                this.target = bus.mInventory[bus.getSizeInventory() - 1];

                for (int i = bus.getSizeInventory() - 2; i >= 0; i--) {
                    ItemStack itemsInSlot = bus.mInventory[i];
                    if (itemsInSlot != null) {
                        GTRecipe tRecipe = generateChiselRecipe(itemsInSlot);
                        if (tRecipe != null) {
                            return tRecipe;
                        }
                    }
                }
            } else {
                target = this.getControllerSlot(); // Common buses
                for (int i = bus.getSizeInventory() - 1; i >= 0; i--) {
                    ItemStack itemsInSlot = bus.mInventory[i];
                    if (itemsInSlot != null) {
                        GTRecipe tRecipe = generateChiselRecipe(itemsInSlot);
                        if (tRecipe != null) {
                            return tRecipe;
                        }
                    }
                }
            }

        }
        return null;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                return GTStreamUtil.ofNullable(getRecipe());
            }
        }.noRecipeCaching()
            .setSpeedBonus(1F / 3F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void sendStartMultiBlockSoundLoop() {
        sendLoopStart(PROCESS_START_SOUND_INDEX);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (16 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private static ResourceLocation sChiselSound = null;

    private static ResourceLocation getChiselSound() {
        if (sChiselSound == null) {
            sChiselSound = new ResourceLocation(Carving.chisel.getVariationSound(Blocks.stone, 0));
        }
        return sChiselSound;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        switch (aIndex) {
            case PROCESS_START_SOUND_INDEX -> GTUtility
                .doSoundAtClient(getChiselSound(), getTimeBetweenProcessSounds(), 1.0F, 1.0F, aX, aY, aZ);
            case INTERRUPT_SOUND_INDEX -> GTUtility
                .doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialChisel;
    }
}
