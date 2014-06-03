#!/bin/bash
git checkout .;
git pull;

# Restart server
sudo forever stop nanar;
sudo NODE_ENV=production forever --uid "nanar" -a start server/app.js;
