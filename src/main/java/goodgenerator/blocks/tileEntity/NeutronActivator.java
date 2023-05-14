package goodgenerator.blocks.tileEntity;

import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.util.Pair;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import goodgenerator.blocks.tileEntity.GTMetaTileEntity.NeutronAccelerator;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.NeutronSensor;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.CharExchanger;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;

public class NeutronActivator extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    protected static IStructureDefinition<NeutronActivator> multiDefinition = null;
    protected final ArrayList<NeutronAccelerator> mNeutronAccelerator = new ArrayList<>();
    protected final ArrayList<NeutronSensor> mNeutronSensor = new ArrayList<>();
    protected int casingAmount = 0;
    protected int height = 0;
    protected int eV = 0, mCeil = 0, mFloor = 0;
    private GT_Recipe lastRecipe;
    final XSTR R = new XSTR();

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("icons/NeutronActivator_On");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
            "icons/NeutronActivator_On_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon(
            "icons/NeutronActivator_Off");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
            "icons/NeutronActivator_Off_GLOW");

    protected final String NA_BOTTOM = mName + "buttom";
    protected final String NA_MID = mName + "mid";
    protected final String NA_TOP = mName + "top";

    public NeutronActivator(String name) {
        super(name);
    }

    public NeutronActivator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        this.mEfficiency = 10000;

        FluidStack[] inFluids = getStoredFluids().toArray(new FluidStack[0]);
        ItemStack[] inItems = getStoredInputs().toArray(new ItemStack[0]);
        int minNKE, maxNKE;

        lastRecipe = getRecipeMap()
                .findRecipe(this.getBaseMetaTileEntity(), lastRecipe, false, Integer.MAX_VALUE, inFluids, inItems);

        if (lastRecipe != null) {
            minNKE = (lastRecipe.mSpecialValue % 10000) * 1000000;
            maxNKE = (lastRecipe.mSpecialValue / 10000) * 1000000;
            if (batchMode) {
                int pall = handleParallelRecipe(lastRecipe, inFluids, inItems, 128);
                if (pall > 0) {
                    mFloor = minNKE;
                    mCeil = maxNKE;
                    mMaxProgresstime = Math.max((int) (lastRecipe.mDuration * pall * Math.pow(0.9, height - 4)), 1);
                    Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> Outputs = getMultiOutput(this.lastRecipe, pall);
                    if (eV <= maxNKE && eV >= minNKE) {
                        this.mOutputFluids = Outputs.getKey().toArray(new FluidStack[0]);
                        this.mOutputItems = Outputs.getValue().toArray(new ItemStack[0]);
                    } else {
                        this.mOutputFluids = null;
                        this.mOutputItems = new ItemStack[] { ItemRefer.Radioactive_Waste.get(4) };
                    }
                    return true;
                }
            } else if (lastRecipe.isRecipeInputEqual(true, inFluids, inItems)) {
                mFloor = minNKE;
                mCeil = maxNKE;
                mMaxProgresstime = Math.max((int) (lastRecipe.mDuration * Math.pow(0.9, height - 4)), 1);
                if (eV <= maxNKE && eV >= minNKE) {
                    this.mOutputFluids = lastRecipe.mFluidOutputs;
                    this.mOutputItems = lastRecipe.mOutputs;
                } else {
                    this.mOutputFluids = null;
                    this.mOutputItems = new ItemStack[] { ItemRefer.Radioactive_Waste.get(4) };
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
            float aX, float aY, float aZ) {
        batchMode = !batchMode;
        if (batchMode) {
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
        } else {
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
        }
        return true;
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
    public void loadNBTData(NBTTagCompound aNBT) {
        eV = aNBT.getInteger("mKeV");
        mCeil = aNBT.getInteger("mCeil");
        mFloor = aNBT.getInteger("mFloor");
        height = aNBT.getInteger("height");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mKeV", eV);
        aNBT.setInteger("mCeil", mCeil);
        aNBT.setInteger("mFloor", mFloor);
        aNBT.setInteger("height", height);
        super.saveNBTData(aNBT);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return MyRecipeAdder.instance.NA;
    }

    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Neutron Activator").addInfo("Controller block for the Neutron Activator")
                .addInfo("Superluminal-velocity Motion.")
                .addInfo("The minimum height of the Speeding Pipe Casing is 4.")
                .addInfo("Per extra Speeding Pipe Casing will give time discount.")
                .addInfo("But it will reduce the Neutron Accelerator efficiency.")
                .addInfo("You need to input energy to the Neutron Accelerator to get it running.")
                .addInfo("It will output correct products with Specific Neutron Kinetic Energy.")
                .addInfo("Otherwise it will output trash.")
                .addInfo("The Neutron Kinetic Energy will decrease 72KeV/s when no Neutron Accelerator is running.")
                .addInfo(
                        "It will explode when the Neutron Kinetic Energy is over" + EnumChatFormatting.RED
                                + " 1200MeV"
                                + EnumChatFormatting.GRAY
                                + ".")
                .addInfo("Inputting Graphite/Beryllium dust can reduce 10MeV per dust immediately.")
                .addInfo("The structure is too complex!").addInfo(BLUE_PRINT_INFO).addSeparator()
                .addController("Front bottom").addInputHatch("Hint block with dot 1")
                .addInputBus("Hint block with dot 1").addOutputHatch("Hint block with dot 2")
                .addOutputBus("Hint block with dot 2").addMaintenanceHatch("Hint block with dot 2")
                .addOtherStructurePart("Neutron Accelerator", "Hint block with dot 2")
                .addOtherStructurePart("Neutron Sensor", "Hint block with dot 2")
                .addCasingInfoRange("Clean Stainless Steel Machine Casing", 7, 31, false)
                .addCasingInfoExactly("Processor Machine Casing", 18, false)
                .addCasingInfoMin("Steel Frame Box", 16, false).addCasingInfoMin("Speeding Pipe Casing", 4, false)
                .addCasingInfoMin("EV+ Glass", 32, false).toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public IStructureDefinition<NeutronActivator> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<NeutronActivator>builder()
                    .addShape(NA_TOP, transpose(new String[][] { { "CCCCC", "CDDDC", "CDDDC", "CDDDC", "CCCCC" } }))
                    .addShape(NA_MID, transpose(new String[][] { { "F   F", " GGG ", " GPG ", " GGG ", "F   F" } }))
                    .addShape(NA_BOTTOM, transpose(new String[][] { { "XX~XX", "XDDDX", "XDDDX", "XDDDX", "XXXXX" } }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildHatchAdder(NeutronActivator.class).atLeast(
                                            GT_HatchElement.InputHatch,
                                            GT_HatchElement.InputBus,
                                            GT_HatchElement.Maintenance).casingIndex(49).dot(1).build(),
                                    onElementPass(
                                            NeutronActivator::onCasingFound,
                                            ofBlock(GregTech_API.sBlockCasings4, 1))))
                    .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 6)).addElement('F', ofFrame(Materials.Steel))
                    .addElement('G', Glasses.chainAllGlasses()).addElement(
                            'P',
                            ofBlock(Loaders.speedingPipe, 0))
                    .addElement(
                            'X',
                            ofChain(
                                    buildHatchAdder(NeutronActivator.class).atLeast(
                                            GT_HatchElement.OutputHatch,
                                            GT_HatchElement.OutputBus,
                                            GT_HatchElement.Maintenance,
                                            NeutronHatchElement.NeutronAccelerator,
                                            NeutronHatchElement.NeutronSensor).casingIndex(49).dot(2).build(),
                                    onElementPass(
                                            NeutronActivator::onCasingFound,
                                            ofBlock(GregTech_API.sBlockCasings4, 1))))
                    .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.casingAmount = 0;
        this.mNeutronAccelerator.clear();
        this.mNeutronSensor.clear();
        if (!structureCheck_EM(NA_BOTTOM, 2, 0, 0)) return false;
        height = 0;
        while (structureCheck_EM(NA_MID, 2, height + 1, 0)) {
            height++;
        }
        if (height < 4) return false;
        return structureCheck_EM(NA_TOP, 2, height + 1, 0) && casingAmount >= 7;
    }

    public final boolean addAcceleratorAndSensor(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof NeutronAccelerator) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mNeutronAccelerator.add((NeutronAccelerator) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof NeutronSensor) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mNeutronSensor.add((NeutronSensor) aMetaTileEntity);
            }
        }
        return false;
    }

    public int maxNeutronKineticEnergy() {
        return 1200000000;
    }

    public int getCurrentNeutronKineticEnergy() {
        return eV;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new NeutronActivator(this.mName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        boolean anyWorking = false;
        if (aBaseMetaTileEntity.isServerSide()) {
            startRecipeProcessing();
            for (ItemStack input : getStoredInputs()) {
                if (input.isItemEqual(Materials.Graphite.getDust(1))
                        || input.isItemEqual(Materials.Beryllium.getDust(1))) {
                    int consume = Math.min(this.eV / 10000000, input.stackSize);
                    depleteInput(GT_Utility.copyAmount(consume, input));
                    this.eV -= 10000000 * consume;
                }
            }

            for (NeutronAccelerator tHatch : mNeutronAccelerator) {
                if (tHatch.getBaseMetaTileEntity().isActive() && this.getRepairStatus() == this.getIdealStatus()) {
                    anyWorking = true;
                    this.eV += Math.max(
                            (R.nextInt(tHatch.getMaxEUConsume() + 1) + tHatch.getMaxEUConsume()) * 10
                                    * Math.pow(0.95, height - 4),
                            10);
                }
            }
            if (!anyWorking) {
                if (this.eV >= 72000 && aTick % 20 == 0) {
                    this.eV -= 72000;
                } else if (this.eV > 0 && aTick % 20 == 0) {
                    this.eV = 0;
                }
            }
            if (this.eV < 0) this.eV = 0;
            if (this.eV > maxNeutronKineticEnergy()) doExplosion(4 * 32);

            for (NeutronSensor tHatch : mNeutronSensor) {
                String tText = tHatch.getText();
                if (CharExchanger.isValidCompareExpress(rawProcessExp(tText))) {
                    if (CharExchanger.compareExpression(rawProcessExp(tText), eV)) {
                        tHatch.outputRedstoneSignal();
                    } else tHatch.stopOutputRedstoneSignal();
                }
            }

            if (mProgresstime < mMaxProgresstime && (eV > mCeil || eV < mFloor)) {
                this.mOutputFluids = null;
                this.mOutputItems = new ItemStack[] { ItemRefer.Radioactive_Waste.get(4) };
            }
            endRecipeProcessing();
        }
    }

    public static String rawProcessExp(String exp) {
        StringBuilder ret = new StringBuilder();
        for (char c : exp.toCharArray()) {
            if (exp.length() - ret.length() == 3) {
                if (Character.isDigit(c)) ret.append(c);
                else {
                    if (c == 'K' || c == 'k') {
                        ret.append("000");
                    }
                    if (c == 'M' || c == 'm') {
                        ret.append("000000");
                    }
                }
                break;
            }
            ret.append(c);
        }
        return ret.toString();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(NA_BOTTOM, 2, 0, 0, stackSize, hintsOnly);
        int heights = stackSize.stackSize + 3;
        structureBuild_EM(NA_TOP, 2, heights + 1, 0, stackSize, hintsOnly);
        while (heights > 0) {
            structureBuild_EM(NA_MID, 2, heights, 0, stackSize, hintsOnly);
            heights--;
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("NeutronActivator.hint", 7);
    }

    @Override
    public String[] getInfoData() {
        int currentNKEInput = 0;
        boolean anyWorking = false;
        for (NeutronAccelerator tHatch : mNeutronAccelerator) {
            if (tHatch.getBaseMetaTileEntity().isActive()) {
                currentNKEInput += (R.nextInt(tHatch.getMaxEUConsume() + 1) + tHatch.getMaxEUConsume()) * 10
                        * Math.pow(0.95, height - 4);
                anyWorking = true;
            }
        }
        if (!anyWorking) currentNKEInput = -72000;
        return new String[] { "Progress:",
                EnumChatFormatting.GREEN + Integer.toString(this.mProgresstime / 20)
                        + EnumChatFormatting.RESET
                        + " s / "
                        + EnumChatFormatting.YELLOW
                        + this.mMaxProgresstime / 20
                        + EnumChatFormatting.RESET
                        + " s",
                "Current Neutron Kinetic Energy Input: " + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(currentNKEInput)
                        + EnumChatFormatting.RESET
                        + "eV",
                StatCollector.translateToLocal("scanner.info.NA") + " "
                        + EnumChatFormatting.LIGHT_PURPLE
                        + GT_Utility.formatNumbers(getCurrentNeutronKineticEnergy())
                        + EnumChatFormatting.RESET
                        + "eV" };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive)
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.of(textureFontOn),
                        TextureFactory.builder().addIcon(textureFontOn_Glow).glow().build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49),
                    TextureFactory.of(textureFontOff),
                    TextureFactory.builder().addIcon(textureFontOff_Glow).glow().build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;

        int built = 0;
        built += survivialBuildPiece(NA_BOTTOM, stackSize, 2, 0, 0, elementBudget, source, actor, false, true);
        int heights = stackSize.stackSize + 3;
        built += survivialBuildPiece(
                NA_TOP,
                stackSize,
                2,
                heights + 1,
                0,
                elementBudget - built,
                source,
                actor,
                false,
                true);
        while (heights > 0) {
            built += survivialBuildPiece(
                    NA_MID,
                    stackSize,
                    2,
                    heights,
                    0,
                    elementBudget - built,
                    source,
                    actor,
                    false,
                    true);
            heights--;
        }
        return built;
    }

    protected void onCasingFound() {
        casingAmount++;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
                .widget(
                        new TextWidget(StatCollector.translateToLocal("gui.NeutronActivator.0"))
                                .setDefaultColor(COLOR_TEXT_WHITE.get()))
                .widget(
                        TextWidget.dynamicString(() -> processNumber(eV) + "eV").setSynced(false)
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> eV, val -> eV = val));
    }

    private String processNumber(int num) {
        float num2;
        num2 = ((float) num) / 1000F;
        if (num2 <= 0) {
            return String.format("%d", num);
        }
        if (num2 < 1000.0) {
            return String.format("%.1fK", num2);
        }
        num2 /= 1000F;
        return String.format("%.1fM", num2);
    }

    private enum NeutronHatchElement implements IHatchElement<NeutronActivator> {

        NeutronSensor(NeutronActivator::addAcceleratorAndSensor, NeutronSensor.class) {

            @Override
            public long count(NeutronActivator t) {
                return t.mNeutronSensor.size();
            }
        },
        NeutronAccelerator(NeutronActivator::addAcceleratorAndSensor, NeutronAccelerator.class) {

            @Override
            public long count(NeutronActivator t) {
                return t.mNeutronAccelerator.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<NeutronActivator> adder;

        @SafeVarargs
        NeutronHatchElement(IGT_HatchAdder<NeutronActivator> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super NeutronActivator> adder() {
            return adder;
        }
    }
}
