### 🐳 Dockerisation de la base de données

Dirigez-vous dans le répertoire docker ` cd docker`.

Exécutez la commande `docker-compose up -d`, puis,
`docker exec -it fsr bash`.
Par défaut, le mot de passe pour entrer dans MySQL est `root`. Il est modifiable directement dans le fichier `docker/docker-compose.yml` dans la section `MYSQL_ROOT_PASSWORD`.
Pour entrer dans l'instance MySQL, exécutez la commande `mysql -uroot -proot`. La section `-proot` changera en fonction du mot de passe que vous auriez déterminé.
En cas d'arrêt du container, pour pouvoir le relancer, exécutez la commande `docker start fsr`, puis, `docker exec -it fsr bash`.
