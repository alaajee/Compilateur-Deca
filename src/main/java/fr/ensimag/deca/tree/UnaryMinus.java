package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
            if (!t.isFloat() && !t.isInt())
            {
                throw new ContextualError("Unary Minus failed: the operand must be float or int" , this.getLocation());             
          
            }
            this.setType(t);
            return t;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
       DVal operand = this.getOperand().codeGenExpr(compiler);
       if (operand instanceof GPRegister){
            compiler.addInstruction(new OPP(operand,(GPRegister) operand));
            return operand;
        }
       else {
           GPRegister reg = compiler.associerReg();
           compiler.addInstruction(new OPP(operand,reg));
           compiler.libererReg(reg.getNumber());
           return reg;
       }
    }

}
