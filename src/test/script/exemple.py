import matplotlib.pyplot as plt

# Données
files = ['AgetX.deca', 'AddPrintDot.deca', 'EstGrand.deca']
execution_time = [0.161448259, 0.167626053, 0.174375199]  # en secondes
energy_consumption = [3.98, 3.77, 3.79]  # en Joules

# Tracer le graphique des temps d'exécution
plt.figure(figsize=(10, 6))
plt.bar(files, execution_time, color='skyblue')
plt.title('Temps d\'exécution par fichier', fontsize=14)
plt.xlabel('Fichiers', fontsize=12)
plt.ylabel('Temps d\'exécution (secondes)', fontsize=12)
plt.show()

# Tracer le graphique de la consommation d'énergie
plt.figure(figsize=(10, 6))
plt.bar(files, energy_consumption, color='lightgreen')
plt.title('Consommation d\'énergie par fichier', fontsize=14)
plt.xlabel('Fichiers', fontsize=12)
plt.ylabel('Consommation d\'énergie (Joules)', fontsize=12)
plt.show()

