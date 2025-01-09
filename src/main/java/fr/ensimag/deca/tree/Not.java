package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
            
            if (!typeOperand.isBoolean())
            {
                throw new ContextualError("Type mismatch:the operand must be boolean" , this.getLocation());             
            }
            this.setType(typeOperand);
            return typeOperand;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

}
