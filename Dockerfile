FROM ubuntu:latest

LABEL authors="sanch"

RUN apt-get update && apt-get install -y \
    curl \
    openjdk-17-jdk \
    unzip \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://releases.hashicorp.com/vault/1.13.0/vault_1.13.0_linux_amd64.zip -o vault.zip \
    && unzip vault.zip \
    && mv vault /usr/local/bin/ \
    && rm vault.zip

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 9010

ENV VAULT_ADDR=http://vault:8200
ENV VAULT_TOKEN=hvs.Rd92loaeGA2tnSaew4mXzPdn

ENTRYPOINT ["sh", "-c", \
    "vault kv get -format=json secret/application | jq -r '.data.data | to_entries | .[] | \"export \" + .key + \"=\" + .value' > /app/env.sh && \
    source /app/env.sh && \
    java -jar app.jar"]
