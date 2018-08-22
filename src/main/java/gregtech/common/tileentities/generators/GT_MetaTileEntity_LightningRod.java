package gregtech.common.tileentities.generators;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GT_MetaTileEntity_LightningRod extends GT_MetaTileEntity_TieredMachineBlock {
    public GT_MetaTileEntity_LightningRod(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Generates EU From Lightning Bolts");
    }

    public GT_MetaTileEntity_LightningRod(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }
    
    public GT_MetaTileEntity_LightningRod(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1],aSide==1?(aActive ? new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW) : new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS)):Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier]};
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LightningRod(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        World aWorld = aBaseMetaTileEntity.getWorld();
        XSTR aXSTR = new XSTR();
        if (!aWorld.isRemote) {
        	if(aBaseMetaTileEntity.getStoredEU()>0){
                aBaseMetaTileEntity.setActive(true);
        	    aBaseMetaTileEntity.decreaseStoredEnergyUnits(aBaseMetaTileEntity.getStoredEU()/100+1, false);
            }else {
                aBaseMetaTileEntity.setActive(false);
            }
        	
            if (aTick % 256 == 0 && (aWorld.isThundering() || (aWorld.isRaining() && aXSTR.nextInt(10) == 0))) {
                int aRodValue = 0;
                boolean isRodValid = true;
                int aX = aBaseMetaTileEntity.getXCoord();
                int aY = aBaseMetaTileEntity.getYCoord();
                int aZ = aBaseMetaTileEntity.getZCoord();

                for (int i = aBaseMetaTileEntity.getYCoord() + 1; i < aWorld.getHeight()-1; i++) {
                    if (isRodValid && aBaseMetaTileEntity.getBlock(aX, i, aZ).getUnlocalizedName().equals("blockFenceIron")) {
                        aRodValue++;
                    } else {
                        isRodValid = false;
                        if (aBaseMetaTileEntity.getBlock(aX, i, aZ) != Blocks.air) {
                            aRodValue=0;
                            break;
                        }
                    }
                }
                if (!aWorld.isThundering() && ((aY + aRodValue) < 128)) aRodValue = 0;
                if (aXSTR.nextInt(4 * aWorld.getHeight()) < (aRodValue * (aY + aRodValue))) {
                    aBaseMetaTileEntity.increaseStoredEnergyUnits(maxEUStore() - aBaseMetaTileEntity.getStoredEU(), false);
                    aWorld.addWeatherEffect(new EntityLightningBolt(aWorld, aX, aY + aRodValue, aZ));
                }
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing==1;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 50000000;
    }

    @Override
    public long maxEUOutput() {
        return GT_Values.V[mTier];
    }

    @Override
    public long maxAmperesOut() {
        return 512;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
    }
}