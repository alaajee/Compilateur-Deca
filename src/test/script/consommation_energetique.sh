#!/bin/bash

# Liste des fichiers .deca à tester
files=("AgetX.deca" "AddPrintDot.deca" "EstGrand.deca")

# Boucle sur chaque fichier
for file in "${files[@]}"
do
    echo "Testing $file..."
    
    # Mesure du temps d'exécution et de la consommation d'énergie
    result=$(perf stat -e power/energy-pkg/ /home/jennine/ensimag/GL/Projet_GL/src/main/bin/decac  $file 2>&1)
    
    # Extraction du temps et de la consommation d'énergie
    energy=$(echo "$result" | grep "power/energy-pkg" | awk '{print $1}')
    time=$(echo "$result" | grep "seconds time elapsed" | awk '{print $1}')
    
    # Affichage des résultats
    echo "Execution time for $file: $time seconds"
    echo "Energy consumption for $file: $energy Joules"
    echo "------------------------------------"
done

