package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.deca.codegen.constructeurADD;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
        this.expression = "instruction";
    }
    
    @Override
    protected String getOperatorName() {
        return "+";
    }


    public String setExpr(){
        this.expression = "instruction";
        return expression;
    }

    public String getExpr(){
        return expression;
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurADD();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        gen.finalizeAndPush(reg, compiler);
        compiler.libererReg(reg.getNumber());
        return register;
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

    public DVal codeGenInit(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenInit(compiler);
        DVal rightOperand = getRightOperand().codeGenInit(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurADD();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        gen.finalizeAndPush(reg, compiler);
        DAddr adresse = compiler.getCurrentAdresse();
        compiler.addInstruction(new STORE((GPRegister)register, adresse));
        return register;
    }
}
