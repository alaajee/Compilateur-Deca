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
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        compiler.addInstruction(new SEQ(reg));
        //gen.finalizeAndPush(reg, compiler);
        compiler.libererReg(reg.getNumber());

        compiler.equals = true;
        return register;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();

        constructeurCMP constructeurCMP = new constructeurCMP();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurCMP, compiler);

        compiler.addInstruction(new SEQ(reg));

        // Affichage du résultat
        compiler.addInstruction(new LOAD(reg, Register.R1));
        compiler.addInstruction(new WINT());
    }

    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel) {
//        System.out.println(compiler.notCond);
//        System.out.println(endLabel);

        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurCMP();
        compiler.addInstruction(new LOAD(rightOperand,reg));
        constructeur.constructeur(compiler,leftOperand,reg);
        compiler.addInstruction(new SEQ(reg));
        if (compiler.and){
            //compiler.addInstruction(new BEQ(bodyLabel));
            if (compiler.compteurAnd){
                compiler.addInstruction(new BEQ(bodyLabel));
                compiler.compteurAnd = false;
            }
            else {
                if (compiler.compteurOr > 1){
                    compiler.addInstruction(new BEQ(endLabel));
                }
                else {
                    compiler.addInstruction(new BNE(endLabel));
                }
                 // celle laaaaaaaaaaa
            }
        }
        else if (compiler.or){
            if (compiler.compteurOr != 0){
                if (compiler.notCond){
                    compiler.addInstruction(new BNE(bodyLabel));
                }
                else {
                    compiler.addInstruction(new BEQ(bodyLabel));
                }
                compiler.compteurOr--;
            }
            else {
                compiler.addInstruction(new BNE(endLabel));
            }

        }
        else if (compiler.notCond){
            compiler.addInstruction(new BEQ(endLabel));
            compiler.notCond = false;
        }
        else {
            if (compiler.compteurAnd ){
                compiler.addInstruction(new BEQ(bodyLabel));
            }
            else {
                compiler.addInstruction(new BNE(endLabel));
            }
        }
       // gen.finalizeAndPush(reg, compiler);
        compiler.libererReg(reg.getNumber());
        return reg;
    }
}



