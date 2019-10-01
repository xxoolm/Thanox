package github.tornaco.thanos.android.ops.ops.repo;

import android.content.Context;
import com.google.common.collect.Sets;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.model.Ops;
import github.tornaco.thanos.android.ops.model.OpsTemplate;

import java.util.*;

public class PkgOpsLoader {

    public List<OpGroup> getPkgOps(Context context, AppInfo appInfo) {
        ThanosManager thanos = ThanosManager.from(context);
        Map<OpsTemplate, OpGroup> opsCategoryOpGroupMap = new HashMap<>();
        Set<String> permissions = Sets.newHashSet(PkgUtils.getAllDeclaredPermissions(context, appInfo.getPkgName()));
        int numOp = AppOpsManager._NUM_OP;
        if (thanos.isServiceInstalled()) {
            AppOpsManager ops = thanos.getAppOpsManager();
            for (int op = 0; op < numOp; op++) {
                String perm = AppOpsManager.opToPermission(op);
                boolean hold = permissions.contains(perm);
                if (hold) {
                    OpsTemplate template = Ops.templateOfOp(op);
                    if (template != null) {
                        OpGroup group = opsCategoryOpGroupMap.get(template);
                        if (group == null) group = new OpGroup(template, new ArrayList<>());
                        int mode = ops.checkOperation(op, appInfo.getUid(), appInfo.getPkgName());
                        if (mode != AppOpsManager.MODE_ERRORED) {
                            Op opm = Ops.toOp(context, op, mode);
                            if (opm != null) {
                                group.getOpList().add(opm);
                            }
                        }
                        opsCategoryOpGroupMap.put(template, group);
                    }
                }
            }
        }

        List<OpGroup> res = new ArrayList<>();
        for (OpGroup group : opsCategoryOpGroupMap.values()) {
            if (!group.isEmpty()) {
                res.add(group);
            }
        }
        Collections.sort(res);
        return res;
    }
}
