# JWT_security

Question : 병렬 서버 구축 계획으로 요청이 어떠한 서버로 들어갈지 모르기 때문에 세션으로 인증된 회원의 인가가 정확하지 않게 됨.

Answer : 요청이 병렬 서버 중 어떠한 서버로 요청이 들어와도 인가를 처리 할 수 있도록 JWT토큰을 이용한 인증, 인가 절차를 구현함.

[소스 소개]
1. src/main/java/com/xi/fmcs/config/security/jwt/JwtAuthenticationFilter.java
   - AbstractAuthenticationProcessingFilter를 상속받은 인증 Filter이며 인증시 멤버용 JWT 토큰을 생성한다.

2. src/main/java/com/xi/fmcs/config/security/jwt/JwtAuthorizationFilter.java
   - BasicAuthenticationFilter 상속받은 서점용 인가 Filter이다.

3. src/main/java/com/xi/fmcs/config/security/jwt/JwtTokenUtil.java
   - JWT 토큰을 생성한다.

4. src/main/java/com/xi/fmcs/config/security/configuration/WebSecurityConfig.java
   - JwtAuthenticationFilter와 JwtAuthorizationFilter를 SecurityFilterChain 추가한다.
