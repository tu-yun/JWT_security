# JWT_security

다중 서버 구축 계획으로 인하여 세션 사용이 불가능하여 JWT 토큰을 이용한 로그인 시스템 구현

1. AbstractAuthenticationProcessingFilter를 상속받은 인증 성공 후 token 생성하는 JwtAuthenticationFilter 구현.

2. BasicAuthenticationFilter를 상속받은 인가용 JwtAuthorizationFilter 구현

3. HttpSecurity에 JwtAuthenticationFilter 와 JwtAuthorizationFilter 등록
