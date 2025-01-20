package fr.ensimag.deca;

import java.io.File;
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
    public boolean getVerify() {
        return verify;
    }    
    
    public boolean getParse() {
        return parse;
    }
    public boolean getNoCHeck() {
        return noCheck;
    }   
    public boolean getRegisterLimit() {
        return registreLimit;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean verify = false; //case "-v"
    private boolean parse = false; //case "-p"
    private boolean noCheck = false; //case "-n"
    private boolean registreLimit = false; //case "-r"
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private int registreLimitValue = 15;


    
    public void parseArgs(String[] args) throws CLIException {
        Logger logger = Logger.getRootLogger();
        for (int i = 0 ;i< args.length; i++ )
        {
            switch(args[i])
            {
                case "-p":
                    if (verify)
                    {
                        throw new IllegalArgumentException("you can't use -p and -v at the same time");
                    }
                    parse =true;
                    break;

                case "-n":
                    noCheck = true;
                    break;


                case "-r":
                    if (i+1<args.length)
                    {
                        try {

                        
                        int x = Integer.parseInt(args[++i]);
                        if (x < 4 || x > 16) {
                            throw new CLIException("Valeur de -r hors limites : " + x + "doit etre comprise entre 4 et 16 ");
                        }
                        registreLimit = true;
                        registreLimitValue = x;

                        } catch (NumberFormatException e) {
                            throw new CLIException("Valeur attendue après -r");
                        } 

                    }   
                    else {
                        throw new CLIException("Option -r sans valeur");
                    }

                    break;

                case "-P":
                    parallel = true;
                    break;
                
                case "-b":
                if (args.length  !=1) {
                    // L’option '-b' ne peut être utilisée que sans autre option
                    throw new CLIException("L'option '-b' doit être utilisée seule, sans autre option ni fichier source.");
                }
                printBanner= true;
                return;
    
                case "-d":
                    debug++;
                    break;

                case "-v":
                    if (parse)
                    {
                        throw new IllegalArgumentException("you can't use -p and -v at the same time");
                    }
                    verify = true;
                    break;
                default:
                    File file = new File(args[i]);
                    if (file.exists() && file.isFile()) {
                        if(!sourceFiles.contains(file)){
                            sourceFiles.add(file);
                        }
                    } 
                    else {
                        throw new CLIException("Fichier source invalide : " + args[i]);
                    }

            }
        }

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
        System.err.println("Erreur : aucun fichier source spécifié.");
        System.out.println("Usage : decac [options] <source files>");
        System.out.println("Options disponibles :");
        System.out.println("  -b        : Affiche une bannière et termine le programme.");
        System.out.println("  -p        : Arrête le compilateur après la construction de l'arbre syntaxique.");
        System.out.println("  -v        : Arrête le compilateur après les vérifications sémantiques.");
        System.out.println("  -n        : Désactive les tests à l'exécution.");
        System.out.println("  -r X      : Limite les registres disponibles à R0 ... R(X-1), où 4 <= X <= 16.");
        System.out.println("  -d        : Active les traces de debug (répétable).");
        System.out.println("  -P        : Active la compilation parallèle si plusieurs fichiers sont fournis.");
        System.out.println("  <source files> : Liste des fichiers source à compiler.");
    }

    public int getRegistreLimitValue() {
        return registreLimitValue;
    }
}
