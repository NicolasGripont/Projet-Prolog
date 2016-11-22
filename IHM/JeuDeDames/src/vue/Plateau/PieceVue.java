package vue.Plateau;

import javafx.scene.shape.Circle;
import modele.Piece;

public class PieceVue extends Circle {

	Piece piece;

	public PieceVue(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

}
