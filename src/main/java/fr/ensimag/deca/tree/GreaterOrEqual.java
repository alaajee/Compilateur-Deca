package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.codegen.constructeurCMP;

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
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();

        
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        
        compiler.addInstruction(new SGE(reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));
        gen.finalizeAndPush(reg, compiler);

        compiler.greaterStric = true;
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
        GPRegister reg = compiler.associerReg();

        constructeurCMP constructeurCMP = new constructeurCMP();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurCMP, compiler);

        compiler.addInstruction(new SGE(reg));

        // Affichage du résultat
        compiler.addInstruction(new LOAD(reg, Register.R1));
        compiler.addInstruction(new WINT());
    }

    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();


        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);

        compiler.libererReg(reg.getNumber());
        compiler.addInstruction(new SGE(reg));
        System.out.println(compiler.or);
        if (compiler.or){
            if (compiler.compteurOr != 0){
                if (compiler.notCond){
                    compiler.addInstruction(new BLT(bodyLabel));
                }
                else {
                    compiler.addInstruction(new BGE(bodyLabel));
                }
                compiler.compteurOr--;
            }
            else {
                compiler.addInstruction(new BLT(endLabel));
            }
        } else if(compiler.ifcond) {
            compiler.addInstruction(new BGE(endLabel));
        }
        else{
            compiler.addInstruction(new BLT(endLabel));
        }
        gen.finalizeAndPush(reg, compiler);

        compiler.greaterStric = true;
        return register;
    }
}
