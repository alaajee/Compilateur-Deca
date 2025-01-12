package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
    
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    
    
    if ((getOperatorName().matches("<|>|<=|>=") || getOperatorName().matches("==|!=")) &&
        ((leftType.isInt() && rightType.isInt()) ||
         (leftType.isFloat() && rightType.isFloat()) ||
         (leftType.isInt() && rightType.isFloat()) ||
         (leftType.isFloat() && rightType.isInt()))) {
        
        if (leftType.isInt() && rightType.isFloat()) {
            setLeftOperand(new ConvFloat(getLeftOperand()));
        } else if (leftType.isFloat() && rightType.isInt()) {
            setRightOperand(new ConvFloat(getRightOperand()));
        }
    }
    
    else if ((getOperatorName().equals("==") || getOperatorName().equals("!=")) &&
             (leftType.isClassOrNull() && rightType.isClassOrNull())) {
        
    } else {
        throw new ContextualError("Operator " + getOperatorName() +
            " is not applicable to those types : " + leftType.getName() + " and " + rightType.getName(),
            getLocation());
    }


    Symbol symbolBool =compiler.createSymbol("boolean");
    Type booleanType=compiler.environmentType.getEnvtypes().get(symbolBool).getType();
    setType(booleanType);
    return booleanType;
}



}