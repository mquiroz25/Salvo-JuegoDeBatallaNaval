package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime creationDate;
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private  Set<GamePlayer> gamePlayerSet;



public Game(Long horas){
    this.creationDate=LocalDateTime.now().plusHours(horas);
}

public Game(){}



//GETTER AND SETTERS
public Long getId() {
    return id;
}

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }

    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayerSet = gamePlayerSet;
    }


    ///////////
  public   Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers",this.getGamePlayerSet()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
	            .collect(Collectors.toList()));

                 return dto;
    }

}
