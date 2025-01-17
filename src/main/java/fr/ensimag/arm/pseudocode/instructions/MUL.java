package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMTernaryInstructionDValToReg;
import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.ima.pseudocode.*;

public class MUL extends ARMTernaryInstructionDValToReg {
    public MUL(DVal op1, DVal op2, ARMGPRegister op3) {
        super(op1, op2, op3);
    }
}
