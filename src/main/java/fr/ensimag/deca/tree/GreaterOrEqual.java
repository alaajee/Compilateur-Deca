package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        
<<<<<<< Updated upstream
=======
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        
        compiler.addInstruction(new SGE(reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));
        gen.finalizeAndPush(reg, compiler);
        return register;
    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler) {
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
>>>>>>> Stashed changes
        GPRegister reg = compiler.associerReg();
        compiler.addInstruction(new LOAD(leftOperand, reg));
        compiler.addInstruction(new CMP(rightOperand, reg)); 

        compiler.addInstruction(new SGE(reg));

        return reg;
    }
}
