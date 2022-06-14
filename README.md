# request-already-read Project
Reproducer for the issue [23360](https://github.com/quarkusio/quarkus/issues/23360).

The certificate for running with https is stored into the `resources/certs` folder. In the `application.properties` change the path to the absolute one for the properties:
- quarkus.http.ssl.certificate.files
- quarkus.http.ssl.certificate.key-files

With chrome disable the security check for self-signed certificates:
```
chrome://flags/#allow-insecure-localhost
```
