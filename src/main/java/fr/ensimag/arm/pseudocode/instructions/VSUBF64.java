package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.arm.pseudocode.ARMTernaryInstructionVSUBF64;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class VSUBF64 extends ARMTernaryInstructionVSUBF64 {
    
    public VSUBF64(ARMGPRegister op1, DVal op2,  DVal op3) {
        super(op1, op2, op3);
    }
}