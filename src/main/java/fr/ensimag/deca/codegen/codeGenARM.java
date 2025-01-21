package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ARMconstructeur;
import fr.ensimag.ima.pseudocode.*;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

public class codeGenARM {
    public codeGenARM(){}
    public ARMGPRegister codeGenARM(DVal op1, DVal op2, ARMGPRegister reg, ARMconstructeur constructeur , DecacCompiler compiler) {
        ARMGPRegister regLeft;
        ARMGPRegister regRight;
        boolean leftPushed = false, rightPushed = false;

        if(compiler.hasAvailableregARM()){
            if (op1 instanceof DAddr) {
                regLeft = compiler.associerRegARM();
                compiler.addInstruction(new LDR(regLeft, op1));
                compiler.addInstruction(new LDR(regLeft, new ARMImmediateString("[R"+regLeft.getNumber()+"]")));
            } else if(op1 instanceof ARMGPRegister){
                regLeft = (ARMGPRegister) op1;
            } else {
                regLeft = compiler.associerRegARM();
                compiler.addInstruction(new MOV(regLeft, op1));
            }
        } else {
            regLeft = null; // Pas de registre disponible
            leftPushed = true;
            if (op1 instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R0, op1));
                compiler.addInstruction(new LDR(ARMRegister.R0, new ARMImmediateString("[R0]")));
            } else {
                compiler.addInstruction(new MOV(ARMRegister.R0, op1));
            }
            compiler.addInstruction(new PUSH(ARMRegister.R0));
        }
        if (compiler.hasAvailableregARM()) {
            if (op2 instanceof DAddr) {
                regRight = compiler.associerRegARM();
                compiler.addInstruction(new LDR(regRight, op2));
                compiler.addInstruction(new LDR(regRight, new ARMImmediateString("[R" + regRight.getNumber() + "]")));
            } else if(op2 instanceof ARMGPRegister){
                regRight = (ARMGPRegister) op2;
            }else{
                regRight = compiler.associerRegARM();
                compiler.addInstruction(new MOV(regRight, op2));
            }
        } else {
            regRight = null; // Pas de registre disponible
            rightPushed = true;
            if (op2 instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R0, op2));
                compiler.addInstruction(new LDR(ARMRegister.R0, new ARMImmediateString("[R0]")));
            } else {
                compiler.addInstruction(new MOV(ARMRegister.R0, op2));
            }
            compiler.addInstruction(new PUSH(ARMRegister.R0));
        }

        if (leftPushed) {
            compiler.addInstruction(new POP(ARMRegister.R0));
            regLeft = ARMRegister.R0;
        }
        if (rightPushed) {
            compiler.addInstruction(new POP(ARMRegister.R1));
            regRight = ARMRegister.R1;
        }
        constructeur.ARMconstructeur(compiler, reg, regLeft, regRight);

        
        compiler.libererRegARM(regLeft.getNumber());
        compiler.libererRegARM(regRight.getNumber());
        
        
        return reg;
    }

}













