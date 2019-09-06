package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_MetaTileEntity_Boiler_Bronze
        extends GT_MetaTileEntity_Boiler {


    public GT_MetaTileEntity_Boiler_Bronze(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[]{
                "An early way to get Steam Power",
                "Produces 120L of Steam per second",
                "Causes 20 Pollution per second"});
    }

    public GT_MetaTileEntity_Boiler_Bronze(int aID, String aName, String aNameRegional, String[] aDescription) {
        super(aID, aName, aNameRegional, aDescription);
    }

    public GT_MetaTileEntity_Boiler_Bronze(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Bronze(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][(i + 1)] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM, Dyes.getModulation(i, Dyes._NULL.mRGBa))};
            rTextures[1][(i + 1)] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP, Dyes.getModulation(i, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE)};
            rTextures[2][(i + 1)] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE, Dyes.getModulation(i, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE)};
            rTextures[3][(i + 1)] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE, Dyes.getModulation(i, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT)};
            rTextures[4][(i + 1)] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE, Dyes.getModulation(i, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT_ACTIVE)};
        }
        return rTextures;
    }

    public int maxProgresstime() {
        return 500;
    }

    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Boiler(aPlayerInventory, aBaseMetaTileEntity, 16000);
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "BronzeBoiler.png", 16000);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Bronze(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        singleBlockBoilerLogic(aBaseMetaTileEntity,aTick,1,45,25L,20);
    }

    protected void singleBlockBoilerLogic(IGregTechTileEntity aBaseMetaTileEntity, long aTick, int aMultiplier, int aTimer, long aTickDivider, int aPollution){
        if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)) {
            if (this.mTemperature <= 20) {
                this.mTemperature = 20;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > aTimer) {
                this.mTemperature -= 1;
                this.mLossTimer = 0;
            }
            for (byte i = 1; (this.mSteam != null) && (i < 6); i = (byte) (i + 1)) {
                if (i != aBaseMetaTileEntity.getFrontFacing()) {
                    IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(i);
                    if (tTileEntity != null) {
                        FluidStack tDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), Math.max(1, this.mSteam.amount / 2), false);
                        if (tDrained != null) {
                            int tFilledAmount = tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), tDrained, false);
                            if (tFilledAmount > 0) {
                                tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), tFilledAmount, true), true);
                            }
                        }
                    }
                }
            }
            if (aTick % aTickDivider == 0L) {
                if (this.mTemperature > 100) {
                    if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
                        this.mHadNoWater = true;
                    } else {
                        if (this.mHadNoWater) {
                            GT_Log.exp.println("Boiler "+this.mName+" had no Water!");
                            aBaseMetaTileEntity.doExplosion(2048L);
                            return;
                        }
                        this.mFluid.amount -= 1;
                        if (this.mSteam == null) {
                            this.mSteam = GT_ModHandler.getSteam(150L);
                        } else if (GT_ModHandler.isSteam(this.mSteam)) {
                            this.mSteam.amount += 150;
                        } else {
                            this.mSteam = GT_ModHandler.getSteam(150L);
                        }
                    }
                } else {
                    this.mHadNoWater = false;
                }
            }
            if ((this.mSteam != null) &&
                    (this.mSteam.amount > (16000*aMultiplier))) {
                sendSound((byte) 1);
                this.mSteam.amount = (12000*aMultiplier);
            }
            if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) &&
                    (this.mInventory[2] != null)) {
                if (
                        (GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Coal) && !GT_Utility.isPartOfOrePrefix(this.mInventory[2],OrePrefixes.block)) ||
                        (GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Charcoal) && !GT_Utility.isPartOfOrePrefix(this.mInventory[2],OrePrefixes.block)) ||
                        (GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Lignite) && !GT_Utility.isPartOfOrePrefix(this.mInventory[2],OrePrefixes.block)) ||
                        (GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Diamond) && !GT_Utility.isPartOfOrePrefix(this.mInventory[2],OrePrefixes.block)) ||
                        GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelCoke")
                ) {
                    if ((TileEntityFurnace.getItemBurnTime(this.mInventory[2])/10) > 0) {
                        this.mProcessingEnergy += (TileEntityFurnace.getItemBurnTime(this.mInventory[2])/10);
                        aBaseMetaTileEntity.decrStackSize(2, 1);
                        if (XSTR.XSTR_INSTANCE.nextInt(GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Coal) || GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Charcoal) ? 3 : GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Lignite) ? 8 : 2 ) == 0) {
                            aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, (GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Lignite) || GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Coal)) ? Materials.DarkAsh : Materials.Ash, 1L));
                        }
                    }
                }
                else if (
                        //If its a block of the following materials
                        GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Coal)) ||
                        GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Lignite)) ||
                        GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Charcoal))||
                        GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Diamond)) ||

                         //if its either a Railcraft Coke Block or a custom GTNH compressed Coal/charcoal/lignite/coke block
                        (
                         Block.getBlockFromItem(this.mInventory[2].getItem()) != null && //check if the block exists
                        (
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("tile") && //check if the block is a tile -> block
                        (
                         //If the name of the block contains these names
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("charcoal") ||
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("coal") ||
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("diamond") ||
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("coke") ||
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("railcraft.cube") ||
                         Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("lignite")
                        )
                        )
                        )
                ){
                    //try to add 10% of the burnvalue as Processing energy, no boost for coal coke here
                    if ((TileEntityFurnace.getItemBurnTime(this.mInventory[2])/10) > 0) {
                        this.mProcessingEnergy += (TileEntityFurnace.getItemBurnTime(this.mInventory[2]) / 10);
                        aBaseMetaTileEntity.decrStackSize(2, 1);
                        aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dust, (GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Lignite) || GT_Utility.isPartOfMaterials(this.mInventory[2],Materials.Coal) || Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("coal") || Block.getBlockFromItem(this.mInventory[2].getItem()).getUnlocalizedName().toLowerCase().contains("lignite") ) ? Materials.DarkAsh : Materials.Ash, 1L));
                    }
                    //enables every other fuel with at least 2000 burntime as a fuel, i.e. peat, Magic/Solid Super Fuel, Coal Singularities, Nitor, while bucket of creosite should be blocked same goes for lava
                }else if ((TileEntityFurnace.getItemBurnTime(this.mInventory[2])) >= 2000 && !(this.mInventory[2].getUnlocalizedName().toLowerCase().contains("bucket") || this.mInventory[2].getUnlocalizedName().toLowerCase().contains("cell"))){
                    this.mProcessingEnergy += (TileEntityFurnace.getItemBurnTime(this.mInventory[2]) / 10);
                    aBaseMetaTileEntity.decrStackSize(2, 1);
                    //adds tiny pile of ash for burntime under 10k, small pile for under 100k and pile for bigger values
                    if (XSTR.XSTR_INSTANCE.nextInt(2) == 0)
                        aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get( (TileEntityFurnace.getItemBurnTime(this.mInventory[2]) >= 10000 ? TileEntityFurnace.getItemBurnTime(this.mInventory[2]) >= 100000 ? OrePrefixes.dust : OrePrefixes.dustSmall : OrePrefixes.dustTiny), Materials.Ash, 1L));
                }
            }
            if ((this.mTemperature < (500*aMultiplier)) && (this.mProcessingEnergy > 0) && (aTick % 12L == 0L)) {
                this.mProcessingEnergy -= aMultiplier;
                this.mTemperature += 1;
            }
            if (this.mProcessingEnergy > 0 && (aTick % 20L == 0L)) {
                GT_Pollution.addPollution(getBaseMetaTileEntity(), aPollution);
            }
            aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
        }
    }

}
