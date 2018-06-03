#!/bin/bash

cd $1
pwd
git init
git remote add origin https://github.com/hornel/super_pong.git
git pull origin $1
echo "*.DS_Store" > .gitignore
git add --all
git commit -am "Initial commit"
git push origin HEAD:$1 --force
