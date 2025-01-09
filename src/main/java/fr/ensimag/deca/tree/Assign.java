package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

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
        DVal val = getRightOperand().codeGenExpr(compiler);
        DVal resultat = getLeftOperand().codeGenExpr(compiler);
        Location locationLeft = getLeftOperand().getLocation();
        String nameLeft = compiler.getNameValTab().get(locationLeft);
        //        System.out.println(name);
//        System.out.println(compiler.getRegUn());
//        System.out.println(compiler.getVarTab().get(name).getOperand());
        Location locationRight = getLeftOperand().getLocation();
        String nameRight = compiler.getNameValTab().get(locationRight);
        DAddr addr = compiler.getVarTab().get(nameLeft).getOperand();
        // Il faut s'assurer que c'est un registre ou non
//        System.out.println(addr.toString());
//        System.out.println(compiler.getRegUn().get(name));
        if (compiler.getRegUn().get(nameLeft) != null){
            compiler.addInstruction(new LOAD(val,compiler.getRegUn().get(nameLeft)));
            compiler.addInstruction(new STORE(compiler.getRegUn().get(nameLeft), addr));
            // Que pour tester
//            compiler.addInstruction(new LOAD(compiler.getRegUn().get(name), Register.R1));
//            compiler.addInstruction(new WSTR(new ImmediateString("Voila on est dans le ieme nbre")));
//            compiler.addInstruction(new WFLOAT());
            return compiler.getRegUn().get(nameLeft);
        }

        else {
            if (val instanceof GPRegister && compiler.getRegUn().get(nameRight) == null){
                compiler.addInstruction(new STORE((GPRegister) val, addr));
                compiler.getRegUn().put(nameLeft, (GPRegister) val);
                return val;
            }
            else {
                GPRegister reg = compiler.associerReg();
                compiler.addInstruction(new LOAD(val, reg));
                compiler.addInstruction(new STORE(reg, addr));
                compiler.getRegUn().put(nameLeft, reg);
                return reg;
            }

        }

    }

}
