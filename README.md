# Webapp

This application is implemented using the Spring Boot v2.0.2.RELEASE.
Maven must be installed in order to build and run the application.

## Build

Run `mvn clean install` in order to build the application.

## Run

Run `mvn spring-boot:run` in order to run the application.
Make sure that the port 8080 is not used.

## Notes

The application is divided in 3 layers:
- Persistence layer
- Service layer
- Web layer

Each layer has its own classes for the entities, although they are almost identical.
This may add some delay in the application,
but it is necessary for complete abstraction between the layers.

The application exposes 2 endpoints:
- `/api/file-data` for `GET` REST requests.
  It returns a list with all the persisted FileData entities
- `/api/file-data/upload` for `POST` REST request.
  The requests consumes a multipart request containing the following parts,
  which are all required:
  - `file`: a multipart file to be uploaded.
  - `title`: a string containing the title given by the user.
  - `description`: a string containing the description given by the user.
  It returns the FileData object created by the uploaded file if success,
  or an error message if an error occurred.
CORS is enabled for all endpoints.

When a file is uploaded, it is saved in the `./file` folder within the project.
This can be changed using the `application.upload.saveFolder` property in the `application.yml` file.
If the file already exists, an exception is thrown preventing the request to be
completed normally.

The application uses an H2 database for persistence, saving the data only in-memory.
If the application is shut down, all the data are lost.
Moreover, the default behaviour is to delete all the uploaded files from the corresponding folder.
This option can be changed by setting the `application.upload.deleteFilesOnShutdown` property to `false`
in the `application.yml` file.

When an error occurs in a request, the application raises and exception and handles it accordingly.
Again, in big loads, it can add delay, as raising an exception is a relatively expensive operation.

In the `MessageConverterConfiguration` class, some application properties (such as write dates as timestamps)
are hardcoded, thus making it impossible to set them from the `application.yml` file,
but I considered it to be an acceptable technique for this particular project,
as the only communication is done with the front end app, which requires this particular format.

Finally, an important enhancement is the addition of build profiles.
In this particular application and within its scope, I considered this feature
not to be necessary.
In a production ready application, various profiles should be created.
