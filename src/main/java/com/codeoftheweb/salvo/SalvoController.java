package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
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

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player getPlayer(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    @Autowired
    PasswordEncoder passwordEncoder;

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

    @RequestMapping("/game_view/{nn}") ///esto tambien se llama endpoint

    public ResponseEntity <Map <String, Object >>s(@PathVariable Long nn,Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);

        Game game = gamePlayerRepository.findById(nn).orElse(null).getGame();

        Long IdPlayerAutenticado = getPlayer(authentication).getId();

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

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email,
            @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) !=  null) {
            return new ResponseEntity<>(makeMap("error","Name already in use"), HttpStatus.UNAUTHORIZED);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "/game/{nn}/players" ,method = RequestMethod.POST) ///nn es el id game
    ResponseEntity <Map <String, Object >>joinGame(@PathVariable Long nn, Authentication authentication ) {

        if(isGuest(authentication)==false){// si no es invitado , osea si esta autenticado entonces

            Game game =gameRepository.findById(nn).orElse(null);

            if(game==null)
            {
                return new ResponseEntity<>(makeMap("prohibido","No existe ese juego"),HttpStatus.FORBIDDEN);
            }

            if(game.getGamePlayers().size()==1){

                GamePlayer gamePlayer= new GamePlayer(game,getPlayer(authentication));
                gamePlayerRepository.save(gamePlayer);

                Map<String, Object> dto = new LinkedHashMap<>();
                dto.put("gpid",gamePlayer.getId());
                return new ResponseEntity<>(dto, HttpStatus.CREATED);
            }

            else{
                return new ResponseEntity<>(makeMap("prohibido","El juego esta lleno"),HttpStatus.FORBIDDEN);
            }
        }
            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
    }



    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    public ResponseEntity<String> addShips(@PathVariable Long gamePlayerId, @RequestBody List<Ship>ships,Authentication authentication) { //request body para convertir a objeto el json enviado por post

        if(isGuest(authentication)==false){

            GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

            ships.forEach((ship) -> { shipRepository.save(ship); });

            //gamePlayerRepository.save(gamePlayer);
        }
        return new ResponseEntity<>("No autorizado", HttpStatus.UNAUTHORIZED);
    }

  /*  @RequestMapping("/books")
    public Map<String, Object> getAll(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName()).makePlayerDTO();
    }*/


}
   /* @RequestMapping("/games2")
    public List<Long> getAlssl() {
        return gameRepository.findAll().stream().map(b -> b.getId())
                .collect(Collectors.toList());
    }*/

   /*@RequestMapping("/game_view/{nn}") ///esto tambien se llama endpoint
    public Map<String, Object>s(@PathVariable Long nn)  { //nn corresponde al id del gamePlayer
        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer= gamePlayerRepository.findById (nn).orElse (null);

        Game game= gamePlayerRepository.findById (nn).orElse (null).getGame();

        dto.put("id",game.getId());
        dto.put("created",game.getCreationDate());
        dto.put("gamePlayers",game.getGamePlayerSet()
                .stream()
                .map(a -> a.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships",gamePlayer.getShips()
                .stream()
                .map(a -> a.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes",game.getGamePlayerSet()
                .stream()
                .map(a -> a.getSalvoes())
                        .flatMap(salvo -> salvo.stream()).map(s -> s.makeSalvoDTO()));

        return dto;
*/








  /*@RequestMapping("/salvoes") ///esto tambien se llama endpoint
    public List<Object> getAlql() {

        return salvoRepository
                .findAll()
                .stream()
                .map(salvo ->salvo.makeSalvoDTO())
                .collect(Collectors.toList());
    }*/


//con esto solo me tira un loop infinito porque es un objeto game que me devuelve ,deberia transformar en un map






   /* @RequestMapping("/api/games")
    public List<Object> getAll() {
        return gameRepository
                .findAll()
                .stream()
                .map(b -> b.makeGameDTO())
                .collect(Collectors.toList());
    }*/


     /*  @RequestMapping("/game_view/{nn}") ///esto tambien se llama endpoint
    public Map<String, Object>s(@PathVariable Long nn)  { //nn corresponde al id del gamePlayer
        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer= gamePlayerRepository.findById (nn).orElse (null);

        Game game= gamePlayerRepository.findById (nn).orElse (null).getGame();


        dto.put("id",game.getId());
        dto.put("created",game.getCreationDate());
        dto.put("gamePlayers",game.getGamePlayerSet()
                .stream()
                .map(a -> a.makeGamePlayerDTO())
                .collect(Collectors.toList()));

        dto.put("ships",gamePlayer.getShips()
                .stream()
                .map(a -> a.makeShipDTO())
                .collect(Collectors.toList()));

        return dto;

    }
*/


//  @RequestMapping("/games") ///esto tambien se llama endpoint

