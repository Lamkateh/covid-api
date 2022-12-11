# Centres de vaccination.

Voici le site de web de centre de vaccination français.

## Equipe

Armutlu Ethan N°31807867 -
Di Livio Bruno N° 31085303 -
Nousse Gaëtan N°

## Contexte

L'objectif de ce projet est de créer un site web permettant tout d'abord la prise de rendez-vous simplifier dans un centre de vaccination pour le COVID-19. De plus, le site web permet une gestion des centres et des rendez-vous simple.

## Enrolement dans le projet

Tout d'abord, vous pourrez retrouver la partie front-end sur ce lien github : https://github.com/Lamkateh/covid-ng-app.

Ensuite, pour lancer simplement le projet, il vous suffit de cloner les deux répertoire github et de lancer la commande suivante sur un des répertoires (Attention à bien mettre les deux répertoires dans le même dossier) :

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

#    angular:
#        container_name: angular
#        build: ./covid-ng-app
#        environment:
#            API_URL: http://api:8080/
#        ports:
#          - "4200:80"

volumes:
  db-data:
```

Ainsi, vous pourrez accéder au site web sur le port 4200 et l'api sur le port 8080.

En effet, le docker compose de la partie back-end va lancer 3 conteneurs : un pour la base de données, un pour l'api et un dernier pour l'application angular.

Afin de récupérer les centres français du site web du gouvernement (conteneur qui se ferme après remplissage de la base de données ), il faut exécuter la commande suivante :

```bash
docker run -it --rm --name centercrawling -v "$PWD/covid-api/crawling:/usr/src/crawling" -w /usr/src/crawling --network=<DB-NETWORK> -e HOST=<DB-HOST> -e USER=<DB-USERNAME> -e PASSWORD=<DB-PASSWORD> -e DATABASE=<DB-NAME> python:3.7-alpine sh -c "apk update && apk add build-base && apk add libpq-dev && pip install -r requirements.txt && python centerCrawling.py"
```

## Fonctionnalités

Tout d'abord, un utilisateur de notre site web peut avoir 4 rôles différents :

Il peut être un simple utilisateur avec le rôle `USER`. Il pourra seulement prendre un rendez-vous dans un centre de vaccination.

Il peut être un docteur avec le rôle `DOCTOR`. Il pourra gérer les rendez-vous de ses patients.

Il peut être un administrateur avec le rôle `ADMIN`. Il pourra gérer les centres de vaccination et les docteurs.

Enfin, il peut être un super administrateur avec le rôle `SUPERADMIN`. Il pourra gérer les centres de vaccination, les docteurs et les administrateurs.

Il s'agit désormais d'étudier quelques fonctionnalités de notre site web.

Tout d'abord, la page d'accueil de notre site web correspond à la requête GET `/public/centers`. Cette requête permet de récupérer tous les centres de vaccination (A noter que la partie public et private est supprimé dans l'url de la page web).

![List_centres](/doc_ressources/centres.png)

On peut constater également qu'il est possible de faire une recherche par ville d'un centre.

Une autre fonctionnalitée est la prise de rendez-vous. Pour ce faire, il faut tout d'abord se connecter. Ensuite, en cliquant sur un centre de vaccination, on peut voir les rendez-vous disponibles avec la requête GET `/public/centers/{id}/appointments` :

![List_appointments](/doc_ressources/appointments.png)

Enfin, une autre fonctionnalité de notre site web est l'ajout de centre et de docteur. Pour ce faire, il faut se connecter en tant qu'administrateur ou super administrateur. Ensuite, il faut cliquer sur le bouton `Ajouter un centre` ou `Ajouter un docteur` :

![Add_center](/doc_ressources/add_center.png)

```

```
