package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGT;

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
    protected DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = this.getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = this.getRightOperand().codeGenExpr(compiler);
        compiler.addInstruction(new CMP(leftOperand,(GPRegister) rightOperand));
        GPRegister register = compiler.associerReg();
        compiler.addInstruction(new SGT(register));
        return register;
    }
}
