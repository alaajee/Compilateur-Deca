package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;

import static fr.ensimag.deca.tree.ManualTestInitialGencode.gencodeSource;

/**
 * Test d'analyse contextuelle avec un type float
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class ManualTestContext {
    public static void main(String[] args) throws IOException {
        // Création du compilateur
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        // Récupération de l'environnement des types prédéfinis
        EnvironmentType envTypes = compiler.environmentType;

        // Déclaration des variables
        SymbolTable symbolTable = compiler.symbolTable;
        Symbol symbolX = symbolTable.create("x");
        Symbol symbolY = symbolTable.create("y");

        // Utilisation du type float
        Identifier floatType = new Identifier(envTypes.FLOAT.getName());

        // Initialisation de x et y
        ListDeclVar listDeclVar = new ListDeclVar();
        listDeclVar.add(new DeclVar(floatType, new Identifier(symbolX), new Initialization(new FloatLiteral(5.0f))));
        listDeclVar.add(new DeclVar(floatType, new Identifier(symbolY), new Initialization(new FloatLiteral(10.0f))));

        // Instructions
        ListInst listInst = new ListInst();
        listInst.add(new Assign(new Identifier(symbolX), new FloatLiteral(3.0f)));
        listInst.add(new Assign(new Identifier(symbolY), new FloatLiteral(7.0f)));
        listInst.add(new Assign(new Identifier(symbolY), new Plus(new Identifier(symbolX), new Identifier(symbolY))));
        listInst.add(new Assign(new Identifier(symbolX), new Minus(new Identifier(symbolX), new FloatLiteral(3.0f))));
        listInst.add(new Assign(new Identifier(symbolX), new Divide(new Identifier(symbolX), new FloatLiteral(2.0f))));
        listInst.add(new Assign(new Identifier(symbolY), new Divide(new Identifier(symbolY), new Identifier(symbolX))));

        // Création du bloc main et du programme
        Main mainBlock = new Main(listDeclVar, listInst);
        AbstractProgram source = new Program(new ListDeclClass(), mainBlock);

        try {
            // Vérification contextuelle
            source.verifyProgram(compiler);
            String result = gencodeSource(source);
            System.out.println(result);
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }

        // Affichage du programme vérifié
        //ssource.prettyPrint(System.out);
    }
}
