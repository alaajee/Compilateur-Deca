package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl02
 * @date 01/01/2025
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("Project developped by group 26");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
            System.exit(0); 
            
        }
        if (options.getARM()){
            for (File source : options.getSourceFiles()){
                DecacCompiler compiler = new DecacCompiler(options, source);
                if(compiler.compileARM()){
                    error = true;
                }
            }
            System.exit(0);
        }


        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            if (options.getParallel()) {
                // Utilisation d'un thread pool pour exécuter les compilations en parallèle
                ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

                // Liste pour stocker les tâches de compilation
                List<Callable<Void>> tasks = new ArrayList<>();

                for (File sourceFile :  options.getSourceFiles()) {
                    tasks.add(() -> {
                        DecacCompiler compiler = new DecacCompiler(options, sourceFile);
                        try {
                            compiler.compile(); // Méthode compile() exécutée pour chaque fichier
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la compilation de " + sourceFile.getName());
                            e.printStackTrace();
                            throw e; // Propagation de l'exception pour gestion
                        }
                        return null; // Void Callable
                    });
                }

                try {
                    // Exécution parallèle des tâches
                    List<Future<Void>> futures = executor.invokeAll(tasks);

                    // Vérification des résultats
                    for (Future<Void> future : futures) {
                        try {
                            future.get(); // Bloque jusqu'à la fin de chaque tâche
                        } catch (ExecutionException e) {
                            System.err.println("Erreur pendant la compilation : " + e.getCause());
                        }
                    }
                } catch (InterruptedException e) {
                    System.err.println("La compilation a été interrompue.");
                    Thread.currentThread().interrupt();
                } finally {
                    executor.shutdown(); // Libération des ressources
                }
            }

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
