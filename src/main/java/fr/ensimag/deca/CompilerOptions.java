package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl02
 * @date 01/01/2025
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private boolean parseStep = false;
    private boolean verificationStep = false;
    private boolean noCheck = false;

    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        if (args.length == 0) {
            displayUsage();
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-b":
                    printBanner = true;
                    System.out.println("Nom de l'équipe :gl02");
                    if (i!=args.length-1){
                        throw new CLIException("-b should be the last option")
                    }
                    break;
                case "-p":
                    if (verificationStep){
                        throw new CLIException("choose betweetn -p or -v");
                }   
                    parseStep = true;
                    break;
                case "-v":
                    if (parseStep){
                        throw new CLIException("choose betweetn -p or -v");
                    }
                    verificationStep = true;
                    break;
                case "-n":
                    noCheck = true;
                    break;
                case "-P":
                    parallel = true;
                    //TODO
                    break;
                case "-d":
                    // TODO
                    break;
                case "-r":
                    //TODO
                    break;
                default:
                    // If the argument is a source file, add it to the list
                    File file = new File(arg);
                    if (file.exists() && file.isFile()) {
                        sourceFiles.add(file);
                    } else {
                        throw new CLIException("Invalid file path: " + arg);
                    }
                    break;
            }
        }
        if (sourceFiles.isEmpty() && !printBanner) {
            throw new CLIException("No source files provided");
        }
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

    }

    protected void displayUsage() {
        System.out.println("Usage: decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
        System.out.println("Options:");
        System.out.println(" -b (banner) : affiche une bannière indiquant le nom de l'équipe");
        System.out.println(" -p (parse) : arrête decac après l'étape de construction de" + 
                        "l'arbre, et affiche la décompilation de ce dernier" +
                        "(i.e. s'il n'y a qu'un fichier source à" +
                        "compiler, la sortie doit être un programme" +
                        "deca syntaxiquement correct)");
        System.out.println("  -v (verification) : arrête decac après l'étape de vérifications" + 
                        "(ne produit aucune sortie en l'absence d'erreur)");
        System.out.println(" -n (no check) : supprime les tests à l'exécution spécifiés dans" +
                        "les points 11.1 et 11.3 de la sémantique de Deca");
        System.out.println(" -r X (registers) : limite les registres banalisés disponibles à" +
                        "R0 ... R{X-1}, avec 4 <= X <= 16");
        System.out.println(" -d (debug) : active les traces de debug. Répéter" +
                        "l'option plusieurs fois pour avoir plus de" +
                        "traces");
        System.out.println(" -P (parallel) : s'il y a plusieurs fichiers sources," +
                        "lance la compilation des fichiers en" +
                        "parallèle (pour accélérer la compilation)");
    }
}
