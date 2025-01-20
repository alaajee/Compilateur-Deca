package fr.ensimag.arm.pseudocode;

import fr.ensimag.arm.pseudocode.ARMImmediateString;

public abstract class ARMUnaryInstructionImmStr extends ARMUnaryInstruction {

    protected ARMUnaryInstructionImmStr(ARMImmediateString operand) {
        super(operand);
    }

    protected ARMUnaryInstructionImmStr(String i) {
        super(new ARMImmediateString(i));
    }

}
