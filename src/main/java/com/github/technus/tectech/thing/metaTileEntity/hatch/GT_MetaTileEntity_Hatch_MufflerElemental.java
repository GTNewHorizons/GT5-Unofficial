package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.machineTT;
import com.github.technus.tectech.elementalMatter.commonValues;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;

import static com.github.technus.tectech.elementalMatter.commonValues.disperseAt;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;

/**
 * Created by danie_000 on 12.12.2016.
 */
public class GT_MetaTileEntity_Hatch_MufflerElemental extends GT_MetaTileEntity_Hatch implements machineTT {
    private static Textures.BlockIcons.CustomIcon EM_T_SIDES;
    private static Textures.BlockIcons.CustomIcon EM_T_ACTIVE;
    private static Textures.BlockIcons.CustomIcon MufflerEM;
    public float overflowMatter = 0f;
    public final float overflowMax;
    public final float overflowDisperse;

    public GT_MetaTileEntity_Hatch_MufflerElemental(int aID, String aName, String aNameRegional, int aTier, float max) {
        super(aID, aName, aNameRegional, aTier, 0, "Disposes excess elemental Matter");
        overflowMatter = max / 2;
        overflowMax = max;
        overflowDisperse = overflowMax / (float) (30 - aTier);
    }

    public GT_MetaTileEntity_Hatch_MufflerElemental(String aName, int aTier, float max, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        overflowMatter = max / 2;
        overflowMax = max;
        overflowDisperse = overflowMax / (float) (30 - aTier);
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_T_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_ACTIVE");
        EM_T_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_SIDES");
        MufflerEM = new Textures.BlockIcons.CustomIcon("iconsets/MUFFLER_EM");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_T_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(MufflerEM)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_T_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_MUFFLER)};
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                mDescription,
                "Mass capacity: " + EnumChatFormatting.AQUA + String.format(java.util.Locale.ENGLISH, "%+.2E", overflowMax) + " eV/c^2",
                "Disposal Speed: " + EnumChatFormatting.AQUA + String.format(java.util.Locale.ENGLISH, "%+.2E", overflowDisperse) + " (eV/c^2)/s",
                "DO NOT OBSTRUCT THE OUTPUT!"
        };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
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
        return new GT_MetaTileEntity_Hatch_MufflerElemental(mName, mTier, overflowMax, mDescription, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("overflowMatter", overflowMatter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        overflowMatter = aNBT.getFloat("overflowMatter");
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
        if (aBaseMetaTileEntity.isServerSide() && (aTick % 20) == disperseAt) {
            if (aBaseMetaTileEntity.isActive()) {
                overflowMatter -= overflowDisperse;
                if (overflowMatter < 0) {
                    overflowMatter = 0;
                    aBaseMetaTileEntity.setActive(false);
                    aBaseMetaTileEntity.setLightValue((byte) 0);
                    aBaseMetaTileEntity.getWorld().updateLightByType(EnumSkyBlock.Block, aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord());
                }
            } else {
                if (overflowMatter > 0) {
                    aBaseMetaTileEntity.setActive(true);
                    aBaseMetaTileEntity.setLightValue((byte) 15);
                    aBaseMetaTileEntity.getWorld().updateLightByType(EnumSkyBlock.Block, aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord());
                }
            }
        } else if (aBaseMetaTileEntity.isClientSide() && getBaseMetaTileEntity().isActive()) {
            TecTech.proxy.particles(getBaseMetaTileEntity(), getBaseMetaTileEntity().getFrontFacing());
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
        //DOES NOT CHECK FOR TOO MUCH, it is done only while putting stuff in (OPTIMIZATION!!!)
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                "Contained mass:",
                EnumChatFormatting.RED + Double.toString(overflowMatter) + EnumChatFormatting.RESET + " eV/c^2 /",
                EnumChatFormatting.GREEN + Double.toString(overflowMax) + EnumChatFormatting.RESET + " eV/c^2",
                "Mass Disposal speed: " + EnumChatFormatting.BLUE + Double.toString(overflowDisperse) + EnumChatFormatting.RESET + " (eV/c^2)/s"
        };
    }

    @Override
    public void onRemoval() {
        if (isValidMetaTileEntity(this) && getBaseMetaTileEntity().isActive())
            if (TecTech.ModConfig.BOOM_ENABLE) getBaseMetaTileEntity().doExplosion(V[15]);
            else
                TecTech.proxy.broadcast("Muffler BOOM! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
    }
}
