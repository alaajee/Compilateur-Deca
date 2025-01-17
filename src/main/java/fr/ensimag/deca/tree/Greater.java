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
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();

        
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        
        
        // Ajout de l'instruction SGT (Set if Greater Than)
        compiler.addInstruction(new SGT(reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));
        gen.finalizeAndPush(reg, compiler);
        compiler.greater = true;
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

        // Ajout de l'instruction SGT pour la comparaison
        compiler.addInstruction(new SGT(reg));

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
        // Ajout de l'instruction SGT (Set if Greater Than)
        compiler.addInstruction(new SGT(reg));
        if (compiler.and){
            compiler.addInstruction(new BLE(endLabel));
        }
        else if (compiler.or){
            if (compiler.compteurOr != 0){
                if (compiler.notCond){
                    compiler.addInstruction(new BLE(bodyLabel));
                }
                else {
                    compiler.addInstruction(new BGT(bodyLabel));
                }
                compiler.compteurOr--;
            }
            else {
                compiler.addInstruction(new BLE(endLabel));
            }
        }else if (compiler.ifcond){
            compiler.addInstruction(new BGT(endLabel));
        }
        else {
            compiler.addInstruction(new BLE(endLabel));
        }
        gen.finalizeAndPush(reg, compiler);
        compiler.greater = true;
        return register;
    }

}
