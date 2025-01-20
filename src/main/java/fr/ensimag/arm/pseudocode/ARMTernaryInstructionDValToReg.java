package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.*;

public class ARMTernaryInstructionDValToReg extends ARMTernaryInstruction {
    public ARMTernaryInstructionDValToReg(DVal op1, DVal op2, ARMGPRegister op3) {
        super(op1, op2, op3);
    }

    public ARMTernaryInstructionDValToReg(ARMGPRegister op1, DVal op2,  DVal op3) {
        super(op1, op2, op3);
    }
}

