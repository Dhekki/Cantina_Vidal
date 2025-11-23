#!/bin/bash

cd /home/ec2-user/Cantina_Vidal || echo "Repositório não encontrado!" && exit

git pull origin main

docker compose up -d --build

docker image prune -f

echo "Deploy concluído!"
