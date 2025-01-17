package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.codegen.constructeurCMP;
import fr.ensimag.deca.codegen.constructeurADD;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
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

    @Override
    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodylabel) {
        compiler.or = true;
        compiler.compteurOr++;
        // System.out.println(compiler.nouvLabel);
        if (compiler.and ){
            //compiler.and = false;
            DVal leftOperand = getLeftOperand().codeGenInstrCond(compiler, compiler.nouvLabel,compiler.nouvLabel);
            if (compiler.compteurOr != 1){
                DVal rightOperand = getRightOperand().codeGenInstrCond(compiler,endLabel,compiler.nouvLabel);
                compiler.compteurOr--;
            }
            else {
                DVal rightOperand = getRightOperand().codeGenInstrCond(compiler, endLabel, bodylabel);
            }
            //compiler.and = true;
        }
        else {
           // compiler.nouvLabel = new Label("nouvLabel" + compiler.getUniqueID());
            DVal leftOperand = getLeftOperand().codeGenInstrCond(compiler, endLabel,bodylabel);
            if (compiler.and){
                compiler.addLabel(compiler.nouvLabel);
                compiler.nouvLabel = new Label("nouvLabel" + compiler.getUniqueID());
                compiler.compteurOr--;
            }

            DVal rightOperand = getRightOperand().codeGenInstrCond(compiler,endLabel,bodylabel);
        }
        GPRegister reg = compiler.associerReg();
        return reg;
    }
}
