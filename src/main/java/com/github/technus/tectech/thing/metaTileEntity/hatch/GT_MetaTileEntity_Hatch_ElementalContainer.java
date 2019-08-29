package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.iElementalInstanceContainer;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.thing.metaTileEntity.pipe.IConnectsToElementalPipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import static com.github.technus.tectech.CommonValues.*;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;

/**
 * Created by danie_000 on 11.12.2016.
 */
public abstract class GT_MetaTileEntity_Hatch_ElementalContainer extends GT_MetaTileEntity_Hatch implements iElementalInstanceContainer, IConnectsToElementalPipe {
    private static Textures.BlockIcons.CustomIcon EM_T_SIDES;
    private static Textures.BlockIcons.CustomIcon EM_T_ACTIVE;
    private static Textures.BlockIcons.CustomIcon EM_T_CONN;

    protected cElementalInstanceStackMap content = new cElementalInstanceStackMap();
    //float lifeTimeMult=1f;
    public int postEnergize = 0;
    public float overflowMatter = 0f;
    public short id = -1;
    private byte deathDelay = 2;

    protected GT_MetaTileEntity_Hatch_ElementalContainer(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
        Util.setTier(aTier, this);
    }

    protected GT_MetaTileEntity_Hatch_ElementalContainer(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_T_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_ACTIVE");
        EM_T_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_SIDES");
        EM_T_CONN = new Textures.BlockIcons.CustomIcon("iconsets/EM_PIPE_CONN");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_T_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(EM_T_CONN)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_T_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(EM_T_CONN)};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("postEnergize", postEnergize);
        //aNBT.setFloat("lifeTimeMult",lifeTimeMult);
        aNBT.setFloat("overflowMatter", overflowMatter);
        aNBT.setTag("eM_Stacks", content.toNBT());
        aNBT.setShort("eID", id);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        postEnergize = aNBT.getInteger("postEnergize");
        //lifeTimeMult=aNBT.getFloat("lifeTimeMult");
        overflowMatter = aNBT.getFloat("overflowMatter");
        id = aNBT.getShort("eID");
        try {
            content = cElementalInstanceStackMap.fromNBT(aNBT.getCompoundTag("eM_Stacks"));
        } catch (tElementalException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            if (content == null) {
                content = new cElementalInstanceStackMap();
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (DECAY_AT == Tick) {
                purgeOverflow();
                content.tickContentByOneSecond(1, postEnergize);//Hatches don't life time mult things
                purgeOverflow();
            } else if (OVERFLOW_AT == Tick) {
                if (overflowMatter <= 0) {
                    deathDelay = 3;
                } else {
                    if (deathDelay == 1) {
                        IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(aBaseMetaTileEntity.getBackFacing());
                        if (tGTTileEntity == null || !(tGTTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_OverflowElemental)) {
                            tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide((byte) 0);
                        }
                        if (tGTTileEntity == null || !(tGTTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_OverflowElemental)) {
                            tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide((byte) 1);
                        }
                        if (tGTTileEntity != null && tGTTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_OverflowElemental) {
                            GT_MetaTileEntity_Hatch_OverflowElemental aMetaTileEntity = (GT_MetaTileEntity_Hatch_OverflowElemental) tGTTileEntity.getMetaTileEntity();
                            if (aMetaTileEntity.addOverflowMatter(overflowMatter)) {
                                if (TecTech.configTecTech.BOOM_ENABLE) {
                                    tGTTileEntity.doExplosion(V[14]);
                                } else {
                                    TecTech.anomalyHandler.addAnomaly(aBaseMetaTileEntity, overflowMatter * 32D);
                                    TecTech.proxy.broadcast("Container1 " + StatCollector.translateToLocal("tt.keyword.BOOM") + " " + aBaseMetaTileEntity.getXCoord() + ' ' + aBaseMetaTileEntity.getYCoord() + ' ' + aBaseMetaTileEntity.getZCoord());
                                }
                            }
                            deathDelay = 3;//needed in some cases like repetitive failures. Should be 4 since there is -- at end but meh...
                            overflowMatter = 0F;
                        }
                    } else if (deathDelay < 1) {
                        if (TecTech.configTecTech.BOOM_ENABLE) {
                            aBaseMetaTileEntity.doExplosion(V[14]);
                        } else {
                            TecTech.anomalyHandler.addAnomaly(aBaseMetaTileEntity, overflowMatter * 32D);
                            deathDelay = 3;
                            overflowMatter = 0;
                            TecTech.proxy.broadcast("Container0 " + StatCollector.translateToLocal("tt.keyword.BOOM") + " " + aBaseMetaTileEntity.getXCoord() + ' ' + aBaseMetaTileEntity.getYCoord() + ' ' + aBaseMetaTileEntity.getZCoord());
                        }
                    }
                    deathDelay--;
                }
            } else if (MOVE_AT == Tick) {
                if (content.hasStacks()) {
                    moveAround(aBaseMetaTileEntity);
                }
                getBaseMetaTileEntity().setActive(content.hasStacks());
            }
        }
    }

    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
    }

    @Override
    public cElementalInstanceStackMap getContainerHandler() {
        return content;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
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
    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
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
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    public int getMaxStacksCount() {
        return mTier * 2;
    }

    public int getMaxStackSize() {
        return mTier * (mTier - 7) * 1000;
    }

    @Override
    public void purgeOverflow() {
        overflowMatter += content.removeOverflow(getMaxStacksCount(), getMaxStackSize());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{StatCollector.translateToLocal("tt.keyword.ID")};
        //if (TecTech.configTecTech.EASY_SCAN) {
        //    if (id > 0) {
        //        if (content == null || content.size() == 0) {
        //            return new String[]{StatCollector.translateToLocal("tt.keyword.ID") + ": " + EnumChatFormatting.AQUA + id, StatCollector.translateToLocal("tt.keyphrase.No_Stacks")};
        //        } else {
        //            String[] lines = content.getElementalInfo();
        //            String[] output = new String[lines.length + 1];
        //            output[0] = StatCollector.translateToLocal("tt.keyword.ID") + ": " + EnumChatFormatting.AQUA + id;
        //            System.arraycopy(lines, 0, output, 1, lines.length);
        //            return output;
        //        }
        //    }
        //    if (content == null || content.size() == 0) {
        //        return new String[]{StatCollector.translateToLocal("tt.keyphrase.No_Stacks")};
        //    }
        //    return content.getElementalInfo();
        //} else {
        //    if (id > 0) {
        //        if (content == null || content.size() == 0) {
        //            return new String[]{StatCollector.translateToLocal("tt.keyword.ID") + ": " + EnumChatFormatting.AQUA + id, StatCollector.translateToLocal("tt.keyphrase.No_Stacks")};
        //        }
        //        return new String[]{StatCollector.translateToLocal("tt.keyword.ID") + ": " + EnumChatFormatting.AQUA + id, StatCollector.translateToLocal("tt.keyphrase.Contains_EM")};
        //    }
        //    if (content == null || content.size() == 0) {
        //        return new String[]{StatCollector.translateToLocal("tt.keyphrase.No_Stacks")};
        //    }
        //    return new String[]{StatCollector.translateToLocal("tt.keyphrase.Contains_EM")};
        //}
    }

    public void updateSlots() {
        purgeOverflow();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                TEC_MARK_EM,
                mDescription,
                StatCollector.translateToLocal("tt.base.emhatch.desc.0") + " " + EnumChatFormatting.AQUA + getMaxStacksCount(),//Max stacks amount:
                StatCollector.translateToLocal("tt.base.emhatch.desc.1") + " " + EnumChatFormatting.AQUA + getMaxStackSize(),//Stack capacity:
                StatCollector.translateToLocal("tt.base.emhatch.desc.2"),//Place Overflow Hatch behind,on top or below
                StatCollector.translateToLocal("tt.base.emhatch.desc.3"),//to provide overflow protection while this block
                StatCollector.translateToLocal("tt.base.emhatch.desc.4"),//is not attached to multi block.
                StatCollector.translateToLocal("tt.base.emhatch.desc.5"),//Transport range can be extended in straight
                StatCollector.translateToLocal("tt.base.emhatch.desc.6"),//line up to 15 blocks with quantum tunnels.
                EnumChatFormatting.AQUA + StatCollector.translateToLocal("tt.base.emhatch.desc.7")//Must be painted to work
        };
    }

    @Override
    public void onRemoval() {
        if (isValidMetaTileEntity(this) && getBaseMetaTileEntity().isActive()) {
            TecTech.anomalyHandler.addAnomaly(getBaseMetaTileEntity(), (overflowMatter + content.getMass()) * 16D);
            IGregTechTileEntity base = getBaseMetaTileEntity();
            if (TecTech.configTecTech.BOOM_ENABLE) {
                base.doExplosion(V[15]);
            } else {
                TecTech.proxy.broadcast(StatCollector.translateToLocal("tt.keyword.BOOM") + " " + base.getXCoord() + ' ' + base.getYCoord() + ' ' + base.getZCoord());
            }
        }
    }
}