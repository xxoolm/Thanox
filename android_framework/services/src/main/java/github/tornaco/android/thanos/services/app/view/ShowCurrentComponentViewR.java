package github.tornaco.android.thanos.services.app.view;

import android.content.ComponentName;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import lombok.Setter;

public class ShowCurrentComponentViewR extends AbstractSafeR {

    @Setter
    private ComponentName name;
    @Setter
    private CurrentComponentView view;

    @Override
    public void runSafety() {
        if (name != null) {
            view.attach();
            view.show();
            view.setText(name.flattenToString());
        }
    }
}
