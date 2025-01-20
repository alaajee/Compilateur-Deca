package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMImmediateInteger;
import fr.ensimag.arm.pseudocode.ARMUnaryInstructionImmInt;

public class SVC extends ARMUnaryInstructionImmInt {
    public SVC(ARMImmediateInteger i) {
        super(i);
    }

    public SVC(int i) {
        super(i);
    }
}