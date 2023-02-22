package hello.community.dto.valid;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ValidErrorDto {

    private List<String> errorMessage = new ArrayList<>();

    public ValidErrorDto(List<ObjectError> errors){
        this.errorMessage = errors.stream().map(objectError -> new String(objectError.getDefaultMessage())).collect(Collectors.toList());
    }
}
