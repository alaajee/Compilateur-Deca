package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.deca.codegen.constructeurADD;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.LinkedList;

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
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurADD();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        gen.finalizeAndPush(reg, compiler);
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

    @Override
    protected void codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines){
        GPRegister reg = compiler.associerReg();

        if (!compiler.registeres.contains(reg)) {
            compiler.registeres.add(reg);
        }

        lines.add(new LOAD(new RegisterOffset(-2,Register.LB),reg));
        lines.add(new LOAD(new RegisterOffset(1 , reg),reg));
        lines.add(new ADD(new RegisterOffset(-3 ,Register.LB),reg));
        lines.add(new STORE(reg,new RegisterOffset(1,Register.R1)));
        // DVal dval = getRightOperand().codeGenExpr(compiler);
        compiler.libererReg(reg.getNumber());
    }
}
