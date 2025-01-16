package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.mockito.stubbing.ValidableAnswer;

import java.util.LinkedList;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        this.setType(leftType);
        return leftType;

    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        compiler.isAssign = true;
        compiler.typeAssign = getLeftOperand().getType().toString();
        DVal val = getRightOperand().codeGenExpr(compiler);
        DVal resultat = getLeftOperand().codeGenExpr(compiler);
        if (val instanceof GPRegister){
            compiler.addInstruction(new STORE((GPRegister)val,(DAddr )resultat));
            compiler.libererReg(((GPRegister) val).getNumber());
            return resultat;
        }
        else {
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(val, reg));
            if (compiler.typeAssign.equals("float")) {
                compiler.addInstruction(new FLOAT(reg,reg));
            }
            compiler.addInstruction(new STORE(reg,(DAddr )resultat));
            compiler.libererReg(reg.getNumber());
            return reg;
        }

    }

    @Override
    protected void codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines){
        GPRegister reg = compiler.associerReg();
        if (!compiler.registeres.contains(reg)) {
            compiler.registeres.add(reg);
        }
        RegisterOffset registerOffset = new RegisterOffset(-2,Register.LB);
        lines.add(new LOAD(registerOffset, reg));
        getRightOperand().codeGenInstClass(compiler, lines);
        compiler.libererReg(reg.getNumber());
    }
}
