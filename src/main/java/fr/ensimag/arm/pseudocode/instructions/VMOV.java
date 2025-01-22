package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMTernaryInstructionDValToReg;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.ima.pseudocode.*;

public class VMOV extends ARMTernaryInstructionDValToReg {
    public VMOV(DVal op1, DVal op2, ARMGPRegister op3) {
        super(op1, op2, op3);
    }
    public VMOV(DVal op1, DVal op2){
        super(op1, op2);
    }
}
