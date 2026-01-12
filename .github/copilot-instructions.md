# Copilot Instructions for PE7EscacsPolH

## Project Overview
This is a Java implementation of a chess game (Escacs in Catalan). The main class is `PE7EscacsPolH` in `PE7EscacsPolH.java`.

## Architecture
- **Board Representation**: Use an 8x8 2D array (e.g., `Piece[][] board`) where each cell holds a piece or null.
- **Pieces**: Implement pieces as an enum or classes with movement logic. Common pieces: King, Queen, Rook, Bishop, Knight, Pawn.
- **Game State**: Track current player (white/black), move history, check/checkmate status.
- **Input/Output**: Use console for moves (e.g., algebraic notation like "e2-e4") and display board as text.

## Key Patterns
- **Piece Movement**: Each piece class should have a `getValidMoves()` method returning possible positions.
- **Validation**: Check moves for legality, including check avoidance and castling/en passant rules.
- **Turn Management**: Alternate between players, validate moves before applying.

## Development Workflow
- **Compile**: `javac PE7EscacsPolH.java`
- **Run**: `java PE7EscacsPolH`
- **Debug**: Use print statements for board state; consider adding a `printBoard()` method.

## Conventions
- Use camelCase for variables/methods.
- Piece positions: Use 0-7 indices for rows/columns (0,0 is a1 in chess notation).
- Handle special moves (castling, promotion) explicitly.

## Examples
- Initialize board: `Piece[][] board = new Piece[8][8];`
- Move validation: `if (piece.canMove(from, to, board)) { applyMove(from, to); }`

## Integration Points
- No external dependencies; pure Java implementation.
- Potential future: GUI with Swing, but currently console-based.