package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class BotService {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;
    private Boolean afterburnerOn = false;
    public static int flag = 1;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
    }

    public GameObject getBot() {
        return this.bot;
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    public Boolean isAfterburnerOn(){
        return this.afterburnerOn;
    }
    
    public void computeNextPlayerAction(PlayerAction playerAction){
        if (!gameState.getGameObjects().isEmpty()){ // Permainan bermulai
            
            if (gameState.world.getCurrentTick() == flag){ // Kondisional if akan menjalankan satu command setiap tick
                
                System.out.println("Tick: " + gameState.world.getCurrentTick() + " " + flag);
                System.out.print("Action: ");
                
                // Strategi Menjauhi Batas Arena jika bot terlalu ke pinggir
                if ((getDistanceWorldCenter(bot) + (2*bot.size)) > gameState.world.getRadius()){
                    playerAction.setHeading(getHeadingWorldCenter());
                    playerAction.setAction(PlayerActions.FORWARD);
                    System.out.println("Near the edge, going to center");
                    
                } else{
                    var food = nearestFood(gameState);
                    var superfood = nearestSuperFood(gameState);
                    var gascloud = nearestGascloud(gameState);
                    var asteroid = nearestAsteroid(gameState);
                    var enemy = nearestEnemy(gameState);
                    var wormhole = nearestWormhole(gameState);
                    
        // Strategi Menjauhi Gas Cloud
                    if (gascloud != null && (getDistanceBetween(bot, gascloud) - (bot.size) - (gascloud.size)) < (gascloud.size*1.5)){
                        playerAction.setHeading((getHeadingBetween(gascloud) + 180) % 360);
                        playerAction.setAction(PlayerActions.FORWARD);
                        System.out.println("Avoiding gas cloud");
                        
        // Strategi Menjauhi Asteroid
                    } else{
                        if (asteroid != null && (getDistanceBetween(bot, asteroid) - (bot.size) - (asteroid.size)) < (asteroid.size*1.5)){
                            playerAction.setHeading((getHeadingBetween(asteroid) + 180) % 360);
                            playerAction.setAction(PlayerActions.FORWARD);
                            System.out.println("Avoiding Asteroid");

                        } else{
        // Strategi Mendekati Musuh yang Berukuran Lebih Kecil
                            if ((bot.size > enemy.size)){
                                if (((getDistanceBetween(bot, enemy) - (bot.size) - (enemy.size)) <= 0.3*gameState.world.getRadius())){
                                    if (bot.size > 70){

        // Strategi Menggunakan Afterburner untuk mengejar musuh
                                        if (!isAfterburnerOn()){
                                            playerAction.setHeading(getHeadingBetween(enemy));
                                            playerAction.setAction(PlayerActions.STARTAFTERBURNER);
                                            this.afterburnerOn = true;
                                            System.out.println("Afterburner is on due to chasing faster enemy");
                                            
                                        } else{
                                            playerAction.setHeading(getHeadingBetween(enemy));
                                            playerAction.setAction(PlayerActions.FORWARD);
                                            System.out.println("Chasing smaller enemy");
                                        }

                                    } else{
                                        if (!isAfterburnerOn()){
                                            playerAction.setHeading(getHeadingBetween(enemy));
                                            playerAction.setAction(PlayerActions.FORWARD);
                                            System.out.println("Chasing smaller enemy");

                    // Afterburner Mati Ketika Ukuran Bot Cukup Kecil          
                                        } else{
                                            playerAction.setHeading(getHeadingBetween(enemy));
                                            playerAction.setAction(PlayerActions.STOPAFTERBURNER);
                                            this.afterburnerOn = false;
                                            System.out.println("Afterburner is off due to small ship size");
                                        }
                                    }

                    // Afterburner Mati Ketika Musuh terlalu jauh       
                                } else{
                                    if (isAfterburnerOn()){
                                        playerAction.setAction(PlayerActions.STOPAFTERBURNER);
                                        this.afterburnerOn = false;
                                        System.out.println("Enemy's too far, afterburner is off");
                                        
                                    } else{
                                        playerAction.setHeading(getHeadingBetween(enemy));
                                        playerAction.setAction(PlayerActions.FORWARD);
                                        System.out.println("Getting closer to smaller enemy");
                                    }
                                }

                    // Afterburner Mati        
                            } else{
                                if (isAfterburnerOn()){
                                    playerAction.setAction(PlayerActions.STOPAFTERBURNER);
                                    this.afterburnerOn = false;
                                    System.out.println("Afterburner is off due to not chasing enemy");
                                
                                } else{
        // Strategi Menghindari Musuh yang Berukuran Lebih Besar
                                    if ((getDistanceBetween(bot, enemy) - (bot.size) - (enemy.size)) <= 0.3*gameState.world.getRadius()){
                                        if (bot.size > 150){

        // Mengaktifkan Shield Jika Ukuran Bot > 150
                                            if (bot.shieldCount == 1){
                                                playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                                playerAction.setAction(PlayerActions.ACTIVATESHIELD);
                                                System.out.println("Enemy's close, activating shield");

                                            } else{
                                                playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                                playerAction.setAction(PlayerActions.FORWARD);
                                                System.out.println("Too close from bigger enemy, running away 1");
                                            }

                                        }

        // Menjauhi Musuh yang Lebih Besar dengan Mendekati Wormhole
                                        else{
                                            if (getDistanceBetween(bot, wormhole) < 0.15*gameState.world.getRadius()){
                                                if (wormhole != null){
                                                    if (wormhole.getSize() > bot.size){
                                                        playerAction.setHeading((getHeadingBetween(wormhole)));
                                                        playerAction.setAction(PlayerActions.FORWARD);
                                                        System.out.println("Avoiding enemy using wormhole");
                                                    
                                                    } else{
                                                        playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                                        playerAction.setAction(PlayerActions.FORWARD);
                                                        System.out.println("Too close from bigger enemy, running away 2");
                                                    }
                                                    
                                                } else{
                                                    playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                                    playerAction.setAction(PlayerActions.FORWARD);
                                                    System.out.println("Too close from bigger enemy, running away 3");
                                                }
                                                
                                            } else{
        // Strategi Menembak Musuh
                                                if ((bot.size + (bot.getTorpedoSalvoCount()*5)) < enemy.size){
                                                    playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                                    playerAction.setAction(PlayerActions.FORWARD);
                                                    System.out.println("Too close from bigger enemy, running away 4");
                                                }
        
                                                else{
                                                    playerAction.setHeading(getHeadingBetween(enemy));
                                                    playerAction.setAction(PlayerActions.FIRETORPEDOES);
                                                    System.out.println("Shooting torpedo salvo to enemy");
                                                }
                                            }
                                        }
        
                                    } else{
        // Strategi Mengonsumsi Food atau Superfood
                                        if ((getDistanceBetween(bot, food) - (bot.size) - (food.size)) >= (1.5)*(getDistanceBetween(bot, superfood) - (bot.size) - (superfood.size))){
                                            playerAction.setHeading(getHeadingBetween(superfood));
                                            playerAction.setAction(PlayerActions.FORWARD);
                                            System.out.println("Found superfood");
                                        } else{
                                            playerAction.setHeading(getHeadingBetween(food));
                                            playerAction.setAction(PlayerActions.FORWARD);
                                            System.out.println("Found regular food");
                                        }
                                    }
                                }                    
                            }
                        }
                    }
                }
                System.out.println("");
                flag += 1;
            } else{
                System.out.println("Waiting");
            }
        }
    }

/* GREEDY IMPLEMENTATION HELPER */
    private double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    private double getDistanceWorldCenter(GameObject object1) {
        var triangleX = Math.abs(object1.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }
    
    private int getHeadingBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
        otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }
    
    private int getHeadingWorldCenter() {
        var direction = toDegrees(Math.atan2(0 - bot.getPosition().y, 0 - bot.getPosition().x));
        return (direction + 360) % 360;
    }
    
    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
    
    private GameObject nearestFood(GameState gameState){
        var food = gameState.getGameObjects().stream()
                    .filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
        return food.get(0);
    }
    
    private GameObject nearestSuperFood(GameState gameState){
        var superFood = gameState.getGameObjects().stream()
                        .filter(item -> item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                        .sorted(Comparator
                                .comparing(item -> getDistanceBetween(bot, item)))
                        .collect(Collectors.toList());
        return superFood.get(0);
    }

    private GameObject nearestEnemy(GameState gameState){
        var enemy = gameState.getPlayerGameObjects().stream()
                    .filter(bot -> bot.id != this.bot.id)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
        return enemy.get(0);
    }
    
    private GameObject nearestWormhole(GameState gameState){
        var wormhole = gameState.getGameObjects().stream()
                        .filter(item -> item.getGameObjectType() == ObjectTypes.WORMHOLE)
                        .min(Comparator
                                .comparing(item -> getDistanceBetween(this.bot, item)))
                        .orElse(null);
        return wormhole;
    }

    private GameObject nearestGascloud(GameState gameState){
        var gascloud = gameState.getGameObjects().stream()
                        .filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD)
                        .min(Comparator
                                .comparing(item -> getDistanceBetween(bot, item)))
                        .orElse(null);
        return gascloud;
    }

    private GameObject nearestAsteroid(GameState gameState){
        var asteroid = gameState.getGameObjects()
                        .stream().filter(item -> item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                        .min(Comparator
                                .comparing(item -> getDistanceBetween(bot, item)))
                        .orElse(null);
        return asteroid;
    }    

    // private void getSupernova(){
    //     var supernova = gameState.getGameObjects()
    //                     .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERNOVA_PICKUP)
    //                     .collect(Collectors.toList());
    //     playerAction.heading = getHeadingBetween(supernova.get(0));}

    // private void avoidSupernovaBomb(GameState gameState){
    //     var supernovaBomb = gameState.getGameObjects()
    //                         .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERNOVA_BOMB)
    //                         .sorted(Comparator
    //                                 .comparing(item -> getDistanceBetween(bot, item)))
    //                         .collect(Collectors.toList());
    //     playerAction.heading = getHeadingBetween(supernovaBomb.get(0));
    // }

    // private void avoidTorpedo(GameState gameState){
    //     var torpedo = gameState.getGameObjects()
    //                     .stream().filter(item -> item.getGameObjectType() == ObjectTypes.TORPEDO_SALVO)
    //                     .sorted(Comparator
    //                             .comparing(item -> getDistanceBetween(bot, item)))
    //                     .collect(Collectors.toList());
    //     playerAction.heading = getHeadingBetween(torpedo.get(0));
    // }
}