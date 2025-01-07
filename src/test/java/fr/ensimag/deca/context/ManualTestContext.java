package fr.ensimag.deca.context;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.deca.tree.Main;
import fr.ensimag.deca.tree.Println;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.deca.tree.StringLiteral;
import java.io.IOException;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public class ManualTestContext {
    public static void main(String[] args) throws IOException {
           
        ListDeclClass listClasses = new ListDeclClass(); 
        ListDeclVar listDeclVar = new ListDeclVar();

ListInst listInst = new ListInst();

// CrÃ©er l'instruction print("Hello")
StringLiteral hello = new StringLiteral("Helloworld");
ListExpr listExpr = new ListExpr();
listExpr.add(hello);
Println printInst = new Println(false, listExpr);

// Ajouter l'instruction print Ã  la liste d'instructions
listInst.add(printInst);

// CrÃ©er le bloc main
Main mainBlock = new Main(listDeclVar, listInst);

// CrÃ©er le programme complet
AbstractProgram source = new Program(listClasses, mainBlock);

// CrÃ©er le compilateur (si nÃ©cessaire pour la vÃ©rification ou l'exÃ©cution)
DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        
        
     

        AbstractProgram prog = source;
        
        try {
            prog.verifyProgram(compiler);
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }
        prog.prettyPrint(System.out);
    }
}
