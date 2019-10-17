package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

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

       @RequestMapping("/api/games")
    public List<Object> getAll() {
        return gameRepository
                .findAll()
                .stream()
                .map(b -> b.makeGameDTO())
                .collect(Collectors.toList());
    }
}
