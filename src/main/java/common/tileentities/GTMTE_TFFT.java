package common.tileentities;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static java.lang.Math.min;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;
import common.Blocks;

import gregtech.api.enums.Textures;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.items.GT_IntegratedCircuit_Item;

public class GTMTE_TFFT extends GT_MetaTileEntity_EnhancedMultiBlockBase<GTMTE_TFFT> implements ISurvivalConstructable {

    public enum Field {

        T1(1_000_000L, 1), // LV
        T2(4_000_000L, 2), // MV
        T3(16_000_000L, 5), // HV
        T4(64_000_000L, 14), // EV
        T5(256_000_000L, 42), // IV

        T6(2_048_000_000L, 132), // LuV
        T7(131_072_000_000L, 429), // UV
        T8(8_388_608_000_000L, 1430), // UEV
        T9(536_870_912_000_000L, 4862), // UMV

        T10(1_099_511_627_776_000_000L, 0); // UXV

        public static final GTMTE_TFFT.Field[] VALUES = values();
        private final long capacity;
        private final int cost;

        Field(long capacity, int cost) {
            this.capacity = capacity;
            this.cost = cost;
        }

        public long getCapacity() {
            return capacity;
        }

        public int getCost() {
            return cost;
        }
    }

    private enum TFFTMultiHatch implements IHatchElement<GTMTE_TFFT> {

        INSTANCE;

        private final List<? extends Class<? extends IMetaTileEntity>> mteClasses;

        @SafeVarargs
        TFFTMultiHatch(Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Arrays.asList(mteClasses);
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGT_HatchAdder<? super GTMTE_TFFT> adder() {
            return GTMTE_TFFT::addMultiHatchToMachineList;
        }

        @Override
        public long count(GTMTE_TFFT t) {
            return t.tfftHatch == null ? 0 : 1;
        }
    }

    private enum TFFTStorageFieldElement implements IStructureElement<GTMTE_TFFT> {

        INSTANCE;

        @Override
        public boolean check(GTMTE_TFFT t, World world, int x, int y, int z) {
            Block worldBlock = world.getBlock(x, y, z);
            int meta = worldBlock.getDamageValue(world, x, y, z);
            if (TFFT_FIELD != worldBlock || meta == 0) return false;
            t.FIELDS[meta - 1]++;
            return true;
        }

        private int getHint(ItemStack stack) {
            return Math.min(Field.VALUES.length, ChannelDataAccessor.getChannelData(stack, "field"));
        }

        @Override
        public boolean spawnHint(GTMTE_TFFT t, World world, int x, int y, int z, ItemStack trigger) {
            StructureLibAPI.hintParticle(world, x, y, z, TFFT_FIELD, getHint(trigger));
            return true;
        }

        @Override
        public boolean placeBlock(GTMTE_TFFT t, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlock(x, y, z, TFFT_FIELD, getHint(trigger), 3);
            return true;
        }

        @Override
        public PlaceResult survivalPlaceBlock(GTMTE_TFFT t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
            if (check(t, world, x, y, z)) return PlaceResult.SKIP;
            int fieldTier = getHint(trigger);
            ItemStack result = env.getSource().takeOne(
                    s -> s != null && s.stackSize >= 0
                            && s.getItem() == TFFT_FIELD_ITEM
                            && s.getItemDamage() != CASING_META
                            && s.getItemDamage() <= fieldTier,
                    true);
            if (result == null) return PlaceResult.REJECT;

            return StructureUtility.survivalPlaceBlock(
                    result,
                    ItemStackPredicate.NBTMode.EXACT,
                    null,
                    true,
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
        }
    }

    private static final IIconContainer TEXTURE_TFFT = new Textures.BlockIcons.CustomIcon("iconsets/TFFT");
    private static final IIconContainer TEXTURE_TFFT_ACTIVE = new Textures.BlockIcons.CustomIcon(
            "iconsets/TFFT_ACTIVE");
    private static final IIconContainer TEXTURE_TFFT_ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
            "iconsets/TFFT_ACTIVE_GLOW");
    private static final int CASING_TEXTURE_ID_1 = (12 << 7) | 127;
    private static final int CASING_TEXTURE_ID_2 = 176;

    private static final Block TFFT_FIELD = Blocks.tfftStorageField;
    private static final Item TFFT_FIELD_ITEM = Item.getItemFromBlock(TFFT_FIELD);
    public static final int MAX_DISTINCT_FLUIDS = 25;
    private static final BigInteger MAX_CAPACITY = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(25));
    private static final int CASING_META = 0;
    private static final int MIN_CASING_AMOUNT = 20;
    private static final int MAX_LAYER_AMOUNT = 13;
    private static final int DEFAULT_LAYER_AMOUNT = 3;

    private static final String STRUCTURE_PIECE_TOP = "top";
    private static final String STRUCTURE_PIECE_MID = "mid";
    private static final String STRUCTURE_PIECE_BOTTOM = "bottom";

    // height channel for height
    // field channel for field
    private static final IStructureDefinition<GTMTE_TFFT> STRUCTURE_DEFINITION = IStructureDefinition
            .<GTMTE_TFFT>builder()
            .addShape(
                    STRUCTURE_PIECE_TOP,
                    transpose(new String[][] { { "ccccc" }, { "cCCCc" }, { "cC~Cc" }, { "cCCCc" }, { "ccccc" } }))
            .addShape(
                    STRUCTURE_PIECE_MID,
                    transpose(new String[][] { { "gGGGg" }, { "GfffG" }, { "GfffG" }, { "GfffG" }, { "gGGGg" } }))
            .addShape(
                    STRUCTURE_PIECE_BOTTOM,
                    transpose(new String[][] { { "ccccc" }, { "cCCCc" }, { "cCCCc" }, { "cCCCc" }, { "ccccc" } }))
            .addElement(
                    'c',
                    buildHatchAdder(GTMTE_TFFT.class).atLeast(Energy, Maintenance).casingIndex(CASING_TEXTURE_ID_1)
                            .dot(1)
                            .buildAndChain(onElementPass(te -> te.casingAmount++, ofBlock(TFFT_FIELD, CASING_META))))
            .addElement(
                    'C',
                    buildHatchAdder(GTMTE_TFFT.class).casingIndex(CASING_TEXTURE_ID_1)
                            .atLeast(
                                    Energy,
                                    Maintenance,
                                    InputHatch.or(TFFTMultiHatch.INSTANCE),
                                    OutputHatch.or(TFFTMultiHatch.INSTANCE))
                            .dot(2)
                            .buildAndChain(onElementPass(te -> te.casingAmount++, ofBlock(TFFT_FIELD, CASING_META))))
            .addElement(
                    'G',
                    buildHatchAdder(GTMTE_TFFT.class)
                            .atLeast(InputHatch.or(TFFTMultiHatch.INSTANCE), OutputHatch.or(TFFTMultiHatch.INSTANCE))
                            .casingIndex(CASING_TEXTURE_ID_2).dot(3).buildAndChain(
                                    ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true),
                                    ofBlockUnlocalizedName("Thaumcraft", "blockCosmeticOpaque", 2, false),
                                    BorosilicateGlass.ofBoroGlassAnyTier()))
            .addElement(
                    'g',
                    ofChain(
                            ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true),
                            ofBlockUnlocalizedName("Thaumcraft", "blockCosmeticOpaque", 2, false),
                            BorosilicateGlass.ofBoroGlassAnyTier()))
            .addElement('f', ofChain(TFFTStorageFieldElement.INSTANCE)).build();

    public final FluidTankGT[] STORE = new FluidTankGT[MAX_DISTINCT_FLUIDS];

    {
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            STORE[i] = new FluidTankGT(0);
        }
    }

    private final int[] FIELDS = new int[Field.VALUES.length];

    private BigInteger capacity = BigInteger.ZERO;
    private long capacityPerFluid = 0L;
    private int casingAmount = 0;
    private int runningCost = 0;

    private boolean locked = true;
    private boolean doVoidExcess = false;
    private byte fluidSelector = -1;

    private GTMTE_TFFTHatch tfftHatch = null;

    public GTMTE_TFFT(String aName) {
        super(aName);
    }

    public GTMTE_TFFT(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IStructureDefinition<GTMTE_TFFT> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTMTE_TFFT(super.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID_1),
                    TextureFactory.builder().addIcon(TEXTURE_TFFT_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(TEXTURE_TFFT_ACTIVE_GLOW).extFacing().glow().build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID_1),
                    TextureFactory.builder().addIcon(TEXTURE_TFFT).extFacing().build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID_1) };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fluid Tank").addInfo("High-Tech fluid tank that can hold up to 25 different fluids!")
                .addInfo("Has 1/25th of the total capacity as capacity for each fluid.")
                .addInfo("Right clicking the controller with a screwdriver will turn on excess voiding.")
                .addInfo("Fluid storage amount and running cost depends on the storage field blocks used.")
                .addSeparator().addInfo("Note on hatch locking:")
                .addInfo("Use an Integrated Circuit in the GUI slot to limit which fluid is output.")
                .addInfo("The index of a stored fluid can be obtained through the Tricorder.").addSeparator()
                .beginVariableStructureBlock(5, 5, 5, 15, 5, 5, false).addController("Top Center")
                .addCasingInfo("T.F.F.T Casing", MIN_CASING_AMOUNT)
                .addOtherStructurePart("Storage Field Blocks (Tier I-X)", "Inner 3xhx3 solid pillar")
                .addStructureInfo("Energy hatch is not required when running cost is 0")
                .addOtherStructurePart(
                        "Borosilicate Glass(any)/Warded Glass/Reinforced Glass",
                        "Outer 5xhx5 glass shell")
                .addMaintenanceHatch("Any top or bottom casing").addEnergyHatch("Any top or bottom casing")
                .addInputHatch("Instead of any casing or glass, has to touch storage field block")
                .addOutputHatch("Instead of any casing or glass, has to touch storage field block")
                .addStructureInfo("You can have a bunch of hatches")
                .addOtherStructurePart(
                        "Multi I/O Hatches",
                        "Instead of any casing or glass, has to touch storage field block")
                .addStructureInfo("Use MIOH with conduits or fluid storage busses to see all fluids at once.")
                .addSubChannelUsage("field", "Maximum Field Tier").addSubChannelUsage("height", "Height of structure")
                .toolTipFinisher("KekzTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int layer = min(stackSize.stackSize + DEFAULT_LAYER_AMOUNT, MAX_LAYER_AMOUNT + 1);
        buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, 2, 2, 0);
        for (int i = -1; i >= 1 - layer; i--) buildPiece(STRUCTURE_PIECE_MID, stackSize, hintsOnly, 2, 2, i);
        buildPiece(STRUCTURE_PIECE_BOTTOM, stackSize, hintsOnly, 2, 2, -layer);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivialBuildPiece(STRUCTURE_PIECE_TOP, stackSize, 2, 2, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        int layer = min(stackSize.stackSize + DEFAULT_LAYER_AMOUNT, MAX_LAYER_AMOUNT + 1);
        for (int i = -1; i >= 1 - layer; i--) {
            build = survivialBuildPiece(STRUCTURE_PIECE_MID, stackSize, 2, 2, i, elementBudget, env, false, true);
            if (build >= 0) return build;
        }
        return survivialBuildPiece(STRUCTURE_PIECE_BOTTOM, stackSize, 2, 2, -layer, elementBudget, env, false, true);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        if (tfftHatch != null) {
            tfftHatch.unbind();
            tfftHatch = null;
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        Arrays.fill(FIELDS, 0);

        this.capacity = BigInteger.ZERO;
        this.capacityPerFluid = 0L;
        this.casingAmount = 0;
        this.runningCost = 0;

        if (!checkPiece(STRUCTURE_PIECE_TOP, 2, 2, 0)) return false;

        int layer = 1;
        while (checkPiece(STRUCTURE_PIECE_MID, 2, 2, -layer)) layer++;
        if (layer - 1 > MAX_LAYER_AMOUNT || layer - 1 < DEFAULT_LAYER_AMOUNT) return false;
        if (!checkPiece(STRUCTURE_PIECE_BOTTOM, 2, 2, -layer)) return false;
        if (casingAmount >= MIN_CASING_AMOUNT
                && (tfftHatch != null || (!mInputHatches.isEmpty() && !mOutputHatches.isEmpty()))
                && mInputHatches.size() + mOutputHatches.size() <= MAX_DISTINCT_FLUIDS * 2
                && mMaintenanceHatches.size() == 1) {
            BigInteger tempCap = BigInteger.ZERO;
            for (int i = 0; i < this.FIELDS.length; i++) {
                tempCap = tempCap.add(
                        BigInteger.valueOf(Field.VALUES[i].getCapacity()).multiply(BigInteger.valueOf(this.FIELDS[i])));
                this.runningCost += Field.VALUES[i].getCost() * this.FIELDS[i];
            }
            this.setCapacity(tempCap);

            if (tfftHatch != null) tfftHatch.bind(this);

            return !mEnergyHatches.isEmpty() ^ this.runningCost == 0;
        }
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        mEfficiency = getCurrentEfficiency(null);
        mEfficiencyIncrease = 10000;
        mEUt = this.runningCost;
        mMaxProgresstime = 20;

        this.fluidSelector = (itemStack != null && itemStack.getItem() instanceof GT_IntegratedCircuit_Item)
                ? (byte) itemStack.getItemDamage()
                : -1;

        // Suck in fluids
        final ArrayList<FluidStack> inputFluids = getStoredFluids();

        if (!inputFluids.isEmpty()) {
            for (FluidStack aFluid : inputFluids) {
                final FluidStack toDeplete = aFluid.copy();
                toDeplete.amount = this.pull(aFluid, true);
                depleteInput(toDeplete);
            }
        }

        // Push out fluids
        if (!this.mOutputHatches.isEmpty()) {
            final FluidTankGT sFluid = this.getSelectedFluid();
            boolean isFluidSelected = this.fluidSelector != -1;

            if (!isFluidSelected || !sFluid.isEmpty()) {
                for (GT_MetaTileEntity_Hatch_Output tHatch : this.mOutputHatches) {
                    int hatchCapacity = tHatch.getCapacity();
                    int hatchAmount = tHatch.getFluidAmount();
                    int remaining = hatchCapacity - hatchAmount;

                    if (remaining <= 0) continue;

                    final FluidStack tFluid = tHatch.getFluid();

                    String lockedFluidName = tHatch.getLockedFluidName() == null ? "" : tHatch.getLockedFluidName();
                    String tFluidName = tFluid == null ? "" : tFluid.getFluid().getName();

                    boolean isFluidLocked = tHatch.isFluidLocked();
                    boolean isFluidEmpty = tFluid == null || tHatch.getFluidAmount() == 0;

                    if (isFluidLocked && !this.contains(lockedFluidName)) continue;
                    if (!isFluidEmpty && !this.contains(tFluid)) continue;
                    if ((isFluidLocked && !isFluidEmpty) && !lockedFluidName.equals(tFluidName)) continue;

                    if (isFluidSelected) {
                        if (isFluidLocked && !lockedFluidName.equals(sFluid.name())) continue;
                        if (!isFluidEmpty && !sFluid.contains(tFluid)) continue;

                        tHatch.fill(this.push(sFluid.get(remaining), true), true);
                    } else if (isFluidLocked) {
                        if (!isFluidEmpty && !lockedFluidName.equals(tFluid.getFluid().getName())) continue;

                        FluidStack aFluid = FluidRegistry.getFluidStack(lockedFluidName, remaining);
                        tHatch.fill(this.push(aFluid, true), true);
                    } else if (isFluidEmpty) {
                        if (this.firstNotNull() != null) tHatch.fill(this.push(hatchCapacity, true), true);
                    } else {
                        tHatch.fill(this.push(new FluidStack(tFluid, remaining), true), true);
                    }
                }
            }
        }

        if (this.mEUt > 0) this.mEUt = -this.mEUt;

        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) this.locked = !aBaseMetaTileEntity.isActive();
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> ll = new ArrayList<>();
        NumberFormat nf = NumberFormat.getNumberInstance();

        ll.add(EnumChatFormatting.YELLOW + "Stored Fluids:" + EnumChatFormatting.RESET);
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            FluidTankGT tank = STORE[i];
            if (tank.isEmpty()) {
                ll.add(MessageFormat.format("{0} - {1}: {2}L ({3}%)", i, "NULL", 0, 0));
            } else {
                String localizedName = STORE[i].get().getLocalizedName();
                String amount = nf.format(STORE[i].amount());
                String percentage = capacityPerFluid > 0 ? String.valueOf(STORE[i].amount() * 100 / capacityPerFluid)
                        : "";

                ll.add(MessageFormat.format("{0} - {1}: {2}L ({3}%)", i, localizedName, amount, percentage));
            }
        }
        ll.add(EnumChatFormatting.YELLOW + "Operational Data:" + EnumChatFormatting.RESET);
        ll.add("Used Capacity: " + nf.format(getStoredAmount()) + "L");
        ll.add("Total Capacity: " + nf.format(capacity) + "L");
        ll.add("Per-Fluid Capacity: " + nf.format(capacityPerFluid) + "L");
        ll.add("Running Cost: " + getActualEnergyUsage() + "EU/t");
        ll.add("Auto-voiding: " + doVoidExcess);
        ll.add(
                "Maintenance Status: " + ((getRepairStatus() == getIdealStatus())
                        ? EnumChatFormatting.GREEN + "Working perfectly" + EnumChatFormatting.RESET
                        : EnumChatFormatting.RED + "Has Problems" + EnumChatFormatting.RESET));
        ll.add("---------------------------------------------");

        return ll.toArray(new String[0]);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByteArray("capacity", capacity.toByteArray());
        aNBT.setBoolean("doVoidExcess", doVoidExcess);
        aNBT.setInteger("runningCost", runningCost);
        aNBT.setBoolean("lockFluid", locked);
        aNBT.setByte("fluidSelector", fluidSelector);

        NBTTagCompound fluidNBT = new NBTTagCompound();
        aNBT.setTag("STORE", fluidNBT);

        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            STORE[i].writeToNBT(fluidNBT, String.valueOf(i));
        }

        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.setCapacity(new BigInteger(aNBT.getByteArray("capacity")));
        this.setDoVoidExcess(aNBT.getBoolean("doVoidExcess"));
        this.runningCost = aNBT.getInteger("runningCost");
        this.locked = aNBT.getBoolean("lockFluid");
        this.fluidSelector = aNBT.getByte("fluidSelector");

        NBTTagCompound fluidNBT = (NBTTagCompound) aNBT.getTag("STORE");
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            STORE[i].readFromNBT(fluidNBT, String.valueOf(i));
        }

        super.loadNBTData(aNBT);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack stack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        this.setDoVoidExcess(!doVoidExcess);
        GT_Utility.sendChatToPlayer(aPlayer, "Auto-voiding " + (this.doVoidExcess ? "enabled" : "disabled"));
    }

    private boolean addMultiHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GTMTE_TFFTHatch) {
                if (this.tfftHatch != null) return false;
                this.tfftHatch = (GTMTE_TFFTHatch) aMetaTileEntity;
                this.tfftHatch.updateTexture(aBaseCasingIndex);
                return true;
            }
        }
        return false;
    }

    public int pull(FluidStack aFluid, boolean doPull) {
        if (locked) return 0;
        int index = getFluidPosition(aFluid);
        if (index >= 0) {
            return STORE[index].fill(aFluid, doPull);
        } else if (fluidCount() < MAX_DISTINCT_FLUIDS) {
            return STORE[getNullSlot()].setCapacity(capacityPerFluid).fill(aFluid, doPull);
        }
        return 0;
    }

    public long pull(FluidStack aFluid, long amount, boolean doPull) {
        if (locked) return 0;
        int index = getFluidPosition(aFluid);
        if (index >= 0) {
            FluidTankGT tank = STORE[index];
            if (doPull) return tank.add(amount);
            return doVoidExcess ? amount
                    : tank.amount() + amount > tank.capacity() ? tank.capacity() - tank.amount() : amount;
        } else if (fluidCount() < MAX_DISTINCT_FLUIDS) {
            FluidTankGT tank = STORE[getNullSlot()];
            if (doPull) return tank.add(amount, aFluid);
            return doVoidExcess ? amount : Math.min(amount, tank.capacity());
        }
        return 0;
    }

    public FluidStack push(FluidStack aFluid, boolean doPush) {
        if (locked) return null;
        int index = getFluidPosition(aFluid);
        if (index < 0) return null;
        return STORE[index].drain(aFluid.amount, doPush);
    }

    public FluidStack push(int amount, boolean doPush) {
        if (locked) return null;
        int index = firstNotNullSlot();
        if (index < 0) return null;
        return STORE[index].drain(amount, doPush);
    }

    public long push(FluidStack aFluid, long amount, boolean doPush) {
        if (locked) return 0;
        int index = getFluidPosition(aFluid);
        if (index < 0) return 0;
        if (doPush) return STORE[index].remove(amount);
        return STORE[index].amount(amount);
    }

    public long getCapacityPerFluid() {
        return this.capacityPerFluid;
    }

    public void setCapacity(BigInteger capacity) {
        if (capacity.compareTo(MAX_CAPACITY) > 0) {
            this.capacity = MAX_CAPACITY;
            this.capacityPerFluid = Long.MAX_VALUE;
        } else {
            this.capacity = capacity;
            this.capacityPerFluid = capacity.divide(BigInteger.valueOf(MAX_DISTINCT_FLUIDS)).longValue();
        }

        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            FluidTankGT tank = STORE[i];
            if (tank.setCapacity(capacityPerFluid).amount() > capacityPerFluid) {
                STORE[i] = new FluidTankGT(tank.get(), capacityPerFluid, capacityPerFluid);
            }
        }
    }

    public int fluidCount() {
        int tCount = 0;
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            if (!STORE[i].isEmpty()) tCount++;
        }
        return tCount;
    }

    public int getFluidPosition(String fluidName) {
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            if (!STORE[i].isEmpty() && STORE[i].name().equals(fluidName)) return i;
        }
        return -1;
    }

    public int getFluidPosition(FluidStack aFluid) {
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            if (STORE[i].contains(aFluid)) return i;
        }
        return -1;
    }

    public int getNullSlot() {
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            if (STORE[i].isEmpty()) return i;
        }
        return -1;
    }

    public boolean contains(String fluidName) {
        return getFluidPosition(fluidName) >= 0;
    }

    public boolean contains(FluidStack aFluid) {
        return getFluidPosition(aFluid) >= 0;
    }

    public int firstNotNullSlot() {
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            if (!STORE[i].isEmpty()) return i;
        }
        return -1;
    }

    public FluidTankGT firstNotNull() {
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            if (!STORE[i].isEmpty()) return STORE[i];
        }
        return null;
    }

    public BigInteger getStoredAmount() {
        BigInteger amount = BigInteger.ZERO;
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            amount = amount.add(BigInteger.valueOf(STORE[i].amount()));
        }
        return amount;
    }

    public byte getFluidSelector() {
        return fluidSelector;
    }

    public FluidTankGT getSelectedFluid() {
        return fluidSelector != -1 ? STORE[fluidSelector] : null;
    }

    public void setDoVoidExcess(boolean doVoidExcess) {
        this.doVoidExcess = doVoidExcess;
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            STORE[i].setVoidExcess(doVoidExcess);
        }
    }

    public FluidTankInfo[] getTankInfo() {
        FluidTankInfo[] info = new FluidTankInfo[MAX_DISTINCT_FLUIDS];
        for (int i = 0; i < MAX_DISTINCT_FLUIDS; i++) {
            STORE[i].getFluid(); //
            info[i] = STORE[i].getInfo();
        }
        return info;
    }
}
