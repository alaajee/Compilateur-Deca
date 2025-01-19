package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.codegen.constructeurCMP;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();

        
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        
        compiler.addInstruction(new SNE(reg));
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
        GPRegister reg = compiler.associerReg();


        constructeurCMP constructeurCMP = new constructeurCMP();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurCMP, compiler);

        compiler.addInstruction(new SNE(reg));

        // Affichage du résultat
        compiler.addInstruction(new LOAD(reg, Register.R1));
        compiler.addInstruction(new WINT());
    }


    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
            compiler.incrementTsto();

        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();


        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);

        compiler.libererReg(reg.getNumber());
        compiler.addInstruction(new SNE(reg));
        if (compiler.and){
            compiler.addInstruction(new BEQ(endLabel));
        }
        else if (compiler.or){
            if (compiler.compteurOr > 1){
                if (compiler.notCond){
                    compiler.addInstruction(new BEQ(bodyLabel));
                }
                else {
                    compiler.addInstruction(new BNE(bodyLabel));
                }
                compiler.compteurOr--;
            }
            else {
                compiler.addInstruction(new BEQ(endLabel));
            }
        } else if (compiler.ifcond){
            compiler.addInstruction(new BNE(endLabel));
        }
        else {
            compiler.addInstruction(new BEQ(endLabel));
        }
        gen.finalizeAndPush(reg, compiler);


        return register;
    }
}
