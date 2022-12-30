# Notre application web des centres de vaccination français

Nous allons vous présenter l'application web des centres de vaccination français que nous avons réalisée.

## Equipe

ARMUTLU Ethan N°31807867 - DI LIVIO Bruno N°31085303 - NOUSSE Gaëtan N°31803922

## Contexte

L'objectif de ce projet est de créer un site web permettant tout d'abord la prise de rendez-vous simplifiée dans un centre de vaccination pour le COVID-19. De plus, le site web permet une gestion simple des centres, des utilisateurs et des rendez-vous.

## Enrolement du projet

Tout d'abord, vous pourrez retrouver la partie front-end sur ce lien github : https://github.com/Lamkateh/covid-ng-app.

Ensuite, pour lancer simplement le projet, il vous suffit de cloner les deux répertoires github et de lancer la commande suivante sur un des répertoires (attention à bien mettre les deux répertoires dans le même dossier) :

```bash
docker compose up
```

Cela va lancer ce fichier docker compose :

```yaml
version: "3"

services:
    api:
        container_name: api
        build: ./covid-api
        ports:
            - "8080:8080"
        environment:
            DATABASE.HOST: db
            DATABASE.PORT: 5432
            DATABASE.PASSWORD: password
            DATABASE.USERNAME: postgres
            DATABASE.DB: covid-db
            TZ: Europe/Paris
        depends_on:
            - db
    db:
        image: postgres:14
        container_name: db
        environment:
            POSTGRES_PASSWORD: password
            POSTGRES_USER: postgres
            POSTGRES_DB: covid-db
            PGDATA: /var/lib/postgresql/data/pgdata
        ports:
            - "5432:5432"
        volumes:
            - db-data:/var/lib/postgresql/data

        angular:
            container_name: angular
            build: ./covid-ng-app
            environment:
                API_URL: http://api:8080/
                TZ: Europe/Paris
            ports:
                - "4200:80"

volumes:
    db-data:
```

Ainsi, vous pourrez accéder au site web sur le port 4200 et à l'api sur le port 8080.

En effet, le docker compose de la partie back-end va lancer 3 conteneurs : un pour la base de données, un pour l'api et un dernier pour l'application angular.

Afin de récupérer les centres français du site web du gouvernement (conteneur qui se ferme après remplissage de la base de données), il faut exécuter la commande suivante :

```bash
docker run -it --rm --name centercrawling -v "$PWD/covid-api/crawling:/usr/src/crawling" -w /usr/src/crawling --network=<DB-NETWORK> -e HOST=<DB-HOST> -e USER=<DB-USERNAME> -e PASSWORD=<DB-PASSWORD> -e DATABASE=<DB-NAME> python:3.7-alpine sh -c "apk update && apk add build-base && apk add libpq-dev && pip install -r requirements.txt && python centerCrawling.py"
```

## Fonctionnalités

### 1 - Se connecter, s'incrire et se déconnecter

Tout d'abord, il est nécessaire de se connecter et de s'inscrire sur le site web afin de pouvoir utiliser les différentes fonctionnalités du site.
Pour cela, il faut cliquer sur le bouton `Se connecter` de la barre de navigation. Une fois sur la page de connexion, il est possible de se connecter avec un compte existant :

![Connexion](/doc_ressources/login.png)

Pour s'inscrire, il suffit de cliquer sur le lien `Pas encore inscrit ? Inscrivez-vous ici`. Une fois l'inscription effectuée, il est possible de se connecter avec le compte créé :

![Inscription](/doc_ressources/signup.png)

Une fois connecté, il est possible de se déconnecter en cliquant sur le bouton `Se déconnecter` de la barre de navigation.

### 2 - Les différents rôles

Chaque utilisateur de notre site web peut avoir 4 rôles différents :

-   Il peut être un simple patient avec le rôle `PATIENT`. Il pourra seulement prendre un rendez-vous dans un centre de vaccination.

-   Il peut être un médecin avec le rôle `DOCTOR`. Il pourra gérer les rendez-vous de ses patients.

-   Il peut être un administrateur avec le rôle `ADMIN`. Il pourra gérer les médecins associés à son centre de vaccination.

-   Enfin, il peut être un super administrateur avec le rôle `SUPERADMIN`. Il pourra gérer les centres de vaccination, les médecins, les administrateurs et les supers administrateurs.

Il est à noter que tous les utilisateurs, peu importe leur rôle, peuvent prendre un rendez-vous dans un centre de vaccination.

### 3 - Les centres de vaccination

Tout d'abord, la page d'accueil de notre application correspond à la requête GET `/public/centers`. Cette requête permet de récupérer tous les centres de vaccination (à noter que la partie public et private est supprimée dans l'url de la page web).

![Liste des centres](/doc_ressources/centres.png)

On peut constater également qu'il est possible de faire une recherche par ville d'un centre.

### 4 - Les rendez-vous

Une autre fonctionnalité est la prise de rendez-vous. Pour cela, il faut tout d'abord être connecté et qu'au moins un médecin soit associé au centre de vaccination. Ensuite, en cliquant sur ce dernier, on peut voir les rendez-vous disponibles avec la requête GET `/public/centers/{id}/appointments` :

![Liste des rendez-vous disponibles](/doc_ressources/appointments.png)

Une popup s'ouvre alors pour prendre un rendez-vous. Vous pouvez alors confirmer el rendez-vous :

![Confirmation d'un rendez-vous](/doc_ressources/confirm_appointment.png)

### 5 - Gestion des centres de vaccination

En tant que super administrateur, il est possible d'ajouter, de modifier et de supprimer un centre depuis la page `Gestion des centres` :

![Gestion des centres](/doc_ressources/management_centers.png)

Il suffit de cliquer sur le bouton `Nouveau centre` pour en ajouter un. Une popup s'ouvre alors où il suffit de remplir les champs et de cliquer sur `Ajouter` :

![Ajout d'un centre](/doc_ressources/add_center.png)

Pour modifier ou suppimer un centre, il suffit de cliquer sur le bouton `Modifier` ou `Supprimer` du centre en question.

### 6 - Gestion des utilisateurs

Enfin, la dernière fonctionnalité est la gestion des utilisateurs.

Seul les supers administrateurs peuvent gérer les supers administrateurs depuis la page `Configuration` :

Ils sont également les seuls à pouvoir gérer les administrateurs, en cliquant sur le bouton `Gérer les administrateurs et médecins` de chaque centre depuis la page `Gestion des centres` (ou en cliquant sur la petite icône ![Icône des utilisateurs](/doc_ressources/users_icon.png) depuis la popup de modification d'un centre :

![Gestions des administrateurs et des médecins](/doc_ressources/management_users.png)

Depuis cette même popup, il leur est aussi possible de gérer les médecins associés aux centres de vaccination.

Les administrateurs peuvent eux gérer seulement les médecins associés à leur centre de vaccination depuis la page `Gestion de mon centre` :

![Gestion de mon centre](/doc_ressources/management_center.png)

Enfin, les administrateurs ont aussi la possibilité de gérer les rendez-vous de leur centre. Les médecins eux peuvent gérer les rendez-vous de leurs patients. Dans les deux cas, cette gestion ce fait depuis la page `Planning` :

![Mon planning](/doc_ressources/planning.png)

## Conclusion

Cette application web permet de gérer les centres de vaccination et les rendez-vous. Elle permet également de gérer les utilisateurs et leurs rôles.
