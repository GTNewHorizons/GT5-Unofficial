package gregtech.common.tileentities.machines.basic;

import cpw.mods.fml.common.FMLLog;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class GT_MetaTileEntity_Scanner
        extends GT_MetaTileEntity_BasicMachine {
	
    public GT_MetaTileEntity_Scanner(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "Scans Crops and other things.", 1, 1, "Scanner.png", "", new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_SCANNER), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_SCANNER), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_SCANNER), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER)});
    }

    public GT_MetaTileEntity_Scanner(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_Scanner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Scanner(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    public int checkRecipe() {
        ItemStack aStack = getInputAt(0);
        if (getOutputAt(0) != null) {
            this.mOutputBlocked += 1;
        } else if ((GT_Utility.isStackValid(aStack)) && (aStack.stackSize > 0)) {
            if ((getFillableStack() != null) && (getFillableStack().containsFluid(Materials.Honey.getFluid(100L)))) {
                try {
                    Object tIndividual = AlleleManager.alleleRegistry.getIndividual(aStack);
                    if (tIndividual != null) {
                        if (((IIndividual) tIndividual).analyze()) {
                            getFillableStack().amount -= 100;
                            this.mOutputItems[0] = GT_Utility.copy(new Object[]{aStack});
                            aStack.stackSize = 0;
                            NBTTagCompound tNBT = new NBTTagCompound();
                            ((IIndividual) tIndividual).writeToNBT(tNBT);
                            this.mOutputItems[0].setTagCompound(tNBT);
                            calculateOverclockedNess(2,500);
                            //In case recipe is too OP for that machine
                            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                            return 2;
                        }
                        this.mOutputItems[0] = GT_Utility.copy(new Object[]{aStack});
                        aStack.stackSize = 0;
                        this.mMaxProgresstime = 1;
                        this.mEUt = 1;
                        return 2;
                    }
                } catch (Throwable e) {
                    if (GT_Values.D1) {
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
                    calculateOverclockedNess(8,160);
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                } else {
                    this.mMaxProgresstime = 1;
                    this.mEUt = 1;
                }
                aStack.stackSize -= 1;
                this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{aStack});
                this.mOutputItems[0].setTagCompound(tNBT);
                return 2;
            }
            if (ItemList.Tool_DataOrb.isStackEqual(getSpecialSlot(), false, true)) {
                if (ItemList.Tool_DataOrb.isStackEqual(aStack, false, true)) {
                    aStack.stackSize -= 1;
                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{ItemList.Tool_DataStick.get(1, new Object[]{})});
                    calculateOverclockedNess(30,512);
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                ItemData tData = GT_OreDictUnificator.getAssociation(aStack);
                if ((tData != null) && ((tData.mPrefix == OrePrefixes.dust) || (tData.mPrefix == OrePrefixes.cell)) && (tData.mMaterial.mMaterial.mElement != null) && (!tData.mMaterial.mMaterial.mElement.mIsIsotope) && (tData.mMaterial.mMaterial != Materials.Magic) && (tData.mMaterial.mMaterial.getMass() > 0L)) {
                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = ItemList.Tool_DataOrb.get(1L, new Object[0]);
                    Behaviour_DataOrb.setDataTitle(this.mOutputItems[0], "Elemental-Scan");
                    Behaviour_DataOrb.setDataName(this.mOutputItems[0], tData.mMaterial.mMaterial.mElement.name());
                    calculateOverclockedNess(30,GT_Utility.safeInt(tData.mMaterial.mMaterial.getMass() * 8192L));
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
            }
            if (ItemList.Tool_DataStick.isStackEqual(getSpecialSlot(), false, true)) {
                if (ItemList.Tool_DataStick.isStackEqual(aStack, false, true)) {
                    aStack.stackSize -= 1;
                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{getSpecialSlot()});
                    calculateOverclockedNess(30,128);
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                if (aStack.getItem() == Items.written_book) {
                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{getSpecialSlot()});
                    this.mOutputItems[0].setTagCompound(aStack.getTagCompound());
                    calculateOverclockedNess(30,128);
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                if (aStack.getItem() == Items.filled_map) {
                    getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{getSpecialSlot()});
                    this.mOutputItems[0].setTagCompound(GT_Utility.getNBTContainingShort(new NBTTagCompound(), "map_id", (short) aStack.getItemDamage()));
                    calculateOverclockedNess(30,128);
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;
                }
                
                if ((aStack.getItem().getUnlocalizedName().contains("Schematic")||aStack.getItem().getUnlocalizedName().contains("schematic"))&&!aStack.getItem().getUnlocalizedName().contains("Schematics")){
                	String sTier ="";
                	
                	if (aStack.getItem().getIdFromItem(aStack.getItem()) == GT_ModHandler.getModItem("GalacticraftCore", "item.schematic", 1L, 0).getItem().getIdFromItem(GT_ModHandler.getModItem("GalacticraftCore", "item.schematic", 1L, 0).getItem())) {
                		if (aStack.getItemDamage()==0 && aStack.toString().equals(GT_ModHandler.getModItem("GalacticraftCore", "item.schematic", 1L, 0).copy().toString())) 
                			sTier = "100";
                		else if (aStack.getItemDamage()==1 && aStack.toString().equals(GT_ModHandler.getModItem("GalacticraftCore", "item.schematic", 1L, 1).copy().toString())) 
                			sTier = "2";
                	}
                	
                	else if (aStack.getItem().getIdFromItem(aStack.getItem()) == GT_ModHandler.getModItem("GalacticraftMars", "item.schematic", 1L, 0).getItem().getIdFromItem(GT_ModHandler.getModItem("GalacticraftMars", "item.schematic", 1L, 0).getItem())) {
                		if(aStack.getItemDamage()==0 && aStack.toString().equals(GT_ModHandler.getModItem("GalacticraftMars", "item.schematic", 1L, 0).copy().toString()))
                			sTier = "3";
                		else if(aStack.getItemDamage()==1 && aStack.toString().equals(GT_ModHandler.getModItem("GalacticraftMars", "item.schematic", 1L, 1).copy().toString())) 
                			sTier = "101";
                		else if(aStack.getItemDamage()==2 && aStack.toString().equals(GT_ModHandler.getModItem("GalacticraftMars", "item.schematic", 1L, 2).copy().toString())) 
                			sTier = "102";
                	}
                	
                	else if (aStack.getUnlocalizedName().matches(".*\\d+.*"))
                		sTier = aStack.getUnlocalizedName().split("(?<=\\D)(?=\\d)")[1].substring(0, 1);
                	else
                		sTier = "1";
                	
                	getSpecialSlot().stackSize -= 1;
                    aStack.stackSize -= 1;
                	
                	this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{getSpecialSlot()});
                	this.mOutputItems[0].setTagCompound(GT_Utility.getNBTContainingShort(new NBTTagCompound(), "rocket_tier", Short.parseShort(sTier) ));
                	
                	calculateOverclockedNess(480,36000);
                    //In case recipe is too OP for that machine
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

                    this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{aStack});
                    calculateOverclockedNess(30,1000);
                    //In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    return 2;

                }
            }
            if(ItemList.Tool_DataStick.isStackEqual(getSpecialSlot(), false, true)&& aStack !=null){
            	for(GT_Recipe.GT_Recipe_AssemblyLine tRecipe:GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes){
            	if(GT_Utility.areStacksEqual(tRecipe.mResearchItem, aStack, true)){
            	
            	String s = tRecipe.mOutput.getDisplayName();
            		
            	this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{getSpecialSlot()});
                GT_Utility.ItemNBT.setBookTitle(this.mOutputItems[0], s+" Construction Data");
                
                NBTTagCompound tNBT = this.mOutputItems[0].getTagCompound();
                if (tNBT == null) {
                    tNBT = new NBTTagCompound();
                }     
                tNBT.setTag("output", tRecipe.mOutput.writeToNBT(new NBTTagCompound()));
                tNBT.setInteger("time", tRecipe.mDuration);
                tNBT.setInteger("eu", tRecipe.mEUt);
                for(int i = 0 ; i < tRecipe.mInputs.length ; i++){
                	tNBT.setTag(""+i, tRecipe.mInputs[i].writeToNBT(new NBTTagCompound()));
                }
                for(int i = 0 ; i < tRecipe.mOreDictAlt.length ; i++){
                	if (tRecipe.mOreDictAlt[i] != null && tRecipe.mOreDictAlt[i].length > 0) {
                		tNBT.setInteger("a" + i, tRecipe.mOreDictAlt[i].length);
                		for (int j = 0; j < tRecipe.mOreDictAlt[i].length; j++) {
                			tNBT.setTag("a" + i + ":" + j, tRecipe.mOreDictAlt[i][j].writeToNBT(new NBTTagCompound()));
                		}
                	}
                }
                for(int i = 0 ; i < tRecipe.mFluidInputs.length ; i++){
                	tNBT.setTag("f"+i, tRecipe.mFluidInputs[i].writeToNBT(new NBTTagCompound()));
                }
                tNBT.setString("author", "Assembling Line Recipe Generator");
                NBTTagList tNBTList = new NBTTagList();
                s=tRecipe.mOutput.getDisplayName();
                tNBTList.appendTag(new NBTTagString("Construction plan for "+tRecipe.mOutput.stackSize+" "+s+". Needed EU/t: "+tRecipe.mEUt+" Production time: "+(tRecipe.mDuration/20)));
                for(int i=0;i<tRecipe.mInputs.length;i++){
                	if (tRecipe.mOreDictAlt[i] != null) {
                		int count = 0;
                		StringBuilder tBuilder = new StringBuilder("Input Bus "+(i+1)+": ");
                		for (ItemStack tStack : tRecipe.mOreDictAlt[i]) {
                			if (tStack != null) {
                				s=tStack.getDisplayName();
                				tBuilder.append((count == 0 ? "" : "\nOr ") + tStack.stackSize+" "+s);
                    			count++;
                			}
                		}
                		if (count > 0) tNBTList.appendTag(new NBTTagString(tBuilder.toString()));
                	} else if(tRecipe.mInputs[i]!=null){
                		s=tRecipe.mInputs[i].getDisplayName();
                		tNBTList.appendTag(new NBTTagString("Input Bus "+(i+1)+": "+tRecipe.mInputs[i].stackSize+" "+s));
                	}
                }
                for(int i=0;i<tRecipe.mFluidInputs.length;i++){
                	if(tRecipe.mFluidInputs[i]!=null){
                		s=tRecipe.mFluidInputs[i].getLocalizedName();
                		tNBTList.appendTag(new NBTTagString("Input Hatch "+(i+1)+": "+tRecipe.mFluidInputs[i].amount+"L "+s));
                	}
                }
                tNBT.setTag("pages", tNBTList);
                
                this.mOutputItems[0].setTagCompound(tNBT);
                
                aStack.stackSize -= 1;
                calculateOverclockedNess(30,tRecipe.mResearchTime);
                //In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
            	getSpecialSlot().stackSize -= 1;
            	return 2;
            	}
            	}
            }

        }
        return 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime >= (mMaxProgresstime - 1)) {
            if ((this.mOutputItems[0] != null) && (this.mOutputItems[0].getUnlocalizedName().equals("gt.metaitem.01.32707"))) {
                GT_Mod.instance.achievements.issueAchievement(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "scanning");
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }


    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes;
    }

    public int getCapacity() {
        return 1000;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (getRecipeList().containsInput(aStack));
    }

    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(212)), 10, 1.0F, aX, aY, aZ);
        }
    }

    public void startProcess() {
        sendLoopStart((byte) 1);
    }
}
