package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.*;

public abstract class ARMUnaryInstructionImmInt extends ARMUnaryInstruction {

    protected ARMUnaryInstructionImmInt(ARMImmediateInteger operand) {
        super(operand);
    }

    protected ARMUnaryInstructionImmInt(int i) {
        super(new ARMImmediateInteger(i));
    }

}
