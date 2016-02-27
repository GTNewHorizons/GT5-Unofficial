package crazypants.enderio.conduit.gas;

import crazypants.enderio.EnderIO;
import crazypants.enderio.conduit.BlockConduitBundle;
import crazypants.enderio.conduit.ConnectionMode;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.enderio.conduit.geom.CollidableComponent;
import crazypants.enderio.conduit.geom.ConnectionModeGeometry;
import crazypants.enderio.conduit.geom.Offset;
import crazypants.enderio.conduit.render.ConduitBundleRenderer;
import crazypants.enderio.conduit.render.DefaultConduitRenderer;
import crazypants.render.BoundingBox;
import crazypants.render.CubeRenderer;
import crazypants.render.RenderUtil;
import crazypants.vecmath.Vector2f;
import crazypants.vecmath.Vector3d;
import crazypants.vecmath.Vertex;
import java.util.List;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class GasConduitRenderer
  extends DefaultConduitRenderer
{
  public boolean isRendererForConduit(IConduit conduit)
  {
    return conduit instanceof GasConduit;
  }
  
  public void renderEntity(ConduitBundleRenderer conduitBundleRenderer, IConduitBundle te, IConduit conduit, double x, double y, double z, float partialTick, float worldLight, RenderBlocks rb)
  {
    super.renderEntity(conduitBundleRenderer, te, conduit, x, y, z, partialTick, worldLight, rb);
    if ((!conduit.hasConnectionMode(ConnectionMode.INPUT)) && (!conduit.hasConnectionMode(ConnectionMode.OUTPUT))) {
      return;
    }
    GasConduit pc = (GasConduit)conduit;
    for (ForgeDirection dir : conduit.getExternalConnections())
    {
      IIcon tex = null;
      if (conduit.getConnectionMode(dir) == ConnectionMode.INPUT) {
        tex = pc.getTextureForInputMode();
      } else if (conduit.getConnectionMode(dir) == ConnectionMode.OUTPUT) {
        tex = pc.getTextureForOutputMode();
      }
      if (tex != null)
      {
        Offset offset = te.getOffset(IGasConduit.class, dir);
        ConnectionModeGeometry.renderModeConnector(dir, offset, tex, true);
      }
    }
  }
  
  protected void renderConduit(IIcon tex, IConduit conduit, CollidableComponent component, float brightness)
  {
    super.renderConduit(tex, conduit, component, brightness);
    if (isNSEWUD(component.dir))
    {
      GasConduit lc = (GasConduit)conduit;
      
      GasStack gas = lc.getGasType();
      IIcon texture = null;
      if (gas != null) {
        texture = gas.getGas().getIcon();
      }
      if (texture == null) {
        texture = lc.getNotSetEdgeTexture();
      }
      float scaleFactor = 0.75F;
      float xLen = Math.abs(component.dir.offsetX) == 1 ? 1.0F : scaleFactor;
      float yLen = Math.abs(component.dir.offsetY) == 1 ? 1.0F : scaleFactor;
      float zLen = Math.abs(component.dir.offsetZ) == 1 ? 1.0F : scaleFactor;
      
      BoundingBox cube = component.bound;
      BoundingBox bb = cube.scale(xLen, yLen, zLen);
      for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
        if ((d != component.dir) && (d != component.dir.getOpposite()))
        {
          ForgeDirection vDir = RenderUtil.getVDirForFace(d);
          if ((component.dir == ForgeDirection.UP) || (component.dir == ForgeDirection.DOWN)) {
            vDir = RenderUtil.getUDirForFace(d);
          } else if (((component.dir == ForgeDirection.NORTH) || (component.dir == ForgeDirection.SOUTH)) && (d.offsetY != 0)) {
            vDir = RenderUtil.getUDirForFace(d);
          }
          float minU = texture.getMinU();
          float maxU = texture.getMaxU();
          float minV = texture.getMinV();
          float maxV = texture.getMaxV();
          
          float sideScale = Math.max(bb.sizeX(), bb.sizeY()) * 2.0F / 16.0F;
          sideScale = Math.max(sideScale, bb.sizeZ() * 2.0F / 16.0F);
          float width = Math.min(bb.sizeX(), bb.sizeY()) * 15.0F / 16.0F;
          
          List<Vertex> corners = bb.getCornersWithUvForFace(d, minU, maxU, minV, maxV);
          moveEdgeCorners(corners, vDir, width);
          moveEdgeCorners(corners, component.dir.getOpposite(), sideScale);
          for (Vertex c : corners) {
            CubeRenderer.addVecWithUV(c.xyz, c.uv.x, c.uv.y);
          }
          corners = bb.getCornersWithUvForFace(d, minU, maxU, minV, maxV);
          moveEdgeCorners(corners, vDir.getOpposite(), width);
          moveEdgeCorners(corners, component.dir.getOpposite(), sideScale);
          for (Vertex c : corners) {
            CubeRenderer.addVecWithUV(c.xyz, c.uv.x, c.uv.y);
          }
        }
      }
      if (conduit.getConnectionMode(component.dir) == ConnectionMode.DISABLED)
      {
        tex = EnderIO.blockConduitBundle.getConnectorIcon(component.data);
        List<Vertex> corners = component.bound.getCornersWithUvForFace(component.dir, tex.getMinU(), tex.getMaxU(), tex.getMinV(), tex.getMaxV());
        Tessellator tessellator = Tessellator.instance;
        for (Vertex c : corners) {
          CubeRenderer.addVecWithUV(c.xyz, c.uv.x, c.uv.y);
        }
        for (int i = corners.size() - 1; i >= 0; i--)
        {
          Vertex c = (Vertex)corners.get(i);
          CubeRenderer.addVecWithUV(c.xyz, c.uv.x, c.uv.y);
        }
      }
    }
  }
  
  protected void renderTransmission(IConduit conduit, IIcon tex, CollidableComponent component, float selfIllum)
  {
    super.renderTransmission(conduit, tex, component, selfIllum);
  }
  
  private void moveEdgeCorners(List<Vertex> vertices, ForgeDirection edge, float scaleFactor)
  {
    int[] indices = getClosest(edge, vertices);
    ((Vertex)vertices.get(indices[0])).xyz.x -= scaleFactor * edge.offsetX;
    ((Vertex)vertices.get(indices[1])).xyz.x -= scaleFactor * edge.offsetX;
    ((Vertex)vertices.get(indices[0])).xyz.y -= scaleFactor * edge.offsetY;
    ((Vertex)vertices.get(indices[1])).xyz.y -= scaleFactor * edge.offsetY;
    ((Vertex)vertices.get(indices[0])).xyz.z -= scaleFactor * edge.offsetZ;
    ((Vertex)vertices.get(indices[1])).xyz.z -= scaleFactor * edge.offsetZ;
  }
  
  private int[] getClosest(ForgeDirection edge, List<Vertex> vertices)
  {
    int[] res = { -1, -1 };
    boolean highest = (edge.offsetX > 0) || (edge.offsetY > 0) || (edge.offsetZ > 0);
    double minMax = highest ? -1.797693134862316E+308D : 1.7976931348623157E+308D;
    int index = 0;
    for (Vertex v : vertices)
    {
      double val = get(v.xyz, edge);
      if (highest ? val >= minMax : val <= minMax)
      {
        if (val != minMax) {
          res[0] = index;
        } else {
          res[1] = index;
        }
        minMax = val;
      }
      index++;
    }
    return res;
  }
  
  private double get(Vector3d xyz, ForgeDirection edge)
  {
    if ((edge == ForgeDirection.EAST) || (edge == ForgeDirection.WEST)) {
      return xyz.x;
    }
    if ((edge == ForgeDirection.UP) || (edge == ForgeDirection.DOWN)) {
      return xyz.y;
    }
    return xyz.z;
  }
}
