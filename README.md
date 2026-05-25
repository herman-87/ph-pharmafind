# PharmaFind User Service - Authentication Flows

This document describes the authentication mechanisms implemented in the PharmaFind User Service.

## Overview

The service supports multiple authentication methods:
- Local authentication (username/password)
- Google OAuth2 authentication
- JWT-based stateless authentication
- Refresh token mechanism for maintaining sessions

## Authentication Endpoints

### Local Authentication

#### Registration
```
POST /api/auth/register
```
Registers a new user with email and password.

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response:**
- 201 Created: User successfully registered
- 409 Conflict: Username or email already exists

#### Login
```
POST /api/auth/login
```
Authenticates a user and returns access and refresh tokens.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
- 200 OK: Authentication successful
  ```json
  {
    "accessToken": "string",
    "refreshToken": "string",
    "tokenType": "Bearer",
    "expiresIn": "number (seconds)"
  }
  ```
- 401 Unauthorized: Invalid credentials

#### Token Refresh
```
POST /api/auth/refresh
```
Obtains a new access token using a refresh token.

**Request Body:**
```json
{
  "refreshToken": "string"
}
```

**Response:**
- 200 OK: New token pair generated
  ```json
  {
    "accessToken": "string",
    "refreshToken": "string",
    "tokenType": "Bearer",
    "expiresIn": "number (seconds)"
  }
  ```
- 401 Unauthorized: Invalid or expired refresh token

#### Logout
```
POST /api/auth/logout
```
Invalidates a refresh token.

**Request Body:**
```json
{
  "refreshToken": "string"
}
```

**Response:**
- 204 No Content: Successfully logged out
- 400 Bad Request: Invalid request

### Google OAuth2 Authentication

#### Initiate Google Login
```
GET /oauth2/authorization/google
```
Redirects user to Google's OAuth2 consent screen.

#### Google Callback
```
GET /login/oauth2/code/google
```
Handles the callback from Google after user authentication.

**Process:**
1. User is redirected to Google for authentication
2. After successful authentication, Google redirects back to the callback URL
3. The application exchanges the authorization code for user information
4. If the user exists in the system, generates JWT tokens
5. If the user doesn't exist, creates a new user account
6. Redirects to the frontend with tokens in URL parameters

**Success Response:**
Redirects to: `{frontendLoginUrl}?access_token={token}&refresh_token={token}&token_type=Bearer`

**Failure Response:**
Redirects to: `{frontendLoginUrl}?error={errorMessage}`

## Security Features

### Password Security
- Passwords are encoded using BCrypt before storage
- Password normalization (trimmed and lowercased) for consistency

### Username/Email Handling
- Usernames and emails are normalized (trimmed and lowercased) before storage and lookup
- Case-insensitive username and email authentication

### Token Security
- Access tokens are JWT signed with RSA private key
- Refresh tokens are stored in database and rotated on use
- Tokens have configurable expiration times

### Brute Force Protection
- Failed login attempts are tracked per IP address
- Temporary IP bans after too many failed attempts
- Automatic cleanup of old records

## Token Usage

### Access Token
- Used for authenticating API requests
- Sent in Authorization header: `Bearer {accessToken}`
- Short-lived (typically 15 minutes)
- Contains user roles and scopes

### Refresh Token
- Used to obtain new access tokens without re-authentication
- Sent in request body for refresh endpoint
- Longer-lived than access tokens
- Rotated on each use (old token invalidated, new token generated)

## User Roles and Authorities

### Default Role
- New users are assigned the `ROLE_USER` role by default

### Authority Mapping
- Roles from JWT are mapped to `ROLE_*` authorities
- Scopes from JWT are mapped to `SCOPE_*` authorities
- Used for method-level and endpoint-level security

## Configuration

Authentication behavior can be configured through:
- `application.yaml` or `application-local.yaml`
- `AppSecurityProperties` class
- `JwtProperties` class

Key configuration properties:
- Token expiration times
- RSA key pair for JWT signing
- Frontend redirect URLs for OAuth2
- Brute force protection thresholds

## Error Handling

Authentication errors return appropriate HTTP status codes:
- 400 Bad Request: Invalid request format
- 401 Unauthorized: Invalid credentials or tokens
- 403 Forbidden: Insufficient permissions
- 409 Conflict: Resource already exists (duplicate username/email)
- 500 Internal Server Error: Unexpected server error

Error responses follow the `ApiErrorResponse` format:
```json
{
  "timestamp": "string (ISO datetime)",
  "status": "number",
  "error": "string",
  "message": "string",
  "path": "string"
}