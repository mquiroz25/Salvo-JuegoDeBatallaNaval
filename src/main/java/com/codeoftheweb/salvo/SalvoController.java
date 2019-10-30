package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private Map<String,Object> mapDamages(Integer carrierHits,Integer battleshipHits,Integer submarineHits,Integer destroyerHits,Integer patrolboatHits,Integer acumuladorPatrolboat,Integer acumuladorCarrier,Integer acumuladorBattleShip,Integer acumuladorSubmarine,Integer acumuladorDestroyer){
        Map<String,Object> map = new HashMap<>();
        map.put("carrierHits",carrierHits);
        map.put("battleshipHits",battleshipHits);
        map.put("submarineHits",submarineHits);
        map.put("destroyerHits",destroyerHits);
        map.put("patrolboatHits",patrolboatHits);
       map.put("carrier",acumuladorCarrier);
        map.put("battleship",acumuladorBattleShip);
        map.put("submarine",acumuladorSubmarine);
        map.put("destroyer",acumuladorDestroyer);
        map.put("patrolboat",acumuladorPatrolboat);

        return  map;
    }




    public  GamePlayer obtenerGamePlayerEnemigo(GamePlayer gamePlayer) {

        Game game = gamePlayer.getGame();

        Set<GamePlayer>listaGamePlayers= game.getGamePlayers();

        GamePlayer gamePlayerEnemigo= listaGamePlayers.stream().filter(gamePlayer1 -> gamePlayer1!=gamePlayer).findAny().orElse(null);

        return gamePlayerEnemigo;
    }


    public   List<String> listaDePosicionesDeTodosLosBarcos(GamePlayer gamePlayer) {

        List <String> ubicacionesDeLosBarcos = gamePlayer.getShips() .stream()
                .flatMap(a -> a.getLocations()
                        .stream())
                .collect(Collectors.toList());

        return ubicacionesDeLosBarcos;
    }


    public   List<String> listaDedisparosParaUnSalvoDelEnemigo (GamePlayer gamePlayer) {

        List <String> listaDeDisparosDeUnSalvo = gamePlayer.getSalvoes().stream()
                .flatMap(a -> a.getSalvoLocations()
                        .stream())
                .collect(Collectors.toList());

        return listaDeDisparosDeUnSalvo;
    }


 /*   public List<Map<String,Object>> turnos (GamePlayer gamePlayer) {

        return  gamePlayer.getSalvoes().stream().map(salvo->salvo.newSalvoDTO()).collect(Collectors.toList());
    }*/

   public List <String> barcos(GamePlayer gamePlayer) {

       List<String> ubicacionesCarrier= new ArrayList<String>();

      /* Ship carrier =  gamePlayer.getShips().stream()
               .filter(ship -> ship.getType()=="carrier").findAny().orElse(null);

        ubicacionesCarrier = carrier.getLocations();*/

        return ubicacionesCarrier;
    }



    public List <Map<String, Object>> obtenerhits(GamePlayer gamePlayer) {

        //gamePlayerEnemigo
        Game game = gamePlayer.getGame();

        Set<GamePlayer>listaGamePlayers= game.getGamePlayers();

        GamePlayer gamePlayerEnemigo= listaGamePlayers.stream().filter(gamePlayer1 -> gamePlayer1!=gamePlayer).findAny().orElse(null);
       // -----------------------------------------------------------//


        Ship barcoCarrier =  gamePlayer.getShips().stream()
                .filter(ship -> ship.getType()=="carrier").findAny().orElse(null);

        Ship barcoBattleship =  gamePlayer.getShips().stream()
                .filter(ship -> ship.getType()=="battleship").findAny().orElse(null);

        Ship barcoSubmarine =  gamePlayer.getShips().stream()
                .filter(ship -> ship.getType()=="submarine").findAny().orElse(null);

        Ship barcoDestroyer =  gamePlayer.getShips().stream()
                .filter(ship -> ship.getType()=="destroyer").findAny().orElse(null);

        Ship barcoPatrolboat =  gamePlayer.getShips().stream()
                .filter(ship -> ship.getType()=="patrolboat").findAny().orElse(null);

        //lista de ubicaciones de cada  barco del gamePlayer

        List<String> ubicacionesCarrier = barcoCarrier.getLocations();
        List<String> ubicacionesBattleShip = barcoBattleship.getLocations();
        List<String> ubicacionesSubmarine= barcoSubmarine.getLocations();
        List<String>  ubicacionesDestroyer= barcoDestroyer.getLocations();
        List<String> ubicacionesPatrolboat= barcoPatrolboat.getLocations();

        // -----------------------------------------------------------//

        List<Map<String, Object>> listMap = new ArrayList<>();

        Set <Salvo> listaDeSalvosDelEnemigo = gamePlayerEnemigo.getSalvoes();

        //acumuladores
        Integer acumuladorCarrier= 0;
         Integer acumuladorBattleShip  = 0;
         Integer acumuladorSubmarine = 0;
        Integer acumuladorDestroyer = 0;
        Integer acumuladorPatrolboat = 0;


        //por cada salvo (conjunto de ubicaciones) veo a que barco le pego
        for(Salvo salvoEnemigo : listaDeSalvosDelEnemigo) {

          //por cada ciclo del foreach se inicializan de nuevo
            List<String> hits = new ArrayList<>();
            Map<String, Object> map = new LinkedHashMap<>();
        //contadores
            Integer carrierHits=0;
            Integer battleshipHits=0;
            Integer submarineHits=0;
            Integer destroyerHits=0;
            Integer patrolboatHits=0;

            List<String>localizacionesSalvo = salvoEnemigo.getSalvoLocations();

            for(String salvoLocation : localizacionesSalvo) {

                if(ubicacionesCarrier.contains(salvoLocation))
                {
                    hits.add(salvoLocation);
                    carrierHits++;

                }

                if(ubicacionesBattleShip.contains(salvoLocation))
                {
                    hits.add(salvoLocation);
                    battleshipHits++;
                }

                if(ubicacionesSubmarine.contains(salvoLocation))
                {
                    hits.add(salvoLocation);
                    submarineHits++;
                }

                if(ubicacionesDestroyer.contains(salvoLocation))
                {
                    hits.add(salvoLocation);
                    destroyerHits++;
                }

                if(ubicacionesPatrolboat.contains(salvoLocation))
                {
                    hits.add(salvoLocation);
                    patrolboatHits++;
                }
            }

            acumuladorPatrolboat = acumuladorPatrolboat +patrolboatHits;
            acumuladorCarrier = acumuladorCarrier +carrierHits;
            acumuladorBattleShip=acumuladorBattleShip + battleshipHits;
            acumuladorSubmarine=acumuladorSubmarine+ submarineHits;
            acumuladorDestroyer=acumuladorDestroyer+ destroyerHits;
      /*      acumuladorCarrier = acumuladorCarrier + carrierHits;
            acumuladorBattleShip = acumuladorBattleShip + battleshipHits;
            acumuladorSubmarine = acumuladorSubmarine + submarineHits;
            acumuladorDestroyer = acumuladorDestroyer + destroyerHits;*/

            map.put("turn", salvoEnemigo.getTurn());
            map.put("hitLocations",hits);
            map.put("damages",mapDamages(carrierHits,battleshipHits,submarineHits,destroyerHits,patrolboatHits,acumuladorPatrolboat,acumuladorCarrier,acumuladorBattleShip,acumuladorSubmarine,acumuladorDestroyer));
       //     map.put("missed",2);
            listMap.add(map);
        }

        return listMap;
    }


    //entrar a juego
    @RequestMapping("/game_view/{nn}") ///nn es gamePlayer id

    public ResponseEntity <Map <String, Object >>enterGame(@PathVariable Long nn,Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);

        if (gamePlayer==null){
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

          //  dto.put("self",obtenerhits(gamePlayer));

            //   System.out.println(listaDePosicionesDeTodosLosBarcos(gamePlayer));

            // System.out.println(obtenerGamePlayerEnemigo(gamePlayer));
            //System.out.println(turnos(gamePlayer));
            // System.out.println(barcos(gamePlayer));
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


//miguel
  /*  @RequestMapping(value="/games/players/{gamePlayerId}/salvoes", method=RequestMethod.POST)
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
    }*/


    @RequestMapping(value="/games/players/{gamePlayerId}/salvoes", method=RequestMethod.POST)
    public ResponseEntity<Object> addSalvoes(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) { //request body para convertir a objeto el json enviado por post

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        Player player =getPlayerForLogin(authentication);

        if(player==null)
        {
            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer==null)
        {
            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Long idPlayerLogin = getPlayerForLogin(authentication).getId();
        Long idPlayerGamePlayer = gamePlayer.getPlayer().getId();

        if (isGuest(authentication)==true || idPlayerLogin !=idPlayerGamePlayer) {

            return new ResponseEntity<>(makeMap("error","no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer opponent = obtenerGamePlayerEnemigo(gamePlayer);
//bien
        if(opponent==null) {

            return new ResponseEntity<>(makeMap("error", "no hay oponente ,no se puede registrar salvo"), HttpStatus.FORBIDDEN);
        }


        if(opponent!=null) {

            //solo entra a este si el gamePlayer no tiene salvos,sino sigue al siguiente if
            if (gamePlayer.getSalvoes().isEmpty()){
                salvo.setTurn(1);
                salvo.setGamePlayer(gamePlayer);
                salvoRepository.save(salvo);
                return new ResponseEntity<>(makeMap("OK","salvo creado"), HttpStatus.CREATED);
            }


            if (gamePlayer.getSalvoes().size() == opponent.getSalvoes().size()) {

                salvo.setTurn(gamePlayer.getSalvoes().size() + 1);
                salvo.setGamePlayer(gamePlayer);
            } else {

                return new ResponseEntity<>(makeMap("error", "ya tiene salvo en este turno"), HttpStatus.FORBIDDEN);

            }
        }

        salvo.setGamePlayer(gamePlayer);
        salvoRepository.save(salvo);

        return new ResponseEntity<>(makeMap("OK", "salvo creado"), HttpStatus.CREATED);
    }

}
