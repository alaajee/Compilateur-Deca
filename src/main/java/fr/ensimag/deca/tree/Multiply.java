package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.LinkedList;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
        this.expression = "instruction";
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }



    @Override
    public void codeGenInst(DecacCompiler compiler){
        codeGenExpr(compiler);
    }


    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
            compiler.incrementTsto();
        }

        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        if (leftOperand instanceof GPRegister){
            constructeur constructeur = new constructeurMUL();
            codeGen gen = new codeGen();
            DVal register = gen.codeGen(leftOperand,rightOperand,(GPRegister) leftOperand,constructeur,compiler);
            gen.finalizeAndPush((GPRegister) register, compiler);
            return leftOperand;
        }
        else {
            GPRegister reg = compiler.associerReg();
            constructeur constructeur = new constructeurMUL();
            codeGen gen = new codeGen();
            DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
            gen.finalizeAndPush((GPRegister) register, compiler);
            return register;
        }
    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        return null;
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
        constructeur constructeurMUL = new constructeurMUL();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurMUL,compiler);
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
        constructeur constructeur = new constructeurMUL();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        gen.finalizeAndPush(reg, compiler);
        DAddr adresse = compiler.getCurrentAdresse();
        compiler.addInstruction(new STORE((GPRegister)register, adresse));
        return register;
    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines, GPRegister register){
        GPRegister reg = compiler.associerReg();

        // LeftOperand et RightOperand ...
        DVal leftOperand = getLeftOperand().codeGenInstClass(compiler,lines,reg);
        DVal rightOperand = getRightOperand().codeGenInstClass(compiler,lines,reg);

        if (!compiler.registeres.contains(reg)) {
            compiler.registeres.add(reg);
        }

        lines.add(new LOAD(new RegisterOffset(-2,Register.LB),reg));
        lines.add(new LOAD(leftOperand,reg));
        lines.add(new MUL(rightOperand,reg));
        // DVal dval = getRightOperand().codeGenExpr(compiler);
        compiler.libererReg(reg.getNumber());
        return reg;
    }

}
