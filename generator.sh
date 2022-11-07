#!/bin/bash
TITLE='\033[0;31m'
NORMAL='\033[0m'


echo -e "${TITLE}Comptence-Repository${NORMAL}"
API_URL="https://staging.sse.uni-hildesheim.de:9010/api-json"
VERSION=$(wget "$API_URL" -q -O - | grep -oP "(?<=info\":\{).*?(?=\}\})" | grep -oP "(?<=\"version\":\").*?(?=\",)")
LANG="python"
GROUP_ID="net.ssehub.e_learning"
ARTIFACT_ID="competence_repository_api"
PACKAGE="${ARTIFACT_ID}"
MODEL_PACKAGE="model"
API_PACKAGE="api"

if ./chk_server_online.sh $API_URL; then
    mvn clean compile -Dspec_source=$API_URL -Dversion=$VERSION -Dlanguage=$LANG -Dgroup_id=$GROUP_ID -Dartifact_id=$ARTIFACT_ID -Dpackage=$PACKAGE -Dmodel=$MODEL_PACKAGE -Dapi=$API_PACKAGE
    rm -f "${ARTIFACT_ID}_${LANG}.zip"
    cd target/generated-sources/swagger/ > /dev/null
    zip -r -q ../../../"${ARTIFACT_ID}_${LANG}.zip" .
    cd - > /dev/null
else
    exit 1
fi
