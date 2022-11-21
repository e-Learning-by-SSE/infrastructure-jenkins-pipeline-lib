#!/bin/bash
RED='\033[1;31m'
GREEN='\033[1;32m'
TITLE='\033[1;34m'
NORMAL='\033[0m'

chk_availability() {
    attempt_counter=0
    max_attempts=10
    SERVER="$1"
    until $(curl --output /dev/null --silent --head --fail "$SERVER"); do
        if [ ${attempt_counter} -eq ${max_attempts} ];then
        echo -e "${RED}Max attempts reached${NORMAL}"
        return 1
        fi

        printf '.'
        attempt_counter=$(($attempt_counter+1))
        sleep 5
    done
    echo -e "${GREEN}Server reachable at: ${NORMAL}$SERVER"

    # Wait additional time to avoid empty responses
    return 0
}

echo -e "${TITLE}Comptence-Repository${NORMAL}"
API_URL="https://staging.sse.uni-hildesheim.de:9010/api-json"
GROUP_ID="net.ssehub.e_learning"
ARTIFACT_ID="competence_repository_api"
PACKAGE="${ARTIFACT_ID}"
MODEL_PACKAGE="model"
API_PACKAGE="api"

if chk_availability $API_URL ; then
    VERSION=$(wget "$API_URL" -q -O - | grep -oP "(?<=info\":\{).*?(?=\}\})" | grep -oP "(?<=\"version\":\").*?(?=\",)")
    rm -f "${ARTIFACT_ID}*.zip"
    echo -e "${TITLE}Version:${NORMAL} ${VERSION}"

    # Python
    LANG="python"
    mvn clean compile -Dspec_source=$API_URL -Dversion=$VERSION -Dlanguage=$LANG -Dgroup_id=$GROUP_ID -Dartifact_id=$ARTIFACT_ID -Dpackage=$PACKAGE -Dmodel=$MODEL_PACKAGE -Dapi=$API_PACKAGE
    cd target/generated-sources/swagger/ > /dev/null
    zip -r -q ../../../"${ARTIFACT_ID}_${LANG}.zip" .
    cd - > /dev/null
    
    # TypeScript
    LANG="typescript-angular"
    mvn clean compile -Dspec_source=$API_URL -Dversion=$VERSION -Dlanguage=$LANG -Dgroup_id=$GROUP_ID -Dartifact_id=$ARTIFACT_ID -Dpackage=$PACKAGE -Dmodel=$MODEL_PACKAGE -Dapi=$API_PACKAGE
    cd target/generated-sources/swagger/ > /dev/null
    zip -r -q ../../../"${ARTIFACT_ID}_${LANG}.zip" .
    cd - > /dev/null
else
    exit 1
fi
