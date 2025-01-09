package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }
}
