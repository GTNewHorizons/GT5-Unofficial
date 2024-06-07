package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class LargeEssentiaSmeltery extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
    implements IConstructable, ISurvivalConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon(
        "icons/LargeEssentiaSmeltery_On");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
        "icons/LargeEssentiaSmeltery_On_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon(
        "icons/LargeEssentiaSmeltery_Off");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
        "icons/LargeEssentiaSmeltery_Off_GLOW");
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    private static final int CASING_INDEX = 1536;
    private static final int MAX_STRUCTURE_LENGTH = 8;
    private static final int DEFAULT_STRUCTURE_LENGTH = 3;
    private static final int MAX_CONFIGURABLE_LENGTH = MAX_STRUCTURE_LENGTH - DEFAULT_STRUCTURE_LENGTH;

    private static final int RECIPE_DURATION = 32;
    private static final int RECIPE_EUT = 480;
    private static final float NODE_COST_MULTIPLIER = 1.15f;

    public AspectList mOutputAspects = new AspectList();
    protected int mCasing = 0;
    protected double mParallel = 0;
    protected int nodePower = 0;
    protected int nodePurificationEfficiency = 0;
    protected int nodeIncrease = 0;

    private IStructureDefinition<LargeEssentiaSmeltery> multiDefinition = null;
    private ArrayList<EssentiaOutputHatch> mEssentiaOutputHatches = new ArrayList<>();
    private int pTier = 0;
    private XSTR xstr = new XSTR();

    public LargeEssentiaSmeltery(String name) {
        super(name);
    }

    public LargeEssentiaSmeltery(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_FIRST, 2, 2, 0, itemStack, hintsOnly);
        // default
        structureBuild_EM(STRUCTURE_PIECE_LATER, 2, 2, -1, itemStack, hintsOnly);
        structureBuild_EM(STRUCTURE_PIECE_LATER, 2, 2, -2, itemStack, hintsOnly);
        int len = itemStack.stackSize;
        if (len > MAX_CONFIGURABLE_LENGTH) len = MAX_CONFIGURABLE_LENGTH;
        structureBuild_EM(STRUCTURE_PIECE_LAST, 2, 2, -len - 3, itemStack, hintsOnly);
        while (len > 0) {
            structureBuild_EM(STRUCTURE_PIECE_LATER, 2, 2, -len - 2, itemStack, hintsOnly);
            len--;
        }
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        mEssentiaOutputHatches.clear();
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.mCasing = 0;
        this.mParallel = 0;
        this.pTier = 0;
        this.nodePower = 0;
        this.nodePurificationEfficiency = 0;
        this.nodeIncrease = 0;

        if (!structureCheck_EM(STRUCTURE_PIECE_FIRST, 2, 2, 0)) return false;
        if (!structureCheck_EM(STRUCTURE_PIECE_LATER, 2, 2, -1)) return false;
        if (!structureCheck_EM(STRUCTURE_PIECE_LATER, 2, 2, -2)) return false;
        int len = 2;
        while (structureCheck_EM(STRUCTURE_PIECE_LATER, 2, 2, -len - 1)) len++;
        if (len > MAX_STRUCTURE_LENGTH - 1 || len < DEFAULT_STRUCTURE_LENGTH) return false;
        if (!structureCheck_EM(STRUCTURE_PIECE_LAST, 2, 2, -len - 1)) return false;
        if (this.mCasing >= 24 && this.mMaintenanceHatches.size() == 1
            && this.mInputBusses.size() >= 1
            && this.mEssentiaOutputHatches.size() >= 1) {
            this.mParallel = Math.floor(this.mParallel += 1 << this.pTier);
            return true;
        }
        return false;
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        if (this.multiDefinition == null) {
            this.multiDefinition = StructureDefinition.<LargeEssentiaSmeltery>builder()
                .addShape(
                    "first",
                    transpose(new String[][] { { "  A  " }, { " AAA " }, { "AA~AA" }, { " AAA " }, { "  A  " } }))
                .addShape(
                    "later",
                    transpose(new String[][] { { " ABA " }, { "AECEA" }, { "D---D" }, { "AEFEA" }, { " AAA " } }))
                .addShape(
                    "last",
                    transpose(new String[][] { { "  A  " }, { " AAA " }, { "AAAAA" }, { " AAA " }, { "  A  " } }))
                .addElement('C', ofBlock(Loaders.essentiaFilterCasing, 0))
                .addElement('D', ofBlock(ConfigBlocks.blockCosmeticOpaque, 2))
                .addElement(
                    'F',
                    ThaumicBases.isModLoaded() ? ofBlock(Block.getBlockFromName("thaumicbases:advAlchFurnace"), 0)
                        : ofBlock(ConfigBlocks.blockStoneDevice, 0))
                .addElement(
                    'E',
                    ofChain(
                        onElementPass(x -> x.onEssentiaCellFound(0), ofBlock(Loaders.essentiaCell, 0)),
                        onElementPass(x -> x.onEssentiaCellFound(1), ofBlock(Loaders.essentiaCell, 1)),
                        onElementPass(x -> x.onEssentiaCellFound(2), ofBlock(Loaders.essentiaCell, 2)),
                        onElementPass(x -> x.onEssentiaCellFound(3), ofBlock(Loaders.essentiaCell, 3))))
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(LargeEssentiaSmeltery.class)
                            .atLeast(GT_HatchElement.Maintenance, GT_HatchElement.Energy, GT_HatchElement.InputBus)
                            .casingIndex(CASING_INDEX)
                            .dot(1)
                            .build(),
                        ofSpecificTileAdder(
                            LargeEssentiaSmeltery::addEssentiaOutputHatchToMachineList,
                            EssentiaOutputHatch.class,
                            Loaders.essentiaOutputHatch,
                            0),
                        onElementPass(LargeEssentiaSmeltery::onCasingFound, ofBlock(Loaders.magicCasing, 0))))
                .addElement('B', GT_HatchElement.Muffler.newAny(CASING_INDEX, 2))
                .build();
        }
        return this.multiDefinition;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Essentia Smeltery")
            .addInfo("Controller block for the Large Essentia Smeltery")
            .addInfo("Necessary evil.")
            .addInfo("Advanced Essentia smelting technology.")
            .addInfo("Max parallel dictated by structure size and Essentia Diffusion Cell tier")
            .addInfo("Energy Hatch tier: HV+")
            .addInfo("You can find more information about this machine in the Thaumonomicon.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .addController("Front center")
            .addCasingInfo("Magic Casing", 24)
            .addMaintenanceHatch("Hint block with dot 1")
            .addInputBus("Hint block with dot 1")
            .addInputHatch("Hint block with dot 1")
            .addEnergyHatch("Hint block with dot 1")
            .addOtherStructurePart("Essentia Output Hatch", "Hint block with dot 1")
            .addMufflerHatch("Hint block with dot 2")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("LargeEssentiaSmeltery.hint", 8);
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[8] = "Node Power: " + EnumChatFormatting.RED
            + this.nodePower
            + EnumChatFormatting.RESET
            + " Purification Efficiency: "
            + EnumChatFormatting.AQUA
            + this.nodePurificationEfficiency
            + "%"
            + EnumChatFormatting.RESET
            + " Speed Up: "
            + EnumChatFormatting.GRAY
            + this.nodeIncrease
            + "%"
            + EnumChatFormatting.RESET;
        return info;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.of(textureFontOn), TextureFactory.builder()
                    .addIcon(textureFontOn_Glow)
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.of(textureFontOff), TextureFactory.builder()
                    .addIcon(textureFontOff_Glow)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    protected void onCasingFound() {
        this.mCasing++;
    }

    protected void onEssentiaCellFound(int tier) {
        this.mParallel += (1 << tier) * 0.25f;
        this.pTier = Math.max(this.pTier, tier);
    }

    private boolean addEnergyHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                if (((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity).mTier < 3) return false;
                ((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
                ((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.eEnergyMulti.add(((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity));
            } else {
                return false;
            }
        }
    }

    private boolean addEssentiaOutputHatchToMachineList(EssentiaOutputHatch aTileEntity) {
        if (aTileEntity instanceof EssentiaOutputHatch) {
            return this.mEssentiaOutputHatches.add((EssentiaOutputHatch) aTileEntity);
        }
        return false;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!this.isFullPower()) return;
        super.runMachine(aBaseMetaTileEntity, aTick);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        if (!isFullPower()) return SimpleCheckRecipeResult.ofFailure("node_too_small");

        ArrayList<ItemStack> tInputList = getStoredInputs();

        if (tInputList.size() == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        int p = (int) this.mParallel;
        for (int i = tInputList.size() - 1; i >= 0; i--) {
            ItemStack itemStack = tInputList.get(i);
            int stackSize = itemStack.stackSize;
            int sur = p - stackSize;

            if (sur > 0) {
                p -= stackSize;
                this.mOutputAspects.add(getEssentia(itemStack, stackSize));
                if (!depleteInput(itemStack)) itemStack.stackSize = 0;
            } else if (sur == 0) {
                this.mOutputAspects.add(getEssentia(itemStack, stackSize));
                if (!depleteInput(itemStack)) itemStack.stackSize = 0;
                break;
            } else {
                this.mOutputAspects.add(getEssentia(itemStack, p));
                itemStack.stackSize -= p;
                break;
            }
        }

        this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;

        final World WORLD = this.getBaseMetaTileEntity()
            .getWorld();
        int x = this.getBaseMetaTileEntity()
            .getXCoord();
        int y = this.getBaseMetaTileEntity()
            .getYCoord();
        int z = this.getBaseMetaTileEntity()
            .getZCoord();

        this.drainNodePower(WORLD, x, y, z);
        this.nodePower -= expectedPower();

        calculatePerfectOverclockedNessMulti(
            RECIPE_EUT,
            (int) Math.ceil(this.mOutputAspects.visSize() * RECIPE_DURATION * (1 - this.nodeIncrease * 0.005)),
            1,
            Math.min(Integer.MAX_VALUE, getMaxInputEnergy_EM()));

        this.updateSlots();
        if (this.mEUt > 0) this.mEUt = -this.mEUt;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private AspectList getEssentia(ItemStack itemStack, int amount) {
        AspectList aspectList = new AspectList();
        AspectList aspects = ThaumcraftCraftingManager.getObjectTags(itemStack);
        aspects = ThaumcraftCraftingManager.getBonusTags(itemStack, aspects);
        if (aspects != null && aspects.size() != 0 && aspects.getAspects()[0] != null) {
            for (int i = 0; i < amount; i++) aspectList.add(aspects);
        } else aspectList.add(Aspect.ENTROPY, amount);
        return aspectList;
    }

    private void fillEssentiaOutputHatch() {
        for (EssentiaOutputHatch outputHatch : this.mEssentiaOutputHatches) {
            for (Map.Entry<Aspect, Integer> entry : this.mOutputAspects.copy().aspects.entrySet()) {
                Aspect aspect = entry.getKey();
                int amount = entry.getValue();
                this.mOutputAspects.remove(aspect, outputHatch.addEssentia(aspect, amount, null));
            }
        }
        this.mOutputAspects.aspects.clear();
    }

    private int expectedPower() {
        return (int) (Math.pow(this.getMaxEnergyInputTier_EM(), 2) * NODE_COST_MULTIPLIER);
    }

    private boolean isFullPower() {
        return this.nodePower > expectedPower();
    }

    private void generateFluxGas(World world, int x, int y, int z) {
        world.setBlock(x, y, z, ConfigBlocks.blockFluxGas, 8, 3);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mParallel", this.mParallel);
        aNBT.setDouble("nodePower", this.nodePower);
        aNBT.setDouble("nodePurificationEfficiency", this.nodePurificationEfficiency);
        aNBT.setDouble("nodeIncrease", this.nodeIncrease);

        Aspect[] aspectA = this.mOutputAspects.getAspects();
        NBTTagList nbtTagList = new NBTTagList();
        for (Aspect aspect : aspectA) {
            if (aspect != null) {
                NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.mOutputAspects.getAmount(aspect));
                nbtTagList.appendTag(f);
            }
        }
        aNBT.setTag("Aspects", nbtTagList);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mParallel = aNBT.getDouble("mParallel");
        this.nodePower = aNBT.getInteger("nodePower");
        this.nodePurificationEfficiency = aNBT.getInteger("nodePurificationEfficiency");
        this.nodeIncrease = aNBT.getInteger("nodeIncrease");

        this.mOutputAspects.aspects.clear();
        NBTTagList tlist = aNBT.getTagList("Aspects", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key"))
                this.mOutputAspects.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
        }
        super.loadNBTData(aNBT);
    }

    @Override
    protected void addClassicOutputs_EM() {
        super.addClassicOutputs_EM();
        fillEssentiaOutputHatch();
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        this.mOutputAspects.aspects.clear();
    }

    private void drainNodePower(World world, int x, int y, int z) {
        int power = this.expectedPower();
        if (this.nodePower < power * 10) {
            this.nodePower += VisNetHandler.drainVis(world, x, y, z, Aspect.WATER, power);
            this.nodePower += VisNetHandler.drainVis(world, x, y, z, Aspect.FIRE, power);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 5 == 0 && this.mMachine) {
            final World WORLD = this.getBaseMetaTileEntity()
                .getWorld();
            int x = this.getBaseMetaTileEntity()
                .getXCoord();
            int y = this.getBaseMetaTileEntity()
                .getYCoord();
            int z = this.getBaseMetaTileEntity()
                .getZCoord();

            this.drainNodePower(WORLD, x, y, z);

            this.nodePurificationEfficiency = Math.max(0, this.nodePurificationEfficiency - 1);
            if (this.nodePurificationEfficiency < 100) {
                this.nodePurificationEfficiency = (int) Math.min(
                    100,
                    this.nodePurificationEfficiency
                        + Math.ceil(VisNetHandler.drainVis(WORLD, x, y, z, Aspect.ORDER, 200) * 0.05));
            }

            this.nodeIncrease = Math.min(100, VisNetHandler.drainVis(WORLD, x, y, z, Aspect.ENTROPY, 125));
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        this.nodePurificationEfficiency = Math.max(0, this.nodePurificationEfficiency - 1);
        if (xstr.nextInt(20) == 0) {
            if (xstr.nextInt(100) < Math.max(100 - this.nodePurificationEfficiency, 0)) {
                final World WORLD = this.getBaseMetaTileEntity()
                    .getWorld();
                GT_MetaTileEntity_Hatch_Muffler mufflerHatch = this.mMufflerHatches
                    .get(xstr.nextInt(this.mMufflerHatches.size()));
                int x = mufflerHatch.getBaseMetaTileEntity()
                    .getXCoord();
                int y = mufflerHatch.getBaseMetaTileEntity()
                    .getYCoord();
                int z = mufflerHatch.getBaseMetaTileEntity()
                    .getZCoord();

                ForgeDirection facing = mufflerHatch.getBaseMetaTileEntity()
                    .getFrontFacing();
                switch (facing) {
                    case SOUTH:
                        z += 1;
                        break;
                    case NORTH:
                        z -= 1;
                        break;
                    case WEST:
                        x -= 1;
                        break;
                    case EAST:
                        x += 1;
                        break;
                    default:
                        y += 1;
                }
                generateFluxGas(WORLD, x, y, z);
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 22 * (100 - this.nodePurificationEfficiency);
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new LargeEssentiaSmeltery(this.mName);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = survivialBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 2, 2, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int length = stackSize.stackSize + 2;
        if (length > MAX_CONFIGURABLE_LENGTH) length = MAX_CONFIGURABLE_LENGTH + 2;
        for (int i = 1; i <= length; i++) {
            built = survivialBuildPiece(STRUCTURE_PIECE_LATER, stackSize, 2, 2, -i, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        return survivialBuildPiece(STRUCTURE_PIECE_LAST, stackSize, 2, 2, -length - 1, elementBudget, env, false, true);
    }
}
