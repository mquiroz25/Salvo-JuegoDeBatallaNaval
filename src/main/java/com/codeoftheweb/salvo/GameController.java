package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    private Map<String,Object> makeMap(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return  map;
    }

    @Autowired//como que llama a gameRepository para usar sus metodos
    private GamePlayerRepository gamePlayerRepository;

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player getPlayer(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    @Autowired //para instanciar el objeto
    private PlayerRepository playerRepository;


    @Autowired //para instanciar el objeto
    private GameRepository gameRepository;

    @RequestMapping("/games") ///esto tambien se llama endpoint
    public Map<String, Object> getAll(Authentication authentication ) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if(isGuest(authentication)==true){
            dto.put("player","Guest");
        }
        else
        {
            dto.put("player",getPlayer(authentication).makePlayerDTO() );
        }
        dto.put("games",gameRepository
                .findAll()
                .stream()
                .map(game ->game.makeGameDTO())
                .collect(Collectors.toList()));
        return  dto;
    }



    @RequestMapping(value = "/games" ,method = RequestMethod.POST) ///esto tambien se llama endpoint
    ResponseEntity<Map <String, Object >> createGames(Authentication authentication ) {

        if(isGuest(authentication)==false){

            Game game =new Game();
            gameRepository.save(game);
            GamePlayer gamePlayer = new GamePlayer(game,getPlayer(authentication));
            gamePlayerRepository.save(gamePlayer);

            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("gpid",gamePlayer.getId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

    }

}
