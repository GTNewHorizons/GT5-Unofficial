package gregtech.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.tileentities.render.TileEntityBlackhole;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockBlackholeRenderer extends Block {
        public BlockBlackholeRenderer() {
            super(Material.iron);
            this.setResistance(20f);
            this.setHardness(-1.0f);
            this.setBlockName("BlackHoleRenderer");
            this.setLightLevel(100.0f);
            GameRegistry.registerBlock(this, getUnlocalizedName());
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerBlockIcons(IIconRegister iconRegister) {
            blockIcon = iconRegister.registerIcon("gregtech:iconsets/TRANSPARENT");
        }

        @Override
        public String getUnlocalizedName() {
            return "gt.blackholerenderer";
        }

        @Override
        public boolean isOpaqueCube() {
            return false;
        }

        @Override
        public boolean canRenderInPass(int a) {
            return true;
        }

        @Override
        public boolean renderAsNormalBlock() {
            return false;
        }

        @Override
        public boolean hasTileEntity(int metadata) {
            return true;
        }

        @Override
        public TileEntity createTileEntity(World world, int metadata) {
            return new TileEntityBlackhole();
        }

        @Override
        public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
            return new ArrayList<>();
        }

        @Override
        public boolean isCollidable() {
            return true;
        }

    }
