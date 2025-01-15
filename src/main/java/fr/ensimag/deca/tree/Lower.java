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
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();

        
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        
        compiler.libererReg(reg.getNumber());
        compiler.addInstruction(new SLT(reg));
        gen.finalizeAndPush(reg, compiler);

        compiler.notGreater = true;
        return register;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();


        constructeurCMP constructeurCMP = new constructeurCMP();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurCMP, compiler);

        compiler.addInstruction(new SLT(reg));

        // Affichage du résultat
        compiler.addInstruction(new LOAD(reg, Register.R1));
        compiler.addInstruction(new WINT());
    }

    @Override
    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();

        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);

        compiler.libererReg(reg.getNumber());
        compiler.addInstruction(new SLT(reg));

        if (compiler.and){
            compiler.addInstruction(new BGE(endLabel));
        }
        else if(compiler.or){
            if (compiler.compteurOr == 1){
                    if (compiler.notCond){
                        compiler.addInstruction(new BGE(bodyLabel));
                }
                    else {
                        compiler.addInstruction(new BLT(bodyLabel));
                    }
                compiler.compteurOr--;            }
            else {
                compiler.addInstruction(new BGE(endLabel));
            }
        }else if (compiler.ifcond){
            compiler.addInstruction(new BLT(endLabel));
        }
        else {
            compiler.addInstruction(new BGE(endLabel));
        }
        gen.finalizeAndPush(reg, compiler);

        compiler.notGreater = true;
        return register;
    }
}
