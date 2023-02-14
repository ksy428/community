package hello.community.dto.board;


import hello.community.domain.board.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BoardInfoDto implements Serializable {

	private String boardType;
	
	private String boardName;
	
	private boolean isSubscribe;
	
	public BoardInfoDto(Board board){
		this.boardType = board.getBoardType();
		this.boardName = board.getBoardName();
		this.isSubscribe = false;
	}
}
