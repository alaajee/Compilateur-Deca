package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass)  {
                Symbol symbolFloat = compiler.createSymbol("float");
                Type typeFloat = compiler.environmentType.getEnvtypes().get(symbolFloat).getType();       
                this.setType(typeFloat);
                return typeFloat;        

    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
