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

    public DVal codeGenInstrCond(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        // Générer le code pour les opérandes gauche et droit
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        compiler.incrementTsto();
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        // Associer un registre pour les opérations
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();

        // Générer le code de comparaison
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);

        // Libérer le registre utilisé
        compiler.libererReg(reg.getNumber());

        // Ajouter l'instruction SGT (Set if Greater Than)
        compiler.addInstruction(new SGT(reg));

        // Gérer les conditions AND
        if (compiler.and) {
            handleAndCondition(compiler, endLabel);
        }
        // Gérer les conditions OR
        else if (compiler.or) {
            handleOrCondition(compiler, endLabel, bodyLabel, compiler.notCond);
        }
        // Gérer les conditions NOT
        else if (compiler.notCond) {
            compiler.addInstruction(new BLE(endLabel));
            compiler.notCond = false; // Réinitialiser la condition NOT
        }
        // Gérer les conditions par défaut
        else {
            handleDefaultCondition(compiler, endLabel);
        }

        // Finaliser et pousser le registre
        gen.finalizeAndPush(reg, compiler);
        compiler.greater = true; // Indiquer que la condition "greater" a été traitée
        return register;
    }

    private void handleAndCondition(DecacCompiler compiler, Label endLabel) {
        compiler.addInstruction(new BLE(endLabel));
    }

    private void handleOrCondition(DecacCompiler compiler, Label endLabel, Label bodyLabel, boolean notCond) {
        if (compiler.compteurOr > 1) {
            if (notCond) {
                compiler.addInstruction(new BLE(bodyLabel));
            } else {
                compiler.addInstruction(new BGT(bodyLabel));
            }
            compiler.compteurOr--;
        } else {
            compiler.addInstruction(new BLE(endLabel));
            compiler.compteurOr--;
        }
    }

    private void handleDefaultCondition(DecacCompiler compiler, Label endLabel) {
        compiler.addInstruction(new BLE(endLabel));
    }

}