Projet Génie Logiciel – Ensimag

gl02 – 01/01/2025
Avancement du projet

Nous avons terminé l'implémentation de la partie du langage Deca sans objets.
L’implémentation couvre les fonctionnalités de base suivantes :
    vérification lexicale et synthaxique du langage Deca (partie A).
    vérification contextuelle du langage Deca (partie B).
    Génération de code pour les programmes Deca valides (partie C).

Des tests personnels ont été développés et ajoutés dans le répertoire gl02/src/test/deca. Chaque partie possède ses propres tests, accompagnés de scripts d’automatisation permettant de vérifier le bon fonctionnement des différentes étapes.
Scripts de tests personnalisés

1. Tests contextuels (partie B)

Le script test_context_perso.sh permet d’automatiser les tests de la vérification contextuelle. Il exécute les fichiers .deca présents dans le répertoire test/deca/context avec la commande decac -v et vérifie si les sorties obtenues correspondent aux résultats attendus.
Exécution : (depuis le répertoire racine gl02)

./src/test/script/test_context_perso.sh

2. Tests de génération de code (partie C)

Le script test_codegen.sh permet de tester la génération de code. Il exécute les fichiers .deca valides présents dans le répertoire test/deca/codegen/valid/provided/perso, génère les fichiers .ass, et les exécute avec ima pour vérifier les résultats.
Exécution :

./src/test/script/test_codegen.sh

3. Tests pour ARM

Le script test_codegen_arm.sh permet de tester la génération de code. Il exécute les fichiers .deca valides présents dans le répertoire test/deca/codegen/valid/provided/perso, génère les fichiers .s, et les exécute avec ima pour vérifier les résultats.
Exécution :

./src/test/script/test_codegen_arm.sh

Structure des tests
    test/deca/syntax : Contient les tests de vérification syntaxique (partie A).
    test/deca/context : Contient les tests de vérification contextuelle (partie B).
    test/deca/codegen : Contient les tests de génération de code (partie C).
    test_context_perso.sh : Script d’automatisation des tests contextuels.
    test_codegen.sh : Script d’automatisation des tests de génération de code.
