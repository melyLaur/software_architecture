# Architecture Decision Record

**Date**: 26/05/2025

## Contexte

La gestion actuelle du parking se fait manuellement par e-mail et Excel, ce qui engendre une charge de travail importante pour les secrétaires et un manque d’autonomie pour les employés.  
L’objectif est de remplacer ce processus par une application simple et intuitive, adaptée aux différents profils utilisateurs (employés, secrétaires, managers), avec gestion des réservations, check-in par QR code, accès mobile et statistiques d’utilisation.

## Décision

- **Client** : Web app avec **Nuxt 3**
- **API** :
    - **Technologie** : **Spring (Java)**
    - **Architecture** : Architecture Hexagonale pour refléter la réalité du domaine métier dans le code puis implémenter l’infrastructure.
    - **Approche** : **API First** – l’API regroupe le domaine métier et ses règles. Elle expose uniquement les routes nécessaires pour récupérer des informations et effectuer des traitements. Cette approche anticipe également le développement potentiel d’une application mobile.
- **Base de données** : **PostgreSQL**

## Conséquences

- **Nuxt (Vue.js)** permet de créer une interface utilisateur moderne, réactive et maintenable.
- **Spring** offre un backend robuste, évolutif et bien adapté à la gestion de règles métier complexes (check-in, rôles, gestion de créneaux).
- **PostgreSQL** assure une gestion fiable et performante des données relationnelles, parfaitement adaptée aux besoins en réservations, historiques et statistiques.
