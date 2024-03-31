[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/I1wyqPeY)


# Mobil Device Repairs

### Submission 2024-03-17

### [GitHub Repository](https://github.com/Distansakademin/dbh-vt24-inlamning-davidobrant)


## Instruktioner

1. Klona repo från Github och installera depencencies med maven.
2. Touch `.env` i root-mapp och sätt variablerna `DB_URL`, `DB_USERNAME` och `DB_PASSWORD`. 
3. (a) Kör MySQL lokalt port:3306 och skapa databas `repair_shop`.
3. (b) Anslut en AWS RDS instans och skapa schema `repair_shop` i mysql-workbench.
4. Skapa och kör AWS S3 instans.
5. Skapa IAM-användare med `AmazonS3FullAccess`-rättigheter. Installera aws-cli och skapa `~/.aws/credentials`. 
6. Kör `Main.java`

## Utvecklingsprocess

Först bestämde jag vilka entiteter som behövdes och fortsatte att skapa modeller för dessa med tillhörande variabler. 
Näst skapade jag ER-diagram för att måla upp vilka relationer klasserna behövde ha för att enkelt kunna få tillgång till 
relevant information för de olika användningsfallen som behövs. Ingen onödig data ska sparas på mer än ett ställe.

När ovanstående grundstenar var på plats började jag med att skapa tabeller för databasen och satte upp anslutningen till java-applicationen.
Då backend var redo började jag skapa ett UI bit för bit allt eftersom jag implementerade CRUD-operationer till modellerna. 

Jag lade mycket tid och vikt vid att smidigt kunna hantera operationer för flera modeller, varför jag använder i princip endast 
generiska functioner i basrepositoriet. Resterande repositorier ärver från bas och allt som behövs justeras är vilken entiteten tillhör. 
Detta arbetssätt sparade mycket tid på att slippa skriva onödigt mycket sql-kod. 

All data flödar från UI <-> classService <-> classRepo <-> basRepo <-> DB.



## Databasstruktur

VG se `../<package>/init/Setup.java` för script som skapar samtliga tabeller. 

### ER-Diagram
![ER-Diagram](/readme/er-diagram.png)


## Funktioner och Kodstruktur

Det mesta kraften ligger i `BaseRepository.java` samt i `Utilities.java` för komplicerade funktioner. 

`Printer.java` hanterar interaktioner från användare i terminalen med "prints" och "prompts". 

`Formatter.java` gör just det - formatterar visning av information. Skulle man utveckla och bygga mer användarvänlig frontend skulle 
man använt formatter mer utförligt. 

Menyer ärver från `Menu.java` och underlättar för att slippa duplicering av kod.  
Filträdet är utformat för att vara så lättläst och lättförståeligt som möjligt. 

Från början skapade jag även mysql-funktioner som hanterade identitetsnummer för de behövande klasserna. 
Då detta krävde SUPER privileges på aws och jag inte lyckades komma åt den funktionen skapade jag funktionaliteten i javan istället. 

Jag har lärt mig mycket under resans gång och tror att jag exempelvis hade använt mig mer av statiska metoder i nästa projekt. 
Det finns mycket att utveckla vidare med applikationen men har lagt väldigt mycket effektiv tid på att förbättra befintlig kod i detta projekt. 

Kul med S3. Har byggt så att filer tillhörande samma order sparas i samma mapp under ordernummer. Filerna tas även bort
från både S3 och lokalt (downloads) vid avslutande av order.



## Testning

...


