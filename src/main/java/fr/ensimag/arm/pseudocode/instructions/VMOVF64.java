package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionCond;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class VMOVF64 extends ARMBinaryInstructionCond {
    
    public VMOVF64(DVal op1, DVal op2) {
        super(op1, op2);
    }
}