package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
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
    protected DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        // Je dois savoir si le leftOperand est stocké dans un registre ou non ?
        // DVal to reg ?
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        if (typeRight.isInt() && typeLeft.isInt()){
            GPRegister reg = compiler.associerReg();
            if (reg.isOffSet){
                if (leftOperand.isOffSet && rightOperand.isOffSet){
                    compiler.addInstruction(new POP(Register.R0));
                    compiler.spVal--;
                    compiler.addInstruction(new  POP(reg));
                    compiler.addInstruction(new REM(Register.R0,reg));
                    return reg;
                }
                else if (leftOperand.isOffSet){
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new REM(rightOperand,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;}
                else {
                    compiler.addInstruction(new LOAD(rightOperand,reg));
                    compiler.addInstruction(new REM(leftOperand,reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }

            }
            else {
                compiler.addInstruction(new LOAD(rightOperand,reg));
                compiler.addInstruction(new REM(leftOperand,reg));
                return reg;
            }
        }
        else {
            // IL doit y avoir une erreur
            return null;
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        // Je dois savoir si le leftOperand est stocké dans un registre ou non ?
        // DVal to reg ?
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        if (typeRight.isInt() && typeLeft.isInt()){
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(leftOperand,reg));
            compiler.addInstruction(new REM(rightOperand,reg));
            compiler.addInstruction(new WINT());

        }
        else {
            // IL doit y avoir une erreur
            return;
        }


    }
}
