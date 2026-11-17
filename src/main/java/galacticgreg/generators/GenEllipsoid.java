package galacticgreg.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import galacticgreg.api.Enums.SpaceObjectType;
import galacticgreg.api.Enums.TargetBlockPosition;
import galacticgreg.api.ISpaceObjectGenerator;
import galacticgreg.api.StructureInformation;

/**
 * Simple ellipsoid-generator. Based on the formular created by Chrysator. Thanks for the help!
 * <p>
 * Generates a simple ellipsoid with dynamic rotation, random size-values, random noise for the surface, etc. Can
 * probably be tweaked even more, but works for now...
 */
public class GenEllipsoid implements ISpaceObjectGenerator {

    private Vec3 _mEllipsoidCenter;
    private Random _mRandom;
    private List<StructureInformation> _mStructure;

    private final double _mCoreDensity = 0.7D;
    private final double _mSineFactor = 0.05D;
    private float _mRandomInfluence;
    private float _mRandomAngleX;
    private float _mRandomAngleY;
    private float _mRandomAngleZ;

    private int _mSizeA;
    private int _mSizeB;
    private int _mSizeC;

    public GenEllipsoid() {
        reset();
    }

    @Override
    public SpaceObjectType getType() {
        return SpaceObjectType.OreAsteroid;
    }

    @Override
    public void randomize(int pSizeMin, int pSizeMax) {
        _mRandom = new Random(System.currentTimeMillis());
        _mRandomAngleX = (float) (_mRandom.nextFloat() * Math.PI);
        _mRandomAngleY = (float) (_mRandom.nextFloat() * Math.PI);
        _mRandomAngleZ = (float) (_mRandom.nextFloat() * Math.PI);

        _mRandomInfluence = _mRandom.nextFloat();

        _mSizeA = pSizeMin + _mRandom.nextInt(pSizeMax - pSizeMin) + 10;
        _mSizeB = pSizeMin + _mRandom.nextInt(pSizeMax - pSizeMin) + 10;
        _mSizeC = pSizeMin + _mRandom.nextInt(pSizeMax - pSizeMin) + 10;
    }

    @Override
    public void setCenterPoint(int pX, int pY, int pZ) {
        _mEllipsoidCenter = Vec3.createVectorHelper(pX, pY, pZ);
    }

    @Override
    public void setCenterPoint(Vec3 pCenter) {
        _mEllipsoidCenter = pCenter;
    }

    @Override
    public Vec3 getCenterPoint() {
        return _mEllipsoidCenter;
    }

    @Override
    public List<StructureInformation> getStructure() {
        return _mStructure;
    }

    @Override
    public void calculate() {
        int xMin = (int) (_mEllipsoidCenter.xCoord - _mSizeA);
        int xMax = (int) (_mEllipsoidCenter.xCoord + _mSizeA);
        int yMin = (int) (_mEllipsoidCenter.yCoord - _mSizeB);
        int yMax = (int) (_mEllipsoidCenter.yCoord + _mSizeB);
        int zMin = (int) (_mEllipsoidCenter.zCoord - _mSizeC);
        int zMax = (int) (_mEllipsoidCenter.zCoord + _mSizeC);

        for (int iX = xMin; iX <= xMax; iX++) {
            for (int iY = yMin; iY <= yMax; iY++) {
                for (int iZ = zMin; iZ <= zMax; iZ++) {
                    double dx = _mEllipsoidCenter.xCoord - iX;
                    double dy = _mEllipsoidCenter.yCoord - iY;
                    double dz = _mEllipsoidCenter.zCoord - iZ;

                    double tmpX = dx * dx / (_mSizeA * _mSizeA);
                    double tmpY = dy * dy / (_mSizeB * _mSizeB);
                    double tmpZ = dz * dz / (_mSizeC * _mSizeC);

                    double val = (tmpX + tmpY + tmpZ);

                    Vec3 tPoint = Vec3.createVectorHelper(iX, iY, iZ);
                    tPoint.rotateAroundX(_mRandomAngleX);
                    tPoint.rotateAroundY(_mRandomAngleY);
                    tPoint.rotateAroundZ(_mRandomAngleZ);

                    TargetBlockPosition tbp = TargetBlockPosition.Invalid;

                    if (val <= 0.01D) tbp = TargetBlockPosition.AsteroidInnerCore;

                    else if (val > 0.01D && val < _mCoreDensity) tbp = TargetBlockPosition.AsteroidCore;

                    else if (val >= _mCoreDensity && val <= (1.0D
                        - (_mSineFactor * MathHelper.sin((iZ + iX + iY - _mRandom.nextFloat() * _mRandomInfluence)))))
                        tbp = TargetBlockPosition.AsteroidShell;

                    if (tbp != TargetBlockPosition.Invalid)
                        _mStructure.add(new StructureInformation(Vec3.createVectorHelper(iX, iY, iZ), tbp));

                }
            }
        }
    }

    @Override
    public void reset() {
        _mStructure = new ArrayList<>();
        _mEllipsoidCenter = Vec3.createVectorHelper(0, 0, 0);
    }
}
