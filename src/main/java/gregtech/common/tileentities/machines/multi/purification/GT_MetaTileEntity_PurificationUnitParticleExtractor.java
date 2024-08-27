package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import gregtech.common.items.ID_MetaItem_03;

public class GT_MetaTileEntity_PurificationUnitParticleExtractor
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitParticleExtractor>
    implements ISurvivalConstructable {

    public static long BARYONIC_MATTER_OUTPUT = 2000L;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 8;
    private static final int STRUCTURE_Y_OFFSET = 8;
    private static final int STRUCTURE_Z_OFFSET = 0;

    static final String[][] structure = new String[][] {
        // spotless:off
        { "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "      AAAAA      ", "      AAAAA      ", "      AA~AA      ", "      AAAAA      ", "      AAAAA      ", "                 ", "                 ", "                 ", "                 ", "                 ", "                 " },
        { "                 ", "        E        ", "        E        ", "        E        ", "        E        ", "        E        ", "      AAAAA      ", "      AAAAA      ", " EEEEEAAAAAEEEEE ", "      AAAAA      ", "      AAAAA      ", "        E        ", "        E        ", "        E        ", "        E        ", "        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "      CCCCC      ", "      CDDDC      ", " E    CDBDC    E ", "      CDDDC      ", "      CCCCC      ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "                 ", "       DDD       ", " E     DBD     E ", "       DDD       ", "                 ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "                 ", "       DDD       ", " E     DBD     E ", "       DDD       ", "                 ", "                 ", "                 ", "                 ", "                 ","        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "                 ", "       DDD       ", " E     DBD     E ", "       DDD       ", "                 ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "      AAAAA      ", "      AAAAA      ", "      CCCCC      ", "                 ", "                 ", "                 ", "AAC   AAAAA   CAA", "AAC   ADDDA   CAA", "AAC   ADBDA   CAA", "AAC   ADDDA   CAA", "AAC   AAAAA   CAA", "                 ", "                 ", "                 ", "      CCCCC      ", "      AAAAA      ", "      AAAAA      " },
        { "      AAAAA      ", "      AAAAA      ", "      CDDDC      ", "       DDD       ", "       DDD       ", "       DDD       ", "AAC   ADDDA   CAA", "AADDDDD   DDDDDAA", "AADDDDD B DDDDDAA", "AADDDDD   DDDDDAA", "AAC   ADDDA   CAA", "       DDD       ", "       DDD       ", "       DDD       ", "      CDDDC      ", "      AAAAA      ", "      AAAAA      " },
        { "      AAAAA      ", " EEEEEAAAAAEEEEE ", " E    CDBDC    E ", " E     DBD     E ", " E     DBD     E ", " E     DBD     E ", "AAC   ADBDA   CAA", "AADDDDD B DDDDDAA", "AABBBBBBBBBBBBBAA", "AADDDDD B DDDDDAA", "AAC   ADBDA   CAA", " E     DBD     E ", " E     DBD     E ", " E     DBD     E ", " E    CDBDC    E ", " EEEEEAAAAAEEEEE ", "      AAAAA      " },
        { "      AAAAA      ", "      AAAAA      ", "      CDDDC      ", "       DDD       ", "       DDD       ", "       DDD       ", "AAC   ADDDA   CAA", "AADDDDD   DDDDDAA", "AADDDDD B DDDDDAA", "AADDDDD   DDDDDAA", "AAC   ADDDA   CAA", "       DDD       ", "       DDD       ", "       DDD       ", "      CDDDC      ", "      AAAAA      ", "      AAAAA      " },
        { "      AAAAA      ", "      AAAAA      ", "      CCCCC      ", "                 ", "                 ", "                 ", "AAC   AAAAA   CAA", "AAC   ADDDA   CAA", "AAC   ADBDA   CAA", "AAC   ADDDA   CAA", "AAC   AAAAA   CAA", "                 ", "                 ", "                 ", "      CCCCC      ", "      AAAAA      ", "      AAAAA      " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "                 ", "       DDD       ", " E     DBD     E ", "       DDD       ", "                 ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "                 ", "       DDD       ", " E     DBD     E ", "       DDD       ", "                 ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "                 ", "       DDD       ", " E     DBD     E ", "       DDD       ", "                 ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "                 ", "        E        ", "                 ", "                 ", "                 ", "                 ", "      CCCCC      ", "      CDDDC      ", " E    CDBDC    E ", "      CDDDC      ", "      CCCCC      ", "                 ", "                 ", "                 ", "                 ", "        E        ", "                 " },
        { "                 ", "        E        ", "        E        ", "        E        ", "        E        ", "        E        ", "      AAAAA      ", "      AAAAA      ", " EEEEEAAAAAEEEEE ", "      AAAAA      ", "      AAAAA      ", "        E        ", "        E        ", "        E        ", "        E        ", "        E        ", "                 " },
        { "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "      AAAAA      ", "      AAAAA      ", "      AAAAA      ", "      AAAAA      ", "      AAAAA      ", "                 ", "                 ", "                 ", "                 ", "                 ", "                 " } };
    // spotless:on

    // Dimensionally transcendent casing (placeholder)
    private static final int CASING_INDEX_MAIN = getTextureIndex(GregTech_API.sBlockCasings10, 2);

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitParticleExtractor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitParticleExtractor>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Quark Exclusion Casing
        .addElement(
            'A',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitParticleExtractor>buildHatchAdder()
                        .atLeastList(Arrays.asList(InputBus, OutputBus, InputHatch, OutputHatch))
                        .dot(1)
                        .casingIndex(CASING_INDEX_MAIN)
                        .build()),
                onElementPass(t -> t.numCasings++, ofBlock(GregTech_API.sBlockCasings10, 2))))
        // Particle Beam Guidance Pipe Casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings9, 14))
        // Femtometer-Calibrated Particle Beam Casing
        .addElement('C', ofBlock(GregTech_API.sBlockCasings9, 15))
        // Non-Photonic Matter Exclusion Glass
        .addElement('D', ofBlock(GregTech_API.sBlockGlass1, 3))
        .addElement('E', ofFrame(Materials.Bedrockium))
        .build();

    private static class CatalystCombination {

        public ItemStack firstCatalyst;
        public ItemStack secondCatalyst;

        public static ItemList[] CATALYST_ITEMS = new ItemList[] { ItemList.Quark_Creation_Catalyst_Up,
            ItemList.Quark_Creation_Catalyst_Down, ItemList.Quark_Creation_Catalyst_Bottom,
            ItemList.Quark_Creation_Catalyst_Top, ItemList.Quark_Creation_Catalyst_Strange,
            ItemList.Quark_Creation_Catalyst_Charm };

        public CatalystCombination(ItemStack first, ItemStack second) {
            firstCatalyst = first;
            secondCatalyst = second;
        }

        public boolean matches(ItemStack a, ItemStack b) {
            return (a.isItemEqual(firstCatalyst) && b.isItemEqual(secondCatalyst))
                || (b.isItemEqual(firstCatalyst) && a.isItemEqual(secondCatalyst));
        }

        public NBTTagCompound saveToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound first = new NBTTagCompound();
            NBTTagCompound second = new NBTTagCompound();
            firstCatalyst.writeToNBT(first);
            secondCatalyst.writeToNBT(second);
            nbt.setTag("first", first);
            nbt.setTag("second", second);
            return nbt;
        }

        public static CatalystCombination readFromNBT(NBTTagCompound nbt) {
            NBTTagCompound first = nbt.getCompoundTag("first");
            NBTTagCompound second = nbt.getCompoundTag("second");
            return new CatalystCombination(
                ItemStack.loadItemStackFromNBT(first),
                ItemStack.loadItemStackFromNBT(second));
        }
    }

    private static CatalystCombination generateNewCombination() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // Generate two unique indices into the list
        int firstIndex = random.nextInt(0, CatalystCombination.CATALYST_ITEMS.length);
        int secondIndex = random.nextInt(0, CatalystCombination.CATALYST_ITEMS.length);
        while (secondIndex == firstIndex) {
            secondIndex = random.nextInt(0, CatalystCombination.CATALYST_ITEMS.length);
        }

        return new CatalystCombination(
            CatalystCombination.CATALYST_ITEMS[firstIndex].get(1),
            CatalystCombination.CATALYST_ITEMS[secondIndex].get(1));
    }

    private CatalystCombination currentCombination = null;

    private ArrayList<ItemStack> insertedCatalysts = new ArrayList<>();

    private static final long CATALYST_BASE_COST = 144L;

    private int correctStartIndex = -1;
    private int numCasings = 0;
    private static final int MIN_CASINGS = 300;

    public GT_MetaTileEntity_PurificationUnitParticleExtractor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitParticleExtractor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitParticleExtractor(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitParticleExtractor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        numCasings = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        if (numCasings < MIN_CASINGS) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Controller block for the Absolute Baryonic Perfection Purification Unit.")
            .addInfo("Must be linked to a Purification Plant using a data stick to work.")
            .addSeparator()
            .addInfo(
                "Insert " + EnumChatFormatting.WHITE
                    + "Quark Releasing Catalysts "
                    + EnumChatFormatting.GRAY
                    + "into the input bus while running.")
            .addInfo(
                "Every recipe cycle, a different combination of " + EnumChatFormatting.RED
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " different "
                    + EnumChatFormatting.WHITE
                    + "Quark Releasing Catalysts")
            .addInfo("will correctly identify the lone quark and succeed the recipe.")
            .addSeparator()
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "20"
                    + EnumChatFormatting.GRAY
                    + " ticks, consumes ALL catalysts in the input bus.")
            .addInfo(
                "The base cost of inserting a catalyst is " + EnumChatFormatting.RED
                    + CATALYST_BASE_COST
                    + "L"
                    + EnumChatFormatting.WHITE
                    + " Molten Infinity")
            .addInfo("For every duplicate occurrence of an inserted catalyst in the sequence, this cost is doubled.")
            .addSeparator()
            .addInfo("Keeps track of the entire sequence of catalysts inserted this recipe.")
            .addInfo(
                "If the correct catalyst combination is in the sequence of inserted catalysts, immediately outputs "
                    + EnumChatFormatting.WHITE
                    + "Stabilised Baryonic Matter")
            .addInfo("At the end of the recipe, all incorrectly inserted catalysts are returned in the output bus.")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "The final stage of water purification goes beyond subatomic particles and identifies the smallest")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "possible imperfections within the baryons themselves. By correctly identifying which pairs of quark")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "flavors are required, the unit will activate the catalysts, stabilizing the errant particles.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "This ultimately creates both Stabilised Baryonic Matter and, most importantly, absolutely perfectly purified water.")
            .addInfo(AuthorNotAPenguin)
            .beginStructureBlock(17, 17, 17, false)
            .addCasingInfoMinColored(
                "Quark Exclusion Casing",
                EnumChatFormatting.GRAY,
                MIN_CASINGS,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Femtometer-Calibrated Particle Beam Casing",
                EnumChatFormatting.GRAY,
                96,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Particle Beam Guidance Pipe Casing",
                EnumChatFormatting.GRAY,
                37,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Non-Photonic Matter Exclusion Glass",
                EnumChatFormatting.GRAY,
                240,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Bedrockium Frame Box",
                EnumChatFormatting.GRAY,
                108,
                EnumChatFormatting.GOLD,
                false)
            .addController("Front Center")
            .addInputBus("Any Quark Exclusion Casing", 1)
            .addInputHatch("Any Quark Exclusion Casing", 1)
            .addOutputBus("Any Quark Exclusion Casing", 1)
            .addOutputHatch("Any Quark Exclusion Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationParticleExtractionRecipes;
    }

    @Override
    public void startCycle(int cycleTime, int progressTime) {
        super.startCycle(cycleTime, progressTime);
        this.insertedCatalysts.clear();
        this.currentCombination = generateNewCombination();
        correctStartIndex = -1;
    }

    private boolean isCatalyst(ItemStack stack) {
        if (stack.getItem() instanceof GT_MetaGenerated_Item_03) {
            int meta = stack.getItemDamage() - 32000; // why, greg.
            return meta >= ID_MetaItem_03.Quark_Creation_Catalyst_Up.ID
                && meta <= ID_MetaItem_03.Quark_Creation_Catalyst_Top.ID;
        }
        return false;
    }

    @Override
    public void endCycle() {
        super.endCycle();
        // Output incorrect indices unchanged, the spent ones will follow if recipe was successful from the actual
        // recipe outputs
        for (int i = 0; i < insertedCatalysts.size(); ++i) {
            if (i == correctStartIndex || i == correctStartIndex + 1) continue;

            addOutput(insertedCatalysts.get(i));
        }
    }

    @Override
    public float calculateFinalSuccessChance() {
        // Only succeed if correct combination was inserted
        if (correctStartIndex != -1) return 100.0f;
        else return 0.0f;
    }

    private int calculateCatalystCost(ItemStack newCatalyst) {
        // Count number of previously inserted catalysts
        int count = 0;
        for (ItemStack cat : this.insertedCatalysts) {
            // We already assume that newCatalyst is a valid catalyst item
            if (cat.getItemDamage() == newCatalyst.getItemDamage()) {
                ++count;
            }
        }
        // Cost is exponential in function of amount of duplicate catalysts
        return (int) (Math.pow(2, count) * CATALYST_BASE_COST);
    }

    // Returns the first index of a valid combination, or -1 if there is no valid combination in the sequence
    public int checkSequence() {
        // Loop over the entire sequence and check if any pair contains a valid combination
        for (int i = 0; i < insertedCatalysts.size() - 1; ++i) {
            ItemStack first = insertedCatalysts.get(i);
            ItemStack second = insertedCatalysts.get(i + 1);
            // Found a match, return its starting index
            if (currentCombination.matches(first, second)) {
                return i;
            }
        }
        // No match found, return -1
        return -1;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        // Every 20 ticks, add all catalysts from the input bus to the internal inventory.
        if (mMaxProgresstime > 0 && aTick % 20 == 0) {
            ArrayList<ItemStack> storedInputs = getStoredInputs();
            // For each stack in the input, check if it is a valid catalyst item and if so consume it
            for (ItemStack stack : storedInputs) {
                if (isCatalyst(stack)) {
                    // Try to deplete catalyst cost first
                    int cost = calculateCatalystCost(stack);
                    FluidStack inputCost = Materials.Infinity.getMolten(cost);
                    // Drain the input cost directly from a hatch since we are not inside
                    // recipe processing
                    boolean drained = false;
                    for (GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
                        FluidStack drainedStack = hatch.drain(ForgeDirection.UNKNOWN, inputCost, true);
                        if (drainedStack != null && drainedStack.amount == inputCost.amount) {
                            drained = true;
                            break;
                        }
                    }
                    // If we could not drain, stop the machine
                    if (!drained) {
                        stopMachine(ShutDownReasonRegistry.outOfFluid(inputCost));
                        return;
                    }
                    // Now add the catalysts to the list, one by one since there may be multiples and we want to
                    // keep them as single entries in the list
                    for (int i = 0; i < stack.stackSize; ++i) {
                        ItemStack singleStack = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
                        this.insertedCatalysts.add(singleStack);
                    }
                    // Then deplete the entire stack
                    this.depleteInput(stack);
                }
            }

            // Only do this check if we didn't find a correct combination yet
            if (correctStartIndex != -1) return;

            // Now check the sequence for a correct combination
            correctStartIndex = checkSequence();
            // If we found something, immediately output stable baryonic matter
            if (correctStartIndex != -1) addOutput(Materials.StableBaryonicMatter.getFluid(BARYONIC_MATTER_OUTPUT));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagCompound insertedNBT = new NBTTagCompound();
        for (int i = 0; i < insertedCatalysts.size(); ++i) {
            ItemStack inserted = insertedCatalysts.get(i);
            NBTTagCompound itemNBT = new NBTTagCompound();
            inserted.writeToNBT(itemNBT);
            insertedNBT.setTag(Integer.toString(i), itemNBT);
        }
        aNBT.setTag("insertedItems", insertedNBT);
        aNBT.setInteger("correctStartIndex", correctStartIndex);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        // Generate a random combination on load
        currentCombination = generateNewCombination();
        if (aNBT.hasKey("insertedItems")) {
            NBTTagCompound insertedList = aNBT.getCompoundTag("insertedItems");
            // Initialize empty list with correct size
            this.insertedCatalysts = new ArrayList<>(
                Collections.nCopies(
                    insertedList.func_150296_c()
                        .size(),
                    null));
            for (String key : insertedList.func_150296_c()) {
                NBTTagCompound itemCompound = insertedList.getCompoundTag(key);
                int index = Integer.parseInt(key);
                this.insertedCatalysts.set(index, ItemStack.loadItemStackFromNBT(itemCompound));
            }
        }
        if (aNBT.hasKey("correctStartIndex")) {
            correctStartIndex = aNBT.getInteger("correctStartIndex");
        }
        super.loadNBTData(aNBT);
    }

    private String getCorrectlyDecodedString() {
        if (correctStartIndex != -1) {
            return EnumChatFormatting.GREEN + "Yes";
        }
        return EnumChatFormatting.RED + "No";
    }

    public EnumChatFormatting getQuarkColor(ItemStack stack) {
        int meta = stack.getItemDamage() - 32000;
        if (meta == ID_MetaItem_03.Quark_Creation_Catalyst_Up.ID) return EnumChatFormatting.BLUE;
        if (meta == ID_MetaItem_03.Quark_Creation_Catalyst_Down.ID) return EnumChatFormatting.LIGHT_PURPLE;
        if (meta == ID_MetaItem_03.Quark_Creation_Catalyst_Strange.ID) return EnumChatFormatting.YELLOW;
        if (meta == ID_MetaItem_03.Quark_Creation_Catalyst_Charm.ID) return EnumChatFormatting.GREEN;
        if (meta == ID_MetaItem_03.Quark_Creation_Catalyst_Bottom.ID) return EnumChatFormatting.AQUA;
        if (meta == ID_MetaItem_03.Quark_Creation_Catalyst_Top.ID) return EnumChatFormatting.RED;
        return EnumChatFormatting.GRAY;
    }

    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Catalyst insertion history for this recipe cycle");
        for (int i = 0; i < insertedCatalysts.size(); ++i) {
            ItemStack stack = insertedCatalysts.get(i);
            String name = stack.getDisplayName();
            String[] split = name.split("-");
            info.add(
                EnumChatFormatting.YELLOW + ""
                    + (i + 1)
                    + ": "
                    + getQuarkColor(stack)
                    + split[0]
                    + EnumChatFormatting.GRAY
                    + "-"
                    + split[1]);
        }
        info.add("Quark Combination correctly identified: " + getCorrectlyDecodedString());
        return info.toArray(new String[] {});
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 8;
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_UEV;
    }
}
