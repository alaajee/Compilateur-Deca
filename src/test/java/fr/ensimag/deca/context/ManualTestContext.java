package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;

import static fr.ensimag.deca.tree.ManualTestInitialGencode.gencodeSource;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
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
        ListDeclClass listClasses = new ListDeclClass();

        // Liste des déclarations de variables
        ListDeclVar listDeclVar = new ListDeclVar();

        // Création de la table des symboles et des identifiants
        SymbolTable symbolTable = compiler.symbolTable;
        Symbol symbolX = symbolTable.create("x");

        // Création de l'identifiant x avec sa location
        Identifier idX = new Identifier(symbolX);
        idX.setLocation(new Location(1, 5, "test.deca"));

        Identifier idX1 = new Identifier(idX.getName());
        idX1.setLocation(idX.getLocation());

        Identifier idX2 = new Identifier(idX.getName());
        idX2.setLocation(idX.getLocation());

// Instruction : xSquared = x * x;

        // Utilisation du type prédéfini int
        Identifier intType = new Identifier(envTypes.INT.getName());
        intType.setLocation(new Location(1, 1, "test.deca"));

        // Déclaration de la variable : int x;
        listDeclVar.add(new DeclVar(intType, idX, new NoInitialization()));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Instruction : x = readInt();
        ReadInt readInt = new ReadInt();
        //readInt.setLocation(new Location(2, 5, "test.deca"));
        listInst.add(new Assign(idX, readInt));

        // Instruction : println(0.5 * (x * x));
        FloatLiteral half = new FloatLiteral(0.5f);
        half.setLocation(new Location(3, 8, "test.deca"));

        Multiply xSquared = new Multiply(idX1, idX2);
        xSquared.setLocation(new Location(3, 14, "test.deca"));

        Plus halfTimesXSquared = new Plus(half, xSquared);
        halfTimesXSquared.setLocation(new Location(3, 8, "test.deca"));

        ListExpr listExpr = new ListExpr();
        listExpr.add(halfTimesXSquared);

        Println printInst = new Println(false, listExpr);
        printInst.setLocation(new Location(3, 1, "test.deca"));
        listInst.add(printInst);

        // Création du bloc main avec les déclarations et les instructions
        Main mainBlock = new Main(listDeclVar, listInst);

        // Création du programme complet
        AbstractProgram source = new Program(listClasses, mainBlock);

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

        source.prettyPrint(System.out);

    }
}