### Start Params

``` bash
# VM PARAMS ( with -D )
-Dfile.encoding=UTF-8
-Dspring.profiles.active=prod

# ENV PARAMS ( in caps )
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET_VALUE=

```

```java
// Optional Configuration to allow login in non-prod without password.
com.example.secure.poc.main.config.EazyBankNonProdUsernamePasswdAuthenticationProvider
com.example.secure.poc.main.config.EazyBankProdUsernamePasswdAuthenticationProvider

```

### Application Properties ( optional )
```chatinput
@ComponentScan(basePackages = {"com.example.secure.poc.noscan","com.example.secure.poc.main"}) 

```

``` bash
docker run -p 3306:3306 --name springsecurity-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=eazybank -d mysql
```

### Cryptography
- encoding ( completely reversible, so secrecy, just convenience e.g. Base64)
- encryption ( symmetric and asymmetric )
  - symmetric ( for data at rest where key never leaves the system e.g. AWS S3 )
  - asymmetric ( for data in motion where public/private key pairs are required )
- hashing ( it is a one-way irreversible process, once hashed we cannot get original data )
  - best for passwords, as hashing is consistent
  - e.g. MD5, BCrypt, Argon, SHA256

```bash
# use git bash
# Base 64
openssl base64 -in data.txt -out encode.txt
openssl base64 -d -in encode.txt -out decode.txt

# Encryption
#This will generate different values for the same data, 
# due to internal salting and Initialization vectors that is added with the encrypted data
openssl enc -aes-256-cbc -pass pass:12345 -pbkdf2 -in data.txt -out encrypt.txt -base64
openssl enc -aes-256-cbc -base64 -pass pass:12345 -d -pbkdf2 -in encrypt.txt -out decrypt.txt

# consistent encrypted data ( highly insecure ) 
openssl enc -aes-256-cbc -pass pass:12345 -in data.txt -out encrypt.txt -base64 -iv 00000000000000000000000000000000 -nosalt

# Hashing ( consistent result )
echo -n "sample data 12345" | openssl dgst -sha256
SHA2-256(stdin)= 1e9519663c622699f95028079b4529a90932bca3c69785bb84c9f030a8bec44d

```

### Api
- http://localhost:8081/swagger-ui/index.html#/status-controller/status


### Description
This project created following the guidance from 
- https://www.udemy.com/course/spring-security-zero-to-master/ 
- https://github.com/eazybytes/spring-security
- CSRF Discussion - https://stackoverflow.com/questions/20504846/why-is-it-common-to-put-csrf-prevention-tokens-in-cookies