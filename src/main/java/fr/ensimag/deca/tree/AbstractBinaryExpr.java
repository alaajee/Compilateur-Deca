package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl02
 * @date 01/01/2025
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }


    public void codeGenBranch(DecacCompiler compiler, Label elseLabel, Label endIfLabel) {

        // Emit the branching instruction based on the operator
        if (this instanceof Greater) {
            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        } else if (this instanceof Lower) {
            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        } else if (this instanceof Equals) {
            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        } else if (this instanceof NotEquals) {
            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        } else if (this instanceof GreaterOrEqual) {
            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        } else if (this instanceof LowerOrEqual) {
            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        } else {
            throw new UnsupportedOperationException("Unsupported operator for branching");
        }
    }
}
