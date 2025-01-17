package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionDValToReg;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.arm.pseudocode.ARMImmediateString;
import fr.ensimag.ima.pseudocode.*;

public class LDR extends ARMBinaryInstructionDValToReg {
    public LDR(DVal op1, ARMGPRegister op2) {
        super(op1, op2);
    }
    public LDR(DVal op1, DVal op2) {
        super(op1, op2);
    }
    public LDR(DVal op1, ARMImmediateString op2) {
        super(op1, op2);
    }
}