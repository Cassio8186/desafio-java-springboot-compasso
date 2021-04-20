FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine as catalogo_produtos_app
RUN mkdir /app
COPY target/catalogo-produtos.jar /app/catalogo-produtos.jar
WORKDIR /app
ENV server_port="9999"
ENV active_profile="h2"
ENV datasource_host="database"
ENV datasource_port="3306"
ENTRYPOINT ["java","-DSERVER_PORT=${server_port}", "-DACTIVE_PROFILE=${active_profile}", "-DDATASOURCE_HOST=${datasource_host}", "-DDATASOURCE_PORT=${datasource_port}",  "-jar","catalogo-produtos.jar"]
EXPOSE ${server_port}
