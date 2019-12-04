package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.recipe.TT_recipe;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Holder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IHatchAdder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.CommonValues.VN;
import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.recipe.TT_recipe.E_RECIPE_ID;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_crafting.crafter;
import static com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine.machine;
import static gregtech.api.enums.GT_Values.E;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_research extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_Holder> eHolders = new ArrayList<>();
    private GT_Recipe.GT_Recipe_AssemblyLine tRecipe;
    private TT_recipe.TT_assLineRecipe aRecipe;
    private String machineType;
    private ItemStack holdItem;
    private long computationRemaining, computationRequired;

    private static LinkedHashMap<String, String> lServerNames;

    private String clientLocale = "en_US";
    //endregion

    //region structure
    private static final String[][] shape = new String[][]{
            {E, "000", E, E, E, "000"/*,E,*/},
            {"A0", "010", "A1", "A!", "A1", "010", "A0",},
            {"A0", "010", E, E, E, "010", "A0",},
            {"000", "010", E, E, E, "010", "000",},
            {"000", "212", "010", "0.0", "010", "212", "000",},
            {"000", "212", "111", "111", "111", "212", "000",},
            {"000", "222", "   ", "   ", "   ", "222", "000",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{1, 3, 2};
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addClassicToMachineList, this::addHolderToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset + 1, textureOffset + 3};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, Blocks.air};
    private static final byte[] blockMetaFallback = new byte[]{1, 0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.research.hint.0"),//1 - Classic/Data Hatches or Computer casing
            translateToLocal("gt.blockmachines.multimachine.em.research.hint.1"),//2 - Holder Hatch
    };
    //endregion

    public GT_MetaTileEntity_EM_research(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_research(String aName) {
        super(aName);
    }

    private void makeStick() {
        mInventory[1].setTagCompound(new NBTTagCompound());
        mInventory[1].setStackDisplayName(GT_LanguageManager.getTranslation(tRecipe.mOutput.getDisplayName()) + " Construction Data");
        GT_Utility.ItemNBT.setBookTitle(mInventory[1], GT_LanguageManager.getTranslation(tRecipe.mOutput.getDisplayName()) + " Construction Data");
        NBTTagCompound tNBT = mInventory[1].getTagCompound();//code above makes it not null

        tNBT.setTag("output", tRecipe.mOutput.writeToNBT(new NBTTagCompound()));
        tNBT.setInteger("time", tRecipe.mDuration);
        tNBT.setInteger("eu", tRecipe.mEUt);
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            tNBT.setTag(String.valueOf(i), tRecipe.mInputs[i].writeToNBT(new NBTTagCompound()));
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            tNBT.setTag("f" + i, tRecipe.mFluidInputs[i].writeToNBT(new NBTTagCompound()));
        }
        tNBT.setString("author", EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech" + EnumChatFormatting.WHITE + " Assembling Line Recipe Generator");
        NBTTagList tNBTList = new NBTTagList();
        tNBTList.appendTag(new NBTTagString("Construction plan for " + tRecipe.mOutput.stackSize + ' ' + GT_LanguageManager.getTranslation(tRecipe.mOutput.getDisplayName()) + ". Needed EU/t: " + tRecipe.mEUt + " Production time: " + tRecipe.mDuration / 20));
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            if (tRecipe.mInputs[i] != null) {
                tNBTList.appendTag(new NBTTagString("Input Bus " + (i + 1) + ": " + tRecipe.mInputs[i].stackSize + ' ' + GT_LanguageManager.getTranslation(tRecipe.mInputs[i].getDisplayName())));
            }
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            if (tRecipe.mFluidInputs[i] != null) {
                tNBTList.appendTag(new NBTTagString("Input Hatch " + (i + 1) + ": " + tRecipe.mFluidInputs[i].amount + "L " + GT_LanguageManager.getTranslation(tRecipe.mFluidInputs[i].getLocalizedName())));
            }
        }
        tNBT.setTag("pages", tNBTList);
    }

    static {
        try {
            Class GT_Assemblyline_Server = Class.forName("gregtech.api.util.GT_Assemblyline_Server");
            lServerNames = (LinkedHashMap<String, String>) GT_Assemblyline_Server.getField("lServerNames").get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            lServerNames = null;
        }
    }

    private void makeStick2() {
        String s = tRecipe.mOutput.getDisplayName();
        if (getBaseMetaTileEntity().isServerSide()) {
            if(lServerNames != null) {
                s = lServerNames.get(tRecipe.mOutput.getDisplayName());
                if (s == null) {
                    s = tRecipe.mOutput.getDisplayName();
                }
            }else{
                s = tRecipe.mOutput.getDisplayName();
            }
        }
        mInventory[1].setTagCompound(new NBTTagCompound());
        mInventory[1].setStackDisplayName(s + " Construction Data");
        GT_Utility.ItemNBT.setBookTitle(mInventory[1], s + " Construction Data");

        NBTTagCompound tNBT = mInventory[1].getTagCompound();

        tNBT.setTag("output", tRecipe.mOutput.writeToNBT(new NBTTagCompound()));
        tNBT.setInteger("time", tRecipe.mDuration);
        tNBT.setInteger("eu", tRecipe.mEUt);
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            tNBT.setTag("" + i, tRecipe.mInputs[i].writeToNBT(new NBTTagCompound()));
        }
        for (int i = 0; i < tRecipe.mOreDictAlt.length; i++) {
            if (tRecipe.mOreDictAlt[i] != null && tRecipe.mOreDictAlt[i].length > 0) {
                tNBT.setInteger("a" + i, tRecipe.mOreDictAlt[i].length);
                for (int j = 0; j < tRecipe.mOreDictAlt[i].length; j++) {
                    tNBT.setTag("a" + i + ":" + j, tRecipe.mOreDictAlt[i][j].writeToNBT(new NBTTagCompound()));
                }
            }
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            tNBT.setTag("f" + i, tRecipe.mFluidInputs[i].writeToNBT(new NBTTagCompound()));
        }
        tNBT.setString("author", EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech" + EnumChatFormatting.WHITE + ' ' + machineType + " Recipe Generator");
        NBTTagList tNBTList = new NBTTagList();
        s = tRecipe.mOutput.getDisplayName();
        if (getBaseMetaTileEntity().isServerSide()) {
            s = lServerNames.get(tRecipe.mOutput.getDisplayName());
            if (s == null) {
                s = tRecipe.mOutput.getDisplayName();
            }
        }
        tNBTList.appendTag(new NBTTagString("Construction plan for " + tRecipe.mOutput.stackSize + " " + s + ". Needed EU/t: " + tRecipe.mEUt + " Production time: " + (tRecipe.mDuration / 20)));
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            if (tRecipe.mOreDictAlt[i] != null) {
                int count = 0;
                StringBuilder tBuilder = new StringBuilder("Input Bus " + (i + 1) + ": ");
                for (ItemStack tStack : tRecipe.mOreDictAlt[i]) {
                    if (tStack != null) {
                        s = tStack.getDisplayName();
                        if (getBaseMetaTileEntity().isServerSide()) {
                            s = lServerNames.get(tStack.getDisplayName());
                            if (s == null)
                                s = tStack.getDisplayName();
                        }


                        tBuilder.append(count == 0 ? "" : "\nOr ").append(tStack.stackSize).append(" ").append(s);
                        count++;
                    }
                }
                if (count > 0) tNBTList.appendTag(new NBTTagString(tBuilder.toString()));
            } else if (tRecipe.mInputs[i] != null) {
                s = tRecipe.mInputs[i].getDisplayName();
                if (getBaseMetaTileEntity().isServerSide()) {
                    s = lServerNames.get(tRecipe.mInputs[i].getDisplayName());
                    if (s == null) {
                        s = tRecipe.mInputs[i].getDisplayName();
                    }
                }
                tNBTList.appendTag(new NBTTagString("Input Bus " + (i + 1) + ": " + tRecipe.mInputs[i].stackSize + " " + s));
            }
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            if (tRecipe.mFluidInputs[i] != null) {
                s = tRecipe.mFluidInputs[i].getLocalizedName();
                if (getBaseMetaTileEntity().isServerSide()) {
                    s = lServerNames.get(tRecipe.mFluidInputs[i].getLocalizedName());
                    if (s == null) {
                        s = tRecipe.mFluidInputs[i].getLocalizedName();
                    }
                }
                tNBTList.appendTag(new NBTTagString("Input Hatch " + (i + 1) + ": " + tRecipe.mFluidInputs[i].amount + "L " + s));
            }
        }
        tNBT.setTag("pages", tNBTList);

        mInventory[1].setTagCompound(tNBT);
    }

    private boolean iterateRecipes() {
        for (GT_Recipe ttRecipe : TT_recipe.GT_Recipe_MapTT.sResearchableFakeRecipes.mRecipeList) {
            if (GT_Utility.areStacksEqual(ttRecipe.mInputs[0], holdItem, true)) {
                computationRequired = computationRemaining = ttRecipe.mDuration * 20L;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                eRequiredData = (short) (ttRecipe.mSpecialValue >>> 16);
                eAmpereFlow = (short) (ttRecipe.mSpecialValue & 0xFFFF);
                mEUt = ttRecipe.mEUt;
                eHolders.get(0).getBaseMetaTileEntity().setActive(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_research(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Holder rack : eHolders) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(false);
            }
        }
        eHolders.clear();

        if (!structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 3, 4)) {
            return false;
        }

        for (GT_MetaTileEntity_Hatch_Holder rack : eHolders) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
            }
        }
        return eHolders.size() == 1;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        tRecipe = null;
        aRecipe = null;
        if (!eHolders.isEmpty() && eHolders.get(0).mInventory[0] != null) {
            holdItem = eHolders.get(0).mInventory[0].copy();
            if (ItemList.Tool_DataStick.isStackEqual(itemStack, false, true)) {
                for (GT_Recipe.GT_Recipe_AssemblyLine assRecipe : TT_recipe.GT_Recipe_MapTT.sAssemblylineRecipes) {
                    if (GT_Utility.areStacksEqual(assRecipe.mResearchItem, holdItem, true)) {
                        tRecipe = assRecipe;
                        //if found
                        if (iterateRecipes()) return true;
                    }
                }
            } else if (ItemList.Tool_DataOrb.isStackEqual(itemStack, false, true)) {
                for (TT_recipe.TT_assLineRecipe assRecipeTT : TT_recipe.TT_Recipe_Map.sMachineRecipes.recipeList()) {
                    if (GT_Utility.areStacksEqual(assRecipeTT.mResearchItem, holdItem, true)) {
                        aRecipe = assRecipeTT;
                        machineType = machine;
                        //if found
                        if (iterateRecipes()) return true;
                    }
                }
                for (TT_recipe.TT_assLineRecipe assRecipeTT : TT_recipe.TT_Recipe_Map.sCrafterRecipes.recipeList()) {
                    if (GT_Utility.areStacksEqual(assRecipeTT.mResearchItem, holdItem, true)) {
                        aRecipe = assRecipeTT;
                        machineType = crafter;
                        //if found
                        if (iterateRecipes()) return true;
                    }
                }
            }
        }
        holdItem = null;
        computationRequired = computationRemaining = 0;
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity().setActive(false);
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eHolders.isEmpty()) {
            if (tRecipe != null && ItemList.Tool_DataStick.isStackEqual(mInventory[1], false, true)) {
                eHolders.get(0).getBaseMetaTileEntity().setActive(false);
                eHolders.get(0).mInventory[0] = null;
                if (lServerNames == null) {
                    makeStick();
                } else {
                    try {
                        makeStick2();
                    } catch (NoSuchFieldError e) {
                        makeStick();
                    }
                }
            } else if (aRecipe != null && ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {
                eHolders.get(0).getBaseMetaTileEntity().setActive(false);
                eHolders.get(0).mInventory[0] = null;

                mInventory[1].setStackDisplayName(GT_LanguageManager.getTranslation(aRecipe.mOutputs[0].getDisplayName()) + ' ' + machineType + " Construction Data");
                NBTTagCompound tNBT = mInventory[1].getTagCompound();//code above makes it not null

                tNBT.setString("eMachineType", machineType);
                GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(aRecipe.mOutputs[0].getItem());
                tNBT.setString(E_RECIPE_ID, uid + ":" + aRecipe.mOutputs[0].getItemDamage());
                tNBT.setString("author", EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech" + EnumChatFormatting.WHITE + ' ' + machineType + " Recipe Generator");
            }
        }
        computationRequired = computationRemaining = 0;
        tRecipe = null;
        aRecipe = null;
        holdItem = null;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.research.desc.0"),//Philosophers didn't even...
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.research.desc.0")//dream about it!
        };
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET + " EU",
                (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": " : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ") +
                        EnumChatFormatting.RED + Math.abs(mEUt) + EnumChatFormatting.RESET + " EU/t " + translateToLocalFormatted("tt.keyword.at", clientLocale) + " " +
                        EnumChatFormatting.RED + eAmpereFlow + EnumChatFormatting.RESET + " A",
                translateToLocalFormatted("tt.keyphrase.Tier_Rating", clientLocale) + ": " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier_EM()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier_EM()] + EnumChatFormatting.RESET +
                        " " + translateToLocalFormatted("tt.keyphrase.Amp_Rating", clientLocale) + ": " + EnumChatFormatting.GREEN + eMaxAmpereFlow + EnumChatFormatting.RESET + " A",
                translateToLocalFormatted("tt.keyword.Problems", clientLocale) + ": " + EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " " + translateToLocalFormatted("tt.keyword.Efficiency", clientLocale) + ": " + EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                translateToLocalFormatted("tt.keyword.PowerPass", clientLocale) + ": " + EnumChatFormatting.BLUE + ePowerPass + EnumChatFormatting.RESET +
                        " " + translateToLocalFormatted("tt.keyword.SafeVoid", clientLocale) + ": " + EnumChatFormatting.BLUE + eSafeVoid,
                translateToLocalFormatted("tt.keyphrase.Computation_Available", clientLocale) + ": " + EnumChatFormatting.GREEN + eAvailableData + EnumChatFormatting.RESET + " / " + EnumChatFormatting.YELLOW + eRequiredData + EnumChatFormatting.RESET,
                translateToLocalFormatted("tt.keyphrase.Computation_Remaining", clientLocale) + ":",
                EnumChatFormatting.GREEN + Long.toString(computationRemaining / 20L) + EnumChatFormatting.RESET + " / " +
                        EnumChatFormatting.YELLOW + computationRequired / 20L
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][3], new TT_RenderedTexture(aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][3]};
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eHolders) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eComputationRemaining", computationRemaining);
        aNBT.setLong("eComputationRequired", computationRequired);
        if (holdItem != null) {
            aNBT.setTag("eHold", holdItem.writeToNBT(new NBTTagCompound()));
        } else {
            aNBT.removeTag("eHold");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        computationRemaining = aNBT.getLong("eComputationRemaining");
        computationRequired = aNBT.getLong("eComputationRequired");
        if (aNBT.hasKey("eHold")) {
            holdItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("eHold"));
        } else {
            holdItem = null;
        }
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity().setActive(false);
        }
        computationRequired = computationRemaining = 0;
        tRecipe = null;
        aRecipe = null;
        holdItem = null;
    }

    //@Override
    //public boolean isFacingValid(byte aFacing) {
    //    return aFacing >= 2;
    //}

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0) {
                aRecipe = null;
                tRecipe = null;
                if (holdItem != null) {
                    if (ItemList.Tool_DataStick.isStackEqual(mInventory[1], false, true)) {
                        for (GT_Recipe.GT_Recipe_AssemblyLine tRecipe : TT_recipe.GT_Recipe_MapTT.sAssemblylineRecipes) {
                            if (GT_Utility.areStacksEqual(tRecipe.mResearchItem, holdItem, true)) {
                                this.tRecipe = tRecipe;
                                break;
                            }
                        }
                    } else if (ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {
                        for (TT_recipe.TT_assLineRecipe assRecipeTT : TT_recipe.TT_Recipe_Map.sMachineRecipes.recipeList()) {
                            if (GT_Utility.areStacksEqual(assRecipeTT.mResearchItem, holdItem, true)) {
                                aRecipe = assRecipeTT;
                                machineType = machine;
                                break;
                            }
                        }
                        if (aRecipe == null) {
                            for (TT_recipe.TT_assLineRecipe assRecipeTT : TT_recipe.TT_Recipe_Map.sCrafterRecipes.recipeList()) {
                                if (GT_Utility.areStacksEqual(assRecipeTT.mResearchItem, holdItem, true)) {
                                    aRecipe = assRecipeTT;
                                    machineType = crafter;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (tRecipe == null && aRecipe == null) {
                    holdItem = null;
                    computationRequired = computationRemaining = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                    for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
                        r.getBaseMetaTileEntity().setActive(false);
                    }
                }
            }
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (computationRemaining <= 0) {
            computationRemaining = 0;
            mProgresstime = mMaxProgresstime;
            return true;
        } else {
            computationRemaining -= eAvailableData;
            mProgresstime = 1;
            return super.onRunningTick(aStack);
        }
    }

    public final boolean addHolderToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Holder) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eHolders.add((GT_MetaTileEntity_Hatch_Holder) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        super.onRightclick(aBaseMetaTileEntity, aPlayer);

        if (!aBaseMetaTileEntity.isClientSide() && aPlayer instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
        } else {
            return true;
        }
        System.out.println(clientLocale);
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta, 1, 3, 4, getBaseMetaTileEntity(), this, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }
}
