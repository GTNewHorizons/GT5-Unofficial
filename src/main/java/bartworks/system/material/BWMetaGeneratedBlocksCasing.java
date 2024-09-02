/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;

public class BWMetaGeneratedBlocksCasing extends BWMetaGeneratedBlocks
    implements com.gtnewhorizon.structurelib.structure.ICustomBlockSetting {

    public BWMetaGeneratedBlocksCasing(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName,
        OrePrefixes prefixes) {
        super(p_i45386_1_, tileEntity, blockName, prefixes);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public float getExplosionResistance(Entity aTNT) {
        return Blocks.iron_block.getExplosionResistance(aTNT);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetaData);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
    }

    @Override
    protected void doRegistrationStuff(Werkstoff tMaterial) {
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public String getUnlocalizedName() {
        if (this._prefixes == OrePrefixes.blockCasing) return "bw.werkstoffblockscasing.01";
        if (this._prefixes == OrePrefixes.blockCasingAdvanced) return "bw.werkstoffblockscasingadvanced.01";
        return "";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        Werkstoff.werkstoffHashSet.stream()
            .filter(
                pMaterial -> Werkstoff.Types.BIOLOGICAL.equals(pMaterial.getType())
                    && pMaterial.hasGenerationFeature(OrePrefixes.blockCasing)
                    || pMaterial.doesOreDictedItemExists(OrePrefixes.plate)
                        && pMaterial.doesOreDictedItemExists(OrePrefixes.screw)
                        && pMaterial.doesOreDictedItemExists(OrePrefixes.plateDouble)
                        && pMaterial.doesOreDictedItemExists(OrePrefixes.gearGt)
                        && pMaterial.doesOreDictedItemExists(OrePrefixes.gearGtSmall))
            .map(pMaterial -> new ItemStack(aItem, 1, pMaterial.getmID()))
            .forEach(aList::add);
    }

    /**
     * ICustomBlockSetting setBlock override
     */
    public void setBlock(World world, int x, int y, int z, int meta) {
        world.setBlock(x, y, z, this, meta, 2);
        try {
            Thread.sleep(1);
            // Fucking Minecraft TE settings.
        } catch (InterruptedException ignored) {}
        Optional.ofNullable(world.getTileEntity(x, y, z))
            .filter(te -> te instanceof TileEntityMetaGeneratedBlock)
            .map(te -> (TileEntityMetaGeneratedBlock) te)
            .ifPresent(te -> te.mMetaData = (short) meta);
    }
}
