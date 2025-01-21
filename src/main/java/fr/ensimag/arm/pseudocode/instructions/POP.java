package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.arm.pseudocode.ARMUnaryInstructionPopPush;

public class POP extends ARMUnaryInstructionPopPush {
    public POP(ARMGPRegister reg) {
        super(reg);
    }

}