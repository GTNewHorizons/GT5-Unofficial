package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofSpecificTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Map;

import gregtech.api.interfaces.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.IndustrialCrucibleRecipes;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import thaumcraft.common.config.ConfigBlocks;

public class MTEIndustrialCrucible extends TTMultiblockBase implements ISurvivalConstructable {

    public ArrayList<MTEEssentiaInputHatch> mEssentiaHatches = new ArrayList<>();

    private static final String STRUCTURE_PIECE_BOTTOM = "bottom";
    private static final String STRUCTURE_PIECE_MIDDLE = "middle";
    private static final String STRUCTURE_PIECE_TOP = "top";
    private static final int MIN_MIDDLE_LENGTH = 1;
    private static final int MAX_MIDDLE_LENGTH = 5;
    private static final int CASING_INDEX = 1536;

    private IStructureDefinition<MTEIndustrialCrucible> STRUCTURE_DEFINITION = null;

    private int mIgnisCentiVis = 0;
    private int mOrdoCentiVis = 0;
    private int mAquaCentiVis = 0;

    protected int mCasing = 0;
    protected int pTier = -1;
    protected int mCurrentLength = 0;
    protected int mParallel = 0;

    public MTEIndustrialCrucible(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialCrucible(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCrucible(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return IndustrialCrucibleRecipes.sCrucibleRecipes;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Industrial Crucible")
            .addInfo("Automates Thaumcraft Crucible recipes.")
            .addInfo("Requires Tome of Knowledge Sharing in the Controller slot.")
            .addInfo("Essentia Hatches can be placed instead of Magic Casings.")
            .addInfo("Grows vertically. Height adds parallel processing.")
            .addInfo("Max Parallels = (Height Layers) * 2^Tier")
            .addInfo("Base Crafting Time: 16 seconds.")
            .addInfo("Pollution: 2200 per cycle. Requires 1 Muffler Hatch on Top.")
            .beginVariableStructureBlock(5, 5, 5, 5, 9, 5, false)
            .addController("Front center, 2nd layer")
            .addOtherStructurePart("Magic Casing", "28 (minimum)")
            .addOtherStructurePart("Essentia Filter Casing", "Inner rings on the 1st and top layers")
            .addOtherStructurePart("Essentia Cell", "Center pillar (T0-T3)")
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addOtherStructurePart("Essentia Input Hatch", "Any Casing")
            .addMufflerHatch("Top Center", 1)
            .toolTipFinisher("Роберт-_P0ZA_");
        return tt;
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialCrucible>builder()
                .addShape(
                    STRUCTURE_PIECE_BOTTOM,
                    transpose(
                        new String[][] { { " M~M ", "MEEEM", "MEDEM", "MEEEM", " MMM " },
                            { " MMM ", "MMMMM", "MMMMM", "MMMMM", " MMM " } }))
                .addShape(
                    STRUCTURE_PIECE_MIDDLE,
                    transpose(new String[][] { { " MWM ", "M---M", "W-D-W", "M---M", " MWM " } }))
                .addShape(
                    STRUCTURE_PIECE_TOP,
                    transpose(
                        new String[][] { { "  M  ", " MMM ", "MMHMM", " MMM ", "  M  " },
                            { " MMM ", "MEEEM", "MEDEM", "MEEEM", " MMM " } }))
                .addElement(
                    'M',
                    ofChain(
                        buildHatchAdder(MTEIndustrialCrucible.class).atLeast(InputBus, OutputBus, Maintenance, Energy)
                            .casingIndex(CASING_INDEX)
                            .hint(1)
                            .build(),
                        ofSpecificTileAdder(
                            MTEIndustrialCrucible::addNormalEssentiaHatch,
                            MTEEssentiaInputHatch.class,
                            Loaders.essentiaInputHatch,
                            0),
                        onElementPass(t -> t.mCasing++, ofBlock(Loaders.magicCasing, 0))))
                .addElement('E', ofBlock(Loaders.essentiaFilterCasing, 0))
                .addElement('W', ofBlock(ConfigBlocks.blockCosmeticOpaque, 2))
                .addElement(
                    'H',
                    buildHatchAdder(MTEIndustrialCrucible.class).atLeast(Muffler)
                        .casingIndex(CASING_INDEX)
                        .hint(2)
                        .build())
                .addElement(
                    'D',
                    ofBlocksTiered(
                        (block, meta) -> block == Loaders.essentiaCell ? meta : null,
                        ImmutableList.of(
                            Pair.of(Loaders.essentiaCell, 0),
                            Pair.of(Loaders.essentiaCell, 1),
                            Pair.of(Loaders.essentiaCell, 2),
                            Pair.of(Loaders.essentiaCell, 3)),
                        -1,
                        (t, meta) -> t.pTier = meta,
                        t -> t.pTier))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasing = 0;
        this.pTier = -1;
        this.mCurrentLength = 0;
        this.mParallel = 0;

        if (!structureCheck_EM(STRUCTURE_PIECE_BOTTOM, 2, 0, 0)) return false;

        int middleLen = 0;
        while (middleLen < MAX_MIDDLE_LENGTH && structureCheck_EM(STRUCTURE_PIECE_MIDDLE, 2, 1 + middleLen, 0)) {
            middleLen++;
        }
        if (middleLen < MIN_MIDDLE_LENGTH) return false;

        if (!structureCheck_EM(STRUCTURE_PIECE_TOP, 2, middleLen + 2, 0)) return false;

        if (this.mCasing >= 28 && this.mMaintenanceHatches.size() == 1
            && !this.mEnergyHatches.isEmpty()
            && this.mMufflerHatches.size() == 1) {

            this.mCurrentLength = middleLen;
            this.mParallel = this.mCurrentLength * (1 << Math.max(0, this.pTier));
            return true;
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_BOTTOM, 2, 0, 0, stackSize, hintsOnly);
        int len = stackSize.stackSize;
        if (len < MIN_MIDDLE_LENGTH) len = MIN_MIDDLE_LENGTH;
        if (len > MAX_MIDDLE_LENGTH) len = MAX_MIDDLE_LENGTH;

        for (int i = 0; i < len; i++) {
            structureBuild_EM(STRUCTURE_PIECE_MIDDLE, 2, 1 + i, 0, stackSize, hintsOnly);
        }
        structureBuild_EM(STRUCTURE_PIECE_TOP, 2, len + 2, 0, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = survivalBuildPiece(STRUCTURE_PIECE_BOTTOM, stackSize, 2, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;

        int len = stackSize.stackSize;
        if (len < MIN_MIDDLE_LENGTH) len = MIN_MIDDLE_LENGTH;
        if (len > MAX_MIDDLE_LENGTH) len = MAX_MIDDLE_LENGTH;

        for (int i = 0; i < len; i++) {
            built = survivalBuildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, 2, 1 + i, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_TOP, stackSize, 2, len + 2, 0, elementBudget, env, false, true);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        long v = getMaxInputVoltage();
        if (mEnergyHatches.isEmpty() || v < 512 || getBaseMetaTileEntity().getStoredEU() < (v * 15 / 16))
            return SimpleCheckRecipeResult.ofFailure("ic.no_energy");

        ArrayList<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        ItemStack tome = mInventory[1];
        if (tome == null) return SimpleCheckRecipeResult.ofFailure("ic.no_tome");
        String bookOwner = null;
        if (tome.hasTagCompound() && tome.getTagCompound()
            .hasKey("player"))
            bookOwner = tome.getTagCompound()
                .getString("player");
        if (bookOwner == null || bookOwner.isEmpty()) return SimpleCheckRecipeResult.ofFailure("ic.no_tome");

        int circuit = 0;
        for (ItemStack item : inputs) {
            if (gregtech.api.enums.ItemList.Circuit_Integrated.isStackEqual(item, true, true))
                circuit = item.getItemDamage();
        }

        IndustrialCrucibleRecipes.CrucibleRecipeInfo info = null;
        for (IndustrialCrucibleRecipes.CrucibleRecipeInfo r : IndustrialCrucibleRecipes.CRUCIBLE_RECIPES) {
            if (r.circuitID != circuit) continue;
            for (ItemStack item : inputs) {
                if (GTUtility.areStacksEqual(r.catalyst, item, true)) {
                    info = r;
                    break;
                }
            }
            if (info != null) break;
        }

        if (info == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (info.researchKey != null && !info.researchKey.isEmpty()) {
            if (!thaumcraft.api.ThaumcraftApiHelper.isResearchComplete(bookOwner, info.researchKey)) {
                return SimpleCheckRecipeResult.ofFailure("ic.missing_research");
            }
        }

        int visPerCraft = 50;
        if (mIgnisCentiVis < visPerCraft || mOrdoCentiVis < visPerCraft || mAquaCentiVis < visPerCraft)
            return SimpleCheckRecipeResult.ofFailure("ic.no_vis");

        int maxP = Math.max(1, this.mParallel);
        int totalCat = 0;
        for (ItemStack is : inputs) {
            if (GTUtility.areStacksEqual(info.catalyst, is, true)) totalCat += is.stackSize;
        }
        int crafts = Math.min(totalCat, maxP);

        for (Map.Entry<thaumcraft.api.aspects.Aspect, Integer> entry : info.aspects.aspects.entrySet()) {
            int totalE = 0;
            for (MTEEssentiaInputHatch h : mEssentiaHatches) totalE += h.containerContains(entry.getKey());
            if (totalE < entry.getValue()) return SimpleCheckRecipeResult.ofFailure("ic.no_aspects");
            crafts = Math.min(crafts, totalE / entry.getValue());
        }

        crafts = Math.min(crafts, mIgnisCentiVis / visPerCraft);
        crafts = Math.min(crafts, mOrdoCentiVis / visPerCraft);
        crafts = Math.min(crafts, mAquaCentiVis / visPerCraft);
        if (crafts <= 0) return CheckRecipeResultRegistry.NO_RECIPE;

        mIgnisCentiVis -= crafts * visPerCraft;
        mOrdoCentiVis -= crafts * visPerCraft;
        mAquaCentiVis -= crafts * visPerCraft;

        int toCons = crafts;
        for (ItemStack in : inputs) {
            if (GTUtility.areStacksEqual(info.catalyst, in, true)) {
                int t = Math.min(in.stackSize, toCons);
                in.stackSize -= t;
                toCons -= t;
                if (toCons <= 0) break;
            }
        }

        for (Map.Entry<thaumcraft.api.aspects.Aspect, Integer> entry : info.aspects.aspects.entrySet()) {
            int needed = entry.getValue() * crafts;
            for (MTEEssentiaInputHatch h : mEssentiaHatches) {
                int take = Math.min(needed, h.containerContains(entry.getKey()));
                if (take > 0 && h.takeFromContainer(entry.getKey(), take)) needed -= take;
                if (needed <= 0) break;
            }
        }

        this.mOutputItems = new ItemStack[] { GTUtility.copyAmount(crafts * info.output.stackSize, info.output) };
        this.mPollution = 2200;

        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(480)
            .setEUt(v)
            .setDuration(320)
            .calculate();

        this.lEUt = -calculator.getConsumption();
        this.mMaxProgresstime = calculator.getDuration();

        this.useLongPower = true;
        this.mEfficiency = 10000;
        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mMachine && aTick % 10 == 0) {
            long v = getMaxInputVoltage();
            if (v >= 512) {
                int tier = GTUtility.getTier(v);
                int maxBuf = tier * 100000;
                int dr = (1 << (tier + 2)) * 25;
                if (mIgnisCentiVis < maxBuf) mIgnisCentiVis += thaumcraft.api.visnet.VisNetHandler.drainVis(
                    aBaseMetaTileEntity.getWorld(),
                    aBaseMetaTileEntity.getXCoord(),
                    aBaseMetaTileEntity.getYCoord(),
                    aBaseMetaTileEntity.getZCoord(),
                    thaumcraft.api.aspects.Aspect.FIRE,
                    dr);
                if (mOrdoCentiVis < maxBuf) mOrdoCentiVis += thaumcraft.api.visnet.VisNetHandler.drainVis(
                    aBaseMetaTileEntity.getWorld(),
                    aBaseMetaTileEntity.getXCoord(),
                    aBaseMetaTileEntity.getYCoord(),
                    aBaseMetaTileEntity.getZCoord(),
                    thaumcraft.api.aspects.Aspect.ORDER,
                    dr);
                if (mAquaCentiVis < maxBuf) mAquaCentiVis += thaumcraft.api.visnet.VisNetHandler.drainVis(
                    aBaseMetaTileEntity.getWorld(),
                    aBaseMetaTileEntity.getXCoord(),
                    aBaseMetaTileEntity.getYCoord(),
                    aBaseMetaTileEntity.getZCoord(),
                    thaumcraft.api.aspects.Aspect.WATER,
                    dr);
            }
        }
    }

    private boolean addNormalEssentiaHatch(MTEEssentiaInputHatch aTileEntity) {
        return aTileEntity != null && this.mEssentiaHatches.add(aTileEntity);
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        this.mEssentiaHatches.clear();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mIgnis", mIgnisCentiVis);
        aNBT.setInteger("mOrdo", mOrdoCentiVis);
        aNBT.setInteger("mAqua", mAquaCentiVis);
        aNBT.setInteger("pTier", pTier);
        aNBT.setInteger("mCurrentLength", mCurrentLength);
        aNBT.setInteger("mParallel", mParallel);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mIgnisCentiVis = aNBT.getInteger("mIgnis");
        mOrdoCentiVis = aNBT.getInteger("mOrdo");
        mAquaCentiVis = aNBT.getInteger("mAqua");
        pTier = aNBT.getInteger("pTier");
        mCurrentLength = aNBT.getInteger("mCurrentLength");
        mParallel = aNBT.getInteger("mParallel");
    }

    //?
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
                                 int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.of(
                    (IIconContainer) (aActive
                        ? Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE
                        : Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE))
            };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }
}
