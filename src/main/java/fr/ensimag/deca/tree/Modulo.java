package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;

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
        return null;
    }
}
