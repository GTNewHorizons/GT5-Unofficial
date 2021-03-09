package gregtech.api.metatileentity.implementations;

import gregtech.GT_Mod;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.GT_Pollution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

public class GT_MetaTileEntity_Hatch_Muffler extends GT_MetaTileEntity_Hatch {
    public GT_MetaTileEntity_Hatch_Muffler(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
    }

    public GT_MetaTileEntity_Hatch_Muffler(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Muffler(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    private int[] mFacings;

    /*private void init()*/ {
        setInValidFacings(ForgeDirection.DOWN);
    }

    @Override
    public String[] getDescription() {
        int pollutionReduction = calculatePollutionReduction(100);
        return new String[]{
                "Outputs the Pollution (Might cause ... things)",
                "DO NOT OBSTRUCT THE OUTPUT!",
                "Reduces Pollution to " + pollutionReduction + "%",
                "Recovers " + (100 - pollutionReduction) + "% of CO2/CO/SO2"
        };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_MUFFLER)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_MUFFLER)};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    //API Code, Bartworks relies on this. It's marked here, not anywhere else.
    public void setInValidFacings(ForgeDirection... aFacings) {
        mFacings = Arrays.stream(aFacings).mapToInt(Enum::ordinal).toArray();
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        boolean valid = true;
        for (int x : mFacings) {
            if (x == aFacing) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Muffler(mName, mTier, mDescriptionArray, mTextures);
    }

    public boolean polluteEnvironment(MetaTileEntity mte) {
        boolean doPollute = false;
        if (getBaseMetaTileEntity().getAirAtSide(getBaseMetaTileEntity().getFrontFacing())) {
            GT_Pollution.addPollution(getBaseMetaTileEntity(), calculatePollutionReduction(10000));
            doPollute = true;
        }
        return doPollute;
    }

    public int calculatePollutionReduction(int aPollution) {
        int pollutionReduction;
        if ((float) mTier < 2) {
            pollutionReduction = aPollution;
        } else {
            pollutionReduction = (int) ((float) aPollution * ((100F - (12.5F * ((float) mTier - 1F))) / 100F));
        }
        return pollutionReduction;
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide() && this.getBaseMetaTileEntity().isActive()) {
            pollutionParticles(this.getBaseMetaTileEntity().getWorld(), "largesmoke");
        }
    }

    public void pollutionParticles(World aWorld, String name) {
        boolean chk1, chk2, chk3;
        float ran1 = XSTR_INSTANCE.nextFloat(), ran2 = 0, ran3 = 0;
        chk1 = ran1 * 100 < calculatePollutionReduction(100);
        if (GT_Pollution.getPollution(getBaseMetaTileEntity()) >= GT_Mod.gregtechproxy.mPollutionSmogLimit) {
            ran2 = XSTR_INSTANCE.nextFloat();
            ran3 = XSTR_INSTANCE.nextFloat();
            chk2 = ran2 * 100 < calculatePollutionReduction(100);
            chk3 = ran3 * 100 < calculatePollutionReduction(100);
            if (!(chk1 || chk2 || chk3)) return;
        } else {
            if (!chk1) return;
            chk2 = chk3 = false;
        }

        IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
        ForgeDirection aDir = ForgeDirection.getOrientation(aMuffler.getFrontFacing());
        float xPos = aDir.offsetX * 0.76F + aMuffler.getXCoord() + 0.25F;
        float yPos = aDir.offsetY * 0.76F + aMuffler.getYCoord() + 0.25F;
        float zPos = aDir.offsetZ * 0.76F + aMuffler.getZCoord() + 0.25F;

        float ySpd = aDir.offsetY * 0.1F + 0.2F + 0.1F * XSTR_INSTANCE.nextFloat();
        float xSpd;
        float zSpd;

        if (aDir.offsetY == -1) {
            float temp = XSTR_INSTANCE.nextFloat() * 2 * (float) Math.PI;
            xSpd = (float) Math.sin(temp) * 0.1F;
            zSpd = (float) Math.cos(temp) * 0.1F;
        } else {
            xSpd = aDir.offsetX * (0.1F + 0.2F * XSTR_INSTANCE.nextFloat());
            zSpd = aDir.offsetZ * (0.1F + 0.2F * XSTR_INSTANCE.nextFloat());
        }

        if (chk1)
            aWorld.spawnParticle(name, xPos + ran1 * 0.5F, yPos + XSTR_INSTANCE.nextFloat() * 0.5F, zPos + XSTR_INSTANCE.nextFloat() * 0.5F, xSpd, ySpd, zSpd);

        if (chk2)
            aWorld.spawnParticle(name, xPos + ran2 * 0.5F, yPos + XSTR_INSTANCE.nextFloat() * 0.5F, zPos + XSTR_INSTANCE.nextFloat() * 0.5F, xSpd, ySpd, zSpd);

        if (chk3)
            aWorld.spawnParticle(name, xPos + ran3 * 0.5F, yPos + XSTR_INSTANCE.nextFloat() * 0.5F, zPos + XSTR_INSTANCE.nextFloat() * 0.5F, xSpd, ySpd, zSpd);
    }
}
