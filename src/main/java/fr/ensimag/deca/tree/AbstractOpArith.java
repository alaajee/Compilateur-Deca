package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl02
 * @date 01/01/2025
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);


                Type returnType = rightType;
                
                if (!rightType.isFloat() && !rightType.isInt() || !leftType.isInt() && !leftType.isFloat() )
                {
                    throw new ContextualError("arithmetic operation failed: one of the operand is not of type :int or float", this.getLocation());

                }

                else if (rightType.isInt() && leftType.isFloat()) {
                    ConvFloat convFloat=new ConvFloat(getRightOperand());
                    Type convFloatType = convFloat.verifyExpr(compiler, localEnv, currentClass);
                    convFloat.setType(convFloatType);
                    this.setRightOperand(convFloat);
                    returnType = leftType;

                    
                }

                else if (rightType.isFloat() && leftType.isInt())
                {
                    ConvFloat convFloat=new ConvFloat(getLeftOperand());
                    Type convFloatType = convFloat.verifyExpr(compiler, localEnv, currentClass);
                    convFloat.setType(convFloatType);
                    this.setLeftOperand(convFloat);

                    
                }
                
                this.setType(returnType);
                return returnType;
    }
}
