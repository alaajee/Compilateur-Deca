package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        
        GPRegister reg = compiler.associerReg();

        if (reg.isOffSet){
            if (leftOperand.isOffSet && rightOperand.isOffSet){
                compiler.addInstruction(new POP(Register.R0));
                compiler.spVal--;
                compiler.addInstruction(new  POP(reg));
                compiler.addInstruction(new CMP(Register.R0,reg));
            }
            else if (leftOperand.isOffSet){
                compiler.addInstruction(new POP(reg));
                compiler.addInstruction(new CMP(rightOperand,reg));
                compiler.addInstruction(new PUSH(reg));
                }
            else {
                compiler.addInstruction(new LOAD(rightOperand,reg));
                compiler.addInstruction(new CMP(leftOperand,reg));

                compiler.addInstruction(new PUSH(reg));
            }

        }
        else {
            compiler.addInstruction(new LOAD(rightOperand,reg));
            compiler.addInstruction(new CMP(leftOperand,reg));
        }
        
        // compiler.addInstruction(new LOAD(leftOperand, reg));
        // compiler.addInstruction(new CMP(rightOperand, reg)); 

        compiler.addInstruction(new SLE(reg));

        return reg;
    }

}
