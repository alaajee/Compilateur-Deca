package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }



    @Override
    public void codeGenInst(DecacCompiler compiler){
        codeGenExpr(compiler);
    }



    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        System.out.println(compiler.getOverflowVal());
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        if (reg.isOffSet) {
            if (leftOperand.isOffSet && rightOperand.isOffSet) {
                compiler.addInstruction(new POP(Register.R0));
                compiler.spVal--;
                compiler.addInstruction(new POP(reg));
                compiler.addInstruction(new MUL(Register.R0, reg));
                return reg;
            } else if (leftOperand.isOffSet) {
                if (rightOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                    compiler.addInstruction(new MUL(Register.R0,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
                else {
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new MUL(rightOperand,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
            } else if (rightOperand.isOffSet) {
                if (leftOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) leftOperand, reg));
                    compiler.addInstruction(new MUL(Register.R0,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
                else {
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new MUL(reg,(GPRegister) leftOperand));
                    compiler.addInstruction(new PUSH(reg));
                    return leftOperand;
                }


            } else {

                compiler.addInstruction(new LOAD(leftOperand, reg));
                compiler.addInstruction(new MUL(rightOperand, reg));
                compiler.addInstruction(new PUSH(reg));
                return reg;
            }
        }
        else {
            if (leftOperand instanceof DAddr){
                compiler.addInstruction(new LOAD(leftOperand,reg));
                compiler.addInstruction(new MUL(rightOperand,reg));
                return reg;
            }
            else {
                System.out.println(leftOperand.isVar);
                //compiler.addInstruction(new LOAD(rightOperand,reg));
                compiler.addInstruction(new MUL(rightOperand, (GPRegister) leftOperand));
                return leftOperand;
            }
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        compiler.addInstruction(new LOAD(leftOperand,reg));
        compiler.addInstruction(new MUL(rightOperand,reg));
        compiler.addInstruction(new LOAD(reg, Register.R1));
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }


    }



}
