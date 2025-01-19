package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMBinaryInstructionVLDRF64;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.arm.pseudocode.*;

public class VLDRF64 extends ARMBinaryInstructionVLDRF64 {
    
    public VLDRF64(DVal op1, ARMGPRegister op2) {
        super(op1, op2);
    }
    public VLDRF64(DVal op1, DVal op2) {
        super(op1, op2);
    }
    public VLDRF64(DVal op1, ARMImmediateString op2) {
        super(op1, op2);
    }
}