package liar.game.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDefaultPageable implements BasePageable {

    Integer page;
    Integer size;

    public void updatePageable() {
        if (this.page == null) this.page = 0;
        if (this.size == null) this.size = 10;
    }

}
