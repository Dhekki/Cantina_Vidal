#!/bin/bash

cd /home/ec2-user/Cantina_Vidal || { echo "Repositório não encontrado!"; exit 1; }

echo "Entrou na pasta. Baixando atualizações..."
git pull origin main

echo "Recriando containers..."
docker compose up -d --build

echo "Limpando imagens antigas..."
docker image prune -f

echo "Deploy concluído!"
