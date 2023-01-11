package hello.community.dto.board;


import hello.community.domain.board.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardInfoDto {

	private String boardType;
	
	private String boardName;
	
	private boolean isSubcribe;
	
	public BoardInfoDto(Board board){
		this.boardType = board.getBoardType();
		this.boardName = board.getBoardName();
		this.isSubcribe = false;
	}
}
