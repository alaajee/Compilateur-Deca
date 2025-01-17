package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.*;

public class ARMBinaryInstructionDAddrToReg extends ARMBinaryInstructionDValToReg {

    public ARMBinaryInstructionDAddrToReg(DAddr op1, ARMGPRegister op2) {
        super(op1, op2);
    }

}
