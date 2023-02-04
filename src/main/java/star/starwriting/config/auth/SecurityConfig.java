//package star.starwriting.config.auth;
//
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//
//@EnableOAuth2Client
//@EnableOAuth2Sso
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http.authorizeRequests()
//                .antMatchers("/api/login/**").permitAll()
//                .antMatchers("/error").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login()
//                .loginPage("/api/login")
//                .failureUrl("/api/login?error=true")
//                .defaultSuccessUrl("/home");
//    }
//}
