public class Collision {
    static public int[] getPlayerTile (Player p, int TILE_HEIGHT, int TILE_WIDTH) {
        int x_ind = p.getPosition()[0] / TILE_WIDTH;
        int y_ind = p.getPosition()[1] / TILE_HEIGHT;
        return new int[]{x_ind, y_ind};
    }

    static public int playerCoinCollision (Player p, int x, int y, int TILE_WIDTH, int TILE_HEIGHT) {
        int cPosX = y*TILE_WIDTH + 8;
        int cPosY = x*TILE_HEIGHT + 8;
        int pPosX = p.getPosition()[0];
        int pPosY = p.getPosition()[1];
        if (
            pPosX < cPosX + TILE_WIDTH/2 &&
            pPosX + p.getSize()[0] > cPosX &&
            pPosY < cPosY + TILE_HEIGHT/2 &&
            pPosY + p.getSize()[1] > cPosY
          ) {
            return 1;
          } else {
            return 0;
          }
    }

    static public int playerTileCollision(Player p, int x, int y, int TILE_WIDTH, int TILE_HEIGHT) {
        int tPosX = y*TILE_WIDTH;
        int tPosY = x*TILE_HEIGHT;
        int pPosX = p.getPosition()[0];
        int pPosY = p.getPosition()[1];
        int pSpeedX = p.getSpeed()[0];
        int pSpeedY = p.getSpeed()[1];

        if (
            pPosX + pSpeedX * p.speedMultiplier < tPosX + TILE_WIDTH &&
            pPosX + pSpeedX * p.speedMultiplier + p.getSize()[0] > tPosX &&
            pPosY + pSpeedY * p.speedMultiplier < tPosY + TILE_HEIGHT &&
            pPosY + pSpeedY * p.speedMultiplier + p.getSize()[1] > tPosY
          ) {
            return 1;
          } else {
            return 0;
          }
    }
}
