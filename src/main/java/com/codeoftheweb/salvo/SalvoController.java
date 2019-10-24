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


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player getPlayer(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    @Autowired
    PasswordEncoder passwordEncoder;



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

    @RequestMapping("/game_view/{nn}") ///esto tambien se llama endpoint
    public Map<String, Object>s(@PathVariable Long nn) { //nn corresponde al id del gamePlayer
        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);
        Game game = gamePlayerRepository.findById(nn).orElse(null).getGame();

        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayerSet()
                .stream()
                .map(a -> a.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(a -> a.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", game.getGamePlayerSet()
                .stream()
                .flatMap(a -> a.getSalvoes()
                        .stream().map(salvo -> salvo.makeSalvoDTO()))
                .collect(Collectors.toList()));

        return dto;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email,
            @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) !=  null) {
            return new ResponseEntity<>(makeMap("error","Name already in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


private Map<String,Object> makeMap(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return  map;
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

