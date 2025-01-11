package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        GPRegister reg = compiler.associerReg();

        compiler.addInstruction(new LOAD(leftOperand, reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));

        Label nonZeroLeft = new Label("non_zero_left");
        compiler.addInstruction(new BEQ(nonZeroLeft));

        compiler.addInstruction(new LOAD(new ImmediateInteger(0), reg));
        compiler.addInstruction(new BRA(nonZeroLeft));

        compiler.addInstruction(new LOAD(rightOperand, reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));
        compiler.addLabel(nonZeroLeft);
        compiler.addInstruction(new SEQ(reg));

        return reg;
    }



}
