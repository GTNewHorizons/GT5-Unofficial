package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GT_StructureUtility.*;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.*;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.YOTTAHatch;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.client.GUI.YOTTankGUIClient;
import goodgenerator.common.container.YOTTankGUIContainer;
import goodgenerator.loader.Loaders;
import goodgenerator.util.CharExchanger;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class YottaFluidTank extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow =
            new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow =
            new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<YottaFluidTank> multiDefinition = null;
    protected final ArrayList<YOTTAHatch> mYottaHatch = new ArrayList<>();

    public BigInteger mStorage = new BigInteger("0", 10);
    public BigInteger mStorageCurrent = new BigInteger("0", 10);
    public String mFluidName = "";
    protected int glassMeta;
    protected int maxCell;
    protected final String YOTTANK_BOTTOM = mName + "buttom";
    protected final String YOTTANK_MID = mName + "mid";
    protected final String YOTTANK_TOP = mName + "top";

    public YottaFluidTank(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public YottaFluidTank(String name) {
        super(name);
    }

    public int getMeta() {
        return glassMeta;
    }

    public void setMeta(int meta) {
        glassMeta = meta;
    }

    public String getCap() {
        return mStorage.toString(10);
    }

    public String getStored() {
        return mStorageCurrent.toString(10);
    }

    public String getFluidName() {
        if (mFluidName == null || mFluidName.equals("") || FluidRegistry.getFluidStack(mFluidName, 1) == null)
            return "Empty";
        return FluidRegistry.getFluidStack(mFluidName, 1).getLocalizedName();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        String tAmount = aNBT.getString("mStorage");
        String tAmountCurrent = aNBT.getString("mStorageCurrent");
        if (tAmount == null || tAmount.equals("")) tAmount = "0";
        if (tAmountCurrent == null || tAmountCurrent.equals("")) tAmountCurrent = "0";
        mStorage = new BigInteger(tAmount, 10);
        mStorageCurrent = new BigInteger(tAmountCurrent, 10);
        mFluidName = aNBT.getString("mFluidName");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("mStorage", mStorage.toString(10));
        aNBT.setString("mStorageCurrent", mStorageCurrent.toString(10));
        aNBT.setString("mFluidName", mFluidName);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        this.mEUt = 0;
        this.mMaxProgresstime = 20;
        return true;
    }

    public boolean reduceFluid(long amount) {
        BigInteger tmp = new BigInteger(amount + "");
        if (mStorageCurrent.compareTo(tmp) < 0) {
            return false;
        } else {
            mStorageCurrent = mStorageCurrent.subtract(tmp);
            return true;
        }
    }

    public boolean addFluid(long amount) {
        BigInteger tmp = new BigInteger(amount + "");
        if (mStorage.subtract(mStorageCurrent).compareTo(tmp) < 0) {
            return false;
        } else {
            mStorageCurrent = mStorageCurrent.add(tmp);
            return true;
        }
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        int fluidSize = mStorageCurrent.compareTo(new BigInteger(Integer.MAX_VALUE + "")) > 0
                ? Integer.MAX_VALUE
                : mStorageCurrent.intValue();
        return new FluidTankInfo[] {new FluidTankInfo(FluidRegistry.getFluidStack(mFluidName, fluidSize), fluidSize)};
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mStorage = BigInteger.ZERO;
        glassMeta = 0;
        maxCell = 0;
        mYottaHatch.clear();
        if (!structureCheck_EM(YOTTANK_BOTTOM, 2, 0, 0)) return false;
        int cnt = 0;
        while (structureCheck_EM(YOTTANK_MID, 2, cnt + 1, 0)) {
            cnt++;
        }
        if (cnt > 15 || cnt < 1) return false;
        if (!structureCheck_EM(YOTTANK_TOP, 2, cnt + 2, 0)) return false;
        // maxCell+1 = Tier of highest Cell. glassMeta is the glass voltage tier
        if (mMaintenanceHatches.size() == 1 && maxCell + 3 <= glassMeta) {
            if (mStorage.compareTo(mStorageCurrent) < 0) mStorageCurrent = mStorage;
            if (FluidRegistry.getFluidStack(mFluidName, 1) == null) {
                mStorageCurrent = BigInteger.ZERO;
                mFluidName = "";
            }
            return true;
        }
        return false;
    }

    @Override
    public IStructureDefinition<YottaFluidTank> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<YottaFluidTank>builder()
                    .addShape(YOTTANK_BOTTOM, transpose(new String[][] {
                        {"MM~MM", "MCCCM", "MCCCM", "MCCCM", "MMMMM"},
                        {"     ", " OOO ", " OOO ", " OOO ", "     "}
                    }))
                    .addShape(YOTTANK_MID, transpose(new String[][] {{"GGGGG", "GRRRG", "GRRRG", "GRRRG", "GGGGG"}}))
                    .addShape(YOTTANK_TOP, transpose(new String[][] {
                        {"FFFFF", "F   F", "F   F", "F   F", "FFFFF"},
                        {"CCCCC", "CIIIC", "CIIIC", "CIIIC", "CCCCC"}
                    }))
                    .addElement('C', ofBlock(Loaders.yottaFluidTankCasing, 0))
                    .addElement(
                            'G',
                            withChannel(
                                    "glass",
                                    BorosilicateGlass.ofBoroGlass(
                                            (byte) 0, (byte) 1, Byte.MAX_VALUE, YottaFluidTank::setMeta, te ->
                                                    (byte) te.getMeta())))
                    .addElement('R', ofChain(cells(10)))
                    .addElement('F', ofFrame(Materials.Steel))
                    .addElement(
                            'I',
                            ofChain(
                                    buildHatchAdder(YottaFluidTank.class)
                                            .atLeast(GT_HatchElement.InputHatch)
                                            .adder(YottaFluidTank::addInput)
                                            .casingIndex(1537)
                                            .dot(1)
                                            .build(),
                                    ofBlock(Loaders.yottaFluidTankCasing, 0)))
                    .addElement(
                            'M',
                            ofChain(
                                    buildHatchAdder(YottaFluidTank.class)
                                            .atLeast(GT_HatchElement.Maintenance)
                                            .casingIndex(1537)
                                            .dot(2)
                                            .build(),
                                    ofBlock(Loaders.yottaFluidTankCasing, 0)))
                    .addElement(
                            'O',
                            ofChain(
                                    buildHatchAdder(YottaFluidTank.class)
                                            .atLeast(GT_HatchElement.OutputHatch)
                                            .adder(YottaFluidTank::addOutput)
                                            .casingIndex(1537)
                                            .dot(1)
                                            .build(),
                                    ofBlock(Loaders.yottaFluidTankCasing, 0)))
                    .build();
        }
        return multiDefinition;
    }

    public List<IStructureElement<YottaFluidTank>> cells(int num) {
        List<IStructureElement<YottaFluidTank>> out = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            int finalI = i;
            out.add(onElementPass(
                    x -> {
                        x.mStorage = x.mStorage.add(calStorage(finalI));
                        x.maxCell = Math.max(x.maxCell, finalI);
                    },
                    ofBlock(Loaders.yottaFluidTankCell, i)));
        }
        return out;
    }

    public final boolean addInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
                }
            }
        }
        return false;
    }

    public final boolean addOutput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof YOTTAHatch) {
                    // only one yothatch allowed
                    if (!this.mYottaHatch.isEmpty()) return false;
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    ((YOTTAHatch) aMetaTileEntity).setTank(this);
                    return this.mYottaHatch.add((YOTTAHatch) aMetaTileEntity);
                }
            }
        }
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("scanner.info.YOTTank.0"),
            StatCollector.translateToLocal(
                    EnumChatFormatting.GREEN + CharExchanger.formatNumber(getCap()) + EnumChatFormatting.RESET + " L"),
            StatCollector.translateToLocal("scanner.info.YOTTank.1"),
            StatCollector.translateToLocal(
                    EnumChatFormatting.YELLOW + CharExchanger.formatNumber(getFluidName()) + EnumChatFormatting.RESET),
            StatCollector.translateToLocal("scanner.info.YOTTank.2"),
            StatCollector.translateToLocal(
                    EnumChatFormatting.BLUE + CharExchanger.formatNumber(getStored()) + EnumChatFormatting.RESET + " L")
        };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fluid Tank")
                .addInfo("Controller block for the YOTTank.")
                .addInfo(
                        "The max output speed is decided by the amount of stored liquid and the output hatch's capacity.")
                .addInfo("The max fluid cell tier is limited by the glass tier.")
                .addInfo("HV glass for T1, EV glass for T2, IV glass for T3. . .")
                .addInfo("The max height of the cell blocks is 15.")
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .beginVariableStructureBlock(5, 5, 1, 15, 5, 5, false)
                .addController("Front of the second layer")
                .addInputHatch("Hint block with dot 1")
                .addMaintenanceHatch("Hint block with dot 2")
                .addOutputHatch("Hint block with dot 3")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    public BigInteger calStorage(int meta) {
        StringBuilder cap = new StringBuilder();
        cap.append("1000000");
        for (int i = 0; i < meta; ++i) cap.append("00");
        return new BigInteger(cap.toString());
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new YOTTankGUIClient(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png");
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new YOTTankGUIContainer(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        super.onRunningTick(aStack);
        if (this.getBaseMetaTileEntity().isServerSide()) {
            List<FluidStack> tStore = getStoredFluids();
            for (FluidStack tFluid : tStore) {
                if (tFluid == null) continue;
                if (mFluidName == null
                        || mFluidName.equals("")
                        || tFluid.getFluid().getName().equals(mFluidName)) {
                    if (mFluidName == null || mFluidName.equals("")) {
                        mFluidName = tFluid.getFluid().getName();
                    }
                    if (mStorageCurrent.add(new BigInteger(tFluid.amount + "")).compareTo(mStorage) < 0) {
                        mStorageCurrent = mStorageCurrent.add(new BigInteger(tFluid.amount + ""));
                        tFluid.amount = 0;
                    } else {
                        BigInteger delta = mStorage.subtract(mStorageCurrent);
                        mStorageCurrent = mStorageCurrent.add(delta);
                        tFluid.amount -= delta.intValue();
                    }
                }
            }
            BigInteger outputAmount = mStorageCurrent.divide(new BigInteger("100", 10));
            if (outputAmount.compareTo(new BigInteger(Integer.MAX_VALUE + "", 10)) > 0)
                outputAmount = new BigInteger(Integer.MAX_VALUE + "");
            if (outputAmount.compareTo(BigInteger.ONE) <= 0) outputAmount = new BigInteger("1", 10);

            if (mStorageCurrent.compareTo(BigInteger.ZERO) <= 0) mFluidName = "";

            if (mFluidName != null && !mFluidName.equals("")) {
                for (GT_MetaTileEntity_Hatch outputHatch : mOutputHatches) {
                    FluidStack tHatchFluid = outputHatch.mFluid;
                    FluidStack tOutput = FluidRegistry.getFluidStack(mFluidName, outputAmount.intValue());
                    if (tHatchFluid != null && tHatchFluid.isFluidEqual(tOutput)) {
                        int leftSpace = outputHatch.getCapacity() - tHatchFluid.amount;
                        if (leftSpace < tOutput.amount) {
                            if (reduceFluid(leftSpace)) tHatchFluid.amount += leftSpace;
                        } else {
                            if (reduceFluid(tOutput.amount)) tHatchFluid.amount += tOutput.amount;
                        }
                    } else if (tHatchFluid == null) {
                        int leftSpace = outputHatch.getCapacity();
                        if (leftSpace < tOutput.amount) {
                            if (reduceFluid(leftSpace))
                                outputHatch.fill(FluidRegistry.getFluidStack(mFluidName, leftSpace), true);
                        } else {
                            if (reduceFluid(tOutput.amount)) outputHatch.fill(tOutput, true);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(YOTTANK_BOTTOM, 2, 0, 0, stackSize, hintsOnly);
        int height = stackSize.stackSize;
        if (height > 15) height = 15;
        structureBuild_EM(YOTTANK_TOP, 2, height + 2, 0, stackSize, hintsOnly);
        while (height > 0) {
            structureBuild_EM(YOTTANK_MID, 2, height, 0, stackSize, hintsOnly);
            height--;
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("YOTTank.hint", 8);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new YottaFluidTank(this.mName);
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1537),
                    TextureFactory.of(textureFontOn),
                    TextureFactory.builder().addIcon(textureFontOn_Glow).glow().build()
                };
            else
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1537),
                    TextureFactory.of(textureFontOff),
                    TextureFactory.builder().addIcon(textureFontOff_Glow).glow().build()
                };
        } else return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(1537)};
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int built = 0;
        built += survivialBuildPiece(YOTTANK_BOTTOM, stackSize, 2, 0, 0, elementBudget, source, actor, false, true);
        int height = stackSize.stackSize;
        if (height > 15) height = 15;
        built += survivialBuildPiece(
                YOTTANK_TOP, stackSize, 2, height + 2, 0, elementBudget - built, source, actor, false, true);
        while (height > 0) {
            built += survivialBuildPiece(
                    YOTTANK_MID, stackSize, 2, height, 0, elementBudget - built, source, actor, false, true);
            height--;
        }
        return built;
    }
}
