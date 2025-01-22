package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionVCVTF64S32;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class VCVTF64S32 extends ARMBinaryInstructionVCVTF64S32 {
    
    public VCVTF64S32(DVal op1, DVal op2) {
        super(op1, op2);
    }
}