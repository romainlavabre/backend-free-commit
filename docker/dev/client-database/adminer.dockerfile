FROM adminer:standalone
COPY ./adminer.css client-database-resource-adminer.css
RUN sed -E -i 's/adminer\.css/client-database-resource-adminer.css/gi' adminer.php \
    && sed -E -i 's/adminer\.css/client-database-resource-adminer.css/gi' index.php