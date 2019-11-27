package github.tornaco.android.thanos.services.profile;

import com.google.common.io.Files;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import github.tornaco.android.thanos.core.util.Timber;

class LocalRuleScanner {

    @SuppressWarnings("UnstableApiUsage")
    Map<String, Rule> getRulesUnder(File dir) {
        Map<String, Rule> res = new HashMap<>();
        if (!dir.exists()) return res;
        if (!dir.isDirectory()) return res;
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        Iterable<File> subFiles = Files.fileTreeTraverser().postOrderTraversal(dir);
        for (File f : subFiles) {
            if (f.isDirectory()) continue;
            Timber.v("Parse file to rule: %s", f);
            try {
                Rule rule = ruleFactory.createRule(new FileReader(f));
                Timber.v("Found rule: %s", rule);
                res.put(Files.getNameWithoutExtension(f.getAbsolutePath()), rule);
            } catch (Exception e) {
                Timber.e(e, "Error parse file to rule: " + f);
            }
        }
        return res;
    }
}
