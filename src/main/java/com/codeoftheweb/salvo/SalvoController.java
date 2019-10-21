package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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




   /* @RequestMapping("/api/games")
    public List<Long> getAll() {
        return gameRepository.findAll().stream().map(b -> b.getId())
                .collect(Collectors.toList());
    }
*/



   /* @RequestMapping("/api/games")
    public List<Object> getAll() {
        return gameRepository
                .findAll()
                .stream()
                .map(b -> b.makeGameDTO())
                .collect(Collectors.toList());
    }*/

    @RequestMapping("/games") ///esto tambien se llama endpoint
    public List<Object> getAll() {

        return gameRepository
                .findAll()
                .stream()
                .map(game ->game.makeGameDTO())
                .collect(Collectors.toList());
    }

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


    @RequestMapping("/game_view/{nn}") ///esto tambien se llama endpoint
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
                .flatMap(a -> a.getSalvoes()
                        .stream().map(salvo -> salvo.makeSalvoDTO()))
                .collect(Collectors.toList()));

        return dto;






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





    }


  /*  @RequestMapping("/salvoes") ///esto tambien se llama endpoint
    public List<Object> getAlql() {

        return salvoRepository
                .findAll()
                .stream()
                .map(salvo ->salvo.makeSalvoDTO())
                .collect(Collectors.toList());
    }
*/

    //con esto solo me tira un loop infinito porque es un objeto game que me devuelve ,deberia transformar en un map
}

//  @RequestMapping("/games") ///esto tambien se llama endpoint

