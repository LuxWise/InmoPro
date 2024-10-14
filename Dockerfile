FROM ubuntu:latest

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

COPY init-secrets.sh /app/init-secrets.sh
RUN chmod +x /app/init-secrets.sh

EXPOSE 8200 8080

ENV VAULT_ADDR='http://127.0.0.1:8200'v

CMD ["sh", "-c", "vault server -dev -dev-root-token-id=j3H8p2T5mQ9bF6vY4wZ1R0cG7S & /app/init-secrets.sh && java -jar app.jar"]
