package gregtech.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.joml.Vector3d;

import com.github.bsideup.jabel.Desugar;

public final class BillboardRenderHelper {

    private BillboardRenderHelper() {}

    public static void addTexturedQuad(Tessellator tessellator, Plane plane, IIcon icon, double half,
        Vector3d scratch) {
        plane.get(half, half, scratch);
        tessellator.addVertexWithUV(scratch.x, scratch.y, scratch.z, icon.getMaxU(), icon.getMaxV());

        plane.get(half, -half, scratch);
        tessellator.addVertexWithUV(scratch.x, scratch.y, scratch.z, icon.getMaxU(), icon.getMinV());

        plane.get(-half, -half, scratch);
        tessellator.addVertexWithUV(scratch.x, scratch.y, scratch.z, icon.getMinU(), icon.getMinV());

        plane.get(-half, half, scratch);
        tessellator.addVertexWithUV(scratch.x, scratch.y, scratch.z, icon.getMinU(), icon.getMaxV());
    }

    @Desugar
    public record Plane(Vector3d centre, Vector3d s, Vector3d t) {

        public static Plane lookingAt(Vector3d centre, Vector3d pos) {
            Vector3d normal = new Vector3d();
            Vector3d relCentre = new Vector3d();

            relCentre.set(centre)
                .sub(pos);
            relCentre.normalize(normal);

            double radians = Math.atan2(normal.x, normal.z) + Math.PI / 2;

            Vector3d s = new Vector3d(0, 0, -1).rotateY(radians);
            Vector3d t = normal.cross(s, new Vector3d());

            return new Plane(relCentre, s, t);
        }

        public void get(double sk, double tk, Vector3d dest) {
            dest.x = centre.x + s.x * sk + t.x * tk;
            dest.y = centre.y + s.y * sk + t.y * tk;
            dest.z = centre.z + s.z * sk + t.z * tk;
        }
    }
}
