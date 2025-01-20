package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionDValToReg;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.ima.pseudocode.*;

public class VSTR extends ARMBinaryInstructionDValToReg {
    public VSTR(DVal op1, ARMGPRegister op2) {
        super(op1, op2);
    }
    public VSTR(DVal op1, DVal op2) {
        super(op1, op2);
    }
    public VSTR(DVal op1, ARMImmediateString op2) {
        super(op1, op2);
    }
}