package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUB;

public class codeGen {
    public codeGen(){}
    public DVal codeGen(DVal leftOperand, DVal rightOperand, GPRegister reg, constructeur constructeur , DecacCompiler compiler) {
        if (reg.isOffSet) {
            if (leftOperand.isOffSet && rightOperand.isOffSet) {
                compiler.addInstruction(new POP(Register.R0));
                compiler.spVal--;
                compiler.addInstruction(new POP(reg));
                constructeur.constructeur(compiler, Register.R0, reg);
                return reg;
            } else if (leftOperand.isOffSet) {
                if (rightOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                    constructeur.constructeur(compiler, Register.R0, reg);
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
                else {
                    compiler.addInstruction(new POP(reg));
                    constructeur.constructeur(compiler, rightOperand, reg);
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
            } else if (rightOperand.isOffSet) {
                if (leftOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) leftOperand, reg));
                    constructeur.constructeur(compiler, Register.R0, reg);
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
                else {
                    compiler.addInstruction(new POP(reg));
                    constructeur.constructeur(compiler, leftOperand, reg);
                    compiler.addInstruction(new PUSH(reg));
                    return leftOperand;
                }


            } else {
                compiler.addInstruction(new LOAD(leftOperand, reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                compiler.addInstruction(new PUSH(reg));
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
                System.out.println(leftOperand.isVar);
                //compiler.addInstruction(new LOAD(rightOperand,reg));
                constructeur.constructeur(compiler, rightOperand, (GPRegister) leftOperand);
                return leftOperand;
            }

        }
    }

    public void codeGenPrint(DVal leftOperand, DVal rightOperand, GPRegister reg, constructeur constructeur , DecacCompiler compiler){
        if (reg.isOffSet) {
            if (leftOperand.isOffSet && rightOperand.isOffSet) {
                compiler.addInstruction(new POP(Register.R0));
                compiler.spVal--;
                compiler.addInstruction(new POP(reg));
                constructeur.constructeur(compiler, Register.R0, reg);
                compiler.addInstruction(new LOAD(reg, Register.R1));
            } else if (leftOperand.isOffSet) {
                if (rightOperand.isVar){
                    System.out.println("ici1");
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                    constructeur.constructeur(compiler, Register.R0, reg);
                    compiler.addInstruction(new PUSH(reg));
                    compiler.addInstruction(new LOAD(reg, Register.R1));
                }
                else {
                    System.out.println("ici2");
                    compiler.addInstruction(new POP(reg));
                    constructeur.constructeur(compiler, rightOperand, reg);
                    compiler.addInstruction(new PUSH(reg));
                    compiler.addInstruction(new LOAD(reg, Register.R1));
                }
            } else if (rightOperand.isOffSet) {
                if (leftOperand.isVar){
                    System.out.println("ici3");
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) leftOperand, reg));
                    constructeur.constructeur(compiler, Register.R0, reg);
                    compiler.addInstruction(new PUSH(reg));
                    compiler.addInstruction(new LOAD(reg, Register.R1));
                }
                else {
                    System.out.println("ici4");
                    compiler.addInstruction(new POP(reg));
                    constructeur.constructeur(compiler, reg,(GPRegister) leftOperand);
                    compiler.addInstruction(new PUSH(reg));
                    compiler.addInstruction(new LOAD(leftOperand, Register.R1));
                }


            } else {
                System.out.println("ici5");
                compiler.addInstruction(new LOAD(leftOperand, reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                compiler.addInstruction(new PUSH(reg));
                compiler.addInstruction(new LOAD(reg, Register.R1));
            }
        }
        else {
            if (leftOperand instanceof DAddr){
                System.out.println("ici6");
                compiler.addInstruction(new LOAD(leftOperand,reg));
                constructeur.constructeur(compiler, rightOperand, reg);
                compiler.addInstruction(new LOAD(reg, Register.R1));
            }
            else {
                System.out.println("ici7");
                //compiler.addInstruction(new LOAD(rightOperand,reg));
                constructeur.constructeur(compiler, rightOperand, (GPRegister) leftOperand);
                compiler.addInstruction(new LOAD(leftOperand, Register.R1));
            }

        }
    }
}