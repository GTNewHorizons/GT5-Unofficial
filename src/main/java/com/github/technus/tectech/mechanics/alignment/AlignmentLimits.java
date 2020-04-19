package com.github.technus.tectech.mechanics.alignment;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.alignment.enumerable.Flip;
import com.github.technus.tectech.mechanics.alignment.enumerable.Rotation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static com.github.technus.tectech.mechanics.alignment.IAlignment.STATES_COUNT;

public class AlignmentLimits implements IAlignmentLimits {

    protected final boolean[] validStates=new boolean[STATES_COUNT];

    public AlignmentLimits() {
        allowAll();
    }

    AlignmentLimits allowAll(){
        Arrays.fill(validStates,true);
        return this;
    }

    AlignmentLimits denyAll(){
        Arrays.fill(validStates,false);
        return this;
    }

    AlignmentLimits randomAll(Random random){
        for (int i = 0; i < validStates.length; i++) {
            validStates[i]=random.nextBoolean();
        }
        return this;
    }

    AlignmentLimits deny(ExtendedFacing... deny){
        if(deny!=null){
            for (ExtendedFacing extendedFacing : deny) {
                validStates[extendedFacing.getIndex()]=false;
            }
        }
        return this;
    }

    AlignmentLimits allow(ExtendedFacing... allow){
        if(allow!=null){
            for (ExtendedFacing extendedFacing : allow) {
                validStates[extendedFacing.getIndex()]=false;
            }
        }
        return this;
    }

    AlignmentLimits deny(ForgeDirection... deny){
        if(deny!=null){
            for (ExtendedFacing value : ExtendedFacing.VALUES) {
                for (ForgeDirection direction : deny) {
                    if (value.getDirection() == direction) {
                        validStates[value.getIndex()] = false;
                        break;
                    }
                }
            }
        }
        return this;
    }

    AlignmentLimits allow(ForgeDirection... allow){
        if(allow!=null){
            for (ExtendedFacing value : ExtendedFacing.VALUES) {
                for (ForgeDirection direction : allow) {
                    if (value.getDirection() == direction) {
                        validStates[value.getIndex()] = true;
                        break;
                    }
                }
            }
        }
        return this;
    }

    AlignmentLimits deny(Rotation... deny){
        if(deny!=null){
            for (ExtendedFacing value : ExtendedFacing.VALUES) {
                for (Rotation rotation : deny) {
                    if (value.getRotation() == rotation) {
                        validStates[value.getIndex()] = false;
                        break;
                    }
                }
            }
        }
        return this;
    }

    AlignmentLimits allow(Rotation... allow){
        if(allow!=null){
            for (ExtendedFacing value : ExtendedFacing.VALUES) {
                for (Rotation rotation : allow) {
                    if (value.getRotation() == rotation) {
                        validStates[value.getIndex()] = true;
                        break;
                    }
                }
            }
        }
        return this;
    }

    AlignmentLimits deny(Flip... deny){
        if(deny!=null){
            for (ExtendedFacing value : ExtendedFacing.VALUES) {
                for (Flip flip : deny) {
                    if (value.getFlip() == flip) {
                        validStates[value.getIndex()] = false;
                        break;
                    }
                }
            }
        }
        return this;
    }

    AlignmentLimits allow(Flip... allow){
        if(allow!=null){
            for (ExtendedFacing value : ExtendedFacing.VALUES) {
                for (Flip flip : allow) {
                    if (value.getFlip() == flip) {
                        validStates[value.getIndex()] = true;
                        break;
                    }
                }
            }
        }
        return this;
    }

    AlignmentLimits predicateApply(Function<ExtendedFacing,Optional<Boolean>> predicate){
        for (ExtendedFacing value : ExtendedFacing.VALUES) {
            predicate.apply(value).ifPresent(bool->validStates[value.getIndex()]=bool);
        }
        return this;
    }

    AlignmentLimits ensureDuplicates(){
        for (ExtendedFacing value : ExtendedFacing.VALUES) {
            if(validStates[value.getIndex()]){
                validStates[value.getDuplicate().getIndex()]=true;
            }
        }
        return this;
    }

    /**
     * Prefers rotation over flip, so both flip will get translated to opposite rotation and no flip
     * @param flip the preferred flip to be used Horizontal or vertical
     * @return this
     */
    AlignmentLimits ensureNoDuplicates(Flip flip){
        if(flip==Flip.BOTH||flip==Flip.NONE){
            throw new IllegalArgumentException("Preffered Flip must be Horizontal or Vertical");
        }
        flip=flip.getOpposite();
        for (ExtendedFacing value : ExtendedFacing.VALUES) {
            if(validStates[value.getIndex()]){
                if(value.getFlip()==Flip.BOTH || value.getFlip()==flip){
                    validStates[value.getIndex()]=false;
                    validStates[value.getDuplicate().getIndex()]=true;
                }
            }
        }
        return this;
    }

    @Override
    public boolean isNewExtendedFacingValid(ForgeDirection direction, Rotation rotation, Flip flip) {
        return validStates[IAlignment.getAlignmentIndex(direction,rotation,flip)];
    }
}
