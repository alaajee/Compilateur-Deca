package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.codegen.constructeurCMP;
import fr.ensimag.deca.codegen.constructeurADD;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
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

    compiler.addInstruction(new LOAD(new ImmediateInteger(1), reg));
    compiler.addInstruction(new BRA(nonZeroLeft));

    compiler.addInstruction(new LOAD(rightOperand, reg));
    compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));
    compiler.addLabel(nonZeroLeft);
    compiler.addInstruction(new SEQ(reg));

    return reg;
}

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler) {
        return null;
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        constructeur constructeurADD = new constructeurADD();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurADD,compiler);
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }
    }
}
