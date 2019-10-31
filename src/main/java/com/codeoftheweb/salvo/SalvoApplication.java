package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

        public static void main(String[] args) {
                SpringApplication.run(SalvoApplication.class, args);

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,ShipRepository shipRepository,SalvoRepository salvoRepository ,ScoreRepository scoreRepository) {
                return (args) -> {

                        // save a couple of Players
                        Player player1=new Player("JackBauer@gmail.com", passwordEncoder().encode("24"));
                        Player player2=new Player("ChloeOBrian@gmail.com",passwordEncoder().encode("42"));
                        Player player3=new Player( "KimBauer@gmail.com",passwordEncoder().encode("kb"));
                        Player player4=new Player( "Tony Almeida",passwordEncoder().encode("mole"));


                        playerRepository.save(player1);
                        playerRepository.save(player2);
                        playerRepository.save(player3);
                        playerRepository.save(player4);

                        Game game1=new Game((long) 0);
                        Game game2=new Game((long) 1);
                        Game game3=new Game((long) 2);
                        Game game4=new Game((long) 3);
                        Game game5=new Game((long) 4);
                        Game game6=new Game((long) 5);
                        Game game7=new Game((long) 6);
                        Game game8=new Game((long) 7);

                        gameRepository.save(game1);
                        gameRepository.save(game2);
                        gameRepository.save(game3);
                        gameRepository.save(game4);
                        gameRepository.save(game5);
                        gameRepository.save(game6);
                        gameRepository.save(game7);
                        gameRepository.save(game8);


                        GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
                        GamePlayer gamePlayer2 = new GamePlayer(game1,player2);

                        GamePlayer gamePlayer3 = new GamePlayer(game2,player1);
                        GamePlayer gamePlayer4 = new GamePlayer(game2,player2);

                        GamePlayer gamePlayer5 = new GamePlayer(game3,player2);
                        GamePlayer gamePlayer6 = new GamePlayer(game3,player4);

                        GamePlayer gamePlayer7 = new GamePlayer(game4,player2);
                        GamePlayer gamePlayer8 = new GamePlayer(game4,player1);

                        GamePlayer gamePlayer9 = new GamePlayer(game5,player4);
                        GamePlayer gamePlayer10 = new GamePlayer(game5,player1);

                        GamePlayer gamePlayer11 = new GamePlayer(game6,player1);
                        // GamePlayer gamePlayer12 = new GamePlayer(game6,null);

                        GamePlayer gamePlayer13 = new GamePlayer(game7,player4);
                        // GamePlayer gamePlayer14 = new GamePlayer(game7,null);

                        GamePlayer gamePlayer15 = new GamePlayer(game8,player1);
                        GamePlayer gamePlayer16 = new GamePlayer(game8,player4);


                        gamePlayerRepository.save(gamePlayer1);
                        gamePlayerRepository.save(gamePlayer2);
                        gamePlayerRepository.save(gamePlayer3);
                        gamePlayerRepository.save(gamePlayer4);
                        gamePlayerRepository.save(gamePlayer5);
                        gamePlayerRepository.save(gamePlayer6);
                        gamePlayerRepository.save(gamePlayer7);
                        gamePlayerRepository.save(gamePlayer8);
                        gamePlayerRepository.save(gamePlayer9);
                        gamePlayerRepository.save(gamePlayer10);
                        gamePlayerRepository.save(gamePlayer11);
                        //  gamePlayerRepository.save(gamePlayer12);
                        gamePlayerRepository.save(gamePlayer13);
                        //  gamePlayerRepository.save(gamePlayer14);
                        gamePlayerRepository.save(gamePlayer15);
                        gamePlayerRepository.save(gamePlayer16);

                        //repository3.save(new GamePlayer(new Game() ,new Player()));//si pongo new game no le estoy pasando un juego previamente creado no esta creado no tiene id

                        Ship ship1 = new Ship("destroyer",gamePlayer1,Arrays.asList("H2", "H3", "H4"));
                        Ship ship2 = new Ship("submarine",gamePlayer1,Arrays.asList("E1", "F1", "G1"));
                        Ship ship3 = new Ship("patrolboat",gamePlayer1,Arrays.asList("B3","B4","B5"));


                        Ship ship4 = new Ship("destroyer",gamePlayer2,Arrays.asList("B5", "C5","D5"));
                        Ship ship5 = new Ship("patrolboat",gamePlayer2,Arrays.asList("F1", "F2"));


                        Ship ship6 = new Ship("destroyer",gamePlayer3,Arrays.asList("B5", "C5","D5"));
                        Ship ship7 = new Ship("patrolboat",gamePlayer3,Arrays.asList("C6", "C7"));

                        Ship ship8 = new Ship("submarine",gamePlayer4,Arrays.asList("A2","A3","A4"));
                        Ship ship9 = new Ship("patrolboat",gamePlayer4,Arrays.asList("G6", "H6"));

                        Ship ship10 = new Ship("destroyer",gamePlayer5,Arrays.asList("B5","C5","D5"));
                        Ship ship11 = new Ship("patrolboat",gamePlayer5,Arrays.asList("C6", "C7"));

                        Ship ship12 = new Ship("submarine",gamePlayer6,Arrays.asList("A2","A3","A4"));
                        Ship ship13 = new Ship("patrolboat",gamePlayer6,Arrays.asList("G6", "H6"));

                        Ship ship14 = new Ship("destroyer",gamePlayer7,Arrays.asList("B5","C5","D5"));
                        Ship ship15 = new Ship("patrolboat",gamePlayer7,Arrays.asList("C6", "C7"));

                        Ship ship16 = new Ship("submarine",gamePlayer8,Arrays.asList("A2","A3","A4"));
                        Ship ship17 = new Ship("patrolboat",gamePlayer8,Arrays.asList("G6", "H6"));

                        Ship ship18 = new Ship("destroyer",gamePlayer9,Arrays.asList("B5","C5","D5"));
                        Ship ship19 = new Ship("patrolboat",gamePlayer9,Arrays.asList("C6", "C7"));

                        Ship ship20 = new Ship("submarine",gamePlayer10,Arrays.asList("A2","A3","A4"));
                        Ship ship21 = new Ship("patrolboat",gamePlayer10,Arrays.asList("G6", "H6"));

                        Ship ship22 = new Ship("destroyer",gamePlayer11,Arrays.asList("B5","C5","D5"));
                        Ship ship23 = new Ship("patrolboat",gamePlayer11,Arrays.asList("C6", "C7"));

                        Ship ship24 = new Ship("destroyer",gamePlayer15,Arrays.asList("B5","C5","D5"));
                        Ship ship25 = new Ship("patrolboat",gamePlayer15,Arrays.asList("C6", "C7"));

                        Ship ship26 = new Ship("submarine",gamePlayer16,Arrays.asList("A2","A3","A4"));
                        Ship ship27 = new Ship("patrolboat",gamePlayer16,Arrays.asList("G6", "H6"));

                        shipRepository.save(ship1);
                        shipRepository.save(ship2);
                        shipRepository.save(ship3);
                        shipRepository.save(ship4);
                        shipRepository.save(ship5);
                        shipRepository.save(ship6);
                        shipRepository.save(ship7);
                        shipRepository.save(ship8);
                        shipRepository.save(ship9);
                        shipRepository.save(ship10);
                        shipRepository.save(ship11);
                        shipRepository.save(ship12);
                        shipRepository.save(ship13);
                        shipRepository.save(ship14);
                        shipRepository.save(ship15);
                        shipRepository.save(ship16);
                        shipRepository.save(ship17);
                        shipRepository.save(ship18);
                        shipRepository.save(ship19);
                        shipRepository.save(ship20);
                        shipRepository.save(ship21);
                        shipRepository.save(ship22);
                        shipRepository.save(ship23);
                        shipRepository.save(ship24);
                        shipRepository.save(ship25);
                        shipRepository.save(ship26);
                        shipRepository.save(ship27);


                        Salvo salvo1= new Salvo(gamePlayer1,1,Arrays.asList("B5","C5","F1"));
                        Salvo salvo2= new Salvo(gamePlayer2,1,Arrays.asList("B4","B5","B6"));

                        Salvo salvo3= new Salvo(gamePlayer1,2,Arrays.asList("F2","D5"));
                        Salvo salvo4= new Salvo(gamePlayer2,2,Arrays.asList("E1","H3","B3"));

                        Salvo salvo5= new Salvo(gamePlayer3,1,Arrays.asList("A2","A4","G6"));
                        Salvo salvo6= new Salvo(gamePlayer4,1,Arrays.asList("B5","D5","C7"));

                        Salvo salvo7= new Salvo(gamePlayer3,2,Arrays.asList("A3","H6"));
                        Salvo salvo8= new Salvo(gamePlayer4,2,Arrays.asList("C5","C6"));

                        Salvo salvo9= new Salvo(gamePlayer5,1,Arrays.asList("G6","H6","A4"));
                        Salvo salvo10= new Salvo(gamePlayer6,1,Arrays.asList("H1","H2","H3"));

                        Salvo salvo11= new Salvo(gamePlayer5,2,Arrays.asList("A2","A3","D8"));
                        Salvo salvo12= new Salvo(gamePlayer6,2,Arrays.asList("E1","F2","G3"));

                        Salvo salvo13= new Salvo(gamePlayer7,1,Arrays.asList("A3","A4","F7"));
                        Salvo salvo14= new Salvo(gamePlayer8,1,Arrays.asList("B5","B6","H1"));

                        Salvo salvo15= new Salvo(gamePlayer7,2,Arrays.asList("A2","G6","H6"));
                        Salvo salvo16= new Salvo(gamePlayer8,2,Arrays.asList("C5","C7","D5"));

                        Salvo salvo17= new Salvo(gamePlayer9,1,Arrays.asList("A1","A2","A3"));
                        Salvo salvo18= new Salvo(gamePlayer10,1,Arrays.asList("B5","B6","C7"));

                        Salvo salvo19= new Salvo(gamePlayer9,2,Arrays.asList("G6","G7","G8"));
                        Salvo salvo20= new Salvo(gamePlayer10,2,Arrays.asList("C6","D6","E6"));

                        Salvo salvo21= new Salvo(null,3,null);
                        Salvo salvo22= new Salvo(gamePlayer10,3,Arrays.asList("H1","H8"));


                        salvoRepository.save(salvo1);
                        salvoRepository.save(salvo2);
                        salvoRepository.save(salvo3);
                        salvoRepository.save(salvo4);
                        salvoRepository.save(salvo5);
                        salvoRepository.save(salvo6);
                        salvoRepository.save(salvo7);
                        salvoRepository.save(salvo8);
                        salvoRepository.save(salvo9);
                        salvoRepository.save(salvo10);
                        salvoRepository.save(salvo11);
                        salvoRepository.save(salvo12);
                        salvoRepository.save(salvo13);
                        salvoRepository.save(salvo14);
                        salvoRepository.save(salvo15);
                        salvoRepository.save(salvo16);
                        salvoRepository.save(salvo17);
                        salvoRepository.save(salvo18);
                        salvoRepository.save(salvo19);
                        salvoRepository.save(salvo20);
                        salvoRepository.save(salvo21);
                        salvoRepository.save(salvo22);


                        Score score1= new Score(game1, player1,1.0);
                        Score score2= new Score(game1, player2,0.0);

                        Score score3= new Score(game2, player1,0.5);
                        Score score4= new Score(game2, player2,0.5);

                        scoreRepository.save(score1);
                        scoreRepository.save(score2);
                        scoreRepository.save(score3);
                        scoreRepository.save(score4);
                };
        }
}

//AUTENTICACION

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        PlayerRepository playerRepository;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(inputName-> {
                        Player player = playerRepository.findByUserName(inputName);
                        if (player != null) {
                                return new User(player.getUserName(), player.getPassword(),
                                        AuthorityUtils.createAuthorityList("USER"));
                        } else {
                                throw new UsernameNotFoundException("Unknown user: " + inputName);
                        }
                });
        }
}


//AUTORIZACION

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests()
                        .antMatchers("/api/game_view/**").hasAuthority("USER")
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/api/games").permitAll()
                        .antMatchers("/web/**").permitAll();

                http.formLogin()
                        .usernameParameter("name")
                        .passwordParameter("pwd")
                        .loginPage("/api/login");
                http.logout().logoutUrl("/api/logout");

                http.headers().frameOptions().disable();

                // turn off checking for CSRF tokens
                http.csrf().disable();

                // if user is not authenticated, just send an authentication failure response
                http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

                // if login is successful, just clear the flags asking for authentication
                http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

                // if login fails, just send an authentication failure response
                http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

                // if logout is successful, just send a success response
                http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        }

        private void clearAuthenticationAttributes(HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                }
        }
}