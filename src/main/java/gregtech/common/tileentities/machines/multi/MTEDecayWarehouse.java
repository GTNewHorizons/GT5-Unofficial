package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RED;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.UNDERLINE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.YELLOW;
import static gregtech.api.casing.Casings.RadiationProofMachineCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTUtility.areStacksEqual;
import static gregtech.api.util.GTUtility.formatNumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.casing.Casings;
import gregtech.api.casing.ICasing;
import gregtech.api.casing.ICasingGroup;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.IStructureInstance;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.ISuperChestAcceptor;
import gregtech.api.structure.StructureWrapper;
import gregtech.api.structure.StructureWrapperInstanceInfo;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

public class MTEDecayWarehouse extends MTEExtendedPowerMultiBlockBase<MTEDecayWarehouse>
    implements ISurvivalConstructable, IStructureProvider<MTEDecayWarehouse>, ISuperChestAcceptor {

    private static final int MODE_NORMAL = 0, MODE_EXPORT = 1;
    public static final double EPSILON = 0.00001;
    public static final long EU_PER_IO = 512L;
    public static final int CAPACITY_DIVISOR = 400;

    protected final StructureWrapper<MTEDecayWarehouse> structure;
    protected final StructureWrapperInstanceInfo<MTEDecayWarehouse> structureInstanceInfo;

    private ItemStack isotope, product;
    private double storedIsotope, storedProduct, decayRate;
    private GTRecipe currentRecipe;

    private final ArrayList<ItemStack> pendingInputs = new ArrayList<>();

    private MTEDigitalChestBase superChest;

    public MTEDecayWarehouse(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;

        structure.loadStructure();
    }

    protected MTEDecayWarehouse(MTEDecayWarehouse prototype) {
        super(prototype.mName);

        structure = prototype.structure;
        structureInstanceInfo = new StructureWrapperInstanceInfo<>(structure);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEDecayWarehouse(this);
    }

    @Override
    public String[][] getDefinition() {
        // spotless:off
        return new String[][]{{
            "BBBBB",
            "BB~BB",
            "BBBBB"
        },{
            "BCCCB",
            "BCCCB",
            "BBBBB"
        },{
            "BCCCB",
            "BCACB",
            "BBBBB"
        },{
            "BCCCB",
            "BCCCB",
            "BBBBB"
        },{
            "BBBBB",
            "BBBBB",
            "BBBBB"
        }};
        // spotless:on
    }

    @Override
    public IStructureDefinition<MTEDecayWarehouse> compile(String[][] def) {
        structure.addCasing('A', Casings.SuperChest)
            .withChannel(GTStructureChannels.SUPER_CHEST);
        structure.addCasing('B', RadiationProofMachineCasing)
            .withHatches(1, 8, Arrays.asList(Maintenance, Energy, InputBus, OutputBus));
        structure.addCasing('C', ICasing.ofBlock(new BlockMeta(Blocks.water, 0)))
            .wrapElement(GTStructureUtility::noSurvivalAutoplace);

        return structure.buildStructure(def);
    }

    @Override
    public boolean onSuperChestAdded(ICasingGroup group, MTEDigitalChestBase chest, int tier) {
        superChest = chest;

        return true;
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        structureInstanceInfo.validate(errors, context);
    }

    @Override
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        structureInstanceInfo.localizeStructureErrors(errors, context, lines);
    }

    @Override
    public IStructureInstance<MTEDecayWarehouse> getStructureInstance() {
        return structureInstanceInfo;
    }

    @Override
    public IStructureDefinition<MTEDecayWarehouse> getStructureDefinition() {
        return structure.structureDefinition;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        structureInstanceInfo.clearHatches();
        superChest = null;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        structure.construct(this, trigger, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = structure.survivalConstruct(this, trigger, elementBudget, env);

        if (built == -1) {
            GTUtility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done! Now go place the water yourself!");
        }

        return built;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structure.checkStructure(this);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEDecayWarehouse> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("Decay Warehouse")
            .addInfo("Stores a single type of radioactive isotope and allows it to decay over time")
            .addInfo("Decay speed is dependent on the isotopes' half-lives (lower is faster)")
            .addInfo("Isotopes decay regardless of whether the warehouse is on or powered")
            .addSeparator()
            .addInfo(
                "The warehouse's capacity equals the super chest's capacity divided by " + BLUE
                    + CAPACITY_DIVISOR
                    + GRAY
                    + ".")
            .addInfo("The warehouse will pull in up to " + BLUE + "N / " + EU_PER_IO + GRAY + " items per second,")
            .addInfo("where " + BLUE + "N" + GRAY + " is the warehouse's EU input (standard energy hatch rules)")
            .addSeparator()
            .addInfo("Right click the controller with a screwdriver to dump stored isotopes into the output bus")
            .addInfo("Right click the controller with a plunger to empty it")
            .addInfo(
                "The warehouse's contents are " + RED + UNDERLINE + "voided" + GRAY + " when the controller is broken");

        tt.addSubChannelUsage(GTStructureChannels.SUPER_CHEST);

        tt.beginStructureBlock(true)
            .addAllCasingInfo();

        tt.toolTipFinisher();

        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10_000;
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
    public ITexture[] getTexture(IGregTechTileEntity igte, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstoneLevel) {
        List<ITexture> textures = new ArrayList<>();

        textures.add(RadiationProofMachineCasing.getCasingTexture());

        if (side == facing) {
            textures.add(
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.DECAY_WAREHOUSE_BACKGROUND)
                    .extFacing()
                    .build());

            if (active) {
                textures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.DECAY_WAREHOUSE_GLOW)
                        .extFacing()
                        .glow()
                        .build());
            }
        }

        return textures.toArray(new ITexture[0]);
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER);
    }

    @Override
    public String getMachineModeName() {
        return machineMode == MODE_NORMAL ? "Normal" : "Exporting";
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.isotopeDecay;
    }

    @Override
    public VoidingMode getDefaultVoidingMode() {
        return VoidingMode.VOID_NONE;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return VoidingMode.VOID_NONE;
    }

    @Override
    public void setVoidingMode(VoidingMode mode) {

    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isServerSide()) return;

        if (!mMachine) {
            decayRate = 0;
            return;
        }

        if (mStartUpCheck > 0) return;
        if (aTick % 20 != 0) return;

        decayRate = 0;

        if (machineMode == MODE_NORMAL && currentRecipe != null) {
            double halfLife = currentRecipe.getMetadataOrDefault(GTRecipeConstants.HALF_LIFE, 0d);

            double ratio = Math.exp(-Math.log(2) / halfLife);

            double consumed = storedIsotope * (1d - ratio);
            decayRate = consumed;

            storedIsotope -= consumed;
            storedProduct += consumed;
        }

        if (machineMode == MODE_EXPORT) {
            if (storedIsotope < EPSILON) {
                isotope = null;
                storedIsotope = 0;
            }

            if (storedProduct < EPSILON) {
                product = null;
                storedProduct = 0;
            }
        }
    }

    @Override
    protected void outputAfterRecipe() {
        for (ItemStack pendingInput : pendingInputs) {
            if (GTUtility.areStacksEqual(isotope, pendingInput, true)) {
                storedIsotope += pendingInput.stackSize;
            }
        }

        pendingInputs.clear();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity base, EntityPlayer player, ForgeDirection side, float aX, float aY,
        float aZ) {
        ItemStack tCurrentItem = player.inventory.getCurrentItem();
        if (tCurrentItem != null && tCurrentItem.getItem() instanceof MetaGeneratedTool) {
            int[] aOreID = OreDictionary.getOreIDs(tCurrentItem);
            for (int id : aOreID) {
                if (OreDictionary.getOreName(id)
                    .equals("craftingToolPlunger")) {
                    if (base.isServerSide()) {
                        isotope = null;
                        storedIsotope = 0;
                        product = null;
                        storedProduct = 0;
                        decayRate = 0;
                        currentRecipe = null;
                    }

                    GTUtility.sendSoundToPlayers(
                        player.worldObj,
                        SoundResource.GTCEU_OP_PLUNGER,
                        1.0F,
                        -1.0F,
                        base.getXCoord() + .5,
                        base.getYCoord() + .5,
                        base.getZCoord() + .5);

                    return true;
                }
            }
        }

        return super.onRightclick(base, player, side, aX, aY, aZ);
    }

    private int getWarehouseCapacity() {
        return superChest == null ? 0 : (superChest.getItemCapacity() / CAPACITY_DIVISOR);
    }

    private int getAvailableCapacity() {
        return MathHelper.floor_double(getWarehouseCapacity() - storedIsotope - storedProduct);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        long availableEUt = mEnergyHatches.size() > 1 ? getMaxInputPower() : getMaxInputVoltage();

        availableEUt = (long) (availableEUt * 0.95);

        int remainingIOQuota = GTUtility.safeInt(availableEUt / EU_PER_IO, 0);

        mMaxProgresstime = 20;
        mEfficiency = 10_000;
        lEUt = 0;

        List<ItemStack> outputs = new ArrayList<>();

        int productAmount = MathHelper.floor_double(storedProduct + EPSILON);

        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this);

        // eject any stored product dusts
        if (productAmount > 0 && remainingIOQuota > 0) {
            int toEject = Math.min(productAmount, remainingIOQuota);

            int ejected = ejectionHelper.ejectStack(GTUtility.copyAmountUnsafe(toEject, product));

            if (ejected > 0) {
                storedProduct -= ejected;
                remainingIOQuota -= ejected;
                lEUt -= EU_PER_IO * ejected;

                outputs.add(GTUtility.copyAmountUnsafe(ejected, product));
            }
        }

        // eject any isotope dusts if we're in export mode
        if (machineMode == MODE_EXPORT && remainingIOQuota > 0 && isotope != null) {
            int isotopeAmount = MathHelper.floor_double(storedIsotope + EPSILON);

            if (isotopeAmount > 0) {
                int toEject = Math.min(isotopeAmount, remainingIOQuota);

                int ejected = ejectionHelper.ejectStack(GTUtility.copyAmountUnsafe(toEject, isotope));

                if (ejected > 0) {
                    storedIsotope -= ejected;
                    remainingIOQuota -= ejected;
                    lEUt -= EU_PER_IO * ejected;

                    outputs.add(GTUtility.copyAmountUnsafe(ejected, isotope));
                }
            }
        }

        mOutputItems = outputs.toArray(new ItemStack[0]);

        if (machineMode == MODE_NORMAL && Math.min(getAvailableCapacity(), remainingIOQuota) > 0) {
            ArrayList<ItemStack> inputs = getStoredInputs();

            if (isotope == null) {
                for (ItemStack input : inputs) {
                    GTRecipe recipe = getRecipeMap().findRecipeQuery()
                        .caching(false)
                        .items(input)
                        .find();

                    if (recipe != null) {
                        if (product != null && !GTUtility.areStacksEqual(product, recipe.mOutputs[0])) continue;

                        currentRecipe = recipe;
                        isotope = recipe.mInputs[0].copy();
                        product = recipe.mOutputs[0].copy();
                        break;
                    }
                }
            }

            for (ItemStack input : inputs) {
                if (GTUtility.areStacksEqual(isotope, input, true)) {
                    ItemStack toConsume = input.copy();

                    toConsume.stackSize = Math
                        .min(toConsume.stackSize, Math.min(getAvailableCapacity(), remainingIOQuota));

                    if (depleteInput(toConsume)) {
                        pendingInputs.add(toConsume);
                        remainingIOQuota -= toConsume.stackSize;
                        lEUt -= EU_PER_IO * toConsume.stackSize;
                        if (remainingIOQuota <= 0) break;
                    }
                }
            }
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        if (isotope != null) {
            aNBT.setTag("isotope", isotope.writeToNBT(new NBTTagCompound()));
            aNBT.setDouble("storedIsotope", storedIsotope);
        }

        if (product != null) {
            aNBT.setTag("product", product.writeToNBT(new NBTTagCompound()));
            aNBT.setDouble("storedProduct", storedProduct);
        }

        aNBT.setTag("pendingInputs", GTUtility.saveItemList(pendingInputs));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        isotope = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("isotope"));
        product = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("product"));

        storedIsotope = aNBT.getDouble("storedIsotope");
        storedProduct = aNBT.getDouble("storedProduct");

        pendingInputs.addAll(GTUtility.loadItemList(aNBT.getTagList("pendingInputs", Constants.NBT.TAG_COMPOUND)));

        GTRecipe recipe = getRecipeMap().findRecipeQuery()
            .caching(false)
            .items(isotope)
            .find();

        if (recipe != null && areStacksEqual(recipe.mOutputs[0], product)) {
            currentRecipe = recipe;
        }
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    private String getDecayRate() {
        StringBuilder sb = new StringBuilder();

        sb.append(" (");
        if (decayRate > 1) {
            sb.append(formatNumbers(decayRate))
                .append("/s)");
        } else {
            sb.append(formatNumbers(1d / decayRate))
                .append("s/ea)");
        }

        return sb.toString();
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widget(new FakeSyncWidget.DoubleSyncer(() -> storedIsotope, x -> storedIsotope = x));
        screenElements.widget(new FakeSyncWidget.DoubleSyncer(() -> storedProduct, x -> storedProduct = x));
        screenElements.widget(new FakeSyncWidget.DoubleSyncer(() -> decayRate, x -> decayRate = x));
        screenElements.widget(new FakeSyncWidget.ItemStackSyncer(() -> isotope, x -> isotope = x));
        screenElements.widget(new FakeSyncWidget.ItemStackSyncer(() -> product, x -> product = x));

        screenElements.widget(TextWidget.localised("GT5U.gui.text.contents"));

        screenElements.widget(
            TextWidget
                .dynamicString(
                    () -> GTUtility.translate(
                        "GT5U.gui.text.content-entry",
                        isotope == null ? "" : isotope.getDisplayName(),
                        formatNumbers(storedIsotope)))
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> isotope != null)
                .setSize(179, 10));
        screenElements.widget(
            TextWidget
                .dynamicString(
                    () -> GTUtility.translate(
                        "GT5U.gui.text.content-entry",
                        product == null ? "" : product.getDisplayName(),
                        formatNumbers(storedProduct)))
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> product != null)
                .setSize(179, 10));
        screenElements.widget(
            TextWidget
                .dynamicString(
                    () -> GTUtility.translate("GT5U.gui.text.decay-rate", formatNumbers(decayRate), getDecayRate()))
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> decayRate > 0)
                .setSize(179, 10));
        screenElements.widget(
            TextWidget.localised("GT5U.gui.text.nothing")
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> isotope == null && product == null)
                .setSize(170, 10));
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add("Isotope: " + GREEN + (isotope == null ? "None" : isotope.getDisplayName()));
        info.add("Product: " + YELLOW + (product == null ? "None" : product.getDisplayName()));
        info.add("Isotope Amount: " + GREEN + storedIsotope);
        info.add("Product Amount: " + YELLOW + storedProduct);
        info.add("Decay Rate: " + BLUE + decayRate);
        info.add("Capacity: " + BLUE + getWarehouseCapacity());
    }
}
