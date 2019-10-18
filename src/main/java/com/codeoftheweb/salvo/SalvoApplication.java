package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);


	}


	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,ShipRepository shipRepository ) {
		return (args) -> {
			// save a couple of Players
			Player player1=new Player("Bauer");
			Player player2=new Player("O'Brian");
			Player player3=new Player( "juan");
			Player player4=new Player( "Dessler");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Game game1=new Game((long) 0);
			Game game2=new Game((long) 1);
			Game game3=new Game((long) 2);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);

			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2);
			GamePlayer gamePlayer3 = new GamePlayer(game1,player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player1);


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);

			//repository3.save(new GamePlayer(new Game() ,new Player()));//si pongo new game no le estoy pasando un juego previamente creado no esta creado no tiene id

			ArrayList<String> ubicacion1 = new ArrayList<String>();
			ubicacion1.add("H3");
			ubicacion1.add("H4");
			ubicacion1.add("H5");

			ArrayList<String> ubicacion2 = new ArrayList<String>();
			ubicacion2.add("F3");
			ubicacion2.add("F4");
			ubicacion2.add("F5");

			Ship ship1 = new Ship("crucero",gamePlayer1,ubicacion1);

			shipRepository.save(ship1);

			Ship ship2 = new Ship("yate",gamePlayer1,ubicacion2);
			shipRepository.save(ship2);

		};
	}


}
