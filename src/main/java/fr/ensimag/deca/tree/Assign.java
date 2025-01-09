package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal val = getRightOperand().codeGenExpr(compiler);
        DVal resultat = getLeftOperand().codeGenExpr(compiler);
        Location location = getLeftOperand().getLocation();
        String name = compiler.getNameValTab().get(location);
        //        System.out.println(name);
        System.out.println(compiler.getRegUn());
//        System.out.println(compiler.getVarTab().get(name).getOperand());
        DAddr addr = compiler.getVarTab().get(name).getOperand();
        // Il faut s'assurer que c'est un registre ou non
//        System.out.println(addr.toString());
        System.out.println(compiler.getRegUn().get(name));
        if (compiler.getRegUn().get(name) != null){
            compiler.addInstruction(new LOAD(val,compiler.getRegUn().get(name)));
            compiler.addInstruction(new STORE(compiler.getRegUn().get(name), addr));
            return compiler.getRegUn().get(name);
        }
        else {
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(val, reg));
            compiler.addInstruction(new STORE(reg, addr));
            return reg;
        }
    }


}
