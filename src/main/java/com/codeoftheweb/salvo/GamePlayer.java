package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;


    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private  Set<Salvo> salvoes;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private  Set<Ship> ships;


    public GamePlayer(Game game,Player player){
        this.game=game;
        this.player=player;
        this.joinDate=LocalDateTime.now();

    }
    public GamePlayer(){}

    //GETTER AND SETTER


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }



    Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
       // dto.put("player", this.getPlayer() != null ? this.getPlayer().makePlayerDTO() : null );

      /*  dto.put("salvoes",this.getSalvoes()
                .stream()
                .map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));*/
        return dto;
}




}
