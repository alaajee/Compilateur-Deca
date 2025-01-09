package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        if (rightOperand instanceof GPRegister){
            compiler.addInstruction(new ADD(leftOperand,(GPRegister) rightOperand));
            return rightOperand;
        }
        else {
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(rightOperand,reg));
            compiler.addInstruction(new ADD(reg,(GPRegister) rightOperand));
            return reg;
        }
    }
}
