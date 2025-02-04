Why is CSRF token saved in HttpServletRequest

```
@GetMapping("/csrf-token")
public CsrfToken getCsrfToken(HttpServletRequest request) {
    // Retrieve and return the existing CSRF token
    return (CsrfToken) request.getAttribute("_csrf");
}
```

Because :

1. The HttpServletRequest represents the incoming request from the client to the server.
   The CSRF token is tied to the current user session and needs to be associated with each
   request to validate whether the incoming request is legitimate.
2. Why HttpServletRequest Is More Logical for Security Validation:
   Validation of CSRF tokens occurs before the request is processed:
   The server inspects the token sent by the client in the request headers or form fields.
   It compares the token against the one stored in the session.
   This process happens entirely within the context of the HttpServletRequest.
   The HttpServletResponse plays no role in validating requests, making it an unsuitable place to store or reference the CSRF token.
