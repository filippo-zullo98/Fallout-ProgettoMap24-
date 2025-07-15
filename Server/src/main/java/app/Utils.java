/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.impl.FalloutGame;
import app.type.AdvObject;
import app.type.Elevator;
import app.type.Furniture;
import app.type.Room;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author pierpaolo
 */
public class Utils {

    public static String parserString(String string){
        return string.toLowerCase().replace(" ", "");
    }

    public static List<String> parseString(String string) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.toLowerCase().split("\\s+");
        for (String t : split) {
            tokens.add(t);
        }
        return tokens;
    }


    public static void loadRoomsFromFiles(String file, FalloutGame game) throws IOException, ParseException {
        FileReader reader = new FileReader(file);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(reader);
        Map<Integer,JSONObject> directionJson = new HashMap<>();
        for (Object objectRoom : jsonObject.keySet()) {//ciclo sulle stanze
            Room room = null;
            String roomName = (String) objectRoom; //contiene il nome della stanza
            //dall'interno di tutto il json va a prendere il valore (che contiene i dettagli) che ha per chiave il roomName
            JSONObject stanzaDettagli = (JSONObject)((JSONArray) jsonObject.get(roomName)).get(0);
            int type = Integer.parseInt(stanzaDettagli.get("type").toString());
            int piano = Integer.parseInt(stanzaDettagli.get("piano").toString());
            if (type==0) {
                room = new Room();
            } else if (type==1) {
                room = new Elevator();
            }

            assert room!=null;
            room.setName(roomName);
            room.setDescription(stanzaDettagli.get("descrizione").toString());
            room.setId(Integer.parseInt(stanzaDettagli.get("id").toString()));
            Object isLock = stanzaDettagli.get("isLock");//is lock può essere un boolean o un json che contiene i dettagli della stanza bloccata
            room.setPiano(piano);
            if (!(isLock instanceof Boolean)){
                // La stanza è bloccata
                JSONObject direction = (JSONObject) ((JSONArray) isLock).get(0); //prende il valore della chiave isLock
                room.setLock((String) direction.get("descriptionLock"), Integer.parseInt(direction.get("objectLock").toString()));
            }

            JSONArray directionsArray = (JSONArray) (stanzaDettagli.get("directions"));
            directionJson.put(room.getId(), (JSONObject) directionsArray.get(0));

            game.getRooms().add(room);
        }


        for (Map.Entry<Integer, JSONObject> entry : directionJson.entrySet()){
            Integer key = entry.getKey(); //idstanza
            JSONObject value = entry.getValue(); //JSON con direzione
            Room room = game.getRoomById(key);
            Elevator elevator = null;
            for (Object s: value.keySet()){
                String directions = s.toString();
                long id = (Long) value.get(directions);
                switch (directions){
                    case "left":
                        room.setLeft(game.getRoomById((int) id));
                        break;
                    case "right":
                        room.setRight(game.getRoomById((int) id));
                        break;
                    case "down":
                        room.setDown(game.getRoomById((int) id));
                        break;
                    case "up":
                        room.setUp(game.getRoomById((int) id));
                        break;
                    case "sali":
                        if(room instanceof Elevator){
                            elevator = (Elevator) room;
                            elevator.setSali(game.getRoomById((int) id));
                        }
                        break;
                    case "scendi":
                        if(room instanceof Elevator){
                            elevator = (Elevator) room;
                            elevator.setScendi(game.getRoomById((int) id));
                        }
                        break;
                }
            }
        }
    }
    public static void loadObjectFromFiles(String file, FalloutGame game) throws IOException, ParseException {
        FileReader reader = new FileReader(file);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(reader);
        for (Object object : jsonObject.keySet()) {//ciclo sulle stanze
            String objectName = (String) object; //contiene il nome della stanza
            JSONObject furnitureDettagli = (JSONObject)((JSONArray) jsonObject.get(objectName)).get(0);
            int idObject= Integer.parseInt(furnitureDettagli.get("id").toString());

            int idFurniture= Integer.parseInt(furnitureDettagli.get("furniture").toString());

            String descObject= furnitureDettagli.get("descrizione").toString();
            AdvObject advObject = new AdvObject(idObject,objectName,descObject);

            game.getFurnitureById(idFurniture).addObject(advObject);
            game.getObjects().add(advObject);
        }
    }

    public static void loadFurnitureFromFiles(String file, FalloutGame game) throws IOException, ParseException {
        FileReader reader = new FileReader(file);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(reader);
        for (Object furniture : jsonObject.keySet()) {//ciclo sulle stanze
            String furnitureName = (String) furniture; //contiene il nome della stanza
            JSONObject furnitureDettagli = (JSONObject)((JSONArray) jsonObject.get(furnitureName)).get(0);
            int idFurniture= Integer.parseInt(furnitureDettagli.get("id").toString());
            int idRoom= Integer.parseInt(furnitureDettagli.get("stanza").toString());
            String descFurniture= furnitureDettagli.get("descrizione").toString();
            Room room = game.getRoomById(idRoom);
            room.setFurniture(new Furniture(idFurniture,furnitureName,descFurniture));
        }
    }

}
