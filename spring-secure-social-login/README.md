
### Description 
POC for Social Login using Spring Security

http://localhost:8090

```chatinput
-Dfile.encoding=UTF-8 -Dspring.profiles.active=
```


Client ID and Client Secret Hardcoded in the code
```java

    private ClientRegistration githubClientRegistration(){
        return CommonOAuth2Provider.GITHUB.getBuilder("github-registrar")
                .clientId("Ov23lix8qZHJWeBmZ0jR")
                .clientSecret("afa1d55ace90cfea9dc8eb38a510fd5a137e7ae1")
                .build();
    }

    private ClientRegistration googleClientRegistration(){
        return CommonOAuth2Provider.GOOGLE.getBuilder("google-registrar")
                .clientId("736116120342-2quk77bfefb49ar6dia70b787e4feda6.apps.googleusercontent.com")
                .clientSecret("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")
                .build();
    }
```