mvn clean package -DskipTests=true
docker build -t ben317523/mycloud .
docker push ben317523/mycloud