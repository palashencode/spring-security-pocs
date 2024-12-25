
## Spring Security POC
This uses JWT auth with Spring Boot

This POC was created with help from Youtibe Video ( https://www.youtube.com/@rytis-codes )
- https://www.youtube.com/watch?v=ac12zNR-OsE&list=PLVuqGBBX_tP3KmownF68ifFmgPQt-ujBg

## Description
- apis kept in /bruno folder
- excalidraw in /excalidraw folder

## Spring Security Architecture

```mermaid
graph TD
    subgraph "Spring Security Request Flow"
	TomcatFilterChain["Tomcat Filter Chain"] --> 
	DelegatingFilterProxy -->
	FilterChainProxy -->
	FilterChain["Spring Security FilterChain"] --> AuthFilter["Authentication Filter
	(note: packages the credentials and passes it to manager)"]
	
    <--> AuthManager["Authentication Manager
	(note: will make sure the creds are verified by provider)"]
    -->|"authenticate()"| AuthProvider["Authentication Provider"]
    end

AuthFilter --> SecurityContext
AuthProvider -->|"loadByUsername(), setUserDetailService()"| UserDetailsService
AuthProvider -->|"matches(), setPasswordEncoder()"| PasswordEncoder
UserDetailsService -->|"findByUsername()"| Database["DataBase\n mongo/sql/in-memory)"]
```