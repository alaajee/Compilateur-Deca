package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.arm.pseudocode.ARMUnaryInstructionPopPush;

public class PUSH extends ARMUnaryInstructionPopPush {
    public PUSH(ARMGPRegister reg) {
        super(reg);
    }

}