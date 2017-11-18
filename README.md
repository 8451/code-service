
# code-service
[![Build Status](https://travis-ci.org/8451/code-service.svg?branch=develop)](https://travis-ci.org/8451/code-service)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/08fb6c0d89704c1793bacaf237c112ef)](https://www.codacy.com/app/tomd8451/code-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=8451/code-service&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/8451/code-service/badge.svg?branch=develop)](https://coveralls.io/github/8451/code-service?branch=develop)  
[![forthebadge](http://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](http://forthebadge.com)

## Environment Variables

|Environment Variable |Description                                               |Example|
|---------------------|----------------------------------------------------------|-------|
|CUSTOMCONNSTR_code-db|The URI Connection String to connect to the Mongo Database|mongodb://user:password@host:port|
|CODE_WEB_URI         |The base address for the Client Application (ng)          |http://code-web-ui.azurewebsites.net|
|CODE_DATABASE_NAME   |The space the environment is running in.                  |prod   |
|JWT_PRIVATE_KEY      |Private key in PKCS8 format                               |none provided|
|JWT_PUBLIC_KEY       |Public key in X.509v3 format                              |none provided|

## Deployment Instructions

```sh
mvn package
python3 azure-deploy.py -d target/ --host ${AZURE_HOST} -u code-service\\${DEPLOY_USER}
```
