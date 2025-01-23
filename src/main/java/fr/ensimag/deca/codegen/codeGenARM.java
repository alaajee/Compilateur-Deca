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

    public ARMGPRegister codeGenARMFloat(DVal op1, DVal op2, ARMGPRegister reg, ARMconstructeur constructeur , DecacCompiler compiler, boolean op1float, boolean op2float){
        if(op1float){
            if (op1 instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R1, op1));
                compiler.addInstruction(new VLDRF64(ARMRegister.D0, new ARMImmediateString("[R1]")));
            } else {
                compiler.addInstruction(new VMOVF64(ARMRegister.D0, op1));
            }
        }else{
            if(op1 instanceof DAddr){
                compiler.addInstruction(new LDR(ARMRegister.R1, op1));
                compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
                compiler.addInstruction(new VMOV(ARMRegister.S0, ARMRegister.R1));
                compiler.addInstruction(new VCVTF64S32(ARMRegister.D0, ARMRegister.S0));
            } else{
                compiler.addInstruction(new VMOVF64(ARMRegister.D0, op1));
            }
        }

        if(op2float){
            if (op2 instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R1, op2));
                compiler.addInstruction(new VLDRF64(ARMRegister.D1, new ARMImmediateString("[R1]")));
            } else {
                compiler.addInstruction(new VMOVF64(ARMRegister.D1, op2));
            }
        }else{
            if(op2 instanceof DAddr){
                compiler.addInstruction(new LDR(ARMRegister.R1, op2));
                compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
                compiler.addInstruction(new VMOV(ARMRegister.S0, ARMRegister.R1));
                compiler.addInstruction(new VCVTF64S32(ARMRegister.D1, ARMRegister.S0));
            } else{
                compiler.addInstruction(new VMOVF64(ARMRegister.D1, op2));
            }
        }
        
        constructeur.ARMconstructeur(compiler, reg, ARMGPRegister.D0, ARMGPRegister.D1);
        
        
        return reg;
    }

}













