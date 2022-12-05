# syntax=docker/dockerfile:1
FROM alpine:latest
RUN apk update
RUN apk add git openjdk8-jre maven
WORKDIR /wt-aline-underwriter
COPY . .
ENV DB_PASS=secret
ENV SECRET_KEY=secret
ENV JWT_KEY=secret
ENV DB_HOST=secret
ENTRYPOINT exec java -DAPP_PORT=8071 -DDB_USERNAME="root" -DDB_PASSWORD=$DB_PASS -DDB_HOST=$DB_HOST -DDB_PORT="3306" -DDB_NAME="aline_db" -DENCRYPTION_SECRET_KEY=$SECRET_KEY -DJWT_SECRET_KEY=$JWT_KEY -jar aline-underwriter-microservice/underwriter-microservice/target/underwriter-microservice-0.1.0.jar
EXPOSE 8071
