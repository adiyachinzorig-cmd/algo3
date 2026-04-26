# ATTENTION, ce code a été écrit avec l'aide de Gemini

import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('histogrammeTache1.csv', header=None, names=['Taille', 'Frequence'])

df['Taille'] = df['Taille'].astype(str)

couleur_barres = 'steelblue'    # Code couleur Hexadécimal (ici un orange vif). Tu peux utiliser 'steelblue', 'seagreen', etc.
transparence = 1            # Opacité (0 = invisible, 1 = opaque)
rotation_x = 55               # Angle des étiquettes (45 ou 90 degrés)
police = 'serif'         # Style de texte ('sans-serif', 'serif', 'monospace')

plt.rcParams['font.family'] = police

plt.figure(figsize=(12, 6))

plt.bar(df['Taille'], df['Frequence'], 
        color=couleur_barres, 
        alpha=transparence, 
        width=0.8,
        edgecolor='black', 
        linewidth=0.7)

plt.yscale('log')

plt.grid(axis='y', linestyle='--', alpha=0.4, color='gray', zorder=0)

plt.title("Distribution des fréquences selon la taille", fontsize=16, fontweight='bold', pad=15)
plt.xlabel("Taille", fontsize=12, labelpad=10)
plt.ylabel("Fréquence (Échelle Logarithmique)", fontsize=12, labelpad=10)
plt.xticks(rotation=rotation_x, ha='right' if rotation_x != 90 else 'center', fontsize=10)

plt.tight_layout()
plt.show()