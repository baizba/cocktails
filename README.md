# Cocktails

## how to compile and deploy
This application is a standard java 8 webb application. Jut download the repository
and compile it. This is a maven project. To make compilation simple use maven.
Switch to the root directory of the project (where pom.xml file is located) and issue maven command: 
`mvn clean package` This will create a **cocktails.war** inside the target folder.  This
is the packaged application. You can deploy it to java servlet container. I used 
**apache-tomcat-9.0.40** for testing. It is easy to download. If you are using tomcat then
start the server and copy the war file to webapps folder, and it should be automatically
deployed.

## how to use REST endpoints
The app has two REST endpoints. One for creating new concepts. Other for listing all
concepts in RDF XML format.

#### Creating new concepts
Endpoint can be called via **http://localhost:8080/cocktails/app/create** url. This is 
a **PUT** method.
Please pay attention to use appropriate server and port of your local server.
You must also submit a header in the request: **Content-Type: application/json** or it
will not work. The payload is sent in the body of the PUT request and this is the example:  
**PUT** http://localhost:8080/cocktails/app/create  
`Content-Type: application/json`
```
{
    "preferredLabel": "White Russian",
    "alternativeLabels": [ "Ruski", "Russian"],
    "broaderConcept": "Wine cocktail"
}
```
If everything is ok you will get empty response with HTTP status 200. If ther ewas an error
you will get a HTTP code for internal server error (500), and a text message to check the logs.

#### Listing all concepts
Listing all concepts is a simple GET request with Accept header:  
`Accept: text/plain`  
**GET** http://localhost:8080/cocktails/app/get  
Response example:  
```
<?xml version="1.0" encoding="UTF-8"?>
<rdf:RDF 
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">

<rdf:Description rdf:about="http://example.org/cocktail#Mimosa">
	<rdf:type rdf:resource="http://www.w3.org/2004/02/skos/core#Concept"/>
	<prefLabel xmlns="http://www.w3.org/2004/02/skos/core#">Mimosa</prefLabel>
	<altLabel xmlns="http://www.w3.org/2004/02/skos/core#">bla</altLabel>
	<altLabel xmlns="http://www.w3.org/2004/02/skos/core#">huuh</altLabel>
	<altLabel xmlns="http://www.w3.org/2004/02/skos/core#">owiii</altLabel>
	<broader xmlns="http://www.w3.org/2004/02/skos/core#">Wine cocktail</broader>
</rdf:Description>

<rdf:Description rdf:about="http://example.org/cocktail#White Russian">
	<rdf:type rdf:resource="http://www.w3.org/2004/02/skos/core#Concept"/>
	<prefLabel xmlns="http://www.w3.org/2004/02/skos/core#">White Russian</prefLabel>
	<altLabel xmlns="http://www.w3.org/2004/02/skos/core#">Ruski</altLabel>
	<altLabel xmlns="http://www.w3.org/2004/02/skos/core#">kasdnjkldfan</altLabel>
	<altLabel xmlns="http://www.w3.org/2004/02/skos/core#">oasdasi</altLabel>
	<broader xmlns="http://www.w3.org/2004/02/skos/core#">Wine cocktail</broader>
</rdf:Description>

</rdf:RDF>
```

#### Get a single concept
Single concept can be obtained via GET call  
**GET** http://localhost:8080/cocktails/app/get/Mimosa  
`Accept: application/rdf+xml` or `Accept: application/rdf+json`  
This retrieves cocktail with preferred label "Mimosa".
Remember that you must send Accept header with exactly one of the values above.
This header controls if you get XML or JSON response. 
If you send wrong header or do not send it you will get Http status 400 (bad request).
The last part of url "Mimosa" is a preferred label of the concept (cocktail)
For example you can replace "Mimosa" with "White Russian" if you have cocktail with 
preferred label "White Russian".