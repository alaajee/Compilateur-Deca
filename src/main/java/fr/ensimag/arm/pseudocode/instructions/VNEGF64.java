package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionVNEGF64;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class VNEGF64 extends ARMBinaryInstructionVNEGF64 {
    
    public VNEGF64(DVal op1, DVal op2) {
        super(op1, op2);
    }
}