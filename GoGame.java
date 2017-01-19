package com.graph;

/**
 * Created by jingge2 on 1/15/17.
 */

import java.io.*;
import java.util.*;

enum Stone {
    black, white, empty;
}

enum GameStatus{
    SUCCESS, FAILED_GAME, INVALID_GAME;
}

public class GoGame {
    public int num_row;
    public int num_col;
    public Stone[][] board;


    public GoGame() {
    }

    public void initBoard(int num_row, int num_col) {
        this.num_row = num_row;
        this.num_col = num_col;
        board = new Stone[this.num_row][this.num_col];
        for (int i = 0; i < this.num_row; i++) {
            for (int j = 0; j < this.num_col; j++) {
                board[i][j] = Stone.empty;
            }
        }
    }


    public boolean placeChess(int row, int col, Stone player_stone) {
        if (player_stone == board[row][col]) {
            return false;
        } else {
            if (board[row][col] == Stone.empty) {
                board[row][col] = player_stone;
                return true;
            }
            return false;
        }
    }

    public char get(char[][] board, int row, int col) {
        return board[row][col];
    }


    public List<int[]> readInputFiles(String file_path, String delimiter) {
        BufferedReader br = null;
        String line = "";
        List<int[]> cleaned_input = new ArrayList<int[]>();
        try {
            br = new BufferedReader(new FileReader(file_path));
            while ((line = br.readLine()) != null) {
                String[] curline = line.split(delimiter);

                cleaned_input.add(new int[]{Integer.parseInt(curline[1]), Integer.parseInt(curline[0])});

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cleaned_input;
    }

    public String getFileBaseName(String file_path){
        int separater_idx = file_path.lastIndexOf('.');
        return file_path.substring(0, separater_idx);
    }

    public void writeGameResult(String output_file_path, List<int[]> history_of_game, Stone[][] board){
        List<String> converted_game_history = new ArrayList<String>();
        /*
        for(int[] item : history_of_game){
            converted_game_history.add(Integer.toString(item[0]) + "," + item[1]);
        }
        */
        for(int i = 0; i< board.length ; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] != Stone.empty){
                    converted_game_history.add(Integer.toString(i) + "<>" + Integer.toString(j));
                }
            }
        }
        System.out.println(Arrays.toString(converted_game_history.toArray()));
    }


    public void floodfill(Stone turn, int row, int col, Stone[][] board) {
        if(row < 0 || col < 0 || row > board.length || col > board[0].length){
            return;
        }
        System.out.println(row + "," + col + ", status = " + board[row][col]);
        if (turn == Stone.white) {
            if (board[row][col] == Stone.black){
                board[row][col] = Stone.empty;
            }
        } else if(turn == Stone.black) {
            if (board[row][col] == Stone.white){
                board[row][col] = Stone.empty;
            }
        }

         // does black = 0? Saying black would be more readable
        floodfill(turn, row, col - 1, board);
        floodfill(turn, row, col + 1, board);
        floodfill(turn, row - 1, col, board);
        floodfill(turn, row + 1, col, board);

    }

    public static void main(String[] args) {
        GoGame obj = new GoGame();
        List<int[]> input = obj.readInputFiles("/Users/jingge2/Downloads/GoGame/games/SimpleCapture.csv", ",");
        for(int[] item : input){
            System.out.println(Arrays.toString(item));
        }

        obj.initBoard(input.get(0)[0], input.get(0)[1]);

        for(int i = 1; i < input.size(); i++){
            if(i % 2 != 0){
                boolean black_player = obj.placeChess(input.get(i)[0], input.get(i)[1], Stone.black);
                if(!black_player){
                    System.out.println("Detected wrong moves at player : " + Stone.black);
                    System.exit(0);
                }else{
                    obj.floodfill(Stone.black, input.get(i)[0], input.get(i)[1], obj.board);
                }
            } else{
                boolean white_player = obj.placeChess(input.get(i)[0], input.get(i)[1], Stone.white);
                if(!white_player){
                    System.out.println("Detected wrong moves at player : " + Stone.white);
                    System.exit(0);
                }else{
                    obj.floodfill(Stone.white, input.get(i)[0], input.get(i)[1], obj.board);

                }
            }
        }
        obj.writeGameResult("tmp", input, obj.board);





    }

}
