package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionDValToReg;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.ima.pseudocode.*;

public class NEG extends ARMBinaryInstructionDValToReg {
    public NEG(DVal op1, ARMGPRegister op2) {
        super(op1, op2);
    }
    public NEG(DVal op1, DVal op2) {
        super(op1, op2);
    }
}