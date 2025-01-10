package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
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
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        if (reg.isOffSet){
            if (leftOperand.isOffSet && rightOperand.isOffSet){
                compiler.addInstruction(new POP(Register.R0));
                compiler.spVal--;
                compiler.addInstruction(new  POP(reg));
                compiler.addInstruction(new MUL(Register.R0,reg));
                return reg;
            }
            else if (leftOperand.isOffSet){
                compiler.addInstruction(new POP(reg));
                compiler.addInstruction(new MUL(rightOperand,reg));
                compiler.addInstruction(new PUSH(reg));
                return reg;}
            else {
                if (!rightOperand.isVar){
                    compiler.addInstruction(new MUL(leftOperand,reg));
                }
                else {
                    compiler.addInstruction(new LOAD(rightOperand,reg));
                    compiler.addInstruction(new MUL(leftOperand,reg));
                }
                compiler.addInstruction(new PUSH(reg));
                return reg;
            }

        }
        else {
            compiler.addInstruction(new LOAD(rightOperand,reg));
            compiler.addInstruction(new MUL(leftOperand,reg));
            return reg;
        }


    }

    @Override
    public void codeGenInst(DecacCompiler compiler){
        codeGenExpr(compiler);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
       //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        compiler.addInstruction(new LOAD(rightOperand,reg));
        compiler.addInstruction(new MUL(leftOperand,reg));
        compiler.addInstruction(new LOAD(reg, Register.R1));
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }


    }


}
