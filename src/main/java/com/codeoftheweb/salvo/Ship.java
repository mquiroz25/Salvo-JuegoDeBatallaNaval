package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO ,generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String type;

    public List<String> getShipLocations() {
        return ShipLocations;
    }

    public void setShipLocations(List<String> shipLocations) {
        ShipLocations = shipLocations;
    }

    @ElementCollection
    @Column(name="ShipLocations ")
    private List<String> ShipLocations  = new ArrayList<>();


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship(){};
    public Ship(String type, GamePlayer gamePlayer,List<String> shipLocations){

        this.type=type;
        this.gamePlayer=gamePlayer;
        this.ShipLocations=shipLocations;
    };

    public Map<String, Object> makeShipDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", this.type);
        dto.put("locations", this.getShipLocations());
        return dto;
    }

}
