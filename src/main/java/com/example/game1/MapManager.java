package com.example.game1;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class MapManager {

    // Lưu thông tin các map
    private final List<MapData> maps = new ArrayList<>();
    private int currentMapIndex = 0;

    // Lớp lưu dữ liệu từng map
    static class MapData {
        List<Rectangle> grounds; // Các mặt đất
        double playerStartX, playerStartY; // Vị trí ban đầu của người chơi
        double portalX, portalY; // Vị trí của cổng

        public MapData(List<Rectangle> grounds, double playerStartX, double playerStartY, double portalX, double portalY) {
            this.grounds = grounds;
            this.playerStartX = playerStartX;
            this.playerStartY = playerStartY;
            this.portalX = portalX;
            this.portalY = portalY;
        }
    }

    // Tạo dữ liệu map ban đầu
    public MapManager() {
        createMaps();
    }

    // Khởi tạo các map
    private void createMaps() {
        // Map 1
        List<Rectangle> map1Grounds = List.of(
                new Rectangle(0, 550, 400, 50), // Mặt đất đầu tiên
                new Rectangle(500, 450, 300, 50) // Mặt đất cao hơn
        );
        maps.add(new MapData(map1Grounds, 50, 500, 700, 400));

        // Map 2
        List<Rectangle> map2Grounds = List.of(
                new Rectangle(0, 550, 200, 50),
                new Rectangle(250, 450, 200, 50),
                new Rectangle(500, 350, 200, 50)
        );
        maps.add(new MapData(map2Grounds, 100, 500, 600, 300));
    }

    // Tạo map dựa trên thông tin từ danh sách
    public void loadMap(Group root, Rectangle player, Rectangle portal) {
        root.getChildren().clear();

        // Lấy map hiện tại
        MapData currentMap = maps.get(currentMapIndex);

        // Tạo các mặt đất
        for (Rectangle ground : currentMap.grounds) {
            ground.setFill(Color.GREEN);
            root.getChildren().add(ground);
        }

        // Đặt nhân vật vào vị trí ban đầu
        player.setX(currentMap.playerStartX);
        player.setY(currentMap.playerStartY);
        player.setFill(Color.BLUE);
        root.getChildren().add(player);

        // Đặt cổng vào vị trí tương ứng
        portal.setX(currentMap.portalX);
        portal.setY(currentMap.portalY);
        portal.setWidth(40);
        portal.setHeight(40);
        portal.setFill(Color.PURPLE);
        root.getChildren().add(portal);
    }

    // Chuyển sang map tiếp theo
    public void nextMap(Group root, Rectangle player, Rectangle portal) {
        currentMapIndex++;
        if (currentMapIndex >= maps.size()) {
            currentMapIndex = 0; // Quay lại map đầu tiên
        }
        loadMap(root, player, portal);
    }
}
