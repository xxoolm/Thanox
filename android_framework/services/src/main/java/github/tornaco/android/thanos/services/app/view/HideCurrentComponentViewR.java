package github.tornaco.android.thanos.services.app.view;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import lombok.Setter;

public class HideCurrentComponentViewR extends AbstractSafeR {

    @Setter
    private CurrentComponentView view;

    @Override
    public void runSafety() {
        if (view != null) {
            view.setText(null);
            view.hideAndDetach();
        }
    }
}
