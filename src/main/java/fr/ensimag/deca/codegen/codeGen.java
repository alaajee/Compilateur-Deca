package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class codeGen {
    public codeGen(){}
    public DVal codeGen(DVal leftOperand, DVal rightOperand, GPRegister reg, constructeur constructeur , DecacCompiler compiler) {
        compiler.etatDivide = false;
        if (reg.isOffSet) {
            if (leftOperand.isOffSet) {
                if (rightOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.decrementTsto();
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                    constructeur.constructeur(compiler, Register.R0, reg);
                    //compiler.addInstruction(new PUSH(reg));
                    compiler.decrementTsto();
                    return reg;
                }
                else {
                    compiler.addInstruction(new LOAD(rightOperand, Register.R0));
                    compiler.addInstruction(new POP(reg));
                    compiler.decrementTsto();
                    constructeur.constructeur(compiler, Register.R0,reg);
                    //compiler.addInstruction(new PUSH(reg));
                    compiler.decrementTsto();
                    return reg;
                }
            }  else {
                compiler.addInstruction(new LOAD(leftOperand, reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                compiler.needToPush = true;
                // compiler.addInstruction(new PUSH(reg));
                return reg;
            }
        }
        else {
            if (leftOperand instanceof DAddr){
                compiler.addInstruction(new LOAD(leftOperand,reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                return reg;
            }
            else {
                // System.out.println(leftOperand.isVar);
                //compiler.addInstruction(new LOAD(rightOperand,reg));
                if (!leftOperand.isVar){
                    compiler.addInstruction(new LOAD(leftOperand,reg));
                    constructeur.constructeur(compiler, rightOperand, reg);
                    return reg;
                }
                else {
                    constructeur.constructeur(compiler, rightOperand, (GPRegister) leftOperand);
                }
                return leftOperand;
            }
        }
    }

    public void finalizeAndPush(GPRegister reg, DecacCompiler compiler) {
//        if(compiler.needToPush){
//            compiler.needToPush = false;
//            compiler.addInstruction(new PUSH(reg));
//            compiler.incrementTsto();
//        }
    }


    public void codeGenPrint(DVal leftOperand, DVal rightOperand, GPRegister reg, constructeur constructeur , DecacCompiler compiler) {
        compiler.isArith = true;
        if (reg.isOffSet) {
            if (leftOperand.isOffSet) {
                if (rightOperand.isVar) {
                    compiler.addInstruction(new POP(reg));
                    compiler.decrementTsto();
                    compiler.addInstruction(new LOAD(reg, Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                    constructeur.constructeur(compiler, Register.R0, reg);
                    //compiler.addInstruction(new PUSH(reg));
                    compiler.decrementTsto();
                    compiler.addInstruction(new LOAD(reg, Register.R1));
                } else {
                    compiler.addInstruction(new LOAD(rightOperand, Register.R0));
                    compiler.addInstruction(new POP(reg));
                    compiler.decrementTsto();
                    constructeur.constructeur(compiler, Register.R0, reg);
                    //compiler.addInstruction(new PUSH(reg));
                    compiler.decrementTsto();
                    compiler.addInstruction(new LOAD(reg, Register.R1));
                }
            } else {
                compiler.addInstruction(new LOAD(leftOperand, reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                compiler.needToPush = true;
                // compiler.addInstruction(new PUSH(reg));
                compiler.addInstruction(new LOAD(reg, Register.R1));
            }
        } else {
            if (leftOperand instanceof DAddr) {
                compiler.addInstruction(new LOAD(leftOperand, reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                compiler.addInstruction(new LOAD(reg, Register.R1));
            } else {
                // System.out.println(leftOperand.isVar);
                //compiler.addInstruction(new LOAD(rightOperand,reg));
                if (!leftOperand.isVar) {
                    compiler.addInstruction(new LOAD(leftOperand, reg));
                    constructeur.constructeur(compiler, rightOperand, reg);
                    compiler.addInstruction(new LOAD(reg, Register.R1));
                } else {
                    constructeur.constructeur(compiler, rightOperand, (GPRegister) leftOperand);
                    compiler.addInstruction(new LOAD(leftOperand, Register.R1));
                }

            }
        }
    }
}













