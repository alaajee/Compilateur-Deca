package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

public class ARMconstructeurVADD extends ARMconstructeur{

    @Override
    public void ARMconstructeur(DecacCompiler compiler, ARMGPRegister op1, ARMGPRegister op2,  ARMGPRegister op3){
        compiler.addInstruction(new VADDF64(op1, op2, op3));
    }
}