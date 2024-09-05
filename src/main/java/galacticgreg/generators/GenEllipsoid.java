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
 *
 * Generates a simple ellipsoid with dynamic rotation, random size-values, random noise for the surface, etc. Can
 * probably be tweaked even more, but works for now...
 */
public class GenEllipsoid implements ISpaceObjectGenerator {

    private Vec3 _mEllipsoidCenter;
    private Random _mRandom;
    private List<StructureInformation> _mStructure;

    private double _mCoreDensity = 0.7D;
    private double _mSineFactor = 0.05D;
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
        int Xmin = (int) (_mEllipsoidCenter.xCoord - _mSizeA);
        int Xmax = (int) (_mEllipsoidCenter.xCoord + _mSizeA);
        int Ymin = (int) (_mEllipsoidCenter.yCoord - _mSizeB);
        int Ymax = (int) (_mEllipsoidCenter.yCoord + _mSizeB);
        int Zmin = (int) (_mEllipsoidCenter.zCoord - _mSizeC);
        int Zmax = (int) (_mEllipsoidCenter.zCoord + _mSizeC);

        for (int iX = Xmin; iX <= Xmax; iX++) {
            for (int iY = Ymin; iY <= Ymax; iY++) {
                for (int iZ = Zmin; iZ <= Zmax; iZ++) {
                    double tmpX = Math.pow(_mEllipsoidCenter.xCoord - iX, 2) / Math.pow(_mSizeA, 2);
                    double tmpY = Math.pow(_mEllipsoidCenter.yCoord - iY, 2) / Math.pow(_mSizeB, 2);
                    double tmpZ = Math.pow(_mEllipsoidCenter.zCoord - iZ, 2) / Math.pow(_mSizeC, 2);
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
