package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }



    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        // Je dois savoir si le leftOperand est stocké dans un registre ou non ?
        // DVal to reg ?
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        if (typeLeft.isNull()){
            throw new RuntimeException("Division by zero");
        }
        if (typeRight.isInt() && typeLeft.isInt()){
                GPRegister reg = compiler.associerReg();
                compiler.addInstruction(new LOAD(leftOperand,reg));
                compiler.addInstruction(new QUO(rightOperand,reg));
                return reg;
        }
        else if (typeLeft.isFloat() || typeRight.isFloat()) {
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(leftOperand,reg));
            compiler.addInstruction(new DIV(rightOperand,reg));
            return reg;
        }
        else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }
    }



}
