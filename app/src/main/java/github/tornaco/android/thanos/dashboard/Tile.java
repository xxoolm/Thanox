package github.tornaco.android.thanos.dashboard;

import android.view.View;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import lombok.*;

@Builder
@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode(exclude = {"onClickListener", "summary"})
public class Tile {
    private String title;
    private String summary;
    private String category;
    @DrawableRes
    private int iconRes;
    private View.OnClickListener onClickListener;
    @ColorRes
    private int themeColor;
    private boolean atEndOfThisCategory;
}
