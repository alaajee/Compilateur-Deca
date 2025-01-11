package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

                if (!rightType.isInt() || !leftType.isInt())
                {
                    throw new ContextualError("Type mismatch:both operands must be of type int" , this.getLocation());             
                }

                this.setType(leftType);
                return leftType;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }


    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        if (reg.isOffSet) {
            if (leftOperand.isOffSet && rightOperand.isOffSet) {
                compiler.addInstruction(new POP(Register.R0));
                compiler.spVal--;
                compiler.addInstruction(new POP(reg));
                compiler.addInstruction(new REM(Register.R0, reg));
                return reg;
            } else if (leftOperand.isOffSet) {
                System.out.println("hhh");
                if (rightOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                    compiler.addInstruction(new REM(Register.R0,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
                else {
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new REM(rightOperand,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
            } else if (rightOperand.isOffSet) {
                System.out.println("hh");
                if (leftOperand.isVar){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new LOAD(reg,Register.R0));
                    compiler.addInstruction(new LOAD((RegisterOffset) leftOperand, reg));
                    compiler.addInstruction(new REM(Register.R0,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
                else {
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new REM(reg,(GPRegister) leftOperand));
                    compiler.addInstruction(new PUSH(reg));
                    return leftOperand;
                }


            } else {
                System.out.println("h");

                compiler.addInstruction(new LOAD(leftOperand, reg));
                compiler.addInstruction(new REM(rightOperand, reg));
                compiler.addInstruction(new PUSH(reg));
                return reg;
            }
        }
        else {
            System.out.println("hhh");
            if (leftOperand instanceof DAddr){
                System.out.println("hhh");
                compiler.addInstruction(new LOAD(leftOperand,reg));
                compiler.addInstruction(new REM(rightOperand,reg));
                return reg;
            }
            else {
                System.out.println(leftOperand.isVar);
                System.out.println("hhh");
                //compiler.addInstruction(new LOAD(rightOperand,reg));
                compiler.addInstruction(new REM(rightOperand, (GPRegister) leftOperand));
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
        compiler.addInstruction(new REM(rightOperand,reg));
        compiler.addInstruction(new LOAD(reg, Register.R1));
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }


    }
}
