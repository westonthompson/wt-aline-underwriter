export APP_PORT=8060
export DB_USERNAME=root
export DB_PASSWORD=root
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=alinedb
export APP_USER_ACCESS_KEY=
export APP_USER_SECRET_KEY=
export PORTAL_LANDING=
export PORTAL_DASHBOARD=
export ENCRYPT_SECRET_KEY="ThisIsAGreatSecretKey!!!"
export JWT_SECRET_KEY="ThisIsAGreatSecretKeyForJWTSecretsAndStuff!!!"

mvn clean install -DskipTests
mvn spring-boot:run -pl underwriter-microservice