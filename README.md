# MoneyLion Assessment - Informal Design Document

### Note:
* **I've used [Project Lombok](https://www.baeldung.com/intro-to-project-lombok) to reduce the amount of manually written boilerplate code. If it's not acceptable to use Lombok and the assessors would like me to write the boilerplate myself, please let me know, I'd be happy to do so.**
* I have deployed the same application to Heroku (https://features-access-control.herokuapp.com/feature) as well if someone just wants to do functional testing without having to build.
* For demo purpose, at every start/restart the appliction will drop the required tables and create them newly and inject with the data as present in [/src/main/resources/data.sql](https://github.com/shubhamsharma1304/moneylion-assessment/blob/master/src/main/resources/data.sql)
* Swagger UI and documentation for the API is available at [SERVER_URL]/swagger-ui.html (https://features-access-control.herokuapp.com/swagger-ui.html if you don't want to build locally).

### Design decisions and their justifications:
1. #### The relationship between Feature and User(email) entities:<br>
    * Presented with the requirement, it was clear that the relationship between a Feature and a User is many-to-many "can be accessed by" realtionship. Meaning, a Feature can be accessed by mutiple Users, and also multiple features can be accessed by a single User.
    * To represent such an entity relationship in the database, it is usual to have a Features table and a Users table and a join table for recording a relationship between a Feature and a User. But doing so will also require to maintain three different tables and all their data related functions and relationships.
    * From the requirements, it also seems that this microservice is more Feature centric and is supposed to be the maintainer/owner of the Features. Therefore, complying to microservices philosophy, an assumption has been made that this service will not be a maintainer/owner of Users, and that either this service will not validate the users against a master table of Users at all. Or if it requires to do so, it will be done through calling another microservice that would be the owner/maintainer of users and validating Users.
    * For the scope of this assessment assignment, I have assumed that this service will not concern itself with validating the users against a master table of Users.
    * In light of the above, we now have another option for defining relationship between Feature and User and that is to:
        * Have a Features table which will be considered as a master table of all Features
        * Have a Features-Users table which has three columns:
            * First column is a foreign key from Features table's primary key
            * Second column is the identifier of the User (in this case an email address) - this is taken from user input and there's no referential integrity constraint on this column by itself
            * Third column is a boolean or any other bistate construct that will mark if the Feature-User relationship represented by the record is considered enabled or not
            * The first and the second columns together form a composite primary key for the Features-Users table
        * Now, the relationship between the Features table and the FeaturesUsers table is one-to-many, but it can still represent a many-to-many relationship between Feature entity and User entity
    * Even though this service will not validate the User against a master table of Users, but still it will validate the data format. In this case a user is represented by an email, so meaning the email provided to the endpoint(s) of this service would still be validated to check if they have a valid email address format.
    * Whereas, the Feature will be validated both in terms of data format and against the master table also (as this service is considered the owner of the Features table)
2. When trying to query a feature access for a certain email, if a record doesn't exist for that combination of feature name and email, there were two possible ways this could have been presented to the user. Either:
    * Consider this as though the given user cannot access the given feature and hiding this detail from the user OR
    * Consider this as a different situation altogether and informing the user more accurately (404 status with error message).
    **This approach was chosen for being the one that presents more accurate information to the user and also keeping in mind future extensibility (what if in future, the API consumer wants to take different action  in case of "Record not found"?)**
3. When trying to query a feature access for a certain email, if there's no matching feature in the Features master table, this will be considered as a different error (it will return the same 404 status as the no. 2 above, but with a different error message, thereby giving more accureate information to the user, so they can take appropriate action)
4. As per the spec, the API POST operation can only either give 200 (if creation/update successful) or 304 statuses (if creation/update not successful). To ensure this behaviour as per spec and to ensure that the server doesn't send other status if there was some other issues (maybe like DB connectivity), we have to catch any and all exceptions during the DB operation and then make it so that any exception during the create/update DB operation results in 304 status. This is not a good practice usually, but was done to comply with the spec requirements.
5. #### Ommission of DAO layer:
    * Since the overall business logic of the application is fairly simple and there's no special need for data related manipulation, we can make do without a DAO, and having the service directly access the repository. This reduces the number of un-necessary classes that don't contribute towards the functionality.
6. #### Use of Lombok:
    * [Project Lombok](https://www.baeldung.com/intro-to-project-lombok) is used to reduce the amount of boilerplate code that was needed to be written manually.
