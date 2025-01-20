package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.*;

public class ARMBinaryInstructionDValToReg extends ARMBinaryInstruction {
    public ARMBinaryInstructionDValToReg(DVal op1, ARMGPRegister op2) {
        super(op1, op2);
    }
    public ARMBinaryInstructionDValToReg(DVal op1, DVal op2) {
        super(op1, op2);
    }
    public ARMBinaryInstructionDValToReg(DVal op1, ARMImmediateString op2) {
        super(op1, op2);
    }
    public ARMBinaryInstructionDValToReg(ARMGPRegister op1, DVal op2) {
        super(op1, op2);
    }
}
