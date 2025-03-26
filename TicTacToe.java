import java.awt.*;
import javax.swing.*;

public class TicTacToe {
    //dimensions
    static final int BORDER_WIDTH = 600;
    static final int BORDER_HEIGHT = 750;
    
    //players
    final String PLAYER_X = "X";
    final String PLAYER_O = "O";
    String currentPlayer = PLAYER_X;
    
    //scores
    int xWins = 0;
    int oWins = 0;
    int ties = 0;
    
    //current game state
    boolean gameOver = false;
    int turns = 0;
    
    //components
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JButton restartButton = new JButton("Restart Game");
    JButton[][] board = new JButton[3][3];
    
    // Colors
    final Color BG_COLOR = new Color(30, 30, 36);
    final Color BOARD_COLOR = new Color(45, 45, 54);
    final Color X_COLOR = new Color(84, 201, 161); //teal
    final Color O_COLOR = new Color(242, 95, 92);  //coral
    final Color WIN_COLOR = new Color(255, 215, 0); //gold
    final Color TIE_COLOR = new Color(200, 200, 200); //light gray
    final Color BUTTON_COLOR = new Color(65, 65, 75);

    public TicTacToe() {
        initializeFrame();
        setupTextPanel();
        setupBoardPanel();
        setupButtonPanel();
        frame.setVisible(true);
    }

    void initializeFrame() {
        frame.setSize(BORDER_WIDTH, BORDER_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BG_COLOR);
    }

    void setupTextPanel() {
        textLabel.setBackground(BG_COLOR);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.BOLD, 40));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText(currentPlayer + "'s turn");
        
        scoreLabel.setBackground(BG_COLOR);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        updateScoreLabel();
        
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(BG_COLOR);
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(scoreLabel, BorderLayout.SOUTH);
        frame.add(textPanel, BorderLayout.NORTH);
    }

    void setupBoardPanel() {
        boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
        boardPanel.setBackground(BG_COLOR);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        frame.add(boardPanel, BorderLayout.CENTER);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(BOARD_COLOR);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);
                tile.setBorder(BorderFactory.createLineBorder(BG_COLOR, 3));

                tile.addActionListener(e -> {
                    if (gameOver) return;
                    JButton clickedTile = (JButton) e.getSource();
                    if (clickedTile.getText().isEmpty()) {
                        clickedTile.setText(currentPlayer);
                        clickedTile.setForeground(currentPlayer.equals(PLAYER_X) ? X_COLOR : O_COLOR);
                        turns++;
                        checkWinner();
                        if (!gameOver) {
                            currentPlayer = currentPlayer.equals(PLAYER_X) ? PLAYER_O : PLAYER_X;
                            textLabel.setText(currentPlayer + "'s turn");
                        }
                    }
                });
            }
        }
    }

    void setupButtonPanel() {
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 20, 150));
        
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(BUTTON_COLOR);
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusable(false);
        restartButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        restartButton.addActionListener(_ -> resetGame());
        
        buttonPanel.add(restartButton, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    void checkWinner() {
        //horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().isEmpty()) continue;
            
            if (board[r][0].getText().equals(board[r][1].getText()) && 
                board[r][1].getText().equals(board[r][2].getText())) {
                highlightWinningTiles(board[r][0], board[r][1], board[r][2]);
                updateScore(currentPlayer);
                return;
            }
        }

        //vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().isEmpty()) continue;
            
            if (board[0][c].getText().equals(board[1][c].getText()) && 
                board[1][c].getText().equals(board[2][c].getText())) {
                highlightWinningTiles(board[0][c], board[1][c], board[2][c]);
                updateScore(currentPlayer);
                return;
            }
        }

        //diagonal
        if (!board[0][0].getText().isEmpty() && 
            board[0][0].getText().equals(board[1][1].getText()) && 
            board[1][1].getText().equals(board[2][2].getText())) {
            highlightWinningTiles(board[0][0], board[1][1], board[2][2]);
            updateScore(currentPlayer);
            return;
        }

        //anti diagonal
        if (!board[0][2].getText().isEmpty() && 
            board[0][2].getText().equals(board[1][1].getText()) && 
            board[1][1].getText().equals(board[2][0].getText())) {
            highlightWinningTiles(board[0][2], board[1][1], board[2][0]);
            updateScore(currentPlayer);
            return;
        }

        //tie
        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    board[r][c].setBackground(TIE_COLOR);
                }
            }
            textLabel.setText("It's a tie!");
            ties++;
            updateScoreLabel();
            gameOver = true;
        }
    }

    void highlightWinningTiles(JButton... tiles) {
        for (JButton tile : tiles) {
            tile.setBackground(WIN_COLOR);
        }
        textLabel.setText(currentPlayer + " wins!");
        gameOver = true;
    }

    void updateScore(String winner) {
        if (winner.equals(PLAYER_X)) {
            xWins++;
        } else {
            oWins++;
        }
        updateScoreLabel();
    }

    void updateScoreLabel() {
        scoreLabel.setText(String.format("X: %d  |  O: %d  |  Ties: %d", xWins, oWins, ties));
    }

    void resetGame() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(BOARD_COLOR);
                board[r][c].setEnabled(true);
            }
        }
        
        currentPlayer = PLAYER_X;
        textLabel.setText(currentPlayer + "'s turn");
        gameOver = false;
        turns = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}