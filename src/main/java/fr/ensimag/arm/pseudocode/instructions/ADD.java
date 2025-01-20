package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMTernaryInstructionDValToReg;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.ima.pseudocode.*;

public class ADD extends ARMTernaryInstructionDValToReg {
    public ADD(ARMGPRegister op1, DVal op2,  DVal op3) {
        super(op1, op2, op3);
    }
}
