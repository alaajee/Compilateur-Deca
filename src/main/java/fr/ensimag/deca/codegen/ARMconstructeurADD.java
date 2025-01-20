package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

public class ARMconstructeurADD extends ARMconstructeur{

    @Override
    public void ARMconstructeur(DecacCompiler compiler, ARMGPRegister op1, DVal op2,  DVal op3){
        compiler.addInstruction(new ADD(op1, op2, op3));
    }
}