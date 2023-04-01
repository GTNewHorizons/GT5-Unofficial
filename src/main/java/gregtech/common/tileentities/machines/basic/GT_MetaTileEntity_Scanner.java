package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.ModIDs.GalacticraftCore;
import static gregtech.api.enums.ModIDs.GalacticraftMars;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes;

import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_AssemblyLineUtils;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;

public class GT_MetaTileEntity_Scanner extends GT_MetaTileEntity_BasicMachine {

    public GT_MetaTileEntity_Scanner(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                1,
                "Scans Crops and other things.",
                1,
                1,
                "Scanner.png",
                "",
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_SIDE_SCANNER_ACTIVE),
                        TextureFactory.builder().addIcon(OVERLAY_SIDE_SCANNER_ACTIVE_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_SIDE_SCANNER),
                        TextureFactory.builder().addIcon(OVERLAY_SIDE_SCANNER_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_FRONT_SCANNER_ACTIVE),
                        TextureFactory.builder().addIcon(OVERLAY_FRONT_SCANNER_ACTIVE_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_FRONT_SCANNER),
                        TextureFactory.builder().addIcon(OVERLAY_FRONT_SCANNER_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_TOP_SCANNER_ACTIVE),
                        TextureFactory.builder().addIcon(OVERLAY_TOP_SCANNER_ACTIVE_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_TOP_SCANNER),
                        TextureFactory.builder().addIcon(OVERLAY_TOP_SCANNER_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_BOTTOM_SCANNER_ACTIVE),
                        TextureFactory.builder().addIcon(OVERLAY_BOTTOM_SCANNER_ACTIVE_GLOW).glow().build()),
                TextureFactory.of(
                        TextureFactory.of(OVERLAY_BOTTOM_SCANNER),
                        TextureFactory.builder().addIcon(OVERLAY_BOTTOM_SCANNER_GLOW).glow().build()));
    }

    public GT_MetaTileEntity_Scanner(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
            String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_Scanner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
            String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Scanner(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures,
                this.mGUIName,
                this.mNEIName);
    }

    @Override
    public int checkRecipe() {
        ItemStack aStack = getInputAt(0);
        if (getOutputAt(0) != null) {
            this.mOutputBlocked += 1;
        } else if ((GT_Utility.isStackValid(aStack)) && (aStack.stackSize > 0)) {
            if ((getFillableStack() != null) && (getFillableStack().containsFluid(Materials.Honey.getFluid(100L)))) {
                try {
                    IIndividual tIndividual = AlleleManager.alleleRegistry.getIndividual(aStack);
                    if (tIndividual != null) {
                        if (tIndividual.analyze()) {
                            getFillableStack().amount -= 100;
                            this.mOutputItems[0] = GT_Utility.copyOrNull(aStack);
                            aStack.stackSize = 0;
                            NBTTagCompound tNBT = new NBTTagCompound();
                            tIndividual.writeToNBT(tNBT);
                            this.mOutputItems[0].setTagCompound(tNBT);
                            calculateOverclockedNess(2, 500);
                            // In case recipe is too OP for that machine
                            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                            return 2;
                        }
                        this.mOutputItems[0] = GT_Utility.copyOrNull(aStack);
                        aStack.stackSize = 0;
                        this.mMaxProgresstime = 1;
                        this.mEUt = 1;
                        return 2;
                    }
                } catch (Throwable e) {
                    if (D1) {
                        e.printStackTrace(GT_Log.err);
                    }
                }
            }
            if (ItemList.IC2_Crop_Seeds.isStackEqual(aStack, true, true)) {
                NBTTagCompound tNBT = aStack.getTagCompound();
                if (tNBT == null) {
                    tNBT = new NBTTagCompound();
                }
                if (tNBT.getByte("scan") < 4) {
                    tNBT.setByte("scan", (byte) 4);
                    calculateOverclockedNess(8, 160);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                } else {
                    this.mMaxProgresstime = 1;
                    this.mEUt = 1;
                }
                aStack.stackSize -= 1;
                this.mOutputItems[0] = GT_Utility.copyAmount(1L, aStack);
                assert this.mOutputItems[0] != null;
                this.mOutputItems[0].setTagCompound(tNBT);
                return 2;
            }
            if (ItemList.Tool_DataOrb.isStackEqual(getSpecialSlot(), false, true)) {
                if (ItemList.Tool_DataOrb.isStackEqual(aStack, false, true)) {
                    aStack.stackSize -= 1;
                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, getSpecialSlot());
                    calculateOverclockedNess(30, 512);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                ItemData tData = GT_OreDictUnificator.getAssociation(aStack);
                if ((tData != null) && ((tData.mPrefix == OrePrefixes.dust) || (tData.mPrefix == OrePrefixes.cell))
                        && (tData.mMaterial.mMaterial.mElement != null)
                        && (!tData.mMaterial.mMaterial.mElement.mIsIsotope)
                        && (tData.mMaterial.mMaterial != Materials.Magic)
                        && (tData.mMaterial.mMaterial.getMass() > 0L)) {
                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(this.mOutputItems[0], "Elemental-Scan");
                    Behaviour_DataOrb.setDataName(this.mOutputItems[0], tData.mMaterial.mMaterial.mElement.name());
                    calculateOverclockedNess(30, GT_Utility.safeInt(tData.mMaterial.mMaterial.getMass() * 8192L));
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
            }
            if (ItemList.Tool_DataStick.isStackEqual(getSpecialSlot(), false, true)) {
                if (ItemList.Tool_DataStick.isStackEqual(aStack, false, true)) {
                    aStack.stackSize -= 1;
                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, getSpecialSlot());
                    calculateOverclockedNess(30, 128);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                if (aStack.getItem() == Items.written_book) {
                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, getSpecialSlot());
                    assert this.mOutputItems[0] != null;
                    this.mOutputItems[0].setTagCompound(aStack.getTagCompound());
                    calculateOverclockedNess(30, 128);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                if (aStack.getItem() == Items.filled_map) {
                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, getSpecialSlot());
                    assert this.mOutputItems[0] != null;
                    this.mOutputItems[0].setTagCompound(
                            GT_Utility.getNBTContainingShort(
                                    new NBTTagCompound(),
                                    "map_id",
                                    (short) aStack.getItemDamage()));
                    calculateOverclockedNess(30, 128);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }

                if ((aStack.getItem().getUnlocalizedName().contains("Schematic")
                        || aStack.getItem().getUnlocalizedName().contains("schematic"))
                        && !aStack.getItem().getUnlocalizedName().contains("Schematics")) {
                    if (mTier < 3) return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    String sTier = "";

                    int stackItemID = Item.getIdFromItem(aStack.getItem());
                    int stackItemDamage = aStack.getItemDamage();
                    if (stackItemID == Item.getIdFromItem(
                            Objects.requireNonNull(
                                    GT_ModHandler.getModItem(GalacticraftCore.modID, "item.schematic", 1L, 0))
                                    .getItem())) {
                        if (stackItemDamage == 0 && aStack.toString().equals(
                                Objects.requireNonNull(
                                        GT_ModHandler.getModItem(GalacticraftCore.modID, "item.schematic", 1L, 0))
                                        .copy().toString()))
                            sTier = "100";
                        else if (stackItemDamage == 1 && aStack.toString().equals(
                                Objects.requireNonNull(
                                        GT_ModHandler.getModItem(GalacticraftCore.modID, "item.schematic", 1L, 1))
                                        .copy().toString()))
                            sTier = "2";
                    } else {
                        if (stackItemID == Item.getIdFromItem(
                                Objects.requireNonNull(
                                        GT_ModHandler.getModItem(GalacticraftMars.modID, "item.schematic", 1L, 0))
                                        .getItem())) {
                            if (stackItemDamage == 0 && aStack.toString().equals(
                                    Objects.requireNonNull(
                                            GT_ModHandler.getModItem(GalacticraftMars.modID, "item.schematic", 1L, 0))
                                            .copy().toString()))
                                sTier = "3";
                            else if (stackItemDamage == 1 && aStack.toString().equals(
                                    Objects.requireNonNull(
                                            GT_ModHandler.getModItem(GalacticraftMars.modID, "item.schematic", 1L, 1))
                                            .copy().toString()))
                                sTier = "101";
                            else if (stackItemDamage == 2 && aStack.toString().equals(
                                    Objects.requireNonNull(
                                            GT_ModHandler.getModItem(GalacticraftMars.modID, "item.schematic", 1L, 2))
                                            .copy().toString()))
                                sTier = "102";
                        } else if (aStack.getUnlocalizedName().matches(".*\\d+.*"))
                            sTier = aStack.getUnlocalizedName().split("(?<=\\D)(?=\\d)")[1].substring(0, 1);
                        else sTier = "1";
                    }

                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, getSpecialSlot());
                    assert this.mOutputItems[0] != null;
                    this.mOutputItems[0].setTagCompound(
                            GT_Utility.getNBTContainingShort(
                                    new NBTTagCompound(),
                                    "rocket_tier",
                                    Short.parseShort(sTier)));

                    calculateOverclockedNess(480, 36000);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
            }
            if (getSpecialSlot() == null && ItemList.Tool_DataStick.isStackEqual(aStack, false, true)) {
                if (GT_Utility.ItemNBT.getBookTitle(aStack).equals("Raw Prospection Data")) {
                    GT_Utility.ItemNBT.setBookTitle(aStack, "Analyzed Prospection Data");
                    GT_Utility.ItemNBT.convertProspectionData(aStack);
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, aStack);
                    calculateOverclockedNess(30, 1000);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
            }
            if (ItemList.Tool_DataStick.isStackEqual(getSpecialSlot(), false, true)) {
                for (GT_Recipe.GT_Recipe_AssemblyLine tRecipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
                    if (GT_Utility.areStacksEqual(tRecipe.mResearchItem, aStack, true)) {
                        boolean failScanner = true;
                        for (GT_Recipe scannerRecipe : sScannerFakeRecipes.mRecipeList) {
                            if (GT_Utility.areStacksEqual(scannerRecipe.mInputs[0], aStack, true)) {
                                failScanner = false;
                                break;
                            }
                        }
                        if (failScanner) {
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        }

                        this.mOutputItems[0] = GT_Utility.copyAmount(1L, getSpecialSlot());

                        // Use Assline Utils
                        if (GT_AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(this.mOutputItems[0], tRecipe)) {
                            aStack.stackSize -= 1;
                            calculateOverclockedNess(30, tRecipe.mResearchTime);
                            // In case recipe is too OP for that machine
                            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                            getSpecialSlot().stackSize -= 1;
                            return 2;
                        }
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime >= (mMaxProgresstime - 1)) {
            if ((this.mOutputItems[0] != null)
                    && (this.mOutputItems[0].getUnlocalizedName().equals("gt.metaitem.01.32707"))) {
                GT_Mod.achievements.issueAchievement(
                        aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                        "scanning");
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return sScannerFakeRecipes;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide,
            ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack)
                && getRecipeList().containsInput(aStack);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }
}
