package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ARMconstructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

public class codeGenARM {
    public codeGenARM(){}
    public DVal codeGenARM(ARMGPRegister op1, ARMGPRegister op2, ARMGPRegister reg, ARMconstructeur constructeur , DecacCompiler compiler) {
        // if( !(op1 instanceof ARMGPRegister) && !(op2 instanceof ARMGPRegister)){
        //     ARMGPRegister op1reg = compiler.associerRegARM();
        //     ARMGPRegister op2reg = compiler.associerRegARM();
        //     // if( op1 instanceof DAddr){
        //     //     compiler.addInstruction(new LDR(op1reg, op1));
        //     // } else if (op1.getClass().getTypeName().equals("int")){
        //     //     compiler.addInstruction(new MOV(op1reg, op1));
        //     // } else if (op1.getClass().getTypeName().equals("float")){
        //     //     // compiler.addInstruction(new VMOVF64(ARMRegister.D0, op1));
        //     // }
        //     // if( op2 instanceof DAddr){
        //     //     compiler.addInstruction(new LDR(op2reg, op2));
        //     // } else if (op2.getClass().getTypeName().equals("int")){
        //     //     compiler.addInstruction(new MOV(op2reg, op2));
        //     // } else if (op2.getClass().getTypeName().equals("float")){
        //     //     // compiler.addInstruction(new VMOVF64(ARMRegister.D0, op1));
        //     // }

        //     constructeur.ARMconstructeur(compiler, reg, op1reg, op2reg);
        // } else if (!(op1 instanceof ARMGPRegister)){
        //     ARMGPRegister op1reg = compiler.associerRegARM();
        //     if( op1 instanceof DAddr){
        //         compiler.addInstruction(new LDR(op1reg, op1));
        //     } else if (op1.getClass().getTypeName().equals("int")){
        //         compiler.addInstruction(new MOV(op1reg, op1));
        //     } else if (op1.getClass().getTypeName().equals("float")){
        //         // compiler.addInstruction(new VMOVF64(ARMRegister.D0, op1));
        //     }
        //     constructeur.ARMconstructeur(compiler, reg, op1reg, op2);
        // } else if (!(op2 instanceof ARMGPRegister)){
        //     ARMGPRegister op2reg = compiler.associerRegARM();
        //     if( op2 instanceof DAddr){
        //         compiler.addInstruction(new LDR(op2reg, op2));
        //     } else if (op2.getClass().getTypeName().equals("int")){
        //         compiler.addInstruction(new MOV(op2reg, op2));
        //     } else if (op2.getClass().getTypeName().equals("float")){
        //         // compiler.addInstruction(new VMOVF64(ARMRegister.D0, op1));
        //     }
        //     constructeur.ARMconstructeur(compiler, reg, op1, op2reg);
        // } else{
        //     constructeur.ARMconstructeur(compiler, reg, op1, op2);
        // }
        constructeur.ARMconstructeur(compiler, reg, op1, op2);
        return reg;
        // if(op1 instanceof DAddr){
        //     constructeur.ARMconstructeur(compiler,op1, op2, reg);
        //     return reg;
        // } else{
        //     if(!leftOperand.isVar){

        //     }
        // }
        // else {
        //     if (leftOperand instanceof DAddr){
        //         compiler.addInstruction(new LOAD(leftOperand,reg));
        //         constructeur.constructeur(compiler, rightOperand, reg);
        //         return reg;
        //     }
        //     else {
        //         // System.out.println(leftOperand.isVar);
        //         //compiler.addInstruction(new LOAD(rightOperand,reg));
        //         if (!leftOperand.isVar){
        //             compiler.addInstruction(new LOAD(leftOperand,reg));
        //             constructeur.constructeur(compiler, rightOperand, reg);
        //             return reg;
        //         }
        //         else {
        //             constructeur.constructeur(compiler, rightOperand, (GPRegister) leftOperand);
        //         }
        //         return leftOperand;
        //     }
        // }
    }

}













