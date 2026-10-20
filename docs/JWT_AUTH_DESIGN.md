# Voyage-AI — Future JWT Authentication & Authorization Security Specification

**Document Version:** 1.0  
**Status:** Approved Architectural Specification (Phase 2 Implementation Target)  
**Scope:** Architecture & Contract Specification Only (No implementation in Phase 0)

---

## 1. Overview & Strategy

The approved authentication strategy for Voyage-AI is **stateless JWT (JSON Web Token) Bearer authentication** with access tokens held exclusively in **in-memory JavaScript application state**.

This document details the lifecycle, security requirements, and threat mitigations to ensure secure implementation in Phase 2.

---

## 2. Token Lifecycle & Transport

### 2.1 Token Issuance
1. **Endpoint:** `POST /api/v1/auth/login` (and `POST /api/v1/auth/register`).
2. **Payload:** User submits credentials (`email` and `password`).
3. **Verification:** Server validates user existence and verifies the submitted password against the stored BCrypt hash.
4. **Token Generation:**
   - Server signs a JWT using HMAC-SHA256 (`HS256`) or HMAC-SHA512 (`HS512`).
   - Standard Claims:
     - `sub`: User ID (UUID or numeric primary key)
     - `email`: User email address
     - `iat`: Issued at timestamp
     - `exp`: Expiration timestamp (e.g. 15 minutes to 1 hour)
   - Custom Claims:
     - `displayName`: User display name
5. **Response:** Server returns the JWT in the JSON response body:
   ```json
   {
     "success": true,
     "message": "Authentication successful",
     "data": {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
       "tokenType": "Bearer",
       "expiresIn": 3600
     }
   }
   ```

### 2.2 Client-Side Storage: In-Memory Only
- **Strict Rule:** The access token is held **strictly in React application memory** (e.g., via React Context or a dedicated auth state hook).
- **Prohibited Storage:**
  - ❌ `localStorage`: Highly vulnerable to XSS token exfiltration.
  - ❌ `sessionStorage`: Vulnerable to XSS scripts reading tab storage.
  - ❌ URL query parameters: Vulnerable to browser history sniffing, referrer leaks, and proxy logging.
  - ❌ Plain client-side cookies accessible to JavaScript.

### 2.3 Token Transport
For all protected endpoints (e.g. `/api/v1/trips/**`), the client attaches the token in the standard HTTP request header:
```http
Authorization: Bearer <token>
```

### 2.4 Token Expiry & Expired Token Handling
- When the token expires (`now > exp`), the server's `JwtAuthenticationFilter` rejects the request with HTTP `401 Unauthorized` and error code `TOKEN_EXPIRED`.
- The client-side HTTP interceptor catches 401 errors, clears the in-memory token, and redirects the user to the login dialog/page.
- Refresh tokens are **deliberately excluded** from the MVP baseline to minimize unnecessary infrastructure complexity.

### 2.5 Logout & Client-Side Token Removal
- When the user selects "Sign Out", the client clears the in-memory token state, immediately terminating authenticated access for subsequent client actions.
- The client calls `POST /api/v1/auth/logout` to allow the server to log the sign-out event.

### 2.6 Server-Side Invalidation Expectations & Limitations
- Because JWTs are stateless by design, a issued token remains cryptographically valid until its expiration time (`exp`) unless a server-side denylist/cache is introduced.
- **MVP Approach:** Short token lifespans (e.g. 30–60 minutes) minimize the exposure window upon logout without requiring distributed Redis cache infrastructure.
- If immediate server-side revocation is ever required prior to expiry, a lightweight in-memory cache (such as Caffeine) recording invalidated token JTI identifiers can be introduced without breaking client contracts.

---

## 3. Server-Side Protection & Ownership Enforcement

### 3.1 Security Filter Chain
In Phase 2, Spring Security's `SecurityFilterChain` will be configured as follows:
- **Public Endpoints (Permit All):**
  - `GET /api/v1/health`
  - `GET /actuator/health`
  - `POST /api/v1/auth/register`
  - `POST /api/v1/auth/login`
  - `GET /api/v1/destinations/**`
  - `GET /api/v1/listings/**`
- **Protected Endpoints (Authenticated):**
  - `GET /api/v1/me/**`
  - `POST /api/v1/trips/**`
  - `GET/PUT/DELETE /api/v1/trips/**`
  - `POST /api/v1/trips/{id}/itinerary-proposals`

### 3.2 Server-Side Resource Ownership Enforcement
- **Strict Rule:** Authentication alone is insufficient. The server must verify that the authenticated subject is the **owner** of the specific requested trip.
- When mutating or reading a trip:
  ```java
  // In TripService.java:
  Trip trip = tripRepository.findByIdAndOwnerId(tripId, authenticatedUserId)
      .orElseThrow(() -> new ResourceNotFoundException("Trip not found or unauthorized"));
  ```
- Cross-user access attempts return `403 Forbidden` (or `404 Not Found` to prevent resource ID enumeration).

---

## 4. Threat Modeling & Mitigations

### 4.1 Cross-Site Scripting (XSS)
- **Risk:** Malicious scripts injected into the web application reading auth tokens.
- **Mitigation:**
  - In-memory token storage prevents permanent persistence on disk or storage.
  - React's default JSX encoding prevents DOM injection.
  - Strict Content Security Policy (CSP) headers in deployed environments.

### 4.2 Cross-Site Request Forgery (CSRF)
- **Evaluation:** CSRF attacks rely on browsers automatically attaching ambient credentials (cookies) to cross-origin requests.
- **Mitigation:** Because authentication relies exclusively on the custom `Authorization: Bearer <token>` header (which browsers **never** attach automatically to cross-origin requests), the REST API is inherently immune to CSRF. CSRF protection in Spring Security can be safely disabled for stateless REST endpoints.

### 4.3 Cross-Origin Resource Sharing (CORS)
- Spring Web CORS configuration strictly limits incoming requests to authorized frontend origins (e.g. `http://localhost:5173` in development).
- `allowedHeaders` explicitly lists `Authorization`, `Content-Type`, and `Accept`.

### 4.4 Password Hashing
- Plaintext passwords must never touch persistent storage or application logs.
- User passwords will be hashed using `BCryptPasswordEncoder` with a cost factor of 10 or 12 prior to database insertion.
