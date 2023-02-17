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
        if (!gameState.getGameObjects().isEmpty()){ // Permainan dimulai
            // while (gameState.world.getCurrentTick() == 0){
            //     // System.out.println("Waiting"); // menunggu tick pertama
            // }
            
            if (gameState.world.getCurrentTick() == flag){ // Kondisional if ini akan dijalankan satu kali setiap tick
            System.out.println("Tick: " + gameState.world.getCurrentTick());
            System.out.print("Action: ");

        // 1) Strategi Menjauhi Batas Arena jika bot terlalu ke pinggir
                if ((getDistanceWorldCenter(bot) + (2*bot.size)) > gameState.world.radius){ // Fungsi seleksi dan kelayakan menjauhi batas arena
                    playerAction.setHeading(getHeadingWorldCenter());
                    playerAction.setAction(PlayerActions.FORWARD);
                    System.out.println("Near the edge, going to center");

                } else{
                    var food = nearestFood(gameState);
                    var superfood = nearestSuperFood(gameState);
                    // var wormhole = nearestWormhole(gameState);
                    // var asteroid = nearestAsteroid(gameState);
                    
        // 2) Strategi Menjauhi Gas Cloud
                    var gascloud = nearestGascloud(gameState); // Fungsi seleksi dan kelayakan dalam mengumpulkan himpunan gas cloud
                    if (gascloud != null && (getDistanceBetween(bot, gascloud) - (bot.size) - (gascloud.size)) < 75){
                        playerAction.setHeading((getHeadingBetween(gascloud) + 180) % 360);
                        playerAction.setAction(PlayerActions.FORWARD);
                        System.out.println("Avoiding gas cloud");
                        
                    } else{
        // 3) Strategi Mendekati Musuh yang Berukuran Lebih Kecil 
                        var enemy = nearestEnemy(gameState); // Fungsi seleksi dan kelayakan mencari musuh dengan jarak terdekat
                        if ((bot.size > enemy.size)){ // ukuran kita lebih gede
                            if ((getDistanceBetween(bot, enemy) - (bot.size) - (enemy.size) <= 300)){ // ukuran kita gede jaraknya dekat
                                if (bot.size > 75){ // ukuran kita gede, jaraknya dekat, cukup buat afterburner
                                    if (!isAfterburnerOn()){ // ukuran gede, jarak deket, cukup afterburner, tapi afterburner mati -> nyalain afterburner
                                        playerAction.setHeading(getHeadingBetween(enemy));
                                        playerAction.setAction(PlayerActions.STARTAFTERBURNER);
                                        this.afterburnerOn = true;
                                        System.out.println("Afterburner is on due to chasing faster enemy");
                                        
                                    } else{ // ukuran gede, jarak dekat, cukup afterburner dan udah start afterburner -> kejar musuh
                                        playerAction.setHeading(getHeadingBetween(enemy));
                                        playerAction.setAction(PlayerActions.FORWARD);
                                        System.out.println("Chasing smaller enemy");
                                    }
                                } else{ // ukuran lebih gede, jarak deket, ga cukup afterburner
                                    if (!isAfterburnerOn()){ // ukuran lebih gede, jarak deket, ga cukup afterburner, dan afterburner mati -> kejar musuh
                                        playerAction.setHeading(getHeadingBetween(enemy));
                                        playerAction.setAction(PlayerActions.FORWARD);
                                        System.out.println("Chasing smaller enemy");
                                   
                                    } else{ // ukuran lebih gede, jarak deket, ga cukup afterburner, dan afterburner nyala -> matiin afterburnernya
                                        playerAction.setHeading(getHeadingBetween(enemy));
                                        playerAction.setAction(PlayerActions.STOPAFTERBURNER);
                                        this.afterburnerOn = false;
                                        System.out.println("Afterburner is off due to small ship size");
                                    }
                                }
                                
                            } else{ // ukuran gede, jaraknya jauh
                                if (isAfterburnerOn()){ // ukuran gede, jaraknya jauh, afterburner nyala -> matiin afterburner
                                    playerAction.setAction(PlayerActions.STOPAFTERBURNER);
                                    this.afterburnerOn = false;
                                    System.out.println("Enemy's too far, afterburner is off");
                                    
                                } else{ // ukuran gede, jarak jauh, afterburner mati -> ???
                                    playerAction.setHeading(getHeadingBetween(enemy));
                                    playerAction.setAction(PlayerActions.FORWARD);
                                    System.out.println("Getting closer to smaller enemy");
                                }
                            }
                            
                        } else{
                            if (isAfterburnerOn()){
                                playerAction.setAction(PlayerActions.STOPAFTERBURNER);
                                this.afterburnerOn = false;
                                System.out.println("Afterburner is off due to not chasing enemy");
                            } else{
        // 4) Strategi Menghindari Musuh yang Berukuran Lebih Besar
                                if ((getDistanceBetween(bot, enemy) - (bot.size) - (enemy.size)) <= 150){
                                    if (bot.size > 25){
                                        playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                        playerAction.setAction(PlayerActions.ACTIVATESHIELD);
                                    }
                                }
                                else if ((getDistanceBetween(bot, enemy) - (bot.size) - (enemy.size)) <= 300){
                                    if ((1.5)*bot.size < enemy.size){
                                        playerAction.setHeading((getHeadingBetween(enemy) + 180) % 360);
                                        playerAction.setAction(PlayerActions.FORWARD);
                                        System.out.println("Too close from bigger enemy, running away");
                                    } 
                                    
                                    else{
                                        playerAction.setHeading(getHeadingBetween(enemy));
                                        playerAction.setAction(PlayerActions.FIRETORPEDOES);
                                        System.out.println("Shooting Enemy");
                                    }

                                } else{
                                    if ((getDistanceBetween(bot, food) - (bot.size) - (food.size)) >= (1.5)*(getDistanceBetween(bot, superfood) - (bot.size) - (superfood.size))){
                                        playerAction.setHeading(getHeadingBetween(superfood));
                                        playerAction.setAction(PlayerActions.FORWARD);
                                        System.out.println("Found superfood");

                                    } else{
                                        playerAction.setHeading(getHeadingBetween(food));
                                        playerAction.setAction(PlayerActions.FORWARD);
                                        System.out.println("Finding regular food");
                                    }
                                }
                            }
                        }
                    }
                }
            System.out.println("");
            flag += 1;
            }
        }
    }

/* GREEDY IMPLEMENTATION */
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

    // private void shootTorpedo(GameObject enemy){
    //     playerAction.heading = getHeadingBetween(enemy);
    //     playerAction.action = PlayerActions.FIRETORPEDOES;
    // }

    // private void shootSupernova(){

    // }
    
    private GameObject nearestFood(GameState gameState){
        var food = gameState.getGameObjects().stream()
                    .filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                    .filter(bot -> getDistanceBetween(bot, this.bot) < gameState.world.radius)
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
                    .filter(bot -> bot.id != this.bot.id).sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
        return enemy.get(0);
    }
    
    // private GameObject nearestWormhole(GameState gameState){
    //     var wormhole = gameState.getGameObjects().stream()
    //                     .filter(item -> item.getGameObjectType() == ObjectTypes.WORMHOLE)
    //                     .sorted(Comparator
    //                             .comparing(item -> getDistanceBetween(bot, item)))
    //                     .collect(Collectors.toList());
    //                     // .orElse(null);
    //     return wormhole.get(0);
    // }

    private GameObject nearestGascloud(GameState gameState){
        var gascloud = gameState.getGameObjects().stream()
                        .filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD)
                        .min(Comparator
                                .comparing(item -> getDistanceBetween(bot, item)))
                        .orElse(null);
        return gascloud;
    }

    // private GameObject nearestAsteroid(GameState gameState){
    //     var asteroid = gameState.getGameObjects()
    //                     .stream().filter(item -> item.getGameObjectType() == ObjectTypes.ASTEROID_FIELD)
    //                     .sorted(Comparator
    //                             .comparing(item -> getDistanceBetween(bot, item)))
    //                     .collect(Collectors.toList());
    //     return asteroid.get(0);
    // }    

    // private void getSupernova(){
    //     var supernova = gameState.getGameObjects()
    //                     .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERNOVA_PICKUP)
    //                     .collect(Collectors.toList());
    //     playerAction.heading = getHeadingBetween(supernova.get(0));
    // }

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

    // private int goToOpposite(GameObject object1){
    //     return ((getHeadingBetween(object1) + 180) % 360);
    // }
}
