package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_CropVisualizer;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.TileEntityCrop;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GT_TileEntity_ExtremeIndustrialGreenhouse extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_ExtremeIndustrialGreenhouse> {

    private int mCasing = 0;
    private int mMaxSlots = 0;
    private int setupphase = 1;
    private boolean isIC2Mode = false;
    private byte glasTier = 0;
    private int waterusage = 0;
    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_ExtremeIndustrialGreenhouse> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_ExtremeIndustrialGreenhouse>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
            {"ccccc", "ccccc", "ccccc", "ccccc", "ccccc"},
            {"ccccc", "clllc", "clllc", "clllc", "ccccc"},
            {"ggggg", "g   g", "g   g", "g   g", "ggggg"},
            {"ggggg", "g   g", "g   g", "g   g", "ggggg"},
            {"ccccc", "cdddc", "cdwdc", "cdddc", "ccccc"},
            {"cc~cc", "ccccc", "ccccc", "ccccc", "ccccc"},
        }))
        .addElement('c', ofChain(
            onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1)),
            ofHatchAdder(GT_TileEntity_ExtremeIndustrialGreenhouse::addEnergyInputToMachineList, CASING_INDEX, 1),
            ofHatchAdder(GT_TileEntity_ExtremeIndustrialGreenhouse::addMaintenanceToMachineList, CASING_INDEX, 1),
            ofHatchAdder(GT_TileEntity_ExtremeIndustrialGreenhouse::addInputToMachineList, CASING_INDEX, 1),
            ofHatchAdder(GT_TileEntity_ExtremeIndustrialGreenhouse::addOutputToMachineList, CASING_INDEX, 1)
        ))
        .addElement('l', LoaderReference.ProjRedIllumination ? ofBlock(Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"), 10) : ofBlock(Blocks.redstone_lamp, 0))
        .addElement('g', BorosilicateGlass.ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glasTier = t, te -> te.glasTier))
        .addElement('d', ofBlock(LoaderReference.RandomThings ? Block.getBlockFromName("RandomThings:fertilizedDirt_tilled") : Blocks.farmland, 0))
        .addElement('w', ofBlock(Blocks.water, 0))
        .build();


    public GT_TileEntity_ExtremeIndustrialGreenhouse(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ExtremeIndustrialGreenhouse(String aName) {
        super(aName);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(aPlayer.isSneaking())
        {
            if(this.mMaxProgresstime > 0)
            {
                GT_Utility.sendChatToPlayer(aPlayer, "You cant change IC2 mode if the machine is working!");
                return;
            }
            if(!mStorage.isEmpty())
            {
                GT_Utility.sendChatToPlayer(aPlayer, "You cant change IC2 mode if there are seeds inside!");
                return;
            }
            this.isIC2Mode = !this.isIC2Mode;
            GT_Utility.sendChatToPlayer(aPlayer, "IC2 mode is now " + (this.isIC2Mode ? "enabled" : "disabled."));
        }
        else {
            if(this.mMaxProgresstime > 0)
            {
                GT_Utility.sendChatToPlayer(aPlayer, "You cant enable/disable setup if the machine is working!");
                return;
            }
            this.setupphase++;
            if(this.setupphase == 3)
                this.setupphase = 0;
            GT_Utility.sendChatToPlayer(aPlayer, "EIG is now running in " + (this.setupphase == 1 ? "setup mode (input)." : ( this.setupphase == 2 ? "setup mode (output)." : "normal operation.")));
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_ExtremeIndustrialGreenhouse(this.mName);
    }

    @Override
    public IStructureDefinition<GT_TileEntity_ExtremeIndustrialGreenhouse> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.
            addMachineType("Crop Farm").
            addInfo("Controller block for the Extreme Industrial Greenhouse").
            addInfo("Grow your crops like a chad !").
            addInfo("Use screwdriver to enable/change/disable setup mode").
            addInfo("Use screwdriver while sneaking to enable/disable IC2 mode").
            addInfo("Uses 1000L of water per crop per operation").
            addInfo("-------------------- SETUP   MODE --------------------").
            addInfo("Does not take power").
            addInfo("There are two modes: input / output").
            addInfo("Input mode: machine will take seeds from input bus and plant them").
            addInfo("Output mode: machine will take planted seeds and output them").
            addInfo("-------------------- NORMAL CROPS --------------------").
            addInfo("Minimal tier: EV").
            addInfo("Starting with 1 slot").
            addInfo("Every slot gives 64 crops").
            addInfo("Every tier past EV adds additional 2 slots").
            addInfo("Base process time: 5 sec").
            addInfo("Process time is divided by number of tiers past HV (Minimum 1 sec)").
            addInfo("All crops are grown at the end of the operation").
            addInfo("Will automatically craft seeds if they are not dropped").
            addInfo("-------------------- IC2    CROPS --------------------").
            addInfo("Minimal tier: UV").
            addInfo("Need UV glass tier").
            addInfo("Starting with 4 slots").
            addInfo("Every slot gives 1 crop").
            addInfo("Every tier past UV, slots are multiplied by 4").
            addInfo("Process time: 5 sec").
            addInfo("All crops are accelerated by x32 times").
            addInfo("Cannot process primordial").
            addInfo(BW_Tooltip_Reference.TT_BLUEPRINT).
            addSeparator().
            beginStructureBlock(5, 4, 5, false).
            addController("Front bottom center").
            addCasingInfo("Clean Stainless Steel Casings", 70).
            addOtherStructurePart("Borosilicate Glass", "Hollow two middle layers", 2).
            addStructureInfo("The glass tier limits the Energy Input tier").
            addMaintenanceHatch("Any casing", 1).
            addInputBus("Any casing", 1).
            addOutputBus("Any casing", 1).
            addInputHatch("Any casing", 1).
            addEnergyHatch("Any casing", 1).
            toolTipFinisher("Added by " + ChatColorHelper.GOLD + "kuba6000" + ChatColorHelper.RESET + ChatColorHelper.GREEN + " via " + BW_Tooltip_Reference.BW);
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("glasTier", glasTier);
        aNBT.setInteger("setupphase", setupphase);
        aNBT.setBoolean("isIC2Mode", isIC2Mode);
        aNBT.setInteger("mStorageSize", mStorage.size());
        for(int i = 0; i < mStorage.size(); i++)
            aNBT.setTag("mStorage." + i, mStorage.get(i).toNBTTagCompound());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        glasTier = aNBT.getByte("glasTier");
        setupphase = aNBT.getInteger("setupphase");
        isIC2Mode = aNBT.getBoolean("isIC2Mode");
        for(int i = 0; i < aNBT.getInteger("mStorageSize"); i++)
            mStorage.add(new GreenHouseSlot(aNBT.getCompoundTag("mStorage." + i)));
    }

    @SideOnly(Side.CLIENT)
    public void spawnVisualCrop(World world, int x, int y, int z, int meta, int age){
        BW_CropVisualizer crop = new BW_CropVisualizer(world, x, y, z, meta, age);
        Minecraft.getMinecraft().effectRenderer.addEffect(crop);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if(aBaseMetaTileEntity.isClientSide())
        {
            if(aBaseMetaTileEntity.isActive() && aTick % 40 == 0) {
                for(int x = -1; x <= 1; x++)
                    for(int z = -1; z <= 1; z++) {
                        if(x == 0 && z == 0)
                            continue;
                        int[] abc = new int[]{x, -2, z+2};
                        int[] xyz = new int[]{0, 0, 0};
                        this.getExtendedFacing().getWorldOffset(abc, xyz);
                        xyz[0] += aBaseMetaTileEntity.getXCoord();
                        xyz[1] += aBaseMetaTileEntity.getYCoord();
                        xyz[2] += aBaseMetaTileEntity.getZCoord();
                        spawnVisualCrop(aBaseMetaTileEntity.getWorld(), xyz[0], xyz[1], xyz[2], aBaseMetaTileEntity.getRandomNumber(8), 40);
                    }
            }
        }
        if(aBaseMetaTileEntity.isServerSide() && this.mMaxProgresstime > 0 && setupphase > 0 && aTick % 5 == 0)
        {
            if(setupphase == 1 && mStorage.size() < mMaxSlots) {
                List<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs)
                    if (addCrop(input))
                        break;
                this.updateSlots();
            }
            else if(setupphase == 2 && mStorage.size() > 0)
            {
                this.addOutput(this.mStorage.get(0).input.copy());
                this.mStorage.remove(0);
                this.updateSlots();
            }
        }
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 5, 0);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        long v = this.getMaxInputVoltage();
        int tier = GT_Utility.getTier(v);
        if(tier < (isIC2Mode ? 8 : 4))
            mMaxSlots = 0;
        else if(isIC2Mode)
            mMaxSlots = 4 << (2 * (tier - 8));
        else
            mMaxSlots = Math.max((tier - 4) * 2, 1);
        if(mStorage.size() > mMaxSlots)
        {
            // Void if user just downgraded power
            for(int i = mMaxSlots; i < mStorage.size(); i++)
            {
                mStorage.remove(i);
                i--;
            }
        }
        if(setupphase > 0) {
            if((mStorage.size() >= mMaxSlots && setupphase == 1) || (mStorage.size() == 0 && setupphase == 2))
                return false;
            this.mMaxProgresstime = 20;
            this.mEUt = 0;
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            return true;
        }
        if(mStorage.isEmpty())
            return false;

        waterusage = 0;
        for(GreenHouseSlot s : mStorage)
            waterusage += s.input.stackSize;

        if(!depleteInput(new FluidStack(FluidRegistry.WATER, waterusage * 1000)))
            return false;

        // OVERCLOCK
        if(isIC2Mode)
        {
            if(glasTier < 8)
                return false;
            this.mMaxProgresstime = 100;
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < Math.min(mMaxSlots, mStorage.size()); i++)
                outputs.addAll(mStorage.get(i).getIC2Drops(this.mMaxProgresstime / 8));
            this.mOutputItems = outputs.toArray(new ItemStack[0]);
        }
        else {
            this.mMaxProgresstime = Math.max(20, 100 / (tier - 3)); // Min 1 s
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < Math.min(mMaxSlots, mStorage.size()); i++) {
                for (ItemStack drop : mStorage.get(i).getDrops())
                    outputs.add(drop.copy());
            }
            this.mOutputItems = outputs.toArray(new ItemStack[0]);
        }
        this.mEUt = -(int)((double)v * 0.99d);
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        return true;
    }


    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        mCasing = 0;
        glasTier = 0;

        if(!checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0))
            return false;

        if (this.glasTier < 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.glasTier < hatchEnergy.mTier)
                    return false;

        return  this.mMaintenanceHatches.size() == 1 &&
                this.mEnergyHatches.size() >= 1 &&
                this.mCasing >= 70;
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
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(Arrays.asList(
            "Running in mode: " + EnumChatFormatting.GREEN + (setupphase == 0 ? (isIC2Mode ? "IC2 crops" : "Normal crops") : ("Setup mode " + (setupphase == 1 ? "(input)" : "(output)"))) + EnumChatFormatting.RESET,
            "Uses " + waterusage * 1000 + "/operation of water",
            "Max slots: " + EnumChatFormatting.GREEN + this.mMaxSlots + EnumChatFormatting.RESET,
            "Used slots: " + EnumChatFormatting.GREEN + this.mStorage.size() + EnumChatFormatting.RESET
        ));
        for(int i = 0; i < mStorage.size(); i++)
            info.add("Slot " + i + ": " + EnumChatFormatting.GREEN + "x" + this.mStorage.get(i).input.stackSize + " " + this.mStorage.get(i).input.getDisplayName() + EnumChatFormatting.RESET);
        info.addAll(Arrays.asList(super.getInfoData()));
        return info.toArray(new String[0]);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    public List<GreenHouseSlot> mStorage = new ArrayList<>();

    public boolean addCrop(ItemStack input){
        if(!isIC2Mode)
            for(GreenHouseSlot g : mStorage)
                if(GT_Utility.areStacksEqual(g.input, input))
                {
                    g.addAll(this.getBaseMetaTileEntity().getWorld(), input);
                    if(input.stackSize == 0)
                        return true;
                }
        GreenHouseSlot h = new GreenHouseSlot(this, input.copy(), true, isIC2Mode);
        if(h.isValid) {
            if(isIC2Mode)
                input.stackSize--;
            else
                input.stackSize = 0;
            mStorage.add(h);
            return true;
        }
        return false;
    }

    private static class GreenHouseSlot extends InventoryCrafting {

        ItemStack input;
        Block crop;
        List<ItemStack> drops;
        boolean isValid;
        boolean isIC2Crop;
        int growthticks;
        List<List<ItemStack>> generations;

        Random rn;
        IRecipe recipe;
        ItemStack recipeInput;

        int optimalgrowth = 7;

        public NBTTagCompound toNBTTagCompound(){
            NBTTagCompound aNBT = new NBTTagCompound();
            aNBT.setTag("input", input.writeToNBT(new NBTTagCompound()));
            if(!isIC2Crop) {
                aNBT.setInteger("crop", Block.getIdFromBlock(crop));
                aNBT.setInteger("dropscount", drops.size());
                for (int i = 0; i < drops.size(); i++)
                    aNBT.setTag("drop." + i, drops.get(i).writeToNBT(new NBTTagCompound()));
                aNBT.setInteger("optimalgrowth", optimalgrowth);
            }
            else {
                aNBT.setInteger("generationscount", generations.size());
                for(int i = 0; i < generations.size(); i++)
                {
                    aNBT.setInteger("generation." + i + ".count", generations.get(i).size());
                    for(int j = 0; j < generations.get(i).size(); j++)
                        aNBT.setTag("generation." + i + "." + j, generations.get(i).get(j).writeToNBT(new NBTTagCompound()));
                }
            }
            aNBT.setBoolean("isValid", isValid);
            aNBT.setBoolean("isIC2Crop", isIC2Crop);
            if(isIC2Crop) aNBT.setInteger("growthticks", growthticks);
            return aNBT;
        }

        public GreenHouseSlot(NBTTagCompound aNBT){
            super(null, 3, 3);
            isIC2Crop = aNBT.getBoolean("isIC2Crop");
            isValid = aNBT.getBoolean("isValid");
            input = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("input"));
            if(!isIC2Crop) {
                crop = Block.getBlockById(aNBT.getInteger("crop"));
                drops = new ArrayList<>();
                for (int i = 0; i < aNBT.getInteger("dropscount"); i++)
                    drops.add(ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("drop." + i)));
                optimalgrowth = aNBT.getInteger("optimalgrowth");
                if(optimalgrowth == 0) optimalgrowth = 7;
            }
            else
            {
                generations = new ArrayList<>();
                for(int i = 0; i < aNBT.getInteger("generationscount"); i++)
                {
                    generations.add(new ArrayList<>());
                    for(int j = 0; j < aNBT.getInteger("generation." + i + ".count"); j++)
                        generations.get(i).add(ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("generation." + i + "." + j)));
                }
                growthticks = aNBT.getInteger("growthticks");
                rn = new Random();
            }
        }

        public boolean addAll(World world, ItemStack input){
            if(!GT_Utility.areStacksEqual(this.input, input))
                return false;
            if(this.input.stackSize == 64)
                return false;
            int toconsume = Math.min(64 - this.input.stackSize, input.stackSize);
            int left = addDrops(world, toconsume, true);
            input.stackSize -= toconsume - left;
            this.input.stackSize += toconsume - left;
            return left == 0;
        }

        public boolean findCropRecipe(World world){
            if(recipe != null)
                return true;
            out : for (ItemStack drop : drops) {
                recipeInput = drop;
                for (int j = 0; j < CraftingManager.getInstance().getRecipeList().size(); j++) {
                    recipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(j);
                    if (recipe.matches(this, world) && GT_Utility.areStacksEqual(recipe.getCraftingResult(this), input)) {
                        break out;
                    } else
                        recipe = null;
                }
            }
            return recipe != null;
        }

        @Override
        public ItemStack getStackInSlot(int p_70301_1_) {
            if(p_70301_1_ == 0)
                return recipeInput.copy();
            return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int par1) {
            return null;
        }

        @Override
        public ItemStack decrStackSize(int par1, int par2)
        {
            return null;
        }

        @Override
        public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
            return;
        }

        public GreenHouseSlot(GT_TileEntity_ExtremeIndustrialGreenhouse tileEntity, ItemStack input, boolean autocraft, boolean IC2){
            super(null, 3, 3);
            World world = tileEntity.getBaseMetaTileEntity().getWorld();
            this.input = input.copy();
            this.isValid = false;
            if(IC2)
            {
                GreenHouseSlotIC2(tileEntity, world, input);
                return;
            }
            if (!(input.getItem() instanceof ItemSeeds)) {
                return;
            }
            Block b = ((ItemSeeds) input.getItem()).getPlant(world, 0, 0, 0);
            if (!(b instanceof IGrowable))
                return;
            GameRegistry.UniqueIdentifier u = GameRegistry.findUniqueIdentifierFor(input.getItem());
            if(u != null && Objects.equals(u.modId, "Natura"))
                optimalgrowth = 8;
            crop = b;
            isIC2Crop = false;
            if(addDrops(world, input.stackSize, autocraft) == 0 && !drops.isEmpty()){
                this.isValid = true;
            }
        }

        public void GreenHouseSlotIC2(GT_TileEntity_ExtremeIndustrialGreenhouse tileEntity, World world, ItemStack input){
            if(!ItemList.IC2_Crop_Seeds.isStackEqual(input, true, true))
                return;
            CropCard cc = Crops.instance.getCropCard(input);
            if(cc.tier() > 15) // dont process primordial
                return;
            this.input.stackSize = 1;
            NBTTagCompound nbt = input.getTagCompound();
            byte gr = nbt.getByte("growth");
            byte ga = nbt.getByte("gain");
            byte re = nbt.getByte("resistance");
            this.isIC2Crop = true;
            int[] abc = new int[]{0, -2, 3};
            int[] xyz = new int[]{0, 0, 0};
            tileEntity.getExtendedFacing().getWorldOffset(abc, xyz);
            xyz[0] += tileEntity.getBaseMetaTileEntity().getXCoord();
            xyz[1] += tileEntity.getBaseMetaTileEntity().getYCoord();
            xyz[2] += tileEntity.getBaseMetaTileEntity().getZCoord();
            try{
                world.setBlock(xyz[0], xyz[1], xyz[2], Block.getBlockFromItem(Ic2Items.crop.getItem()), 0, 3);
                TileEntity wte = world.getTileEntity(xyz[0], xyz[1], xyz[2]);
                if(!(wte instanceof TileEntityCrop))
                {
                    // should not be even possible
                    return;
                }
                TileEntityCrop te = (TileEntityCrop)wte;
                te.ticker = 1; // dont even think about ticking once
                te.setCrop(cc);
                te.setSize((byte) cc.maxSize());
                te.setGrowth(gr);
                te.setGain(ga);
                te.setResistance(re);

                if(!cc.canBeHarvested(te))
                    return;
                // GENERATE DROPS
                generations = new ArrayList<>();
                for(int i = 0; i < 10; i++) // get 10 generations
                {
                    ItemStack[] st = te.harvest_automated(false);
                    te.setSize((byte) cc.maxSize());
                    if (st == null){
                        i--;
                        continue;
                    }
                    if (st.length == 0)
                        continue;
                    generations.add(new ArrayList<>(Arrays.asList(st)));
                }
                if(generations.isEmpty())
                    return;
                rn = new Random();
                input.stackSize --;

                // CHECK GROWTH SPEED
                te.humidity = 12;   // humidity with full water storage
                te.airQuality = 6;  // air quality when sky is seen
                te.nutrients = 8;   // netrients with full nutrient storage

                int dur = cc.growthDuration(te);
                int rate = te.calcGrowthRate();
                if(rate == 0) // should not be possible with those stats
                    return;
                growthticks = dur / rate;
                if(growthticks < 1)
                    growthticks = 1;

                this.isValid = true;
            }
            catch (Exception e){
                e.printStackTrace(System.err);
            }
            finally {
                world.setBlockToAir(xyz[0], xyz[1], xyz[2]);
            }
        }

        public List<ItemStack> getDrops(){
            return drops;
        }

        Map<String, Double> dropprogress = new HashMap<>();
        static Map<String, ItemStack> dropstacks = new HashMap<>();

        public List<ItemStack> getIC2Drops(int timeelapsed){
            int r = rn.nextInt(10);
            if(generations.size() <= r)
                return new ArrayList<>();
            double growthPercent = ((double)timeelapsed / (double)growthticks);
            List<ItemStack> generation = generations.get(r);
            List<ItemStack> copied = new ArrayList<>();
            for(ItemStack g : generation)
                copied.add(g.copy());
            for(ItemStack s : copied)
            {
                double pro = ((double)s.stackSize * growthPercent);
                s.stackSize = 1;
                if(dropprogress.containsKey(s.toString()))
                    dropprogress.put(s.toString(), dropprogress.get(s.toString()) + pro);
                else
                    dropprogress.put(s.toString(), pro);
                if(!dropstacks.containsKey(s.toString()))
                    dropstacks.put(s.toString(), s.copy());
            }
            copied.clear();
            for(Map.Entry<String, Double> entry : dropprogress.entrySet())
                if(entry.getValue() >= 1d)
                {
                    copied.add(dropstacks.get(entry.getKey()).copy());
                    copied.get(copied.size()-1).stackSize = entry.getValue().intValue();
                    entry.setValue(entry.getValue() - (double)entry.getValue().intValue());
                }
            return copied;
        }

        public int addDrops(World world, int count, boolean autocraft){
            drops = new ArrayList<>();
            for(int i = 0; i < count; i++) {
                List<ItemStack> d = crop.getDrops(world, 0, 0, 0, optimalgrowth, 0);
                for(ItemStack x : drops)
                    for(ItemStack y : d)
                        if(GT_Utility.areStacksEqual(x, y))
                        {
                            x.stackSize += y.stackSize;
                            y.stackSize = 0;
                        }
                for(ItemStack x : d)
                    if(x.stackSize > 0)
                        drops.add(x.copy());
            }
            for(int i = 0; i < drops.size(); i++)
            {
                if(GT_Utility.areStacksEqual(drops.get(i), input))
                {
                    int took = Math.min(drops.get(i).stackSize, count);
                    drops.get(i).stackSize -= took;
                    count -= took;
                    if(drops.get(i).stackSize == 0) {
                        drops.remove(i);
                        i--;
                    }
                    if(count == 0) {
                        return 0;
                    }
                }
            }
            if(autocraft)
            {
                if(!findCropRecipe(world))
                    return count;
                int totake = count / recipe.getCraftingResult(this).stackSize + 1;
                for(int i = 0; i < drops.size(); i++) {
                    if(GT_Utility.areStacksEqual(drops.get(i), recipeInput)) {
                        int took = Math.min(drops.get(i).stackSize, totake);
                        drops.get(i).stackSize -= took;
                        totake -= took;
                        if (drops.get(i).stackSize == 0) {
                            drops.remove(i);
                            i--;
                        }
                        if(totake == 0) {
                            return 0;
                        }
                    }
                }
            }
            return count;
        }
    }


}
