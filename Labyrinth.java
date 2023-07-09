package REK;

import java.util.Random;

public class Labyrinth {

    private final char[][] lab;

    private final char wallSymbol;
    private final char pathSymbol;

    private final int labLength;
    private final int labHeight;

    private final int LAB_MIN_SIZE = 10;

    public Labyrinth(final char wallSymbol, final char pathSymbol, final int x, final int y)
    {
        this.wallSymbol = wallSymbol;
        this.pathSymbol = pathSymbol;
        if ( x < LAB_MIN_SIZE || y < LAB_MIN_SIZE)
        {
            labHeight = LAB_MIN_SIZE;
            labLength = LAB_MIN_SIZE;
        }
        else {
            labHeight = y;
            labLength = x;
        }
        lab = new char[labHeight][labLength];
        fillLabWithWallSymbol();
        createPaths();
        findPath(labLength/2, labHeight/2, labLength/2 + 1,labHeight/2);
    }

    private void fillLabWithWallSymbol()
    {
        for (int y = 0; y<labHeight ;y++)
        {
            for (int x = 0; x<labLength ; x++)
            {
                lab[y][x] = wallSymbol;
            }
        }
    }

    private void createPaths()
    {
        createPath(false);
        for(int i = 0; i < (labHeight + labLength) / 2; ++i)
        {
            createPath(true);
        }
    }

    private void createPath(boolean isBaitPath)
    {
        int currentPosX = labLength / 2;
        int currentPosY = labHeight / 2;
        if (isBaitPath) {
            currentPosX = 2 + new Random().nextInt(labLength - 4);
            currentPosY = 2 + new Random().nextInt(labHeight - 4);
        }
        int targetPosX = currentPosX;
        int targetPosY = currentPosY;
        int blockedDirection = getRandomDirection(-1);
        int bait = isBaitPath ? 1 : 0;
        int depth = 0;

        lab[currentPosY][currentPosX] = ' ';

        while( currentPosX != bait && currentPosY != bait && currentPosX != labLength-1-bait && currentPosY != labHeight-1-bait)
        {
            int moveDir = getRandomDirection(blockedDirection);
            if (moveDir == 0) {
                targetPosY--;
            } else if (moveDir == 1) {
                targetPosX++;
            } else if (moveDir == 2) {
                targetPosY++;
            } else if (moveDir == 3) {
                targetPosX--;
            }
            if (isValidMove(targetPosX, targetPosY))
            {
                currentPosX = targetPosX;
                currentPosY = targetPosY;
                lab[currentPosY][currentPosX] = ' ';
            }
            else {
                targetPosX = currentPosX;
                targetPosY = currentPosY;
                depth++;
                if (depth > 9 && isBaitPath)
                {
                    break;
                }
            }
        }
    }

    private boolean isValidMove(int posX, int posY)
    {
        int err = 0;
        try {
            if (posX > labLength || posX < 0 || lab[posY][posX-1] == ' ') {
                err++;
            }
            if (posX > labLength || posX < 0 || lab[posY][posX+1] == ' ') {
                err++;
            }
            if (posY > labHeight || posY < 0 || lab[posY+1][posX] == ' ') {
                err++;
            }
            if (posY > labHeight || posY < 0 || lab[posY-1][posX] == ' ') {
                err++;
            }
            return err < 2;
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return true;
        }
    }

    private int getRandomDirection(int blockedDirection)
    {
        int rnd = new Random().nextInt(4);
        if (rnd == blockedDirection)
            rnd = getRandomDirection(blockedDirection);
        return rnd;
    }

    public void findPath(int curPosX, int curPosY, int targetPosX, int targetPosY)
    {
        if(curPosX == 0 || curPosY == 0 || curPosX == labLength-1 || curPosY == labHeight-1) {
            return;
        }
        lab[curPosY][curPosX] = pathSymbol;

        if (lab[targetPosY][targetPosX] == wallSymbol)
        {
            int[] res = turn(curPosX, curPosY, targetPosX, targetPosY);
            if (res == null) {
                return;
            }
            targetPosX = res[1];
            targetPosY = res[0];

            int tmpX = curPosX;
            int tmpY = curPosY;
            curPosX = targetPosX;
            curPosY = targetPosY;
            if(targetPosX > tmpX || targetPosY > tmpY)
            {
                targetPosX = targetPosX + (tmpX - targetPosX) * (-1);
                targetPosY = targetPosY + (tmpY - targetPosY) * (-1);
            }else
            {
                targetPosX = targetPosX + (targetPosX - tmpX) * (1);
                targetPosY = targetPosY + (targetPosY - tmpY) * (1);
            }


        }
        else
        {


            int tmpX = curPosX;
            int tmpY = curPosY;
            curPosX = targetPosX;
            curPosY = targetPosY;
            if (targetPosX > tmpX || targetPosY > tmpY)
            {
                targetPosX = targetPosX + (tmpX - targetPosX) * (-1);
                targetPosY = targetPosY + (tmpY - targetPosY) * (-1);
            } else {
                targetPosX = targetPosX + (targetPosX - tmpX) * (1);
                targetPosY = targetPosY + (targetPosY - tmpY) * (1);
            }


        }
        findPath(curPosX, curPosY, targetPosX, targetPosY);
    }

    public int[] turn(int curPosX, int curPosY, int targetPosX, int targetPosY)
    {
        int[] res;

        res = moveRight(curPosX, curPosY, targetPosX, targetPosY);
        if (res != null) {
            return res;
        }

        res = moveLeft(curPosX, curPosY, targetPosX, targetPosY);
        if (res != null) {
            return res;
        }

        res = moveBack(curPosX, curPosY, targetPosX, targetPosY);
        if (res != null) {
            return res;
        }

        return null;
    }

    public int[] moveRight(int curPosX, int curPosY, int targetPosX, int targetPosY)
    {


            int[] point = {
                    curPosY,
                    curPosX
            };

            int tmpX = targetPosX - curPosX;
            int tmpY = targetPosY - curPosY;

            if (tmpX != 0) {
                point[0] = curPosY + tmpX;
            } else if (tmpY != 0) {
                point[1] = curPosX + tmpY * (-1);
            }



        return (lab[point[0]][point[1]] != wallSymbol) ? point : null;
    }

    public int[] moveLeft(int curPosX, int curPosY, int targetPosX, int targetPosY)
    {
        int[] point = {
                curPosY,
                curPosX
        };

        int tmpX = targetPosX - curPosX;
        int tmpY = targetPosY - curPosY;

        if(tmpX != 0) {
            point[0] = curPosY - tmpX;
        } else if(tmpY != 0) {
            point[1] = curPosX - tmpY * (-1);
        }

        return (lab[point[0]][point[1]] != wallSymbol) ? point : null;
    }

    public int[] moveBack(int curPosX, int curPosY, int targetPosX, int targetPosY)
    {
        int tmpX = targetPosX - curPosX;
        int tmpY = targetPosY - curPosY;

        return new int[]{
            curPosY + tmpY * (-1),
            curPosX + tmpX * (-1)
        };
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < labHeight ;y++)
        {
            for (int x = 0; x < labLength ; x++)
            {
                sb.append(lab[y][x]);
                if(x == labLength -1)
                {
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

}
