# Arbetsplatskoder
Arbetsplatskoder (directly translated to "working place codes") is an application for managing working place codes. Working place codes are required for prescriptions to be subsidized.

## Getting started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites
The following software is required to run the application:

* GIT
* Maven 3+
* Java 8+
* NPM 5+
* Nodejs 8+
* Relational database - tested with PostgreSQL 9.5

Aso keystore and truststore files need to be created/acquired prior to launching the application (for the KIV integration).

### Running the application
Place the files below in ${user.home}/.app/arbetsplatskoder and remove the .template suffix.

```
application.properties.template
export.jdbc.properties.template
main.jdbc.properties.template
quartz.properties.template
```

Also place the truststore and keystore files in line with your application.properties file.

Edit the files according to your environment.

Go to the project root and execute:

```
mvn install
mvn tomcat7:run
```

Then open another terminal and cd to the project root. Then execute:

```
cd core-bc/modules/client
npm install
npm start
```

Now hit `localhost:4200` with your browser.

Default admin credentials can be set in application.properties. A properly encoded password can be generated via `se.vgregion.arbetsplatskoder.util.PasswordEncoder.main` method.

### Deployment
There are many options for deployment but these are used at Västra Götalandsregionen:

* Apache HTTP Server
* Apache Tomcat

First copy the property files to the user home directory described above (the user running the Apache Tomcat) and edit them to your needs.

#### Build client
The client module is built as static web resources.

```
cd core-bc/modules/client
npm run build-prod
```

Then copy the contents under the dist folder to the Apache HTTP server directory used to serve the content.

#### Build server
Make sure you're in the project root. Then:

```
mvn package
```

Then deploy `core-bc/modules/intsvc/target/arbetsplatskoder.war` to Apache Tomcat.

#### Apache HTTP Server configuration
For the Angular application to function properly all URL:s not pointing to a specific resource should return index.html. In addition, URL:s with a certain context path should be proxied to Apache Tomcat. This is an example configuration which assumes /api is proxied to Tomcat and the static files are located under /var/www/apk:

```
<VirtualHost *:443>
        ServerName domain.example.com

        DocumentRoot /var/www/apk

        RewriteEngine On
        # If an existing asset or directory is requested go to it as it is
        RewriteCond %{DOCUMENT_ROOT}%{REQUEST_URI} -f [OR]
        RewriteCond %{DOCUMENT_ROOT}%{REQUEST_URI} -d [OR]
        RewriteCond %{REQUEST_URI} ^/?(api)/
        RewriteRule ^ - [L]

        # If the requested resource doesn't exist, use index.html
        RewriteRule ^ /index.html

        ProxyPass /arbetsplatskoder ajp://localhost:8009/arbetsplatskoder

        CustomLog ${APACHE_LOG_DIR}/application.log combined
</VirtualHost>
```

## Technical overview
The core frameworks used are:

* Angular (client)
* Spring Framework (server)

### Integrations
The only integration for fetching data is to KIV (Katalog i Väst) which is used for searching units when creating/editing working place codes. The KIV integration is also used for finding out when HSA ID:s stored in the application no longer exists in KIV.

There are five "outputs" of the application:

* EHM (E-hälsomyndigheten) - The application creates a file which is placed on a Samba share.
* Strålfors - The application creates a file which is placed on a Samba share.
* KIV - Database integration. Entites which are subsets of working place codes are inserted into a table in an external database.
* Sesam LMN - Database integration. Data is inserted into two tables in an external database.
* Loke - Database integration. Data is inserted into two tables in an external database.

### Scheduling
The scheduling is performed with Quartz. The reason is that Quartz handles scheduled jobs in a clustered environment. Only one node should perform each job each time and if one node is down it is the other node's responsibility.

## Importing data from the old application
Do the following steps to import the old data with subsequent modifications (make sure you have legacy.jdbc.properties on the computer you're running from):

1. Run se.vgregion.arbetsplatskoder.db.migration.Job#main to import the data "as-is".
2. Start the application to modify entites to conform to the JPA entities as well as transform some of the data to new entity structures (via BatchService).
3. Stop the application.
4. Run se.vgregion.arbetsplatskoder.db.migration.level.Levels#main to update data.
5. Run se.vgregion.arbetsplatskoder.db.migration.ImportHistory.main to copy old structure of historic changes to the new structure.
6. The application is now ready.
