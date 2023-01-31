package hello.community.dto.subscribe;


import hello.community.domain.board.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscribeInfoDto {

	private String boardType;
	
	private String boardName;
	
	
	public SubscribeInfoDto(Board board) {
		this.boardType = board.getBoardType();
		this.boardName = board.getBoardName();
	}
}
