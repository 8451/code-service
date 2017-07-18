# code-service

## Environment Variables

|Environment Variable |Description                                               |
|---------------------|----------------------------------------------------------|
|CUSTOMCONNSTR_code-db|The URI Connection String to connect to the Mongo Database|
|CODE_WEB_URI         |The base address for the Client Application (ng)          |

## Deployment Instructions

```sh
mvn package
python3 azure-deploy.py -d target/ --host ${AZURE_HOST} -u code-service\\${DEPLOY_USER}
```