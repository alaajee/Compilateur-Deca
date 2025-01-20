package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;

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
        compiler.notCond = true;
        DVal resultat = getOperand().codeGenExpr(compiler);
        return resultat;
    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
    }

    public DVal codeGenInstrCond(DecacCompiler compiler, Label endLabel, Label bodyLabel){
        compiler.notCond = true;
        DVal resultat = getOperand().codeGenInstrCond(compiler,endLabel,bodyLabel);
        return resultat;
    }
}
