package com.example.game1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GamePlatform extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Nhân vật
    private Rectangle player;
    private double playerX = 100, playerY = 500, playerVelocityY = 0;
    private final double gravity = 0.5, jumpPower = -20, moveSpeed = 5;
    private boolean jumping = false, onGround = true, isMovingLeft = false, isMovingRight = false;

    // Quái vật
    private Rectangle monster;
    private double monsterX = 400, monsterY = 500, monsterVelocityY = 0;
    private boolean monsterJumping = false, monsterOnGround = true, monsterMovingRight = true;

    // Đòn tấn công nhân vật
    private Rectangle attackBox;
    private boolean attackActive = false;
    private long attackStartTime = 0;
    private final long attackDuration = 1000000000; // 1 giây

    // Mặt đất và cổng
    private Rectangle ground;
    private Rectangle portal;

    // Vòng lặp trò chơi
    private AnimationTimer gameLoop;
    private int currentMap = 1; // Biến lưu map hiện tại

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.LIGHTBLUE);

        createMap(root);

        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);

        // Tạo AnimationTimer
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame(now);
            }
        };
        gameLoop.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Platform");
        primaryStage.show();
    }
    // Thêm biến cho máu
    private int playerHealth = 100; // Máu người chơi
    private int monsterHealth = 100; // Máu quái vật
    private Rectangle playerHealthBar; // Thanh máu người chơi
    private Rectangle monsterHealthBar; // Thanh máu quái vật
    private final int MAX_HEALTH = 100; // Máu tối đa


    private void createMap(Group root) {
        root.getChildren().clear();

        // Mặt đất - màu xanh lá
        ground = new Rectangle(0, HEIGHT - 50, WIDTH, 50);
        ground.setFill(Color.GREEN);
        root.getChildren().add(ground);

        // Nhân vật - màu xanh dương
        player = new Rectangle(50, 50, Color.BLUE);
        player.setX(playerX);
        player.setY(playerY);
        root.getChildren().add(player);

        // Quái vật - màu đỏ
        monster = new Rectangle(50, 50, Color.RED);
        monster.setX(monsterX);
        monster.setY(monsterY);
        root.getChildren().add(monster);

        // Thanh máu người chơi
        playerHealthBar = new Rectangle(150, 20, Color.GREEN);
        playerHealthBar.setX(10);
        playerHealthBar.setY(10);
        root.getChildren().add(playerHealthBar);

        // Thanh máu quái vật
        monsterHealthBar = new Rectangle(150, 20, Color.RED);
        monsterHealthBar.setX(WIDTH - 170); // Cách mép phải
        monsterHealthBar.setY(10);
        root.getChildren().add(monsterHealthBar);

        // Đòn tấn công người chơi
        attackBox = new Rectangle(60, 20, Color.YELLOW);
        attackBox.setVisible(false);
        root.getChildren().add(attackBox);

        // Cổng qua map
        portal = new Rectangle(WIDTH - 100, HEIGHT - 100, 40, 40);
        portal.setFill(Color.PURPLE);
        root.getChildren().add(portal);
    }


    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE && onGround) {
            jumping = true;
            onGround = false;
            playerVelocityY = jumpPower;
        } else if (event.getCode() == KeyCode.RIGHT) {
            isMovingRight = true;
        } else if (event.getCode() == KeyCode.LEFT) {
            isMovingLeft = true;
        } else if (event.getCode() == KeyCode.ENTER) {
            attackActive = true;
            attackStartTime = System.nanoTime();
            attackBox.setVisible(true);
            attackBox.setX(playerX + 50); // Vị trí tấn công
            attackBox.setY(playerY + 15);
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT) {
            isMovingRight = false;
        } else if (event.getCode() == KeyCode.LEFT) {
            isMovingLeft = false;
        }
    }

    private void updateGame(long now) {
        // Di chuyển nhân vật
        if (isMovingRight) {
            playerX += moveSpeed;
        }
        if (isMovingLeft) {
            playerX -= moveSpeed;
        }
        player.setX(playerX);

        // Cập nhật trạng thái nhảy
        if (jumping) {
            playerVelocityY += gravity;
            playerY += playerVelocityY;
            if (playerY + player.getHeight() >= HEIGHT - 50) {
                playerY = HEIGHT - 50 - player.getHeight();
                jumping = false;
                onGround = true;
                playerVelocityY = 0;
            }
        }
        player.setY(playerY);

        // Di chuyển quái vật
        if (monsterMovingRight) {
            monsterX++;
        } else {
            monsterX--;
        }
        if (monsterX <= 0 || monsterX >= WIDTH - monster.getWidth()) {
            monsterMovingRight = !monsterMovingRight;
        }
        monster.setX(monsterX);

        // Kiểm tra đòn tấn công
        if (attackActive) {
            long elapsedTime = now - attackStartTime;
            if (elapsedTime > attackDuration) {
                attackActive = false;
                attackBox.setVisible(false);
            }
        }

        // Kiểm tra qua cổng
        if (player.getBoundsInParent().intersects(portal.getBoundsInParent())) {
            loadNextMap();
        }
    }

    private void loadNextMap() {
        // Chuyển map mới
        currentMap++;
        if (currentMap > 2) { // Giới hạn số map (tạm ví dụ 2 map)
            currentMap = 1;
        }

        // Reset vị trí nhân vật và quái vật
        playerX = 100;
        playerY = 500;
        monsterX = 400;
        monsterY = 500;

        // Tạo lại map
        createMap((Group) player.getParent());
    }
}
