# Git Commit Message Instructions

- Use the conventional commit message format for clarity and consistency.
- Start with a type prefix: `feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, or `chore:`.
- Write the summary in imperative mood, e.g., `Add authentication module`, not `Added authentication module`.
- Limit the summary (first line) to 50 characters.
- Separate the summary from the body with a blank line.
- Wrap body lines at 72 characters for readability.
- Provide additional context in the body if needed, using bullet points for multiple items.
- Reference related issues or pull requests when applicable, e.g., `Closes #123`.
- Ensure the message is concise, clear, and relevant to the changes made.
- Example: `feat: Add user authentication module`

## Extended Format for Complex Changes

For significant features or fixes, include these sections:

### How was this implemented?
- Briefly explain the technical approach or solution method
- Mention key technologies, patterns, or algorithms used
- Note any important architectural decisions

### What should reviewers focus on?
- Highlight critical code sections that need careful review
- Point out potential edge cases or performance considerations
- Mention any breaking changes or migration requirements
- Identify areas where you'd like specific feedback

## Example

```
feat: Add user authentication module

Implements JWT-based authentication with refresh token rotation.

## How was this implemented?
- Used bcrypt for password hashing with salt rounds of 12
- Implemented JWT access tokens (15min) + refresh tokens (7d)
- Added Redis for refresh token blacklisting on logout
- Created middleware for route protection

## What should reviewers focus on?
- Token expiration handling in AuthMiddleware.js
- Password validation logic in UserController.js
- Redis connection error scenarios
- Rate limiting on login attempts

Closes #123
```