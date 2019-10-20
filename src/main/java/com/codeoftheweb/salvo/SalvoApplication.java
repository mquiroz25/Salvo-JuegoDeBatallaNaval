package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);


	}


	@Bean
	public CommandLineRunner initData(PlayerRepository repository,GameRepository repository2,GamePlayerRepository repository3 ) {
		return (args) -> {
			// save a couple of Players
			Player player1=new Player("Bauer");
			Player player2=new Player("O'Brian");
			Player player3=new Player( "juan");
			Player player4=new Player( "Dessler");

			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);

			Game game1=new Game((long) 0);
			Game game2=new Game((long) 1);
			Game game3=new Game((long) 2);

			repository2.save(game1);
			repository2.save(game2);
			repository2.save(game3);

			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2);
			GamePlayer gamePlayer3 = new GamePlayer(game1,player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player1);


			repository3.save(gamePlayer1);
			repository3.save(gamePlayer2);
			repository3.save(gamePlayer3);
			repository3.save(gamePlayer4);


			//repository3.save(new GamePlayer(new Game() ,new Player()));//si pongo new game no le estoy pasando un juego previamente creado no esta creado no tiene id


		};
	}


}
