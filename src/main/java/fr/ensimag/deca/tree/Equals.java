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
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        compiler.addInstruction(new SEQ(reg));
        gen.finalizeAndPush(reg, compiler);
        compiler.libererReg(reg.getNumber());

        compiler.equals = true;
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

        compiler.addInstruction(new SEQ(reg));

        // Affichage du résultat
        compiler.addInstruction(new LOAD(reg, Register.R1));
        compiler.addInstruction(new WINT());
    }

    public DVal codeGenInstrCond(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        // Générer le code pour les opérandes gauche et droit
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        // Associer un registre pour les opérations
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurCMP();
        codeGen gen = new codeGen();

        // Générer le code de comparaison
        DVal comparisonResult = gen.codeGen(leftOperand, rightOperand, reg, constructeur, compiler);
        compiler.addInstruction(new SEQ((GPRegister) comparisonResult));

        // Gérer les conditions AND
        if (compiler.and) {
            handleAndCondition(compiler, endLabel, bodyLabel);
        }
        // Gérer les conditions OR
        else if (compiler.or) {
            handleOrCondition(compiler, endLabel, bodyLabel);
        }
        // Gérer les conditions NOT
        else if (compiler.notCond) {
            compiler.addInstruction(new BEQ(endLabel));
            compiler.notCond = false;
        }
        // Gérer les conditions par défaut
        else {
            handleDefaultCondition(compiler, endLabel, bodyLabel);
        }

        // Libérer le registre utilisé
        compiler.libererReg(reg.getNumber());
        return comparisonResult;
    }

    private void handleAndCondition(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        if (compiler.compteurAnd) {
            // Si c'est une condition AND avec compteur
            compiler.addInstruction(compiler.notCond ? new BEQ(bodyLabel) : new BNE(endLabel));
            compiler.compteurAnd = false;
            compiler.notCond = false;
        } else {
            // Si c'est une condition AND sans compteur
            if (compiler.compteurOr > 1) {
                compiler.addInstruction(compiler.notCond ? new BNE(bodyLabel) : new BEQ(bodyLabel));
            } else {
                compiler.addInstruction(new BNE(endLabel));
            }
            compiler.compteurOr--;
        }
    }

    private void handleOrCondition(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        if (compiler.compteurOr > 1) {
            compiler.addInstruction(compiler.notCond ? new BNE(bodyLabel) : new BEQ(bodyLabel));
            compiler.compteurOr--;
        } else {
            compiler.addInstruction(new BNE(endLabel));
            compiler.compteurOr--;
        }
    }

    private void handleDefaultCondition(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        if (compiler.compteurAnd) {
            compiler.addInstruction(new BEQ(bodyLabel));
        } else {
            compiler.addInstruction(new BNE(endLabel));
        }
    }}