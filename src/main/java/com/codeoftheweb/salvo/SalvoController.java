package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired//como que llama a gameRepository para usar sus metodos
    private GamePlayerRepository gamePlayerRepository;

    @Autowired //para instanciar el objeto
    private GameRepository gameRepository;

    @Autowired //para instanciar el objeto
    private SalvoRepository salvoRepository;

    @Autowired //para instanciar el objeto
    private PlayerRepository playerRepository;

    @Autowired //para instanciar el objeto
    private ShipRepository shipRepository;


    //metodos

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player getPlayerForLogin(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    private Map<String,Object> makeMap(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return  map;
    }

    private Map<String,Object> createMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("self",new ArrayList<Object>());
        map.put("opponent",new ArrayList<Object>());
        return  map;
    }

    //entrar a juego
    @RequestMapping("/game_view/{nn}") ///nn es gamePlayer id

    public ResponseEntity <Map <String, Object >>enterGame(@PathVariable Long nn,Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);

        if (gamePlayer==null
        ){

            return new ResponseEntity<>(makeMap("error","no se puede acceder"), HttpStatus.UNAUTHORIZED);

        }

        Game game = gamePlayerRepository.findById(nn).orElse(null).getGame();

        Long IdPlayerAutenticado = getPlayerForLogin(authentication).getId();

        Long IdPlayerDeGamePlayerIngresado = gamePlayer.getPlayer().getId();

        if (IdPlayerAutenticado == IdPlayerDeGamePlayerIngresado){

            dto.put("id", game.getId());
            dto.put("created", game.getCreationDate());
            dto.put("gameState","PLACESHIPS");
            dto.put("gamePlayers", game.getGamePlayers()
                    .stream()
                    .map(a -> a.makeGamePlayerDTO())
                    .collect(Collectors.toList()));
            dto.put("ships", gamePlayer.getShips()
                    .stream()
                    .map(a -> a.makeShipDTO())
                    .collect(Collectors.toList()));
            dto.put("salvoes", game.getGamePlayers()
                    .stream()
                    .flatMap(a -> a.getSalvoes()
                            .stream().map(salvo -> salvo.makeSalvoDTO()))
                    .collect(Collectors.toList()));
            dto.put("hits",createMap());

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(makeMap("error","no se puede acceder"), HttpStatus.UNAUTHORIZED);

    }


    @RequestMapping(value = "/game/{nn}/players" ,method = RequestMethod.POST) ///nn es el id game
    ResponseEntity <Map <String, Object >>joinGame(@PathVariable Long nn, Authentication authentication ) {

        if(isGuest(authentication)==true) {//si es invitado

            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Game game =gameRepository.findById(nn).orElse(null);

        if(game==null)
        {
            return new ResponseEntity<>(makeMap("prohibido","No existe ese juego"),HttpStatus.FORBIDDEN);
        }

        if(game.getGamePlayers().size()==1){

            GamePlayer gamePlayer= new GamePlayer(game,getPlayerForLogin(authentication));
            gamePlayerRepository.save(gamePlayer);

            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("gpid",gamePlayer.getId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }

        else{
            return new ResponseEntity<>(makeMap("prohibido","El juego esta lleno"),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    public ResponseEntity<Object> addShips(@PathVariable Long gamePlayerId, @RequestBody List<Ship>ships, Authentication authentication) { //request body para convertir a objeto el json enviado por post


        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if(gamePlayer==null)
        {
            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);

        }

        Long idPlayerLogin = getPlayerForLogin(authentication).getId();
        Long idPlayerGamePlayer = gamePlayer.getPlayer().getId();

        if (isGuest(authentication)==true || idPlayerLogin !=idPlayerGamePlayer) {

            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getShips().size()!=0)
        {
            return new ResponseEntity<>(makeMap("error","ya tiene barcos "),HttpStatus.FORBIDDEN);
        }

        ships.forEach((ship) -> {
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship); });

        return new ResponseEntity<>(makeMap("OK","los barcos se colocaron correctamente"), HttpStatus.CREATED);
    }



    @RequestMapping(value="/games/players/{gamePlayerId}/salvoes", method=RequestMethod.POST)
    public ResponseEntity<Object> addSalvoes(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) { //request body para convertir a objeto el json enviado por post

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if(gamePlayer==null)
        {
            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);

        }

        Long idPlayerLogin = getPlayerForLogin(authentication).getId();
        Long idPlayerGamePlayer = gamePlayer.getPlayer().getId();

        if (isGuest(authentication)==true || idPlayerLogin !=idPlayerGamePlayer) {

            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        //filtrar salvos del gamePlayer por el turno  del salvo enviado por post
        List<Salvo> salvosFiltradosPorTurno = gamePlayer.getSalvoes().stream(). filter(salvo1 -> salvo1.getTurn()==salvo.getTurn()).collect(Collectors.toList());

        if(salvosFiltradosPorTurno.size()!=0) //si ya tiene salvo para ese turno
        {
            return new ResponseEntity<>(makeMap("error","ya tiene salvo agregados para ese turno "),HttpStatus.FORBIDDEN);
        }

        salvo.setGamePlayer(gamePlayer);
        salvoRepository.save(salvo);

        return new ResponseEntity<>(makeMap("OK","el salvo se coloco correctamente"), HttpStatus.CREATED);
    }
}