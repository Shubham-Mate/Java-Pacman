public class Collision {
    static public int[] getPlayerTile (Player p, int TILE_HEIGHT, int TILE_WIDTH) {
        int x_ind = p.getPosition()[0] / TILE_WIDTH;
        int y_ind = p.getPosition()[1] / TILE_HEIGHT;
        return new int[]{x_ind, y_ind};
    }

    static public int[] getEnemyTile (Enemy e, int TILE_HEIGHT, int TILE_WIDTH) {
      int x_ind = e.getPosition()[0] / TILE_WIDTH;
      int y_ind = e.getPosition()[1] / TILE_HEIGHT;
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

    static public int enemyTileCollision(Enemy e, int x, int y, int TILE_WIDTH, int TILE_HEIGHT) {
      int tPosX = y*TILE_WIDTH;
      int tPosY = x*TILE_HEIGHT;
      int pPosX = e.getPosition()[0];
      int pPosY = e.getPosition()[1];
      int pSpeedX = e.getSpeed()[0];
      int pSpeedY = e.getSpeed()[1];

      if (
          pPosX + pSpeedX * e.speedMultiplier < tPosX + TILE_WIDTH &&
          pPosX + pSpeedX * e.speedMultiplier + e.getSize()[0] > tPosX &&
          pPosY + pSpeedY * e.speedMultiplier < tPosY + TILE_HEIGHT &&
          pPosY + pSpeedY * e.speedMultiplier + e.getSize()[1] > tPosY
        ) {
          return 1;
        } else {
          return 0;
        }
    }

    static public float distance (int x1, int y1, int x2, int y2) {
      return (float)Math.sqrt(Math.pow((x1-x2), 2) + Math.pow(y1-y2, 2));
    }

    static public int playerEnemyCollision(Player p, Enemy e) {
      int ePosX = e.getPosition()[0];
      int ePosY = e.getPosition()[1];
      int pPosX = p.getPosition()[0];
      int pPosY = p.getPosition()[1];
      if (
          pPosX < ePosX + e.getSize()[0] &&
          pPosX + p.getSize()[0] > ePosX &&
          pPosY < ePosY + e.getSize()[1] &&
          pPosY + p.getSize()[1] > ePosY
        ) {
          return 1;
        } else {
          return 0;
        }
    }
}
